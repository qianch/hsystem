package com.bluebirdme.mes.core.excel;

import com.bluebirdme.mes.core.base.entity.Filter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public abstract class ExcelExportHandler {
    public ExcelExportHandler() {
    }

    public abstract ExcelContent getContent();

    public abstract ExcelContent getContent(String[] var1);

    public abstract ExcelContent getContent(Filter var1);

    public Workbook getExcel(ExcelContent content) {
        Workbook wb = new SXSSFWorkbook();
        Sheet sheet;
        Row row;
        Cell cell;
        sheet = wb.createSheet();
        row = sheet.createRow(0);
        CellStyle cs = wb.createCellStyle();
        cs.setDataFormat(wb.createDataFormat().getFormat("@"));

        int i;
        for (i = 0; i < content.getTitles().size(); ++i) {
            sheet.setDefaultColumnStyle(i, cs);
            cell = row.createCell(i);
            cell.setCellValue(content.getTitles().get(i));
        }

        for (i = 0; i < content.getData().size(); ++i) {
            row = sheet.createRow(i + 1);

            for (int j = 0; j < content.getData().get(0).length; ++j) {
                cell = row.createCell(j);
                cell.setCellStyle(cs);
                cell.setCellValue(content.getData().get(i)[j] == null ? "" : content.getData().get(i)[j]);
            }
        }

        return wb;
    }
}
