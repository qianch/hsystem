package com.bluebirdme.mes.mobile.stock.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.mobile.common.service.IMobileService;
import com.bluebirdme.mes.mobile.stock.dao.IMobilePackageDao;
import com.bluebirdme.mes.mobile.stock.service.IMobilePackage2Service;
import com.bluebirdme.mes.mobile.stock.service.IMobilePackageService;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.statistics.entity.TotalStatistics;
import com.bluebirdme.mes.store.dao.ITrayBarCodeDao;
import com.bluebirdme.mes.store.entity.*;
import com.bluebirdme.mes.store.service.IBarCodeService;
import com.bluebirdme.mes.utils.ProductState;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MathUtils;
import org.xdemo.superutil.j2se.StringUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.*;

/**
 * 打包Service
 *
 * @author Goofy
 * @Date 2016年11月18日 上午9:39:43
 */
@Service
@AnyExceptionRollback
public class MobilePackage2ServiceImpl extends BaseServiceImpl implements IMobilePackage2Service {
    @Resource
    IMobilePackageDao packageDao;
    @Resource
    IBarCodeService bcService;
    @Resource
    IMobileService mService;

    @Resource
    IMobilePackageService mobilePackageService;

    @Resource
    ITrayBarCodeDao trayBarCodeDao;

    @Override
    protected IBaseDao getBaseDao() {
        return packageDao;
    }


    @Override
    public String tray(String puname, Long packagingStaff, String trayCode, String boxCodes, String rollCodes, Long planId, String partName, String model, Long partId) throws Exception {
        if (!StringUtils.isBlank(rollCodes)) {
            return trayRollCodes(puname, packagingStaff, trayCode, rollCodes, planId, partName, partId);
        } else if (!StringUtils.isBlank(boxCodes)) {
            return mobilePackageService.tray(puname, packagingStaff, trayCode, boxCodes, rollCodes, planId, partName, model, partId);
        } else {
            return GsonTools.toJson("请扫描卷或者箱条码");
        }
    }


    private String trayRollCodes(String puname, Long packagingStaff, String trayCode, String rollCodes, Long planId, String partName, Long partId) throws Exception {
        StringBuilder exception = new StringBuilder();
        String[] _rolles = rollCodes.split(",");
        if (StringUtils.isBlank(rollCodes)) {
            throw new Exception("卷条码为空");
        }
        Map<String, Object> param = new HashMap();
        List<Map<String, Object>> ibarcodeByBarcodelist = trayBarCodeDao.findIbarcodeByBarcode(rollCodes);
        if (ibarcodeByBarcodelist == null || ibarcodeByBarcodelist.size() == 0) {
            throw new Exception("不存在条码信息");
        }
        if (ibarcodeByBarcodelist.size() > 1) {
            throw new Exception("不是同一批次的条码不能打包!");
        }
        List<Map<String, Object>> listTrayBoxlist = trayBarCodeDao.findTrayBoxRollByBarcode(rollCodes);
        if (listTrayBoxlist != null && listTrayBoxlist.size() > 0) {
            exception.append("条码已经打包:");
            for (Map<String, Object> trayboxroll : listTrayBoxlist) {
                String barCode = (trayboxroll.get("ROLLBARCODE") == null ? "" : trayboxroll.get("ROLLBARCODE").toString()) + (trayboxroll.get("PARTBARCODE") == null ? "" : trayboxroll.get("PARTBARCODE").toString());
                String trayBarcode = trayboxroll.get("TRAYBARCODE") == null ? "" : trayboxroll.get("TRAYBARCODE").toString();
                exception.append(trayBarcode).append(":").append(barCode).append(";");
            }
            throw new Exception(exception.toString());
        }

        String type = ibarcodeByBarcodelist.get(0).get("TYPE").toString();
        long _SalesOrderDetailId = Long.parseLong(ibarcodeByBarcodelist.get(0).get("SALESORDERDETAILID").toString());
        String _SalesOrderCode = ibarcodeByBarcodelist.get(0).get("SALESORDERCODE").toString();
        String _BatchCode = ibarcodeByBarcodelist.get(0).get("BATCHCODE").toString();

        param.put("barcode", trayCode);
        TrayBarCode tbc = findUniqueByMap(TrayBarCode.class, param);

        SalesOrderDetail sod = findById(SalesOrderDetail.class, _SalesOrderDetailId);
        FinishedProduct fp = packageDao.findById(FinishedProduct.class, sod.getProductId());
        Consumer consumer = packageDao.findById(Consumer.class, fp.getProductConsumerId());
        User user = packageDao.findById(User.class, packagingStaff);
        double weight = 0D;
        List<TrayBoxRoll> trayBoxRolllist = new ArrayList<>();
        ProducePlanDetail ppd = null;
        switch (type) {
            case "rollbarcode" -> {
                WeavePlan wp = findById(WeavePlan.class, planId);
                ppd = findById(ProducePlanDetail.class, wp.getProducePlanDetailId());
            }
            case "partbarcode" -> {
                CutPlan cp = findById(CutPlan.class, planId);
                ppd = findById(ProducePlanDetail.class, cp.getProducePlanDetailId());
            }
            default -> {
            }
        }
        String bladeProfile = "";
        if (ppd != null && ppd.getPartId() != null) {
            SalesOrderDetail salesOrderDetail = packageDao.findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
            bladeProfile = salesOrderDetail.getBladeProfile();
        } else if (ppd.getProductIsTc() == 1) {
            bladeProfile = ppd.getConsumerProductName();
        }

        ProducePlan produce = findById(ProducePlan.class, ppd.getProducePlanId());
        if (tbc.getPlanId() == null) {
            tbc.setSalesOrderCode(_SalesOrderCode);
            tbc.setBatchCode(_BatchCode);
            tbc.setPartName(partName);
            tbc.setPlanId(planId);
            tbc.setPartId(partId);
            tbc.setProducePlanDetailId(ppd.getId());
            tbc.setSalesProductId(sod.getProductId());
            tbc.setSalesOrderDetailId(_SalesOrderDetailId);
            tbc.setPlanDeliveryDate(ppd.getDeleveryDate());
            update(tbc);
        }

        String rollDeviceCode = "";
        for (String rollCode : _rolles) {
            Roll roll = new Roll();
            switch (type) {
                case "rollbarcode" -> {
                    roll = findOne(Roll.class, "rollBarcode", rollCode);
                    RollBarcode rb = findOne(RollBarcode.class, "barcode", rollCode);
                    if (rb != null) {
                        if (rb.getIsPackage() == 1) {
                            List<TrayBoxRoll> listTrayBoxRoll = mService.find(TrayBoxRoll.class, "rollBarcode", rb.getBarcode());
                            if (listTrayBoxRoll != null && listTrayBoxRoll.size() > 0) {
                                throw new Exception("条码号：" + rb.getBarcode() + "，已经被" + listTrayBoxRoll.get(0).getTrayBarcode() + "打包");
                            }
                        }
                        rb.setIsPackage(1);
                        update(rb);
                    }
                }
                case "partbarcode" -> roll = findOne(Roll.class, "partBarcode", rollCode);
                default -> {
                }
            }

            if (roll.getId() == null) {
                throw new Exception("roll：" + rollCode + "卷条码不存在!");
            }
            rollDeviceCode = roll.getRollDeviceCode();
            weight = MathUtils.add(weight, roll.getRollWeight(), 1);
            // 保存托卷关系
            TrayBoxRoll tbr = new TrayBoxRoll();
            tbr.setRollBarcode(rollCode);
            tbr.setTrayBarcode(trayCode);
            tbr.setPackagingStaff(packagingStaff);
            tbr.setPackagingTime(new Date());
            trayBoxRolllist.add(tbr);
            TotalStatistics tsRoll = findOne(TotalStatistics.class, "rollBarcode", rollCode);
            if (tsRoll != null) {
                tsRoll.setIsPacked(1);
                update(tsRoll);
            }
        }

        param.clear();
        param.put("trayBarcode", trayCode);
        Tray tray = findUniqueByMap(Tray.class, param);
        if (tray == null) {
            tray = new Tray();
            tray.setPackagingStaff(packagingStaff);
            tray.setPackagingTime(new Date());
            tray.setTrayBarcode(trayCode);
            tray.setRollQualityGradeCode("A");
            tray.setState(ProductState.VALID);
            tray.setEndPack(0);
            tray.setProductModel(fp.getProductModel());
            tray.setBatchCode(_BatchCode);
            tray.setDeviceCode(rollDeviceCode);
            tray.setProducePlanCode(produce.getProducePlanCode());
        }

        if (null == tray.getWeight()) {
            tray.setWeight(MathUtils.add(0D, MathUtils.add(weight, 0D, 2), 2));
        } else {
            tray.setWeight(MathUtils.add(tray.getWeight(), MathUtils.add(weight, 0D, 2), 2));
        }

        if (tray.getPackagingStaff() == -1L) {
            tray.setPackagingStaff(packagingStaff);
            tray.setPackagingTime(new Date());
        }
        saveList(trayBoxRolllist);
        tray.setRollCountInTray(bcService.countRollsInTray(tray.getTrayBarcode()));
        save(tray);
        param.clear();
        param.put("rollBarcode", trayCode);
        TotalStatistics ts = findUniqueByMap(TotalStatistics.class, param);
        if (null == ts) {
            ts = new TotalStatistics();
            ts.setRollBarcode(trayCode);
            ts.setBarcodeType("tray");
            ts.setBatchCode(tbc.getBatchCode());
            ts.setIsLocked(-1);
            ts.setIsPacked(0);
            User u = findById(User.class, packagingStaff);
            ts.setLoginName(u.getUserName());
            ts.setCONSUMERNAME(consumer.getConsumerName());
            ts.setName(packageDao.findById(Department.class, user.getDid()).getName());
            ts.setWorkShopCode(packageDao.findById(Department.class, user.getDid()).getCode());
            ts.setProducePlanCode(produce.getProducePlanCode());
            ts.setProductLength(ppd.getProductLength());
            ts.setProductModel(fp.getProductModel());
            ts.setProductName(fp.getFactoryProductName());
            ts.setProductWidth(ppd.getProductWidth());
            ts.setProductLength(ppd.getProductLength());
            ts.setRollOutputTime(new Date());
            ts.setRollQualityGradeCode("A");
            ts.setProductWeight(tray.getWeight());
            ts.setSalesOrderCode(tbc.getSalesOrderCode());
            ts.setState(0);
            ts.setDeviceCode(rollDeviceCode);
            ts.setIsPacked(1);
            ts.setBladeProfile(bladeProfile);
            save(ts);
        } else {
            ts.setProductWeight(MathUtils.add(ts.getProductWeight(), weight, 1));
            ts.setProductModel(fp.getProductModel());
            ts.setDeviceCode(rollDeviceCode);
            update(ts);
        }
        return GsonTools.toJson(tray);
    }
}
