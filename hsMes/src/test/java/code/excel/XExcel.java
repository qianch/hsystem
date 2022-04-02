/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2017版权所有
 */
package code.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xdemo.superutil.j2se.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Goofy
 * @Date 2017年11月14日 下午3:44:27
 */
public class XExcel {
    public static void main(String[] args) throws IOException {
        String x = "D:\\x.xlsx";
        Workbook wb = new XSSFWorkbook(new FileInputStream(new File(x)));

        Sheet dir = wb.getSheetAt(0);
        System.out.println(dir.getLastRowNum());
        System.out.println(dir.getPhysicalNumberOfRows());

        int i = 0;
        for (Iterator<Row> it = dir.rowIterator(); it.hasNext(); ) {
            it.next();
            System.out.println(++i);
        }
//
//		List<String[]> list = readCodeAndVersion(dir);
//
//		for (String[] sa : list) {
//			// 判断版本是否存在
//			System.out.println(GsonTools.toJson(sa));
//			System.out.println(GsonTools.toJson(readPackMaterial(wb.getSheet(sa[1]))));
//			System.out.println(GsonTools.toJson(readPackRequirement(wb.getSheet(sa[1]))));
//			
//		}

    }

    public static List<String> readPackRequirement(Sheet sheet) {

        List<String> ret = new ArrayList<String>();

        Row row;
        Cell cell;

        int rowIndex = 0;

        for (Iterator<Row> it = sheet.rowIterator(); it.hasNext(); ) {
            rowIndex++;
            row = it.next();
            cell = row.getCell(0);
            if (cell == null)
                continue;
            if (StringUtils.trimAll(readValue(cell)).equals("包装要求")) {
                break;
            }
        }


        for (int i = rowIndex; i < rowIndex + 9; i++) {
            row = sheet.getRow(i);

            if (row == null) {
                break;
            }

            if (row.getCell(0) == null) {
                break;
            } else {
                if (StringUtils.trimAll(readValue(row.getCell(0))).equals("包装示意图")) {
                    break;
                }
            }
            cell = row.getCell(1);

            ret.add(readValue(cell));
        }

        return ret;

    }

    public static List<String[]> readPackMaterial(Sheet sheet) {

        List<String[]> ret = new ArrayList<String[]>();

        Row row;
        Cell cell;

        int rowIndex = 0;

        for (Iterator<Row> it = sheet.rowIterator(); it.hasNext(); ) {
            rowIndex++;
            row = it.next();
            cell = row.getCell(0);
            if (cell == null)
                continue;
            if (StringUtils.trimAll(readValue(cell)).equals("包装材料")) {
                break;
            }
        }

        String[] unit = null;

        int[] indexes = {0, 1, 2, 4, 6, 7, 8};

        for (; ; ) {
            rowIndex++;
            row = sheet.getRow(rowIndex);

            if (row == null) {
                break;
            }

            if (row.getCell(0) == null) {
                break;
            } else {
                if (StringUtils.trimAll(readValue(row.getCell(0))).equals("包装要求")) {
                    break;
                }
            }
            int j = 0;
            unit = new String[7];
            for (int i : indexes) {
                unit[j++] = readValue(row.getCell(i));
            }
            ret.add(unit);
        }

        return ret;

    }

    public static List<String[]> readCodeAndVersion(Sheet sheet) {

        List<String[]> ret = new ArrayList<String[]>();

        Row row;
        Cell cell;

        int rowIndex = 0;

        for (Iterator<Row> it = sheet.rowIterator(); it.hasNext(); ) {
            rowIndex++;
            row = it.next();
            cell = row.getCell(0);
            if (cell == null)
                continue;
            if (StringUtils.trimAll(readValue(cell)).equals("文件说明")) {
                break;
            }
        }

        int codePackCategory = 13;
        int codeColumnIndex = 16;
        int codeVersionIndex = 18;

        String category, code, ver;
        String[] unit;
        for (; ; ) {

            unit = new String[3];

            rowIndex++;
            row = sheet.getRow(rowIndex);
            if (row == null)
                break;

            cell = row.getCell(codePackCategory);
            if (cell == null)
                break;
            category = readValue(cell);
            if (StringUtils.isBlank(category))
                break;
            unit[0] = category;

            cell = row.getCell(codeColumnIndex);
            if (cell == null)
                break;
            code = readValue(cell);
            if (StringUtils.isBlank(code))
                break;
            unit[1] = code;

            cell = row.getCell(codeVersionIndex);
            ver = readValue(cell);
            if (StringUtils.isBlank(ver))
                break;
            unit[2] = ver;

            ret.add(unit);
        }

        return ret;

    }

    public static String readValue(Cell cell) {
        String rs = "";
        switch (cell.getCellType()) {
            case BOOLEAN:
                rs = cell.getBooleanCellValue() + "";
                break;
            case NUMERIC:
                rs = cell.getNumericCellValue() + "";
                if (DateUtil.isCellDateFormatted(cell)) {
                    rs = DateUtil.getJavaDate(cell.getNumericCellValue()).toLocaleString();
                }
                break;
            case STRING:
                rs = cell.getStringCellValue();
                break;
            case ERROR:
                rs = cell.getErrorCellValue() + "";
                break;
            case BLANK:
                rs = "";
                break;
            case FORMULA:
                rs = cell.getCellFormula() + "";
                break;
            default:
                rs = "";
                break;
        }
        return rs;
    }

    public static void dir(Sheet dir) {

    }
}
