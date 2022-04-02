package com.bluebirdme.mes.mobile.stock.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.planner.delivery.entity.*;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlan;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlanDetails;
import com.bluebirdme.mes.stock.entity.*;
import com.bluebirdme.mes.store.entity.Roll;
import com.bluebirdme.mes.utils.StockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.mobile.stock.service.IMobileProductStockService;
import com.bluebirdme.mes.planner.delivery.service.IDeliveryPlanService;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.statistics.entity.TotalStatistics;
import com.bluebirdme.mes.statistics.service.ITotalStatisticsService;
import com.bluebirdme.mes.stock.service.IProductStockService;
import com.bluebirdme.mes.store.entity.Tray;
import com.bluebirdme.mes.store.service.ITrayBarCodeService;
import com.bluebirdme.mes.utils.ProductState;

/**
 * 成品库存操作
 *
 * @author Goofy
 * @Date 2016年10月31日 上午9:46:11
 */
@RestController
@RequestMapping("/mobile/stock/product")
public class MobileProductStockController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(MobileProductStockController.class);
    @Resource
    IProductStockService productStockService;
    @Resource
    IDeliveryPlanService deliveryPlanService;
    @Resource
    ITrayBarCodeService trayBarCodeService;
    @Resource
    ITotalStatisticsService totalStatisticsService;
    @Resource
    IMobileProductStockService mobileProductStockService;

    @NoLogin
    @Journal(name = "保存PDA成品入库信息及库存信息", logType = LogType.DB)
    @RequestMapping(value = "in", method = RequestMethod.POST)
    public String pIn(String puname, ProductInRecord productInRecord, Long overTime) throws Exception {
        if (productInRecord.getWarehouseCode() == null || "".equals(productInRecord.getWarehouseCode()) || productInRecord.getWarehousePosCode() == null || "".equals(productInRecord.getWarehousePosCode())) {
            return GsonTools.toJson("请选择库位");
        }
        return GsonTools.toJson(productStockService.saveInRecordAndStock(productInRecord, overTime)); //edit by jxl老入库
    }

    @NoLogin
    @Journal(name = "查询同一批次的其他货物所在的位置")
    @RequestMapping(value = "warhourse", method = RequestMethod.GET)
    public String pwarhourse(String salesOrderCode, String batchCode, String productModel) throws Exception {
        return GsonTools.toJson(productStockService.pwarhourse(salesOrderCode, batchCode, productModel));
    }

    @NoLogin
    @Journal(name = "出库", logType = LogType.DB)
    @RequestMapping(value = "out", method = RequestMethod.POST)
    public String pOut(String puname, String codes, Long UserId, String packingNum, String plate, String boxNumber, Double count) throws Exception {
        boolean flag = false;
        //不为合格或者退货的原料编码
        String errorCode = "";
        Map<String, Object> map = new HashMap<String, Object>();
        String[] _codes = codes.split(",");
        Tray tray = new Tray();
        for (int i = 0; i < _codes.length; i++) {
            map.clear();
            map.put("trayBarcode", _codes[i]);
            tray = productStockService.findUniqueByMap(Tray.class, map);
            if (tray != null && !tray.getRollQualityGradeCode().equals("A")) {
                //拼接异常的条码
                errorCode += errorCode == "" ? _codes[i] : "," + _codes[i];
                flag = true;
            }

            map.clear();
            map.put("partBarcode", _codes[i]);
            Roll roll = productStockService.findUniqueByMap(Roll.class, map);
            if (roll != null && !roll.getRollQualityGradeCode().equals("A")) {
                //拼接异常的条码
                errorCode += errorCode == "" ? _codes[i] : "," + _codes[i];
                flag = true;
            }
        }

        if (flag) {
            return ajaxError(errorCode + "条码状态异常，只有合格品才能出库！");
        } else {
            return GsonTools.toJson(productStockService.saveOutRecordAndUpdateStock(codes, UserId, packingNum, plate, boxNumber, count));// edit by jxl 老出库
        }
    }

    @NoLogin
    @Journal(name = "成品移库", logType = LogType.DB)
    @RequestMapping(value = "move", method = RequestMethod.POST)
    public String pMove(StockMove stockMove, String code) throws Exception {
        if (stockMove.getNewWarehouseCode() == null || "".equals(stockMove.getNewWarehouseCode()) || stockMove.getNewWarehousePosCode() == null || "".equals(stockMove.getNewWarehousePosCode())) {
            return GsonTools.toJson("请选择库位");
        }
        return GsonTools.toJson(productStockService.saveAndUpdate(stockMove, code));//edit by jxl 老移库
    }

    @NoLogin
    @Journal(name = "成品回库", logType = LogType.DB)
    @RequestMapping(value = "pBack", method = RequestMethod.POST)
    public String pBack(ProductStockTran productStockTran, String code) throws Exception {
        if (productStockTran.getNewWarehouseCode() == null || "".equals(productStockTran.getNewWarehouseCode()) || productStockTran.getNewWarehousePosCode() == null || "".equals(productStockTran.getNewWarehousePosCode())) {
            return GsonTools.toJson("请选择库位");
        }
        return GsonTools.toJson(productStockService.pBack(productStockTran, code));
    }

    @NoLogin
    @Journal(name = "胚布从编织线边库到裁剪线边库", logType = LogType.DB)
    @RequestMapping(value = "pbmove", method = RequestMethod.POST)
    public String pbMove(StockFabricMove stockMove, String code) throws Exception {
        if (stockMove.getNewWarehouseCode() == null || "".equals(stockMove.getNewWarehouseCode()) || stockMove.getNewWarehousePosCode() == null || "".equals(stockMove.getNewWarehousePosCode())) {
            return GsonTools.toJson("请选择库位");
        }
        String result = productStockService.saveAndUpdate1(stockMove, code);
        if (result.equals("locked")) {
            return ajaxError("产品被冻结");
        } else if (result.equals("out")) {
            return ajaxError("产品未入库");
        }
        return GsonTools.toJson(result);
    }

    @NoLogin
    @Journal(name = "查询库存信息")
    @RequestMapping(value = "findProductStock", method = RequestMethod.POST)
    public List<Map<String, Object>> findProductStockInfo(String warehouseCode, String warehousePosCode) throws Exception {
        return productStockService.findProductStockInfo(warehouseCode, warehousePosCode);
    }

    @NoLogin
    @Journal(name = "成品盘库", logType = LogType.DB)
    @RequestMapping(value = "check", method = RequestMethod.POST)
    public String pCheck(@RequestBody StockCheck stockCheck) {
        return ajaxSuccess();
    }

    @NoLogin
    @Journal(name = "根据条码查看库存信息")
    @RequestMapping(value = "checkState", method = RequestMethod.POST)
    public String checkState(String code) throws Exception {
        HashMap<String, Object> map = new HashMap();
        map.put("barCode", code);
        if (!productStockService.isExist(ProductStockState.class, map)) {
            return GsonTools.toJson(2);
        }
        return GsonTools.toJson(productStockService.findListByMap(ProductStockState.class, map).get(0));
    }

    @NoLogin
    @Journal(name = "加载出库单号信息")
    @RequestMapping(value = "findDeliveryCode")
    public String findDeliveryCode() {
        return GsonTools.toJson(deliveryPlanService.findDeliveryCode());
    }

    @NoLogin
    @Journal(name = "根据生产计划ID查询生产计划明细")
    @RequestMapping(value = "findDeliveryPlanDetailUrl", method = RequestMethod.POST)
    public String findDeliveryPlanDetailUrl(String packingNumber) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("packingNumber", packingNumber);
        List<DeliveryPlanDetails> d = deliveryPlanService.findListByMap(DeliveryPlanDetails.class, map);
        DeliveryPlanSalesOrders dpsos = deliveryPlanService.findUniqueByMap(DeliveryPlanSalesOrders.class, map);
        if (d.size() == 0) {
            return ajaxError("未找到计划");
        }
        DeliveryPlan p = deliveryPlanService.findById(DeliveryPlan.class, d.get(0).getDeliveryId());
        if (p.getAuditState() != 2) {
            return ajaxError("未审核的计划");
        }
        if (p.getIsClosed() != null && p.getIsClosed() == 1) {
            return ajaxError("计划已关闭");
        } else if (p.getIsComplete() != null && p.getIsComplete() == 1) {
            return ajaxError("计划已完成");
        }
        if (dpsos.getIsFinished() != null && dpsos.getIsFinished() == 1) {
            return ajaxError("该车已出货");
        }
        if (d.size() == 0) {
            return ajaxError("未找到产品");
        }
        List<DeliveryPlanSalesOrders> od = new ArrayList<DeliveryPlanSalesOrders>();
        od.add(dpsos);
        p.setOrderDatas(od);
        p.setProductDatas(d);
        return GsonTools.toJson(p);
    }

    @NoLogin
    @Journal(name = "根据搜索项目和内容查询生产计划明细")
    @RequestMapping(value = "findDeliveryPlanDetail", method = RequestMethod.GET)
    public String findDeliveryPlanDetail(String project, String content) throws Exception {
        return GsonTools.toJson(mobileProductStockService.findDeliveryPlanDetail(project, content));
    }

    @NoLogin
    @Journal(name = "根据出库计划单Id出库单信息")
    @RequestMapping(value = "findDeliveryPlan", method = RequestMethod.GET)
    public String findDeliveryPlanById(String id, String pn) throws Exception {
        return GsonTools.toJson(mobileProductStockService.findDeliveryPlanById(id, pn));
    }

    @NoLogin
    @Journal(name = "根据托条码查询托信息和是否超期")
    @RequestMapping(value = "findTray", method = RequestMethod.GET)
    public String findTray(String barcode) throws Exception {
        return GsonTools.toJson(mobileProductStockService.findTrayByBarCode(barcode));
    }

    @NoLogin
    @Journal(name = "根据出库计划单Id查找该箱号的产品信息")
    @RequestMapping(value = "findDeliveryPlanProduct", method = RequestMethod.GET)
    public String findDeliveryPlanProductById(String id, String pn) throws Exception {
        return GsonTools.toJson(mobileProductStockService.findDeliveryPlanProductById(id, pn));
    }

    @NoLogin
    @Journal(name = "查看货物仓库位置")
    @RequestMapping(value = "stockPosition", method = RequestMethod.GET)
    public String searchProduct(String id) throws Exception {
        DeliveryPlanDetails deliveryPlanDetails = deliveryPlanService.findById(DeliveryPlanDetails.class, Long.parseLong(id));
        String salesOrderSubCode = deliveryPlanDetails.getSalesOrderSubCode();
        String batchCode = deliveryPlanDetails.getBatchCode();
        Long productId = deliveryPlanDetails.getProductId();
        Long partId = deliveryPlanDetails.getPartID();
        List<Map<String, Object>> stock = new ArrayList();
        stock.addAll(deliveryPlanService.searchProduct(salesOrderSubCode, batchCode, productId, partId));
        return GsonTools.toJson(stock);
    }

    @NoLogin
    @Journal(name = "根据托条码查询产品信息", logType = LogType.DB)
    @RequestMapping(value = "findProduct", method = RequestMethod.POST)
    public String findProductByTrayCode(String productIsTc, String trayCode) {
        return GsonTools.toJson(trayBarCodeService.findProductByTraycode(productIsTc, trayCode));
    }

    @NoLogin
    @Journal(name = "冻结条码", logType = LogType.DB)
    @RequestMapping(value = "freeze", method = RequestMethod.POST)
    public String freeze(String puname, Long puid, String codes) throws Exception {
        String[] barcodes = codes.split(",");
        String unKnowBarcode = "";
        String outBarcode = "";
        String noProduce = "";
        boolean isCanSave = true;
        for (String barcode : barcodes) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("rollBarcode", barcode);
            if (!productStockService.isExist(TotalStatistics.class, map)) {
                isCanSave = false;
                if (noProduce.length() == 0) {
                    noProduce = barcode;
                } else {
                    noProduce += "," + barcode;
                }
            }
            map.clear();
            map.put("barcode", barcode);
            // 检查条码的在库状态
            if (productStockService.isExist(ProductStockState.class, map)) {
                ProductStockState state = productStockService.findUniqueByMap(ProductStockState.class, map);
                /*
                 * if(state.getState()!=StockState.IN){
                 * if(outBarcode.length()==0){ outBarcode=barcode; }else{
                 * outBarcode+=","+outBarcode; } }
                 */
            } else {
                isCanSave = false;
                if (unKnowBarcode.length() == 0) {
                    unKnowBarcode = barcode;
                } else {
                    unKnowBarcode += "," + barcode;
                }
            }
        }
        if (isCanSave) {
            productStockService.doFreeze(codes);
            return GsonTools.toJson("");
        } else {
            String result = "";
            if (unKnowBarcode.length() > 0) {
                result = unKnowBarcode + " 未入库</br>";
            }
            if (outBarcode.length() > 0) {
                result += outBarcode + "已出库</br>";
            }
            if (noProduce.length() > 0) {
                result += noProduce + "未产出登记";
            }
            return GsonTools.toJson(result);
        }
    }

    @NoLogin
    @Journal(name = "根据条码查询成品信息", logType = LogType.DB)
    @RequestMapping(value = "findProductInfo", method = RequestMethod.POST)
    public String findProductInfo(String trayCode, String boxCode, String rollCode) throws Exception {
        return GsonTools.toJson(productStockService.findProductInfo(trayCode, boxCode, rollCode));
    }

    @NoLogin
    @ResponseBody
    @Journal(name = "根据条码解冻产品", logType = LogType.DB)
    @RequestMapping(value = "unLock", method = RequestMethod.POST)
    public String unLockByBarcode(String pname, String barcodes) {
        totalStatisticsService.iflockByBarcode(barcodes, ProductState.VALID);
        return Constant.AJAX_SUCCESS;
    }

    @NoLogin
    @ResponseBody
    @Journal(name = "根据条码冻结产品", logType = LogType.DB)
    @RequestMapping(value = "lock", method = RequestMethod.POST)
    public String lockByBarcode(String pname, String barcodes) {
        totalStatisticsService.iflockByBarcode(barcodes, ProductState.FROZEN);
        return Constant.AJAX_SUCCESS;
    }

    @NoLogin
    @ResponseBody
    @Journal(name = "PDA质量判级", logType = LogType.DB)
    @RequestMapping(value = "quality", method = RequestMethod.POST)
    public String quality(String pname, String barcodes, String quality) throws Exception {
        totalStatisticsService.quality(barcodes, quality);
        return Constant.AJAX_SUCCESS;
    }

    @NoLogin
    @Journal(name = "根据托条码查询其库存状态")
    @RequestMapping("state/{trayCode}")
    public String getProductStockState(@PathVariable("trayCode") String trayCode) {
        Map<String, Object> map = new HashMap();
        map.put("barCode", trayCode);
        map.put("p", productStockService.findUniqueByMap(ProductStockState.class, map));
        return GsonTools.toJson(map);
    }

    @NoLogin
    @Journal(name = "卷条码作废", logType = LogType.DB)
    @RequestMapping("abandon")
    public String abandon(String code) throws Exception {
        productStockService.abandon(code);
        return ajaxSuccess();
    }

    @NoLogin
    @Journal(name = "卷条码作废")
    @RequestMapping("abandon2")
    public String abandon2(String code) throws Exception {
        String userId = session.getAttribute(Constant.CURRENT_USER_ID).toString();
        productStockService.abandon(code, userId);
        return ajaxSuccess();
    }

    @NoLogin
    @Journal(name = "查询仓库库位的托条码")
    @RequestMapping("queryTrayBarcode")
    public String queryTrayBarcode(String warehouseCode, String warehousePosCode) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("warehouseCode", warehouseCode);
        map.put("warehousePosCode", warehousePosCode);
        map.put("stockState", 1);
        List<ProductStockState> pssList = productStockService.findListByMap(ProductStockState.class, map);
        return GsonTools.toJson(pssList);
    }

    @NoLogin
    @Journal(name = "异常产品退回车间", logType = LogType.DB)
    @RequestMapping("backToWorkShop")
    public String backToWorkShop(ProductForceOutRecord pfr) {
        mobileProductStockService.save(pfr);
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("trayBarcode", pfr.getBarcode());
        Tray tray = mobileProductStockService.findUniqueByMap(Tray.class, param);
        if (tray != null) {
            User user = mobileProductStockService.findById(User.class, tray.getPackagingStaff());
            Department dept = mobileProductStockService.findById(Department.class, user.getDid());
            pfr.setOutAddress(dept.getName());
            pfr.setOutTime(new Date());
            mobileProductStockService.update(pfr);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("barCode", pfr.getBarcode());
        ProductStockState pss = mobileProductStockService.findUniqueByMap(ProductStockState.class, map);
        if (null == pss) {
            return ajaxError("该条码没有入库");
        }
        pss.setStockState(StockState.back);
        mobileProductStockService.update(pss);
        try {
            map.clear();
            map.put("rollBarcode", pfr.getBarcode());
            TotalStatistics ts = mobileProductStockService.findUniqueByMap(TotalStatistics.class, map);
            ts.setState(0);
            mobileProductStockService.update(ts);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(),e);
        }
        return ajaxSuccess();
    }

    @NoLogin
    @RequestMapping("cars")
    public String cars() {
        return GsonTools.toJson(deliveryPlanService.cars());
    }

    @Journal(name = "绑定出货计划的PDA和操作人", logType = LogType.DB)
    @NoLogin
    @RequestMapping("bind")
    public String bindPdaAndUser(Long id, String pdaID, String optUser) {
        DeliveryPlanSalesOrders o = deliveryPlanService.findById(DeliveryPlanSalesOrders.class, id);
        o.setPdaID(pdaID);
        o.setOptUser(optUser);
        deliveryPlanService.update(o);
        return ajaxSuccess();
    }

    @Journal(name = "取消绑定出货计划的PDA和操作人", logType = LogType.DB)
    @NoLogin
    @RequestMapping("unbind")
    public String unBindPdaAndUser(Long id) {
        DeliveryPlanSalesOrders o = deliveryPlanService.findById(DeliveryPlanSalesOrders.class, id);
        o.setPdaID(null);
        o.setOptUser(null);
        deliveryPlanService.update(o);
        return ajaxSuccess();
    }

    @Journal(name = "获取成品入库信息")
    @NoLogin
    @RequestMapping("list")
    public String getProductStock(String stockType, String stockState, String salesCode, String batchCode, String factoryProductName, String consumerProductnNme) throws Exception {
        Page page = new Page();
        page.setAll(1);
        Filter filter = new Filter();
        filter.set("stockType", stockType);
        filter.set("stockState", stockState);
        if (salesCode != null && !"".equals(salesCode))
            filter.set("salesCode", "like:" + salesCode);
        if (batchCode != null && !"".equals(batchCode))
            filter.set("batchCode", "like:" + batchCode);
        if (factoryProductName != null && !"".equals(factoryProductName))
            filter.set("factoryProductName", "like:" + factoryProductName);
        if (consumerProductnNme != null && !"".equals(consumerProductnNme))
            filter.set("consumerProductnNme", "like:" + consumerProductnNme);
        Map<String, Object> findPageInfo = productStockService.findPageInfo(filter, page);
        return GsonTools.toJson(findPageInfo);
    }

    @NoLogin
    @Journal(name = "通过仓库代码获取仓库名字")
    @RequestMapping(value = "warhourseName", method = RequestMethod.GET)
    public String pwarhourseName(String warehouseCode) {
        return GsonTools.toJson(productStockService.pwarhourseName(warehouseCode));
    }
}
