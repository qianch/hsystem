/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.statistics.service.impl;

import com.bluebirdme.mes.common.service.IProcessService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;
import com.bluebirdme.mes.statistics.dao.ITotalStatisticsDao;
import com.bluebirdme.mes.statistics.entity.TotalStatistics;
import com.bluebirdme.mes.statistics.service.ITotalStatisticsService;
import com.bluebirdme.mes.store.entity.*;
import com.bluebirdme.mes.tracings.entity.TracingLog;
import com.bluebirdme.mes.utils.ProductState;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MathUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 徐波
 * @Date 2016-11-26 14:44:04
 */
@Service
@AnyExceptionRollback
public class TotalStatisticsServiceImpl extends BaseServiceImpl implements ITotalStatisticsService {
    @Resource
    ITotalStatisticsDao totalStatisticsDao;
    @Resource
    IProcessService processService;
    @Resource
    IWeavePlanService weavePlanService;

    @Override
    protected IBaseDao getBaseDao() {
        return totalStatisticsDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return totalStatisticsDao.findPageInfo(filter, page);
    }

    @Override
    public Map<String, Object> findPageInfoByBox(Filter filter, Page page) throws Exception {
        return totalStatisticsDao.findPageInfoByBox(filter, page);
    }

    @Override
    public Map<String, Object> findPageInfoByTray(Filter filter, Page page) throws Exception {
        return totalStatisticsDao.findPageInfoByTray(filter, page);
    }

    @Override
    public Map<String, Object> findPageInfoByPart(Filter filter, Page page) throws Exception {
        return totalStatisticsDao.findPageInfoByPart(filter, page);
    }

    public Map<String, Object> findPageInfoByRoll(Filter filter, Page page) throws Exception {
        return totalStatisticsDao.findPageInfoByRoll(filter, page);
    }

    public List<TotalStatistics> getByIds(String ids) {
        List<TotalStatistics> list = new ArrayList<>();
        String[] ids_temp = ids.split(",");
        for (String s : ids_temp) {
            list.add(findById(TotalStatistics.class, Long.parseLong(s)));
        }
        return list;
    }

    public void saveLockState(List<TotalStatistics> list, String complaintCode, String reasons) {
        for (TotalStatistics ts : list) {
            ts.setIsLocked(1);
            String barcode = ts.getRollBarcode();
            iflockByBarcode(barcode, ProductState.FROZEN);
            // 往冻结页面添加数据
            TracingLog t = new TracingLog();
            t.setRollBarcode(ts.getRollBarcode());
            t.setProductModel(ts.getProductModel());
            t.setBatchCode(ts.getBatchCode());
            t.setSalesCode(ts.getSalesOrderCode());
            t.setDeviceCode(ts.getDeviceCode());
            if (complaintCode != null && !complaintCode.equals("")) {
                t.setTriggerInfo("投诉号：" + complaintCode + "：" + reasons);
            } else {
                t.setTriggerInfo("：" + reasons);
            }
            t.setUserName(ts.getIsNameLock());
            t.setLogType(9);
            t.setTriggerDate(new Date());
            System.out.println(t.getTriggerInfo());
            save(t);
            merge(ts);
        }
    }

    public void saveUnLockState(List<TotalStatistics> list, String complaintCode, String reasons) {
        for (TotalStatistics ts : list) {
            ts.setIsLocked(-1);
            String barcode = ts.getRollBarcode();
            iflockByBarcode(barcode, ProductState.VALID);
            // 往冻结页面添加数据
            TracingLog t = new TracingLog();
            t.setRollBarcode(ts.getRollBarcode());
            t.setProductModel(ts.getProductModel());
            t.setBatchCode(ts.getBatchCode());
            t.setSalesCode(ts.getSalesOrderCode());
            t.setDeviceCode(ts.getDeviceCode());
            if (complaintCode != null && !complaintCode.equals("")) {
                t.setTriggerInfo("投诉号：" + complaintCode + "：" + reasons);
            } else {
                t.setTriggerInfo("：" + reasons);
            }
            t.setUserName(ts.getIsNameLock());
            t.setLogType(10);
            t.setTriggerDate(new Date());
            save(t);
            merge(ts);
        }

    }

    public HashSet<String> getBoxRolls(String barcode) {
        HashSet<String> rollSet = new HashSet<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("boxBarcode", barcode);
        List<BoxRoll> boxRoll = this.findListByMap(BoxRoll.class, map);
        for (BoxRoll b : boxRoll) {
            if (b.getRollBarcode() != null) {
                rollSet.add(b.getRollBarcode());
            } else {
                rollSet.add(b.getPartBarcode());
            }
        }
        return rollSet;
    }

    public void iflockByBarcode(String barcodes, int state) {
        String[] barcodeList = barcodes.split(",");
        HashSet<String> rollSet = new HashSet<>();
        HashSet<String> boxSet = new HashSet<>();
        HashSet<String> traySet = new HashSet<>();
        for (String barcode : barcodeList) {
            // 判断是否是卷条码，存入要冻结的卷的集合
            if (barcode.startsWith("R")) {
                rollSet.add(barcode);
            }
            // 判断是否是部件条码，存入要冻结的卷的集合
            HashMap<String, Object> partMap = new HashMap<>();
            partMap.put("partBarcode", barcode);
            Roll roll = findUniqueByMap(Roll.class, partMap);
            if (barcode.startsWith("P") && roll != null) {
                rollSet.add(barcode);
            }
            // 判断是否是箱条码，存入要冻结的箱的集合，遍历箱中的卷和部件条码，存入要冻结的卷的集合
            if (barcode.startsWith("B")) {
                boxSet.add(barcode);
                rollSet.addAll(getBoxRolls(barcode));
            }
            // 判断是否是托条码，存入要冻结的托的集合
            if (barcode.startsWith("T") || barcode.startsWith("P")) {
                traySet.add(barcode);
                HashMap<String, Object> map = new HashMap<>();
                map.put("trayBarcode", barcode);
                List<TrayBoxRoll> trayBoxRoll = this.findListByMap(TrayBoxRoll.class, map);
                // 遍历托箱卷关系，如果是卷或部件，存入要冻结的卷的集合，如果是箱，存入要冻结的箱的集合，并遍历箱中的卷和部件，存入要冻结的卷的集合
                for (TrayBoxRoll tbr : trayBoxRoll) {
                    if (tbr.getRollBarcode() != null) {
                        rollSet.add(tbr.getRollBarcode());
                    }
                    if (tbr.getPartBarcode() != null) {
                        rollSet.add(tbr.getPartBarcode());
                    }
                    if (tbr.getBoxBarcode() != null) {
                        boxSet.add(tbr.getBoxBarcode());
                        rollSet.addAll(getBoxRolls(tbr.getBoxBarcode()));
                    }
                }
            }
        }
        for (String rollBarcode : rollSet) {
            // 判断卷中的条码，如果是R开头，通过rollbarcode查询卷，如果是P开头，通过partBarCode查询
            if (rollBarcode.startsWith("R")) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("rollBarcode", rollBarcode);
                Roll roll = findUniqueByMap(Roll.class, map);
                roll.setState(state);
                update(roll);
            }
            if (rollBarcode.startsWith("P")) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("partBarcode", rollBarcode);
                Roll roll = findUniqueByMap(Roll.class, map);
                roll.setState(state);
                update(roll);
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("rollBarcode", rollBarcode);
            TotalStatistics ts = findUniqueByMap(TotalStatistics.class, map);
            if (state == ProductState.FROZEN)
                ts.setIsLocked(1);
            else
                ts.setIsLocked(-1);
            update(ts);
        }
        for (String barcode : boxSet) {
            isFrozen(barcode);
        }
        for (String barcode : traySet) {
            isFrozen(barcode);
        }

        for (String barcode : barcodeList) {
            // 获取拖箱卷关系，判断是否冻结
            if (barcode.startsWith("R")) {
                // 获取上级条码判断是否冻结
                HashMap<String, Object> map = new HashMap<>();
                map.put("rollBarcode", barcode);
                BoxRoll br = findUniqueByMap(BoxRoll.class, map);
                TrayBoxRoll tbr = findUniqueByMap(TrayBoxRoll.class, map);
                if (br != null) {
                    isFrozen(br.getBoxBarcode());
                    map.clear();
                    map.put("boxBarcode", br.getBoxBarcode());
                    TrayBoxRoll tbr1 = findUniqueByMap(TrayBoxRoll.class, map);
                    if (tbr1 != null) {
                        isFrozen(tbr1.getTrayBarcode());
                    }
                }
                if (tbr != null) {
                    isFrozen(tbr.getTrayBarcode());
                }
            } else if (barcode.startsWith("P")) {
                // 获取上级条码判断是否冻结
                HashMap<String, Object> map = new HashMap<>();
                map.put("partBarcode", barcode);
                BoxRoll br = findUniqueByMap(BoxRoll.class, map);
                TrayBoxRoll tbr = findUniqueByMap(TrayBoxRoll.class, map);
                if (br != null) {
                    isFrozen(br.getBoxBarcode());
                    map.clear();
                    map.put("boxBarcode", br.getBoxBarcode());
                    TrayBoxRoll tbr1 = findUniqueByMap(TrayBoxRoll.class, map);
                    isFrozen(tbr1.getTrayBarcode());
                }
                if (tbr != null) {
                    isFrozen(tbr.getTrayBarcode());
                }
            } else if (barcode.startsWith("B")) {
                // 获取上级条码判断是否冻结
                HashMap<String, Object> map = new HashMap<>();
                map.put("boxBarcode", barcode);
                TrayBoxRoll tbr1 = findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr1 != null) {
                    isFrozen(tbr1.getTrayBarcode());
                }
            }
        }
    }

    public void quality(String barcodes, String state) throws Exception {
        String[] barcodeList = barcodes.split(",");
        HashSet<String> rollSet = new HashSet<>();
        HashSet<String> boxSet = new HashSet<>();
        HashSet<String> traySet = new HashSet<>();
        for (String barcode : barcodeList) {
            HashMap<String, Object> partMap = new HashMap<>();
            partMap.put("partBarcode", barcode);
            Roll part = findUniqueByMap(Roll.class, partMap);
            // 判断是否是卷条码，存入要冻结的卷的集合
            if (barcode.startsWith("R")) {
                rollSet.add(barcode);
            }
            // 判断是否是部件条码，存入要冻结的卷的集合
            if (barcode.startsWith("P") && part != null) {
                rollSet.add(barcode);
            }
            // 判断是否是箱条码，存入要冻结的箱的集合，遍历箱中的卷和部件条码，存入要冻结的卷的集合
            if (barcode.startsWith("B")) {
                boxSet.add(barcode);
                rollSet.addAll(getBoxRolls(barcode));
            }
            // 判断是否是托条码，存入要冻结的托的集合
            if (barcode.startsWith("T") || barcode.startsWith("P")) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("trayBarcode", barcode);
                List<TrayBoxRoll> trayBoxRoll = this.findListByMap(TrayBoxRoll.class, map);
                if (trayBoxRoll.size() > 0) {
                    traySet.add(barcode);
                }
                // 遍历托箱卷关系，如果是卷或部件，存入要冻结的卷的集合，如果是箱，存入要冻结的箱的集合，并遍历箱中的卷和部件，存入要冻结的卷的集合
                for (TrayBoxRoll tbr : trayBoxRoll) {
                    if (tbr.getRollBarcode() != null) {
                        rollSet.add(tbr.getRollBarcode());
                    }
                    if (tbr.getPartBarcode() != null) {
                        rollSet.add(tbr.getPartBarcode());
                    }
                    if (tbr.getBoxBarcode() != null) {
                        boxSet.add(tbr.getBoxBarcode());
                        rollSet.addAll(getBoxRolls(tbr.getBoxBarcode()));
                    }
                }
            }
        }
        BigDecimal bg;
        for (String rollBarcode : rollSet) {
            // 判断卷中的条码，如果是R开头，通过rollbarcode查询卷，如果是P开头，通过partBarCode查询
            if (rollBarcode.startsWith("R")) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("rollBarcode", rollBarcode);
                Roll roll = findUniqueByMap(Roll.class, map);
                roll.setRollQualityGradeCode(state);
                update(roll);
                // 判级 非A的 不参与平均值重量
                if (!state.equals("A")) {
                    Map<String, Object> param = new HashMap<>();
                    param.put("rollBarcode", rollBarcode);
                    Roll rolls = processService.findUniqueByMap(Roll.class, param);
                    param.clear();
                    param.put("barcode", rollBarcode);
                    RollBarcode rbc = processService.findUniqueByMap(RollBarcode.class, param);
                    WeavePlan w = processService.findById(WeavePlan.class, rbc.getPlanId());
                    w.setToVoid(w.getToVoid() == null ? 1 : (w.getToVoid() + 1));
                    bg = new BigDecimal(w.getToVoidWeight() == null ? (rolls.getRollAutoWeight() == null ? 0 : rolls.getRollAutoWeight()) : (w.getToVoidWeight() + (rolls.getRollAutoWeight() == null ? 0 : rolls.getRollAutoWeight())));
                    w.setToVoidWeight(bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
                    weavePlanService.update(w);
                }
            }
            if (rollBarcode.startsWith("P")) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("partBarcode", rollBarcode);
                Roll roll = findUniqueByMap(Roll.class, map);
                roll.setRollQualityGradeCode(state);
                update(roll);
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("rollBarcode", rollBarcode);
            TotalStatistics ts = findUniqueByMap(TotalStatistics.class, map);
            if (ts == null) {
                throw new Exception("综合统计表无数据，请检查是否称重；");
            }
            ts.setRollQualityGradeCode(state);
            update(ts);
        }
        for (String boxBarcode : boxSet) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("boxBarcode", boxBarcode);
            Box box = findUniqueByMap(Box.class, map);
            box.setRollQualityGradeCode(state);
            update(box);
            map.clear();
            map.put("rollBarcode", boxBarcode);
            TotalStatistics ts = findUniqueByMap(TotalStatistics.class, map);
            ts.setRollQualityGradeCode(state);
            update(ts);
        }
        for (String trayBarcode : traySet) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("trayBarcode", trayBarcode);
            Tray tray = findUniqueByMap(Tray.class, map);
            tray.setRollQualityGradeCode(state);
            update(tray);
            map.clear();
            map.put("rollBarcode", trayBarcode);
            TotalStatistics ts = findUniqueByMap(TotalStatistics.class, map);
            ts.setRollQualityGradeCode(state);
            update(ts);
        }
    }

    public boolean isPackaged(String barcode) {
        return totalStatisticsDao.isPackaged(barcode);
    }

    // 判断条码是否为冻结状态
    public Integer isFrozen(String barcode) {
        //P开头的有两种情况，一种是托PHS，一种是部件PCJ
        HashMap<String, Object> rollPartMap = new HashMap<>();
        rollPartMap.put("partBarcode", barcode);
        Roll rollPart = findUniqueByMap(Roll.class, rollPartMap);

        if (barcode.startsWith("R")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("rollBarcode", barcode);
            Roll roll = findUniqueByMap(Roll.class, map);
            return roll.getState();
        } else if (barcode.startsWith("P") && rollPart != null) {
            return rollPart.getState();
        } else if (barcode.startsWith("B")) {
            HashMap<String, Object> map = new HashMap<>();
            HashSet<String> rollSet = getBoxRolls(barcode);
            for (String rollBarcode : rollSet) {
                if (rollBarcode.startsWith("R")) {
                    map.clear();
                    map.put("rollBarcode", rollBarcode);
                    Roll roll = findUniqueByMap(Roll.class, map);
                    if (roll.getState() == ProductState.FROZEN) {
                        map.clear();
                        map.put("rollBarcode", barcode);
                        TotalStatistics ts = findUniqueByMap(TotalStatistics.class, map);
                        if (ts.getIsLocked() != 1) {
                            ts.setIsLocked(1);
                            update(ts);
                        }
                        return ProductState.FROZEN;
                    }
                } else if (rollBarcode.startsWith("P")) {
                    map.clear();
                    map.put("partBarcode", rollBarcode);
                    Roll roll = findUniqueByMap(Roll.class, map);
                    if (roll.getState() == ProductState.FROZEN) {
                        map.clear();
                        map.put("rollBarcode", barcode);
                        TotalStatistics ts = findUniqueByMap(TotalStatistics.class, map);
                        if (ts.getIsLocked() == -1) {
                            ts.setIsLocked(1);
                            update(ts);
                        }
                        return ProductState.FROZEN;
                    }
                }
            }
            map.clear();
            map.put("rollBarcode", barcode);
            TotalStatistics ts = findUniqueByMap(TotalStatistics.class, map);
            if (ts == null)
                return ProductState.VALID;
            if (ts.getIsLocked() != null && ts.getIsLocked() == 1) {
                ts.setIsLocked(-1);
                update(ts);
            }
            return ProductState.VALID;
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("trayBarcode", barcode);
            HashSet<String> rollSet = new HashSet<>();
            List<TrayBoxRoll> trayBoxRoll = this.findListByMap(TrayBoxRoll.class, map);
            // 遍历托箱卷关系，如果是卷或部件，存入要冻结的卷的集合，如果是箱，存入要冻结的箱的集合，并遍历箱中的卷和部件，存入要冻结的卷的集合
            for (TrayBoxRoll tbr : trayBoxRoll) {
                if (tbr.getRollBarcode() != null) {
                    rollSet.add(tbr.getRollBarcode());
                }
                if (tbr.getPartBarcode() != null) {
                    rollSet.add(tbr.getPartBarcode());
                }
                if (tbr.getBoxBarcode() != null) {
                    rollSet.addAll(getBoxRolls(tbr.getBoxBarcode()));
                }
            }
            for (String rollBarcode : rollSet) {
                if (rollBarcode.startsWith("R")) {
                    HashMap<String, Object> map1 = new HashMap<>();
                    map1.put("rollBarcode", rollBarcode);
                    Roll roll = findUniqueByMap(Roll.class, map1);
                    map1.clear();
                    map1.put("barcode", rollBarcode);
                    RollBarcode rb = findUniqueByMap(RollBarcode.class, map1);
                    if (roll.getState() == ProductState.FROZEN) {
                        map.clear();
                        map.put("rollBarcode", barcode);
                        TotalStatistics ts = findUniqueByMap(TotalStatistics.class, map);
                        if (ts.getIsLocked().equals(-1)) {
                            ts.setIsLocked(1);
                            update(ts);
                        }
                        return ProductState.FROZEN;
                    }
                } else if (rollBarcode.startsWith("P")) {
                    HashMap<String, Object> map1 = new HashMap<>();
                    map1.put("partBarcode", rollBarcode);
                    Roll roll = findUniqueByMap(Roll.class, map1);
                    if (roll.getState() == ProductState.FROZEN) {
                        map.clear();
                        map.put("rollBarcode", barcode);
                        TotalStatistics ts = findUniqueByMap(TotalStatistics.class, map);
                        if (ts.getIsLocked().equals(-1)) {
                            ts.setIsLocked(1);
                            update(ts);
                        }
                        return ProductState.FROZEN;
                    }
                }
            }
            map.clear();
            map.put("rollBarcode", barcode);
            TotalStatistics ts = findUniqueByMap(TotalStatistics.class, map);
            if (ts == null)
                return ProductState.VALID;
            if (ts.getIsLocked().equals(1)) {
                ts.setIsLocked(-1);
                update(ts);
            }
            return ProductState.VALID;
        }
    }

    public void changeInfo(String barcode, String parentBarocde, String topBarcode, Double newWeight) {
        // 如果为条码为卷或者部件，根据条码修改对应roll和totalStatistics的重量信息，并记录原信息
        Double oldWeight;
        HashMap<String, Object> map = new HashMap<>();
        if (barcode.startsWith("R")) {
            //处理卷条码
            map.put("rollBarcode", barcode);
            Roll roll = findUniqueByMap(Roll.class, map);
            // 原来重量
            oldWeight = roll.getRollWeight();
            roll.setRollWeight(newWeight);
            TotalStatistics ts = findUniqueByMap(TotalStatistics.class, map);
            ts.setRollWeight(newWeight);
            // 先修改卷重
            update(roll);
            update(ts);
            // 旧的重量，减去新的重量，计算差距
            double diff = MathUtils.sub(oldWeight, newWeight, 1);
            // 查询是否打包在盒中
            BoxRoll br = findOne(BoxRoll.class, "rollBarcode", barcode);
            // 如果打包在盒中
            if (br != null) {
                // 更新盒重量
                Box box = findOne(Box.class, "boxBarcode", br.getBoxBarcode());
                // 旧的重量，减去新的重量，得到新的盒重量
                box.setWeight(MathUtils.sub(box.getWeight(), diff, 1));
                // 更新盒重量
                update(box);

                map.clear();
                map.put("rollBarcode", box.getBoxBarcode());
                ts = findUniqueByMap(TotalStatistics.class, map);
                ts.setRollWeight(box.getWeight());
                // 更新生产统计
                update(ts);
                TrayBoxRoll tbr = findOne(TrayBoxRoll.class, "boxBarcode", box.getBoxBarcode());
                if (tbr != null) {
                    Tray tray = findOne(Tray.class, "trayBarcode", tbr.getTrayBarcode());
                    tray.setWeight(MathUtils.sub(tray.getWeight(), diff, 1));
                    update(tray);
                    map.clear();
                    map.put("rollBarcode", tray.getTrayBarcode());
                    ts = findUniqueByMap(TotalStatistics.class, map);
                    ts.setRollWeight(tray.getWeight());
                    update(ts);
                }
            } else {
                // 如果未打包到盒子中，查询是否直接导报到托中
                TrayBoxRoll tbr = findOne(TrayBoxRoll.class, "rollBarcode", barcode);
                if (tbr != null) {
                    Tray tray = findOne(Tray.class, "trayBarcode", tbr.getTrayBarcode());
                    tray.setWeight(MathUtils.sub(tray.getWeight(), diff, 1));
                    update(tray);
                    map.clear();
                    map.put("rollBarcode", tray.getTrayBarcode());
                    ts = findUniqueByMap(TotalStatistics.class, map);
                    ts.setRollWeight(tray.getWeight());
                    update(ts);
                }
            }
        } else {
            //处理部件条码
            map.put("partBarcode", barcode);
            Roll roll = findUniqueByMap(Roll.class, map);
            oldWeight = roll.getRollWeight();
            roll.setRollWeight(newWeight);
            map.clear();
            map.put("rollBarcode", barcode);
            TotalStatistics ts = findUniqueByMap(TotalStatistics.class, map);
            ts.setProductWeight(newWeight);
            update(roll);
            update(ts);
            // 旧的重量，减去新的重量，计算差距
            double diff = MathUtils.sub(oldWeight, newWeight, 1);
            // 查询是否打包在盒中
            BoxRoll br = findOne(BoxRoll.class, "partBarcode", barcode);
            // 如果打包在盒中
            if (br != null) {
                // 更新盒重量
                Box box = findOne(Box.class, "boxBarcode", br.getBoxBarcode());
                // 旧的重量，减去新的重量，得到新的盒重量
                box.setWeight(MathUtils.sub(box.getWeight(), diff, 1));
                // 更新盒重量
                update(box);

                map.clear();
                map.put("rollBarcode", box.getBoxBarcode());
                ts = findUniqueByMap(TotalStatistics.class, map);
                ts.setRollWeight(box.getWeight());
                // 更新生产统计
                update(ts);
                TrayBoxRoll tbr = findOne(TrayBoxRoll.class, "boxBarcode", box.getBoxBarcode());
                if (tbr != null) {
                    Tray tray = findOne(Tray.class, "trayBarcode", tbr.getTrayBarcode());
                    tray.setWeight(MathUtils.sub(tray.getWeight(), diff, 1));
                    update(tray);
                    map.clear();
                    map.put("rollBarcode", tray.getTrayBarcode());
                    ts = findUniqueByMap(TotalStatistics.class, map);
                    ts.setRollWeight(tray.getWeight());
                    update(ts);
                }
            } else {
                // 如果未打包到盒子中，查询是否直接导报到托中
                TrayBoxRoll tbr = findOne(TrayBoxRoll.class, "rollBarcode", barcode);
                if (tbr != null) {
                    Tray tray = findOne(Tray.class, "trayBarcode", tbr.getTrayBarcode());
                    tray.setWeight(MathUtils.sub(tray.getWeight(), diff, 1));
                    update(tray);

                    map.clear();
                    map.put("rollBarcode", tray.getTrayBarcode());
                    ts = findUniqueByMap(TotalStatistics.class, map);
                    ts.setRollWeight(tray.getWeight());
                    update(ts);
                }
            }
        }
    }

    @Override
    public void judge(String ids, String qualityGrade) throws Exception {
        List<TotalStatistics> list = getByIds(ids);
        for (TotalStatistics ts : list) {
            ts.setRollQualityGradeCode(qualityGrade);
            String barcode = ts.getRollBarcode();
            if (barcode.startsWith("R")) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("rollBarcode", barcode);
                Roll roll = findUniqueByMap(Roll.class, map);
                roll.setRollQualityGradeCode(qualityGrade);
                update(roll);
            } else if (barcode.startsWith("P")) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("partBarcode", barcode);
                Roll roll = findUniqueByMap(Roll.class, map);
                roll.setRollQualityGradeCode(qualityGrade);
                update(roll);
            } else if (barcode.startsWith("B")) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("boxBarcode", barcode);
                Box box = findUniqueByMap(Box.class, map);
                box.setRollQualityGradeCode(qualityGrade);
                update(box);
            } else if (barcode.startsWith("T")) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("trayBarcode", barcode);
                Tray tray = findUniqueByMap(Tray.class, map);
                tray.setRollQualityGradeCode(qualityGrade);
                update(tray);
            }
            update2(ts);
        }
    }

    @Override
    public Map<String, Object> productsShopSummary(Filter filter, Page page) throws Exception {
        return totalStatisticsDao.productsShopSummary(filter, page);
    }

    @Override
    public Map<String, Object> genericFactorySummary(Filter filter, Page page) throws Exception {
        return totalStatisticsDao.genericFactorySummary(filter, page);
    }

    @Override
    public Map<String, Object> getPickingStatistics(Filter filter, Page page) throws Exception {
        return totalStatisticsDao.getPickingStatistics(filter, page);
    }

    @Override
    public SXSSFWorkbook exportDailyStatistics(Filter filter, String searchType) throws Exception {
        Page page = new Page();
        page.setAll(1);
        page.setRows(999);
        Map<String, Object> findPageInfo;
        List<Map<String, Object>> rows = new ArrayList<>();
        if (searchType != null) {
            if ((searchType.equals("roll")) && filter.get("rollBarcode") == null) {
                findPageInfo = findPageInfoByRoll(filter, page);
                rows = (List<Map<String, Object>>) findPageInfo.get("rows");
                if (rows.size() > 0) {
                    for (Map<String, Object> row : rows) {
                        row.put("ROLLCOUNT", 1);
                    }
                }
            } else if (searchType.equals("box") && filter.get("rollBarcode") == null) {
                findPageInfo = findPageInfoByBox(filter, page);
                rows = (List<Map<String, Object>>) findPageInfo.get("rows");
            } else if (searchType.equals("tray") && filter.get("rollBarcode") == null) {
                findPageInfo = findPageInfoByTray(filter, page);
                rows = (List<Map<String, Object>>) findPageInfo.get("rows");
            } else if (searchType.equals("part") && filter.get("rollBarcode") == null) {
                findPageInfo = findPageInfoByPart(filter, page);
                rows = (List<Map<String, Object>>) findPageInfo.get("rows");
                if (rows.size() > 0) {
                    for (Map<String, Object> row : rows) {
                        row.put("ISPACKED", 1);
                    }
                }
            } else if (filter.get("rollBarcode") != null && (filter.get("rollBarcode").startsWith("like:R") || filter.get("rollBarcode").startsWith("like:P"))) {
                findPageInfo = findPageInfo(filter, page);
                rows = (List<Map<String, Object>>) findPageInfo.get("rows");
                if (rows.size() > 0) {
                    for (Map<String, Object> row : rows) {
                        row.put("rollCount", 1);
                        Object rollbarcode = row.get("ROLLBARCODE");
                        if (rollbarcode != null) {
                            // 查询该条码是否成托
                            String topBarcode = TopBarcode(rollbarcode.toString());
                            if (topBarcode.indexOf("T") == 0 || topBarcode.indexOf("P") == 0) {
                                row.put("ISPACKED", 1);
                            } else {
                                row.put("ISPACKED", 0);
                            }
                        }
                    }
                }
            } else if (filter.get("rollBarcode") != null && filter.get("rollBarcode").startsWith("like:B")) {
                findPageInfo = findPageInfoByBox(filter, page);
                rows = (List<Map<String, Object>>) findPageInfo.get("rows");
            } else if (filter.get("rollBarcode") != null && filter.get("rollBarcode").startsWith("like:T")) {
                findPageInfo = findPageInfoByTray(filter, page);
                rows = (List<Map<String, Object>>) findPageInfo.get("rows");
            }
        } else {
            findPageInfo = findPageInfo(filter, page);
            rows = (List<Map<String, Object>>) findPageInfo.get("rows");
        }

        SXSSFWorkbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        Sheet sheet = wb.createSheet();
        Integer xx = null;
        Row row;
        Cell cell;
        String[] columnName = new String[]{"条码号", "条码类型", "计划单号", "订单号", "客户名称", "产品规格", "批次号", "质量等级", "机台号", "车间", "卷重（kg）", "门幅（mm）",
                "称重重量（kg）", "卷长（m）", "实际卷长（m）", "卷数", "生产时间", "操作人", "库存状态",
                "状态", "是否作废", "是否打包", "是否拆包", "备注"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 24; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 23));
        r++;
        row = sheet.createRow(r);
        row.createCell(0);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }
        r++;
        sheet.setColumnWidth(0, 20 * 256);// 设置列宽
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 25 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(11, 10 * 256);
        sheet.setColumnWidth(12, 10 * 256);
        sheet.setColumnWidth(13, 10 * 256);
        sheet.setColumnWidth(14, 10 * 256);
        sheet.setColumnWidth(15, 10 * 256);
        sheet.setColumnWidth(16, 10 * 256);
        sheet.setColumnWidth(17, 10 * 256);
        sheet.setColumnWidth(18, 10 * 256);
        sheet.setColumnWidth(19, 10 * 256);
        sheet.setColumnWidth(20, 10 * 256);
        sheet.setColumnWidth(21, 10 * 256);
        sheet.setColumnWidth(22, 10 * 256);
        sheet.setColumnWidth(23, 10 * 256);
        row = sheet.createRow(r);
        cell = row.createCell(0);

        for (int i = 0; i < rows.size(); i++) {
            row = sheet.createRow(r);
            for (int j = 0; j < columnName.length; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        cell.setCellValue(rows.get(i).get("ROLLBARCODE").toString());
                        break;
                    case 1:
                        if (rows.get(i).get("BARCODETYPE").equals("tray")) {
                            cell.setCellValue("托条码");
                        } else if (rows.get(i).get("BARCODETYPE").equals("box")) {
                            cell.setCellValue("箱条码");
                        } else if (rows.get(i).get("BARCODETYPE").equals("part")) {
                            cell.setCellValue("部件条码");
                        } else {
                            cell.setCellValue("卷条码");
                        }
                        break;
                    case 2:
                        cell.setCellValue(rows.get(i).get("PRODUCEPLANCODE") != null ? rows.get(i).get("PRODUCEPLANCODE").toString() : "");
                        break;
                    case 3:
                        cell.setCellValue(rows.get(i).get("SALESORDERCODE") != null ? rows.get(i).get("SALESORDERCODE").toString() : "");
                        break;
                    case 4:
                        cell.setCellValue(rows.get(i).get("CONSUMERNAME") != null ? rows.get(i).get("CONSUMERNAME").toString() : "");
                        break;
                    case 5:
                        cell.setCellValue(rows.get(i).get("PRODUCTMODEL") != null ? rows.get(i).get("PRODUCTMODEL").toString() : "");
                        break;
                    case 6:
                        cell.setCellValue(rows.get(i).get("BATCHCODE") != null ? rows.get(i).get("BATCHCODE").toString() : "");
                        break;
                    case 7:
                        cell.setCellValue(rows.get(i).get("ROLLQUALITYGRADECODE") != null ? rows.get(i).get("ROLLQUALITYGRADECODE").toString() : "");
                        break;
                    case 8:
                        cell.setCellValue(rows.get(i).get("DEVICECODE") != null ? rows.get(i).get("DEVICECODE").toString() : "");
                        break;
                    case 9:
                        cell.setCellValue(rows.get(i).get("NAME") != null ? rows.get(i).get("NAME").toString() : "");
                        break;
                    case 10:
                        cell.setCellValue(rows.get(i).get("ROLLWEIGHT") != null ? rows.get(i).get("ROLLWEIGHT").toString() : "");
                        break;
                    case 11:
                        if (rows.get(i).get("PRODUCTWIDTH") != null) {
                            cell.setCellValue((Double) rows.get(i).get("PRODUCTWIDTH"));
                        }
                        break;
                    case 12:
                        if (rows.get(i).get("PRODUCTWEIGHT") != null) {
                            cell.setCellValue((Double) rows.get(i).get("PRODUCTWEIGHT"));
                        }
                        break;
                    case 13:
                        if (rows.get(i).get("PRODUCTLENGTH") != null) {
                            cell.setCellValue((Double) rows.get(i).get("PRODUCTLENGTH"));
                        } else {
                            cell.setCellValue("0.00");
                        }
                        break;
                    case 14:
                        if (rows.get(i).get("ROLLREALLENGTH") != null) {
                            cell.setCellValue((Double) rows.get(i).get("ROLLREALLENGTH"));
                        } else {
                            cell.setCellValue("0.00");
                        }
                        break;
                    case 15:
                        if (rows.get(i).get("ROLLBARCODE").toString().startsWith("R")) {
                            cell.setCellValue(1);
                        }
                        if (rows.get(i).get("ROLLCOUNT") != null) {
                            cell.setCellValue(rows.get(i).get("ROLLCOUNT").toString());
                        }
                        break;
                    case 16:
                        if (rows.get(i).get("ROLLOUTPUTTIME") != null) {
                            cell.setCellValue(rows.get(i).get("ROLLOUTPUTTIME").toString());
                        }
                        break;
                    case 17:
                        if (rows.get(i).get("LOGINNAME") != null) {
                            cell.setCellValue(rows.get(i).get("LOGINNAME").toString());
                        }
                        break;
                    case 18:
                        if (rows.get(i).get("STATE") == null || xx.parseInt(rows.get(i).get("STATE").toString()) == 0) {
                            cell.setCellValue("未入库");
                            break;
                        }
                        if (xx.parseInt(rows.get(i).get("STATE").toString()) == 1) {
                            cell.setCellValue("在库");
                        } else if (xx.parseInt(rows.get(i).get("STATE").toString()) == -1) {
                            cell.setCellValue("出库");
                        }
                        break;
                    case 19:
                        if (rows.get(i).get("ISLOCKED") == "1") {
                            cell.setCellValue("冻结");
                        } else {
                            cell.setCellValue("正常");
                        }
                        break;
                    case 20:
                        if (rows.get(i).get("ISABANDON") == null) {
                            cell.setCellValue("正常");
                        } else if (xx.parseInt(rows.get(i).get("ISABANDON").toString()) == 0) {
                            cell.setCellValue("正常");
                        } else if (xx.parseInt(rows.get(i).get("ISABANDON").toString()) == 1) {
                            cell.setCellValue("已作废");
                        }
                        break;
                    case 21:
                        if (rows.get(i).get("ISPACKED") != null) {
                            if (xx.parseInt(rows.get(i).get("ISPACKED").toString()) == 1) {
                                cell.setCellValue("已打包");
                            } else if (xx.parseInt(rows.get(i).get("ISPACKED").toString()) == 0) {
                                cell.setCellValue("未打包");
                            }
                        }
                        break;
                    case 22:
                        if (rows.get(i).get("ROLLBARCODE").toString().indexOf("T") == 0 || rows.get(i).get("ROLLBARCODE").toString().indexOf("P") == 0) {
                            if (rows.get(i).get("ISOPENED") == null) {
                                cell.setCellValue("正常");
                            } else if (xx.parseInt(rows.get(i).get("ISOPENED").toString()) == 0) {
                                cell.setCellValue("正常");
                            } else if (xx.parseInt(rows.get(i).get("ISOPENED").toString()) == 1) {
                                cell.setCellValue("已拆包");
                            }
                        }
                        break;
                    case 23:
                        if (rows.get(i).get("MEMO") != null) {
                            cell.setCellValue(rows.get(i).get("MEMO").toString());
                        }
                        break;
                }
            }
            r++;
        }
        row = sheet.createRow(r);
        row.createCell(0);
        return wb;
    }

    @Override
    public String TopBarcode(String barcode) {
        // 最顶级的条码，默认为当前查询条码，如果有父级条码，改为父级条码，用于最后查询拼装树状结构
        String topBarcode = barcode;
        // 根据条码查询托箱卷关系组织树状关系
        // 如果条码号R/P开头，查询卷箱关系中是否存在箱，如果有箱，查询箱条码的是否有父级托关系，如果没有箱，查询是否在托中，如果都没有，直接返回卷条码
        if (barcode.startsWith("R")) {
            HashMap<String, Object> map = new HashMap();
            map.put("rollBarcode", barcode);
            // 查询出对应的箱条码信息，如果没有，查询托条码信息
            BoxRoll br = findUniqueByMap(BoxRoll.class, map);
            // 根据关系查询出顶级节点
            map.clear();
            if (br != null) {
                map.put("boxBarcode", br.getBoxBarcode());
                TrayBoxRoll tbr = findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                } else {
                    topBarcode = br.getBoxBarcode();
                }
            } else {
                List<TrayBoxRoll> listTrayBoxRoll = find(TrayBoxRoll.class, "rollBarcode", barcode);
                if (listTrayBoxRoll != null && listTrayBoxRoll.size() > 0) {
                    listTrayBoxRoll.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
                    topBarcode = listTrayBoxRoll.get(0).getTrayBarcode();
                }

            }
        } else if (barcode.startsWith("P")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("partBarcode", barcode);
            // 查询出对应的箱条码信息，如果没有，查询托条码信息
            BoxRoll br = findUniqueByMap(BoxRoll.class, map);
            // 根据关系查询出顶级节点
            map.clear();
            if (br != null) {
                map.put("boxBarcode", br.getBoxBarcode());
                TrayBoxRoll tbr = findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                } else {
                    topBarcode = br.getBoxBarcode();
                }
            } else {
                map.put("partBarcode", barcode);
                TrayBoxRoll tbr = findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                }
            }
        } else if (barcode.startsWith("B")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("boxBarcode", barcode);
            TrayBoxRoll tbr = findUniqueByMap(TrayBoxRoll.class, map);
            if (tbr != null) {
                topBarcode = tbr.getTrayBarcode();
            }
        }
        return topBarcode;
    }
}
