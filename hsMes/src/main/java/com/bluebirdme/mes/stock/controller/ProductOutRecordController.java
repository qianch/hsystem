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
import com.bluebirdme.mes.stock.entity.ProductInRecord;
import com.bluebirdme.mes.stock.entity.ProductOutRecord;
import com.bluebirdme.mes.stock.service.IProductOutRecordService;
import com.bluebirdme.mes.store.entity.Tray;
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
import java.util.List;
import java.util.Map;

/**
 * 成品出库记录表Controller
 *
 * @author 宋黎明
 * @Date 2016-11-16 13:44:46
 */
@Controller
@RequestMapping("stock/productOutRecord")
@Journal(name = "成品出库记录表")
public class ProductOutRecordController extends BaseController {
    /**
     * 成品出库记录表页面
     */
    final String index = "stock/productStock/productOutRecord";
    final String addOrEdit = "stock/productStock/productOutRecordAddOrEdit";

    @Resource
    IProductOutRecordService productOutRecordService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取成品出库记录表列表信息")
    @RequestMapping("list")
    public String getProductOutRecord(Filter filter, Page page) throws Exception {
        Map<String, Object> findPageInfo = productOutRecordService.findPageInfo(filter, page);
        for (int i = 0; i < ((List<Map<String, Object>>) findPageInfo.get("rows")).size(); i++) {
            String barCode = ((List<Map<String, Object>>) findPageInfo.get("rows")).get(i).get("barCode".toUpperCase()).toString();
            Tray tray = productOutRecordService.findOne(Tray.class, "trayBarcode", barCode);
            if (tray != null && tray.getWeight() != null && tray.getWeight() > 0) {
                ((List<Map<String, Object>>) findPageInfo.get("rows")).get(i).put("rollweight".toUpperCase(), tray.getWeight());
                continue;
            }
            ProductInRecord productInRecord = productOutRecordService.findOne(ProductInRecord.class, "barCode", barCode);
            if (productInRecord != null && productInRecord.getWeight() != null) {
                ((List<Map<String, Object>>) findPageInfo.get("rows")).get(i).put("rollweight".toUpperCase(), productInRecord.getWeight());
            }
        }
        return GsonTools.toJson(findPageInfo);
    }


    @Journal(name = "添加成品出库记录表页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(ProductOutRecord productOutRecord) {
        return new ModelAndView(addOrEdit, model.addAttribute("productOutRecord", productOutRecord));
    }

    @ResponseBody
    @Journal(name = "保存成品出库记录表", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(ProductOutRecord productOutRecord) throws Exception {
        productOutRecordService.save(productOutRecord);
        return GsonTools.toJson(productOutRecord);
    }

    @Journal(name = "编辑成品出库记录表页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(ProductOutRecord productOutRecord) {
        productOutRecord = productOutRecordService.findById(ProductOutRecord.class, productOutRecord.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("productOutRecord", productOutRecord));
    }

    @ResponseBody
    @Journal(name = "编辑成品出库记录表", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(ProductOutRecord productOutRecord) throws Exception {
        productOutRecordService.update(productOutRecord);
        return GsonTools.toJson(productOutRecord);
    }

    @ResponseBody
    @Journal(name = "删除成品出库记录表", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        productOutRecordService.delete(ProductOutRecord.class, ids);
        return Constant.AJAX_SUCCESS;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "导出成品出库信息")
    @RequestMapping("export")
    public void getProductStockExport(Filter filter) throws Exception {
        Page page = new Page();
        page.setAll(1);
        Map<String, Object> pageInfo = productOutRecordService.findPageInfo(filter, page);
        for (int i = 0; i < ((List<Map<String, Object>>) pageInfo.get("rows")).size(); i++) {
            String barCode = ((List<Map<String, Object>>) pageInfo.get("rows")).get(i).get("barCode".toUpperCase()).toString();

            Tray tray = productOutRecordService.findOne(Tray.class, "trayBarcode", barCode);
            if (tray != null && tray.getWeight() != null && tray.getWeight() > 0) {
                ((List<Map<String, Object>>) pageInfo.get("rows")).get(i).put("rollweight".toUpperCase(), tray.getWeight());
                continue;
            }

            ProductInRecord productInRecord = productOutRecordService.findOne(ProductInRecord.class, "barCode", barCode);
            if (productInRecord != null && productInRecord.getWeight() != null) {
                ((List<Map<String, Object>>) pageInfo.get("rows")).get(i).put("rollweight".toUpperCase(), productInRecord.getWeight());
            }
        }

        List<Map<String, Object>> list = (List) pageInfo.get("rows");

        String templateName = "成品出库记录单";
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
        String[] columnName = new String[]{"条码号", "产品规格", "重量(kg)", "订单号", "批次号", "客户", "仓库", "库位", "操作人", "出库时间"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司成品出库明细表");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 9; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
        r++;
        sheet.setColumnWidth(0, 20 * 256);// 设置列宽
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 30 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        sheet.setColumnWidth(9, 20 * 256);
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
                        if (data.get("PRODUCTMODEL") != null) {
                            cell.setCellValue(data.get("PRODUCTMODEL").toString());
                        }
                        break;
                    case 2:
                        if (data.get("ROLLWEIGHT") != null) {
                            cell.setCellValue(data.get("ROLLWEIGHT").toString());
                        }
                        break;
                    case 3:
                        if (data.get("SALESORDERCODE") != null) {
                            cell.setCellValue(data.get("SALESORDERCODE").toString());
                        }
                        break;
                    case 4:
                        if (data.get("BATCHCODE") != null) {
                            cell.setCellValue(data.get("BATCHCODE").toString());
                        }
                        break;
                    case 5:
                        if (data.get("CONSUMERNAME") != null) {
                            cell.setCellValue(data.get("CONSUMERNAME").toString());
                        }
                        break;
                    case 6:
                        if (data.get("WAREHOUSENAME") != null) {
                            cell.setCellValue(data.get("WAREHOUSENAME").toString());
                        }
                        break;
                    case 7:
                        if (data.get("WAREHOUSEPOSCODE") != null) {
                            cell.setCellValue(data.get("WAREHOUSEPOSCODE").toString());
                        }
                        break;
                    case 8:
                        if (data.get("OPERATEUSERNAME") != null) {
                            cell.setCellValue(data.get("OPERATEUSERNAME")
                                    .toString());
                        }
                        break;
                    case 9:
                        if (data.get("OUTTIME") != null) {
                            cell.setCellValue(data.get("OUTTIME").toString().substring(0, 19));
                        }
                        break;
                }
            }
            r++;
        }
        HttpUtils.download(response, wb, templateName);
    }
}