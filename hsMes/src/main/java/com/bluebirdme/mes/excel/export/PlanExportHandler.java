package com.bluebirdme.mes.excel.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdemo.superutil.j2se.PathUtils;

import com.bluebirdme.mes.complaint.service.IComplaintService;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.excel.ExcelContent;
import com.bluebirdme.mes.core.excel.ExcelExportHandler;
import com.bluebirdme.mes.core.spring.SpringCtx;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;

/**
 * 投诉编制计划功能
 *
 * @author Goofy
 * @Date 2016年11月26日 下午12:43:28
 */
public class PlanExportHandler extends ExcelExportHandler {
    private static Logger logger = LoggerFactory.getLogger(PlanExportHandler.class);

    @Override
    public ExcelContent getContent() {
        return null;
    }

    @Override
    public ExcelContent getContent(String[] id) {
        return null;
    }

    @Override
    public ExcelContent getContent(Filter filter) {
        ExcelContent content = new ExcelContent();
        InputStream is = null;
        try {
            is = new FileInputStream(new File(PathUtils.getClassPath() + "excel/电子下单161209~BZ3-47.xlsx"));
        } catch (FileNotFoundException e1) {
            logger.error(e1.getLocalizedMessage(), e1);
        }

        Workbook wb = null;
        try {
            wb = new XSSFWorkbook(is);
        } catch (IOException e1) {
            logger.error(e1.getLocalizedMessage(), e1);
        }
        Sheet sheet = wb.getSheetAt(0);
        Workbook wbwrite = null;
        Sheet sheetWrite = wbwrite.createSheet();
        for (int a = 0; a < 4; a++) {
            Row writeRow = sheetWrite.createRow(a);
            Row row = sheet.getRow(a);
            for (int b = 0; b <= row.getLastCellNum(); b++) {
                Cell writecell = writeRow.createCell(b);
                Cell cell = row.getCell(b);
                writecell.setCellStyle(cell.getCellStyle());
                switch (cell.getCellType()) {
                    case NUMERIC:
                        writecell.setCellValue(cell.getNumericCellValue());
                        break;
                    case STRING:
                        writecell.setCellValue(cell.getStringCellValue());
                        break;
                }
            }
        }
        IWeavePlanService planService = (IWeavePlanService) SpringCtx.getBean("weavePlanServiceImpl");
        Page page = new Page();
        page.setAll(1);
        List<Map<String, Object>> rs = null;
        try {
            rs = (List<Map<String, Object>>) planService.findPageInfo(filter, page).get("rows");
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        for (int i = 0; i < rs.size(); i++) {
            Row writeRow = sheetWrite.createRow(i + 4);
            for (int a = 0; a < 14; a++) {
                Cell cell = writeRow.createCell(a);
                switch (a) {
                    case 0:
                        cell.setCellValue(rs.get(a).get("consumerName".toUpperCase()).toString());
                        break;
                    case 1:
                        cell.setCellValue(rs.get(a).get("salesOrderCode".toUpperCase()).toString());
                        break;
                    case 2:
                        cell.setCellValue(rs.get(a).get("batchCode".toUpperCase()).toString());
                        break;
                    case 3:
                        cell.setCellValue(rs.get(a).get("productModel".toUpperCase()).toString());
                        break;
                    case 4:
                        cell.setCellValue(rs.get(a).get("productName".toUpperCase()).toString());
                        break;
                    case 5:
                        cell.setCellValue(rs.get(a).get("productWidth".toUpperCase()).toString());
                        break;
                    case 6:
                        cell.setCellValue(rs.get(a).get("orderCount".toUpperCase()).toString());
                        break;
                    case 7:
                        cell.setCellValue(rs.get(a).get("bcBomCode".toUpperCase()).toString());
                        break;
                    case 8:
                        cell.setCellValue(rs.get(a).get("consumerName".toUpperCase()).toString());
                        break;
                    case 9:
                        cell.setCellValue(rs.get(a).get("consumerName".toUpperCase()).toString());
                        break;
                    case 10:
                        cell.setCellValue(rs.get(a).get("consumerName".toUpperCase()).toString());
                        break;
                    case 11:
                        cell.setCellValue(rs.get(a).get("consumerName".toUpperCase()).toString());
                        break;
                    case 12:
                        cell.setCellValue(rs.get(a).get("consumerName".toUpperCase()).toString());
                        break;
                }
            }
        }
        return content;
    }
}
