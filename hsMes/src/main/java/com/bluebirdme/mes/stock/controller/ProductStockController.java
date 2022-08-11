/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.stock.entity.ProductInRecord;
import com.bluebirdme.mes.stock.entity.ProductStockState;
import com.bluebirdme.mes.stock.entity.StockCheckResult;
import com.bluebirdme.mes.stock.service.IProductStockService;
import com.bluebirdme.mes.store.entity.Roll;
import com.bluebirdme.mes.utils.HttpUtils;
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
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 成品库存表Controller
 *
 * @author 徐波
 * @Date 2016-10-24 15:08:20
 */
@Controller
@RequestMapping("stock/productStock")
@Journal(name = "成品库存表")
public class ProductStockController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ProductStockController.class);

    /**
     * 成品库存表页面
     */
    final String index = "stock/productStock/productStock";
    final String addOrEdit = "stock/productStock/productStockAddOrEdit";
    final String warehouse = "stock/productStock/warehouseDetail";
    final String summary = "stock/productStock/summaryDetail";
    final String comparison = "stock/productStock/comparisonDetail";
    final String greige = "stock/productStock/greigeStock";
    final String stockmoveUrl = "stock/stockmove/stockmove";

    @Resource
    IProductStockService productStockService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @Journal(name = "显示库存页面")
    @RequestMapping("viewStock")
    public ModelAndView viewStock(String factoryProductName, String productProcessCode, String productProcessBomVersion, String workShopCode) {
        model.addAttribute("factoryProductName", factoryProductName);
        model.addAttribute("productProcessCode", productProcessCode);
        model.addAttribute("productProcessBomVersion", productProcessBomVersion);
        model.addAttribute("workShopCode", workShopCode);
        return new ModelAndView("planner/produce/viewStock", model);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取成品入库信息")
    @RequestMapping("list")
    public String getProductStock(Filter filter, Page page) throws Exception {
        if (null == filter.get("stockType")) {
            return ajaxError("请选择库存类型");
        }
        if (null == filter.get("stockState")) {
            return ajaxError("请选择库存状态");
        }

        Map<String, Object> findPageInfo = productStockService.findPageInfo(filter, page);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
        DecimalFormat df = new DecimalFormat("#.00");
        if (rows.size() != 0) {
            Double weight = 0.0;
            for (Map<String, Object> row : rows) {
                if (null != row.get("WEIGHT")
                        && row.get("WEIGHT") != "") {
                    weight += (Double) row.get("WEIGHT");
                } else if (null == row.get("WEIGHT")) {
                    ProductInRecord productInRecord = productStockService.findOne(ProductInRecord.class, "barCode", row.get("BARCODE"));
                    if (productInRecord != null && null != productInRecord.getWeight() && null != productInRecord.getInTime()) {
                        row.put("WEIGHT", productInRecord.getWeight());
                        weight += (Double) row.get("WEIGHT");
                        row.put("INTIME", productInRecord.getInTime());
                    }
                }
            }
            Object o = df.format(weight);
            map.put("CONSUMERNAME", "总重量(kg)");
            map.put("WEIGHT", o);
            list.add(map);
        } else {
            map.put("CONSUMERNAME", "总重量(kg)");
            map.put("WEIGHT", 0);
            list.add(map);
        }
        findPageInfo.put("footer", list);
        return GsonTools.toJson(findPageInfo);
    }


    @NoAuth
    @ResponseBody
    @Journal(name = "获取生产计划中库存信息")
    @RequestMapping("viewList")
    public String getViewStock(Filter filter, Page page, String factoryProductName, String productProcessCode, String productProcessBomVersion) throws Exception {
        filter.set("factoryProductName", factoryProductName);
        filter.set("productProcessCode", productProcessCode);
        filter.set("productProcessBomVersion", productProcessBomVersion);
        page.setAll(1);
        Map<String, Object> findPageInfo = productStockService.stockView(filter, page);
        BigDecimal weight = new BigDecimal(0);
        BigDecimal count = new BigDecimal(0);
        List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
        for (Map<String, Object> m : rows) {
            weight = weight.add((BigDecimal) m.get("WEIGHT"));
            count = count.add((BigDecimal) m.get("COUNT"));
        }
        Map<String, Object> footer = new HashMap<>();
        footer.put("WEIGHT", "共 " + weight + " KG");
        footer.put("COUNT", "共 " + count + " 托");
        findPageInfo.put("footer", new Object[]{footer});
        return GsonTools.toJson(findPageInfo);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取生产计划中库存信息1")
    @RequestMapping("viewList1")
    public String getViewStock1(Filter filter, Page page, String factoryProductName, String productProcessCode, String productProcessBomVersion, String workShopCode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("code", workShopCode);
        List<Department> departments = productStockService.findListByMap(Department.class, map);
        Map<String, Object> findPageInfo = new HashMap<>();
        if (departments.get(0).getType().equals("weave")) {
            filter.set("factoryProductName", factoryProductName);
            filter.set("productProcessCode", productProcessCode);
            filter.set("productProcessBomVersion", productProcessBomVersion);
            page.setAll(1);
            findPageInfo = productStockService.stockViewNew(filter, page);
        } else if (departments.get(0).getType().equals("cut")) {
            filter.set("factoryProductName", factoryProductName);
            filter.set("productProcessCode", productProcessCode);
            filter.set("productProcessBomVersion", productProcessBomVersion);
            page.setAll(1);
            findPageInfo = productStockService.stockViewNewPcj(filter, page);
        }
        if (findPageInfo.size() == 0) {
            return GsonTools.toJson(findPageInfo);
        }
        BigDecimal weight = new BigDecimal(0);
        BigDecimal count = new BigDecimal(0);
        DecimalFormat df = new DecimalFormat("0.00");
        List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
        for (Map<String, Object> m : rows) {
            weight = weight.add(new BigDecimal(m.get("WEIGHT").toString()));
            count = count.add(new BigDecimal(m.get("COUNT").toString()));
            Double weight1 = (Double) m.get("WEIGHT");
            m.put("WEIGHT", df.format(weight1));
        }
        double value = weight.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        Map<String, Object> footer = new HashMap<>();
        footer.put("WEIGHT", "共 " + value + " KG");
        footer.put("COUNT", "共 " + count + " 托");
        findPageInfo.put("footer", new Object[]{footer});
        return GsonTools.toJson(findPageInfo);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "导出成品入库信息")
    @RequestMapping(value = "productStockExport")
    public void getProductStockExport(Filter filter) throws Exception {
        Page page = new Page();
        page.setAll(1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Map<String, Object> map = productStockService.findPageInfo(filter, page);
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
        InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/productStock.xlsx");
        Workbook wb = new XSSFWorkbook(is);
        Sheet sheet = wb.getSheetAt(0);
        Row row;
        Cell cell;
        int i = 1;
        for (Map<String, Object> data : list) {
            row = sheet.createRow(i++);
            for (int j = 0; j < 25; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        cell.setCellValue(data.get("BARCODE") == null ? "" : data.get("BARCODE").toString());
                        break;
                    case 1:
                        cell.setCellValue(data.get("SALESORDERCODE") == null ? "" : data.get("SALESORDERCODE").toString());
                        break;
                    case 2:
                        cell.setCellValue(data.get("SALESORDERSUBCODEPRINT") == null ? "" : data.get("SALESORDERSUBCODEPRINT").toString());
                        break;
                    case 3:
                        cell.setCellValue(data.get("BATCHCODE") == null ? "" : data.get("BATCHCODE").toString());
                        break;
                    case 4:
                        cell.setCellValue(data.get("CATEGORYNAME") == null ? "" : data.get("CATEGORYNAME").toString());
                        break;
                    case 5:
                        cell.setCellValue(data.get("CATEGORYCODE") == null ? "" : data.get("CATEGORYCODE").toString());
                        break;
                    case 6:
                        cell.setCellValue(data.get("FACTORYPRODUCTNAME") == null ? "" : data.get("FACTORYPRODUCTNAME").toString());
                        break;
                    case 7:
                        cell.setCellValue(data.get("PRODUCTPROCESSCODE") == null ? "" : data.get("PRODUCTPROCESSCODE").toString());
                        break;
                    case 8:
                        cell.setCellValue(data.get("PRODUCTPROCESSBOMVERSION") == null ? "" : data.get("PRODUCTPROCESSBOMVERSION").toString());
                        break;
                    case 9:
                        cell.setCellValue(data.get("PRODUCTPACKAGINGCODE") == null ? "" : data.get("PRODUCTPACKAGINGCODE").toString());
                        break;
                    case 10:
                        cell.setCellValue(data.get("PRODUCTPACKAGEVERSION") == null ? "" : data.get("PRODUCTPACKAGEVERSION").toString());
                        break;
                    case 11:
                        cell.setCellValue(data.get("CONSUMERPRODUCTNAME") == null ? "" : data.get("CONSUMERPRODUCTNAME").toString());
                        break;
                    case 12:
                        cell.setCellValue(data.get("TCPROCBOMVERSIONPARTSNAME") == null ? "" : data.get("TCPROCBOMVERSIONPARTSNAME").toString());
                        break;
                    case 13:
                        cell.setCellValue(data.get("PRODUCTMODEL") == null ? "" : data.get("PRODUCTMODEL").toString());
                        break;
                    case 14:
                        cell.setCellValue(data.get("CONSUMERNAME") == null ? "" : data.get("CONSUMERNAME").toString());
                        break;
                    case 15:
                        if (data.get("WEIGHT") == null) {
                            Roll roll = productStockService.findOne(Roll.class, "partBarcode", data.get("BARCODE"));
                            if (roll != null) {
                                cell.setCellValue(roll.getRollWeight() == null ? "" : roll.getRollWeight().toString());
                            }
                        } else {
                            cell.setCellValue(data.get("WEIGHT").toString());
                        }
                        break;
                    case 16:
                        cell.setCellValue(data.get("WAREHOUSENAME") == null ? "" : data.get("WAREHOUSENAME").toString());
                        break;
                    case 17:
                        cell.setCellValue(data.get("WAREHOUSEPOSCODE") == null ? "" : data.get("WAREHOUSEPOSCODE").toString());
                        break;
                    case 18:
                        cell.setCellValue(data.get("INTIME") == null ? "" : data.get("INTIME").toString());
                        break;
                    case 19:
                        if (data.get("INTIME") != null) {
                            Date begintime_ms = dateFormat.parse(dateFormat.format((Date) data.get("INTIME")));
                            Date nowtimes = new Date();
                            Long millions = nowtimes.getTime() - begintime_ms.getTime();
                            double days = Math.floor(millions / (24 * 3600 * 1000));
                            cell.setCellValue(days);
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 20:
                        cell.setCellValue(data.get("PRODUCTSHELFLIFE") == null ? "" : data.get("PRODUCTSHELFLIFE").toString());
                        break;
                    case 21:
                        if (data.get("STOCKSTATE") != null) {
                            int stockState = Integer.parseInt(data.get("STOCKSTATE").toString());
                            if (stockState == 1) {
                                cell.setCellValue("在库");
                            } else if (stockState == -1) {
                                cell.setCellValue("不在库");
                            } else {
                                cell.setCellValue("");
                            }
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 22:
                        if (data.get("ISLOCKED") != null) {
                            int isLocked = Integer.parseInt(data.get("ISLOCKED").toString());
                            if (isLocked == 1) {
                                cell.setCellValue("冻结");
                            } else {
                                cell.setCellValue("正常");
                            }
                        } else {
                            cell.setCellValue("正常");
                        }
                        break;
                    case 23:
                        cell.setCellValue(data.get("ROLLQUALITYGRADECODE") == null ? "" : data.get("ROLLQUALITYGRADECODE").toString());
                        break;
                    case 24:
                        cell.setCellValue(data.get("ROLLOUTPUTTIME") == null ? "" : data.get("ROLLOUTPUTTIME").toString());
                        break;
                }
            }
        }
        HttpUtils.download(response, wb, "成品仓库信息表");
        is.close();
    }

    @Journal(name = "添加成品库存表页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(ProductStockState productStock) {
        return new ModelAndView(addOrEdit, model.addAttribute("productStock", productStock));
    }

    @ResponseBody
    @Journal(name = "保存成品库存表", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(ProductStockState productStock) throws Exception {
        productStockService.save(productStock);
        return GsonTools.toJson(productStock);
    }

    @Journal(name = "编辑成品库存表页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(ProductStockState productStock) {
        productStock = productStockService.findById(ProductStockState.class, productStock.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("productStock", productStock));
    }

    @ResponseBody
    @Journal(name = "编辑成品库存表", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(ProductStockState productStock) throws Exception {
        productStockService.update(productStock);
        return GsonTools.toJson(productStock);
    }

    @ResponseBody
    @Journal(name = "删除成品库存表", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        productStockService.delete(ProductStockState.class, ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "查询盘点明细")
    @RequestMapping(value = "findCheck", method = RequestMethod.POST)
    public String findCheck(String id) {
        Map<String, Object> c = new HashMap<>();
        c.put("cid", Long.valueOf(id));
        List<StockCheckResult> list = productStockService.findListByMap(StockCheckResult.class, c);
        return GsonTools.toJson(list);
    }

    @Journal(name = "库龄明细页面")
    @RequestMapping(value = "warehouse", method = RequestMethod.GET)
    public String warehouse() {
        return warehouse;
    }

    @ResponseBody
    @Journal(name = "库龄明细表")
    @RequestMapping(value = "warehouseDetail", method = RequestMethod.POST)
    public String warehouseDetail(Filter filter, Page page) {
        return GsonTools.toJson(productStockService.warehouseDetail(filter, page));
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "导出库龄明细表")
    @RequestMapping(value = "export")
    public void getProductStockExport1(Filter filter) throws Exception {
        Page page = new Page();
        page.setAll(1);
        Map<String, Object> productMap = productStockService.warehouseDetail(filter, page);
        List<Map<String, Object>> list = (List) productMap.get("rows");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String templateName = "库龄明细表";
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
        String[] columnName = new String[]{"订单号", "批次号", "场内名称", "客户产品名称",
                "期末结存数量(kg)", "客户", "进库日期", "库龄", "责任人"};
        int r = 0;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司库龄明细表");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 10; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
        r++;

        row = sheet.createRow(r);
        sheet.setColumnWidth(0, 16 * 256);// 设置列宽
        sheet.setColumnWidth(1, 16 * 256);
        sheet.setColumnWidth(2, 18 * 256);
        sheet.setColumnWidth(3, 26 * 256);
        sheet.setColumnWidth(5, 28 * 256);
        sheet.setColumnWidth(7, 28 * 256);
        sheet.setColumnWidth(8, 28 * 256);
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
                        if (data.get("SALESORDERCODE") != null) {
                            cell.setCellValue(data.get("SALESORDERCODE").toString());
                        }
                        break;
                    case 1:
                        if (data.get("BATCHCODE") != null) {
                            cell.setCellValue(data.get("BATCHCODE").toString());
                        }
                        break;
                    case 2:
                        if (data.get("FACTORYPRODUCTNAME") != null) {
                            cell.setCellValue(data.get("FACTORYPRODUCTNAME")
                                    .toString());
                        }
                        break;
                    case 3:
                        if (data.get("CONSUMERPRODUCTNAME") != null) {
                            cell.setCellValue(data.get("CONSUMERPRODUCTNAME")
                                    .toString());
                        }
                        break;
                    case 4:
                        if (data.get("SUMWEIGHT") != null) {
                            cell.setCellValue(data.get("SUMWEIGHT").toString());
                        }
                        break;
                    case 5:
                        if (data.get("CONSUMERNAME") != null) {
                            cell.setCellValue(data.get("CONSUMERNAME").toString());
                        }
                        break;
                    case 6:
                        if (data.get("INTIME") != null) {
                            cell.setCellValue(data.get("INTIME").toString());
                        }
                        break;
                    case 7:
                        if (data.get("INTIME") != null) {
                            java.util.Date date = sdf1.parse(data.get("INTIME").toString());
                            long s1 = date.getTime();
                            long s2 = System.currentTimeMillis();
                            long day = (s2 - s1) / (24 * 3600 * 1000);
                            if (day <= 30)
                                cell.setCellValue("1个月以内");
                            else if (day <= 90)
                                cell.setCellValue("1-3个月");
                            else if (day <= 180)
                                cell.setCellValue("3-6个月");
                            else if (day <= 360)
                                cell.setCellValue("6-12个月");
                            else
                                cell.setCellValue("12个月以上");
                        }
                        break;
                    case 8:
                        if (data.get("LOGINNAME") != null) {
                            cell.setCellValue(data.get("LOGINNAME").toString());
                        }
                        break;
                }
            }
            r++;
        }
        HttpUtils.download(response, wb, templateName);
    }

    @Journal(name = "库龄汇总页面")
    @RequestMapping(value = "summary", method = RequestMethod.GET)
    public String summary() {
        return summary;
    }

    @ResponseBody
    @Journal(name = "库龄汇总表")
    @RequestMapping(value = "summaryDetail", method = RequestMethod.POST)
    public String summaryDetail(Filter filter, Page page) {
        if (filter.get("TIME1") == null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cale = Calendar.getInstance();
            // cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            String firstday = format.format(cale.getTime());
            filter.set("TIME1", firstday + " 08:00:00");
        }
        if (filter.get("TIME2") == null) {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(d);
            filter.set("TIME2", date);
        }
        return GsonTools.toJson(productStockService.summaryDetail(filter, page));
    }

    @ResponseBody
    @Journal(name = "库龄汇总表导出")
    @RequestMapping(value = "exportSummary", method = RequestMethod.GET)
    public void exportSummary(Filter filter, Page page) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] title = {"库龄时间段", "上期结存", "本期结存", "增降数量(-为减少)"};
        InputStream is = new FileInputStream(new File(PathUtils.getClassPath()
                + "template/shop-storage-category.xlsx"));
        Workbook wb = new XSSFWorkbook(is);
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.createRow(0);
        Cell cell;

        Page page1 = new Page();
        page1.setRows(10000);
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }
        String start = filter.get("TIME1");
        String end = filter.get("TIME2");
        filter.set("TIME2", Objects.requireNonNullElseGet(end, () -> sdf.format(new Date())));
        if (!"".equals(start)) {
            filter.set("TIME1", start);
        }

        try {
            Map<String, Object> map = productStockService.summaryDetail(filter, page1);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> list = (ArrayList<Map<String, Object>>) map.get("rows");
            Cell nextCell;
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> m = list.get(i);
                Row nextRow = sheet.createRow(i + 1);
                nextCell = nextRow.createCell(0);
                nextCell.setCellValue(m.get("DAYS").toString());
                nextCell = nextRow.createCell(1);
                nextCell.setCellValue(m.get("OLDWEIGHT").toString() == null ? "" : m.get("OLDWEIGHT").toString());
                nextCell = nextRow.createCell(2);
                nextCell.setCellValue(m.get("NOWWEIGHT").toString() == null ? "" : m.get("NOWWEIGHT").toString());
                nextCell = nextRow.createCell(3);
                nextCell.setCellValue(m.get("DIFFERENCE").toString() == null ? "" : m.get("DIFFERENCE").toString());
            }
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String("库龄汇总表".getBytes("gbk"), "iso8859-1") + ".xls");
            response.setContentType("application/msexcel");
            ServletOutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @Journal(name = "库龄数量对比表页面")
    @RequestMapping(value = "comparison", method = RequestMethod.GET)
    public String comparison() {
        return comparison;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @Journal(name = "库龄数量对比表")
    @RequestMapping(value = "comparisonDetail", method = RequestMethod.POST)
    public String comparisonDetail(Filter filter, Page page) throws Exception {
        if (filter.get("TIME1") == null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cale = Calendar.getInstance();
            cale.set(Calendar.DAY_OF_MONTH, 0);
            String firstday = format.format(cale.getTime());
            filter.set("TIME1", firstday + " 08:00:00");
        }
        if (filter.get("TIME2") == null) {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(d);
            filter.set("TIME2", date);
        }
        // 总计
        Map<String, Object> findPageInfo = productStockService.comparisonDetail(filter, page);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
        DecimalFormat df = new DecimalFormat("#.0");
        findPageInfo.put("footer", list);
        if (rows.size() != 0) {
            page.setAll(1);
            Map<String, Object> map2 = productStockService.comparisonDetail(filter, page);
            List<Map<String, Object>> r = (List<Map<String, Object>>) map2.get("rows");
            Double old = 0.0;
            Double now = 0.0;
            Double diff = 0.0;
            for (Map<String, Object> stringObjectMap : r) {
                Double oldweight = (Double) stringObjectMap.get("OLDWEIGHT");
                old += oldweight == null ? 0.0 : oldweight;
                Double nowweight = (Double) stringObjectMap.get("NOWWEIGHT");
                now += nowweight == null ? 0.0 : nowweight;
                diff += (Double) stringObjectMap.get("DIFFERENCE");
            }
            Object s = df.format(old);
            Object i = df.format(now);
            Object d = df.format(diff);
            map.put("CONSUMERPRODUCTNAME", "总计：");
            map.put("OLDWEIGHT", s);
            map.put("NOWWEIGHT", i);
            map.put("DIFFERENCE", d);
            list.add(map);
        } else {
            map.put("CONSUMERPRODUCTNAME", "总计：");
            map.put("OLDWEIGHT", 0);
            map.put("NOWWEIGHT", 0);
            map.put("DIFFERENCE", 0);
            list.add(map);
        }
        findPageInfo.put("footer", list);
        return GsonTools.toJson(findPageInfo);
    }

    @ResponseBody
    @Journal(name = "库龄数量对比表导出")
    @RequestMapping(value = "exportComparison", method = RequestMethod.GET)
    public void exportComparison(Filter filter) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = filter.get("TIME1");
        String end = filter.get("TIME2");
        filter.set("TIME2", Objects.requireNonNullElseGet(end, () -> sdf.format(new Date())));
        if (!"".equals(start)) {
            filter.set("TIME1", start);
        }
        Page page = new Page();
        page.setAll(1);
        Map<String, Object> map = productStockService.comparisonDetail(filter, page);
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
        InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/productComparison.xlsx");
        Workbook wb = new SXSSFWorkbook(new XSSFWorkbook(is));
        Sheet sheet = wb.getSheetAt(0);
        Row row;
        Cell cell;
        int i = 1;
        for (Map<String, Object> data : list) {
            row = sheet.createRow(i++);
            for (int j = 0; j < 20; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0 ->
                            cell.setCellValue(data.get("SALESORDERCODE") == null ? "" : data.get("SALESORDERCODE").toString());
                    case 1 -> cell.setCellValue(data.get("BATCHCODE") == null ? "" : data.get("BATCHCODE").toString());
                    case 2 ->
                            cell.setCellValue(data.get("PRODUCTMODEL") == null ? "" : data.get("PRODUCTMODEL").toString());
                    case 3 ->
                            cell.setCellValue(data.get("FACTORYPRODUCTNAME") == null ? "" : data.get("FACTORYPRODUCTNAME").toString());
                    case 4 ->
                            cell.setCellValue(data.get("CONSUMERPRODUCTNAME") == null ? "" : data.get("CONSUMERPRODUCTNAME").toString());
                    case 5 -> cell.setCellValue(data.get("OLDWEIGHT") == null ? "" : data.get("OLDWEIGHT").toString());
                    case 6 -> cell.setCellValue(data.get("NOWWEIGHT") == null ? "" : data.get("NOWWEIGHT").toString());
                    case 7 ->
                            cell.setCellValue(data.get("DIFFERENCE") == null ? "" : data.get("DIFFERENCE").toString());
                    case 8 ->
                            cell.setCellValue(data.get("CONSUMERNAME") == null ? "" : data.get("CONSUMERNAME").toString());
                    case 9 -> cell.setCellValue(data.get("INTIME") == null ? "" : data.get("INTIME").toString());
                    case 10 -> cell.setCellValue(data.get("DAYS") == null ? "" : data.get("DAYS").toString());
                }
            }
        }
        HttpUtils.download(response, wb, "库龄数量对比表");
        is.close();
    }

    @Journal(name = "胚布仓库")
    @RequestMapping(value = "greigeStock", method = RequestMethod.GET)
    public String greigeStock() {
        return greige;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取胚布库存信息")
    @RequestMapping("getGreigeStockInfo")
    public String getGreigeStockInfo(Filter filter, Page page) {
        Map<String, Object> findPageInfo = productStockService.getGreigeStockInfo(filter, page);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
        DecimalFormat df = new DecimalFormat("#.00");
        if (rows.size() != 0) {
            Double d = 0.0;
            for (Map<String, Object> row : rows) {
                if (row.get("WEIGHT") != null && row.get("WEIGHT") != "") {
                    d += (Double) row.get("WEIGHT");
                }
            }
            Object o = df.format(d);
            map.put("CONSUMERNAME", "总重量(kg)");
            map.put("WEIGHT", o);
            list.add(map);
        } else {
            map.put("CONSUMERNAME", "总重量(kg)");
            map.put("WEIGHT", 0);
            list.add(map);
        }
        findPageInfo.put("footer", list);
        return GsonTools.toJson(findPageInfo);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "导出胚布库存信息")
    @RequestMapping(value = "greigeStockExport")
    public void getGreigeStockExport(Filter filter) throws Exception {
        Page page = new Page();
        page.setAll(1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Map<String, Object> map = productStockService.getGreigeStockInfo(filter, page);
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
        InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/productStock.xlsx");
        Workbook wb = new SXSSFWorkbook(new XSSFWorkbook(is));
        Sheet sheet = wb.getSheetAt(0);
        Row row;
        Cell cell;
        int i = 1;
        for (Map<String, Object> data : list) {
            row = sheet.createRow(i++);
            for (int j = 0; j < 20; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        cell.setCellValue(data.get("BARCODE") == null ? "" : data.get("BARCODE").toString());
                        break;
                    case 1:
                        cell.setCellValue(data.get("SALESORDERCODE") == null ? "" : data.get("SALESORDERCODE").toString());
                        break;
                    case 2:
                        cell.setCellValue(data.get("BATCHCODE") == null ? "" : data.get("BATCHCODE").toString());
                        break;
                    case 3:
                        cell.setCellValue(data.get("CATEGORYNAME") == null ? "" : data.get("CATEGORYNAME").toString());
                        break;
                    case 4:
                        cell.setCellValue(data.get("CATEGORYCODE") == null ? "" : data.get("CATEGORYCODE").toString());
                        break;
                    case 5:
                        cell.setCellValue(data.get("FACTORYPRODUCTNAME") == null ? "" : data.get("FACTORYPRODUCTNAME").toString());
                        break;
                    case 6:
                        cell.setCellValue(data.get("CONSUMERPRODUCTNAME") == null ? "" : data.get("CONSUMERPRODUCTNAME").toString());
                        break;
                    case 7:
                        cell.setCellValue(data.get("TCPROCBOMVERSIONPARTSNAME") == null ? "" : data.get("TCPROCBOMVERSIONPARTSNAME").toString());
                        break;
                    case 8:
                        cell.setCellValue(data.get("PRODUCTMODEL") == null ? "" : data.get("PRODUCTMODEL").toString());
                        break;
                    case 9:
                        cell.setCellValue(data.get("CONSUMERNAME") == null ? "" : data.get("CONSUMERNAME").toString());
                        break;
                    case 10:
                        cell.setCellValue(data.get("WEIGHT") == null ? "" : data.get("WEIGHT").toString());
                        break;
                    case 11:
                        cell.setCellValue(data.get("WAREHOUSECODE") == null ? "" : data.get("WAREHOUSECODE").toString());
                        break;
                    case 12:
                        cell.setCellValue(data.get("WAREHOUSEPOSCODE") == null ? "" : data.get("WAREHOUSEPOSCODE").toString());
                        break;
                    case 13:
                        cell.setCellValue(data.get("INTIME") == null ? "" : data.get("INTIME").toString());
                        break;
                    case 14:
                        if (data.get("INTIME") != null) {
                            Date begintime_ms = dateFormat.parse(dateFormat.format((Date) data.get("INTIME")));
                            Date nowtimes = new Date();
                            Long millions = nowtimes.getTime() - begintime_ms.getTime();
                            double days = Math.floor(millions / (24 * 3600 * 1000));
                            cell.setCellValue(days);
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 15:
                        cell.setCellValue(data.get("PRODUCTSHELFLIFE") == null ? "" : data.get("PRODUCTSHELFLIFE").toString());
                        break;
                    case 16:
                        if (data.get("STOCKSTATE") != null) {
                            int stockState = Integer.parseInt(data.get("STOCKSTATE").toString());
                            if (stockState == 1) {
                                cell.setCellValue("在库");
                            } else if (stockState == -1) {
                                cell.setCellValue("不在库");
                            } else {
                                cell.setCellValue("");
                            }
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 17:
                        cell.setCellValue(data.get("ROLLQUALITYGRADECODE") == null ? "" : data.get("ROLLQUALITYGRADECODE").toString());
                        break;
                }
            }
        }
        HttpUtils.download(response, wb, "胚布仓库信息表");
        is.close();
    }


    @Journal(name = "移库记录页面")
    @RequestMapping("move")
    public ModelAndView moveInfo() {
        return new ModelAndView("stock/productStock/productMoveInfo", model);
    }

    @Journal(name = "移库记录页面")
    @RequestMapping("stockmove")
    public ModelAndView moveInfo2() {
        return new ModelAndView(stockmoveUrl, model);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取移库记录")
    @RequestMapping("moveList")
    public String moveList(Filter filter, Page page) {
        Map<String, Object> findPageInfo = productStockService.findPageInfoMoveList(filter, page);
        return GsonTools.toJson(findPageInfo);
    }

    @ResponseBody
    @Journal(name = "获取移库记录2")
    @RequestMapping("moveList2")
    public String moveList2(String producePlanDetailId, String start, String end) {
        Page page = new Page();
        page.setAll(1);
        Filter filter = new Filter();
        filter.set("producePlanDetailId", producePlanDetailId);
        if (start != null) filter.set("start", start);
        if (end != null) filter.set("end", end);
        Map<String, Object> findPageInfo = productStockService.moveInfolist(filter, page);
        return GsonTools.toJson(findPageInfo);
    }
}
