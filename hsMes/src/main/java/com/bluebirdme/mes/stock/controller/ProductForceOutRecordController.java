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
import com.bluebirdme.mes.stock.entity.ProductForceOutRecord;
import com.bluebirdme.mes.stock.service.IProductForceOutRecordService;
import com.bluebirdme.mes.utils.HttpUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 原料强制出库Controller
 *
 * @author 徐波
 * @Date 2017-2-13 14:10:25
 */
@Controller
@RequestMapping("/productForceOutRecord")
@Journal(name = "原料强制出库")
public class ProductForceOutRecordController extends BaseController {
    /**
     * 原料强制出库页面
     */
    final String index = "stock/productForceOutRecord";
    final String addOrEdit = "stock/productForceOutRecordAddOrEdit";

    @Resource
    IProductForceOutRecordService productForceOutRecordService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取原料强制出库列表信息")
    @RequestMapping("list")
    public String getProductForceOutRecord(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(productForceOutRecordService.findPageInfo(filter, page));
    }


    @Journal(name = "添加原料强制出库页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(ProductForceOutRecord productForceOutRecord) {
        return new ModelAndView(addOrEdit, model.addAttribute("productForceOutRecord", productForceOutRecord));
    }

    @ResponseBody
    @Journal(name = "保存原料强制出库", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(ProductForceOutRecord productForceOutRecord) throws Exception {
        productForceOutRecordService.save(productForceOutRecord);
        return GsonTools.toJson(productForceOutRecord);
    }

    @Journal(name = "编辑原料强制出库页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(ProductForceOutRecord productForceOutRecord) {
        productForceOutRecord = productForceOutRecordService.findById(ProductForceOutRecord.class, productForceOutRecord.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("productForceOutRecord", productForceOutRecord));
    }

    @ResponseBody
    @Journal(name = "编辑原料强制出库", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(ProductForceOutRecord productForceOutRecord) throws Exception {
        productForceOutRecordService.update(productForceOutRecord);
        return GsonTools.toJson(productForceOutRecord);
    }

    @ResponseBody
    @Journal(name = "删除原料强制出库", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        productForceOutRecordService.delete(ProductForceOutRecord.class, ids);
        return Constant.AJAX_SUCCESS;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "导出成品强制出库", logType = LogType.DB)
    @RequestMapping(value = "exportExcel", method = RequestMethod.GET)
    public void export1(Filter filter) throws Exception {
        Page page = new Page();
        page.setAll(1);
        Map<String, Object> map = productForceOutRecordService.findPageInfo(filter, page);
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/productForceOutRecord.xlsx");
        Workbook wb = new SXSSFWorkbook(new XSSFWorkbook(is));
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        Sheet sheet = wb.getSheetAt(0);
        Row row;
        Cell cell;
        int i = 2;
        for (Map<String, Object> data : list) {
            row = sheet.createRow(i++);
            for (int j = 0; j < 20; j++) {
                cell = row.createCell(j);
                cell.setCellStyle(cellStyle);
                switch (j) {
                    case 0:
                        cell.setCellValue(data.get("BARCODE") == null ? "" : data.get("BARCODE").toString());
                        break;
                    case 1:
                        cell.setCellValue(data.get("SALESORDERCODE") == null ? "" : data.get("SALESORDERCODE").toString());
                        break;
                    case 2:
                        cell.setCellValue(data.get("CATEGORYNAME") == null ? "" : data.get("CATEGORYNAME").toString());
                        break;
                    case 3:
                        cell.setCellValue(data.get("CATEGORYCODE") == null ? " " : data.get("CATEGORYCODE").toString());
                        break;
                    case 4:
                        cell.setCellValue(data.get("SCRW") == null ? " " : data.get("SCRW").toString());
                        break;
                    case 5:
                        cell.setCellValue(data.get("FACTORYNAME") == null ? " " : data.get("FACTORYNAME").toString());
                        break;
                    case 6:
                        cell.setCellValue(data.get("CONSUMERPRODUCTNAME") == null ? " " : data.get("CONSUMERPRODUCTNAME").toString());
                        break;
                    case 7:
                        cell.setCellValue(data.get("TCPROCBOMVERSIONPARTSNAME") == null ? " " : data.get("TCPROCBOMVERSIONPARTSNAME").toString());
                        break;
                    case 8:
                        cell.setCellValue(data.get("PRODUCTMODEL") == null ? " " : data.get("PRODUCTMODEL").toString());
                        break;
                    case 9:
                        cell.setCellValue(data.get("BATCHCODE") == null ? " " : data.get("BATCHCODE").toString());
                        break;
                    case 10:
                        cell.setCellValue(data.get("CONSUMERNAME") == null ? " " : data.get("CONSUMERNAME").toString());
                        break;
                    case 11:
                        cell.setCellValue(data.get("PRODUCTLENGTH") == null ? " " : data.get("PRODUCTLENGTH").toString());
                        break;
                    case 12:
                        cell.setCellValue(data.get("PRODUCTWIDTH") == null ? " " : data.get("PRODUCTWIDTH").toString());
                        break;
                    case 13:
                        cell.setCellValue(data.get("PRODUCTWEIGHT") == null ? " " : data.get("PRODUCTWEIGHT").toString());
                        break;
                    case 14:
                        if (data.get("ISLOCKED") != null) {
                            if ("1".equals(data.get("ISLOCKED").toString())) {
                                cell.setCellValue("冻结");
                                break;
                            } else {
                                cell.setCellValue("正常");
                                break;
                            }
                        } else {
                            cell.setCellValue("");
                            break;
                        }

                    case 15:
                        cell.setCellValue(data.get("WAREHOUSENAME") == null ? "" : data.get("WAREHOUSENAME").toString());
                        break;
                    case 16:
                        cell.setCellValue(data.get("WAREHOUSEPOSCODE") == null ? "" : data.get("WAREHOUSEPOSCODE").toString());
                        break;
                    case 17:
                        cell.setCellValue(data.get("OUTADDRESS") == null ? "" : data.get("OUTADDRESS").toString());
                        break;
                    case 18:
                        cell.setCellValue(data.get("OUTUSER") == null ? "" : data.get("OUTUSER").toString());
                        break;
                    case 19:
                        cell.setCellValue(data.get("OUTTIME") == null ? "" : dateFormat.format((Date) data.get("OUTTIME")));
                        break;

                }
            }
        }
        HttpUtils.download(response, wb, "异常产品退回信息表");
        is.close();
    }
}