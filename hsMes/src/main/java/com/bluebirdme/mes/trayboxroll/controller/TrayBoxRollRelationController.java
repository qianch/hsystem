package com.bluebirdme.mes.trayboxroll.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.trayboxroll.service.TrayBoxRollRelationService;
import com.bluebirdme.mes.utils.HttpUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 托盒卷对应关系
 *
 * @author Hengdong Zhang
 */
@Controller
@RequestMapping("/trayBoxRollRelation")
@Journal(name = "托盒卷对应关系")
public class TrayBoxRollRelationController extends BaseController {
    /**
     * 产成品汇总(成品类别)页面
     */
    final String trayBoxRollRelationSummaryView = "trayBoxRollRelation/trayBoxRollRelationSummary";

    @Resource
    TrayBoxRollRelationService trayBoxRollRelationService;

    /**
     * 托盒卷对应关系
     */
    @Journal(name = "托盒卷对应关系")
    @RequestMapping(value = "trayBoxRollRelationSummaryView", method = RequestMethod.GET)
    public String trayBoxRollRelationSummaryView() {
        return trayBoxRollRelationSummaryView;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取托盒卷对应关系列表信息")
    @RequestMapping("list")
    public String getTrayBoxRollRelation(Filter filter, Page page) throws Exception {
        Map<String, Object> pageInfo = trayBoxRollRelationService.findPageInfo(filter, page);
        Page rollWeightPage = new Page();
        page.setAll(1);
        Map<String, Object> pageInfoRollWeight = trayBoxRollRelationService.findPageInfoRollWeight(filter, rollWeightPage);
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> rollWeightrows = (List<Map<String, Object>>) pageInfoRollWeight.get("rows");
        List<Map<String, Object>> allRows = (List<Map<String, Object>>) pageInfo.get("rows");
        BigDecimal totalRollWeight = new BigDecimal(0);
        if (rollWeightrows.size() > 0) {
            totalRollWeight = BigDecimal.valueOf(Double.parseDouble(rollWeightrows.get(0).get("TOTALROLLWEIGHT").toString().replace(",", "")));
        }
        BigDecimal count = new BigDecimal(0);
        for (Map<String, Object> oneRecord : allRows) {
            count = count.add(BigDecimal.valueOf((Double) oneRecord.get("ROLLWEIGHT")));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("ROLLBARCODE", "当前页总计：");
        map.put("ROLLWEIGHT", count.setScale(2, RoundingMode.HALF_UP) + "kg");
        list.add(map);
        map = new HashMap<>();
        map.put("ROLLBARCODE", "当搜索条件总计：");
        map.put("ROLLWEIGHT", totalRollWeight.setScale(2, RoundingMode.HALF_UP) + "kg");
        list.add(map);
        pageInfo.put("footer", list);
        return GsonTools.toJson(pageInfo);
    }

    /**
     * 托盒卷对应关系导出到Excel
     */
    @Journal(name = "导出Excel")
    @ResponseBody
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void export(Filter filter) throws Exception {
        Page page = new Page();
        page.setAll(1);

        String name = filter.getFilter().get("name");
        String ids = filter.getFilter().get("d1.id");
        if (ids != null && ids != "") {
            filter.getFilter().put("d1.id", "in:" + ids);
        }
        if (name != null && name != "") {
            filter.getFilter().put("name", "like:" + name);
        }

        Map<String, Object> map = trayBoxRollRelationService.findPageInfo(filter, page);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
        InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/trayBoxRollRelation.xlsx");
        Workbook wb = new SXSSFWorkbook(new XSSFWorkbook(is));
        Sheet sheet = wb.getSheetAt(0);
        Row row;
        Cell cell;

        int i = 1;
        for (Map<String, Object> data : list) {
            row = sheet.createRow(i++);
            for (int j = 0; j < 16; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        if (data.get("ROLLBARCODE") != null && !"".equals(data.get("ROLLBARCODE"))) {
                            cell.setCellValue(data.get("ROLLBARCODE") == null ? "" : data.get("ROLLBARCODE").toString());//卷条码
                        }
                        break;
                    case 1:
                        if (data.get("BOXBARCODE") != null && !"".equals(data.get("BOXBARCODE"))) {
                            cell.setCellValue(data.get("BOXBARCODE") == null ? "" : data.get("BOXBARCODE").toString());//盒条码
                        }
                        break;
                    case 2:
                        if (data.get("TRAYBARCODE") != null && !"".equals(data.get("TRAYBARCODE"))) {
                            cell.setCellValue(data.get("TRAYBARCODE") == null ? "" : data.get("TRAYBARCODE").toString());//托条码
                            // sheet.addMergedRegion(new CellRangeAddress(55, 56, 2, 2));
                        }
                        break;
                    case 3:
                        if (data.get("PRODUCEPLANCODE") != null && !"".equals(data.get("PRODUCEPLANCODE"))) {
                            cell.setCellValue(data.get("PRODUCEPLANCODE") == null ? "" : data.get("PRODUCEPLANCODE").toString());//计划单号
                        }
                        break;
                    case 4:
                        if (data.get("SALESORDERCODE") != null && !"".equals(data.get("SALESORDERCODE"))) {
                            cell.setCellValue(data.get("SALESORDERCODE") == null ? "" : data.get("SALESORDERCODE").toString());//订单号
                        }
                        break;
                    case 5:
                        if (data.get("CONSUMERNAME") != null && !"".equals(data.get("CONSUMERNAME"))) {
                            cell.setCellValue(data.get("CONSUMERNAME") == null ? "" : data.get("CONSUMERNAME").toString());//客户名称
                        }
                        break;
                    case 6:
                        if (data.get("PRODUCTMODEL") != null && !"".equals(data.get("PRODUCTMODEL"))) {
                            cell.setCellValue(data.get("PRODUCTMODEL") == null ? "" : data.get("PRODUCTMODEL").toString());//产品规格
                        }
                        break;
                    case 7:
                        if (data.get("BATCHCODE") != null && !"".equals(data.get("BATCHCODE"))) {
                            cell.setCellValue(data.get("BATCHCODE") == null ? "" : data.get("BATCHCODE").toString());//批次号
                        }
                        break;
                    case 8:
                        if (data.get("ROLLQUALITYGRADECODE") != null && !"".equals(data.get("ROLLQUALITYGRADECODE"))) {
                            cell.setCellValue(data.get("ROLLQUALITYGRADECODE") == null ? "" : data.get("ROLLQUALITYGRADECODE").toString());//质量等级
                        }
                        break;
                    case 9:
                        if (data.get("ROLLDEVICECODE") != null && !"".equals(data.get("ROLLDEVICECODE"))) {
                            cell.setCellValue(data.get("ROLLDEVICECODE") == null ? "" : data.get("ROLLDEVICECODE").toString());//机台号
                        }
                        break;
                    case 10:
                        if (data.get("NAME") != null && !"".equals(data.get("NAME"))) {
                            cell.setCellValue(data.get("NAME") == null ? "" : data.get("NAME").toString());//车间
                        }
                        break;
                    case 11:
                        if (data.get("ROLLWEIGHT") != null && !"".equals(data.get("ROLLWEIGHT"))) {
                            cell.setCellValue(data.get("ROLLWEIGHT") == null ? "" : data.get("ROLLWEIGHT").toString());//卷重(kg)
                        }
                        break;
                    case 12:
                        if (data.get("PRODUCTWIDTH") != null && !"".equals(data.get("PRODUCTWIDTH"))) {
                            cell.setCellValue(data.get("PRODUCTWIDTH") == null ? "" : data.get("PRODUCTWIDTH").toString());//门幅(mm)
                        }
                        break;
                    case 13:
                        if (data.get("PRODUCTWEIGHT") != null && !"".equals(data.get("PRODUCTWEIGHT"))) {
                            cell.setCellValue(data.get("PRODUCTWEIGHT") == null ? "" : data.get("PRODUCTWEIGHT").toString());//实际重量（kg）
                        }
                        break;
                    case 14:
                        if (data.get("PRODUCTLENGTH") != null && !"".equals(data.get("PRODUCTLENGTH"))) {
                            cell.setCellValue(data.get("PRODUCTLENGTH") == null ? "" : data.get("PRODUCTLENGTH").toString());//卷长(m)
                        }
                        break;
                    case 15:
                        if (data.get("ROLLOUTPUTTIME") != null && !"".equals(data.get("ROLLOUTPUTTIME"))) {
                            cell.setCellValue(data.get("ROLLOUTPUTTIME") == null ? "" : data.get("ROLLOUTPUTTIME").toString());//生产时间
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        row = sheet.createRow(i++);
        Cell cell_0 = row.createCell(0);
        cell_0.setCellValue("合计");
        Map<String, Object> pageInfoRollWeight = trayBoxRollRelationService.findPageInfoRollWeight(filter, page);
        List<Map<String, Object>> rows = (List<Map<String, Object>>) pageInfoRollWeight.get("rows");
        Cell cell_11 = row.createCell(11);
        cell_11.setCellValue(rows.get(0).get("TOTALROLLWEIGHT").toString());
        HttpUtils.download(response, wb, "托盒卷对应关系");
        is.close();
    }
}
