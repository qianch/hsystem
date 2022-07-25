/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.deliveryontheway.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.planner.deliveryontheway.entity.DeliveryOnTheWayPlan;
import com.bluebirdme.mes.planner.deliveryontheway.service.IDeliveryOnTheWayPlanService;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.utils.HttpUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 出货计划Controller
 *
 * @author 徐波
 * @Date 2016-11-2 9:30:06
 */
@Controller
@RequestMapping("/planner/deliveryOnTheWayPlan")
@Journal(name = "出货调拨单计划")
public class DeliveryOnTheWayPlanController extends BaseController {
    /**
     * 出货调拨单页面
     */
    final String index = "stock/productStock/productDeliveryOnTheWayRecord";
    final String pMoveUrl = "stock/productStock/productMove";
    final String selectorOnTheWayBarCodeUrl = "selector/selectorOnTheWayBarCode";

    @Resource
    IDeliveryOnTheWayPlanService deliveryOnTheWayPlanService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取成品出货调拨单列表信息")
    @RequestMapping("list")
    public String getProductDeliveryRecord(Filter filter, Page page) throws Exception {
        Map<String, Object> pageInfo = deliveryOnTheWayPlanService.findPageInfo(filter, page);
        Page rollWeightPage = new Page();
        page.setAll(1);
        Map<String, Object> pageInfoTotalWeight = deliveryOnTheWayPlanService.findPageInfoTotalWeight(filter, rollWeightPage);
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> totalWeightrows = (List<Map<String, Object>>) pageInfoTotalWeight.get("rows");
        List<Map<String, Object>> allRows = (List<Map<String, Object>>) pageInfo.get("rows");
        BigDecimal allTotalWeight = new BigDecimal(0);
        int allTotalCount = 0;

        BigDecimal totalWeight = new BigDecimal(0);
        int totalCount = 0;
        if (totalWeightrows != null && totalWeightrows.size() > 0) {
            allTotalWeight = BigDecimal.valueOf(Double.parseDouble(totalWeightrows.get(0).get("TOTALWEIGHT").toString().replace(",", "")));
            allTotalCount += Integer.parseInt(totalWeightrows.get(0).get("TOTALCOUNT").toString());
        }

        if (allRows != null) {
            for (Map<String, Object> oneRecord : allRows) {
                totalWeight = totalWeight.add(BigDecimal.valueOf((Double) oneRecord.get("TOTALWEIGHT")));
                totalCount += Integer.parseInt(oneRecord.get("TOTALCOUNT").toString().replace(",", ""));
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("DELIVERYCODE", "当前页总计：");
        map.put("TOTALCOUNT", totalCount);
        map.put("TOTALWEIGHT", totalWeight.setScale(2, BigDecimal.ROUND_HALF_UP) + "kg");
        list.add(map);
        map = new HashMap<>();
        map.put("DELIVERYCODE", "当搜索条件总计：");
        map.put("TOTALCOUNT", allTotalCount);
        map.put("TOTALWEIGHT", allTotalWeight.setScale(2, BigDecimal.ROUND_HALF_UP) + "kg");
        list.add(map);
        pageInfo.put("footer", list);
        return GsonTools.toJson(pageInfo);
    }


    @ResponseBody
    @Journal(name = "根据出货调拨单编码查询信息", logType = LogType.DB)
    @RequestMapping(value = "findDeliveryOnTheWayPlanDetails", method = RequestMethod.POST)
    public String findDeliveryOnTheWayPlanDetails(Long deliveryId) {
        return GsonTools.toJson(deliveryOnTheWayPlanService.findDeliveryOnTheWayPlanDetails(deliveryId));
    }

    @NoLogin
    @Journal(name = "根据出货单id导出成品出库单汇总")
    @ResponseBody
    @RequestMapping(value = "exportDetail")
    public void exportDetail(Filter filter) throws Exception {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        CellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle1.setBorderBottom(BorderStyle.NONE);
        cellStyle1.setBorderTop(BorderStyle.NONE);
        cellStyle1.setBorderRight(BorderStyle.NONE);
        cellStyle1.setBorderLeft(BorderStyle.NONE);
        cellStyle1.setWrapText(true);
        CellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle2.setBorderBottom(BorderStyle.NONE);
        cellStyle2.setBorderTop(BorderStyle.NONE);
        cellStyle2.setBorderRight(BorderStyle.NONE);
        cellStyle2.setBorderLeft(BorderStyle.NONE);
        cellStyle2.setWrapText(true);
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 14);// 设置字体大小
        cellStyle2.setFont(font);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");
        int trayNum = 0;
        double totalWeight = 0.0;

        Page page = new Page();
        page.setAll(1);
        Map<String, Object> detailplanPageInfo = deliveryOnTheWayPlanService.findDeliveryOnTheWayPlanDetails(filter, page);
        List<Map<String, Object>> allRows = (List<Map<String, Object>>) detailplanPageInfo.get("rows");

        Sheet sheet = wb.createSheet("出库调拨单明细");
        Row row;
        Cell cell;
        String[] columnName = new String[]{"序号", "出库调拨单", "仓库名称", "在途时间", "客户订单号", "批号", "厂内名称", "客户名称", "部件名称", "条码编号", "重量(kg)"};
        int i = 0;//第零行
        row = sheet.createRow(i);
        // 标题
        for (int a = 0; a < columnName.length; a++) {
            sheet.setColumnWidth(0, 6 * 256);// 设置列宽
            sheet.setColumnWidth(1, 15 * 256);
            sheet.setColumnWidth(2, 15 * 256);
            sheet.setColumnWidth(3, 15 * 256);
            sheet.setColumnWidth(4, 12 * 256);
            sheet.setColumnWidth(5, 12 * 256);
            sheet.setColumnWidth(6, 15 * 256);
            sheet.setColumnWidth(7, 15 * 256);
            sheet.setColumnWidth(8, 15 * 256);
            sheet.setColumnWidth(9, 15 * 256);
            sheet.setColumnWidth(10, 15 * 256);
            cell = row.createCell(a);
            cell.setCellStyle(cellStyle2);
            if (a == 0) {
                cell.setCellValue("浙江恒石纤维基业有限公司出库调拨单明细汇总");
                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 6));
            }
            if (a == columnName.length - 4) {
                cell.setCellValue("Q/HS RHS0081-2015");
                cell.setCellStyle(cellStyle1);
                sheet.addMergedRegion(new CellRangeAddress(i, i, 7, 10));
            }
        }
        //第一行
        i++;
        // 日期和文件编号
        row = sheet.createRow(i);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(columnName[a]);
        }

        int index = 0;
        for (Map<String, Object> detail : allRows) {
            index++;
            i++;
            row = sheet.createRow(i);
            trayNum++;

            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                cell.setCellStyle(cellStyle);
                switch (columnName[a]) {
                    case "序号" -> cell.setCellValue(index);
                    case "出库调拨单" -> cell.setCellValue(detail.get("deliveryCode".toUpperCase()).toString());
                    case "仓库名称" ->
                            cell.setCellValue(detail.get("WAREHOUSENAME".toUpperCase()) == null ? "" : detail.get("WAREHOUSENAME".toUpperCase()).toString());
                    case "在途时间" -> cell.setCellValue(detail.getOrDefault("deliveryDate".toUpperCase(), "").toString());
                    case "客户订单号" -> cell.setCellValue(detail.get("salesOrderSubCode".toUpperCase()).toString());
                    case "批号" -> cell.setCellValue(detail.get("batchcode".toUpperCase()).toString());
                    case "厂内名称" -> cell.setCellValue(detail.get("factoryProductName".toUpperCase()).toString());
                    case "客户名称" -> cell.setCellValue(detail.get("consumerProductName".toUpperCase()).toString());
                    case "部件名称" -> cell.setCellValue(detail.get("partname".toUpperCase()).toString());
                    case "条码编号" -> cell.setCellValue(detail.get("barcode".toUpperCase()).toString());
                    case "重量(kg)" -> {
                        totalWeight += (detail.get("weight".toUpperCase()) == null ? 0.0 : (Double) detail.get("weight".toUpperCase()));
                        cell.setCellValue(detail.get("weight".toUpperCase()).toString());
                    }
                }
            }
        }

        i++;
        row = sheet.createRow(i);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellStyle(cellStyle);
            if (a == 0) {
                cell.setCellValue("总计");
            } else if (a == 7) {//托
                cell.setCellValue("数量：");
            } else if (a == 8) {//托
                cell.setCellValue(trayNum);
            } else if (a == 9) {
                cell.setCellValue("重量：");
            } else if (a == 10) {//重量
                cell.setCellValue(totalWeight);
            }
        }
        HttpUtils.download(response, wb, sdf.format(date));
    }

    @NoLogin
    @Journal(name = "根据出货单id导出成品出库单汇总")
    @ResponseBody
    @RequestMapping(value = "export3", method = RequestMethod.GET)
    public void export3(long deliveryId) throws Exception {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        CellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle1.setBorderBottom(BorderStyle.NONE);
        cellStyle1.setBorderTop(BorderStyle.NONE);
        cellStyle1.setBorderRight(BorderStyle.NONE);
        cellStyle1.setBorderLeft(BorderStyle.NONE);
        cellStyle1.setWrapText(true);
        CellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle2.setBorderBottom(BorderStyle.NONE);
        cellStyle2.setBorderTop(BorderStyle.NONE);
        cellStyle2.setBorderRight(BorderStyle.NONE);
        cellStyle2.setBorderLeft(BorderStyle.NONE);
        cellStyle2.setWrapText(true);
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 14);// 设置字体大小
        cellStyle2.setFont(font);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        DeliveryOnTheWayPlan poo = deliveryOnTheWayPlanService.findById(DeliveryOnTheWayPlan.class, deliveryId);
        int trayNum = 0;
        double totalWeight = 0.0;
        List<Map<String, Object>> listDeliveryOnTheWayPlan = deliveryOnTheWayPlanService.findProductDeliveryOnTheWayPlanDetailsByDeliveryId(poo.getId());
        Sheet sheet = wb.createSheet(poo.getDeliveryCode());
        Row row;
        Cell cell;
        String[] columnName = new String[]{"序号", "客户订单号", "批号", "厂内名称", "客户名称", "部件名称", "数量(托)", "数量(套)", "数量(kg)"};
        int i = 0;//第零行
        row = sheet.createRow(i);
        // 标题
        for (int a = 0; a < columnName.length; a++) {
            sheet.setColumnWidth(0, 6 * 256);// 设置列宽
            sheet.setColumnWidth(1, 8 * 256);
            sheet.setColumnWidth(2, 10 * 256);
            sheet.setColumnWidth(3, 18 * 256);
            sheet.setColumnWidth(4, 12 * 256);
            sheet.setColumnWidth(5, 6 * 256);
            sheet.setColumnWidth(6, 6 * 256);
            sheet.setColumnWidth(7, 6 * 256);
            sheet.setColumnWidth(8, 8 * 256);
            sheet.setColumnWidth(9, 12 * 256);
            sheet.setColumnWidth(10, 6 * 256);
            cell = row.createCell(a);
            cell.setCellStyle(cellStyle2);
            if (a == 0) {
                cell.setCellValue("浙江恒石纤维基业有限公司出库调拨单汇总");
                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 6));
            }
            if (a == columnName.length - 4) {
                cell.setCellValue("Q/HS RHS0081-2015");
                cell.setCellStyle(cellStyle1);
                sheet.addMergedRegion(new CellRangeAddress(i, i, 7, 10));
            }
        }
        i++;//第一行
        // 日期和文件编号
        row = sheet.createRow(i);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellStyle(cellStyle);
            if (a == 0) {
                cell.setCellValue("出库调拨单号:");
                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 1));
                continue;
            }
            if (a == 2) {
                cell.setCellValue(poo.getDeliveryCode());
                continue;
            }
            if (a == 3) {
                cell.setCellValue("终点仓库：");
                continue;
            }
            if (a == 4) {
                cell.setCellValue(poo.getWareHouseCode());
                continue;
            }
            if (a == 5) {
                cell.setCellValue("日期：");
                continue;
            }
            if (a == 6) {
                cell.setCellValue(sdf.format(poo.getDeliveryDate()));
            }
        }

        i++;
        // 表头列名
        row = sheet.createRow(i);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(columnName[a]);
        }
        int index = 0;
        for (Map<String, Object> detail : listDeliveryOnTheWayPlan) {
            index++;
            i++;
            row = sheet.createRow(i);
            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                cell.setCellStyle(cellStyle);
                switch (a) {
                    case 0 -> cell.setCellValue(index);
                    case 1 ->//客户订单号
                            cell.setCellValue(detail.get("salesOrderSubCode".toUpperCase()).toString());
                    case 2 ->//批号
                            cell.setCellValue(detail.get("batchcode".toUpperCase()).toString());
                    case 3 ->//厂内名称
                            cell.setCellValue(detail.get("factoryProductName".toUpperCase()).toString());
                    case 4 ->//客户名称
                            cell.setCellValue(detail.get("consumerProductName".toUpperCase()).toString());
                    case 5 ->//部件名称
                            cell.setCellValue(detail.get("partname".toUpperCase()).toString());
                    case 6 -> {//数量(托)
                        trayNum += Integer.parseInt(detail.get("totaltrayNum".toUpperCase()).toString());
                        cell.setCellValue(detail.get("totaltrayNum".toUpperCase()).toString());
                    }
                    case 7 ->//数量(套)
                            cell.setCellValue("");
                    case 8 -> {//数量(kg)
                        totalWeight += (detail.get("totalweight".toUpperCase()) == null ? 0.0 : (Double) detail.get("totalweight".toUpperCase()));
                        cell.setCellValue(detail.get("totalweight".toUpperCase()).toString());
                    }
                }
            }
        }

        i++;
        row = sheet.createRow(i);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellStyle(cellStyle);
            if (a == 0) {
                cell.setCellValue("总计");
            } else if (a == 6) {//托
                cell.setCellValue(trayNum);
            } else if (a == 7) {//套
                cell.setCellValue("");
            } else if (a == 8) {//重量
                cell.setCellValue(totalWeight);
            }
        }

        i++;
        row = sheet.createRow(i);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            if (a == 3) {
                cell.setCellValue("车牌:" + poo.getPlate());
            }
            if (a == 6) {
                cell.setCellValue("发货人:");
            }
            if (a == 8) {
                User u = deliveryOnTheWayPlanService.findById(User.class, poo.getDeliveryBizUserId());
                cell.setCellValue(u.getUserName());
            }
        }
        HttpUtils.download(response, wb, sdf.format(date));
    }

    @Journal(name = "加载销售订单页面")
    @RequestMapping(value = "pMove", method = RequestMethod.GET)
    public ModelAndView pMove(DeliveryOnTheWayPlan deliveryOnTheWayPlan, String force) {
        deliveryOnTheWayPlan = deliveryOnTheWayPlanService.findById(DeliveryOnTheWayPlan.class, deliveryOnTheWayPlan.getId());
        return new ModelAndView(pMoveUrl, model.addAttribute("deliveryOnTheWayPlan", deliveryOnTheWayPlan));
    }

    @Journal(name = "查看生产任务页面")
    @RequestMapping(value = "selectorOnTheWay", method = RequestMethod.GET)
    public ModelAndView selectorOnTheWay(Long deliveryId) throws IllegalArgumentException, SecurityException {
        return new ModelAndView(selectorOnTheWayBarCodeUrl, model.addAttribute("deliveryId", deliveryId.toString()));
    }
}