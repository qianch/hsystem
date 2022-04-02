package com.bluebirdme.mes.core.excel;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public abstract class ExcelImportHandler {
    public ExcelImportHandler() {
    }

    public abstract ExcelImportMessage check(ExcelContent content);

    public abstract ExcelImportMessage toDB(ExcelContent content);

    public ExcelContent getContent(String path) throws IOException {
        List<String> titles = new ArrayList();
        List<String[]> data = new ArrayList();
        InputStream is = new FileInputStream(path);
        Workbook wb = new XSSFWorkbook(is);
        Sheet sheet = wb.getSheetAt(0);
        Row title = sheet.getRow(0);

        int rowIndex;
        for (rowIndex = 0; rowIndex < title.getPhysicalNumberOfCells(); ++rowIndex) {
            titles.add(title.getCell(rowIndex).getStringCellValue());
        }

        rowIndex = 0;
        int columns = titles.size();
        Cell cell;
        Row row;
        Iterator iterator = sheet.rowIterator();

        while (iterator.hasNext()) {
            row = (Row) iterator.next();
            if (rowIndex++ != 0) {
                if (row == null) {
                    break;
                }

                String[] rowData = new String[columns];
                Object value;

                for (int i = 0; i < columns; ++i) {
                    cell = row.getCell(i);
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case NUMERIC:
                                value = cell.getNumericCellValue();
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    value = DateUtil.getJavaDate(cell.getNumericCellValue());
                                }
                                break;
                            case STRING:
                                value = cell.getStringCellValue();
                                break;
                            case FORMULA:
                                value = cell.getCellFormula();
                                break;
                            case BOOLEAN:
                                value = cell.getBooleanCellValue();
                                break;
                            case ERROR:
                                value = cell.getErrorCellValue();
                                break;
                            default:
                                value = null;
                        }

                        if (value != null) {
                            rowData[i] = value.toString();
                        } else {
                            rowData[i] = null;
                        }
                    } else {
                        rowData[i] = null;
                    }
                }
                data.add(rowData);
            }
        }
        return new ExcelContent(titles, data);
    }
}
