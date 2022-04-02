package com.bluebirdme.mes.mobile.stock.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.bluebirdme.mes.platform.entity.Log;
import com.bluebirdme.mes.platform.service.ILogService;
import com.bluebirdme.mes.store.entity.*;
import com.bluebirdme.mes.store.service.ITrayService;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MathUtils;
import org.xdemo.superutil.j2se.StringUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.common.service.IProcessService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.mobile.common.service.IMobileService;
import com.bluebirdme.mes.mobile.stock.dao.IMobilePackageDao;
import com.bluebirdme.mes.mobile.stock.service.IMobilePackageService;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.turnbag.service.ITurnBagPlanService;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.statistics.entity.TotalStatistics;
import com.bluebirdme.mes.statistics.service.ITotalStatisticsService;
import com.bluebirdme.mes.store.service.IBarCodeService;
import com.bluebirdme.mes.utils.ProductState;

/**
 * 打包Service
 *
 * @author Goofy
 * @Date 2016年11月18日 上午9:39:43
 */
@Service
@AnyExceptionRollback
public class MobilePackageServiceImpl extends BaseServiceImpl implements IMobilePackageService {
    @Resource
    ITotalStatisticsService totalStatisticsService;
    @Resource
    IMobilePackageDao packageDao;
    @Resource
    IProcessService processService;
    @Resource
    IBarCodeService bcService;
    @Resource
    IMobileService mService;
    @Resource
    ITurnBagPlanService tbService;
    @Resource
    ITrayService trayService;
    @Resource
    ILogService logService;

    @Override
    protected IBaseDao getBaseDao() {
        return packageDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return null;
    }

    public void saveBoxRoll(Box box, BoxRoll[] boxRolls, Long planId, String partName) {
        packageDao.saveBoxRoll(box, boxRolls, planId, partName);
    }

    public void saveTrayBoxRoll(Tray tray, TrayBoxRoll[] trayBoxRolls, Long planId, String partName) {
        packageDao.saveTrayBoxRoll(tray, trayBoxRolls, planId, partName);
    }

    @Override
    public void updateProgress(Tray tray) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("barcode", tray.getTrayBarcode());
        List<TrayBarCode> li = packageDao.findListByMap(TrayBarCode.class, map);
        if (li.size() != 0) {
            // 打包进度
            TrayBarCode trayBarCode = li.get(0);
            WeavePlan w = packageDao.findById(WeavePlan.class, trayBarCode.getPlanId());
            ProducePlanDetail producePlanDetail = packageDao.findById(ProducePlanDetail.class, w.getProducePlanDetailId());
            w.setProduceTrayCount(w.getProduceTrayCount() + 1);
            producePlanDetail.setPackagedCount(producePlanDetail.getPackagedCount() + 1);
            packageDao.update(w);
            packageDao.update(producePlanDetail);
        }
    }

    public List<Map<String, Object>> isPackedRoll(String rollCodes) {
        String rolls[] = rollCodes.split(",");
        return packageDao.isPackedRoll(rolls);
    }

    @Override
    public List<Map<String, Object>> isPackedBoxRoll(String rollCodes, String boxCodes) {
        String rolls[] = rollCodes.split(",");
        String boxs[] = boxCodes.split(",");
        return packageDao.isPackedBoxRoll(rolls, boxs);
    }

    @Override
    public String tray(String puname, Long packagingStaff, String trayCode, String boxCodes, String rollCodes, Long planId, String partName, String model, Long partId) throws Exception {
        Map<String, Object> param = new HashMap();
        Set<String> deviceCodes = new HashSet();
        String[] _boxes = boxCodes.split(",");
        String[] _rolles = rollCodes.split(",");

        String firstBatchCode = "";
        for (String code : _boxes) {
            param.clear();
            param.put("trayBarcode", trayCode);
            param.put("boxBarcode", code);
            if (has(TrayBoxRoll.class, param)) {
                throw new Exception("条码号重复");
            }

            BoxBarcode boxBarcode = findOne(BoxBarcode.class, "barCode", code);
            if (boxBarcode != null) {
                if (StringUtils.isBlank(firstBatchCode)) {
                    firstBatchCode = boxBarcode.getBatchCode();
                } else if (!firstBatchCode.equals(boxBarcode.getBatchCode())) {
                    throw new Exception("条码号：" + boxBarcode.getBarcode() + "，批次号：" + boxBarcode.getBatchCode() + "，与其他卷的批次号:" + firstBatchCode + "不一致");
                }
            }
        }

        // 卷打托时候给卷加上被打托标记
        for (String str : _rolles) {
            param.clear();
            param.put("barcode", str);
            RollBarcode rb = mService.findUniqueByMap(RollBarcode.class, param);
            if (rb != null) {
                if (rb.getIsPackage() == 1) {
                    List<TrayBoxRoll> listTrayBoxRoll = mService.find(TrayBoxRoll.class, "rollBarcode", rb.getBarcode());
                    if (listTrayBoxRoll != null && listTrayBoxRoll.size() > 0) {
                        throw new Exception("条码号：" + rb.getBarcode() + "，已经被" + listTrayBoxRoll.get(0).getTrayBarcode() + "打包");
                    }
                }

                rb.setIsPackage(1);
                mService.update(rb);
                //校验批次号，只有同一个批次号的卷才能打成一托
                if (StringUtils.isBlank(firstBatchCode)) {
                    firstBatchCode = rb.getBatchCode();
                } else if (!firstBatchCode.equals(rb.getBatchCode())) {
                    throw new Exception("条码号：" + rb.getBarcode() + "，批次号：" + rb.getBatchCode() + "，与其他卷的批次号:" + firstBatchCode + "不一致");
                }
            }
        }

        for (String code : _rolles) {
            param.clear();
            param.put("trayBarcode", trayCode);
            param.put("rollBarcode", code);
            if (has(TrayBoxRoll.class, param)) {
                throw new Exception("条码号重复");
            }

            param.clear();
            param.put("trayBarcode", trayCode);
            param.put("partBarcode", code);
            if (has(TrayBoxRoll.class, param)) {
                throw new Exception("条码号重复");
            }
        }

        param.clear();
        param.put("barcode", trayCode);
        TrayBarCode tbc = findUniqueByMap(TrayBarCode.class, param);
        param.clear();

        if (tbc.getPlanId() == null) {
            TotalStatistics ts = new TotalStatistics();
            Tray tray = new Tray();
            tray.setPackagingStaff(packagingStaff);
            tray.setPackagingTime(new Date());
            tray.setTrayBarcode(trayCode);
            tray.setRollQualityGradeCode("A");
            tray.setState(ProductState.VALID);
            tray.setEndPack(0);
            String bladeProfile="";
            if (!StringUtils.isBlank(rollCodes)) {
                String _rb = rollCodes.split(",")[0];
                RollBarcode rollbarcode;
                PartBarcode partBarcode;
                rollbarcode = mService.findBarcodeInfo(BarCodeType.ROLL, _rb);
                partBarcode = mService.findBarcodeInfo(BarCodeType.PART, _rb);
                IBarcode ib;
                ProducePlanDetail ppd = null;
                if (rollbarcode != null) {
                    ib = rollbarcode;
                    WeavePlan wp = findById(WeavePlan.class, planId);
                    ppd = findById(ProducePlanDetail.class, wp.getProducePlanDetailId());
                } else {
                    ib = partBarcode;
                    CutPlan cp = findById(CutPlan.class, planId);
                    ppd = findById(ProducePlanDetail.class, cp.getProducePlanDetailId());
                }


                if (ppd.getPartId() != null){
                    SalesOrderDetail salesOrderDetail = mService.findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
                    bladeProfile = salesOrderDetail.getBladeProfile();
                }else if(ppd.getProductIsTc()==1){
                    bladeProfile = ppd.getConsumerProductName();
                }

                tbc.setSalesOrderCode(ib.getSalesOrderCode());
                tbc.setBatchCode(ib.getBatchCode());
                tbc.setPartName(partName);
                tbc.setPlanId(planId);
                tbc.setPartId(partId);
                tbc.setProducePlanDetailId(ppd.getId());
                SalesOrderDetail sod = findById(SalesOrderDetail.class, ib.getSalesOrderDetailId());
                tbc.setSalesProductId(sod.getProductId());
                tbc.setSalesOrderDetailId(ib.getSalesOrderDetailId());
                tbc.setPlanDeliveryDate(ppd.getDeleveryDate());
            } else {
                param.clear();
                param.put("barcode", boxCodes.split(",")[0]);
                BoxBarcode bbc = findUniqueByMap(BoxBarcode.class, param);

                param.clear();
                param.put("boxBarcode", bbc.getBarcode());
                List<BoxRoll> boxRolls = findListByMap(BoxRoll.class, param);

                String _rb = boxRolls.get(0).getRollBarcode();
                ProducePlanDetail ppd = null;
                if (_rb != null) {
                    WeavePlan wp = findById(WeavePlan.class, planId);
                    ppd = findById(ProducePlanDetail.class, wp.getProducePlanDetailId());
                } else {
                    CutPlan cp = findById(CutPlan.class, planId);
                    ppd = findById(ProducePlanDetail.class, cp.getProducePlanDetailId());
                }

                if (ppd.getPartId() != null){
                    SalesOrderDetail salesOrderDetail = mService.findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
                    bladeProfile = salesOrderDetail.getBladeProfile();
                }else if(ppd.getProductIsTc()==1){
                    bladeProfile = ppd.getConsumerProductName();
                }

                tbc.setSalesOrderCode(bbc.getSalesOrderCode());
                tbc.setBatchCode(bbc.getBatchCode());
                tbc.setPartName(partName);
                tbc.setPlanId(planId);
                tbc.setPartId(partId);
                tbc.setProducePlanDetailId(ppd.getId());

                SalesOrderDetail sod = findById(SalesOrderDetail.class, bbc.getSalesOrderDetailId());
                ProducePlan produce = findById(ProducePlan.class, ppd.getProducePlanId());
                tbc.setSalesProductId(sod.getProductId());
                tbc.setSalesOrderDetailId(bbc.getSalesOrderDetailId());
                tbc.setPlanDeliveryDate(ppd.getDeleveryDate());

                ts.setProductLength(ppd.getProductLength());
                ts.setProductWidth(ppd.getProductWidth());
                ts.setProducePlanCode(produce.getProducePlanCode());
                ts.setSalesOrderCode(ppd.getSalesOrderCode());
                ts.setBatchCode(ppd.getBatchCode());
                ts.setCONSUMERNAME(ppd.getConsumerName());
            }
            update(tbc);

            List<TrayBoxRoll> list = new ArrayList<TrayBoxRoll>();
            TrayBoxRoll tbr = new TrayBoxRoll();
            Box box = null;
            Roll roll = null;

            Double boxTotalWeight = 0D;
            Double rollTotalWeight = 0D;

            if (!StringUtils.isBlank(boxCodes)) {
                String[] boxes = boxCodes.split(",");
                for (String boxCode : boxes) {
                    if (totalStatisticsService.isFrozen(boxCode) == ProductState.FROZEN) {
                        return GsonTools.toJson("条码：" + boxCode + "已冻结，不能打包");
                    }
                    tbr = new TrayBoxRoll();
                    tbr.setBoxBarcode(boxCode);
                    tbr.setTrayBarcode(trayCode);
                    tbr.setPackagingStaff(packagingStaff);
                    tbr.setPackagingTime(new Date());
                    list.add(tbr);

                    param.clear();
                    param.put("boxBarcode", boxCode);

                    box = findUniqueByMap(Box.class, param);
                    tray.setProductModel(box.getProductModel());
                    boxTotalWeight = MathUtils.add(box.getWeight(), boxTotalWeight, 1);
                    IBarcode r = bcService.findBarcodeInfo(BarCodeType.BOX, boxCode);
                    param.clear();
                    param.put("boxBarcode", boxCode);
                    planId = r.getPlanId();
                    List<TotalStatistics> _tss = totalStatisticsService.find(TotalStatistics.class, "rollBarcode", box.getBoxBarcode());
                    for (TotalStatistics t : _tss) {
                        if (t.getDeviceCode() != null) {
                            for (String c : t.getDeviceCode().split(",")) {
                                deviceCodes.add(c);
                            }
                        }
                    }
                    ts.setDeviceCode(deviceCodes.toString().replace("[", "").replace("]", ""));
                }
            }

            if (!StringUtils.isBlank(rollCodes)) {
                String[] rolls = rollCodes.split(",");
                for (String rollCode : rolls) {
                    if (totalStatisticsService.isFrozen(rollCode) == ProductState.FROZEN) {
                        return GsonTools.toJson("条码：" + rollCode + "已冻结，不能打包");
                    }

                    tbr = new TrayBoxRoll();
                    tbr.setRollBarcode(rollCode);
                    tbr.setTrayBarcode(trayCode);
                    tbr.setPackagingStaff(packagingStaff);
                    tbr.setPackagingTime(new Date());
                    list.add(tbr);
                    IBarcode r = null;
                    HashMap<String, Object> map1 = new HashMap();
                    map1.put("barcode", rollCode);
                    r = findUniqueByMap(RollBarcode.class, map1);
                    param.clear();
                    param.put("rollBarcode", rollCode);
                    roll = findUniqueByMap(Roll.class, param);

                    if (roll == null) {
                        param.clear();
                        param.put("partBarcode", rollCode);
                        roll = findUniqueByMap(Roll.class, param);
                        r = findUniqueByMap(PartBarcode.class, map1);
                    }

                    BigDecimal rollWeight = new BigDecimal(roll.getRollWeight() == null ? "0" : roll.getRollWeight().toString());
                    if (rollWeight.compareTo(new BigDecimal(0)) == 0) {
                        throw new Exception("条码：" + roll.getRollBarcode() + "还未称重，不能打包");
                    }

                    rollTotalWeight = MathUtils.add(roll.getRollWeight(), rollTotalWeight, 1);
                    FinishedProduct fp = packageDao.findById(FinishedProduct.class, r.getSalesProductId());
                    tray.setProductModel(fp.getProductModel());
                    Consumer con = packageDao.findById(Consumer.class, fp.getProductConsumerId());
                    ts.setCONSUMERNAME(con.getConsumerName());
                    if (r instanceof RollBarcode) {
                        ts.setDeviceCode(roll.getRollDeviceCode());
                    }
                    ts.setBatchCode(r.getBatchCode());
                    ts.setProductName(fp.getFactoryProductName());
                    ts.setSalesOrderCode(r.getSalesOrderCode());
                    planId = r.getPlanId();

                    TotalStatistics tsRoll = findOne(TotalStatistics.class, "rollBarcode", rollCode);
                    if (tsRoll != null) {
                        tsRoll.setIsPacked(1);
                        update(tsRoll);
                    }
                }
            }

            tray.setWeight(MathUtils.add(boxTotalWeight, rollTotalWeight, 1));
            saveTrayBoxRoll(tray, list.toArray(new TrayBoxRoll[list.size()]), planId, partName);
            tray.setRollCountInTray(bcService.countRollsInTray(tray.getTrayBarcode()));
            update(tray);
            ProducePlanDetail ppd = findById(ProducePlanDetail.class, tbc.getProducePlanDetailId());
            ProducePlan produce = findById(ProducePlan.class, ppd.getProducePlanId());
            ts.setIsLocked(-1);
            ts.setProductWeight(tray.getWeight());
            ts.setRollOutputTime(new Date());
            ts.setRollQualityGradeCode("A");
            ts.setLoginName(puname);

            User user = packageDao.findById(User.class, packagingStaff);
            ts.setName(packageDao.findById(Department.class, user.getDid()).getName());
            ts.setWorkShopCode(packageDao.findById(Department.class, user.getDid()).getCode());
            ts.setProductModel(tray.getProductModel());
            ts.setBarcodeType("tray");
            ts.setProducePlanCode(produce.getProducePlanCode());
            ts.setProductWidth(ppd.getProductWidth());
            ts.setProductLength(ppd.getProductLength());
            ts.setRollBarcode(trayCode);
            ts.setIsPacked(1);
            ts.getProducePlanCode();
            ts.setBladeProfile(bladeProfile);
            processService.save(ts);
            return GsonTools.toJson(tray);
        } else {
            double weight = 0D;
            TrayBoxRoll tbrs[];
            TrayBoxRoll tbr;
            Map<String, Object> con = new HashMap();
            if (!StringUtils.isBlank(rollCodes)) {
                String[] codes = rollCodes.split(",");
                tbrs = new TrayBoxRoll[codes.length];
                Roll roll;
                int i = 0;
                for (String code : rollCodes.split(",")) {
                    if (code.startsWith("P")) {
                        con.clear();
                        con.put("partBarcode", code);
                        roll = findUniqueByMap(Roll.class, con);
                    } else {
                        con.clear();
                        con.put("rollBarcode", code);
                        roll = findUniqueByMap(Roll.class, con);

                        BigDecimal rollWeight = new BigDecimal(roll.getRollWeight() == null ? "0" : roll.getRollWeight().toString());
                        if (rollWeight.compareTo(new BigDecimal(0)) == 0) {
                            throw new Exception("条码：" + code + "还未称重，不能打包");
                        }
                    }
                    con.clear();
                    con.put("barcode", code);
                    weight = MathUtils.add(weight, roll.getRollWeight(), 1);
                    // 保存托卷关系
                    tbr = new TrayBoxRoll();
                    tbr.setRollBarcode(code);
                    tbr.setTrayBarcode(trayCode);
                    tbr.setPackagingStaff(packagingStaff);
                    tbr.setPackagingTime(new Date());
                    tbrs[i++] = tbr;
                }
            } else {
                String[] codes = boxCodes.split(",");
                tbrs = new TrayBoxRoll[codes.length];
                Box box;
                int i = 0;
                for (String code : boxCodes.split(",")) {
                    con.clear();
                    con.put("boxBarcode", code);
                    box = findUniqueByMap(Box.class, con);
                    weight = MathUtils.add(weight, box.getWeight(), 1);
                    // 保存托卷关系
                    tbr = new TrayBoxRoll();
                    tbr.setBoxBarcode(code);
                    tbr.setTrayBarcode(trayCode);
                    tbr.setPackagingStaff(packagingStaff);
                    tbr.setPackagingTime(new Date());
                    tbrs[i++] = tbr;
                    List<TotalStatistics> _tss = totalStatisticsService.find(TotalStatistics.class, "rollBarcode", box.getBoxBarcode());
                    for (TotalStatistics t : _tss) {
                        if (t.getDeviceCode() != null) {
                            for (String c : t.getDeviceCode().split(",")) {
                                deviceCodes.add(c);
                            }
                        }
                    }
                }
            }
            save(tbrs);
            con.clear();
            con.put("trayBarcode", trayCode);
            Tray t = findUniqueByMap(Tray.class, con);
            if (null == t.getWeight()) {
                t.setWeight(MathUtils.add(0D, MathUtils.add(weight, 0D, 2), 2));
            } else {
                t.setWeight(MathUtils.add(t.getWeight(), MathUtils.add(weight, 0D, 2), 2));
            }

            if (t.getPackagingStaff() == -1L) {
                t.setPackagingStaff(packagingStaff);
                t.setPackagingTime(new Date());
            }
            t.setRollCountInTray(bcService.countRollsInTray(t.getTrayBarcode()));
            update(t);

            con.clear();
            con.put("rollBarcode", trayCode);
            TotalStatistics ts = findUniqueByMap(TotalStatistics.class, con);
            if (null == ts) {
                ts = new TotalStatistics();
                ts.setBarcodeType("tray");
                ts.setBatchCode(tbc.getBatchCode());
                ts.setDeviceCode(tbService.trayDeviceCode(trayCode));
                ts.setIsLocked(-1);
                ts.setIsPacked(0);
                User u = findById(User.class, packagingStaff);
                ts.setLoginName(u.getUserName());
                ProducePlanDetail ppd = findById(ProducePlanDetail.class, tbc.getProducePlanDetailId());
                String bladeProfile="";
                if (ppd.getPartId() != null){
                    SalesOrderDetail salesOrderDetail = tbService.findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
                    bladeProfile = salesOrderDetail.getBladeProfile();
                }else if(ppd.getProductIsTc()==1){
                    bladeProfile = ppd.getConsumerProductName();
                }
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
                ts.setRollBarcode(trayCode);
                ts.setRollOutputTime(new Date());
                ts.setRollQualityGradeCode("A");
                ts.setProductWeight(t.getWeight());
                ts.setSalesOrderCode(tbc.getSalesOrderCode());
                ts.setState(0);
                ts.setDeviceCode(deviceCodes.toString());
                ts.setBladeProfile(bladeProfile);
                save(ts);
            } else {
                ts.setProductWeight(MathUtils.add(ts.getProductWeight(), weight, 1));
                ts.setProductModel(t.getProductModel());
                ts.setDeviceCode(deviceCodes.toString().replace("[", "").replace("]", ""));
                update(ts);
            }
            return GsonTools.toJson(t);
        }
    }

    @Override
    public String box(String puname, String boxCode, Long packagingStaff, String rollCodes, Long planId, String partName, Long partId) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        String[] _rolles = rollCodes.split(",");
        String firstBatchCode = "";
        for (String code : _rolles) {
            RollBarcode rollBarcode = findOne(RollBarcode.class, "barCode", code);
            if (rollBarcode != null) {
                if (StringUtils.isBlank(firstBatchCode)) {
                    firstBatchCode = rollBarcode.getBatchCode();
                } else if (!firstBatchCode.equals(rollBarcode.getBatchCode())) {
                    throw new Exception("条码号：" + rollBarcode.getBarcode() + "，批次号：" + rollBarcode.getBatchCode() + "，与其他卷的批次号:" + firstBatchCode + "不一致");
                }
            }

            param.clear();
            param.put("rollBarcode", code);
            if (has(BoxRoll.class, param)) {
                throw new Exception(code + "已被打包");
            }

            param.clear();
            param.put("partBarcode", code);
            if (has(BoxRoll.class, param)) {
                throw new Exception(code + "已被打包");
            }

            param.clear();
            param.put("boxBarcode", boxCode);
            param.put("rollBarcode", code);
            if (has(BoxRoll.class, param)) {
                throw new Exception("条码号重复");
            }

            param.clear();
            param.put("boxBarcode", boxCode);
            param.put("partBarcode", code);
            if (has(BoxRoll.class, param)) {
                throw new Exception("条码号重复");
            }
        }
        param.clear();
        param.put("barcode", boxCode);
        BoxBarcode bbc = findUniqueByMap(BoxBarcode.class, param);
        param.clear();

        if (bbc.getPlanId() == null) {
            String _rb = rollCodes.split(",")[0];
            RollBarcode rollbarcode;
            PartBarcode partBarcode;

            rollbarcode = mService.findBarcodeInfo(BarCodeType.ROLL, _rb);
            partBarcode = mService.findBarcodeInfo(BarCodeType.PART, _rb);

            IBarcode ib;
            if (rollbarcode != null) {
                ib = rollbarcode;
            } else {
                ib = partBarcode;
            }

            bbc.setSalesProductId(ib.getSalesProductId());
            bbc.setSalesOrderCode(ib.getSalesOrderCode());
            bbc.setBatchCode(ib.getBatchCode());
            bbc.setPartName(partName);
            bbc.setPartId(partId);
            bbc.setPlanId(planId);
            bbc.setSalesOrderDetailId(ib.getSalesOrderDetailId());
            update(bbc);
            Box box = new Box();
            box.setEndPack(0);
            box.setBoxBarcode(boxCode);
            box.setPackagingStaff(packagingStaff);
            box.setPackagingTime(new Date());
            box.setBatchCode(ib.getBatchCode());

            param.clear();
            // box.setBatchCode();
            box.setRollQualityGradeCode("A");
            box.setState(ProductState.VALID);
            String rolls[] = rollCodes.split(",");
            BoxRoll[] brs = new BoxRoll[rolls.length];
            BoxRoll boxRoll;
            Map<String, Object> map1 = new HashMap<String, Object>();
            String consumerName = "";
            int type = 1;
            Double weight = 0D;
            IBarcode r = null;
            TotalStatistics ts = new TotalStatistics();
            HashMap<String, HashSet<String>> model = new HashMap<String, HashSet<String>>();
            for (int i = 0; i < brs.length; i++) {
                boxRoll = new BoxRoll();
                boxRoll.setBoxBarcode(boxCode);
                boxRoll.setPackagingStaff(packagingStaff);
                boxRoll.setPackagingTime(new Date());
                if (totalStatisticsService.isFrozen(rolls[i]) == ProductState.FROZEN) {
                    return GsonTools.toJson("条码：" + rolls[i] + "已冻结，不能打包");
                }
                if (rolls[i].startsWith("R")) {
                    param.put("rollBarcode", rolls[i]);
                    map1.put("barcode", rolls[i]);
                    r = findUniqueByMap(RollBarcode.class, map1);
                    boxRoll.setRollBarcode(rolls[i]);
                }
                if (rolls[i].startsWith("P")) {
                    type = 2;
                    param.put("partBarcode", rolls[i]);
                    map1.put("barcode", rolls[i]);
                    r = findUniqueByMap(PartBarcode.class, map1);
                    boxRoll.setPartBarcode(rolls[i]);
                    ts.setName("CJ1");
                    ts.setWorkShopCode("00116");
                }
                Roll roll = findUniqueByMap(Roll.class, param);
                BigDecimal rollWeight = new BigDecimal(roll.getRollWeight() == null ? "0" : roll.getRollWeight().toString());
                if (rollWeight.compareTo(new BigDecimal(0)) == 0) {
                    throw new Exception("条码：" + roll.getRollBarcode() + "还未称重，不能打包");
                }
                weight = MathUtils.add(weight, roll.getRollWeight(), 1);
                brs[i] = boxRoll;
                FinishedProduct fp = packageDao.findById(FinishedProduct.class, r.getSalesProductId());
                box.setProductModel(fp.getProductModel());
                Consumer con = packageDao.findById(Consumer.class, fp.getProductConsumerId());
                if (model.containsKey("consumer")) {
                    model.get("consumer").add(con.getConsumerName());
                } else {
                    HashSet<String> set = new HashSet<String>();
                    set.add(con.getConsumerName());
                    model.put("consumer", set);
                }

                if (r instanceof RollBarcode) {
                    if (model.containsKey("device")) {
                        model.get("device").add(roll.getRollDeviceCode());
                    } else {
                        HashSet<String> set = new HashSet<String>();
                        set.add(roll.getRollDeviceCode());
                        model.put("device", set);
                    }
                }

                if (model.containsKey("pmodel")) {
                    model.get("pmodel").add(fp.getProductModel());
                } else {
                    HashSet<String> set = new HashSet<String>();
                    set.add(fp.getProductModel());
                    model.put("pmodel", set);
                }

                if (model.containsKey("pName")) {
                    model.get("pName").add(fp.getFactoryProductName());
                } else {
                    HashSet<String> set = new HashSet<String>();
                    set.add(fp.getFactoryProductName());
                    model.put("pName", set);
                }
            }
            // 箱子设置重量
            box.setWeight(weight);
            saveBoxRoll(box, brs, planId, partName);
            ts.setIsLocked(-1);
            if (model.containsKey("device")) {
                String deviceCode = "";
                for (String dcode : model.get("device")) {
                    if (deviceCode.length() == 0) {
                        deviceCode = dcode;
                    } else {
                        deviceCode += "," + dcode;
                    }
                }
                ts.setDeviceCode(deviceCode);
            }
            if (model.containsKey("consumer")) {
                String deviceCode = "";
                for (String dcode : model.get("consumer")) {
                    if (deviceCode.length() == 0) {
                        deviceCode = dcode;
                    } else {
                        deviceCode += "," + dcode;
                    }
                }
                ts.setCONSUMERNAME(deviceCode);
            }
            if (model.containsKey("pmodel")) {
                String deviceCode = "";
                for (String dcode : model.get("pmodel")) {
                    if (deviceCode.length() == 0) {
                        deviceCode = dcode;
                    } else {
                        deviceCode += "," + dcode;
                    }
                }
                ts.setProductModel(deviceCode);
            }
            if (model.containsKey("pName")) {
                String deviceCode = "";
                for (String dcode : model.get("pName")) {
                    if (deviceCode.length() == 0) {
                        deviceCode = dcode;
                    } else {
                        deviceCode += "," + dcode;
                    }
                }
                ts.setProductName(deviceCode);
            }
            // ts.setProductName(fp.getFactoryProductName());
            ts.setBarcodeType("box");
            ts.setRollBarcode(boxCode);
            ts.setBatchCode(box.getBatchCode());
            ts.setProductWeight(box.getWeight());
            ts.setRollOutputTime(new Date());
            ts.setRollQualityGradeCode("A");
            ts.setLoginName(puname);
            ts.setSalesOrderCode(r.getSalesOrderCode());
            ts.setIsLocked(ProductState.VALID);
            User user = packageDao.findById(User.class, packagingStaff);
            ts.setName(packageDao.findById(Department.class, user.getDid()).getName());
            ts.setWorkShopCode(packageDao.findById(Department.class, user.getDid()).getCode());
            if (type == 1) {
                WeavePlan weavePlan = processService.findById(WeavePlan.class, planId);
                ProducePlanDetail ppd = processService.findById(ProducePlanDetail.class, weavePlan.getProducePlanDetailId());
                String bladeProfile="";
                if (ppd.getPartId() != null){
                    SalesOrderDetail salesOrderDetail = processService.findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
                    bladeProfile = salesOrderDetail.getBladeProfile();
                }else if(ppd.getProductIsTc()==1){
                    bladeProfile = ppd.getConsumerProductName();
                }
                ts.setProductLength(weavePlan.getProductLength());
                ts.setProductWidth(weavePlan.getProductWidth());
                ts.setProducePlanCode(weavePlan.getPlanCode());
                ts.setBatchCode(weavePlan.getBatchCode());
                ts.setBladeProfile(bladeProfile);
            } else {
                CutPlan cutPlan = packageDao.findById(CutPlan.class, planId);
                ProducePlanDetail ppd = processService.findById(ProducePlanDetail.class, cutPlan.getProducePlanDetailId());
                ts.setBladeProfile(ppd.getConsumerProductName());
                ts.setProducePlanCode(cutPlan.getPlanCode());
                ts.setProductLength(cutPlan.getProductLength());
                ts.setProductWidth(cutPlan.getProductWidth());
                ts.setProducePlanCode(cutPlan.getPlanCode());
                ts.setBatchCode(cutPlan.getBatchCode());
            }
            save(ts);
        } else {
            param.clear();
            param.put("boxBarcode", boxCode);
            Box box = findUniqueByMap(Box.class, param);
            double weight = 0D;
            String _rolls[] = rollCodes.split(",");
            BoxRoll brs[] = new BoxRoll[_rolls.length];
            BoxRoll br;
            Roll roll;
            int i = 0;
            for (String rb : _rolls) {
                if (rb.startsWith("R")) {
                    param.clear();
                    param.put("rollBarcode", rb);
                    roll = findUniqueByMap(Roll.class, param);
                    br = new BoxRoll();
                    br.setBoxBarcode(boxCode);
                    br.setRollBarcode(rb);
                    br.setPackagingStaff(packagingStaff);
                    br.setPackagingTime(new Date());
                    brs[i++] = br;
                } else {
                    param.clear();
                    param.put("partBarcode", rb);
                    roll = findUniqueByMap(Roll.class, param);
                    br = new BoxRoll();
                    br.setBoxBarcode(boxCode);
                    br.setRollBarcode(rb);
                    br.setPackagingStaff(packagingStaff);
                    br.setPackagingTime(new Date());
                    brs[i++] = br;
                }
                BigDecimal rollWeight = new BigDecimal(roll.getRollWeight() == null ? "0" : roll.getRollWeight().toString());
                if (rollWeight.compareTo(new BigDecimal(0)) == 0) {
                    throw new Exception("条码：" + roll.getRollBarcode() + "还未称重，不能打包");
                }
                weight = MathUtils.add(weight, roll.getRollWeight(), 1);
            }
            save(brs);
            box.setWeight(MathUtils.add(box.getWeight(), weight, 2));
            if (box.getPackagingStaff() == -1L) {
                box.setPackagingStaff(packagingStaff);
                box.setPackagingTime(new Date());
            }
            update(box);
            param.clear();
            param.put("rollBarcode", boxCode);
            TotalStatistics ts = findUniqueByMap(TotalStatistics.class, param);
            if (ts == null) {
                ts = new TotalStatistics();
                ts.setBarcodeType("tray");
                ts.setBatchCode(bbc.getBatchCode());
                ts.setDeviceCode(tbService.trayDeviceCode(boxCode));
                ts.setIsLocked(-1);
                ts.setIsPacked(0);
                User u = findById(User.class, packagingStaff);
                ts.setLoginName(u.getUserName());
                ProducePlanDetail ppd = findById(ProducePlanDetail.class, bbc.getProducePlanDetailId());
                String bladeProfile="";
                if (ppd.getPartId() != null){
                    SalesOrderDetail salesOrderDetail = processService.findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
                    bladeProfile = salesOrderDetail.getBladeProfile();
                }else if(ppd.getProductIsTc()==1){
                    bladeProfile = ppd.getConsumerProductName();
                }
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
                ts.setRollBarcode(boxCode);
                ts.setRollOutputTime(new Date());
                ts.setRollQualityGradeCode("A");
                ts.setRollWeight(box.getWeight());
                ts.setSalesOrderCode(bbc.getSalesOrderCode());
                ts.setBladeProfile(bladeProfile);
                ts.setState(0);
                save(ts);
            } else {
                ts.setDeviceCode(tbService.trayDeviceCode(boxCode));
                ts.setRollWeight(box.getWeight());
                update(ts);
            }
        }
        return GsonTools.toJson(null);
    }

    public void updateTrayInfo(String trayCode, String rollCode) throws Exception {
        Map<String, Object> con = new HashMap<String, Object>();
        con.put("rollBarcode", rollCode);
        Roll r = findUniqueByMap(Roll.class, con);

        if (!r.getRollQualityGradeCode().equals("A")) {
            throw new Exception(rollCode + ":质量等级不合格");
        }

        if (r.getState() == ProductState.FROZEN) {
            throw new Exception(rollCode + ":已冻结");
        }

        if (r.getState() == ProductState.INVALID) {
            throw new Exception(rollCode + ":不合格");
        }

        con.clear();
        con.put("trayBarcode", trayCode);
        Tray t = findUniqueByMap(Tray.class, con);
        t.setWeight(MathUtils.add(t.getWeight(), r.getRollWeight(), 2));
        // 更新托重量
        update(t);
        // 保存托卷关系
        TrayBoxRoll tbr = new TrayBoxRoll();
        tbr.setRollBarcode(rollCode);
        tbr.setTrayBarcode(trayCode);

        save(tbr);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("rollBarcode", t.getTrayBarcode());
        TotalStatistics ts = findUniqueByMap(TotalStatistics.class, map1);
        ts.setProductWeight(t.getWeight());
        update(ts);
    }

    @Override
    public String endPack(String code) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (code.startsWith("B")) {
            map.put("boxBarcode", code);
            Box box = mService.findUniqueByMap(Box.class, map);
            if (box == null) {
                return "{\"error\":\"" + "该条码尚未使用,无法结束打包" + "\"}";
            }
            box.setEndPack(1);
            mService.update(box);
        } else {
            map.put("trayBarcode", code);
            Tray tray = mService.findUniqueByMap(Tray.class, map);
            if (tray == null) {
                return "{\"error\":\"" + "该条码尚未使用,无法结束打包" + "\"}";
            } else {
                BigDecimal rollBoxWeight = new BigDecimal(0);
                List<Map<String, Object>> rollBoxs = trayService.findRollBox(code);
                for (Map<String, Object> rollBox : rollBoxs) {
                    rollBoxWeight = rollBoxWeight.add(new BigDecimal(rollBox.get("BOXWEIGHT").toString()));
                    rollBoxWeight = rollBoxWeight.add(new BigDecimal(rollBox.get("ROLLWEIGHT").toString()));
                }

                rollBoxWeight = rollBoxWeight.setScale(1, RoundingMode.HALF_UP);
                BigDecimal trayWeight = new BigDecimal(tray.getWeight().toString()).setScale(1, RoundingMode.HALF_UP);
                Log log = checkWeightLog();
                log.setParamsValue("托条码：" + tray.getTrayBarcode() + ";托重:" + trayWeight + ";卷重、箱重之和:" + rollBoxWeight);
                logService.save(log);
                if (trayWeight.compareTo(rollBoxWeight) != 0 && rollBoxWeight.compareTo(new BigDecimal(0)) != 0) {
                    return "{\"error\":\"" + "托重:" + trayWeight + "与卷重、箱重之和:" + rollBoxWeight + "不匹配，请核对。" + "\"}";
                }
            }
            tray.setEndPack(1);
            mService.update(tray);
        }
        return "{}";
    }

    private Log checkWeightLog() {
        Log log = new Log();
        log.setLogDate(new Date());
        log.setOperate("条码校验重量");
        log.setLoginName("");
        log.setUserId(new Long(-999));
        log.setUserName("");
        return log;
    }

    /**
     * 拆盒或者托
     *
     * @param code 条码号
     */
    public void open(String code) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 盒拆包
        if (code.startsWith("B")) {
            BoxBarcode bbc = mService.findBarcodeInfo(BarCodeType.BOX, code);
            if (bbc == null) {
                return;
            }
            // 设置拆包状态
            bbc.setIsOpened(1);
            update(bbc);
            map.put("boxBarcode", code);
            // 删除卷、盒关系
            delete(BoxRoll.class, map);
        } else {
            map.put("trayBarcode", code);
            List<TrayBoxRoll> _tbr = mService.findListByMap(TrayBoxRoll.class, map);
            if (_tbr != null) {
                for (TrayBoxRoll rb : _tbr) {
                    RollBarcode _rb = mService.findBarcodeInfo(BarCodeType.ROLL, rb.getRollBarcode());
                    if (_rb == null) {
                        continue;
                    }
                    // 卷条码解除被打托状态
                    _rb.setIsPackage(0);
                    mService.update(_rb);
                }
            }

            // 托拆包
            TrayBarCode tbc = mService.findBarcodeInfo(BarCodeType.TRAY, code);
            if (tbc == null) {
                return;
            }

            // 设置拆包状态
            tbc.setIsOpened(1);
            update(tbc);
            map.clear();
            map.put("trayBarcode", code);
            // 删除卷、盒关系
            delete(TrayBoxRoll.class, map);
        }
    }

    public void openPackBarCode(String code, Long operateUserId) {
        Map<String, Object> map = new HashMap<String, Object>();
        OpenPackBarCode openPackBarCode = new OpenPackBarCode();
        openPackBarCode.setBarcode(code);
        openPackBarCode.setOperateUserId(operateUserId);
        openPackBarCode.setOpenPackDate(new Date());

        String content = "卷条码:";
        // 盒拆包
        if (code.startsWith("B")) {
            BoxBarcode bbc = mService.findBarcodeInfo(BarCodeType.BOX, code);
            if (bbc == null) {
                return;
            }
            // 设置拆包状态
            bbc.setIsOpened(1);
            update(bbc);
            map.put("boxBarcode", code);

            List<BoxRoll> list = findListByMap(BoxRoll.class, map);
            if (list != null) {
                for (BoxRoll rb : list) {
                    content += rb.getRollBarcode() + ";";
                }
            }
            // 删除卷、盒关系
            delete(BoxRoll.class, map);
        } else {
            map.put("trayBarcode", code);
            List<TrayBoxRoll> _tbr = mService.findListByMap(TrayBoxRoll.class, map);
            if (_tbr != null) {
                for (TrayBoxRoll rb : _tbr) {
                    RollBarcode _rb = mService.findBarcodeInfo(BarCodeType.ROLL, rb.getRollBarcode());
                    if (_rb == null) {
                        continue;
                    }
                    content += rb.getRollBarcode() + ";";
                    // 卷条码解除被打托状态
                    _rb.setIsPackage(0);
                    mService.update(_rb);
                }
            }

            // 托拆包
            TrayBarCode tbc = mService.findBarcodeInfo(BarCodeType.TRAY, code);
            if (tbc == null) {
                return;
            }

            // 设置拆包状态
            tbc.setIsOpened(1);
            update(tbc);
            map.clear();
            map.put("trayBarcode", code);
            // 删除卷、托关系
            delete(TrayBoxRoll.class, map);
        }
        openPackBarCode.setOpenPackContent(content);
        save(openPackBarCode);
    }


    public void deleteInner(String type, Long id) {
        type = type.toUpperCase();
        Map<String, Object> map = new HashMap();
        if (type.equals("BOX")) {
            BoxRoll boxRoll = findById(BoxRoll.class, id);
            Box box = mService.findBarCodeReg(BarCodeRegType.BOX, boxRoll.getBoxBarcode());
            map.clear();
            map.put("rollBarcode", boxRoll.getBoxBarcode());
            TotalStatistics ts = mService.findUniqueByMap(TotalStatistics.class, map);

            if (boxRoll.getPartBarcode() != null) {
                Roll roll = mService.findBarCodeReg(BarCodeRegType.ROLL, boxRoll.getPartBarcode());
                box.setWeight(MathUtils.sub(box.getWeight(), roll.getRollWeight(), 1));
                ts.setProductWeight(box.getWeight());
            } else {
                Roll roll = mService.findBarCodeReg(BarCodeRegType.ROLL, boxRoll.getRollBarcode());
                box.setWeight(MathUtils.sub(box.getWeight(), roll.getRollWeight(), 1));
                ts.setProductWeight(box.getWeight());

                map.clear();
                map.put("boxBarcode", box.getBoxBarcode());
                List<TrayBoxRoll> trayBoxRolls = mService.findListByMap(TrayBoxRoll.class, map);
                if (trayBoxRolls.size() > 0) {
                    map.clear();
                    map.put("trayBarcode", trayBoxRolls.get(0).getTrayBarcode());
                    List<Tray> trays = mService.findListByMap(Tray.class, map);
                    trays.get(0).setRollCountInTray(trays.get(0).getRollCountInTray() - 1);
                }
            }

            delete(boxRoll);
            update(box);
            update(ts);
        }

        if (type.equals("TRAY")) {
            TrayBoxRoll tbr = findById(TrayBoxRoll.class, id);
            Tray tray = mService.findBarCodeReg(BarCodeRegType.TRAY, tbr.getTrayBarcode());
            map.clear();
            map.put("rollBarcode", tbr.getTrayBarcode());
            TotalStatistics ts = mService.findUniqueByMap(TotalStatistics.class, map);
            if (tbr.getPartBarcode() != null) {
                Roll roll = mService.findBarCodeReg(BarCodeRegType.ROLL, tbr.getPartBarcode());
                tray.setWeight(MathUtils.sub(tray.getWeight(), roll.getRollWeight(), 1));
                tray.setRollCountInTray(tray.getRollCountInTray() - 1);
                ts.setProductWeight(tray.getWeight());
            } else if (tbr.getRollBarcode() != null) {
                Roll roll = mService.findBarCodeReg(BarCodeRegType.ROLL, tbr.getRollBarcode());
                tray.setWeight(MathUtils.sub(tray.getWeight(), roll.getRollWeight(), 1));
                tray.setRollCountInTray(tray.getRollCountInTray() - 1);
                ts.setProductWeight(tray.getWeight());
            } else {
                Box box = mService.findBarCodeReg(BarCodeRegType.BOX, tbr.getBoxBarcode());
                map.clear();
                map.put("boxBarcode", box.getBoxBarcode());
                List<BoxRoll> BoxRolls = mService.findListByMap(BoxRoll.class, map);
                tray.setWeight(MathUtils.sub(tray.getWeight(), box.getWeight(), 1));
                tray.setRollCountInTray(tray.getRollCountInTray() - BoxRolls.size());
                ts.setProductWeight(tray.getWeight());
            }

            delete(tbr);
            update(tray);
            update(ts);
        }
    }
}
