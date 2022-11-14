/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.mobile.produce.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.mobile.produce.dao.IFeedingRecordDao;
import com.bluebirdme.mes.mobile.produce.entity.FeedingRecord;
import com.bluebirdme.mes.mobile.produce.service.IFeedingRecordService;
import com.bluebirdme.mes.mobile.stock.controller.MobileMaterialController;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;
import com.bluebirdme.mes.printer.entity.MyException;
import com.bluebirdme.mes.stock.entity.ProductStockState;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-8 14:03:33
 */
@Service
@AnyExceptionRollback
public class FeedingRecordServiceImpl extends BaseServiceImpl implements IFeedingRecordService {
    @Resource
    IFeedingRecordDao feedingRecordDao;

    @Override
    protected IBaseDao getBaseDao() {
        return feedingRecordDao;
    }


    @Override
    public <T> Map<String, Object> findPageInfo2(Filter filter, Page page) throws Exception {
        return feedingRecordDao.findPageInfo2(filter, page);
    }

    public void add(FeedingRecord feedingRecord) throws MyException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (feedingRecord.getMaterialCode() != null && feedingRecord.getMaterialCode().length() > 20) {
            feedingRecord.setMaterialCode(MobileMaterialController.parseMaterialCode(feedingRecord.getMaterialCode()));
            map.put("materialCode", feedingRecord.getMaterialCode());
            if (isExist(FeedingRecord.class, map)) {
                throw new MyException("该条码已投料，请勿重复投料！");
            }
        } else {
            if (feedingRecord.getRollCode().startsWith("RBZ")) {
                map.clear();
                map.put("barCode", feedingRecord.getMaterialCode());
                ProductStockState pss = findUniqueByMap(ProductStockState.class, map);
                pss.setStockState(-1);
                update(pss);
            }
        }
        save(feedingRecord);
    }

    //裁剪投料导出
    @Resource
    IWeavePlanService weavePlanService;

    @Override
    public SXSSFWorkbook getForceMaterialExport1(Filter filter) {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setBold(true); // 字体增粗
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);// 单元格自动换行
        cellStyle.setFont(font);

        CellStyle cellStyle0 = wb.createCellStyle();
        cellStyle0.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle0.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle0.setBorderBottom(BorderStyle.THIN);
        cellStyle0.setBorderTop(BorderStyle.THIN);
        cellStyle0.setBorderRight(BorderStyle.THIN);
        cellStyle0.setBorderLeft(BorderStyle.THIN);
        cellStyle0.setWrapText(true);// 单元格自动换行

        Font font1 = wb.createFont();
        font1.setFontHeightInPoints((short) 10);
        font1.setBold(true); // 字体增粗
        CellStyle cellStyle3 = wb.createCellStyle();
        cellStyle3.setAlignment(HorizontalAlignment.LEFT); // 左对齐
        cellStyle3.setBorderBottom(BorderStyle.THIN);
        cellStyle3.setBorderTop(BorderStyle.THIN);
        cellStyle3.setBorderRight(BorderStyle.THIN);
        cellStyle3.setBorderLeft(BorderStyle.THIN);
        cellStyle3.setFont(font1);
        Sheet sheet = wb.createSheet();

        Row row;
        Cell cell;
        Page page = new Page();
        page.setRows(99999);
        Map<String, Object> materialMap = weavePlanService.findWeavePageInfo2(filter, page);
        List<Map<String, Object>> list = (List) materialMap.get("rows");

        String[] columnName = new String[]{"机台号", "胚布条码", "投料日期", "操作人", "卷重(Kg)"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司裁剪投料记录表");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 5; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
        r++;

        row = sheet.createRow(r);
        sheet.setColumnWidth(0, 8 * 256);// 设置列宽
        sheet.setColumnWidth(1, 22 * 256);
        sheet.setColumnWidth(2, 22 * 256);
        sheet.setColumnWidth(3, 8 * 256);
        sheet.setColumnWidth(4, 10 * 256);
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
                        //机台号
                        if (data.get("DEVICECODE") != null) {
                            cell.setCellValue(data.get("DEVICECODE").toString());
                        }
                        break;
                    case 1:
                        //卷条码
                        if (data.get("ROLLCODE") != null) {
                            cell.setCellValue(data.get("ROLLCODE").toString());
                        }
                        break;
                    case 2:
                        //投料日期
                        if (data.get("FEEDINGDATE") != null) {
                            cell.setCellValue(data.get("FEEDINGDATE").toString());
                        }
                        break;

                    case 3:
                        //操作人
                        if (data.get("USERNAME") != null) {
                            cell.setCellValue(data.get("USERNAME").toString());
                        }
                        break;

                    case 4:
                        //卷重
                        if (data.get("ROLLWEIGHT") != null) {
                            cell.setCellValue(data.get("ROLLWEIGHT").toString());
                        }
                        break;
                }
            }
            r++;
        }
        return wb;
    }

    //编织投料导出
    @Override
    public SXSSFWorkbook getweaveExport1(Filter filter) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        SXSSFWorkbook wb = new SXSSFWorkbook();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setBold(true); // 字体增粗
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);// 单元格自动换行
        cellStyle.setFont(font);

        CellStyle cellStyle0 = wb.createCellStyle();
        cellStyle0.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle0.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle0.setBorderBottom(BorderStyle.THIN);
        cellStyle0.setBorderTop(BorderStyle.THIN);
        cellStyle0.setBorderRight(BorderStyle.THIN);
        cellStyle0.setBorderLeft(BorderStyle.THIN);
        cellStyle0.setWrapText(true);// 单元格自动换行

        Font font1 = wb.createFont();
        font1.setFontHeightInPoints((short) 10);
        font1.setBold(true); // 字体增粗
        CellStyle cellStyle3 = wb.createCellStyle();
        cellStyle3.setAlignment(HorizontalAlignment.LEFT); // 左对齐
        cellStyle3.setBorderBottom(BorderStyle.THIN);
        cellStyle3.setBorderTop(BorderStyle.THIN);
        cellStyle3.setBorderRight(BorderStyle.THIN);
        cellStyle3.setBorderLeft(BorderStyle.THIN);
        cellStyle3.setFont(font1);
        Sheet sheet = wb.createSheet();

        Row row;
        Cell cell;
        Page page = new Page();
        page.setRows(99999);
        Map<String, Object> materialMap = feedingRecordDao.findPageInfo(filter, page);
        List<Map<String, Object>> list = (List) materialMap.get("rows");

        String columnName[] = new String[]{"原料条码", "原料型号", "机台", "操作人", "投料日期", "重量(KG)"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司编织投料记录表");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 6; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        r++;
        row = sheet.createRow(r);
        // 设置列宽
        sheet.setColumnWidth(0, 18 * 256);
        sheet.setColumnWidth(1, 18 * 256);
        sheet.setColumnWidth(2, 6 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 18 * 256);
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
                        //原料条码
                        if (data.get("MATERIALCODE") != null) {
                            cell.setCellValue(data.get("MATERIALCODE").toString());
                        }
                        break;
                    case 1:
                        //原料型号
                        if (data.get("MATERIALMODEL") != null) {
                            cell.setCellValue(data.get("MATERIALMODEL").toString());
                        }
                        break;

                    case 2:
                        //机台
                        if (data.get("DEVICECODE") != null) {
                            cell.setCellValue(data.get("DEVICECODE").toString());
                        }
                        break;
                    case 3:
                        //操作人
                        if (data.get("USERNAME") != null) {
                            cell.setCellValue(data.get("USERNAME").toString());
                        }
                        break;
                    case 4:
                        //投料日期
                        if (data.get("FEEDINGDATE") != null) {
                            cell.setCellValue(sf.format(data.get("FEEDINGDATE")));
                        }
                        break;
                    case 5:
                        //重量
                        if (data.get("WEIGHT") != null) {
                            cell.setCellValue(data.get("WEIGHT").toString());
                        }
                        break;
                }
            }
            r++;
        }
        return wb;
    }
}
