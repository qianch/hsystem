/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.mobile.common.service.IMobileService;
import com.bluebirdme.mes.mobile.stock.service.IMobilePackageService;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlan;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlanSalesOrders;
import com.bluebirdme.mes.planner.deliveryontheway.service.IDeliveryOnTheWayPlanService;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.turnbag.service.ITurnBagPlanService;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.service.ILogService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.service.ISalesOrderStockService;
import com.bluebirdme.mes.statistics.entity.TotalStatistics;
import com.bluebirdme.mes.statistics.service.ITotalStatisticsService;
import com.bluebirdme.mes.stock.dao.IProductStockDao;
import com.bluebirdme.mes.stock.entity.*;
import com.bluebirdme.mes.stock.service.IProductStockService;
import com.bluebirdme.mes.store.dao.IWarehouseDao;
import com.bluebirdme.mes.store.entity.*;
import com.bluebirdme.mes.store.service.IBarCodeService;
import com.bluebirdme.mes.utils.ProductState;
import com.bluebirdme.mes.utils.StockState;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 徐波
 * @Date 2016-10-24 15:08:20
 */
@Service
@AnyExceptionRollback
public class ProductStockServiceImpl extends BaseServiceImpl implements IProductStockService {
    @Resource
    IProductStockDao productStockDao;
    @Resource
    ITotalStatisticsService totalStatisticsService;
    @Resource
    ISalesOrderStockService salesOrderStockService;
    @Resource
    IMobileService mobileService;
    @Resource
    IWeavePlanService weavePlanService;
    @Resource
    IBarCodeService codeService;
    @Resource
    ITurnBagPlanService tbService;
    @Resource
    IMobilePackageService packageService;
    @Resource
    ILogService logService;
    @Resource
    IDeliveryOnTheWayPlanService deliveryonthewayPlanService;
    @Resource
    IWarehouseDao warehouseDao;

    @Override
    protected IBaseDao getBaseDao() {
        return productStockDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return productStockDao.findPageInfo(filter, page);
    }

    public String saveAndUpdate(StockMove stockMove, String code) throws Exception {
        String result = "";
        String[] codes = code.split(",");
        List<StockMove> st = new ArrayList<StockMove>();
        List<ProductStockState> pss = new ArrayList<ProductStockState>();

        for (String s : codes) {
            StockMove _stockMove = new StockMove();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("barCode", s);
            // 根据每个条码去库存表查询原来的库位库房代码
            List<ProductStockState> productStockState = this.findListByMap(ProductStockState.class, map);
            for (ProductStockState stockState : productStockState) {
                if (stockState.getWarehouseCode().equals(stockMove.getNewWarehouseCode()) && stockState.getWarehousePosCode().equals(stockMove.getNewWarehousePosCode())) {
                } else {
                    // 保存信息到库存移库表
                    _stockMove.setMoveTime(new Date());
                    _stockMove.setBarcode(s);
                    _stockMove.setNewWarehouseCode(stockMove.getNewWarehouseCode());
                    _stockMove.setNewWarehousePosCode(stockMove.getNewWarehousePosCode());
                    _stockMove.setOriginWarehouseCode(productStockState.get(0).getWarehouseCode());
                    _stockMove.setOriginWarehousePosCode(productStockState.get(0).getWarehousePosCode());
                    _stockMove.setMoveUserId(stockMove.getMoveUserId());
                    st.add(_stockMove);
                    ProductStockState _productStockState = this.findById(ProductStockState.class, productStockState.get(0).getId());
                    _productStockState.setWarehouseCode(stockMove.getNewWarehouseCode());
                    _productStockState.setWarehousePosCode(stockMove.getNewWarehousePosCode());
                    if (_productStockState.getStockState() == StockState.OUT) {
                        _productStockState.setStockState(StockState.IN);
                    }
                    pss.add(_productStockState);
                }
            }
        }
        productStockDao.save(st.toArray(new StockMove[]{}));
        productStockDao.update2(pss.toArray(new ProductStockState[]{}));
        return result;
    }

    public String saveAndUpdate1(StockFabricMove stockMove, String code) throws Exception {
        String result = "";
        String[] codes = code.split(",");
        List<StockFabricMove> st = new ArrayList<>();
        List<ProductStockState> pss = new ArrayList<>();
        for (String value : codes) {
            StockFabricMove _stockMove = new StockFabricMove();
            Map<String, Object> map = new HashMap<>();
            map.put("barCode", value);
            // 根据每个条码去库存表查询原来的库位库房代码
            List<ProductStockState> productStockState = this.findListByMap(ProductStockState.class, map);
            map.clear();
            map.put("rollBarcode", value);
            TotalStatistics ts = findUniqueByMap(TotalStatistics.class, map);
            if (ts.getIsLocked() == 1) {
                return "locked";
            }
            if (ts.getState() != 1) {
                return "out";
            }

            for (ProductStockState p1 : productStockState) {
                //是托的话存入卷状态
                if (p1.getBarCode().startsWith("T") || p1.getBarCode().startsWith("P")) {
                    p1.setStockState(-1);
                    productStockDao.update(p1);
                    HashMap<String, Object> mm = new HashMap<>();
                    mm.put("trayBarCode", p1.getBarCode());
                    List<TrayBoxRoll> tbr = productStockDao.findListByMap(TrayBoxRoll.class, mm);
                    for (TrayBoxRoll t : tbr) {
                        mm.clear();
                        mm.put("barcode", t.getRollBarcode());
                        List<ProductStockState> list = productStockDao.findListByMap(ProductStockState.class, mm);
                        for (ProductStockState p2 : list) {
                            p2.setWarehouseCode(stockMove.getNewWarehouseCode());
                            productStockDao.update(p2);
                            StockFabricMove s = new StockFabricMove();
                            s.setMoveTime(new Date());
                            s.setBarcode(p2.getBarCode());
                            s.setNewWarehouseCode(stockMove.getNewWarehouseCode());
                            s.setNewWarehousePosCode(stockMove.getNewWarehousePosCode());
                            s.setOriginWarehouseCode(productStockState.get(0).getWarehouseCode());
                            s.setOriginWarehousePosCode(productStockState.get(0).getWarehousePosCode());
                            s.setMoveUserId(stockMove.getMoveUserId());
                            st.add(s);
                        }
                    }
                    packageService.open(p1.getBarCode());
                }
            }

            for (ProductStockState stockState : productStockState) {
                boolean b = stockState.getWarehouseCode().equals(stockMove.getNewWarehouseCode()) && stockState.getWarehousePosCode().equals(stockMove.getNewWarehousePosCode());
                if (!b) {
                    // 保存信息到库存移库表
                    _stockMove.setMoveTime(new Date());
                    _stockMove.setBarcode(value);
                    _stockMove.setNewWarehouseCode(stockMove.getNewWarehouseCode());
                    _stockMove.setNewWarehousePosCode(stockMove.getNewWarehousePosCode());
                    _stockMove.setOriginWarehouseCode(productStockState.get(0).getWarehouseCode());
                    _stockMove.setOriginWarehousePosCode(productStockState.get(0).getWarehousePosCode());
                    _stockMove.setMoveUserId(stockMove.getMoveUserId());
                    st.add(_stockMove);
                    ProductStockState _productStockState = this.findById(ProductStockState.class, productStockState.get(0).getId());
                    _productStockState.setWarehouseCode(stockMove.getNewWarehouseCode());
                    _productStockState.setWarehousePosCode(stockMove.getNewWarehousePosCode());
                    if (_productStockState.getStockState() == StockState.OUT) {
                        _productStockState.setStockState(StockState.IN);
                    }
                    pss.add(_productStockState);
                }
            }
        }
        productStockDao.save(st.toArray(new StockFabricMove[]{}));
        productStockDao.update2(pss.toArray(new ProductStockState[]{}));
        return result;
    }

    @Override
    public List<Map<String, Object>> findProductStockInfo(String warehouseCode, String warehousePosCode) throws SQLTemplateException {
        return productStockDao.findProductStockInfo(warehouseCode, warehousePosCode);
    }

    @Override
    public String pwarhourse(String salesOrderCode, String batchCode, String productModel) throws Exception {
        List<Map<String, Object>> findWarhourse = productStockDao.findWarhourse(salesOrderCode, batchCode, productModel);
        String warehouse = "";
        if (findWarhourse.size() > 0) {
            Object wcode = findWarhourse.get(0).get("WAREHOUSENAME");
            Object wposcode = findWarhourse.get(0).get("WAREHOUSEPOSCODE");
            warehouse = wcode + "（" + wposcode + "）";
            return warehouse;
        }
        return warehouse;
    }

    @Override
    public String saveInRecordAndStock(ProductInRecord productInRecord, Long overTime) throws Exception {
        String result = "";
        ProductStockState productStockState = new ProductStockState();
        productStockState.setStockState(StockState.IN);
        productStockState.setWarehouseCode(productInRecord.getWarehouseCode());
        productStockState.setWarehousePosCode(productInRecord.getWarehousePosCode());
        productStockState.setOverTime(overTime);
        HashMap<String, Object> map = new HashMap<String, Object>();
        productStockState.setBarCode(productInRecord.getBarCode());
        map.put("barCode", productInRecord.getBarCode());
        ProductStockState pss = productStockDao.findUniqueByMap(ProductStockState.class, map);
        Tray t = codeService.findBarCodeReg(BarCodeRegType.TRAY, productInRecord.getBarCode());
        TrayBarCode tbc = codeService.findBarcodeInfo(BarCodeType.TRAY, productInRecord.getBarCode());
        if (pss != null) {
            if (pss.getStockState() == -1) {
                pss.setStockState(1);
                map.clear();
                map.put("rollBarcode", pss.getBarCode());
                TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map);
                ts.setState(StockState.IN);
                productStockDao.update(ts);
                productStockDao.update2(pss);
            }
        } else {
            map.clear();
            map.put("rollBarcode", productStockState.getBarCode());
            // 保存卷库存状态表
            productStockDao.save(productStockState);
            TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map);
            if (ts == null) {
                ts = new TotalStatistics();
                ts.setBarcodeType("tray");
                ts.setBatchCode(productStockState.getBarCode());
                ts.setDeviceCode(tbService.trayDeviceCode(productInRecord.getBarCode()));
                ts.setIsLocked(-1);
                ts.setIsPacked(0);
                User u = findById(User.class, t.getPackagingStaff());
                ts.setLoginName(u.getLoginName());
                ProducePlanDetail ppd = findById(ProducePlanDetail.class, tbc.getProducePlanDetailId());
                ProducePlan produce = findById(ProducePlan.class, ppd.getProducePlanId());
                ts.setCONSUMERNAME(ppd.getConsumerName());
                ts.setName(produce.getWorkshop());
                ts.setWorkShopCode(produce.getWorkShopCode());
                ts.setProducePlanCode(produce.getProducePlanCode());
                ts.setProductLength(ppd.getProductLength());
                ts.setProductModel(ppd.getProductModel());
                ts.setProductName(ppd.getFactoryProductName());
                ts.setProductWeight(ppd.getProductWeight());
                ts.setProductWidth(ppd.getProductWidth());
                ts.setRollBarcode(productInRecord.getBarCode());
                ts.setRollOutputTime(new Date());
                ts.setRollQualityGradeCode("A");
                ts.setRollWeight(t.getWeight());
                ts.setSalesOrderCode(tbc.getSalesOrderCode());
                ts.setState(StockState.IN);
                save(ts);
            } else {
                ts.setState(StockState.IN);
                productStockDao.update(ts);
            }
        }
        String barcode = productInRecord.getBarCode();
        if (barcode.startsWith("T") || barcode.startsWith("P")) {
            HashMap<String, Object> map1 = new HashMap<String, Object>();
            map1.put("trayBarcode", barcode);
            List<TrayBoxRoll> tbrList = findListByMap(TrayBoxRoll.class, map1);
            for (TrayBoxRoll tbr : tbrList) {
                map1.clear();
                if (tbr.getRollBarcode() != null) {
                    map1.put("rollBarcode", tbr.getRollBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.IN);
                    productStockDao.update(ts);
                    //是托的话存入卷状态
                    ProductStockState pss1 = new ProductStockState();
                    pss1.setBarCode(tbr.getRollBarcode());
                    pss1.setStockState(StockState.IN);
                    pss1.setWarehouseCode(productInRecord.getWarehouseCode());
                    pss1.setWarehousePosCode(productInRecord.getWarehousePosCode());
                    pss1.setOverTime(overTime);
                    productStockDao.save(pss1);
                    //对打托的卷进行入库记录
                    ProductInRecord pIn = new ProductInRecord();
                    map1.clear();
                    map1.put("rollBarcode", tbr.getRollBarcode());
                    Roll r = productStockDao.findUniqueByMap(Roll.class, map1);
                    if (r != null) {
                        pIn.setBarCode(tbr.getRollBarcode());
                        pIn.setInTime(new Date());
                        pIn.setOperateUserId(r.getRollUserId());
                        pIn.setWeight(r.getRollWeight());
                        pIn.setWarehouseCode(productInRecord.getWarehouseCode());
                        pIn.setWarehousePosCode(productInRecord.getWarehousePosCode());
                        pIn.setInBankSource(productInRecord.getInBankSource());
                        pIn.setSyncState(1);
                        productStockDao.save(pIn);
                    }
                } else if (tbr.getPartBarcode() != null) {
                    map1.put("rollBarcode", tbr.getPartBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.IN);
                    productStockDao.update(ts);
                } else {
                    map1.put("rollBarcode", tbr.getBoxBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.IN);
                    productStockDao.update(ts);
                    // 获取盒卷关系信息，修改在库状态
                    map1.clear();
                    map1.put("boxBarcode", tbr.getBoxBarcode());
                    List<BoxRoll> brList = productStockDao.findListByMap(BoxRoll.class, map1);
                    for (BoxRoll br : brList) {
                        map1.clear();
                        if (br.getRollBarcode() != null) {
                            map1.put("rollBarcode", br.getRollBarcode());
                            TotalStatistics ts1 = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                            ts.setState(StockState.IN);
                            productStockDao.update(ts1);
                        } else if (br.getPartBarcode() != null) {
                            map1.put("rollBarcode", br.getPartBarcode());
                            TotalStatistics ts1 = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                            ts.setState(StockState.IN);
                            productStockDao.update(ts1);
                        }
                    }
                }
            }
        }

        productInRecord.setInTime(new Date());
        ProductInRecord record = productStockDao.findOne(ProductInRecord.class, "barCode", productInRecord.getBarCode());
        if (record == null) {
            productStockDao.save(productInRecord);
        } else {
            record.setInTime(productInRecord.getInTime());
            record.setOperateUserId(productInRecord.getOperateUserId());
            record.setWarehouseCode(productInRecord.getWarehouseCode());
            record.setWarehousePosCode(productInRecord.getWarehousePosCode());
            record.setWeight(productInRecord.getWeight());
            productStockDao.update(record);
        }
        return result;
    }


    /**
     * 胚布待入库
     */
    @Override
    public String pbPendingIn(PendingInRecord pendingInRecord, Long overTime) {
        String result = "";
        ProductStockState productStockState = new ProductStockState();
        productStockState.setStockState(StockState.STOCKPENDING);
        productStockState.setWarehouseCode(pendingInRecord.getWarehouseCode());
        productStockState.setWarehousePosCode(pendingInRecord.getWarehousePosCode());
        productStockState.setOverTime(overTime);
        HashMap<String, Object> map = new HashMap<String, Object>();
        productStockState.setBarCode(pendingInRecord.getBarCode());
        map.put("barCode", pendingInRecord.getBarCode());
        ProductStockState pss = productStockDao.findUniqueByMap(ProductStockState.class, map);
        Tray t = codeService.findBarCodeReg(BarCodeRegType.TRAY, pendingInRecord.getBarCode());
        TrayBarCode tbc = codeService.findBarcodeInfo(BarCodeType.TRAY, pendingInRecord.getBarCode());
        if (pss == null) {
            map.clear();
            map.put("rollBarcode", productStockState.getBarCode());
            // 保存卷库存状态表
            productStockDao.save(productStockState);
            TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map);
            if (ts == null) {
                ts = new TotalStatistics();
                ts.setBarcodeType("tray");
                ts.setBatchCode(productStockState.getBarCode());
                ts.setDeviceCode(tbService.trayDeviceCode(pendingInRecord.getBarCode()));
                ts.setIsLocked(-1);
                ts.setIsPacked(0);
                User u = findById(User.class, t.getPackagingStaff());
                ts.setLoginName(u.getLoginName());
                ProducePlanDetail ppd = findById(ProducePlanDetail.class, tbc.getProducePlanDetailId());
                ProducePlan produce = findById(ProducePlan.class, ppd.getProducePlanId());
                ts.setCONSUMERNAME(ppd.getConsumerName());
                ts.setName(produce.getWorkshop());
                ts.setWorkShopCode(produce.getWorkShopCode());
                ts.setProducePlanCode(produce.getProducePlanCode());
                ts.setProductLength(ppd.getProductLength());
                ts.setProductModel(ppd.getProductModel());
                ts.setProductName(ppd.getFactoryProductName());
                ts.setProductWeight(ppd.getProductWeight());
                ts.setProductWidth(ppd.getProductWidth());
                ts.setRollBarcode(pendingInRecord.getBarCode());
                ts.setRollOutputTime(new Date());
                ts.setRollQualityGradeCode("A");
                ts.setRollWeight(t.getWeight());
                ts.setSalesOrderCode(tbc.getSalesOrderCode());
                ts.setState(StockState.STOCKPENDING);
                save(ts);
            } else {
                ts.setState(StockState.STOCKPENDING);
                productStockDao.update(ts);
            }
        }
        String barcode = pendingInRecord.getBarCode();
        if (barcode.startsWith("T") || barcode.startsWith("P")) {
            HashMap<String, Object> map1 = new HashMap<String, Object>();
            map1.put("trayBarcode", barcode);
            List<TrayBoxRoll> tbrList = findListByMap(TrayBoxRoll.class, map1);
            for (TrayBoxRoll tbr : tbrList) {
                map1.clear();
                if (tbr.getRollBarcode() != null) {
                    map1.put("rollBarcode", tbr.getRollBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.STOCKPENDING);
                    productStockDao.update(ts);
                    //是托的话存入卷状态
                    ProductStockState pss1 = new ProductStockState();
                    pss1.setBarCode(tbr.getRollBarcode());
                    pss1.setStockState(StockState.STOCKPENDING);
                    pss1.setWarehouseCode(pendingInRecord.getWarehouseCode());
                    pss1.setWarehousePosCode(pendingInRecord.getWarehousePosCode());
                    pss1.setOverTime(overTime);
                    productStockDao.save(pss1);
                    //对打托的卷进行入库记录
                    PendingInRecord pIn = new PendingInRecord();
                    map1.clear();
                    map1.put("rollBarcode", tbr.getRollBarcode());
                    Roll r = productStockDao.findUniqueByMap(Roll.class, map1);
                    if (r != null) {
                        pIn.setBarCode(tbr.getRollBarcode());
                        pIn.setInTime(new Date());
                        pIn.setOperateUserId(r.getRollUserId());
                        pIn.setWeight(r.getRollWeight());
                        pIn.setWarehouseCode(pendingInRecord.getWarehouseCode());
                        pIn.setWarehousePosCode(pendingInRecord.getWarehousePosCode());
                        pIn.setInBankSource(pendingInRecord.getInBankSource());
                        pIn.setSyncState(1);
                        productStockDao.save(pIn);
                    }
                } else if (tbr.getPartBarcode() != null) {
                    map1.put("rollBarcode", tbr.getPartBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.STOCKPENDING);
                    productStockDao.update(ts);
                } else {
                    map1.put("rollBarcode", tbr.getBoxBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.STOCKPENDING);
                    productStockDao.update(ts);
                    // 获取盒卷关系信息，修改在库状态
                    map1.clear();
                    map1.put("boxBarcode", tbr.getBoxBarcode());
                    List<BoxRoll> brList = productStockDao.findListByMap(BoxRoll.class, map1);
                    for (BoxRoll br : brList) {
                        map1.clear();
                        if (br.getRollBarcode() != null) {
                            map1.put("rollBarcode", br.getRollBarcode());
                            TotalStatistics ts1 = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                            ts.setState(StockState.STOCKPENDING);
                            productStockDao.update(ts1);
                        } else if (br.getPartBarcode() != null) {
                            map1.put("rollBarcode", br.getPartBarcode());
                            TotalStatistics ts1 = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                            ts.setState(StockState.STOCKPENDING);
                            productStockDao.update(ts1);
                        }
                    }
                }
            }
        }

        pendingInRecord.setInTime(new Date());
        PendingInRecord record = productStockDao.findOne(PendingInRecord.class, "barCode", pendingInRecord.getBarCode());
        if (record == null) {
            productStockDao.save(pendingInRecord);
        } else {
            record.setInTime(pendingInRecord.getInTime());
            record.setOperateUserId(pendingInRecord.getOperateUserId());
            record.setWarehouseCode(pendingInRecord.getWarehouseCode());
            record.setWarehousePosCode(pendingInRecord.getWarehousePosCode());
            record.setWeight(pendingInRecord.getWeight());
            productStockDao.update(record);
        }
        return result;
    }

    /**
     * 胚布新入库
     */
    @Override
    public String pbIn(ProductInRecord productInRecord) throws Exception {
        List<ProductInRecord> productInRecordlist = new ArrayList<>();
        return pbIns(productInRecordlist);
    }


    /**
     * 胚布老入库
     */
    @Override
    public String savePbIn(ProductInRecord productInRecord, Long overTime) {
        String result = "";
        ProductStockState productStockState = new ProductStockState();
        productStockState.setStockState(StockState.IN);
        productStockState.setWarehouseCode(productInRecord.getWarehouseCode());
        productStockState.setWarehousePosCode(productInRecord.getWarehousePosCode());
        productStockState.setOverTime(overTime);
        HashMap<String, Object> map = new HashMap<>();
        productStockState.setBarCode(productInRecord.getBarCode());
        map.put("barCode", productInRecord.getBarCode());
        ProductStockState pss = productStockDao.findUniqueByMap(ProductStockState.class, map);
        Tray t = codeService.findBarCodeReg(BarCodeRegType.TRAY, productInRecord.getBarCode());
        TrayBarCode tbc = codeService.findBarcodeInfo(BarCodeType.TRAY, productInRecord.getBarCode());
        if (pss == null) {
            map.clear();
            map.put("rollBarcode", productStockState.getBarCode());
            // 保存卷库存状态表
            productStockDao.save(productStockState);
            TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map);
            if (ts == null) {
                ts = new TotalStatistics();
                ts.setBarcodeType("tray");
                ts.setBatchCode(productStockState.getBarCode());
                ts.setDeviceCode(tbService.trayDeviceCode(productInRecord.getBarCode()));
                ts.setIsLocked(-1);
                ts.setIsPacked(0);
                User u = findById(User.class, t.getPackagingStaff());
                ts.setLoginName(u.getLoginName());
                ProducePlanDetail ppd = findById(ProducePlanDetail.class, tbc.getProducePlanDetailId());
                ProducePlan produce = findById(ProducePlan.class, ppd.getProducePlanId());
                ts.setCONSUMERNAME(ppd.getConsumerName());
                ts.setName(produce.getWorkshop());
                ts.setWorkShopCode(produce.getWorkShopCode());
                ts.setProducePlanCode(produce.getProducePlanCode());
                ts.setProductLength(ppd.getProductLength());
                ts.setProductModel(ppd.getProductModel());
                ts.setProductName(ppd.getFactoryProductName());
                ts.setProductWeight(ppd.getProductWeight());
                ts.setProductWidth(ppd.getProductWidth());
                ts.setRollBarcode(productInRecord.getBarCode());
                ts.setRollOutputTime(new Date());
                ts.setRollQualityGradeCode("A");
                ts.setRollWeight(t.getWeight());
                ts.setSalesOrderCode(tbc.getSalesOrderCode());
                ts.setState(StockState.IN);
                save(ts);
            } else {
                ts.setState(StockState.IN);
                productStockDao.update(ts);
            }
        }
        String barcode = productInRecord.getBarCode();
        if (barcode.startsWith("T") || barcode.startsWith("P")) {
            HashMap<String, Object> map1 = new HashMap<String, Object>();
            map1.put("trayBarcode", barcode);
            List<TrayBoxRoll> tbrList = findListByMap(TrayBoxRoll.class, map1);
            for (TrayBoxRoll tbr : tbrList) {
                map1.clear();
                if (tbr.getRollBarcode() != null) {
                    map1.put("rollBarcode", tbr.getRollBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.IN);
                    productStockDao.update(ts);
                    //是托的话存入卷状态
                    ProductStockState pss1 = new ProductStockState();
                    pss1.setBarCode(tbr.getRollBarcode());
                    pss1.setStockState(StockState.IN);
                    pss1.setWarehouseCode(productInRecord.getWarehouseCode());
                    pss1.setWarehousePosCode(productInRecord.getWarehousePosCode());
                    pss1.setOverTime(overTime);
                    productStockDao.save(pss1);
                    //对打托的卷进行入库记录
                    ProductInRecord pIn = new ProductInRecord();
                    map1.clear();
                    map1.put("rollBarcode", tbr.getRollBarcode());
                    Roll r = productStockDao.findUniqueByMap(Roll.class, map1);
                    if (r != null) {
                        pIn.setBarCode(tbr.getRollBarcode());
                        pIn.setInTime(new Date());
                        pIn.setOperateUserId(r.getRollUserId());
                        pIn.setWeight(r.getRollWeight());
                        pIn.setWarehouseCode(productInRecord.getWarehouseCode());
                        pIn.setWarehousePosCode(productInRecord.getWarehousePosCode());
                        pIn.setInBankSource(productInRecord.getInBankSource());
                        pIn.setSyncState(1);
                        productStockDao.save(pIn);
                    }
                } else if (tbr.getPartBarcode() != null) {
                    map1.put("rollBarcode", tbr.getPartBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.IN);
                    productStockDao.update(ts);
                } else {
                    map1.put("rollBarcode", tbr.getBoxBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.IN);
                    productStockDao.update(ts);
                    // 获取盒卷关系信息，修改在库状态
                    map1.clear();
                    map1.put("boxBarcode", tbr.getBoxBarcode());
                    List<BoxRoll> brList = productStockDao.findListByMap(BoxRoll.class, map1);
                    for (BoxRoll br : brList) {
                        map1.clear();
                        if (br.getRollBarcode() != null) {
                            map1.put("rollBarcode", br.getRollBarcode());
                            TotalStatistics ts1 = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                            ts.setState(StockState.IN);
                            productStockDao.update(ts1);
                        } else if (br.getPartBarcode() != null) {
                            map1.put("rollBarcode", br.getPartBarcode());
                            TotalStatistics ts1 = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                            ts.setState(StockState.IN);
                            productStockDao.update(ts1);
                        }
                    }
                }
            }
        }

        productInRecord.setInTime(new Date());
        ProductInRecord record = productStockDao.findOne(ProductInRecord.class, "barCode", productInRecord.getBarCode());
        if (record == null) {
            productStockDao.save(productInRecord);
        } else {
            record.setInTime(productInRecord.getInTime());
            record.setOperateUserId(productInRecord.getOperateUserId());
            record.setWarehouseCode(productInRecord.getWarehouseCode());
            record.setWarehousePosCode(productInRecord.getWarehousePosCode());
            record.setWeight(productInRecord.getWeight());
            productStockDao.update(record);
        }
        return result;
    }

    /**
     * 老出库
     */
    @Override
    public String saveOutRecordAndUpdateStock(String codes, Long UserId, String packingNum, String plate, String boxNumber, Double count) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("packingNumber", packingNum);
        DeliveryPlanSalesOrders dpso = findUniqueByMap(DeliveryPlanSalesOrders.class, map);
        map.clear();
        Long deliveryId;
        if (dpso == null) {
            return "未找到出货计划";
        } else {
            deliveryId = dpso.getDeliveryId();
        }
        DeliveryPlan dp = productStockDao.findById(DeliveryPlan.class, deliveryId);
        String result = "";
        String[] code = codes.split(",");
        List<ProductStockState> _pss = new ArrayList<>();
        List<ProductOutRecord> _por = new ArrayList<>();
        map.clear();

        for (String s : code) {
            if (totalStatisticsService.isFrozen(s) == ProductState.FROZEN) {
                return s + "已冻结，禁止出库";
            }
            map.clear();
            map.put("barCode", s);
            List<ProductStockState> li = productStockDao.findListByMap(ProductStockState.class, map);
            map.clear();
            map.put("trayBarcode", s);
            for (ProductStockState pss : li) {
                if (pss == null) {
                    return "无库存记录，禁止出库";
                }
                if (pss.getStockState() == StockState.OUT) {
                    return pss.getBarCode() + "已出库，禁止重复出库";
                }
                map.clear();
                map.put("rollBarcode", pss.getBarCode());
                TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map);
                ts.setState(StockState.OUT);
                map.clear();
                map.put("barcode", pss.getBarCode());

                Long salesProductId = 0L;
                TrayBarCode trayBarCode = findUniqueByMap(TrayBarCode.class, map);
                if (trayBarCode != null) {
                    salesProductId = trayBarCode.getSalesProductId();
                }

                PartBarcode partBarcode = findUniqueByMap(PartBarcode.class, map);
                if (partBarcode != null) {
                    salesProductId = partBarcode.getSalesProductId();
                }

                FinishedProduct fp = findById(FinishedProduct.class, salesProductId);
                pss.setStockState(StockState.OUT);
                _pss.add(pss);

                ProductOutRecord por = new ProductOutRecord();
                por.setPackingNumber(packingNum);
                por.setProductConsumerName(fp.getConsumerProductName());
                por.setProductFactoryName(fp.getFactoryProductName());
                por.setProductModel(fp.getProductModel());
                por.setDeliveryId(Integer.valueOf(deliveryId.toString()));
                if (fp.getProductWidth() != null) {
                    por.setWidth(fp.getProductWidth());
                }
                if (fp.getProductRollLength() != null) {
                    por.setLength(fp.getProductRollLength());
                }
                por.setWarehouseCode(pss.getWarehouseCode());
                por.setWarehousePosCode(pss.getWarehousePosCode());
                por.setBarCode(pss.getBarCode());
                por.setOutTime(new Date());

                map.clear();
                map.put("trayBarcode", pss.getBarCode());
                List<Tray> tray = productStockDao.findListByMap(Tray.class, map);
                if (tray.size() > 0) {
                    por.setWeight(tray.get(tray.size() - 1).getWeight());
                }

                map.clear();
                map.put("partBarcode", pss.getBarCode());
                List<Roll> rolls = productStockDao.findListByMap(Roll.class, map);
                if (rolls.size() > 0) {
                    por.setWeight(rolls.get(rolls.size() - 1).getRollWeight());
                }
                por.setOperateUserId(UserId);
                por.setDeliveryCode(dp.getDeliveryCode());
                _por.add(por);
                productStockDao.update(ts);
            }

            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("trayBarcode", s);
            List<TrayBoxRoll> tbrList = findListByMap(TrayBoxRoll.class, map1);
            for (TrayBoxRoll tbr : tbrList) {
                map1.clear();
                if (tbr.getRollBarcode() != null) {
                    map1.put("rollBarcode", tbr.getRollBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.OUT);
                    productStockDao.update(ts);
                } else if (tbr.getPartBarcode() != null) {
                    map1.put("rollBarcode", tbr.getPartBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.OUT);
                    productStockDao.update(ts);
                } else {
                    map1.put("rollBarcode", tbr.getBoxBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.OUT);
                    productStockDao.update(ts);
                    // 获取盒卷关系信息，修改在库状态
                    map1.clear();
                    map1.put("boxBarcode", tbr.getBoxBarcode());
                    List<BoxRoll> brList = productStockDao.findListByMap(BoxRoll.class, map1);
                    for (BoxRoll br : brList) {
                        map1.clear();
                        if (br.getRollBarcode() != null) {
                            map1.put("rollBarcode", br.getRollBarcode());
                        } else if (br.getPartBarcode() != null) {
                            map1.put("rollBarcode", br.getPartBarcode());
                        }
                        TotalStatistics rollTS = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                        rollTS.setState(StockState.OUT);
                        productStockDao.update(rollTS);
                    }
                }
            }
        }

        dpso.setRealBoxNumber(boxNumber);
        dpso.setPlate(plate);
        dpso.setIsFinished(1);
        productStockDao.update(dpso);
        map.clear();
        map.put("deliveryId", deliveryId);
        List<DeliveryPlanSalesOrders> dpsos = productStockDao.findListByMap(DeliveryPlanSalesOrders.class, map);
        boolean isFinished = true;
        for (DeliveryPlanSalesOrders d : dpsos) {
            if (d.getIsFinished() == null || d.getIsFinished() == 0) {
                isFinished = false;
                break;
            }
        }
        if (isFinished) {
            dp.setIsComplete(1);
            productStockDao.update2(dp);
        }
        ProductOutOrder poo = new ProductOutOrder();
        poo.setOutTime(new Date());
        poo.setBoxNumber(dpso.getBoxNumber());
        poo.setConsumerName(dp.getDeliveryTargetCompany());
        poo.setCount(count);
        poo.setDeliveryCode(dp.getDeliveryCode());
        poo.setPn(dpso.getPn());
        poo.setLadingCode(dpso.getLadingCode());
        poo.setLogisticsCompany(dp.getLogisticsCompany());
        poo.setPackingNumber(dpso.getPackingNumber());
        poo.setOperateUserId(dp.getDeliveryBizUserId());
        poo.setPlate(dpso.getPlate());
        poo.setRealBoxNumber(dpso.getRealBoxNumber());
        poo.setSerialNumber(dpso.getSerialNumber());
        poo.setDeliveryId(Long.valueOf(deliveryId.toString()));
        poo.setSampleInformation(dp.getSampleInformation());
        productStockDao.save(poo);
        productStockDao.update2(_pss.toArray(new ProductStockState[]{}));
        productStockDao.save(_por.toArray(new ProductOutRecord[]{}));
        return result;
    }

    @Override
    public Map<String, Object> findStateByCode(String barCode, String materialCode) {
        Map<String, Object> ret = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        String PRODUCED = "produced", STOCK = "stock";
        // 成品条码
        if (!StringUtils.isBlank(barCode)) {
            // 如果是托条码
            if (barCode.startsWith("T")) {
                param.put("trayBarcode", barCode);
                // 查询产出状态
                if (this.isExist(Tray.class, param)) {
                    ret.put(PRODUCED, true);
                } else {
                    ret.put(PRODUCED, false);
                }
            } else if (barCode.startsWith("B")) {
                // 箱信息查询
                param.put("boxBarcode", barCode);
                // 产出状态查询
                if (this.isExist(Box.class, param)) {
                    ret.put(PRODUCED, true);
                } else {
                    ret.put(PRODUCED, false);
                }
            } else if (barCode.startsWith("R")) {
                // 卷产出状态查询
                param.put("rollBarcode", barCode);
                if (this.isExist(Roll.class, param)) {
                    ret.put(PRODUCED, true);
                } else {
                    ret.put(PRODUCED, false);
                }
            } else if (barCode.startsWith("P")) {
                // 部件产出状态查询
                param.put("partBarcode", barCode);
                if (this.isExist(Roll.class, param)) {
                    ret.put(PRODUCED, true);
                } else {
                    ret.put(PRODUCED, false);
                }
            }
            param.clear();
            param.put("barCode", barCode);
            ProductStockState pss = this.findUniqueByMap(ProductStockState.class, param);
            ret.put(STOCK, pss == null ? null : pss.getStockState());
        }
        return ret;
    }

    @Override
    public void doFreeze(String codes) throws Exception {
        String[] code = codes.split(",");
        Map<String, Object> map = new HashMap<>();
        List<ProductStockState> _pss = new ArrayList<>();
        for (String s : code) {
            map.put("barCode", s);
            List<ProductStockState> li = productStockDao.findListByMap(ProductStockState.class, map);
            map.clear();
            map.put("rollBarcode", s);
            TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map);
            ts.setIsLocked(1);
            productStockDao.update(ts);
        }
        productStockDao.update2(_pss.toArray(new ProductStockState[]{}));
    }

    @Override
    public List<Map<String, Object>> findRoll(String code) {
        return productStockDao.findRoll(code);
    }

    @Override
    public List<Map<String, Object>> findTray(String code) {
        return productStockDao.findTray(code);
    }

    @Override
    public List<Map<String, Object>> findMaterial(String code) {
        return productStockDao.findMaterial(code);
    }

    @Override
    public List<Map<String, Object>> findProductInfo(String trayCode, String boxCode, String rollCode) {
        return productStockDao.findProductInfo(trayCode, boxCode, rollCode);
    }


    public void abandon(String code) throws Exception {
        Map<String, Object> param = new HashMap<>();
        RollBarcode rbc;
        PartBarcode pbc;
        boolean packed;
        pbc = mobileService.findBarcodeInfo(BarCodeType.PART, code);
        rbc = mobileService.findBarcodeInfo(BarCodeType.ROLL, code);
        if (rbc == null) {
            throw new Exception("只有卷条码才能作废");
        }
        if (code.startsWith("P") && pbc != null) {
            param.put("partBarcode", code);
            boolean packedToBox = mobileService.isExist(BoxRoll.class, param, true);
            boolean packedToTray = mobileService.isExist(TrayBoxRoll.class, param, true);
            packed = packedToBox || packedToTray;
            pbc.setIsAbandon(1);
            update(pbc);
        } else {
            param.put("rollBarcode", code);
            boolean packedToBox = mobileService.isExist(BoxRoll.class, param, true);
            boolean packedToTray = mobileService.isExist(TrayBoxRoll.class, param, true);
            // 是否已打包
            packed = packedToBox || packedToTray;
            rbc.setIsAbandon(1);
            update(rbc);
            param.clear();
            param.put("rollBarcode", code);
            Roll roll = mobileService.findUniqueByMap(Roll.class, param);
            //修改实际称重的总重量
            if (roll.getRollAutoWeight() != null && roll.getRollAutoWeight() != 0) {
                WeavePlan w = weavePlanService.findById(WeavePlan.class, rbc.getPlanId());
                List<Map<String, Object>> list = weavePlanService.findRollisNoA(rbc.getBatchCode());
                w.setToVoid(Integer.parseInt(list.get(0).get("CC").toString()));
                BigDecimal bg = BigDecimal.valueOf(Double.parseDouble(list.get(0).get("SS").toString()));
                w.setToVoidWeight(bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
                weavePlanService.update(w);
            }
        }
        if (packed) {
            throw new Exception("已被打包，无法作废");
        }
    }

    @Override
    public void abandon(String code, String userId) throws Exception {
        Map<String, Object> param = new HashMap<>();
        RollBarcode rbc;
        PartBarcode pbc;
        boolean packed;

        pbc = mobileService.findBarcodeInfo(BarCodeType.PART, code);
        rbc = mobileService.findBarcodeInfo(BarCodeType.ROLL, code);

        if (rbc == null) {
            throw new Exception("只有卷条码才能作废");
        }

        if (code.startsWith("P") && pbc != null) {
            param.put("partBarcode", code);
            boolean packedToBox = mobileService.isExist(BoxRoll.class, param, true);
            boolean packedToTray = mobileService.isExist(TrayBoxRoll.class, param, true);
            packed = packedToBox || packedToTray;
            pbc.setIsAbandon(1);
            update(pbc);
        } else {
            param.put("rollBarcode", code);
            boolean packedToBox = mobileService.isExist(BoxRoll.class, param, true);
            boolean packedToTray = mobileService.isExist(TrayBoxRoll.class, param, true);
            // 是否已打包
            packed = packedToBox || packedToTray;
            rbc.setIsAbandon(1);
            update(rbc);
            param.clear();
            param.put("rollBarcode", code);
            Roll roll = mobileService.findUniqueByMap(Roll.class, param);
            //修改实际称重的总重量
            if (roll.getRollAutoWeight() != null && roll.getRollAutoWeight() != 0) {
                WeavePlan w = weavePlanService.findById(WeavePlan.class, rbc.getPlanId());
                List<Map<String, Object>> list = weavePlanService.findRollisNoA(rbc.getBatchCode());
                w.setToVoid(Integer.parseInt(list.get(0).get("CC").toString()));
                BigDecimal bg = BigDecimal.valueOf(Double.parseDouble(list.get(0).get("SS").toString()));
                w.setToVoidWeight(bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
                weavePlanService.update(w);
            }
        }
        if (packed) {
            throw new Exception("已被打包，无法作废");
        }
        AbandonBarCode abandonBarCode = new AbandonBarCode();
        abandonBarCode.setBarCode(code);
        abandonBarCode.setUserId(userId);
        abandonBarCode.setCreateDate(new Date());
        save(abandonBarCode);
    }


    @Override
    public Map<String, Object> warehouseDetail(Filter filter, Page page) {
        return productStockDao.warehouseDetail(filter, page);
    }

    @Override
    public Map<String, Object> summaryDetail(Filter filter, Page page) {
        return productStockDao.summaryDetail(filter, page);
    }

    @Override
    public Map<String, Object> comparisonDetail(Filter filter, Page page) {
        return productStockDao.comparisonDetail(filter, page);
    }

    @Override
    public Map<String, Object> getGreigeStockInfo(Filter filter, Page page) {
        return productStockDao.getGreigeStockInfo(filter, page);
    }


    public Map<String, Object> stockView(Filter filter, Page page) throws Exception {
        return productStockDao.stockView(filter, page);
    }

    @Override
    public Map<String, Object> stockViewNew(Filter filter, Page page) throws Exception {
        return productStockDao.stockViewNew(filter, page);
    }

    @Override
    public Map<String, Object> stockViewNewPcj(Filter filter, Page page) throws Exception {
        return productStockDao.stockViewNewPcj(filter, page);
    }


    /**
     * 待入库
     */
    @Override
    public String saveStockPending(ProductInRecord productInRecord, Long overTime) throws Exception {
        String result = "";
        ProductStockState productStockState = new ProductStockState();
        productStockState.setStockState(StockState.STOCKPENDING);
        productStockState.setWarehouseCode(productInRecord.getWarehouseCode());
        productStockState.setWarehousePosCode(productInRecord.getWarehousePosCode());
        productStockState.setOverTime(overTime);
        HashMap<String, Object> map = new HashMap<>();
        productStockState.setBarCode(productInRecord.getBarCode());
        map.put("barCode", productInRecord.getBarCode());
        ProductStockState pss = productStockDao.findUniqueByMap(ProductStockState.class, map);
        Tray t = codeService.findBarCodeReg(BarCodeRegType.TRAY, productInRecord.getBarCode());
        TrayBarCode tbc = codeService.findBarcodeInfo(BarCodeType.TRAY, productInRecord.getBarCode());
        if (pss != null) {
            if (pss.getStockState() == StockState.OUT || pss.getStockState() == StockState.back) {
                pss.setStockState(StockState.STOCKPENDING);
                pss.setWarehouseCode(productStockState.getWarehouseCode());
                pss.setWarehousePosCode(productStockState.getWarehousePosCode());
                map.clear();
                map.put("rollBarcode", pss.getBarCode());
                TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map);
                ts.setState(StockState.STOCKPENDING);
                productStockDao.update(ts);
                productStockDao.update2(pss);
            }
        } else {
            map.clear();
            map.put("rollBarcode", productStockState.getBarCode());
            // 保存卷库存状态表
            productStockDao.save(productStockState);
            TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map);
            if (ts == null) {
                ts = new TotalStatistics();
                ts.setBarcodeType("tray");
                ts.setBatchCode(productStockState.getBarCode());
                ts.setDeviceCode(tbService.trayDeviceCode(productInRecord.getBarCode()));
                ts.setIsLocked(-1);
                ts.setIsPacked(0);
                User u = findById(User.class, t.getPackagingStaff());
                ts.setLoginName(u.getLoginName());
                ProducePlanDetail ppd = findById(ProducePlanDetail.class, tbc.getProducePlanDetailId());
                ProducePlan produce = findById(ProducePlan.class, ppd.getProducePlanId());
                ts.setCONSUMERNAME(ppd.getConsumerName());
                ts.setName(produce.getWorkshop());
                ts.setProducePlanCode(produce.getProducePlanCode());
                ts.setProductLength(ppd.getProductLength());
                ts.setProductModel(ppd.getProductModel());
                ts.setProductName(ppd.getFactoryProductName());
                ts.setProductWeight(ppd.getProductWeight());
                ts.setProductWidth(ppd.getProductWidth());
                ts.setRollBarcode(productInRecord.getBarCode());
                ts.setRollOutputTime(new Date());
                ts.setRollQualityGradeCode("A");
                ts.setRollWeight(t.getWeight());
                ts.setSalesOrderCode(tbc.getSalesOrderCode());
                ts.setState(StockState.STOCKPENDING);
                save(ts);
            } else {
                ts.setState(StockState.STOCKPENDING);
                productStockDao.update(ts);
            }
        }

        String barcode = productInRecord.getBarCode();
        if (barcode.startsWith("T") || barcode.startsWith("P")) {
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("trayBarcode", barcode);
            List<TrayBoxRoll> tbrList = findListByMap(TrayBoxRoll.class, map1);
            for (TrayBoxRoll tbr : tbrList) {
                map1.clear();
                if (tbr.getRollBarcode() != null) {
                    map1.put("rollBarcode", tbr.getRollBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.STOCKPENDING);
                    productStockDao.update(ts);
                    //是托的话存入卷状态
                    ProductStockState pss1 = new ProductStockState();
                    pss1.setBarCode(tbr.getRollBarcode());
                    pss1.setStockState(StockState.STOCKPENDING);
                    pss1.setWarehouseCode(productInRecord.getWarehouseCode());
                    pss1.setWarehousePosCode(productInRecord.getWarehousePosCode());
                    pss1.setOverTime(overTime);
                    productStockDao.save(pss1);
                    //对打托的卷进行入库记录
                    PendingInRecord pdIn = new PendingInRecord();
                    map1.clear();
                    map1.put("rollBarcode", tbr.getRollBarcode());
                    Roll r = productStockDao.findUniqueByMap(Roll.class, map1);
                    if (r != null) {
                        pdIn.setBarCode(tbr.getRollBarcode());
                        pdIn.setInTime(new Date());
                        pdIn.setOperateUserId(r.getRollUserId());
                        pdIn.setWeight(r.getRollWeight());
                        pdIn.setWarehouseCode(productInRecord.getWarehouseCode());
                        pdIn.setWarehousePosCode(productInRecord.getWarehousePosCode());
                        pdIn.setInBankSource(productInRecord.getInBankSource());
                        pdIn.setSyncState(1);
                        productStockDao.save(pdIn);
                    }
                } else if (tbr.getPartBarcode() != null) {
                    map1.put("rollBarcode", tbr.getPartBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.STOCKPENDING);
                    productStockDao.update(ts);
                } else {
                    map1.put("rollBarcode", tbr.getBoxBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.STOCKPENDING);
                    productStockDao.update(ts);
                    // 获取盒卷关系信息，修改在库状态
                    map1.clear();
                    map1.put("boxBarcode", tbr.getBoxBarcode());
                    List<BoxRoll> brList = productStockDao.findListByMap(BoxRoll.class, map1);
                    for (BoxRoll br : brList) {
                        map1.clear();
                        if (br.getRollBarcode() != null) {
                            map1.put("rollBarcode", br.getRollBarcode());
                            TotalStatistics ts1 = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                            ts.setState(StockState.STOCKPENDING);
                            productStockDao.update(ts1);
                        } else if (br.getPartBarcode() != null) {
                            map1.put("rollBarcode", br.getPartBarcode());
                            TotalStatistics ts1 = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                            ts.setState(StockState.STOCKPENDING);
                            productStockDao.update(ts1);
                        }
                    }
                }
            }
        }

        List<ProductStockTran> st = new ArrayList<>();
        productInRecord.setInTime(new Date());
        PendingInRecord record = productStockDao.findOne(PendingInRecord.class, "barCode", productInRecord.getBarCode());
        ProductStockTran productStockTran = new ProductStockTran();
        productStockTran.setInTime(productInRecord.getInTime());
        productStockTran.setBarCode(productInRecord.getBarCode());
        productStockTran.setNewWarehouseCode(productInRecord.getWarehouseCode());
        productStockTran.setNewWarehousePosCode(productInRecord.getWarehousePosCode());
        productStockTran.setOperateUserId(productInRecord.getOperateUserId());
        productStockTran.setStatus(StockState.STOCKPENDING);
        st.add(productStockTran);
        PendingInRecord pdIn1 = new PendingInRecord();
        if (record == null) {
            pdIn1.setBarCode(productInRecord.getBarCode());
            pdIn1.setInTime(productInRecord.getInTime());
            pdIn1.setOperateUserId(productInRecord.getOperateUserId());
            pdIn1.setWeight(productInRecord.getWeight());
            pdIn1.setWarehouseCode(productInRecord.getWarehouseCode());
            pdIn1.setWarehousePosCode(productInRecord.getWarehousePosCode());
            pdIn1.setInBankSource(productInRecord.getInBankSource());
            pdIn1.setSyncState(1);
            productStockDao.save(pdIn1);
            productStockDao.save(st.toArray(new ProductStockTran[]{}));
        } else {
            record.setInTime(productInRecord.getInTime());
            record.setOperateUserId(productInRecord.getOperateUserId());
            record.setWarehouseCode(productInRecord.getWarehouseCode());
            record.setWarehousePosCode(productInRecord.getWarehousePosCode());
            record.setWeight(productInRecord.getWeight());
            productStockDao.update(record);
        }
        return result;
    }


    /**
     * 新入库
     */
    @Override
    public String pIn(ProductInRecord productInRecord, Long overTime) throws Exception {
        String result = "";
        productInRecord.setInTime(new Date());
        HashMap<String, Object> map = new HashMap<>();
        map.put("barCode", productInRecord.getBarCode());
        ProductStockState pss = productStockDao.findUniqueByMap(ProductStockState.class, map);
        Tray t = codeService.findBarCodeReg(BarCodeRegType.TRAY, productInRecord.getBarCode());
        if (pss != null) {
            if (pss.getStockState() == StockState.STOCKPENDING) {
                pss.setStockState(StockState.IN);
                pss.setWarehouseCode(productInRecord.getWarehouseCode());
                pss.setWarehousePosCode(productInRecord.getWarehousePosCode());
                map.clear();
                map.put("rollBarcode", pss.getBarCode());
                TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map);
                ts.setState(StockState.IN);
                ts.setInTime(productInRecord.getInTime());
                ts.setLoginName(ts.getLoginName());
                productStockDao.update(ts);
                productStockDao.update2(pss);
            }
        } else {
            map.clear();
            map.put("rollBarcode", productInRecord.getBarCode());
            TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map);
            if (ts != null) {
                ts.setLoginName(ts.getLoginName());
                ts.setRollOutputTime(new Date());
                ts.setState(StockState.IN);
                ts.setInTime(productInRecord.getInTime());
                productStockDao.update(ts);
            }
        }
        String barcode = productInRecord.getBarCode();
        if (barcode.startsWith("T") || barcode.startsWith("P")) {
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("trayBarcode", barcode);
            List<TrayBoxRoll> tbrList = findListByMap(TrayBoxRoll.class, map1);
            for (TrayBoxRoll tbr : tbrList) {
                map1.clear();
                if (tbr.getRollBarcode() != null) {
                    //如果为空则循环跳过去
                    if (tbr.getRollBarcode().equals("")) continue;
                    map1.clear();
                    map1.put("rollBarcode", tbr.getRollBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.IN);
                    ts.setInTime(productInRecord.getInTime());
                    productStockDao.update(ts);
                    //是托的话更新卷状态
                    List<ProductStockState> listProductStockState = productStockDao.find(ProductStockState.class, "barcode", tbr.getRollBarcode());
                    if (listProductStockState != null && listProductStockState.size() > 0) {
                        listProductStockState.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
                        ProductStockState pstockstate = listProductStockState.get(0);
                        pstockstate.setStockState(StockState.IN);
                        pstockstate.setWarehouseCode(productInRecord.getWarehouseCode());
                        pstockstate.setWarehousePosCode(productInRecord.getWarehousePosCode());
                        productStockDao.update(pstockstate);
                    }

                    map1.clear();
                    map1.put("rollBarcode", tbr.getRollBarcode());
                    Roll roll = productStockDao.findUniqueByMap(Roll.class, map1);
                    //对打托的卷进行入库记录
                    List<PendingInRecord> listProductInRecord = productStockDao.find(PendingInRecord.class, "barcode", tbr.getRollBarcode());
                    if (roll != null && listProductInRecord != null && listProductInRecord.size() > 0) {
                        listProductInRecord.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
                        PendingInRecord pdIn = listProductInRecord.get(0);
                        ProductInRecord pIn = new ProductInRecord();
                        BeanUtils.copyProperties(pdIn, pIn);
                        pIn.setInTime(new Date());
                        pIn.setOperateUserId(roll.getRollUserId());
                        pIn.setWarehouseCode(productInRecord.getWarehouseCode());
                        pIn.setWarehousePosCode(productInRecord.getWarehousePosCode());
                        productStockDao.save(pIn);
                        //更新卷时间
                        roll.setInTime(pIn.getInTime());
                        productStockDao.update(roll);
                    }

                } else if (tbr.getPartBarcode() != null) {
                    //更新卷时间
                    map1.put("rollBarcode", tbr.getPartBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.IN);
                    ts.setInTime(productInRecord.getInTime());
                    productStockDao.update(ts);
                } else {
                    map1.put("rollBarcode", tbr.getBoxBarcode());
                    TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                    ts.setState(StockState.IN);
                    ts.setInTime(productInRecord.getInTime());
                    productStockDao.update(ts);
                    // 获取盒卷关系信息，修改在库状态
                    map1.clear();
                    map1.put("boxBarcode", tbr.getBoxBarcode());
                    List<BoxRoll> brList = productStockDao.findListByMap(BoxRoll.class, map1);
                    for (BoxRoll br : brList) {
                        map1.clear();
                        if (br.getRollBarcode() != null) {
                            map1.put("rollBarcode", br.getRollBarcode());
                            TotalStatistics ts1 = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                            ts1.setState(StockState.IN);
                            ts1.setInTime(productInRecord.getInTime());
                            productStockDao.update(ts1);
                        } else if (br.getPartBarcode() != null) {
                            map1.put("rollBarcode", br.getPartBarcode());
                            TotalStatistics ts1 = productStockDao.findUniqueByMap(TotalStatistics.class, map1);
                            ts1.setState(StockState.IN);
                            ts1.setInTime(productInRecord.getInTime());
                            productStockDao.update(ts1);
                        }
                    }
                }
            }
        }

        ProductInRecord record = productStockDao.findOne(ProductInRecord.class, "barCode", productInRecord.getBarCode());
        if (record == null) {
            productStockDao.save(productInRecord);
        } else {
            record.setInTime(productInRecord.getInTime());
            record.setOperateUserId(productInRecord.getOperateUserId());
            record.setWarehouseCode(productInRecord.getWarehouseCode());
            record.setWarehousePosCode(productInRecord.getWarehousePosCode());
            record.setWeight(productInRecord.getWeight());
            productStockDao.update(record);
        }

        //部件条码直接入库
        if (barcode.startsWith("PCJ")) {
            Roll roll = findOne(Roll.class, "partBarcode", barcode);
            if (roll != null) {
                roll.setInTime(productInRecord.getInTime());
                productStockDao.update(roll);
            }
        }
        return result;
    }

    /**
     * 成品托变成在途库
     *
     * @param code             编码
     * @param warehousecode    终点仓库编码
     * @param logisticscompany 货运
     * @param plate            车牌
     */
    public String POnTheWay(String code, String warehousecode, String logisticscompany, String plate, long loginid) throws Exception {
        String result = "";
        int onTheWayStockState = StockState.OnTheWay;
        //保存出库调拨单逻辑
        deliveryonthewayPlanService.saveDeliveryPlan(code, warehousecode, logisticscompany, plate, loginid);
        //更新在途状态逻辑
        String[] codes = code.split(",");
        for (String barcode : codes) {
            Map<String, Object> map = new HashMap<>();
            map.put("barCode", barcode);
            ProductStockState pss = productStockDao.findUniqueByMap(ProductStockState.class, map);
            if (pss != null) {
                action(barcode, onTheWayStockState);
            }
        }
        return result;
    }


    /**
     * 新移库
     *
     * @param stockMove String code
     */
    public String pMove(StockMove stockMove, String code) throws Exception {
        String result = "";
        String[] codes = code.split(",");
        List<StockMove> st = new ArrayList<>();
        List<ProductStockState> pss = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("WAREHOUSECODE", stockMove.getNewWarehouseCode());
        Warehouse _newwarehouse = warehouseDao.findUniqueByMap(Warehouse.class, map);
        if (_newwarehouse == null) {
            return "未找到仓库编码：" + stockMove.getNewWarehouseCode();
        }

        for (String barCode : codes) {
            StockMove _stockMove = new StockMove();
            map.clear();
            map.put("barCode", barCode);
            // 根据每个条码去库存表查询原来的库位库房代码
            List<ProductStockState> listProductStockState = this.findListByMap(ProductStockState.class, map);
            if (listProductStockState != null && listProductStockState.size() > 0) {
                listProductStockState.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
                ProductStockState _productStockState = listProductStockState.get(0);
                if (_productStockState.getWarehouseCode().equals(stockMove.getNewWarehouseCode()) && _productStockState.getWarehousePosCode().equals(stockMove.getNewWarehousePosCode())) {
                    if (_productStockState.getStockState() != StockState.OnTheWay) {
                        result += barCode + "库位位置没变";
                        continue;
                    }
                }
                map.clear();
                map.put("WAREHOUSECODE", _productStockState.getWarehouseCode());
                //如果要移入到外库,外库可以直接到外库
                if (barCode.startsWith("PCJ")) {
                    PartBarcode partBarcode = findOne(PartBarcode.class, "barcode", barCode);
                    _stockMove.setProducePlanDetailId(partBarcode == null ? null : partBarcode.getProducePlanDetailId());
                } else {
                    TrayBarCode trayBarCode = findOne(TrayBarCode.class, "barcode", barCode);
                    _stockMove.setProducePlanDetailId(trayBarCode == null ? null : trayBarCode.getProducePlanDetailId());
                }
                // 保存信息到库存移库表
                _stockMove.setMoveTime(new Date());
                _stockMove.setBarcode(barCode);
                _stockMove.setNewWarehouseCode(stockMove.getNewWarehouseCode());
                _stockMove.setNewWarehousePosCode(stockMove.getNewWarehousePosCode());
                _stockMove.setOriginWarehouseCode(_productStockState.getWarehouseCode());
                _stockMove.setOriginWarehousePosCode(_productStockState.getWarehousePosCode());
                _stockMove.setMoveUserId(stockMove.getMoveUserId());
                st.add(_stockMove);
                _productStockState.setWarehouseCode(stockMove.getNewWarehouseCode());
                _productStockState.setWarehousePosCode(stockMove.getNewWarehousePosCode());
                if (_productStockState.getStockState() == StockState.OUT) {
                    _productStockState.setStockState(StockState.IN);
                } else if (_productStockState.getStockState() == StockState.OnTheWay || _productStockState.getStockState() == StockState.Pick) {
                    _productStockState.setStockState(StockState.IN);
                    action(barCode, StockState.IN);
                }
                pss.add(_productStockState);
            }
        }
        productStockDao.save(st.toArray(new StockMove[]{}));
        productStockDao.update2(pss.toArray(new ProductStockState[]{}));
        return result;
    }


    /**
     * 新出库
     *
     * @param codes
     * @param UserId
     * @param packingNum
     * @param plate
     * @param boxNumber
     * @param count
     * @param isfinished
     * @return
     * @throws Exception
     */
    @Override
    public String pOut(String codes, Long UserId, String packingNum, String plate, String boxNumber, Double count, int isfinished) throws Exception {
        ProductOutOrder poo = new ProductOutOrder();
        poo.setOutTime(new Date());
        HashMap<String, Object> map = new HashMap<>();
        map.put("packingNumber", packingNum);
        DeliveryPlanSalesOrders dpso = findUniqueByMap(DeliveryPlanSalesOrders.class, map);
        map.clear();
        Long deliveryId;
        if (dpso == null) {
            return "未找到出货计划";
        } else {
            deliveryId = dpso.getDeliveryId();
        }
        DeliveryPlan dp = productStockDao.findById(DeliveryPlan.class, deliveryId);
        String result = "";
        String[] code = codes.split(",");
        List<ProductStockState> _pss = new ArrayList<>();
        List<ProductOutRecord> _listporsave = new ArrayList<>();
        List<ProductOutRecord> _listporupdate = new ArrayList<>();
        map.clear();
        for (String barcode : code) {
            if (totalStatisticsService.isFrozen(barcode) == ProductState.FROZEN) {
                return barcode + "已冻结，禁止出库";
            }
            map.clear();
            map.put("barCode", barcode);
            List<ProductStockState> listProductStockState = productStockDao.findListByMap(ProductStockState.class, map);
            map.clear();
            map.put("trayBarcode", barcode);
            for (ProductStockState pss : listProductStockState) {
                if (pss == null) {
                    return "无库存记录，禁止出库";
                }
                if (pss.getStockState() == StockState.OUT) {
                    return pss.getBarCode() + "已出库，禁止重复出库";
                }
                map.clear();
                map.put("rollBarcode", pss.getBarCode());
                TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map);
                ts.setState(StockState.OUT);
                ts.setOutTime(poo.getOutTime());
                map.clear();
                map.put("barcode", pss.getBarCode());
                Long salesProductId = 0L;
                TrayBarCode trayBarCode = findUniqueByMap(TrayBarCode.class, map);
                if (trayBarCode != null) {
                    salesProductId = trayBarCode.getSalesProductId();
                }
                PartBarcode partBarcode = findUniqueByMap(PartBarcode.class, map);
                if (partBarcode != null) {
                    salesProductId = partBarcode.getSalesProductId();
                }
                FinishedProduct fp = findById(FinishedProduct.class, salesProductId);
                pss.setStockState(StockState.OUT);
                _pss.add(pss);
                ProductOutRecord por = productStockDao.findOne(ProductOutRecord.class, "barCode", barcode);
                if (por == null) {
                    por = new ProductOutRecord();
                }
                por.setPackingNumber(packingNum);
                por.setProductConsumerName(fp.getConsumerProductName());
                por.setProductFactoryName(fp.getFactoryProductName());
                por.setProductModel(fp.getProductModel());
                por.setDeliveryId(Integer.valueOf(deliveryId.toString()));
                if (fp.getProductWidth() != null) {
                    por.setWidth(fp.getProductWidth());
                }
                if (fp.getProductRollLength() != null) {
                    por.setLength(fp.getProductRollLength());
                }
                por.setWarehouseCode(pss.getWarehouseCode());
                por.setWarehousePosCode(pss.getWarehousePosCode());
                por.setBarCode(pss.getBarCode());
                por.setOutTime(new Date());

                map.clear();
                map.put("trayBarcode", pss.getBarCode());
                List<Tray> tray = productStockDao.findListByMap(Tray.class, map);
                if (tray.size() > 0) {
                    por.setWeight(tray.get(tray.size() - 1).getWeight());
                }
                map.clear();
                map.put("partBarcode", pss.getBarCode());
                List<Roll> rolls = productStockDao.findListByMap(Roll.class, map);
                if (rolls.size() > 0) {
                    por.setWeight(rolls.get(rolls.size() - 1).getRollWeight());
                }
                por.setOperateUserId(UserId);
                por.setDeliveryCode(dp.getDeliveryCode());
                if (por.getId() == null) {
                    _listporsave.add(por);
                } else {
                    _listporupdate.add(por);
                }
                productStockDao.update(ts);
            }
            HashMap<String, Object> trayMap = new HashMap<>();
            trayMap.put("trayBarcode", barcode);
            List<TrayBoxRoll> tbrList = findListByMap(TrayBoxRoll.class, trayMap);
            for (TrayBoxRoll tbr : tbrList) {
                Map<String, Object> tsMap = new HashMap<>();
                Map<String, Object> psMap = new HashMap<>();
                if (tbr.getRollBarcode() != null) {
                    tsMap.put("rollBarcode", tbr.getRollBarcode());
                    psMap.put("barCode", tbr.getRollBarcode());
                } else if (tbr.getPartBarcode() != null) {
                    tsMap.put("rollBarcode", tbr.getPartBarcode());
                    psMap.put("barCode", tbr.getPartBarcode());
                } else {
                    tsMap.put("rollBarcode", tbr.getBoxBarcode());
                    psMap.put("barCode", tbr.getBoxBarcode());
                    // 获取盒卷关系信息，修改在库状态
                    Map<String, Object> boxMap = new HashMap<>();
                    boxMap.put("boxBarcode", tbr.getBoxBarcode());
                    List<BoxRoll> brList = productStockDao.findListByMap(BoxRoll.class, boxMap);
                    for (BoxRoll br : brList) {
                        Map<String, Object> rollMap = new HashMap<>();
                        if (br.getRollBarcode() != null) {
                            rollMap.put("rollBarcode", br.getRollBarcode());
                        }
                        if (br.getPartBarcode() != null) {
                            rollMap.put("rollBarcode", br.getPartBarcode());
                        }
                        TotalStatistics rollTS = productStockDao.findUniqueByMap(TotalStatistics.class, rollMap);
                        rollTS.setState(StockState.OUT);
                        rollTS.setOutTime(poo.getOutTime());
                        productStockDao.update(rollTS);
                    }
                }
                TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, tsMap);
                ts.setState(StockState.OUT);
                ts.setOutTime(poo.getOutTime());
                productStockDao.update(ts);
                List<ProductStockState> states = productStockDao.findListByMap(ProductStockState.class, psMap);
                for (ProductStockState state : states) {
                    state.setStockState(StockState.OUT);
                    _pss.add(state);
                }
            }
        }

        dpso.setRealBoxNumber(boxNumber);
        dpso.setPlate(plate);
        dpso.setIsFinished(isfinished);
        productStockDao.update(dpso);
        map.clear();
        map.put("deliveryId", deliveryId);
        List<DeliveryPlanSalesOrders> dpsos = productStockDao.findListByMap(DeliveryPlanSalesOrders.class, map);
        boolean boolisFinished = true;
        for (DeliveryPlanSalesOrders d : dpsos) {
            if (d.getIsFinished() == null || d.getIsFinished() == 0) {
                boolisFinished = false;
                break;
            }
        }
        if (boolisFinished) {
            // 设置该出库计划已完成
            dp.setIsComplete(1);
            productStockDao.update2(dp);
        }
        poo.setBoxNumber(dpso.getBoxNumber());
        poo.setConsumerName(dp.getDeliveryTargetCompany());
        poo.setCount(count);
        poo.setDeliveryCode(dp.getDeliveryCode());
        poo.setPn(dpso.getPn());
        poo.setLadingCode(dpso.getLadingCode());
        poo.setLogisticsCompany(dp.getLogisticsCompany());
        poo.setPackingNumber(dpso.getPackingNumber());
        poo.setOperateUserId(dp.getDeliveryBizUserId());
        poo.setPlate(dpso.getPlate());
        poo.setRealBoxNumber(dpso.getRealBoxNumber());
        poo.setSerialNumber(dpso.getSerialNumber());
        poo.setDeliveryId(Long.valueOf(deliveryId.toString()));
        poo.setSampleInformation(dp.getSampleInformation());
        productStockDao.save(poo);
        productStockDao.update2(_pss.toArray(new ProductStockState[]{}));

        _listporsave.forEach(p -> p.setOutOrderId(poo.getId()));//季晓龙-add-2020-11-03 增加主表ID
        _listporupdate.forEach(p -> p.setOutOrderId(poo.getId()));

        productStockDao.save(_listporsave.toArray(new ProductOutRecord[]{}));
        productStockDao.update2(_listporupdate.toArray(new ProductOutRecord[]{}));
        return result;
    }


    /**
     * 回库
     */
    public String pBack(ProductStockTran productStockTran, String code) throws Exception {
        String result = "";
        String[] codes = code.split(",");
        List<ProductStockTran> st = new ArrayList<>();
        List<ProductStockState> pss = new ArrayList<>();
        for (String barCode : codes) {
            // 根据每个条码去库存表查询原来的库位库房代码
            ProductStockState productStockState = productStockDao.findOne(ProductStockState.class, "barCode", barCode);
            if (productStockState.getStockState() != StockState.OUT) {
                result += barCode + "非出库状态;";
                continue;
            }
            productStockState.setWarehouseCode(productStockTran.getNewWarehouseCode());
            productStockState.setWarehousePosCode(productStockTran.getNewWarehousePosCode());
            productStockState.setStockState(StockState.IN);
            // 保存信息到库存移库表
            ProductStockTran _productStockTran = new ProductStockTran();
            _productStockTran.setInTime(new Date());
            _productStockTran.setBarCode(barCode);
            _productStockTran.setNewWarehouseCode(productStockTran.getNewWarehouseCode());
            _productStockTran.setNewWarehousePosCode(productStockTran.getNewWarehousePosCode());
            _productStockTran.setOriginWarehouseCode(productStockState.getWarehouseCode());
            _productStockTran.setOriginWarehousePosCode(productStockState.getWarehousePosCode());
            _productStockTran.setOperateUserId(productStockTran.getOperateUserId());
            _productStockTran.setStatus(StockState.IN);
            st.add(_productStockTran);
            action(barCode, StockState.IN);
            pss.add(productStockState);
        }
        productStockDao.save(st.toArray(new ProductStockTran[]{}));
        productStockDao.update2(pss.toArray(new ProductStockState[]{}));
        return result;
    }

    @Override
    public List<Map<String, Object>> getMoveInfoBybarcode(String barcode) throws SQLTemplateException {
        return productStockDao.getMoveInfoBybarcode(barcode);
    }

    //在途，移库 根据托条码，更新托，托下面的卷，部件的ProductStockState的状态。
    public boolean action(String barcode, int stockState) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        List<ProductStockState> listpss = new ArrayList<>();
        List<TotalStatistics> listts = new ArrayList<>();
        map.put("barCode", barcode);
        ProductStockState pss = productStockDao.findUniqueByMap(ProductStockState.class, map);
        if (pss != null) {
            pss.setStockState(stockState);
            listpss.add(pss);
        }
        map.clear();
        map.put("rollBarcode", pss.getBarCode());
        TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map);
        if (ts != null) {
            ts.setState(stockState);
            listts.add(ts);
        }
        if (barcode.startsWith("T") || barcode.startsWith("P")) {
            map.clear();
            map.put("trayBarcode", barcode);
            List<TrayBoxRoll> tbrList = findListByMap(TrayBoxRoll.class, map);//找到托箱关系
            for (TrayBoxRoll tbr : tbrList) {
                map.clear();
                if (tbr.getRollBarcode() != null) {
                    if (tbr.getRollBarcode().equals("")) continue;//如果为空则循环跳过去
                    //是托的话更新卷状态
                    List<ProductStockState> listProductStockState = productStockDao.find(ProductStockState.class, "barcode", tbr.getRollBarcode());
                    if (listProductStockState != null && listProductStockState.size() > 0) {
                        listProductStockState.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
                        ProductStockState pstockstate = listProductStockState.get(0);
                        pstockstate.setStockState(stockState);
                        listpss.add(pstockstate);
                    }
                    map.clear();
                    map.put("rollBarcode", tbr.getRollBarcode());
                    TotalStatistics ts1 = productStockDao.findUniqueByMap(TotalStatistics.class, map);//找到托下面的卷
                    if (ts1 != null) {
                        ts1.setState(stockState);
                        listts.add(ts1);
                    }
                } else if (tbr.getPartBarcode() != null) {
                    map.put("rollBarcode", tbr.getPartBarcode());
                    TotalStatistics ts2 = productStockDao.findUniqueByMap(TotalStatistics.class, map);
                    if (ts2 != null) {
                        ts2.setState(stockState);
                        listts.add(ts2);
                    }
                } else {
                    map.put("rollBarcode", tbr.getBoxBarcode());
                    TotalStatistics ts3 = productStockDao.findUniqueByMap(TotalStatistics.class, map);
                    if (ts3 != null) {
                        ts3.setState(stockState);
                        listts.add(ts3);
                    }
                    // 获取盒卷关系信息，修改在库状态
                    map.clear();
                    map.put("boxBarcode", tbr.getBoxBarcode());
                    List<BoxRoll> brList = productStockDao.findListByMap(BoxRoll.class, map);
                    for (BoxRoll br : brList) {
                        map.clear();
                        if (br.getRollBarcode() != null) {
                            map.put("rollBarcode", br.getRollBarcode());
                            TotalStatistics ts4 = productStockDao.findUniqueByMap(TotalStatistics.class, map);
                            if (ts4 != null) {
                                ts4.setState(stockState);
                                listts.add(ts4);
                            }
                        } else if (br.getPartBarcode() != null) {
                            map.put("rollBarcode", br.getPartBarcode());
                            TotalStatistics ts5 = productStockDao.findUniqueByMap(TotalStatistics.class, map);
                            if (ts5 != null) {
                                ts5.setState(stockState);
                                listts.add(ts5);
                            }
                        }
                    }
                }
            }
        }
        productStockDao.update2(listpss.toArray(new ProductStockState[]{}));
        productStockDao.update2(listts.toArray(new TotalStatistics[]{}));
        return true;
    }

    //在途，移库 根据托条码，更新托，托下面的卷，部件的ProductStockState的状态。
    public boolean actionBarCode(String barcode, int stockState, String warehouseCode, String warehousePosCode, String inBankSource) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        List<ProductStockState> listpss = new ArrayList<>();
        List<TotalStatistics> listts = new ArrayList<>();
        map.put("barCode", barcode);
        ProductStockState pss = productStockDao.findUniqueByMap(ProductStockState.class, map);
        if (pss != null) {
            pss.setStockState(stockState);
            listpss.add(pss);
        }
        map.clear();
        map.put("rollBarcode", pss.getBarCode());
        TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map);
        if (ts != null) {
            ts.setState(stockState);
            listts.add(ts);
        }
        if (barcode.startsWith("T") || barcode.startsWith("P")) {
            map.clear();
            map.put("trayBarcode", barcode);
            //找到托箱关系
            List<TrayBoxRoll> tbrList = findListByMap(TrayBoxRoll.class, map);
            for (TrayBoxRoll tbr : tbrList) {
                map.clear();
                if (tbr.getRollBarcode() != null) {
                    //如果为空则循环跳过去
                    if (tbr.getRollBarcode().equals("")) continue;
                    //是托的话更新卷状态
                    List<ProductStockState> listProductStockState = productStockDao.find(ProductStockState.class, "barcode", tbr.getRollBarcode());
                    if (listProductStockState != null && listProductStockState.size() > 0) {
                        listProductStockState.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
                        ProductStockState pstockstate = listProductStockState.get(0);
                        pstockstate.setStockState(stockState);
                        listpss.add(pstockstate);
                    } else {
                        //是托的话存入卷状态
                        ProductStockState pss1 = new ProductStockState();
                        pss1.setBarCode(tbr.getRollBarcode());
                        pss1.setStockState(stockState);
                        pss1.setWarehouseCode(warehouseCode);
                        pss1.setWarehousePosCode(warehousePosCode);
                        productStockDao.save(pss1);
                    }
                    map.clear();
                    map.put("rollBarcode", tbr.getRollBarcode());
                    TotalStatistics ts1 = productStockDao.findUniqueByMap(TotalStatistics.class, map);//找到托下面的卷
                    if (ts1 != null) {
                        ts1.setState(stockState);
                        listts.add(ts1);
                    }
                    //对打托的卷进行入库记录
                    map.clear();
                    map.put("rollBarcode", tbr.getRollBarcode());
                    Roll r = productStockDao.findUniqueByMap(Roll.class, map);
                    if (r == null) {
                        map.clear();
                        map.put("partBarcode", tbr.getRollBarcode());
                        r = productStockDao.findUniqueByMap(Roll.class, map);
                    }

                    switch (stockState) {
                        case StockState.STOCKPENDING -> {
                            PendingInRecord pdIn = new PendingInRecord();
                            pdIn.setBarCode(tbr.getRollBarcode());
                            pdIn.setInTime(new Date());
                            pdIn.setOperateUserId(r.getRollUserId());
                            pdIn.setWeight(r.getRollWeight());
                            pdIn.setWarehouseCode(warehouseCode);
                            pdIn.setWarehousePosCode(warehousePosCode);
                            pdIn.setInBankSource(inBankSource);
                            pdIn.setSyncState(1);
                            productStockDao.save(pdIn);
                        }
                        case StockState.IN -> {
                            ProductInRecord productInRecord = new ProductInRecord();
                            productInRecord.setBarCode(tbr.getRollBarcode());
                            productInRecord.setInTime(new Date());
                            productInRecord.setOperateUserId(r.getRollUserId());
                            productInRecord.setWeight(r.getRollWeight());
                            productInRecord.setWarehouseCode(warehouseCode);
                            productInRecord.setWarehousePosCode(warehousePosCode);
                            productInRecord.setInBankSource(inBankSource);
                            productInRecord.setSyncState(1);
                            productStockDao.save(productInRecord);
                        }
                        case StockState.Pick -> {
                            FabricPickRecord fabricPickRecord = new FabricPickRecord();
                            fabricPickRecord.setBarCode(tbr.getRollBarcode());
                            fabricPickRecord.setInTime(new Date());
                            fabricPickRecord.setOperateUserId(r.getRollUserId());
                            fabricPickRecord.setWeight(r.getRollWeight());
                            fabricPickRecord.setWarehouseCode(warehouseCode);
                            fabricPickRecord.setWarehousePosCode(warehousePosCode);
                            fabricPickRecord.setInBankSource(inBankSource);
                            fabricPickRecord.setSyncState(1);
                            productStockDao.save(fabricPickRecord);
                        }
                    }
                } else if (tbr.getPartBarcode() != null) {
                    map.put("rollBarcode", tbr.getPartBarcode());
                    TotalStatistics ts2 = productStockDao.findUniqueByMap(TotalStatistics.class, map);
                    if (ts2 != null) {
                        ts2.setState(stockState);
                        listts.add(ts2);
                    }
                } else {
                    map.put("rollBarcode", tbr.getBoxBarcode());
                    TotalStatistics ts3 = productStockDao.findUniqueByMap(TotalStatistics.class, map);
                    if (ts3 != null) {
                        ts3.setState(stockState);
                        listts.add(ts3);
                    }
                    // 获取盒卷关系信息，修改在库状态
                    map.clear();
                    map.put("boxBarcode", tbr.getBoxBarcode());
                    List<BoxRoll> brList = productStockDao.findListByMap(BoxRoll.class, map);
                    for (BoxRoll br : brList) {
                        map.clear();
                        if (br.getRollBarcode() != null) {
                            map.put("rollBarcode", br.getRollBarcode());
                            TotalStatistics ts4 = productStockDao.findUniqueByMap(TotalStatistics.class, map);
                            if (ts4 != null) {
                                ts4.setState(stockState);
                                listts.add(ts4);
                            }
                        } else if (br.getPartBarcode() != null) {
                            map.put("rollBarcode", br.getPartBarcode());
                            TotalStatistics ts5 = productStockDao.findUniqueByMap(TotalStatistics.class, map);
                            if (ts5 != null) {
                                ts5.setState(stockState);
                                listts.add(ts5);
                            }
                        }
                    }
                }
            }
        }
        productStockDao.update2(listpss.toArray(new ProductStockState[]{}));
        productStockDao.update2(listts.toArray(new TotalStatistics[]{}));
        return true;
    }


    public String GetStockState(int stockState) {
        return switch (stockState) {
            case -1 -> "不在库";
            case 1 -> "在库";
            case 2 -> "待入库";
            case 3 -> "在途";
            case 4 -> "领料";
            default -> "未知";
        };
    }

    @Override
    public Map<String, Object> findPageInfoMoveList(Filter filter, Page page) {
        return productStockDao.findPageInfoMoveList(filter, page);
    }

    /**
     * 查询仓库中文名称
     */
    @Override
    public String pwarhourseName(String warehouseCode) {
        Warehouse warehouse = productStockDao.findOne(Warehouse.class, "warehouseCode", warehouseCode);
        String warehouseName = "";
        if (null != warehouse) {
            warehouseName = warehouse.getWarehouseName();
        }
        return warehouseName;
    }

    @Override
    public String pendingWarhourse(String salesOrderCode, String batchCode, String productModel) {
        List<Map<String, Object>> findWarhourse = productStockDao.findPendingWarhourse(salesOrderCode, batchCode, productModel);
        String warehouse = "";
        if (findWarhourse.size() > 0) {
            Object wcode = findWarhourse.get(0).get("WAREHOUSENAME");
            Object wposcode = findWarhourse.get(0).get("WAREHOUSEPOSCODE");
            warehouse = wcode + "（" + wposcode + "）";
            return warehouse;
        }
        return warehouse;
    }


    /**
     * 新成品批量入库
     */
    public String pIns(List<ProductInRecord> productInRecordlist) throws Exception {
        StringBuilder result = new StringBuilder();
        List<ProductStockState> pss = new ArrayList<>();
        for (ProductInRecord productInRecord : productInRecordlist) {
            String barCode = productInRecord.getBarCode();
            productInRecord.setInTime(new Date());
            List<ProductStockState> listProductStockState = productStockDao.find(ProductStockState.class, "barcode", barCode);
            if (listProductStockState != null && listProductStockState.size() > 0) {
                listProductStockState.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
            } else {
                result.append(barCode).append("未找到库存状态!");
                continue;
            }

            ProductStockState _productStockState = listProductStockState.get(0);
            if (_productStockState.getWarehouseCode().equals(productInRecord.getWarehouseCode()) && _productStockState.getWarehousePosCode().equals(productInRecord.getWarehousePosCode())) {
                result.append(barCode).append("库位位置没变!");
                continue;
            }
            _productStockState.setWarehouseCode(productInRecord.getWarehouseCode());
            _productStockState.setWarehousePosCode(productInRecord.getWarehousePosCode());
            _productStockState.setStockState(StockState.IN);
            pss.add(_productStockState);
            actionBarCode(barCode, StockState.IN, productInRecord.getWarehouseCode(), productInRecord.getWarehousePosCode(), productInRecord.getInBankSource());
            List<ProductInRecord> listProductInRecord = productStockDao.find(ProductInRecord.class, "barcode", barCode);
            if (listProductInRecord != null && listProductInRecord.size() > 0) {
                listProductInRecord.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
                ProductInRecord record = listProductInRecord.get(0);
                record.setInTime(productInRecord.getInTime());
                record.setOperateUserId(productInRecord.getOperateUserId());
                record.setWarehouseCode(productInRecord.getWarehouseCode());
                record.setWarehousePosCode(productInRecord.getWarehousePosCode());
                record.setWeight(productInRecord.getWeight());
                productStockDao.update(record);
            } else {
                productStockDao.save(productInRecord);
            }
            //部件条码直接入库
            if (barCode.startsWith("PCJ")) {
                Roll roll = findOne(Roll.class, "partBarcode", barCode);
                if (roll != null) {
                    roll.setInTime(productInRecord.getInTime());
                    productStockDao.update(roll);
                }
            }
        }
        productStockDao.update2(pss.toArray(new ProductStockState[]{}));
        return result.toString();
    }

    /**
     * 新胚布批量入库
     */
    public String pbIns(List<ProductInRecord> productInRecordlist) throws Exception {
        StringBuilder result = new StringBuilder();
        int intStockState = StockState.IN;
        List<ProductStockState> pss = new ArrayList<>();
        for (ProductInRecord productInRecord : productInRecordlist) {
            HashMap<String, Object> map = new HashMap<>();
            String barCode = productInRecord.getBarCode();
            productInRecord.setInTime(new Date());
            List<ProductStockState> listProductStockState = productStockDao.find(ProductStockState.class, "barcode", barCode);
            if (listProductStockState != null && listProductStockState.size() > 0) {
                listProductStockState.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
            } else {
                result.append(barCode).append("未找到库存状态!");
                continue;
            }
            ProductStockState _productStockState = listProductStockState.get(0);
            if (_productStockState.getWarehouseCode().equals(productInRecord.getWarehouseCode()) && _productStockState.getWarehousePosCode().equals(productInRecord.getWarehousePosCode())) {
                result.append(barCode).append("库位位置没变!");
                continue;
            }
            _productStockState.setWarehouseCode(productInRecord.getWarehouseCode());
            _productStockState.setWarehousePosCode(productInRecord.getWarehousePosCode());
            _productStockState.setStockState(intStockState);
            pss.add(_productStockState);

            map.put("rollBarcode", _productStockState.getBarCode());
            TotalStatistics ts = productStockDao.findUniqueByMap(TotalStatistics.class, map);
            Tray t = codeService.findBarCodeReg(BarCodeRegType.TRAY, productInRecord.getBarCode());
            TrayBarCode tbc = codeService.findBarcodeInfo(BarCodeType.TRAY, productInRecord.getBarCode());
            if (ts == null) {
                if (tbc != null) {
                    ts = new TotalStatistics();
                    ts.setBarcodeType("tray");
                    ts.setBatchCode(_productStockState.getBarCode());
                    ts.setDeviceCode(tbService.trayDeviceCode(productInRecord.getBarCode()));
                    ts.setIsLocked(-1);
                    ts.setIsPacked(0);
                    if (t != null) {
                        User u = findById(User.class, t.getPackagingStaff());
                        ts.setLoginName(u.getLoginName());
                    }
                    ProducePlanDetail ppd = findById(ProducePlanDetail.class, tbc.getProducePlanDetailId());
                    ProducePlan produce = findById(ProducePlan.class, ppd.getProducePlanId());
                    ts.setCONSUMERNAME(ppd.getConsumerName());
                    ts.setName(produce.getWorkshop());
                    ts.setProducePlanCode(produce.getProducePlanCode());
                    ts.setProductLength(ppd.getProductLength());
                    ts.setProductModel(ppd.getProductModel());
                    ts.setProductName(ppd.getFactoryProductName());
                    ts.setProductWeight(ppd.getProductWeight());
                    ts.setProductWidth(ppd.getProductWidth());
                    ts.setRollBarcode(productInRecord.getBarCode());
                    ts.setRollOutputTime(new Date());
                    ts.setRollQualityGradeCode("A");
                    ts.setRollWeight(t.getWeight());
                    ts.setSalesOrderCode(tbc.getSalesOrderCode());
                    ts.setState(intStockState);
                    save(ts);
                }
            } else {
                ts.setState(intStockState);
                productStockDao.update(ts);
            }
            actionBarCode(barCode, StockState.IN, productInRecord.getWarehouseCode(), productInRecord.getWarehousePosCode(), productInRecord.getInBankSource());
            List<ProductInRecord> listProductInRecord = productStockDao.find(ProductInRecord.class, "barcode", barCode);
            if (listProductInRecord != null && listProductInRecord.size() > 0) {
                listProductInRecord.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
                ProductInRecord record = listProductInRecord.get(0);
                record.setInTime(productInRecord.getInTime());
                record.setOperateUserId(productInRecord.getOperateUserId());
                record.setWarehouseCode(productInRecord.getWarehouseCode());
                record.setWarehousePosCode(productInRecord.getWarehousePosCode());
                record.setWeight(productInRecord.getWeight());
                productStockDao.update(record);
            } else {
                productStockDao.save(productInRecord);
            }
            //部件条码直接入库
            if (barCode.startsWith("PCJ")) {
                Roll roll = findOne(Roll.class, "partBarcode", barCode);
                if (roll != null) {
                    roll.setInTime(productInRecord.getInTime());
                    productStockDao.update(roll);
                }
            }
        }
        productStockDao.update2(pss.toArray(new ProductStockState[]{}));
        return result.toString();
    }

    /**
     * 胚布批量领料
     */
    public String pbPicks(List<FabricPickRecord> fabricPickRecordlist) throws Exception {
        StringBuilder result = new StringBuilder();
        int intStockState = StockState.Pick;
        List<ProductStockState> pss = new ArrayList<>();
        for (FabricPickRecord fabricPickRecord : fabricPickRecordlist) {
            String barCode = fabricPickRecord.getBarCode();
            fabricPickRecord.setInTime(new Date());
            List<ProductStockState> listProductStockState = productStockDao.find(ProductStockState.class, "barcode", barCode);
            if (listProductStockState != null && listProductStockState.size() > 0) {
                listProductStockState.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
            } else {
                result.append(barCode).append("未找到库存状态!");
                continue;
            }

            ProductStockState _productStockState = listProductStockState.get(0);
            if (_productStockState.getWarehouseCode().equals(fabricPickRecord.getWarehouseCode()) && _productStockState.getWarehousePosCode().equals(fabricPickRecord.getWarehousePosCode())) {
                result.append(barCode).append("库位位置没变!");
                continue;
            }

            _productStockState.setWarehouseCode(fabricPickRecord.getWarehouseCode());
            _productStockState.setWarehousePosCode(fabricPickRecord.getWarehousePosCode());
            _productStockState.setStockState(intStockState);
            pss.add(_productStockState);
            actionBarCode(barCode, intStockState, fabricPickRecord.getWarehouseCode(), fabricPickRecord.getWarehousePosCode(), fabricPickRecord.getInBankSource());

            List<FabricPickRecord> listPendingPickRecord = productStockDao.find(FabricPickRecord.class, "barcode", barCode);
            if (listPendingPickRecord != null && listPendingPickRecord.size() > 0) {
                listPendingPickRecord.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
                FabricPickRecord record = listPendingPickRecord.get(0);
                record.setInTime(fabricPickRecord.getInTime());
                record.setOperateUserId(fabricPickRecord.getOperateUserId());
                record.setWarehouseCode(fabricPickRecord.getWarehouseCode());
                record.setWarehousePosCode(fabricPickRecord.getWarehousePosCode());
                record.setWeight(fabricPickRecord.getWeight());
                productStockDao.update(record);
            } else {
                productStockDao.save(fabricPickRecord);
            }
        }
        productStockDao.update2(pss.toArray(new ProductStockState[]{}));
        return result.toString();
    }

    @Override
    public Map<String, Object> moveInfolist(Filter filter, Page page) {
        return productStockDao.moveInfolist(filter, page);
    }
}
