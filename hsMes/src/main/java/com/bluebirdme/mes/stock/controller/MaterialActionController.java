/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2018版权所有
 */
package com.bluebirdme.mes.stock.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.stock.service.IMaterialActionService;
import com.bluebirdme.mes.utils.HttpUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 原料出入库、退回，库存明细界面
 *
 * @author Goofy
 * @Date 2018年3月1日 上午10:36:37
 */
@Controller
@RequestMapping("/material")
public class MaterialActionController extends BaseController {
    @Resource
    IMaterialActionService materialActionService;

    /**
     * 入库、退库页面 使用视图 hs_material_in_record_view
     */
    @Journal(name = "入库/退库页面")
    @RequestMapping("in")
    public String mIn() {
        return "stock/material/in";
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取原料入库/退库列表信息")
    @RequestMapping("in/list")
    public String getMaterialInRecord(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(materialActionService.findPageInfo(filter, page));
    }

    /**
     * 出库页面 使用视图 hs_material_out_record_view
     */
    @Journal(name = "出库页面")
    @RequestMapping("out")
    public String mOut() {
        return "stock/material/out";
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取原料出库列表信息")
    @RequestMapping("out/list")
    public String getMaterialOutRecord(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(materialActionService.findPageOutInfo(filter, page));
    }

    /**
     * 库存明细页面 使用视图 hs_material_stock_detail_view
     */
    @Journal(name = "库存页面")
    @RequestMapping("stock")
    public String mStock() {
        return "stock/material/detail";
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "总库存(明细)")
    @RequestMapping("detail/list")
    public String getMaterialDetailRecord(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(materialActionService.findPageDetailInfo(filter, page));
    }

    /**
     * 根据ID 冻结\解冻|放行\取消放行
     */
    @ResponseBody
    @Journal(name = "原料 冻结\\解冻|放行\\取消放行")
    @RequestMapping("action")
    public String action(Long mssId[], String action, String actionValue) throws Exception {
        materialActionService.action(mssId, action, actionValue);
        return ajaxSuccess();
    }

    @Journal(name = "物料判级页面")
    @RequestMapping("grade")
    public String grade() {
        return "stock/material/grade";
    }

    /**
     * 异常退回巨石页面 使用视图 hs_material_forceout_record_view
     */
    @Journal(name = "异常退回巨石页面")
    @RequestMapping("forceOut")
    public String forceOut() {
        return "stock/material/forceOut";
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取原料出库列表信息")
    @RequestMapping("forceOut/list")
    public String getForceOutRecord(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(materialActionService.findPageForceOutInfo(filter, page));
    }

    /**
     * 原料入库明细导出
     */
    @NoAuth
    @ResponseBody
    @Journal(name = "导出原料入库明细")
    @RequestMapping("export1")
    public void getMaterialInExport(Filter filter) throws Exception {
        Page page = new Page();
        page.setRows(99999);
        Map<String, Object> materialMap = materialActionService.findPageInfo(filter, page);
        List<Map<String, Object>> list = (List) materialMap.get("rows");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String templateName = "原料入库明细单";
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
        Row row;
        Cell cell;
        String[] columnName = new String[]{"原料编码", "产品大类", "规格型号", "托盘编码", "入库时间", "仓库", "库位", "重量", "入库来源", "实际偏差",
                "理论偏差", "接头方式", "操作人", "k3同步状态", "入库类型", "状态", "库存状态", "生产日期"};
        int r = 0;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司原料入库明细表");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 19; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 17));
        r++;

        row = sheet.createRow(r);
        sheet.setColumnWidth(0, 16 * 256);// 设置列宽
        sheet.setColumnWidth(1, 16 * 256);
        sheet.setColumnWidth(2, 18 * 256);
        sheet.setColumnWidth(3, 18 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 11 * 256);
        sheet.setColumnWidth(8, 11 * 256);
        sheet.setColumnWidth(9, 10 * 256);
        sheet.setColumnWidth(10, 10 * 256);
        sheet.setColumnWidth(11, 11 * 256);
        sheet.setColumnWidth(12, 11 * 256);
        sheet.setColumnWidth(13, 11 * 256);
        sheet.setColumnWidth(14, 11 * 256);
        sheet.setColumnWidth(15, 11 * 256);
        sheet.setColumnWidth(16, 11 * 256);
        sheet.setColumnWidth(17, 18 * 256);
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
                        // 原料编码
                        if (data.get("MATERIALCODE") != null) {
                            cell.setCellValue(data.get("MATERIALCODE").toString());
                        }
                        break;
                    case 1:
                        // 产品大类
                        if (data.get("PRODUCECATEGORY") != null) {
                            cell.setCellValue(data.get("PRODUCECATEGORY").toString());
                        }
                        break;
                    case 2:
                        // 规格型号
                        if (data.get("MATERIALMODEL") != null) {
                            cell.setCellValue(data.get("MATERIALMODEL").toString());
                        }
                        break;
                    case 3:
                        // 托盘编码
                        if (data.get("PALLETCODE") != null) {
                            cell.setCellValue(data.get("PALLETCODE").toString());
                        }
                        break;
                    case 4:
                        // 入库时间
                        if (data.get("INTIME") != null) {
                            c.setTimeInMillis(Long.parseLong(data.get("INTIME").toString()));
                            Date date = c.getTime();
                            cell.setCellValue(sf.format(date));
                        }
                        break;
                    case 5:
                        // 仓库
                        if (data.get("WAREHOUSECODE") != null) {
                            cell.setCellValue(data.get("WAREHOUSECODE").toString());
                        }
                        break;
                    case 6:
                        // 库位
                        if (data.get("WAREHOUSEPOSCODE") != null) {
                            cell.setCellValue(data.get("WAREHOUSEPOSCODE").toString());
                        }
                        break;
                    case 7:
                        // 重量
                        if (data.get("WEIGHT") != null) {
                            cell.setCellValue(data.get("WEIGHT").toString());
                        }
                        break;
                    case 8:
                        // 入库来源
                        if (data.get("INBANKSOURCE") != null) {
                            cell.setCellValue(data.get("INBANKSOURCE").toString());
                        }
                        break;
                    case 9:
                        // 实际偏差
                        if (data.get("REALUPPERDEVIATION") != null) {
                            cell.setCellValue(data.get("REALLOWERDEVIATION").toString() + "~"
                                    + data.get("REALUPPERDEVIATION").toString());
                        }
                        break;
                    case 10:
                        // 理论偏差
                        if (data.get("UPPERDEVIATION") != null) {
                            cell.setCellValue(
                                    data.get("LOWERDEVIATION").toString() + "~" + data.get("UPPERDEVIATION").toString());
                        }
                        break;

                    case 11:
                        // 接头方式
                        if (data.get("REALSUBWAY") != null) {
                            cell.setCellValue(data.get("REALSUBWAY").toString());
                        }
                        break;
                    case 12:
                        // 操作人
                        if (data.get("OPTUSER") != null) {
                            cell.setCellValue(data.get("OPTUSER").toString());
                        }
                        break;
                    case 13:
                        // k3同步状态
                        if (data.get("SYNCSTATE") != null) {
                            cell.setCellValue(Integer.parseInt(data.get("SYNCSTATE").toString()) == 0 ? "未同步" : "已同步");
                        }
                        break;
                    case 14:
                        // 入库类型
                        if (data.get("INBANKTYPE") != null) {
                            if (Integer.parseInt(data.get("INBANKTYPE").toString()) == 0) {
                                cell.setCellValue("入库");
                            } else {
                                cell.setCellValue("退库");
                            }

                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 15:
                        // 状态
                        if (data.get("STATE") != null) {
                            int state = Integer.parseInt(data.get("STATE").toString());
                            if (state == 0) {
                                cell.setCellValue("待检");
                            } else if (state == 1) {
                                cell.setCellValue("合格");
                            } else {
                                cell.setCellValue("不合格");
                            }

                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 16:
                        // 库存状态
                        if (data.get("STOCKSTATE") != null) {
                            int state = Integer.parseInt(data.get("STOCKSTATE").toString());
                            if (state == 0) {
                                cell.setCellValue("在库");
                            } else {
                                cell.setCellValue("不在库");
                            }

                        } else {
                            cell.setCellValue("");
                        }

                        break;
                    case 17:
                        // 生产日期
                        if (data.get("PRODUCTIONDATE") != null) {
                            c.setTimeInMillis(Long.parseLong(data.get("PRODUCTIONDATE").toString()));
                            Date date = c.getTime();
                            cell.setCellValue(sf.format(date));
                        }
                        break;
                }
            }
            r++;
        }
        HttpUtils.download(response, wb, templateName);
    }

    /**
     * 原料入库明细导出
     */
    @NoAuth
    @ResponseBody
    @Journal(name = "导出原料入库明细")
    @RequestMapping("exportIn")
    public void getMaterialInExport2(Filter filter) throws Exception {
        Page page = new Page();
        page.setRows(99999);
        Map<String, Object> materialMap = materialActionService.findPageInfo(filter, page);
        List<Map<String, Object>> list = (List) materialMap.get("rows");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String templateName = "原料入库明细单";
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
        Row row;
        Cell cell;
        String[] columnName = new String[]{"规格型号", "托盘编码", "入库时间", "库位", "重量", "出库日期", "出库人", "操作人"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        row.setHeightInPoints(80);

        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 18);
        titleFont.setBold(true);
        CellStyle cellStyleTitle = wb.createCellStyle();
        cellStyleTitle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleTitle.setBorderBottom(BorderStyle.THIN);
        cellStyleTitle.setBorderTop(BorderStyle.THIN);
        cellStyleTitle.setBorderRight(BorderStyle.THIN);
        cellStyleTitle.setBorderLeft(BorderStyle.THIN);
        cellStyleTitle.setWrapText(true);
        cellStyleTitle.setFont(titleFont);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司\r\n原料入库明细表\r\n Q/HS RHS0088-2018");
        cell.setCellStyle(cellStyleTitle);

        for (int i = 1; i < 8; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
        r++;

        row = sheet.createRow(r);
        sheet.setColumnWidth(0, 20 * 256);// 设置列宽
        sheet.setColumnWidth(1, 16 * 256);
        sheet.setColumnWidth(2, 12 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 8 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 11 * 256);

        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }
        r++;

        double totalWeight = 0d;
        for (Map<String, Object> data : list) {
            row = sheet.createRow(r);
            for (int j = 0; j < columnName.length; j++) {
                cell = row.createCell(j);
                cell.setCellStyle(cellStyle);
                switch (j) {
                    case 0:
                        // 规格型号
                        if (data.get("MATERIALMODEL") != null) {
                            cell.setCellValue(data.get("MATERIALMODEL").toString());
                        }
                        break;
                    case 1:
                        // 托盘编码
                        if (data.get("PALLETCODE") != null) {
                            cell.setCellValue(data.get("PALLETCODE").toString());
                        }
                        break;
                    case 2:
                        // 入库时间
                        if (data.get("INTIME") != null) {
                            c.setTimeInMillis(Long.parseLong(data.get("INTIME").toString()));
                            Date date = (Date) c.getTime();
                            cell.setCellValue(sf.format(date).toString());
                        }
                        break;
                    case 3:
                        // 库位
                        if (data.get("WAREHOUSEPOSCODE") != null) {
                            cell.setCellValue(data.get("WAREHOUSEPOSCODE").toString());
                        }
                        break;
                    case 4:
                        // 重量
                        if (data.get("WEIGHT") != null) {
                            totalWeight += new BigDecimal(data.get("WEIGHT").toString()).doubleValue();
                            cell.setCellValue(data.get("WEIGHT").toString());
                        }
                        break;
                    case 5:
                        // 出库日期
                        cell.setCellValue("");
                        break;
                    case 6:
                        // 出库人
                        cell.setCellValue("");
                        break;
                    case 7:
                        // 操作人
                        if (data.get("OPTUSER") != null) {
                            cell.setCellValue(data.get("OPTUSER").toString());
                        }
                        break;
                }
            }
            r++;
        }
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(list.size());
        cell = row.createCell(1);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("");
        cell = row.createCell(2);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("");
        cell = row.createCell(3);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("");
        cell = row.createCell(4);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(totalWeight);
        cell = row.createCell(5);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("");
        cell = row.createCell(6);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("");
        cell = row.createCell(7);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("");
        HttpUtils.download(response, wb, templateName);
    }

    /**
     * 原料出库明细导出
     */
    @NoAuth
    @ResponseBody
    @Journal(name = "导出原料 出库明细")
    @RequestMapping("outExport")
    public void getMaterialOutExport(Filter filter) throws Exception {
        Page page = new Page();
        page.setRows(99999);
        Map<String, Object> materialMap = materialActionService.findPageOutInfo(filter, page);
        List<Map<String, Object>> list = (List) materialMap.get("rows");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        String templateName = "原料出库明细单";
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
        Row row;
        Cell cell;
        String[] columnName = new String[]{"编码", "产品大类", "规格型号", "托盘编码", "状态", "是否放行", "是否冻结", "库存状态", "生产日期", "有效期",
                "重量", "出库单号", "出库时间", "操作人", "仓库", "库位", "领料车间", "最低库存", "最大库存", "理论上偏差", "理论下偏差", "实际上下偏差", "接头方式", "保质期", "制成率",
                "k3同步状态", "备注"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司原料出库明细表");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 27; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 25));
        r++;

        row = sheet.createRow(r);
        sheet.setColumnWidth(0, 16 * 256);// 设置列宽
        sheet.setColumnWidth(1, 16 * 256);
        sheet.setColumnWidth(2, 18 * 256);
        sheet.setColumnWidth(3, 18 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 11 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(9, 20 * 256);
        sheet.setColumnWidth(10, 10 * 256);
        sheet.setColumnWidth(11, 20 * 256);
        sheet.setColumnWidth(12, 20 * 256);
        sheet.setColumnWidth(13, 11 * 256);
        sheet.setColumnWidth(14, 11 * 256);
        sheet.setColumnWidth(15, 11 * 256);
        sheet.setColumnWidth(16, 11 * 256);
        sheet.setColumnWidth(17, 10 * 256);
        sheet.setColumnWidth(18, 10 * 256);
        sheet.setColumnWidth(19, 10 * 256);
        sheet.setColumnWidth(20, 10 * 256);
        sheet.setColumnWidth(21, 10 * 256);
        sheet.setColumnWidth(22, 10 * 256);
        sheet.setColumnWidth(23, 18 * 256);
        sheet.setColumnWidth(24, 10 * 256);
        sheet.setColumnWidth(25, 10 * 256);
        sheet.setColumnWidth(26, 10 * 256);
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
                        // 原料编码
                        if (data.get("MATERIALCODE") != null) {
                            cell.setCellValue(data.get("MATERIALCODE").toString());
                        }
                        break;
                    case 1:
                        // 产品大类
                        if (data.get("PRODUCECATEGORY") != null) {
                            cell.setCellValue(data.get("PRODUCECATEGORY").toString());
                        }
                        break;
                    case 2:
                        // 规格型号
                        if (data.get("MATERIALMODEL") != null) {
                            cell.setCellValue(data.get("MATERIALMODEL").toString());
                        }
                        break;
                    case 3:
                        // 托盘编码
                        if (data.get("PALLETCODE") != null) {
                            cell.setCellValue(data.get("PALLETCODE").toString());
                        }
                        break;
                    case 4:
                        // 状态
                        if (data.get("STATE") != null) {
                            int state = Integer.valueOf(data.get("STATE").toString());
                            if (state == 0) {
                                cell.setCellValue("待检");
                            } else if (state == 1) {
                                cell.setCellValue("合格");
                            } else {
                                cell.setCellValue("不合格");
                            }
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 5:
                        // 是否放行
                        if (data.get("ISPASS") != null) {
                            cell.setCellValue(Integer.parseInt(data.get("ISPASS").toString()) == 0 ? "正常" : "放行");
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 6:
                        // 是否冻结
                        if (data.get("ISLOCKED") != null) {
                            cell.setCellValue(Integer.parseInt(data.get("ISLOCKED").toString()) == 0 ? "正常" : "冻结");
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 7:
                        // 库存状态
                        if (data.get("STOCKSTATE") != null) {
                            int state = Integer.valueOf(data.get("STOCKSTATE").toString());
                            if (state == 0) {
                                cell.setCellValue("在库");
                            } else {
                                cell.setCellValue("不在库");
                            }
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 8:
                        // 生产日期
                        if (data.get("PRODUCTIONDATE") != null) {
                            c.setTimeInMillis(Long.parseLong(data.get("PRODUCTIONDATE").toString()));
                            Date date = c.getTime();
                            cell.setCellValue(sf.format(date));
                        }
                        break;
                    case 9:
                        // 有效期
                        if (data.get("EXPIREDATE") != null) {
                            if (Long.parseLong(data.get("EXPIREDATE").toString()) != 0) {
                                c.setTimeInMillis(Long.parseLong(data.get("EXPIREDATE").toString()));
                                Date date = c.getTime();
                                cell.setCellValue(sf.format(date));
                            } else {
                                cell.setCellValue("");
                            }
                        }
                        break;
                    case 10:
                        // 重量
                        if (data.get("WEIGHT") != null) {
                            cell.setCellValue(data.get("WEIGHT").toString());
                        }
                        break;
                    case 11:
                        // 出库单号
                        if (data.get("OUTORDERCODE") != null) {
                            cell.setCellValue(data.get("OUTORDERCODE").toString());
                        }
                        break;
                    case 12:
                        // 出库时间
                        if (data.get("OUTTIME") != null) {
                            c.setTimeInMillis(Long.parseLong(data.get("OUTTIME").toString()));
                            Date date = c.getTime();
                            cell.setCellValue(sf.format(date));
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 13:
                        // 操作人
                        if (data.get("OUTOPTUSER") != null) {
                            cell.setCellValue(data.get("OUTOPTUSER").toString());
                        }
                        break;
                    case 14:
                        // 仓库
                        if (data.get("WAREHOUSECODE") != null) {
                            cell.setCellValue(data.get("WAREHOUSECODE").toString());
                        }
                        break;

                    case 15:
                        // 库位
                        if (data.get("WAREHOUSEPOSCODE") != null) {
                            cell.setCellValue(data.get("WAREHOUSEPOSCODE").toString());
                        }
                        break;
                    case 16:
                        if (data.get("WORKSHOPNAME") != null) {
                            cell.setCellValue(data.get("WORKSHOPNAME").toString());
                        }
                        break;
                    case 17:
                        // 最低库存
                        if (data.get("MATERIALMINSTOCK") != null) {
                            cell.setCellValue(data.get("MATERIALMINSTOCK").toString());
                        }
                        break;
                    case 18:
                        // 最大库存
                        if (data.get("MATERIALMAXSTOCK") != null) {
                            cell.setCellValue(data.get("MATERIALMAXSTOCK").toString());
                        }
                        break;
                    case 19:
                        // 理论上偏差
                        if (data.get("UPPERDEVIATION") != null) {
                            cell.setCellValue(data.get("UPPERDEVIATION").toString());
                        }
                        break;
                    case 20:
                        // 理论下偏差
                        if (data.get("LOWERDEVIATION") != null) {
                            cell.setCellValue(data.get("LOWERDEVIATION").toString());
                        }
                        break;
                    case 21:
                        // 实际上下偏差
                        if (data.get("REALUPPERDEVIATION") != null) {
                            cell.setCellValue(data.get("REALLOWERDEVIATION").toString() + "~"
                                    + data.get("REALUPPERDEVIATION").toString());
                        }
                        break;
                    case 22:
                        // 接头方式
                        if (data.get("SUBWAY") != null) {
                            cell.setCellValue(data.get("SUBWAY").toString());
                        }
                        break;
                    case 23:
                        // 保质期
                        if (data.get("MATERIALSHELFLIFE") != null) {
                            if (Long.parseLong(data.get("MATERIALSHELFLIFE").toString()) != 0) {
                                c.setTimeInMillis(Long.parseLong(data.get("MATERIALSHELFLIFE").toString()));
                                Date date = c.getTime();
                                cell.setCellValue(sf.format(date));
                            } else {
                                cell.setCellValue("0");
                            }
                        }
                        break;
                    case 24:
                        // 制成率
                        if (data.get("MADERATE") != null) {
                            cell.setCellValue(Double.parseDouble(data.get("MADERATE").toString()));
                        }
                        break;

                    case 25:
                        // k3同步状态
                        if (data.get("SYNCSTATE") != null) {
                            cell.setCellValue(Integer.parseInt(data.get("SYNCSTATE").toString()) == 0 ? "未同步" : "已同步");
                        }
                        break;
                    case 26:
                        // 备注
                        if (data.get("MATERIALMEMO") != null) {
                            cell.setCellValue(data.get("MATERIALMEMO").toString());
                        }
                        break;
                }
            }
            r++;
        }
        HttpUtils.download(response, wb, templateName);
    }

    /**
     * 原料出库明细导出
     *
     * @param filter
     * @throws Exception
     */
    @NoAuth
    @ResponseBody
    @Journal(name = "导出原料 出库明细")
    @RequestMapping("outExport2")
    public void getMaterialOutExport2(Filter filter) throws Exception {
        Page page = new Page();
        page.setRows(99999);
        Map<String, Object> materialMap = materialActionService.findPageOutInfo(filter, page);
        List<Map<String, Object>> list = (List) materialMap.get("rows");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        String templateName = "原料出库明细单";
        Workbook wb = new SXSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row row;
        Cell cell;
        String[] columnName = new String[]{"规格型号", "托盘编码", "重量", "出库时间", "操作人", "库位", "领料车间"};
        // 从第1行开始写数据
        int r = 0;
        row = sheet.createRow(r);
        row.setHeightInPoints(80);

        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 18);
        titleFont.setBold(true);
        CellStyle cellStyleTitle = wb.createCellStyle();
        cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
        cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleTitle.setBorderBottom(BorderStyle.THIN);
        cellStyleTitle.setBorderTop(BorderStyle.THIN);
        cellStyleTitle.setBorderRight(BorderStyle.THIN);
        cellStyleTitle.setBorderLeft(BorderStyle.THIN);
        cellStyleTitle.setWrapText(true);
        cellStyleTitle.setFont(titleFont);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司\r\n原料出库明细表\r\n Q/HS RJD0081-2018\r\n");
        cell.setCellStyle(cellStyleTitle);
        for (int i = 1; i < 7; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyleTitle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
        r++;

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        row = sheet.createRow(r);
        sheet.setColumnWidth(0, 20 * 256);// 设置列宽
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 25 * 256);
        sheet.setColumnWidth(4, 12 * 256);
        sheet.setColumnWidth(5, 12 * 256);
        sheet.setColumnWidth(6, 12 * 256);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }
        r++;

        Map<String, List<Map<String, Object>>> resultMap = new HashMap<>();
        for (Map<String, Object> data : list) {
            String materialmodel = data.get("MATERIALMODEL").toString();
            if (resultMap.containsKey(materialmodel)) {
                resultMap.get(materialmodel).add(data);
            } else {
                List<Map<String, Object>> subList = new ArrayList<>();
                subList.add(data);
                resultMap.put(materialmodel, subList);
            }
        }

        CellStyle cellStyleSum = wb.createCellStyle();
        cellStyleSum.setAlignment(HorizontalAlignment.RIGHT);
        cellStyleSum.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleSum.setBorderBottom(BorderStyle.THIN);
        cellStyleSum.setBorderTop(BorderStyle.THIN);
        cellStyleSum.setBorderRight(BorderStyle.THIN);
        cellStyleSum.setBorderLeft(BorderStyle.THIN);
        cellStyleSum.setWrapText(true);

        for (Map.Entry<String, List<Map<String, Object>>> m : resultMap.entrySet()) {
            double totalWeight = 0d;
            for (Map<String, Object> data : m.getValue()) {
                row = sheet.createRow(r);
                for (int j = 0; j < columnName.length; j++) {
                    cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    switch (j) {
                        case 0:
                            // 规格型号
                            if (data.get("MATERIALMODEL") != null) {
                                cell.setCellValue(data.get("MATERIALMODEL").toString());
                            }
                            break;
                        case 1:
                            // 托盘编码
                            if (data.get("PALLETCODE") != null) {
                                cell.setCellValue(data.get("PALLETCODE").toString());
                            }
                            break;
                        case 2:
                            // 重量
                            if (data.get("WEIGHT") != null) {
                                totalWeight += new BigDecimal(data.get("WEIGHT").toString()).doubleValue();
                                cell.setCellValue(data.get("WEIGHT").toString());
                            }
                            break;
                        case 3:
                            // 出库时间
                            if (data.get("OUTTIME") != null) {
                                c.setTimeInMillis(Long.parseLong(data.get("OUTTIME").toString()));
                                Date date = (Date) c.getTime();
                                cell.setCellValue(sf.format(date).toString());
                            } else {
                                cell.setCellValue("");
                            }
                            break;
                        case 4:
                            // 操作人
                            if (data.get("OUTOPTUSER") != null) {
                                cell.setCellValue(data.get("OUTOPTUSER").toString());
                            }
                            break;
                        case 5:
                            // 库位
                            if (data.get("WAREHOUSEPOSCODE") != null) {
                                cell.setCellValue(data.get("WAREHOUSEPOSCODE").toString());
                            }
                            break;

                        case 6:
                            // 领料车间
                            if (data.get("WORKSHOPNAME") != null) {
                                cell.setCellValue(data.get("WORKSHOPNAME").toString());
                            }
                            break;
                    }
                }
                r++;
            }

            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue(m.getValue().size());
            cell.setCellStyle(cellStyleSum);
            cell = row.createCell(1);
            cell.setCellValue("");
            cell.setCellStyle(cellStyleSum);
            cell = row.createCell(2);
            cell.setCellValue(totalWeight);
            cell.setCellStyle(cellStyleSum);
            cell = row.createCell(3);
            cell.setCellValue("");
            cell.setCellStyle(cellStyleSum);
            cell = row.createCell(4);
            cell.setCellValue("");
            cell.setCellStyle(cellStyleSum);
            cell = row.createCell(5);
            cell.setCellValue("");
            cell.setCellStyle(cellStyleSum);
            cell = row.createCell(6);
            cell.setCellValue("");
            cell.setCellStyle(cellStyleSum);
            r++;
        }
        HttpUtils.download(response, wb, templateName);
    }

    /**
     * 原料库存导出
     */
    @NoAuth
    @ResponseBody
    @Journal(name = "导出原料库存记录")
    @RequestMapping("detailExport")
    public void getMaterialDetailExport(Filter filter) throws Exception {
        Page page = new Page();
        page.setRows(99999);
        Map<String, Object> materialMap = materialActionService.findPageDetailInfo(filter, page);
        List<Map<String, Object>> list = (List) materialMap.get("rows");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String templateName = "原料库存记录";
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
        Row row;
        Cell cell;
        String[] columnName = new String[]{"原料编码", "产品大类", "规格型号", "托盘编码", "入库时间", "仓库", "库位", "质量等级", "是否放行", "是否冻结",
                "库存状态", "生产日期", "有效期", "重量(KG)", "实际偏差", "理论偏差", "接头方式", "保质期"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司原料库存表");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 19; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 17));
        r++;

        row = sheet.createRow(r);
        sheet.setColumnWidth(0, 16 * 256);// 设置列宽
        sheet.setColumnWidth(1, 16 * 256);
        sheet.setColumnWidth(2, 18 * 256);
        sheet.setColumnWidth(3, 18 * 256);
        sheet.setColumnWidth(4, 12 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        sheet.setColumnWidth(9, 10 * 256);
        sheet.setColumnWidth(10, 10 * 256);
        sheet.setColumnWidth(11, 12 * 256);
        sheet.setColumnWidth(12, 12 * 256);
        sheet.setColumnWidth(13, 11 * 256);
        sheet.setColumnWidth(14, 11 * 256);
        sheet.setColumnWidth(15, 11 * 256);
        sheet.setColumnWidth(16, 11 * 256);
        sheet.setColumnWidth(17, 10 * 256);
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
                        // 原料编码
                        if (data.get("MATERIALCODE") != null) {
                            cell.setCellValue(data.get("MATERIALCODE").toString());
                        }
                        break;
                    case 1:
                        // 产品大类
                        if (data.get("PRODUCECATEGORY") != null) {
                            cell.setCellValue(data.get("PRODUCECATEGORY").toString());
                        }
                        break;
                    case 2:
                        // 规格型号
                        if (data.get("MATERIALMODEL") != null) {
                            cell.setCellValue(data.get("MATERIALMODEL").toString());
                        }
                        break;
                    case 3:
                        // 托盘编码
                        if (data.get("PALLETCODE") != null) {
                            cell.setCellValue(data.get("PALLETCODE").toString());
                        }
                        break;
                    case 4:
                        // 入库时间
                        if (data.get("INTIME") != null) {
                            c.setTimeInMillis(Long.parseLong(data.get("INTIME").toString()));
                            Date date = (Date) c.getTime();
                            cell.setCellValue(sf.format(date).toString());
                        }
                        break;
                    case 5:
                        // 仓库
                        if (data.get("WAREHOUSECODE") != null) {
                            cell.setCellValue(data.get("WAREHOUSECODE").toString());
                        }
                        break;

                    case 6:
                        // 库位
                        if (data.get("WAREHOUSEPOSCODE") != null) {
                            cell.setCellValue(data.get("WAREHOUSEPOSCODE").toString());
                        }
                        break;
                    case 7:
                        // 状态
                        if (data.get("STATE") != null) {
                            int state = Integer.valueOf(data.get("STATE").toString());
                            if (state == 0) {
                                cell.setCellValue("待检");
                            } else if (state == 1) {
                                cell.setCellValue("合格");
                            } else {
                                cell.setCellValue("不合格");
                            }

                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 8:
                        // 是否放行
                        if (data.get("ISPASS") != null) {

                            cell.setCellValue(Integer.parseInt(data.get("ISPASS").toString()) == 0 ? "正常" : "放行");
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 9:
                        // 是否冻结
                        if (data.get("ISLOCKED") != null) {

                            cell.setCellValue(Integer.parseInt(data.get("ISLOCKED").toString()) == 0 ? "正常" : "冻结");
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 10:
                        // 库存状态
                        if (data.get("STOCKSTATE") != null) {
                            int state = Integer.parseInt(data.get("STOCKSTATE").toString());
                            if (state == 0) {
                                cell.setCellValue("在库");
                            } else {
                                cell.setCellValue("不在库");
                            }
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 11:
                        // 生产日期
                        if (data.get("PRODUCTIONDATE") != null) {
                            c.setTimeInMillis(Long.parseLong(data.get("PRODUCTIONDATE").toString()));
                            Date date = c.getTime();
                            cell.setCellValue(sf.format(date));
                        }
                        break;
                    case 12:
                        // 有效期
                        if (data.get("EXPIREDATE") != null) {
                            if (Long.parseLong(data.get("EXPIREDATE").toString()) != 0) {
                                c.setTimeInMillis(Long.parseLong(data.get("EXPIREDATE").toString()));
                                Date date = c.getTime();
                                cell.setCellValue(sf.format(date));
                            } else {
                                cell.setCellValue("");
                            }
                        }
                        break;
                    case 13:
                        // 重量
                        if (data.get("WEIGHT") != null) {
                            cell.setCellValue(data.get("WEIGHT").toString());
                        }
                        break;
                    case 14:
                        // 实际偏差
                        if (data.get("REALUPPERDEVIATION") != null) {
                            cell.setCellValue(data.get("REALLOWERDEVIATION").toString() + "~"
                                    + data.get("REALUPPERDEVIATION").toString());
                        }
                        break;
                    case 15:
                        // 理论偏差
                        if (data.get("UPPERDEVIATION") != null) {
                            cell.setCellValue(
                                    data.get("LOWERDEVIATION").toString() + "~" + data.get("UPPERDEVIATION").toString());
                        }
                        break;
                    case 16:
                        // 接头方式
                        if (data.get("REALSUBWAY") != null) {
                            cell.setCellValue(data.get("REALSUBWAY").toString());
                        }
                        break;
                    case 17:
                        // 保质期
                        if (data.get("MATERIALSHELFLIFE") != null) {
                            if (Long.parseLong(data.get("MATERIALSHELFLIFE").toString()) != 0) {
                                c.setTimeInMillis(Long.parseLong(data.get("MATERIALSHELFLIFE").toString()));
                                Date date = (Date) c.getTime();
                                cell.setCellValue(sf.format(date).toString());
                            } else {
                                cell.setCellValue("0");
                            }
                        }
                        break;
                }
            }
            r++;
        }
        HttpUtils.download(response, wb, templateName);
    }

    /**
     * 原料出库明细导出
     */
    @NoAuth
    @ResponseBody
    @Journal(name = "导出异常原料退回")
    @RequestMapping("forceExport")
    public void getForceMaterialExport(Filter filter) throws Exception {
        DecimalFormat df = new DecimalFormat("######0");
        Page page = new Page();
        page.setRows(99999);
        Map<String, Object> materialMap = materialActionService.findPageForceOutInfo(filter, page);
        List<Map<String, Object>> list = (List) materialMap.get("rows");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        String templateName = "异常原料退回";
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
        Row row;
        Cell cell;
        String[] columnName = new String[]{"原料编码", "产品大类", "规格型号", "出库时间", "仓库编码", "库位", "出库地址", "操作人", "托盘编码", "状态",
                "是否放行", "库存状态", "上偏差", "下偏差", "接头方式", "保质期", "制成率(%)", "生产日期", "有效期", "k3同步状态", "备注"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司异常原料退回表");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 21; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 20));
        r++;

        row = sheet.createRow(r);
        sheet.setColumnWidth(0, 16 * 256);// 设置列宽
        sheet.setColumnWidth(1, 16 * 256);
        sheet.setColumnWidth(2, 18 * 256);
        sheet.setColumnWidth(3, 22 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 11 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(9, 10 * 256);
        sheet.setColumnWidth(10, 10 * 256);
        sheet.setColumnWidth(11, 10 * 256);
        sheet.setColumnWidth(12, 8 * 256);
        sheet.setColumnWidth(13, 8 * 256);
        sheet.setColumnWidth(14, 11 * 256);
        sheet.setColumnWidth(15, 11 * 256);
        sheet.setColumnWidth(16, 11 * 256);
        sheet.setColumnWidth(17, 22 * 256);
        sheet.setColumnWidth(18, 10 * 256);
        sheet.setColumnWidth(19, 12 * 256);
        sheet.setColumnWidth(20, 10 * 256);
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
                        // 原料编码
                        if (data.get("MATERIALCODE") != null) {
                            cell.setCellValue(data.get("MATERIALCODE").toString());
                        }
                        break;
                    case 1:
                        // 产品大类
                        if (data.get("PRODUCECATEGORY") != null) {
                            cell.setCellValue(data.get("PRODUCECATEGORY").toString());
                        }
                        break;
                    case 2:
                        // 规格型号
                        if (data.get("MATERIALMODEL") != null) {
                            cell.setCellValue(data.get("MATERIALMODEL").toString());
                        }
                        break;
                    case 3:
                        // 出库日期
                        if (data.get("OUTTIME") != null) {
                            c.setTimeInMillis(Long.parseLong(data.get("OUTTIME").toString()));
                            Date date = c.getTime();
                            cell.setCellValue(sf.format(date));
                        }
                        break;
                    case 4:
                        // 仓库
                        if (data.get("WAREHOUSECODE") != null) {
                            cell.setCellValue(data.get("WAREHOUSECODE").toString());
                        }
                        break;
                    case 5:
                        // 库位
                        if (data.get("WAREHOUSEPOSCODE") != null) {
                            cell.setCellValue(data.get("WAREHOUSEPOSCODE").toString());
                        }
                        break;
                    case 6:
                        // 出库地址
                        if (data.get("OUTADDRESS") != null) {
                            cell.setCellValue(data.get("OUTADDRESS").toString());
                        }
                        break;
                    case 7:
                        // 操作人
                        if (data.get("OUTUSER") != null) {
                            cell.setCellValue(data.get("OUTUSER").toString());
                        }
                        break;
                    case 8:
                        // 托盘编码
                        if (data.get("PALLETCODE") != null) {
                            cell.setCellValue(data.get("PALLETCODE").toString());
                        }
                        break;
                    case 9:
                        // 状态
                        if (data.get("STATE") != null) {
                            int state = Integer.valueOf(data.get("STATE").toString());
                            if (state == 0) {
                                cell.setCellValue("待检");
                            } else if (state == 1) {
                                cell.setCellValue("合格");
                            } else {
                                cell.setCellValue("不合格");
                            }

                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 10:
                        // 是否放行
                        if (data.get("ISPASS") != null) {
                            cell.setCellValue(Integer.parseInt(data.get("ISPASS").toString()) == 0 ? "正常" : "放行");
                        } else {
                            cell.setCellValue("");
                        }
                        break;

                    case 11:
                        // 库存状态
                        if (data.get("STOCKSTATE") != null) {
                            int state = Integer.parseInt(data.get("STOCKSTATE").toString());
                            if (state == 0) {
                                cell.setCellValue("在库");
                            } else {
                                cell.setCellValue("不在库");
                            }

                        } else {
                            cell.setCellValue("");
                        }

                        break;
                    case 12:
                        // 上偏差
                        if (data.get("REALUPPERDEVIATION") != null) {
                            cell.setCellValue(data.get("REALUPPERDEVIATION").toString());
                        }
                        break;
                    case 13:
                        // 下偏差
                        if (data.get("REALLOWERDEVIATION") != null) {
                            cell.setCellValue(data.get("REALLOWERDEVIATION").toString());
                        }
                        break;
                    case 14:
                        // 接头方式
                        if (data.get("REALSUBWAY") != null) {
                            cell.setCellValue(data.get("REALSUBWAY").toString());
                        }
                        break;
                    case 15:
                        // 保质期
                        if (data.get("MATERIALSHELFLIFE") != null) {
                            if (Long.parseLong(data.get("MATERIALSHELFLIFE").toString()) != 0) {
                                c.setTimeInMillis(Long.parseLong(data.get("MATERIALSHELFLIFE").toString()));
                                Date date = (Date) c.getTime();
                                cell.setCellValue(sf.format(date).toString());
                            } else {
                                cell.setCellValue("0");
                            }
                        }
                        break;
                    case 16:
                        // 制成率
                        if (data.get("MADERATE") != null) {
                            cell.setCellValue(df.format(data.get("MADERATE")));
                        }
                        break;
                    case 17:
                        // 生产日期
                        if (data.get("PRODUCTIONDATE") != null) {
                            c.setTimeInMillis(Long.parseLong(data.get("PRODUCTIONDATE").toString()));
                            Date date = c.getTime();
                            cell.setCellValue(sf.format(date));
                        }
                        break;
                    case 18:
                        // 有效期
                        if (data.get("EXPIREDATE") != null) {
                            if (Long.parseLong(data.get("EXPIREDATE").toString()) != 0) {
                                c.setTimeInMillis(Long.parseLong(data.get("EXPIREDATE").toString()));
                                Date date = c.getTime();
                                cell.setCellValue(sf.format(date));
                            } else {
                                cell.setCellValue("");
                            }
                        }
                        break;
                    case 19:
                        // k3同步状态
                        if (data.get("SYNCSTATE") != null) {
                            int state = Integer.parseInt(data.get("SYNCSTATE").toString());
                            if (state == 0) {
                                cell.setCellValue("未同步");
                            } else {
                                cell.setCellValue("已同步");
                            }

                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 20:
                        // 备注
                        if (data.get("MATERIALMEMO") != null) {
                            cell.setCellValue(data.get("MATERIALMEMO").toString());
                        }
                        break;
                }
            }
            r++;
        }
        HttpUtils.download(response, wb, templateName);
    }
}
