/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.turnbag.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import com.bluebirdme.mes.store.entity.*;
import org.springframework.stereotype.Service;





import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.turnbag.dao.ITurnBagPlanDao;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagOutRecord;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlan;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlanDetails;
import com.bluebirdme.mes.planner.turnbag.service.ITurnBagPlanService;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.statistics.entity.TotalStatistics;
import com.bluebirdme.mes.stock.entity.ProductStockState;
import com.bluebirdme.mes.store.service.IBarCodeService;
import com.bluebirdme.mes.store.service.impl.BarCodeServiceImpl;
import com.bluebirdme.mes.utils.StockState;

/**
 * 
 * @author 高飞
 * @Date 2017-2-9 11:28:32
 */
@Service
@AnyExceptionRollback
public class TurnBagPlanServiceImpl extends BaseServiceImpl implements ITurnBagPlanService {

	@Resource
	ITurnBagPlanDao turnBagPlanDao;

	@Resource
	IBarCodeService codeService;

	@Override
	protected IBaseDao getBaseDao() {
		return turnBagPlanDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return turnBagPlanDao.findPageInfo(filter, page);
	}

	public <T> Map<String, Object> findOrderPageInfo(Filter filter, Page page) throws Exception {
		return turnBagPlanDao.findOrderPageInfo(filter, page);
	}

	public List<Map<String, Object>> getBatchCodeCountBySalesOrderCode(Long orderId,Long partId) {
		return turnBagPlanDao.getBatchCodeCountBySalesOrderCode(orderId, partId);
	}

	public synchronized String getSerial() {
		return turnBagPlanDao.getSerial();
	}

	public void addOrEdit(List<TurnBagPlanDetails> list) {
		deleteDetails(list.get(0).getProducePlanDetailId());
		saveList(list);
	}

	public void delete(List<TurnBagPlan> list) {
		// delete(list);
		for (TurnBagPlan tbp : list) {
			delete(tbp);
			deleteDetails(tbp.getId());
		}
	}

	public void deleteDetails(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("producePlanDetailId", id);
		delete(TurnBagPlanDetails.class, map);
	}

	public void saveDetails(TurnBagPlan tbp) {
		/*
		 * List<TurnBagPlanDetails> list=tbp.getDetails();
		 * for(TurnBagPlanDetails tbpd:list){
		 * tbpd.setTurnBagPlanId(tbp.getId()); } save(list.toArray(new
		 * TurnBagPlanDetails[list.size()]));
		 */
	}

	public List<Map<String, Object>> getDetails(Long producePlanDetailId) {
		return turnBagPlanDao.getDetails(producePlanDetailId);
	}

	public void saveOutRecord(List<TurnBagOutRecord> list) throws Exception {

		saveList(list);

		TurnBagPlanDetails detail = null;
		ProductStockState pss = null;

		Map<String, Object> map = new HashMap<String, Object>();

		for (TurnBagOutRecord r : list) {
			detail = findById(TurnBagPlanDetails.class, r.getTurnBagPlanDetailId());
			if (detail.getTakeOutCount() >= detail.getTakeOutCountFromWareHouse()) {
				throw new Exception("翻包数量超出");
			}
			detail.setTakeOutCount(detail.getTakeOutCount() + 1);
			update(detail);

			map.clear();
			map.put("barCode", r.getTrayCode());
			pss = findUniqueByMap(ProductStockState.class, map);
			if(pss==null){
				throw new Exception(r.getTrayCode()+"尚未入库");
			}
			if(pss.getStockState()==StockState.OUT)
				throw new Exception(pss.getBarCode()+"已经领出，无法再领");
			map.clear();
			map.put("rollBarcode", r.getTrayCode());
			TotalStatistics ts=findUniqueByMap(TotalStatistics.class, map);
			ts.setState(0);
			pss.setStockState(StockState.OUT);
			update(pss);
		}
	}
	public void doCutTurnBag(String newTrayCode, String oldTrayCode, String optUser) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		TotalStatistics tot=new TotalStatistics();
		PartBarcode newTbc = codeService.findBarcodeInfo(BarCodeType.PART, newTrayCode);
		PartBarcode oldTbc = codeService.findBarcodeInfo(BarCodeType.PART, oldTrayCode);
		ProducePlanDetail planDetail=codeService.findById(ProducePlanDetail.class, newTbc.getProducePlanDetailId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rollBarcode", oldTrayCode);
		List<TotalStatistics> oldTots=codeService.findListByMap(TotalStatistics.class, map);
		TotalStatistics oldTot = oldTots.get(0);

		SalesOrderDetail sod=findById(SalesOrderDetail.class, newTbc.getSalesOrderDetailId());

		newTbc.setTurnBagDate(sdf.format(now));
		newTbc.setTurnBagUser(optUser);
		newTbc.setOldBatchCode(oldTbc.getBatchCode());
		newTbc.setOldSalesOrder(oldTbc.getSalesOrderCode());
		newTbc.setOldbarcode(oldTbc.getBarcode());
		oldTbc.setIsOpened(1);
		update(oldTbc);
		update(newTbc);
		Map<String, Object> param = new HashMap<String, Object>();
		param.clear();
		param.put("partBarcode", oldTrayCode);
		Roll roll = codeService.findUniqueByMap(Roll.class, param);
		roll.setPartBarcode(newTrayCode);
		update(roll);
		//加到生产统计中
		tot.setRollBarcode(newTrayCode);
		tot.setBarcodeType("part");
		tot.setCONSUMERNAME(oldTot.getCONSUMERNAME());
		tot.setProductModel(sod.getProductModel());
		tot.setBatchCode(newTbc.getBatchCode());
		tot.setRollWeight(0.0);
		tot.setProducePlanCode(planDetail.getPlanCode());
		tot.setSalesOrderCode(newTbc.getSalesOrderCode());
		tot.setRollOutputTime(now);
		tot.setDeviceCode(oldTot.getDeviceCode());
		tot.setName(oldTot.getName());
		if(null != oldTot.getWorkShopCode()){
			tot.setWorkShopCode(oldTot.getWorkShopCode());
		}
		tot.setLoginName(optUser);
		tot.setState(0);
		tot.setIsLocked(oldTot.getIsLocked());
		tot.setProductName(oldTot.getProductName());
		tot.setProductLength(oldTot.getProductLength());
		tot.setProductWidth(oldTot.getProductWidth());
		tot.setIsNameLock(oldTot.getIsNameLock());
		tot.setIsPacked(1);
		save(tot);
	}
	public void doTurnBag(String newTrayCode, String oldTrayCode, String optUser) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		TotalStatistics tot=new TotalStatistics();
		TrayBarCode newTbc = codeService.findBarcodeInfo(BarCodeType.TRAY, newTrayCode);
		TrayBarCode oldTbc = codeService.findBarcodeInfo(BarCodeType.TRAY, oldTrayCode);
		Tray oldTray=codeService.findBarCodeReg(BarCodeRegType.TRAY, oldTrayCode);
		ProducePlanDetail planDetail=codeService.findById(ProducePlanDetail.class, newTbc.getProducePlanDetailId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rollBarcode", oldTrayCode);
		List<TotalStatistics> oldTots=codeService.findListByMap(TotalStatistics.class, map);
		TotalStatistics oldTot = oldTots.get(0);
		oldTray.setBatchCode(newTbc.getBatchCode());
		oldTray.setTrayBarcode(newTrayCode);
		SalesOrderDetail sod=findById(SalesOrderDetail.class, newTbc.getSalesOrderDetailId());
		oldTray.setProductModel(sod.getProductModel());
		newTbc.setTurnBagDate(sdf.format(now));
		newTbc.setTurnBagUser(optUser);
		newTbc.setOldBatchCode(oldTbc.getBatchCode());
		newTbc.setOldSalesOrder(oldTbc.getSalesOrderCode());
		oldTbc.setIsOpened(1);

		update(oldTbc);
		update(newTbc);
		update(oldTray);
		//加到生产统计中
		tot.setRollBarcode(newTrayCode);
		tot.setBarcodeType("tray");
		tot.setCONSUMERNAME(oldTot.getCONSUMERNAME());
		tot.setProductModel(sod.getProductModel());
		tot.setRollQualityGradeCode(oldTray.getRollQualityGradeCode());
		tot.setBatchCode(newTbc.getBatchCode());
		tot.setRollWeight(0.0);
		tot.setProducePlanCode(planDetail.getPlanCode());
		tot.setSalesOrderCode(newTbc.getSalesOrderCode());
		tot.setRollOutputTime(now);
		tot.setDeviceCode(oldTot.getDeviceCode());
		tot.setName(oldTot.getName());
		if(null != oldTot.getWorkShopCode()){
			tot.setWorkShopCode(oldTot.getWorkShopCode());
		}
		tot.setLoginName(optUser);
		tot.setState(0);
		tot.setIsLocked(oldTot.getIsLocked());
		tot.setProductName(oldTot.getProductName());
		tot.setProductLength(oldTot.getProductLength());
		tot.setProductWeight(oldTray.getWeight());
		tot.setProductWidth(oldTot.getProductWidth());
		tot.setIsNameLock(oldTot.getIsNameLock());
		tot.setIsPacked(1);
		save(tot);
		map.clear();
		map.put("trayBarcode", oldTrayCode);
		List<TrayBoxRoll> list=findListByMap(TrayBoxRoll.class, map);
		for(TrayBoxRoll tbr:list){
			tbr.setTrayBarcode(newTrayCode);
			update(tbr);
		}
	}
	
	public List<Map<String, Object>> getGoodsPosition(List<Long> ids,List<String> batchCodes){
		return turnBagPlanDao.getGoodsPosition(ids,batchCodes);
	}

	public List<Map<String, Object>> getPackChildren(String code){
		return turnBagPlanDao.getPackChildren(code);
	}
	
	public String trayDeviceCode(String trayCode){
		return turnBagPlanDao.trayDeviceCode(trayCode);
	}
	
	public Map<String,Object> getBatchInfo(Long targetProducePlanDetailId,Long fromProducePlanDetailId){
		return turnBagPlanDao.getBatchInfo(targetProducePlanDetailId,fromProducePlanDetailId);
	}
	/**
	 * 查询翻包领出记录
	 */
	@Override
	public <T> Map<String, Object> findTurnBagOutPageInfo(Filter filter,
			Page page) throws Exception {
		return turnBagPlanDao.findTurnBagOutPageInfo(filter, page);
	}
}
