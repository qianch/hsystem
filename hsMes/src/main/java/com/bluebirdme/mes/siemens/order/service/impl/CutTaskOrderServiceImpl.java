/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.order.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.planner.cut.dao.ICutDailyPlanDetailDao;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MathUtils;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.j2se.StringUtils;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.siemens.barcode.entity.FragmentBarcode;
import com.bluebirdme.mes.siemens.order.dao.ICutTaskOrderDao;
import com.bluebirdme.mes.siemens.order.entity.CutTask;
import com.bluebirdme.mes.siemens.order.entity.CutTaskDrawings;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrder;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrderDrawings;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrderPrintTask;
import com.bluebirdme.mes.siemens.order.service.ICutTaskOrderService;
import com.bluebirdme.mes.siemens.order.service.ICutTaskService;
import com.bluebirdme.mes.utils.MathTool;
import com.bluebirdme.mes.utils.PrintUtils;

/**
 * @author 高飞
 * @Date 2017-7-31 17:04:13
 */
@Service
@AnyExceptionRollback
public class CutTaskOrderServiceImpl extends BaseServiceImpl implements ICutTaskOrderService {

    @Resource
    ICutTaskOrderDao cutTaskOrderDao;

    @Resource
    ICutTaskService cutTaskService;
    @Resource
    ICutDailyPlanDetailDao cutDailyPlanDetailDao;

    @Override
    protected IBaseDao getBaseDao() {
        return cutTaskOrderDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return cutTaskOrderDao.findPageInfo(filter, page);
    }

    public String getSerial() {
        return cutTaskOrderDao.getSerial();
    }

    public List<CutTaskDrawings> getCutTaskDrawings(Long ctId) {
        return cutTaskOrderDao.getCutTaskDrawings(ctId);
    }

    public void save(CutTaskOrder cto) throws Exception {
        int[] suitCountPerDrawings = cutTaskService.getSuitCountPerDrawings(cto.getPartId(),cto.getCtId());

        int lcm = MathTool.LCM(suitCountPerDrawings);

        if (cto.getAssignSuitCount() % lcm != 0) {
            throw new Exception("派工套数不合理，应该为" + lcm + "的整数倍");
        }

        CutTask ct = findById(CutTask.class, cto.getCtId());
        if (ct.getIsClosed() == 1) {
            throw new Exception("任务单已关闭");
        }
        if (cto.getId() != null) {
            CutTaskOrder _cto = findById(CutTaskOrder.class, cto.getId());
            if (_cto.getIsClosed() == 1) {
                throw new Exception("任务单已关闭，无法编辑");
            }
            if (ct.getAssignSuitCount() + (cto.getAssignSuitCount() - _cto.getAssignSuitCount()) > ct.getSuitCount()) {
                throw new Exception("已超出最大派工数量");
            }

            if (getTotalPrintedCount(_cto.getId()) > 0) {
                throw new Exception("派工单已打印条码，无法编辑");
            }

            ct.setAssignSuitCount(ct.getAssignSuitCount() + cto.getAssignSuitCount() - _cto.getAssignSuitCount());

            _cto.setAssignSuitCount(cto.getAssignSuitCount());
            _cto.setCtoGroupLeader(cto.getCtoGroupLeader());
            _cto.setCtoGroupName(cto.getCtoGroupName());

            super.update(_cto);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ctoId", _cto.getId());
            // 删除旧的派工单图纸
            delete(CutTaskOrderDrawings.class, map);

        } else {
            if (ct.getAssignSuitCount() + cto.getAssignSuitCount() > ct.getSuitCount()) {
                throw new Exception("已超出最大派工数量");
            }
            ct.setAssignSuitCount(ct.getAssignSuitCount() + cto.getAssignSuitCount());
            cto.setPackedSuitCount(0);
            super.save(cto);
        }

        List<CutTaskDrawings> ctdList = find(CutTaskDrawings.class, "ctId", cto.getCtId());
        List<CutTaskOrderDrawings> list = new ArrayList<CutTaskOrderDrawings>();
        CutTaskOrderDrawings ctod = null;
        for (CutTaskDrawings ctd : ctdList) {
            ctod = ctd.convert2(CutTaskOrderDrawings.class);
            ctod.setCtoId(cto.getId());
            ctod.setPrintedCount(0);
            ctod.setRePrintCount(0);
            ctod.setExtraPrintCount(0);
            // 胚布卷数
            ctod.setFarbicRollCount(MathUtils.div(cto.getAssignSuitCount(), ctod.getSuitCountPerDrawings(), 1) + "");
            ctod.setNeedToPrintCount(ctod.getFragmentCountPerDrawings() * (cto.getAssignSuitCount() / ctod.getSuitCountPerDrawings()));
            list.add(ctod);
        }
        saveList(list);

        update(ct);
    }

    public void deleteTask(String id) throws Exception {
        cutTaskOrderDao.deleteTask(id);
    }

    public void close(String id, Integer closed) throws Exception {
        cutTaskOrderDao.close(id, closed);
    }

    /**
     * 条码打印
     */
    public void printBarcode(Long ctoId, String[] drawingsNo, Integer suitCount, String printer, String cutPlanId, String user) throws Exception {
        // 生成条码号和打印的txt文件
        List<String> content = genBarcode(ctoId, drawingsNo, cutPlanId, suitCount, user);
        // 调用打印机打印
        // String file="D:\\muban\\恒石条码(裁片).txt";
        String btw = new File(PathUtils.getClassPath()) + File.separator + "BtwFiles" + File.separator + "恒石条码(裁片).btw";
        PrintUtils.print(content, btw, printer);
    }

    private List<String> genBarcode(Long ctoId, String[] drawingsNo, String cutPlanId, Integer suitCount, String user) throws Exception {

        List<String> content = new ArrayList<String>();

        CutTaskOrder cto = findById(CutTaskOrder.class, ctoId);

        if (cto.getIsClosed() == 1) {
            throw new Exception("派工单已关闭，无法打印条码");
        }

        int[] suitCountPerDrawings = cutTaskOrderDao.getSuitCountPerDrawings(drawingsNo, ctoId);

        int lcm = MathTool.LCM(suitCountPerDrawings);

        if (suitCount % lcm != 0) {
            throw new Exception("套数不合理，应该为" + lcm + "的整数倍");
        }

        Long ctId = cto.getCtId();

        List<FragmentBarcode> fbs = new ArrayList<FragmentBarcode>();

        // 前端选择的，要打印的图纸号
        List<CutTaskOrderDrawings> list = cutTaskOrderDao.getDrawings(drawingsNo, ctoId);

        FragmentBarcode fb = null;
        List<Map<String, Object>> workshopList = cutDailyPlanDetailDao.findWorkShop(cutPlanId);
        String preffix = "";
        if (workshopList.size() == 0) {
            preffix = preffix("1");
        } else {
            preffix = preffix((String) workshopList.get(0).get("WORKSHOPCODE"));
        }

        FragmentBarcode latest = cutTaskOrderDao.getLatestFragmentBarcode(preffix);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String printTime = sdf.format(new Date());
        String suffix = null;

        Map<String, Integer> updateList = new HashMap<String, Integer>();
        List<Long> dwIds = new ArrayList<Long>();

        int start = 0;
        for (CutTaskOrderDrawings c : list) {

            // 打印数量不可以超出应打数量
            if ((c.getPrintedCount() + c.getFragmentCountPerDrawings() * (suitCount / c.getSuitCountPerDrawings())) > c.getNeedToPrintCount()) {
                throw new Exception("图号: " + c.getFragmentDrawingNo() + " 裁片名称: " + c.getFragmentName() + " 打印数量 " + (c.getPrintedCount() + c.getFragmentCountPerDrawings() * (suitCount / c.getSuitCountPerDrawings())) + " 超出应打数量 " + c.getNeedToPrintCount());
            }

            // 条码打印数量=图纸BOM数量*套数
            for (int i = 0; i < (suitCount / c.getSuitCountPerDrawings()) * c.getFragmentCountPerDrawings(); i++) {
                fb = new FragmentBarcode();
                if (start == 0) {
                    if (latest == null) {
                        start = 1;
                    } else {
                        start = Integer.parseInt(latest.getBarcode().replace(preffix, "")) + 1;
                    }
                }

                start++;

                suffix = "0000000" + start + "";
                suffix = suffix.substring(suffix.length() - 5);

                fb.setBarcode(preffix + suffix);
                fb.setBatchCode(cto.getBatchCode());
                fb.setConsumerCategory(cto.getConsumerCategory() == 1 ? "国内" : "国外");
                fb.setConsumerName(cto.getConsumerSimpleName());
                fb.setCtCode(cto.getTaskCode());
                fb.setCtoCode(cto.getCtoCode());
                fb.setDeviceCode(null);
                fb.setFragmentLength(c.getFragmentLength());
                fb.setFragmentCode(c.getFragmentCode());
                fb.setFragmentName(c.getFragmentName());
                fb.setFragmentWeight(c.getFragmentWeight() + "");
                fb.setFragmentWidth(c.getFragmentWidth());
                fb.setGroupLeader(cto.getCtoGroupLeader());
                fb.setGroupName(cto.getCtoGroupName());
                fb.setIsPacked(0);
                fb.setOrderCode(cto.getOrderCode());
                fb.setPartName(cto.getPartName());
                fb.setPrintTime(printTime);
                fb.setPrintUser(user);
                if (workshopList.size() > 0) {
                    fb.setWorkshop(workshopList.get(0).get("WORKSHOP").toString());
                    fb.setWorkShopCode(workshopList.get(0).get("WORKSHOPCODE").toString());
                } else {
                    fb.setWorkshop("裁剪一车间");
                    fb.setWorkShopCode("00116");
                }
                fb.setPrintType("常规");
                fb.setFragmentMemo(c.getFragmentMemo());
                fb.setFragmentDrawingNo(c.getFragmentDrawingNo());
                fb.setFragmentDrawingVer(c.getFragmentDrawingVer());
                fb.setFarbicModel(c.getFarbicModel());
                fb.setPrintSort(c.getPrintSort());
                fb.setCtoId(ctoId);
                fb.setCtId(ctId);
                fb.setDwId(c.getDwId());
                fbs.add(fb);
                content.add(StringUtils.trimAll(fb.getPartName()).replaceAll("\n|\r", "") + " \t "
                        + StringUtils.trimAll(fb.getFarbicModel()).replaceAll("\n|\r", "") + " \t"
                        + StringUtils.trimAll(fb.getFragmentName()).replaceAll("\n|\r", "") + " \t:"
                        + fb.getFragmentLength().replaceAll("\n|\r", "") + "M \tW:"
                        + fb.getFragmentWidth().replaceAll("\n|\r", "") + "MM \t"
                        + fb.getFragmentMemo().replaceAll("\n|\r", "") + " \t"
                        + fb.getGroupName().replaceAll("\n|\r", "") + " \t"
                        + fb.getBarcode() + "\t"
                        + fb.getFragmentDrawingNo());
            }
            // 设置已打印数量
            dwIds.add(c.getDwId());
            updateList.put(c.getDwId().toString(), c.getFragmentCountPerDrawings() * (suitCount / c.getSuitCountPerDrawings()));
        }
        super.saveList(fbs);
        // 更新任务打打印套数
        super.update(cto);
        cutTaskOrderDao.updatePrintedCount(updateList, dwIds, ctId, ctoId);
        return content;
    }

    private static String preffix(String workShop) {
        Calendar c = Calendar.getInstance();

        String year = "000" + (c.get(Calendar.YEAR) - 1999);
        String day = "00" + c.get(Calendar.DAY_OF_YEAR);

        year = year.substring(year.length() - 3, year.length());
        day = day.substring(day.length() - 3, day.length());

        String dateString = year + day;
        String preffix = "CCJ1";
        if (workShop.equals("00117")) {
            preffix = "CCJ2";
        }
        return preffix + dateString;
    }

    public void rePrint(Long ctoId, Long dwId, Integer rePrintCount, String printer, String user, String reason) throws Exception {
        CutTaskOrder cto = findById(CutTaskOrder.class, ctoId);
        if (cto.getIsClosed() == 1) {
            throw new Exception("派工单已关闭，无法打印条码");
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ctId", cto.getCtId());
        map.put("dwId", dwId);
        CutTaskDrawings ctd = findUniqueByMap(CutTaskDrawings.class, map);
        ctd.setRePrintCount(ctd.getRePrintCount() + 1);

        map.put("ctoId", ctoId);
        map.remove("ctId");
        CutTaskOrderDrawings ctod = findUniqueByMap(CutTaskOrderDrawings.class, map);
        ctod.setRePrintCount(ctod.getRePrintCount() + 1);

        update(ctd);
        update(ctod);

        Long ctId = cto.getCtId();
        FragmentBarcode fb = null;
        List<FragmentBarcode> list = new ArrayList<FragmentBarcode>();
        List<Map<String, Object>> workshopList = cutDailyPlanDetailDao.findWorkShop(cto.getCutPlanId() + "");
        String preffix = "";
        if (workshopList.size() == 0) {
            preffix = preffix("1");
        } else {
            preffix = preffix((String) workshopList.get(0).get("WORKSHOPCODE"));
        }
        String suffix = "";
        FragmentBarcode latest = cutTaskOrderDao.getLatestFragmentBarcode(preffix);
        List<String> content = new ArrayList<String>();

        for (int i = 0; i < rePrintCount; i++) {
            fb = new FragmentBarcode();
            if (latest == null) {
                suffix = "00001";
            } else {
                suffix = "0000" + (Integer.parseInt(latest.getBarcode().replace(preffix, "")) + (i + 1));
                suffix = suffix.substring(suffix.length() - 5);
            }
            fb.setBarcode(preffix + suffix);
            fb.setBatchCode(cto.getBatchCode());
            fb.setConsumerCategory(cto.getConsumerCategory() == 1 ? "国内" : "国外");
            fb.setConsumerName(cto.getConsumerSimpleName());
            fb.setCtCode(cto.getTaskCode());
            fb.setCtoCode(cto.getCtoCode());
            fb.setDeviceCode(null);
            fb.setFragmentLength(ctod.getFragmentLength());
            fb.setFragmentName(ctod.getFragmentName());
            fb.setFragmentWeight(ctod.getFragmentWeight() + "");
            fb.setFragmentCode(ctod.getFragmentCode());
            fb.setFragmentWidth(ctod.getFragmentWidth());
            fb.setGroupLeader(cto.getCtoGroupLeader());
            fb.setGroupName(cto.getCtoGroupName());
            fb.setIsPacked(0);
            fb.setOrderCode(cto.getOrderCode());
            fb.setPartName(cto.getPartName());
            fb.setPrintTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            fb.setPrintUser(user);
            if (workshopList.size() > 0) {
                fb.setWorkshop(workshopList.get(0).get("WORKSHOP").toString());
                fb.setWorkShopCode(workshopList.get(0).get("WORKSHOPCODE").toString());
            } else {
                fb.setWorkshop("裁剪一车间");
                fb.setWorkShopCode("00116");
            }
            fb.setPrintType("重打");
            fb.setFragmentMemo(ctod.getFragmentMemo());
            fb.setFragmentDrawingNo(ctod.getFragmentDrawingNo());
            fb.setFragmentDrawingVer(ctod.getFragmentDrawingVer());
            fb.setFarbicModel(ctod.getFarbicModel());
            fb.setPrintSort(ctod.getPrintSort());
            fb.setCtoId(ctoId);
            fb.setCtId(ctId);
            fb.setDwId(ctod.getDwId());
            fb.setRePrintReason(reason);

            list.add(fb);
            content.add(StringUtils.trimAll(fb.getPartName()).replaceAll("\r|\n", "") + " \t "
                    + StringUtils.trimAll(fb.getFarbicModel()).replaceAll("\r|\n", "") + " \t"
                    + StringUtils.trimAll(fb.getFragmentName()).replaceAll("\r|\n", "") + " \tL:"
                    + StringUtils.trimAll(fb.getFragmentLength()).replaceAll("\r|\n", "") + "M \tW:"
                    + StringUtils.trimAll(fb.getFragmentWidth()).replaceAll("\r|\n", "") + "MM \t"
                    + StringUtils.trimAll(fb.getFragmentMemo()).replaceAll("\r|\n", "") + " \t"
                    + StringUtils.trimAll(fb.getGroupName()).replaceAll("\r|\n", "") + " \t"
                    + StringUtils.trimAll(fb.getBarcode()).replaceAll("\r|\n", "") + "\t"
                    + StringUtils.trimAll(fb.getFragmentDrawingNo()).replaceAll("\r|\n", ""));
        }
        super.saveList(list);
        String btw = new File(PathUtils.getClassPath()) + File.separator + "BtwFiles" + File.separator + "恒石条码(裁片).btw";
        PrintUtils.print(content, btw, printer);
    }

    public int getTotalPrintedCount(Long ctoId) {
        return cutTaskOrderDao.getTotalPrintedCount(ctoId);
    }

    public static void main(String[] args) {
        System.out.println(Calendar.getInstance().get(Calendar.YEAR));
    }

    @Override
    public String getDrawingNo(Long ctoId) {
        return cutTaskOrderDao.getDrawingNo(ctoId);
    }

    public CutTaskOrderPrintTask task(String ip, String ctoCode, String drawingNo) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ctoCode", ctoCode);
        param.put("ip", ip);
        param.put("drawingNo", drawingNo);
        CutTaskOrderPrintTask task = findUniqueByMap(CutTaskOrderPrintTask.class, param);
        return task;
    }

    public void print(String printer, String ip, Long ctoId, Long drawingId, String order, Integer levelCount) throws Exception {
        // 先查询上一个任务
        CutTaskOrderPrintTask task = findById(CutTaskOrderPrintTask.class, ctoId);
        CutTaskOrderDrawings draw = findById(CutTaskOrderDrawings.class, drawingId);
        CutTaskOrder cto = findById(CutTaskOrder.class, ctoId);
        if (task == null) {
            task = new CutTaskOrderPrintTask();
            task.setCtoCode(task.getCtoCode());
            task.setIp(ip);
            task.setCurrentPrintOrder(order);
            task.setLeveCount(levelCount + "");
            task.setDrawingNo(draw.getFragmentDrawingNo());
            save(task);
        } else {
            task.setCurrentPrintOrder(order);
            update(task);
        }
        List<String> content = genBarcode2(cto, draw, levelCount, ip);
        String btw = new File(PathUtils.getClassPath()) + File.separator + "btwFile" + File.separator + "恒石条码(裁片).btw";
        PrintUtils.print(content, btw, printer);
    }

    private List<String> genBarcode2(CutTaskOrder cto, CutTaskOrderDrawings c, Integer levelCount, String user) throws Exception {
        List<String> content = new ArrayList<String>();
        if (cto.getIsClosed() == 1) {
            throw new Exception("派工单已关闭，无法打印条码");
        }

        Long ctId = cto.getCtId();
        List<FragmentBarcode> fbs = new ArrayList<FragmentBarcode>();
        FragmentBarcode fb = null;
        String preffix = preffix("1");
        FragmentBarcode latest = cutTaskOrderDao.getLatestFragmentBarcode(preffix("1"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String printTime = sdf.format(new Date());
        String suffix = null;
        int start = 0;
        // 打印数量不可以超出应打数量
        if ((c.getPrintedCount() + c.getFragmentCountPerDrawings() * levelCount) > c.getNeedToPrintCount()) {
            throw new Exception("图号: " + c.getFragmentDrawingNo() + " 裁片名称: " + c.getFragmentName() + " 打印数量 " + (c.getPrintedCount() + c.getFragmentCountPerDrawings() * levelCount) + " 超出应打数量 " + c.getNeedToPrintCount());
        }

        // 条码打印数量=图纸BOM数量*套数
        for (int i = 0; i < levelCount * c.getFragmentCountPerDrawings(); i++) {
            fb = new FragmentBarcode();
            if (start == 0) {
                if (latest == null) {
                    start = 1;
                } else {
                    start = Integer.parseInt(latest.getBarcode().replace(preffix, "")) + 1;
                }
            }
            start++;
            suffix = "0000000" + start + "";
            suffix = suffix.substring(suffix.length() - 5);

            fb.setBarcode(preffix + suffix);
            fb.setBatchCode(cto.getBatchCode());
            fb.setConsumerCategory(cto.getConsumerCategory() == 1 ? "国内" : "国外");
            fb.setConsumerName(cto.getConsumerSimpleName());
            fb.setCtCode(cto.getTaskCode());
            fb.setCtoCode(cto.getCtoCode());
            fb.setDeviceCode(null);
            fb.setFragmentLength(c.getFragmentLength());
            fb.setFragmentCode(c.getFragmentCode());
            fb.setFragmentName(c.getFragmentName());
            fb.setFragmentWeight(c.getFragmentWeight() + "");
            fb.setFragmentWidth(c.getFragmentWidth());
            fb.setGroupLeader(cto.getCtoGroupLeader());
            fb.setGroupName(cto.getCtoGroupName());
            fb.setIsPacked(0);
            fb.setOrderCode(cto.getOrderCode());
            fb.setPartName(StringUtils.trimAll(cto.getPartName()));
            fb.setPrintTime(printTime);
            fb.setPrintUser(user);
            fb.setWorkshop("裁剪车间");
            fb.setPrintType("常规");
            fb.setFragmentMemo(c.getFragmentMemo());
            fb.setFragmentDrawingNo(c.getFragmentDrawingNo());
            fb.setFragmentDrawingVer(c.getFragmentDrawingVer());
            fb.setFarbicModel(c.getFarbicModel());
            fb.setPrintSort(c.getPrintSort());
            fb.setCtoId(cto.getId());
            fb.setCtId(ctId);
            fb.setDwId(c.getDwId());
            fbs.add(fb);
            content.add(StringUtils.trimAll(fb.getPartName()).replaceAll("\r|\n", "") + " \t "
                    + StringUtils.trimAll(fb.getFarbicModel()).replaceAll("\r|\n", "") + " \t"
                    + StringUtils.trimAll(fb.getFragmentName()).replaceAll("\r|\n", "") + " \t:"
                    + StringUtils.trimAll(fb.getFragmentLength()).replaceAll("\r|\n", "") + "M \tW:"
                    + StringUtils.trimAll(fb.getFragmentWidth()).replaceAll("\r|\n", "") + "MM \t"
                    + StringUtils.trimAll(fb.getFragmentMemo()).replaceAll("\r|\n", "") + " \t"
                    + StringUtils.trimAll(fb.getGroupName()).replaceAll("\r|\n", "") + " \t"
                    + StringUtils.trimAll(fb.getBarcode()).replaceAll("\r|\n", "") + "\t"
                    + StringUtils.trimAll(fb.getFragmentDrawingNo().replaceAll("\r|\n", "")));
        }
        // 设置已打印数量
//		dwIds.add(c.getDwId());

        c.setPrintedCount(c.getPrintedCount() + c.getFragmentCountPerDrawings() * levelCount);

//		updateList.put(c.getDwId().toString(), c.getFragmentCountPerDrawings() * (suitCount / c.getSuitCountPerDrawings()));
        super.saveList(fbs);
        // 更新任务打打印套数
//		super.update(cto);
        return content;
    }

    public CutTaskOrderDrawings next(String ip, String ctoCode, String drawingNo) {
        CutTaskOrder cto = findOne(CutTaskOrder.class, "ctoCode", ctoCode);
        CutTaskOrderPrintTask task = task(ip, ctoCode, drawingNo);
        int index = 0;

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ctoId", cto.getId());
        param.put("fragmentDrawingNo", drawingNo);
        List<CutTaskOrderDrawings> list = findListByMap(CutTaskOrderDrawings.class, param);

        Collections.sort(list, new Comparator<CutTaskOrderDrawings>() {
            @Override
            public int compare(CutTaskOrderDrawings o1, CutTaskOrderDrawings o2) {
                return o1.getPrintSort() - o2.getPrintSort();
            }
        });

        if (task != null) {
            index = Integer.parseInt(task.getCurrentPrintOrder()) + 1;
        }

        if (list.get(index) == null) {
            index = 0;
        }

        return list.get(index);
    }

}
