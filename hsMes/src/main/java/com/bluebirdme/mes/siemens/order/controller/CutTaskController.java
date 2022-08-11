/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.order.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.siemens.bom.entity.CutGroup;
import com.bluebirdme.mes.siemens.order.entity.CutTask;
import com.bluebirdme.mes.siemens.order.entity.CutTaskDrawings;
import com.bluebirdme.mes.siemens.order.entity.CutTaskSuit;
import com.bluebirdme.mes.siemens.order.service.ICutTaskService;
import com.bluebirdme.mes.utils.HttpUtils;
import com.bluebirdme.mes.utils.MapUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 西门子裁剪任务单Controller
 *
 * @author 高飞
 * @Date 2017-7-26 10:56:16
 */
@Controller
@RequestMapping("/siemens/cutTask")
@Journal(name = "西门子裁剪任务单")
public class CutTaskController extends BaseController {
    /**
     * 西门子裁剪任务单页面
     */
    final String index = "siemens/order/cutTask";
    final String addOrEdit = "siemens/order/cutTaskAddOrEdit";

    @Resource
    ICutTaskService cutTaskService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() {
        List<CutGroup> list = cutTaskService.findAll(CutGroup.class);
        model.addAttribute("groups", GsonTools.toJson(list));
        return new ModelAndView(index, model);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取西门子裁剪任务单列表信息")
    @RequestMapping("list")
    public String getCutTask(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(cutTaskService.findPageInfo(filter, page));
    }


    @Journal(name = "添加西门子裁剪任务单页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(CutTask cutTask) {
        return new ModelAndView(addOrEdit, model.addAttribute("cutTask", cutTask));
    }

    @ResponseBody
    @Journal(name = "保存西门子裁剪任务单", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(CutTask cutTask, Long pcId) throws Exception {
        cutTaskService.save(cutTask, pcId);
        return GsonTools.toJson(cutTask);
    }

    @Journal(name = "编辑西门子裁剪任务单页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(CutTask cutTask) {
        cutTask = cutTaskService.findById(CutTask.class, cutTask.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("cutTask", cutTask));
    }

    @ResponseBody
    @Journal(name = "编辑西门子裁剪任务单", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(CutTask cutTask) throws Exception {
        cutTaskService.update(cutTask);
        return GsonTools.toJson(cutTask);
    }

    @ResponseBody
    @Journal(name = "删除西门子裁剪任务单", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        cutTaskService.delete(CutTask.class, ids);
        return Constant.AJAX_SUCCESS;
    }

    @Journal(name = "获取任务单号")
    @ResponseBody
    @RequestMapping("serial")
    public synchronized String getSerial() {
        return cutTaskService.getSerial();
    }

    @Journal(name = "关闭/启用生产任务单")
    @ResponseBody
    @RequestMapping("close")
    public String close(String id, Integer closed) throws Exception {
        cutTaskService.close(closed, id);
        return ajaxSuccess();
    }

    @Journal(name = "获取图纸")
    @ResponseBody
    @RequestMapping("drawings")
    public String drawings(Long ctId) {
        List<CutTaskDrawings> list = cutTaskService.find(CutTaskDrawings.class, "ctId", ctId);
        return GsonTools.toJson(list);
    }

    @Journal(name = "派工单条码核对表")
    @RequestMapping("checkBarcode")
    public void exportCutTaskOrderBarcode(Long ctId) throws Exception {
        CutTask ct = cutTaskService.findById(CutTask.class, ctId);
        InputStream is = new FileInputStream(PathUtils.getClassPath() + "/template/ctb.xlsx");
        Workbook wb = new XSSFWorkbook(is);
        //创建一个工作表sheet
        Sheet sheet = wb.getSheetAt(0);
        //申明行
        Row row = sheet.createRow(3);

        Cell cell = row.createCell(0);
        cell.setCellValue(ct.getIsClosed() == 0 ? "启用" : "关闭");
        cell = row.createCell(1);
        cell.setCellValue(ct.getTaskCode());
        cell = row.createCell(2);
        cell.setCellValue(ct.getOrderCode());
        cell = row.createCell(3);
        cell.setCellValue(ct.getPartName());
        cell = row.createCell(4);
        cell.setCellValue(ct.getBatchCode());
        cell = row.createCell(5);
        cell.setCellValue(ct.getConsumerSimpleName());
        cell = row.createCell(6);
        cell.setCellValue(ct.getConsumerCategory() == 1 ? "国内" : "国外");
        cell = row.createCell(7);
        cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(ct.getDeliveryDate()));
        cell = row.createCell(8);
        cell.setCellValue(ct.getSuitCount());
        cell = row.createCell(9);
        cell.setCellValue(ct.getAssignSuitCount());
        cell = row.createCell(10);
        cell.setCellValue(ct.getPackedSuitCount());
        cell = row.createCell(11);
        cell.setCellValue(ct.getIsComplete() == 0 ? "未完成" : "完成");
        cell = row.createCell(12);
        cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(ct.getCreateTime()));
        cell = row.createCell(13);
        cell.setCellValue(ct.getCreateUserName());

        List<CutTaskDrawings> list = cutTaskService.find(CutTaskDrawings.class, "ctId", ctId);

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(6 + i);
            cell = row.createCell(0);
            cell.setCellValue(list.get(i).getFarbicModel());
            cell = row.createCell(1);
            cell.setCellValue(list.get(i).getFragmentCode());
            cell = row.createCell(2);
            cell.setCellValue(list.get(i).getFragmentName());
            cell = row.createCell(3);
            cell.setCellValue(list.get(i).getFragmentCountPerDrawings());
            cell = row.createCell(4);
            cell.setCellValue(list.get(i).getNeedToPrintCount());
            cell = row.createCell(5);
            cell.setCellValue(list.get(i).getPrintedCount());
            cell = row.createCell(6);
            cell.setCellValue(list.get(i).getExtraPrintCount());
            cell = row.createCell(7);
            cell.setCellValue(list.get(i).getRePrintCount());
            cell = row.createCell(8);
            cell.setCellValue(list.get(i).getFragmentMemo());
        }
        HttpUtils.download(response, wb, "任务单小部件条码核对表");
    }

    @ResponseBody
    @RequestMapping("view/suit")
    public String viewSuit(Long ctId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        List<CutTaskSuit> suitList = cutTaskService.find(CutTaskSuit.class, "ctId", ctId);
        List<Map<Object, Object>> ret = new ArrayList<>();
        Map<Object, Object> obj;
        for (CutTaskSuit cts : suitList) {
            obj = MapUtils.entityToMap(cts);
            ret.add(obj);
        }
        return GsonTools.toJson(ret);
    }

    @ResponseBody
    @RequestMapping("view/drawings")
    public String viewDrawings(Long ctId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        List<CutTaskDrawings> drawingsList = cutTaskService.find(CutTaskDrawings.class, "ctId", ctId);
        List<Map<Object, Object>> ret = new ArrayList<>();
        Map<Object, Object> obj;
        for (CutTaskDrawings ctd : drawingsList) {
            obj = MapUtils.entityToMap(ctd);
            ret.add(obj);
        }
        return GsonTools.toJson(ret);
    }
}