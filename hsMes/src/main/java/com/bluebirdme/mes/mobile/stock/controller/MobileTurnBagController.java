package com.bluebirdme.mes.mobile.stock.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.mobile.common.controller.MobileCommonController;
import com.bluebirdme.mes.mobile.common.controller.MobileController;
import com.bluebirdme.mes.mobile.common.service.IMobileService;
import com.bluebirdme.mes.mobile.stock.service.IMobileTurnBagService;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagOutRecord;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlan;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlanDetails;
import com.bluebirdme.mes.planner.turnbag.service.ITurnBagPlanService;
import com.bluebirdme.mes.platform.entity.ExceptionMessage;
import com.bluebirdme.mes.platform.service.IExceptionMessageService;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import com.bluebirdme.mes.statistics.service.ITotalStatisticsService;
import com.bluebirdme.mes.stock.entity.ProductStockState;
import com.bluebirdme.mes.stock.service.IProductStockService;
import com.bluebirdme.mes.store.entity.*;
import com.bluebirdme.mes.store.service.IBarCodeService;
import com.bluebirdme.mes.utils.StockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 翻包模块
 * 
 * @author Goofy
 * @Date 2017年2月10日 下午4:36:33
 */
@RestController
@RequestMapping("/mobile/turnbag")
public class MobileTurnBagController extends MobileController {
	Logger log = LoggerFactory.getLogger(MobileTurnBagController.class);

	@Resource
	IExceptionMessageService exceptionService;

	@Resource
	ISalesOrderService orderService;

	@Resource
	IMobileService mobileService;
	@Resource
	ITotalStatisticsService totalStatisticsService;
	@Resource
	IProductStockService productStockService;

	@Resource
	IBarCodeService barcodeService;
	@Resource
	ITurnBagPlanService turnBagPlanService;
	@Resource
	IMobileTurnBagService mobileTurnBagService;

	@NoLogin
	@Journal(name = "查询计划列表")
	@RequestMapping("plans")
	public String list() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("auditState", 2);
		param.put("isCompleted", 0);
		List<TurnBagPlan> list = turnBagPlanService.findListByMap(TurnBagPlan.class, param);
		return GsonTools.toJson(list);
	}

	@NoLogin
	@ResponseBody
	@Journal(name = "获取翻包订单明细")
	@RequestMapping(value = "details")
	public String getDetails(String tbCode) throws IOException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("turnBagCode", tbCode);
		ProducePlanDetail ppd = turnBagPlanService.findUniqueByMap(ProducePlanDetail.class, param);
		if (ppd == null)
			return ajaxError("找不到单号");
		if (ppd.getClosed() != null && ppd.getClosed() == 1)
			return ajaxError("该单号已被关闭");
		List<Map<String, Object>> list = turnBagPlanService.getDetails(ppd.getId());
		ProducePlan pp=turnBagPlanService.findById(ProducePlan.class, ppd.getProducePlanId());
		
		param.clear();
		param.put("list", list);
		param.put("workshop", pp.getWorkshop());
		
		return GsonTools.toJson(param);
	}

	@NoLogin
	@Journal(name = "翻包查询托信息")
	@ResponseBody
	@RequestMapping("infos")
	public String info(String barCode) throws IOException {
		Tray tray = barcodeService.findBarCodeReg(BarCodeRegType.TRAY, barCode);
		TrayBarCode tb = barcodeService.findBarcodeInfo(BarCodeType.TRAY, barCode);
		ProductStockState pss = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("barCode", barCode);
		pss = barcodeService.findUniqueByMap(ProductStockState.class, map);

		if (pss != null && pss.getStockState() == StockState.OUT)
			return ajaxError("不在库");

		if (tray == null)
			return ajaxError("该条码尚未使用");
		if (!tray.getRollQualityGradeCode().equals("A"))
			return ajaxError("非A等品");
		if (tb.getIsOpened() != null && tb.getIsOpened() == 1)
			return ajaxError("无效条码");
		if (tray.getState() != 0)
			return ajaxError("不合格产品");
		if (tb.getBatchCode() == null || tb.getBatchCode().isEmpty())
			return ajaxError("无效条码，批次号为空");
		if (tb.getIsOpened() == 1)
			return ajaxError("无效条码");

		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("QR_CODE", tb.getBarcode());
		ret.put("ORDER_ID", tb.getSalesOrderDetailId());
		ret.put("BATCH_CODE", tb.getBatchCode());
		ret.put("ROLLS_IN_TRAY", barcodeService.countRollsInTray(barCode));

		return GsonTools.toJson(ret);
	}
	@NoLogin
	@Journal(name = "裁剪翻包查询部件信息")
	@ResponseBody
	@RequestMapping("cutinfos")
	public String cutInfo(String barCode) throws IOException {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> ret = new HashMap<String, Object>();
		param.put("barcode", barCode);
		PartBarcode part = mobileService.findUniqueByMap(PartBarcode.class, param);
		ret.put("QR_CODE", part.getBarcode());
		ret.put("ORDER_ID", part.getSalesOrderDetailId());
		ret.put("BATCH_CODE", part.getBatchCode());
		ret.put("ROLLS_IN_TRAY", barcodeService.countRollsInTray(barCode));
		return GsonTools.toJson(ret);
	}

	@NoLogin
	@Journal(name = "翻包查询部件信息")
	@ResponseBody
	@RequestMapping("infos3")
	public String info3(String barCode) throws IOException {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> ret = new HashMap<String, Object>();
		param.put("barcode", barCode);
		PartBarcode part = mobileService.findUniqueByMap(PartBarcode.class, param);
		if(part==null){
			return ajaxError("无效条码");
		}
		ProductStockState pss = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("barCode", barCode);
		pss = barcodeService.findUniqueByMap(ProductStockState.class, map);

		if (pss != null && pss.getStockState() == StockState.IN)
			return ajaxError("尚未领出，无法翻包");

		if (part.getIsAbandon() != null && part.getIsAbandon() == 1)
			return ajaxError("无效条码");
		if (part.getBatchCode() == null || part.getBatchCode().isEmpty())
			return ajaxError("无效条码，批次号为空");
		if (part.getIsAbandon() == 1)
			return ajaxError("无效条码");
		ProducePlanDetail ppd = turnBagPlanService.findById(ProducePlanDetail.class, part.getProducePlanDetailId());
			map.clear();
			map.put("producePlanDetailId", ppd.getId());
			List<TurnBagPlanDetails> list = turnBagPlanService.findListByMap(TurnBagPlanDetails.class, map);
			StringBuffer sb = new StringBuffer("#");
			for (TurnBagPlanDetails d : list) {
				sb.append(d.getSalesOrderDetailId() + "." + d.getBatchCode() + "#");
			}
			if (sb.length() != 1) {
				ret.put("FB_CHECK", sb.toString());
				ret.put("FB_CODE", ppd.getTurnBagCode());
			}

		SalesOrderDetail sod = turnBagPlanService.findById(SalesOrderDetail.class, part.getSalesOrderDetailId());
		ret.put("ORDER_CODE", part.getSalesOrderCode());
		ret.put("BATCH_CODE", part.getBatchCode());
		ret.put("ORDER_ID", part.getSalesOrderDetailId());
		ret.put("PRODUCT_FACTORY_NAME", sod.getFactoryProductName());
		ret.put("PRODUCT_MODEL", sod.getProductModel());
		ret.put("QR_CODE", part.getBarcode());
		return GsonTools.toJson(ret);
	}
	@NoLogin
	@Journal(name = "翻包查询托信息")
	@ResponseBody
	@RequestMapping("infos2")
	public String info2(String barCode) throws IOException {
		Tray tray = barcodeService.findBarCodeReg(BarCodeRegType.TRAY, barCode);
		TrayBarCode tb = barcodeService.findBarcodeInfo(BarCodeType.TRAY, barCode);
		if(tb==null){
			return ajaxError("无效条码");
		}
		ProductStockState pss = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("barCode", barCode);
		pss = barcodeService.findUniqueByMap(ProductStockState.class, map);

		if (pss != null && pss.getStockState() == StockState.IN)
			return ajaxError("尚未领出，无法翻包");

		if (tb.getIsOpened() != null && tb.getIsOpened() == 1)
			return ajaxError("无效条码");
		if (tb.getBatchCode() == null || tb.getBatchCode().isEmpty())
			return ajaxError("无效条码，批次号为空");
		if (tb.getIsOpened() == 1)
			return ajaxError("无效条码");

		Map<String, Object> ret = new HashMap<String, Object>();
		ProducePlanDetail ppd = turnBagPlanService.findById(ProducePlanDetail.class, tb.getProducePlanDetailId());
		if (ppd.getIsTurnBagPlan().equals("翻包")) {
			if (tray != null){//如果不为空，说明已打包，如果打包人是-1，那么这种情况是其他翻包页面造成的
				if(tray.getPackagingStaff().longValue()==-1L){
					turnBagPlanService.delete(tray);
				}
			}
			map.clear();
			map.put("producePlanDetailId", ppd.getId());
			List<TurnBagPlanDetails> list = turnBagPlanService.findListByMap(TurnBagPlanDetails.class, map);
			StringBuffer sb = new StringBuffer("#");
			for (TurnBagPlanDetails d : list) {
				sb.append(d.getSalesOrderDetailId() + "." + d.getBatchCode() + "#");
			}
			if (sb.length() != 1) {
				ret.put("FB_CHECK", sb.toString());
				ret.put("FB_CODE", ppd.getTurnBagCode());
			}
		}

		SalesOrderDetail sod = turnBagPlanService.findById(SalesOrderDetail.class, tb.getSalesOrderDetailId());
		ret.put("ORDER_CODE", tb.getSalesOrderCode());
		ret.put("BATCH_CODE", tb.getBatchCode());
		ret.put("ORDER_ID", tb.getSalesOrderDetailId());
		ret.put("PRODUCT_FACTORY_NAME", sod.getFactoryProductName());
		ret.put("PRODUCT_MODEL", sod.getProductModel());
		ret.put("QR_CODE", tb.getBarcode());
		return GsonTools.toJson(ret);
	}

	@NoLogin
	@Journal(name = "保存领出记录",logType = LogType.DB)
	@RequestMapping("/out")
	public String saveOutRecord(@RequestBody List<TurnBagOutRecord> list) throws Exception {
		Date now = new Date();
		for (TurnBagOutRecord r : list) {
			r.setOutDate(now);
		}
		turnBagPlanService.saveOutRecord(list);
		return ajaxSuccess();
	}

	@NoLogin
	@Journal(name = "翻包",logType = LogType.DB)
	@RequestMapping("/doTurnBag")
	public String doTurnBag(String newTrayCode, String oldTrayCode, String optUser) {
		turnBagPlanService.doTurnBag(newTrayCode, oldTrayCode, optUser);
		return ajaxSuccess();
	}
	@NoLogin
	@Journal(name = "裁剪翻包",logType = LogType.DB)
	@RequestMapping("/doCutTurnBag")
	public String doCutTurnBag(String newTrayCode, String oldTrayCode, String optUser) {
		turnBagPlanService.doCutTurnBag(newTrayCode, oldTrayCode, optUser);
		return ajaxSuccess();
	}

	@NoLogin
	@Journal(name = "PDA查询盒信息")
	@RequestMapping("/box/info/{code}")
	public String boxInfo(@PathVariable("code") String boxCode) throws IOException {
		
		// 盒条码
		BoxBarcode bbc =barcodeService.findBarcodeInfo(BarCodeType.BOX, boxCode);
		if(bbc==null)
			return ajaxError("无效条码号");
		
		// 盒信息
		Box box = barcodeService.findBarCodeReg(BarCodeRegType.BOX, boxCode);

		Map<String, Object> ret = new HashMap<String, Object>();
		
		List<Map<String, Object>> brs =turnBagPlanService.getPackChildren(boxCode);

		boolean packedToTray =barcodeService.packed(boxCode);
		
		if(packedToTray)
			return ajaxError("已被打包，无法继续打包");

		if(totalStatisticsService.isFrozen(boxCode)==3)
			return ajaxError("条码已冻结");
		
		if(box!=null&&!box.getRollQualityGradeCode().equals("A"))
			return ajaxError("非A等品");
		if(box!=null&&box.getEndPack().intValue() == 1)
			return ajaxError("打包已结束，无法继续打包");
		if(bbc.getIsOpened()==1)
			return ajaxError("已被拆包，无法打包");
		
		if(box==null){
			if(bbc.getProducePlanDetailId()==null){
				return ajaxError("该盒非翻包打印条码，且该条码尚未使用");
			}
			ProducePlanDetail ppd=turnBagPlanService.findById(ProducePlanDetail.class, bbc.getProducePlanDetailId());
			box=new Box();
			box.setBatchCode(bbc.getBatchCode());
			box.setDeviceCode(null);
			box.setEndPack(0);
			box.setMemo("翻包");
			box.setName(null);
			box.setPackagingStaff(-1L);
			box.setPackagingTime(new Date());
			box.setProducePlanCode(null);
			box.setProductModel(ppd.getProductModel());
			box.setRollQualityGradeCode("A");
			box.setState(0);
			box.setWeight(0D);
			box.setBoxBarcode(boxCode);
			turnBagPlanService.save(box);
		}
		
		ret.put("CHILDREN", brs);
		ret.put("CODE", boxCode);
		ret.put("PLANID", bbc.getPlanId());
		
		return GsonTools.toJson(ret);
	} 
	
	@NoLogin
	@Journal(name = "PDA查询托信息")
	@RequestMapping("/tray/info/{code}")
	public String trayInfo(@PathVariable("code") String trayCode) throws IOException {
		
		// 托条码
		TrayBarCode tbc =barcodeService.findBarcodeInfo(BarCodeType.TRAY, trayCode);
		if(tbc==null)
			return ajaxError("无效条码号");
		
		// 托信息
		Tray tray = barcodeService.findBarCodeReg(BarCodeRegType.TRAY, trayCode);

		Map<String, Object> ret = new HashMap<String, Object>();
		List<Map<String, Object>> brs =turnBagPlanService.getPackChildren(trayCode);

		if(totalStatisticsService.isFrozen(trayCode)==3)
			return ajaxError("条码已冻结");
		if(tray!=null&&!tray.getRollQualityGradeCode().equals("A"))
			return ajaxError("非A等品");
		if(tray!=null&&tray.getEndPack().intValue() == 1)
			return ajaxError("打包已结束，无法继续打包");
		if(tbc.getIsOpened()==1)
			return ajaxError("已被拆包，无法打包");
		
		if(tray==null){
			if(tbc.getProducePlanDetailId()==null){
				return ajaxError("该托非翻包打印条码，且该条码尚未使用");
			}
			ProducePlanDetail ppd=turnBagPlanService.findById(ProducePlanDetail.class, tbc.getProducePlanDetailId());
			tray=new Tray();
			tray.setBatchCode(tbc.getBatchCode());
			tray.setDeviceCode(null);
			tray.setEndPack(0);
			tray.setMemo("翻包");
			tray.setName(null);
			tray.setPackagingStaff(-1L);
			tray.setPackagingTime(new Date());
			tray.setProducePlanCode(null);
			tray.setProductModel(ppd.getProductModel());
			tray.setRollQualityGradeCode("A");
			tray.setState(0);
			tray.setRollCountInTray(0);
			tray.setWeight(0D);
			tray.setTrayBarcode(trayCode);
			turnBagPlanService.save(tray);
		}
		
		ret.put("CHILDREN", brs);
		ret.put("CODE", trayCode);
		ret.put("PLANID", tbc.getPlanId());
		
		return GsonTools.toJson(ret);
	}
	
	@NoLogin
	@Journal(name = "PDA查询托信息")
	@RequestMapping("/roll/info/{code}")
	public String rollInfo(@PathVariable("code") String rollCode) throws IOException {
		
		// 托条码
		RollBarcode rbc =barcodeService.findBarcodeInfo(BarCodeType.ROLL, rollCode);
		if(rbc==null)
			return ajaxError("无效条码号");
		
		// 托信息
		Roll roll = barcodeService.findBarCodeReg(BarCodeRegType.ROLL, rollCode);
		if(roll==null)
			return ajaxError("条码尚未登记");

		Map<String, Object> ret = new HashMap<String, Object>();

		if(totalStatisticsService.isFrozen(rollCode)==3)
			return ajaxError("条码已冻结");
		if(roll!=null&&!roll.getRollQualityGradeCode().equals("A"))
			return ajaxError("非A等品");
		
		
		
		if(barcodeService.packed(rollCode))
			return ajaxError("已被打包，无法继续打包");
		
		ret.put("CODE", rollCode);
		ret.put("PLANID", rbc.getPlanId());
		
		return GsonTools.toJson(ret);
	}

	public void log(String msg) throws Exception {
		log.error(msg);
		ExceptionMessage e = new ExceptionMessage();
		e.setClazz(MobileCommonController.class.getName());
		e.setLineNumber(0);
		e.setMethod("infos");
		e.setOccurDate(new Date());
		e.setMsg(msg);
		exceptionService.save(e);
		throw new Exception(msg);
	}
}
