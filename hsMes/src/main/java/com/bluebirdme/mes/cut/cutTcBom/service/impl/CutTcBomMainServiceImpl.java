/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.cut.cutTcBom.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.cut.cutTcBom.dao.ICutTcBomMainDao;
import com.bluebirdme.mes.cut.cutTcBom.entity.CutTcBomDetail;
import com.bluebirdme.mes.cut.cutTcBom.entity.CutTcBomMain;
import com.bluebirdme.mes.cut.cutTcBom.service.ICutTcBomMainService;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.utils.ImportExcel;
import com.bluebirdme.mes.utils.State;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xdemo.superutil.j2se.MapUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 徐波
 * @Date 2016-11-2 9:30:07
 */
@Service
@AnyExceptionRollback
public class CutTcBomMainServiceImpl extends BaseServiceImpl implements ICutTcBomMainService {

    @Resource
    ICutTcBomMainDao cutTcBomMainDao;

    @Override
    protected IBaseDao getBaseDao() {
        return cutTcBomMainDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {

        Map<String, Object> findPageInfo = cutTcBomMainDao.findPageInfo(filter, page);
        return findPageInfo;
    }

    @Override
    public List<Map<String, Object>> findCutTcBomDetailByMainId(Long mainId) {
        return cutTcBomMainDao.findCutTcBomDetailByMainId(mainId);
    }

    @Override
    public String saveCutTcBomMain(CutTcBomMain cutTcBomMain, String userId) throws Exception {

        String result = "";
        cutTcBomMain.setState(State.Effect);
        if (cutTcBomMain.getId() != null) {
            cutTcBomMain.setModifyTime(new Date());
            cutTcBomMain.setModifyUser(userId);
            cutTcBomMainDao.update(cutTcBomMain);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("mainId", cutTcBomMain.getId());
            delete(CutTcBomDetail.class, map);
        } else {
            cutTcBomMain.setCreater(userId);
            cutTcBomMain.setCreateTime(new Date());
            cutTcBomMain.setModifyTime(new Date());
            cutTcBomMain.setModifyUser(userId);
            cutTcBomMainDao.save(cutTcBomMain);
        }

        List<CutTcBomDetail> listCutTcBomDetailsave = new ArrayList<CutTcBomDetail>();
        for (CutTcBomDetail cutTcBomDetail : cutTcBomMain.getListCutTcBomDetail()) {
            cutTcBomDetail.setMainId(cutTcBomMain.getId());
            cutTcBomDetail.setCreateTime(new Date());
            cutTcBomDetail.setCreater(userId);
            cutTcBomDetail.setModifyTime(new Date());
            cutTcBomDetail.setModifyUser(userId);
            listCutTcBomDetailsave.add(cutTcBomDetail);
        }

        cutTcBomMainDao.save(listCutTcBomDetailsave.toArray(new CutTcBomDetail[]{}));

        return result;
    }

    @Override
    public String importCutTcBomMainUploadFile(MultipartFile file, String userId) throws Exception {
        String jsonData = "";

        Boolean bool = ImportExcel.checkFile(file);
        if (!bool) {
            return "文件类型不正确或为空";
        }

        //工具类在下面
        HashMap<String, ArrayList<String[]>> hashMap = ImportExcel.analysisFile(file);
        ArrayList<String[]> arrayList = hashMap.get("OK");
        if (arrayList == null) {
            return "文件内容为空";
        }

        CutTcBomMain cutTcBomMain;
        String tcProcBomCodeVersion = "";
        if (arrayList.size() > 0) {
            if (arrayList.get(0).length < 6) {
                return "文件格式";

            }

            String tcProcBomCodeVersionText = arrayList.get(0)[0];
            tcProcBomCodeVersion = arrayList.get(0)[1];

            String bladeTypeName = arrayList.get(0)[5];
            String customerName = arrayList.get(0)[3];

            if (!tcProcBomCodeVersionText.equals("版本")) {
                return "文件格式不对，请检查第一个单元格是否为版本!";
            }

            Consumer consumer = findOne(Consumer.class, "consumerName", customerName);
            if (consumer == null) {
                return "客户名称：" + customerName + " 在客户表中不存在，请核对!";
            }

            cutTcBomMain = findOne(CutTcBomMain.class, "tcProcBomCodeVersion", tcProcBomCodeVersion);
            if (cutTcBomMain == null) {
                cutTcBomMain = new CutTcBomMain();
                cutTcBomMain.setCreater(userId);
                cutTcBomMain.setCreateTime(new Date());
            }
            cutTcBomMain.setTcProcBomCodeVersion(tcProcBomCodeVersion);
            cutTcBomMain.setState(State.Effect);
            cutTcBomMain.setBladeTypeName(bladeTypeName);
            cutTcBomMain.setCustomerCode(consumer.getConsumerCode());
            cutTcBomMain.setCustomerName(customerName);
            cutTcBomMain.setModifyTime(new Date());
            cutTcBomMain.setModifyUser(userId);

            if (cutTcBomMain.getId() == null) {
                save(cutTcBomMain);
            } else {
                update(cutTcBomMain);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("mainId", cutTcBomMain.getId());
                delete(CutTcBomDetail.class, map);
            }

        }

        List<CutTcBomDetail> listCutTcBomDetail = new ArrayList<>();
        cutTcBomMain = findOne(CutTcBomMain.class, "tcProcBomCodeVersion", tcProcBomCodeVersion);
        for (int i = 0; i < arrayList.size(); i++) {
            if (i <= 1) continue;
            String partName = arrayList.get(i)[0];
            String drawName = arrayList.get(i)[1];
            String orientation = arrayList.get(i)[2];
            String productModel = arrayList.get(i)[3];
            String productWidth = arrayList.get(i)[4];
            String length = arrayList.get(i)[5];
            String gramWeight = arrayList.get(i)[6];
            String productionRate = arrayList.get(i)[7];
            String unitPrice = arrayList.get(i)[8];
//            String upperSizeLimit = arrayList.get(i)[8];
//            String lowerSizeLimit = arrayList.get(i)[9];
//            String sizePercentage = arrayList.get(i)[10];
//            String sizeAbsoluteValue = arrayList.get(i)[11];

            CutTcBomDetail cutTcBomDetail = new CutTcBomDetail();
            cutTcBomDetail.setCreateTime(new Date());
            cutTcBomDetail.setCreater(userId);
            cutTcBomDetail.setModifyTime(new Date());
            cutTcBomDetail.setModifyUser(userId);
            cutTcBomDetail.setPartName(partName);
            cutTcBomDetail.setMainId(cutTcBomMain.getId());
            cutTcBomDetail.setDrawName(drawName);
            cutTcBomDetail.setOrientation(orientation);
            cutTcBomDetail.setProductModel(productModel);
            cutTcBomDetail.setProductWidth(Double.parseDouble(productWidth));
            cutTcBomDetail.setLength(Double.parseDouble(length));
            cutTcBomDetail.setGramWeight(Double.parseDouble(gramWeight));
            cutTcBomDetail.setProductionRate(Double.parseDouble(productionRate));
            cutTcBomDetail.setUnitPrice(Double.parseDouble(unitPrice));
//            cutTcBomDetail.setUpperSizeLimit(Double.parseDouble(upperSizeLimit));
//            cutTcBomDetail.setLowerSizeLimit(Double.parseDouble(lowerSizeLimit));
//            cutTcBomDetail.setSizePercentage(Double.parseDouble(sizePercentage));
//            cutTcBomDetail.setSizeAbsoluteValue(Double.parseDouble(sizeAbsoluteValue));
            listCutTcBomDetail.add(cutTcBomDetail);
        }
        saveList(listCutTcBomDetail);
        return "";
    }

    @Override
    public SXSSFWorkbook exportcutTcBomMain(Long id) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        String templateName = "裁剪套才bom";
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        SXSSFWorkbook wb = new SXSSFWorkbook();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 18);
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


        Row row = null;
        Cell cell = null;

        CutTcBomMain cutTcBomMain = findById(CutTcBomMain.class, id);
        List<CutTcBomDetail> listCutTcBomDetail = find(CutTcBomDetail.class, "mainId", cutTcBomMain.getId());

        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        sheet.setColumnWidth(0, 13 * 256);// 设置列宽
        cell.setCellStyle(cellStyle);
        cell.setCellValue("版本");

        cell = row.createCell(1);
        sheet.setColumnWidth(1, 26 * 256);// 设置列宽
        cell.setCellStyle(cellStyle);
        cell.setCellValue(cutTcBomMain.getTcProcBomCodeVersion());

        cell = row.createCell(2);
        sheet.setColumnWidth(2, 13 * 256);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("客户");

        cell = row.createCell(3);
        sheet.setColumnWidth(3, 26 * 256);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(cutTcBomMain.getCustomerName());

        cell = row.createCell(4);
        sheet.setColumnWidth(4, 22 * 256);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("叶型");

        cell = row.createCell(5);
        sheet.setColumnWidth(5, 16 * 256);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(cutTcBomMain.getBladeTypeName());

        // sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 10));

        r++;
        String cellName[] = new String[]{"部件", "图纸", "朝向", "规格", "门幅", "米长", "克重", "制成率", "单价"};
        row = sheet.createRow(r);
        for (int b = 0; b < cellName.length; b++) {
            Cell cell2 = row.createCell(b);
            cell2.setCellValue(cellName[b]);
            cell2.setCellStyle(cellStyle0);
        }

        for (CutTcBomDetail cutTcBomDetail : listCutTcBomDetail) {
            r++;
            row = sheet.createRow(r);

            for (int b = 0; b < cellName.length; b++) {
                Cell cell2 = row.createCell(b);
                cell2.setCellStyle(cellStyle0);
                switch (cellName[b]) {
                    case "部件":
                        cell2.setCellValue(cutTcBomDetail.getPartName());
                        break;
                    case "图纸":
                        cell2.setCellValue(cutTcBomDetail.getDrawName());
                        break;
                    case "朝向":
                        cell2.setCellValue(cutTcBomDetail.getOrientation());
                        break;
                    case "规格":
                        cell2.setCellValue(cutTcBomDetail.getProductModel());
                        break;
                    case "门幅":
                        cell2.setCellValue(cutTcBomDetail.getProductWidth());
                        break;
                    case "米长":
                        cell2.setCellValue(cutTcBomDetail.getLength());
                        break;
                    case "克重":
                        cell2.setCellValue(cutTcBomDetail.getGramWeight());
                        break;
                    case "制成率":
                        cell2.setCellValue(cutTcBomDetail.getProductionRate());
                        break;
                    case "单价":
                        cell2.setCellValue(cutTcBomDetail.getUnitPrice());
                        break;
                    default:
                        break;
                }
            }
        }

        return wb;
    }




    @Override
    public List<Map<String, Object>> getCutBomJson(String data) {
        List<Map<String, Object>> listMap;
        try {
            listMap = cutTcBomMainDao.getCutBomJson(data);
        } catch (SQLTemplateException e) {
            return null;
        }
        Map<String, Object> ret = null;
        List<Map<String, Object>> _ret = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : listMap) {
            ret = new HashMap<String, Object>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "tcProcBomCodeVersion".toUpperCase()));
            ret.put("state", "closed");
            map.put("nodeType", "bom");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }
}
