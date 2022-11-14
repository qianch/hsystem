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
import com.bluebirdme.mes.cut.cutTcBom.dao.ICutTcBomPartMainDao;
import com.bluebirdme.mes.cut.cutTcBom.entity.CutTcBomMain;
import com.bluebirdme.mes.cut.cutTcBom.entity.CutTcBomPartDetail;
import com.bluebirdme.mes.cut.cutTcBom.entity.CutTcBomPartMain;
import com.bluebirdme.mes.cut.cutTcBom.service.ICutTcBomPartMainService;
import com.bluebirdme.mes.utils.ImportExcel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 徐波
 * @Date 2016-11-2 9:30:07
 */
@Service
@AnyExceptionRollback
public class CutTcBomPartMainServiceImpl extends BaseServiceImpl implements ICutTcBomPartMainService {
    @Resource
    ICutTcBomPartMainDao cutTcBomPartMainDao;

    @Override
    protected IBaseDao getBaseDao() {
        return cutTcBomPartMainDao;
    }

    @Override
    public List<Map<String, Object>> findCutTcBomPartMainByTcBomMainId(Long tcBomMainId) {
        return cutTcBomPartMainDao.findCutTcBomPartMainByTcBomMainId(tcBomMainId);
    }

    @Override
    public List<Map<String, Object>> findCutTcBomPartDetailByMainId(Long mainId) {
        return cutTcBomPartMainDao.findCutTcBomPartDetailByMainId(mainId);
    }

    @Override
    public String saveCutTcBomPartMain(CutTcBomPartMain cutTcBomPartMain, String userId) throws Exception {
        Map<String, Object> param = new HashMap();
        param.put("tcBomMainId", cutTcBomPartMain.getTcBomMainId());
        param.put("partName", cutTcBomPartMain.getPartName());
        param.put("productModel", cutTcBomPartMain.getProductModel());
        param.put("cutName", cutTcBomPartMain.getCutName());
        if (has(CutTcBomPartMain.class, param, cutTcBomPartMain.getId())) {
            throw new Exception("裁片重复PartName:" + cutTcBomPartMain.getPartName() + "  ProductModel:" + cutTcBomPartMain.getProductModel() + " CutName:" + cutTcBomPartMain.getCutName());
        }

        if (cutTcBomPartMain.getId() != null) {
            cutTcBomPartMain.setModifyTime(new Date());
            cutTcBomPartMain.setModifyUser(userId);
            cutTcBomPartMainDao.update(cutTcBomPartMain);
        } else {
            cutTcBomPartMain.setCreater(userId);
            cutTcBomPartMain.setCreateTime(new Date());
            cutTcBomPartMain.setModifyTime(new Date());
            cutTcBomPartMain.setModifyUser(userId);
            cutTcBomPartMainDao.save(cutTcBomPartMain);
        }

        return "";
    }

    @Override
    public String doDeletePartMain(String ids) throws Exception {
        String ids_temp[] = ids.split(",");
        Serializable ids_target[] = new Serializable[ids_temp.length];
        for (int i = 0; i < ids_temp.length; i++) {
            CutTcBomPartMain cutTcBomPartMain = findById(CutTcBomPartMain.class, Long.parseLong(ids_temp[i]));
            if (cutTcBomPartMain == null) {
                continue;
            }
            delete(cutTcBomPartMain);
            Map<String, Object> param = new HashMap();
            param.put("mainId", cutTcBomPartMain.getId());
            delete(CutTcBomPartDetail.class, param);
        }
        return "";
    }

    @Override
    public String doDeletePartDetail(String ids) throws Exception {
        String ids_temp[] = ids.split(",");
        Serializable ids_target[] = new Serializable[ids_temp.length];
        for (int i = 0; i < ids_temp.length; i++) {
            CutTcBomPartDetail cutTcBomPartDetail = findById(CutTcBomPartDetail.class, Long.parseLong(ids_temp[i]));
            if (cutTcBomPartDetail == null) {
                continue;
            }
            delete(cutTcBomPartDetail);
        }
        return "";
    }

    @Override
    public String saveCutTcBomPartDetail(CutTcBomPartDetail cutTcBomPartDetail, String userId) throws Exception {
        Map<String, Object> param = new HashMap();
        param.put("tcBomMainId", cutTcBomPartDetail.getTcBomMainId());
        param.put("mainId", cutTcBomPartDetail.getMainId());
        param.put("cutNameLayNo", cutTcBomPartDetail.getCutNameLayNo());
        if (has(CutTcBomPartDetail.class, param, cutTcBomPartDetail.getId())) {
            throw new Exception("裁片明细重复:" + cutTcBomPartDetail.getCutNameLayNo());
        }

        if (cutTcBomPartDetail.getId() != null) {
            cutTcBomPartDetail.setModifyTime(new Date());
            cutTcBomPartDetail.setModifyUser(userId);
            cutTcBomPartMainDao.update(cutTcBomPartDetail);
        } else {
            cutTcBomPartDetail.setCreater(userId);
            cutTcBomPartDetail.setCreateTime(new Date());
            cutTcBomPartDetail.setModifyTime(new Date());
            cutTcBomPartDetail.setModifyUser(userId);
            cutTcBomPartMainDao.save(cutTcBomPartDetail);
        }
        return "";
    }

    @Override
    public String importCutTcBomPartMainUploadFile(MultipartFile file, String userId) throws Exception {
        String jsonData = "";
        Map<String, Object> param = new HashMap();

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

        if (arrayList.size() < 5) {

            return "文件格式保质期";
        }

        String tcProcBomCodeVersionText = arrayList.get(2)[5];


        if (!tcProcBomCodeVersionText.equals("版本号")) {
            return "文件格式不对，请检查第一个单元格是否为版本!";
        }

        String tcProcBomCodeVersion = arrayList.get(1)[6] + arrayList.get(2)[6];
        CutTcBomMain cutTcBomMain = findOne(CutTcBomMain.class, "tcProcBomCodeVersion", tcProcBomCodeVersion);
        if (cutTcBomMain == null) {
            return "请先导入裁剪图纸bom";
        }

        param.clear();
        param.put("tcBomMainId", cutTcBomMain.getId());
        delete(CutTcBomPartMain.class, param);
        delete(CutTcBomPartDetail.class, param);

        boolean flag = false;
        for (int i = 0; i < arrayList.size(); i++) {
            String value = arrayList.get(i)[0];
            if ("裁片物料号".equals(value)) {
                flag = true;
                continue;
            }

            if ("图纸文件信息".equals(value)) {
                flag = false;
                continue;
            }

            if (flag) {
                String partName = arrayList.get(i)[2];
                String productModel = arrayList.get(i)[3];
                String cutNameLayNo = arrayList.get(i)[4];
                String amount = arrayList.get(i)[6];
                String packSequence = arrayList.get(i)[7];
                String cutName = cutNameLayNo.contains("-") ? cutNameLayNo.substring(0, cutNameLayNo.lastIndexOf("-")) : cutNameLayNo;
                String layNo = cutNameLayNo.replace(cutName, "");
                layNo = layNo.replace("-", "");
                String remark = arrayList.get(i)[10];

                param.clear();
                param.put("tcBomMainId", cutTcBomMain.getId());
                param.put("partName", partName);
                param.put("productModel", productModel);
                param.put("cutName", cutName);
                CutTcBomPartMain cutTcBomDetailPartMain = findUniqueByMap(CutTcBomPartMain.class, param);
                if (cutTcBomDetailPartMain == null) {
                    cutTcBomDetailPartMain = new CutTcBomPartMain();
                    cutTcBomDetailPartMain.setTcBomMainId(cutTcBomMain.getId());
                    cutTcBomDetailPartMain.setPartName(partName);
                    cutTcBomDetailPartMain.setProductModel(productModel);
                    cutTcBomDetailPartMain.setCutName(cutName);
                    cutTcBomDetailPartMain.setCreateTime(new Date());
                    cutTcBomDetailPartMain.setCreater(userId);

                }

                if (!"/".equals(remark) && !"".equals(remark)) {
                    cutTcBomDetailPartMain.setRemark(remark);
                }

                cutTcBomDetailPartMain.setModifyTime(new Date());
                cutTcBomDetailPartMain.setModifyUser(userId);
                cutTcBomDetailPartMain.setLayerNum((cutTcBomDetailPartMain.getLayerNum() == null ? 0 : cutTcBomDetailPartMain.getLayerNum()) + 1);
                save(cutTcBomDetailPartMain);

                CutTcBomPartDetail cutTcBomDetailPartDetail = new CutTcBomPartDetail();
                cutTcBomDetailPartDetail.setTcBomMainId(cutTcBomMain.getId());
                cutTcBomDetailPartDetail.setMainId(cutTcBomDetailPartMain.getId());
                cutTcBomDetailPartDetail.setAmount(Integer.parseInt(amount));
                cutTcBomDetailPartDetail.setPackSequence(Integer.parseInt(packSequence));
                cutTcBomDetailPartDetail.setCutNameLayNo(cutNameLayNo);
                cutTcBomDetailPartDetail.setCreateTime(new Date());
                cutTcBomDetailPartDetail.setCreater(userId);
                cutTcBomDetailPartDetail.setCreateTime(new Date());
                cutTcBomDetailPartDetail.setCreater(userId);
                cutTcBomDetailPartDetail.setModifyTime(new Date());
                cutTcBomDetailPartDetail.setModifyUser(userId);
                cutTcBomDetailPartDetail.setLayNo("".equals(layNo) ? 1 : Integer.parseInt(layNo));
                save(cutTcBomDetailPartDetail);
            }
        }
        return "";
    }


    public SXSSFWorkbook exportCutTcBomPart(Long tcBomMainId) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
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

        CutTcBomMain cutTcBomMain = findById(CutTcBomMain.class, tcBomMainId);
        List<Map<String, Object>> listCutTcBomPartMain = findCutTcBomPartMainByTcBomMainId(tcBomMainId);
        String cellName[] = new String[]{"叶片部位", "胚布规格", "产品名称", "数量", "托1", "托2", "托3", "托4", "叶片部位", "胚布规格", "产品名称", "数量", "托1", "托2", "托3", "托4"};

        int intTr = 0;
        int r = 0;// 从第1行开始写数据
        int rowCell = 31;

        for (Map<String, Object> partMain : listCutTcBomPartMain) {
            String partName = partMain.get("PARTNAME").toString();
            String productModel = partMain.get("PRODUCTMODEL").toString();
            String remark = partMain.get("REMARK").toString();
            long mainId = Long.parseLong(partMain.get("ID").toString());

            List<CutTcBomPartDetail> listCutTcBomPartDetail = find(CutTcBomPartDetail.class, "mainId", mainId);
            if (listCutTcBomPartDetail != null && listCutTcBomPartDetail.size() > 0) {
                Collections.sort(listCutTcBomPartDetail, (o1, o2) -> o2.getPackSequence().compareTo(o1.getPackSequence()));
            }

            for (int i = 0; i < listCutTcBomPartDetail.size(); i++) {
                CutTcBomPartDetail cutTcBomPartDetail = listCutTcBomPartDetail.get(i);

                if (intTr == 0) {
                    r = createHeadRow(sheet, cutTcBomMain, r, cellStyle, cellName);
                }

                if (intTr == 2 * rowCell) {
                    intTr = 0;
                    r = createFootRow(sheet, r, cellStyle);
                    r++;
                    row = sheet.createRow(r);
                    for (int j = 0; j < cellName.length; j++) {
                        cell = row.createCell(j);
                        cell.setCellStyle(cellStyle);
                    }
                    sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 15));
                    r = createHeadRow(sheet, cutTcBomMain, r, cellStyle, cellName);
                }

                int h = intTr < rowCell ? 0 : 8;
                if (h == 0) {
                    r++;
                    row = sheet.createRow(r);
                }
                intTr++;
                for (int b = h; b < h + 8; b++) {
                    Cell cell2 = null;
                    if (h == 0) {
                        cell2 = row.createCell(b);
                    } else {
                        cell2 = sheet.getRow(r - rowCell + (intTr - rowCell)).createCell(b);
                    }

                    cell2.setCellStyle(cellStyle0);
                    switch (cellName[b]) {
                        case "叶片部位":
                            cell2.setCellValue(partName + remark);
                            break;
                        case "胚布规格":
                            cell2.setCellValue(productModel);
                            break;
                        case "产品名称":
                            cell2.setCellValue(cutTcBomPartDetail.getCutNameLayNo());
                            break;
                        case "数量":
                            cell2.setCellValue(cutTcBomPartDetail.getAmount());
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return wb;
    }

    public int createHeadRow(Sheet sheet, CutTcBomMain cutTcBomMain, int r, CellStyle cellStyle, String cellName[]) {
        Row row = null;
        Cell cell = null;

        r++;
        row = sheet.createRow(r);

        cell = row.createCell(0);
        sheet.setColumnWidth(0, 26 * 256);// 设置列宽
        cell.setCellStyle(cellStyle);
        cell.setCellValue(cutTcBomMain.getBladeTypeName() + "质检确认表");

        cell = row.createCell(1);
        sheet.setColumnWidth(1, 26 * 256);// 设置列宽
        cell.setCellStyle(cellStyle);

        cell = row.createCell(2);
        sheet.setColumnWidth(2, 26 * 256);
        cell.setCellStyle(cellStyle);

        cell = row.createCell(3);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(4);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(5);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(6);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(7);
        cell.setCellStyle(cellStyle);

        cell = row.createCell(8);
        sheet.setColumnWidth(8, 26 * 256);// 设置列宽
        cell.setCellStyle(cellStyle);

        cell = row.createCell(9);
        sheet.setColumnWidth(9, 26 * 256);// 设置列宽
        cell.setCellStyle(cellStyle);

        cell = row.createCell(10);
        sheet.setColumnWidth(10, 26 * 256);
        cell.setCellStyle(cellStyle);

        cell = row.createCell(11);
        cell.setCellStyle(cellStyle);

        cell = row.createCell(12);
        cell.setCellStyle(cellStyle);

        cell = row.createCell(13);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(14);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(15);
        cell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 15));
        r++;
        row = sheet.createRow(r);

        for (int i = 0; i < cellName.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            if (i == 12) {
                cell.setCellValue(cutTcBomMain.getTcProcBomCodeVersion());
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 12, 15));

        r++;
        row = sheet.createRow(r);
        for (int b = 0; b < cellName.length; b++) {
            Cell cell2 = row.createCell(b);
            cell2.setCellValue(cellName[b]);
            cell2.setCellStyle(cellStyle);
        }
        return r;
    }

    public int createFootRow(Sheet sheet, int r, CellStyle cellStyle) {
        Row row = null;
        Cell cell = null;

        r++;
        row = sheet.createRow(r);
        for (int i = 0; i < 16; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            if (i == 0) {
                cell.setCellValue("一套/层,最多4套/托");
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 15));

        r++;
        row = sheet.createRow(r);
        for (int i = 0; i < 16; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            switch (i) {
                case 0:
                    cell.setCellValue("裁剪日期：");
                    break;
                case 3:
                    cell.setCellValue("裁剪小组：");
                    break;
                case 8:
                    cell.setCellValue("质检：");
                    break;
                case 11:
                    cell.setCellValue("托号：");
                    break;
            }
        }

        sheet.addMergedRegion(new CellRangeAddress(r, r, 3, 4));
        sheet.addMergedRegion(new CellRangeAddress(r, r, 5, 7));

        sheet.addMergedRegion(new CellRangeAddress(r, r, 11, 12));
        sheet.addMergedRegion(new CellRangeAddress(r, r, 13, 15));

        return r;
    }
}
