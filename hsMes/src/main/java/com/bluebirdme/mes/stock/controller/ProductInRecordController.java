/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.stock.service.IProductInRecordService;
import com.bluebirdme.mes.utils.HttpUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成品入库记录表Controller
 *
 * @author 宋黎明
 * @Date 2016-10-24 15:08:20
 */
@Controller
@RequestMapping("stock/productInRecord")
@Journal(name = "成品入库记录表")
public class ProductInRecordController extends BaseController {
    /**
     * 成品入库记录页面
     */
    final String index = "stock/productStock/productStockInRecord";

    /**
     * 待入库
     */
    final String index1 = "stock/productStock/productStockInRecordDRK";

    @Resource
    IProductInRecordService productInRecordService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @Journal(name = "待入库记录查询")
    @RequestMapping(method = RequestMethod.GET, value = "drkPage")
    public String drkPage() {
        return index1;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取成品入库信息")
    @RequestMapping("list")
    public String getProductStock(Filter filter, Page page) throws Exception {
        Map<String, Object> findPageInfo = productInRecordService.findPageInfo(filter, page);
        return GsonTools.toJson(findPageInfo);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取待入库入库信息")
    @RequestMapping("drkList")
    public String getProductStockDRK(Filter filter, Page page) throws Exception {
        Map<String, Object> findPageInfo = productInRecordService.findPageInfoDRK(filter, page);
        return GsonTools.toJson(findPageInfo);
    }


    @NoAuth
    @ResponseBody
    @Journal(name = "导出成品入库信息1")
    @RequestMapping(value = "export1")
    public void getProductStockExport1(Filter filter) throws Exception {
        Page page = new Page();
        page.setRows(99999);
        Map<String, Object> productMap = productInRecordService.findPageInfo(filter, page);
        List<Map<String, Object>> list = (List) productMap.get("rows");
        String templateName = "成品入库记录单";
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
        String[] columnName = new String[]{"条码号", "订单号", "成品类别名称", "成品类别代码", "生产任务单编号", "厂内名称", "客户产品名称", "部件名称", "产品规格", "批次号", "客户",
                "米长(m)", "门幅(mm)", "重量(kg)", "仓库", "库位", "生产车间", "入库人", "入库时间"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司成品入库明细表");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 20; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 18));
        r++;

        row = sheet.createRow(r);
        sheet.setColumnWidth(0, 16 * 256);// 设置列宽
        sheet.setColumnWidth(1, 16 * 256);
        sheet.setColumnWidth(2, 18 * 256);
        sheet.setColumnWidth(3, 18 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 24 * 256);
        sheet.setColumnWidth(6, 30 * 256);
        sheet.setColumnWidth(7, 11 * 256);
        sheet.setColumnWidth(8, 11 * 256);
        sheet.setColumnWidth(9, 11 * 256);
        sheet.setColumnWidth(10, 15 * 256);
        sheet.setColumnWidth(11, 11 * 256);
        sheet.setColumnWidth(12, 11 * 256);
        sheet.setColumnWidth(13, 11 * 256);
        sheet.setColumnWidth(14, 11 * 256);
        sheet.setColumnWidth(15, 11 * 256);
        sheet.setColumnWidth(16, 11 * 256);
        sheet.setColumnWidth(17, 11 * 256);
        sheet.setColumnWidth(18, 24 * 256);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }
        r++;

        for (Map<String, Object> data : list) {
            row = sheet.createRow(r);
            // 当产品型号和原来的产品型号不同时，记录现在的型号并插入一行型号
            for (int j = 0; j < columnName.length; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        //条码号
                        if (data.get("BARCODE") != null) {
                            cell.setCellValue(data.get("BARCODE").toString());
                        }
                        break;
                    case 1:
                        //订单号
                        if (data.get("SALESORDERCODE") != null) {
                            cell.setCellValue(data.get("SALESORDERCODE").toString());
                        }
                        break;
                    case 2:
                        //成品类别名称
                        if (data.get("CATEGORYNAME") != null) {
                            cell.setCellValue(data.get("CATEGORYNAME").toString());
                        }
                        break;
                    case 3:
                        //成品类别代码
                        if (data.get("CATEGORYCODE") != null) {
                            cell.setCellValue(data.get("CATEGORYCODE").toString());
                        }
                        break;
                    case 4:
                        //生产任务单编号
                        if (data.get("SCRW") != null) {
                            cell.setCellValue(data.get("SCRW").toString());
                        }
                        break;
                    case 5:
                        //厂内名称
                        if (data.get("FACTORYPRODUCTNAME") != null) {
                            cell.setCellValue(data.get("FACTORYPRODUCTNAME").toString());
                        }
                        break;
                    case 6:
                        //客户产品名称
                        if (data.get("CONSUMERPRODUCTNAME") != null) {
                            cell.setCellValue(data.get("CONSUMERPRODUCTNAME").toString());
                        }
                        break;
                    case 7:
                        //部件名称
                        if (data.get("TCPROCBOMVERSIONPARTSNAME") != null) {
                            cell.setCellValue(data.get("TCPROCBOMVERSIONPARTSNAME").toString());
                        }
                        break;
                    case 8:
                        //产品规格
                        if (data.get("PRODUCTMODEL") != null) {
                            cell.setCellValue(data.get("PRODUCTMODEL").toString());
                        }
                        break;
                    case 9:
                        //批次号
                        if (data.get("BATCHCODE") != null) {
                            cell.setCellValue(data.get("BATCHCODE").toString());
                        }
                        break;

                    case 10:
                        //客户
                        if (data.get("CONSUMERNAME") != null) {
                            cell.setCellValue(data.get("CONSUMERNAME").toString());
                        }
                        break;
                    case 11:
                        //米长(m)
                        if (data.get("PRODUCTLENGTH") != null) {
                            cell.setCellValue(data.get("PRODUCTLENGTH").toString());
                        }
                        break;
                    case 12:
                        //门幅(mm)
                        if (data.get("PRODUCTWIDTH") != null) {
                            cell.setCellValue(data.get("PRODUCTWIDTH").toString());
                        }
                        break;

                    case 13:
                        //重量(kg)
                        if (data.get("WEIGHT") != null) {
                            cell.setCellValue(data.get("WEIGHT")
                                    .toString());
                        }
                        break;
                    case 14:
                        //仓库
                        if (data.get("WAREHOUSENAME") != null) {
                            cell.setCellValue(data.get("WAREHOUSENAME")
                                    .toString());
                        }
                        break;
                    case 15:
                        //库位
                        if (data.get("WAREHOUSEPOSCODE") != null) {
                            cell.setCellValue(data.get("WAREHOUSEPOSCODE")
                                    .toString());
                        }
                        break;
                    case 16:
                        //生产车间
                        if (data.get("WORKSHOP") != null) {
                            cell.setCellValue(data.get("WORKSHOP")
                                    .toString());
                        }
                        break;
                    case 17:
                        //操作人
                        if (data.get("OPERATEUSERNAME") != null) {
                            cell.setCellValue(data.get("OPERATEUSERNAME")
                                    .toString());
                        }
                        break;
                    case 18:
                        //入库时间
                        if (data.get("INTIME") != null) {
                            cell.setCellValue(data.get("INTIME").toString().substring(0, 19));
                        }
                        break;
                }
            }
            r++;
        }
        HttpUtils.download(response, wb, templateName);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "导出成品入库信息")
    @RequestMapping("export")
    public void getProductStockExport(String warehouseCode, String warehousePosCode, String code, String workShop, String salesCode, String batchCode, String consumer, String model, String start, String end) throws Exception {
        Page page = new Page();
        page.setAll(1);
        Filter filter = new Filter();
        HashMap<String, String> filterMap = new HashMap<>();
        if (warehouseCode != null) {
            filterMap.put("warehouseCode", "like:" + warehouseCode);
        }
        if (warehousePosCode != null) {
            filterMap.put("warehousePosCode", "like:" + warehousePosCode);
        }
        if (code != null) {
            filterMap.put("code", "like:" + code);
        }
        if (workShop != null) {
            filterMap.put("workShop", "like:" + workShop);
        }
        if (salesCode != null) {
            filterMap.put("salesCode", "like:" + salesCode);
        }
        if (batchCode != null) {
            filterMap.put("batchCode", "like:" + batchCode);
        }
        ;
        if (consumer != null) {
            filterMap.put("consumer", "like:" + consumer);
        }
        if (model != null) {
            filterMap.put("model", "like:" + model);
        }
        if (start != null) {
            filterMap.put("start", start);
        }
        if (end != null) {
            filterMap.put("end", end);
        }
        filter.setFilter(filterMap);
        Map<String, Object> pageInfo = productInRecordService.findPageInfo(
                filter, page);
        List<Map<String, Object>> list = (List) pageInfo.get("rows");

        String templateName = "成品入库记录单";
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
        String[] columnName = new String[]{"条码号", "订单号", "产品规格", "批次号", "客户",
                "米长(m)", "门幅(mm)", "重量(kg)", "库位", "仓库", "生产车间", "操作人", "入库时间"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司成品入库明细表");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 13; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 12));
        r++;

        row = sheet.createRow(r);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }
        r++;

        for (Map<String, Object> data : list) {
            row = sheet.createRow(r);
            // 当产品型号和原来的产品型号不同时，记录现在的型号并插入一行型号
            for (int j = 0; j < columnName.length; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        if (data.get("BARCODE") != null) {
                            cell.setCellValue(data.get("BARCODE").toString());
                        }
                        break;
                    case 1:
                        if (data.get("SALESORDERCODE") != null) {
                            cell.setCellValue(data.get("SALESORDERCODE").toString());
                        }
                        break;
                    case 2:
                        if (data.get("PRODUCTMODEL") != null) {
                            cell.setCellValue(data.get("PRODUCTMODEL").toString());
                        }
                        break;
                    case 3:
                        if (data.get("BATCHCODE") != null) {
                            cell.setCellValue(data.get("BATCHCODE").toString());
                        }
                        break;
                    case 4:
                        if (data.get("CONSUMERNAME") != null) {
                            cell.setCellValue(data.get("CONSUMERNAME").toString());
                        }
                        break;
                    case 5:
                        if (data.get("PRODUCTLENGTH") != null) {
                            cell.setCellValue(data.get("PRODUCTLENGTH").toString());
                        }
                        break;
                    case 6:
                        if (data.get("PRODUCTWIDTH") != null) {
                            cell.setCellValue(data.get("PRODUCTWIDTH").toString());
                        }
                        break;
                    case 7:
                        if (data.get("ROLLWEIGHT") != null) {
                            cell.setCellValue(data.get("ROLLWEIGHT").toString());
                        }
                        break;

                    case 8:
                        if (data.get("WAREHOUSEPOSCODE") != null) {
                            cell.setCellValue(data.get("WAREHOUSEPOSCODE")
                                    .toString());
                        }
                        break;
                    case 9:
                        if (data.get("WAREHOUSENAME") != null) {
                            cell.setCellValue(data.get("WAREHOUSENAME").toString());
                        }
                        break;
                    case 10:
                        if (data.get("WORKSHOP") != null) {
                            cell.setCellValue(data.get("WORKSHOP").toString());
                        }
                        break;

                    case 11:
                        if (data.get("OPERATEUSERNAME") != null) {
                            cell.setCellValue(data.get("OPERATEUSERNAME")
                                    .toString());
                        }
                        break;
                    case 12:
                        if (data.get("INTIME") != null) {
                            cell.setCellValue(data.get("INTIME").toString().substring(0, 19));
                        }
                        break;
                }
            }
            r++;
        }
        HttpUtils.download(response, wb, templateName);
    }
}