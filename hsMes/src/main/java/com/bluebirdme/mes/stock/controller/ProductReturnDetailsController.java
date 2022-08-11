package com.bluebirdme.mes.stock.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.stock.service.IProductReturnDetailService;
import com.bluebirdme.mes.utils.HttpUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/productReturnDetails")
@Journal(name = "成品回库名细")
public class ProductReturnDetailsController extends BaseController {
    @Resource
    IProductReturnDetailService productReturnDetailService;

    /**
     * 成品回库明细页面
     */
    final String index = "stock/productReturnDetails";

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取原料强制出库列表信息")
    @RequestMapping("list")
    public String getProductReturnDetails(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(productReturnDetailService.findPageInfo(filter, page));
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "产品回库明细导出", logType = LogType.DB)
    @RequestMapping(value = "exportExcel", method = RequestMethod.GET)
    public void export(Filter filter) throws Exception {
        Page page = new Page();
        page.setAll(1);
        Map<String, Object> map = productReturnDetailService.findPageInfo(filter, page);
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/productReturnDetail.xlsx");
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
            for (int j = 0; j < 19; j++) {
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
                        cell.setCellValue(data.get("NEWWAREHOUSEPOSCODE") == null ? "" : data.get("NEWWAREHOUSEPOSCODE").toString());
                        break;
                    case 17:
                        cell.setCellValue(data.get("USERNAME") == null ? "" : data.get("USERNAME").toString());
                        break;
                    case 18:
                        cell.setCellValue(data.get("INTIME") == null ? "" : dateFormat.format((Date) data.get("INTIME")));
                        break;

                }
            }
        }
        HttpUtils.download(response, wb, "产品回库信息表");
        is.close();
    }
}