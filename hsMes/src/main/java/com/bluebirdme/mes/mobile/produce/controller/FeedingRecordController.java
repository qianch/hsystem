/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.mobile.produce.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.utils.HttpUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.mobile.produce.entity.FeedingRecord;
import com.bluebirdme.mes.mobile.produce.service.IFeedingRecordService;
import com.bluebirdme.mes.printer.entity.MyException;
import com.bluebirdme.mes.stock.entity.ProductStockState;

/**
 * 投料记录Controller
 *
 * @author 徐波
 * @Date 2016-11-8 14:03:32
 */
@Controller
@RequestMapping("/feedingRecord")
@Journal(name = "投料记录")
public class FeedingRecordController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(FeedingRecordController.class);
    // 投料记录页面
    final String index = "produce/feeding/weave/feedingRecord1";
    final String addOrEdit = "produce/feeding/weave/feedingRecordAddOrEdit";
    final String index2 = "produce/feeding/cut/feedingRecord1";
    @Resource
    IFeedingRecordService feedingRecordService;

    @Journal(name = "首页")
    @RequestMapping(value = "weave", method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @Journal(name = "首页")
    @RequestMapping(value = "cut", method = RequestMethod.GET)
    public String index2() {
        return index2;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取投料记录列表信息")
    @RequestMapping("list")
    public String getFeedingRecord(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(feedingRecordService.findPageInfo(filter, page));
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取投料记录列表信息")
    @RequestMapping("list2")
    public String getFeedingRecord2(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(feedingRecordService.findPageInfo2(filter, page));
    }

    @Journal(name = "添加投料记录页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(FeedingRecord feedingRecord) {
        return new ModelAndView(addOrEdit, model.addAttribute("feedingRecord", feedingRecord));
    }

    @ResponseBody
    @Journal(name = "保存投料记录", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(FeedingRecord feedingRecord) {
        try {
            feedingRecordService.add(feedingRecord);
        } catch (MyException e) {
            logger.error(e.getLocalizedMessage(), e);
            return ajaxError(e.getExceptionMessage());
        }
        return GsonTools.toJson(feedingRecord);
    }

    @Journal(name = "编辑投料记录页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(FeedingRecord feedingRecord) {
        feedingRecord = feedingRecordService.findById(FeedingRecord.class, feedingRecord.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("feedingRecord", feedingRecord));
    }

    @ResponseBody
    @Journal(name = "编辑投料记录", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(FeedingRecord feedingRecord) throws Exception {
        feedingRecordService.update(feedingRecord);
        return GsonTools.toJson(feedingRecord);
    }

    @ResponseBody
    @Journal(name = "删除投料记录", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        feedingRecordService.delete(FeedingRecord.class, ids);
        return Constant.AJAX_SUCCESS;
    }

    /**
     * 裁剪投料导出
     *
     * @param filter
     * @throws Exception
     */
    @NoAuth
    @ResponseBody
    @Journal(name = "裁剪投料导出")
    @RequestMapping("export")
    public void getForceMaterialExport(Filter filter) throws Exception {
        Page page = new Page();
        page.setRows(99999);
        Map<String, Object> materialMap = feedingRecordService.findPageInfo2(filter, page);
        List<Map<String, Object>> list = (List) materialMap.get("rows");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String templateName = "裁剪投料";
        Workbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);

        Sheet sheet = wb.createSheet();
        // sheet.setDisplayGridlines(true);
        Row row = null;
        Cell cell = null;
        String columnName[] = new String[]{"客户简称", "订单", "批次号", "计划号", "产品型号", "产品名称", "胚布条码", "胚布名称", "机台", "操作人", "投料日期"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司裁剪投料记录表");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 12; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
        r++;

        row = sheet.createRow(r);
        sheet.setColumnWidth(0, 15 * 256);// 设置列宽
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 18 * 256);
        sheet.setColumnWidth(3, 22 * 256);
        sheet.setColumnWidth(4, 18 * 256);
        sheet.setColumnWidth(5, 18 * 256);
        sheet.setColumnWidth(6, 18 * 256);
        sheet.setColumnWidth(7, 18 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        sheet.setColumnWidth(9, 10 * 256);
        sheet.setColumnWidth(10, 20 * 256);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }
        r++;
        for (Map<String, Object> data : list) {
            row = sheet.createRow(r);
            for (int j = 0; j < columnName.length; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        //客户简称
                        if (data.get("CONSUMERSIMPLENAME") != null) {
                            cell.setCellValue(data.get("CONSUMERSIMPLENAME").toString());
                        }
                        break;
                    case 1:
                        //订单号
                        if (data.get("SALESORDERCODE") != null) {
                            cell.setCellValue(data.get("SALESORDERCODE").toString());
                        }
                        break;
                    case 2:
                        //批次号
                        if (data.get("BATCHCODE") != null) {
                            cell.setCellValue(data.get("BATCHCODE").toString());
                        }
                        break;

                    case 3:
                        //计划单号
                        if (data.get("PLANCODE") != null) {
                            cell.setCellValue(data.get("PLANCODE").toString());
                        }
                        break;

                    case 4:
                        //产品型号
                        if (data.get("PRODUCTMODEL") != null) {
                            cell.setCellValue(data.get("PRODUCTMODEL").toString());
                        }
                        break;
                    case 5:
                        //产品名称
                        if (data.get("PRODUCTNAME") != null) {
                            cell.setCellValue(data.get("PRODUCTNAME").toString());
                        }
                        break;
                    case 6:
                        //胚布条码
                        if (data.get("ROLLCODE") != null) {
                            cell.setCellValue(data.get("ROLLCODE").toString());
                        }
                        break;
                    case 7:
                        //胚布名称
                        if (data.get("FEEDPRODUCTNAME") != null) {
                            cell.setCellValue(data.get("FEEDPRODUCTNAME").toString());
                        }
                        break;

                    case 8:
                        //机台
                        if (data.get("DEVICECODE") != null) {
                            cell.setCellValue(data.get("DEVICECODE").toString());
                        }
                        break;
                    case 9:
                        //操作人
                        if (data.get("USERNAME") != null) {
                            cell.setCellValue(data.get("USERNAME").toString());
                        }
                        break;
                    case 10:
                        //投料日期
                        if (data.get("FEEDINGDATE") != null) {
                            cell.setCellValue(sf.format(data.get("FEEDINGDATE")));
                        }
                        break;
                }
            }
            r++;
        }
        HttpUtils.download(response, wb, templateName);
    }

    /**
     * 编织投料导出
     *
     * @param filter
     * @throws Exception
     */
    @NoAuth
    @ResponseBody
    @Journal(name = "编织投料导出")
    @RequestMapping("weaveExport")
    public void getweaveExport(Filter filter) throws Exception {
        Page page = new Page();
        page.setRows(99999);
        Map<String, Object> materialMap = feedingRecordService.findPageInfo(filter, page);
        List<Map<String, Object>> list = (List) materialMap.get("rows");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String templateName = "编织投料";
        Workbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);

        Sheet sheet = wb.createSheet();
        // sheet.setDisplayGridlines(true);
        Row row = null;
        Cell cell = null;
        String columnName[] = new String[]{"客户简称", "订单", "批次号", "计划号", "产品型号", "产品名称", "原料条码", "原料型号", "机台", "操作人", "投料日期", "重量(KG)"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司裁剪投料记录表");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 12; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
        r++;
        row = sheet.createRow(r);
        sheet.setColumnWidth(0, 15 * 256);// 设置列宽
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 18 * 256);
        sheet.setColumnWidth(3, 22 * 256);
        sheet.setColumnWidth(4, 18 * 256);
        sheet.setColumnWidth(5, 18 * 256);
        sheet.setColumnWidth(6, 18 * 256);
        sheet.setColumnWidth(7, 18 * 256);
        sheet.setColumnWidth(8, 6 * 256);
        sheet.setColumnWidth(9, 10 * 256);
        sheet.setColumnWidth(10, 20 * 256);
        sheet.setColumnWidth(11, 18 * 256);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }
        r++;
        for (Map<String, Object> data : list) {
            row = sheet.createRow(r);
            for (int j = 0; j < columnName.length; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        //客户简称
                        if (data.get("CONSUMERSIMPLENAME") != null) {
                            cell.setCellValue(data.get("CONSUMERSIMPLENAME").toString());
                        }
                        break;
                    case 1:
                        //订单号
                        if (data.get("SALESORDERCODE") != null) {
                            cell.setCellValue(data.get("SALESORDERCODE").toString());
                        }
                        break;
                    case 2:
                        //批次号
                        if (data.get("BATCHCODE") != null) {
                            cell.setCellValue(data.get("BATCHCODE").toString());
                        }
                        break;

                    case 3:
                        //计划单号
                        if (data.get("PLANCODE") != null) {
                            cell.setCellValue(data.get("PLANCODE").toString());
                        }
                        break;

                    case 4:
                        //产品型号
                        if (data.get("PRODUCTMODEL") != null) {
                            cell.setCellValue(data.get("PRODUCTMODEL").toString());
                        }
                        break;
                    case 5:
                        //产品名称
                        if (data.get("PRODUCTNAME") != null) {
                            cell.setCellValue(data.get("PRODUCTNAME").toString());
                        }
                        break;
                    case 6:
                        //原料条码
                        if (data.get("MATERIALCODE") != null) {
                            cell.setCellValue(data.get("MATERIALCODE").toString());
                        }
                        break;
                    case 7:
                        //原料型号
                        if (data.get("MATERIALMODEL") != null) {
                            cell.setCellValue(data.get("MATERIALMODEL").toString());
                        }
                        break;

                    case 8:
                        //机台
                        if (data.get("DEVICECODE") != null) {
                            cell.setCellValue(data.get("DEVICECODE").toString());
                        }
                        break;
                    case 9:
                        //操作人
                        if (data.get("USERNAME") != null) {
                            cell.setCellValue(data.get("USERNAME").toString());
                        }
                        break;
                    case 10:
                        //投料日期
                        if (data.get("FEEDINGDATE") != null) {
                            cell.setCellValue(sf.format(data.get("FEEDINGDATE")));
                        }
                        break;
                    case 11:
                        //重量
                        if (data.get("WEIGHT") != null) {
                            cell.setCellValue(data.get("WEIGHT").toString());
                        }
                        break;
                }
            }
            r++;
        }
        HttpUtils.download(response, wb, templateName);
    }

    @NoLogin
    @Journal(name = "编织投料导出")
    @ResponseBody
    @RequestMapping(value = "weaveExport1", method = RequestMethod.GET)
    public void getweaveExport1(Filter filter) throws Exception {
        SXSSFWorkbook wb = feedingRecordService.getweaveExport1(filter);
        HttpUtils.download(response, wb, "编织投料");
    }

    @NoLogin
    @Journal(name = "裁剪投料导出")
    @ResponseBody
    @RequestMapping(value = "export1", method = RequestMethod.GET)
    public void getForceMaterialExport1(Filter filter) throws Exception {
        SXSSFWorkbook wb = feedingRecordService.getForceMaterialExport1(filter);
        HttpUtils.download(response, wb, "裁剪投料");
    }
}