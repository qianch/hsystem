/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service.impl;

import com.aspose.cells.Worksheet;
import com.bluebirdme.mes.baseInfo.dao.IFtcBcBomDao;
import com.bluebirdme.mes.baseInfo.entity.*;
import com.bluebirdme.mes.baseInfo.service.IFtcBcBomService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.platform.entity.Attachment;
import com.bluebirdme.mes.sales.entity.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.xdemo.superutil.j2se.MapUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @author 高飞
 * @Date 2017-11-28 11:10:48
 */
@Service
@AnyExceptionRollback
public class FtcBcBomServiceImpl extends BaseServiceImpl implements IFtcBcBomService {
    private static final Logger log = LoggerFactory.getLogger(FtcBcBomServiceImpl.class);
    @Resource
    IFtcBcBomDao ftcBcBomDao;

    @Override
    protected IBaseDao getBaseDao() {
        return ftcBcBomDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return ftcBcBomDao.findPageInfo(filter, page);
    }

    /**
     * getFtcBcBomJson 方法的简述.
     * 获取非套材包材bom的json格式数据，用于组装treeview<br>
     *
     * @param level
     * @return 类型:List<Map<String, Object>>，返回组装完成的非套材包材bom的json数据
     */
    public List<Map<String, Object>> getFtcBcBomJson(String pid, String queryData, Integer level) {
        List<FtcBcBom> ftcBcBomList;
        try {
            ftcBcBomList = ftcBcBomDao.tree(pid, queryData, level);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<>();
        for (FtcBcBom ftcBcBom : ftcBcBomList) {
            ret = new HashMap<>();
            ret.put("id", ftcBcBom.getId());
            if (ftcBcBom.getPackCanceled() != null && -1 == ftcBcBom.getPackCanceled()) {
                ret.put("text", ftcBcBom.getName() + "/" + ftcBcBom.getCode() + "<font color='red'>[作废]</font>");
            } else {
                ret.put("text", ftcBcBom.getName() + "/" + ftcBcBom.getCode());
            }
            ret.put("state", "closed");
            Map<String, Object> map = new HashMap<>();
            map.put("nodeType", "bom" + ftcBcBom.getLevel());
            map.put("level", ftcBcBom.getLevel());
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }


    @Override
    /**
     * delete 方法的简述.
     * 根据传入的非套材包材bom的id删除对应的非套材包材bom和明细<br>
     * @param ids 类型:String，多个id用‘,’号分割
     * @return 无
     */
    public void deleteAll(String ids) throws Exception {
        String[] id = ids.split(",");
        for (String s : id) {
            FtcBcBom fbc = findById(FtcBcBom.class, Long.valueOf(s));
            HashMap<String, Object> map = new HashMap<>();
            //三级包装
            if (fbc.getLevel() == 3) {
                map.put("bid", fbc.getId());
                List<FtcBcBomVersion> versionList = findListByMap(FtcBcBomVersion.class, map);
                for (FtcBcBomVersion version : versionList) {
                    map.clear();
                    map.put("packVersionId", version.getId());
                    List<FtcBcBomVersionDetail> detailList = findListByMap(FtcBcBomVersionDetail.class, map);
                    delete(detailList.toArray(new FtcBcBomVersionDetail[]{}));
                }
                delete(versionList.toArray(new FtcBcBomVersion[]{}));
            } else if (fbc.getLevel() == 2) {//二级包装
                map.put("pid", fbc.getId());
                List<FtcBcBom> fbbList = findListByMap(FtcBcBom.class, map);
                for (FtcBcBom fbb : fbbList) {
                    map.clear();
                    map.put("bid", fbb.getId());
                    List<FtcBcBomVersion> versionList = findListByMap(FtcBcBomVersion.class, map);
                    for (FtcBcBomVersion version : versionList) {
                        map.clear();
                        map.put("packVersionId", version.getId());
                        List<FtcBcBomVersionDetail> detailList = findListByMap(FtcBcBomVersionDetail.class, map);
                        delete(detailList.toArray(new FtcBcBomVersionDetail[]{}));
                    }
                    delete(versionList.toArray(new FtcBcBomVersion[]{}));
                }
                delete(fbbList.toArray(new FtcBcBom[]{}));
            } else if (fbc.getLevel() == 1) {//一级包装
                map.put("pid", fbc.getId());
                List<FtcBcBom> fbaList = findListByMap(FtcBcBom.class, map);
                for (FtcBcBom fba : fbaList) {
                    map.clear();
                    map.put("bid", fba.getId());
                    List<FtcBcBom> fbbList = findListByMap(FtcBcBom.class, map);
                    for (FtcBcBom fbb : fbbList) {
                        map.clear();
                        map.put("bid", fbb.getId());
                        List<FtcBcBomVersion> versionList = findListByMap(FtcBcBomVersion.class, map);
                        for (FtcBcBomVersion version : versionList) {
                            map.clear();
                            map.put("packVersionId", version.getId());
                            List<FtcBcBomVersionDetail> detailList = findListByMap(FtcBcBomVersionDetail.class, map);
                            delete(detailList.toArray(new FtcBcBomVersionDetail[]{}));
                        }
                        delete(versionList.toArray(new FtcBcBomVersion[]{}));
                    }
                    delete(fbbList.toArray(new FtcBcBom[]{}));
                }
                delete(fbaList.toArray(new FtcBcBom[]{}));
            }
            delete(fbc);
        }
    }

    @Override
    public ExcelImportMessage doAddFtcBcBom(FtcBcBom ftcBcBom, Long fileId) throws Exception {
        ftcBcBomDao.save(ftcBcBom);
        //存放错误消息
        ExcelImportMessage eim = new ExcelImportMessage();
        if (ftcBcBom.getId() != null && fileId != null) {
            Attachment att = ftcBcBomDao.findById(Attachment.class, fileId);
            String filePath = att.getFilePath();
            // 读取文件
            InputStream is = new FileInputStream(filePath);
            Workbook wb = WorkbookFactory.create(is);
            List<Map<String, Object>> data = new ArrayList<>();
            Sheet sheet1 = wb.getSheetAt(0);
            int fi = -1;
            for (int i = 0; i < sheet1.getLastRowNum(); i++) {
                Row row = sheet1.getRow(i);
                if (row != null && "文件说明".equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {
                    fi = i + 1;
                    continue;
                }
                if (fi != -1 && fi < i) {
                    if (row != null) {
                        boolean rowNotBlank = false;
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            if (StringUtils.isNotBlank(cell.toString())) {
                                rowNotBlank = true;
                            }
                        }
                        if (rowNotBlank) {
                            String str1 = row.getCell(12, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                            String str2 = row.getCell(15, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                            String str3 = row.getCell(17, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                            Map<String, Object> map = new HashMap<>();
                            if (StringUtils.isBlank(str1)) {
                                eim.addMessage(i + 1, 13, sheet1.getSheetName() + "工作表\"产品包装名称\"不能为空");
                            } else {
                                map.put("name3", str1);
                            }

                            if (StringUtils.isBlank(str2)) {
                                eim.addMessage(i + 1, 16, sheet1.getSheetName() + "工作表\"三级包装工艺代码\"不能为空");
                            } else {
                                map.put("code3", str2);
                            }

                            if (StringUtils.isBlank(str3)) {
                                eim.addMessage(i + 1, 18, sheet1.getSheetName() + "工作表\"版本\"不能为空");
                            } else {
                                map.put("version", str3);
                            }
                            data.add(map);
                        } else {
                            break;
                        }
                    }
                }
            }
            if (fi == -1) {
                eim.addMessage("Excel文件格式错误");
            }
            //Excel文件中sheet的个数
            int sheetCount = wb.getNumberOfSheets();
            Map<String, String> titleMap = new HashMap<>();
            for (Map<String, Object> map : data) {
                String code3 = MapUtils.getAsString(map, "code3");
                //从第2个sheet开始循环
                Sheet sheet = null;
                for (int i = 1; i < sheetCount; i++) {
                    Sheet sheet0 = wb.getSheetAt(i);
                    String sheetName = sheet0.getSheetName();
                    if (StringUtils.equalsIgnoreCase(code3, sheetName)) {
                        sheet = sheet0;
                        break;
                    }
                }
                if (sheet == null) {
                    eim.addMessage(code3 + "对应的sheet不存在");
                } else {
                    //包装材料 所在的行号
                    int fa = -1;
                    //包装要求 所在的行号
                    int fb = -1;
                    List<Packaging> packagingList = new ArrayList<>();
                    for (int i = 0; i < sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        if (row != null) {
                            for (int j = 0; j < row.getLastCellNum(); j++) {
                                Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                if (StringUtils.startsWith(cell.toString(), "包材重量")) {
                                    titleMap.put("bcTotalWeight", "包材重量");
                                    map.put("bcTotalWeight", row.getCell(j + 2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "产品类别")) {
                                    titleMap.put("productType", "产品类别");
                                    Cell cc = row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    if (StringUtils.isBlank(cc.toString())) {
                                        eim.addMessage(i + 1, j + 2, code3 + "工作表\"产品类别\"不能为空");
                                    } else if (!"常规产品".equals(cc.toString()) && !"变更试样".equals(cc.toString()) && !"新品试样".equals(cc.toString())) {
                                        eim.addMessage(i + 1, j + 2, code3 + "工作表\"产品类别\"不正确");
                                    } else {
                                        if ("常规产品".equals(cc.toString())) {
                                            map.put("productType", 0);
                                        } else if ("变更试样".equals(cc.toString())) {
                                            map.put("productType", 1);
                                        } else {
                                            map.put("productType", 2);
                                        }
                                    }
                                }
                                if (StringUtils.equals(cell.toString(), "客户代码")) {
                                    titleMap.put("consumerCode", "客户代码");
                                    Cell cc = row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    if (StringUtils.isBlank(cc.toString())) {
                                        eim.addMessage(i + 1, j + 2, code3 + "工作表\"客户代码\"不能为空");
                                    } else if (!StringUtils.startsWith(cc.toString(), "HS")) {
                                        eim.addMessage(i + 1, j + 2, code3 + "工作表\"客户代码\"必须以HS开头");
                                    } else {
                                        map.put("consumerCode", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                    }
                                }
                                if (StringUtils.startsWith(cell.toString(), "卷径")) {
                                    titleMap.put("rollDiameter", "卷径");
                                    map.put("rollDiameter", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.startsWith(cell.toString(), "托长")) {
                                    titleMap.put("palletLength", "托长");
                                    map.put("palletLength", row.getCell(j + 2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.startsWith(cell.toString(), "托宽")) {
                                    titleMap.put("palletWidth", "托宽");
                                    map.put("palletWidth", row.getCell(j + 2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.startsWith(cell.toString(), "部件数量")) {
                                    titleMap.put("rollsPerPallet", "部件数量");
                                    Cell cc = row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    if (StringUtils.isBlank(cc.toString())) {
                                        eim.addMessage(i + 1, j + 2, code3 + "工作表\"部件数量\"不能为空");
                                    } else {
                                        map.put("rollsPerPallet", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                    }

                                }
                                if (StringUtils.startsWith(cell.toString(), "托高")) {
                                    titleMap.put("palletHeight", "托高");
                                    map.put("palletHeight", row.getCell(j + 2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }

                                if (StringUtils.equals(cell.toString(), "塑料膜要求")) {
                                    titleMap.put("requirement_suliaomo", "塑料膜要求");
                                    map.put("requirement_suliaomo", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "摆放要求")) {
                                    titleMap.put("requirement_baifang", "摆放要求");
                                    map.put("requirement_baifang", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "打包带要求")) {
                                    titleMap.put("requirement_dabaodai", "打包带要求");
                                    map.put("requirement_dabaodai", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "标签要求")) {
                                    titleMap.put("requirement_biaoqian", "标签要求");
                                    map.put("requirement_biaoqian", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "小标签要求")) {
                                    titleMap.put("requirement_xiaobiaoqian", "小标签要求");
                                    map.put("requirement_xiaobiaoqian", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "卷（箱）标签要求")) {
                                    titleMap.put("requirement_juanbiaoqian", "卷（箱）标签要求");
                                    map.put("requirement_juanbiaoqian", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "托标签要求")) {
                                    titleMap.put("requirement_tuobiaoqian", "托标签要求");
                                    map.put("requirement_tuobiaoqian", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "缠绕要求")) {
                                    titleMap.put("requirement_chanrao", "缠绕要求");
                                    map.put("requirement_chanrao", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "其他要求")) {
                                    titleMap.put("requirement_other", "其他要求");
                                    map.put("requirement_other", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                            }//end for j

                            if (StringUtils.equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "包装材料")) {
                                fa = i;
                                continue;
                            }
                            if (fa != -1 && fa + 1 < i && fb == -1) {//行号在 包装材料 和 包装要求 之间的数据
                                if (StringUtils.equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "包装要求")) {
                                    fb = i;
                                    continue;
                                }
                                Cell cell1 = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell2 = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell3 = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell4 = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell5 = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell6 = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell7 = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                if (StringUtils.isNotBlank(cell1.toString()) || StringUtils.isNotBlank(cell2.toString())
                                        || StringUtils.isNotBlank(cell3.toString()) || StringUtils.isNotBlank(cell4.toString())
                                        || StringUtils.isNotBlank(cell5.toString()) || StringUtils.isNotBlank(cell6.toString())
                                        || StringUtils.isNotBlank(cell7.toString())) {
                                    Packaging pack = new Packaging();
                                    if (StringUtils.isBlank(cell1.toString())) {
                                        eim.addMessage(i + 1, 1, "物料代码不能为空");
                                    } else {
                                        pack.setMaterialCode(cell1.toString());
                                    }
                                    pack.setStandardCode(cell2.toString());
                                    if (StringUtils.isBlank(cell3.toString())) {
                                        eim.addMessage(i + 1, 3, "材料名称不能为空");
                                    } else {
                                        pack.setMaterialName(cell3.toString());
                                    }
                                    pack.setSpecifications(cell4.toString());
                                    if (StringUtils.isBlank(cell5.toString())) {
                                        eim.addMessage(i + 1, 7, "单位不能为空");
                                    } else {
                                        pack.setUnit(cell5.toString());
                                    }
                                    if (StringUtils.isBlank(cell6.toString())) {
                                        eim.addMessage(i + 1, 8, "数量不能为空");
                                    } else if (cell6.getCellType() != CellType.NUMERIC) {
                                        eim.addMessage(i + 1, 8, "数量只能是数字");
                                    } else {
                                        pack.setQuantity(cell6.getNumericCellValue());
                                    }
                                    pack.setRemarks(cell7.toString());
                                    packagingList.add(pack);
                                }
                            }
                        }
                    }
                    if (fa == -1) {
                        eim.addMessage(code3 + "工作表\"包装材料\"标题不存在");
                    }
                    if (fb == -1) {
                        eim.addMessage(code3 + "工作表\"包装要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("bcTotalWeight")) {
                        eim.addMessage(code3 + "工作表\"包材重量\"标题不存在");
                    }
                    if (!titleMap.containsKey("productType")) {
                        eim.addMessage(code3 + "工作表\"产品类别\"标题不存在");
                    }
                    if (!titleMap.containsKey("consumerCode")) {
                        eim.addMessage(code3 + "工作表\"客户代码\"标题不存在");
                    }
                    if (!titleMap.containsKey("rollDiameter")) {
                        eim.addMessage(code3 + "工作表\"卷径\"标题不存在");
                    }
                    if (!titleMap.containsKey("palletLength")) {
                        eim.addMessage(code3 + "工作表\"托长\"标题不存在");
                    }
                    if (!titleMap.containsKey("palletWidth")) {
                        eim.addMessage(code3 + "工作表\"托宽\"标题不存在");
                    }
                    if (!titleMap.containsKey("rollsPerPallet")) {
                        eim.addMessage(code3 + "工作表\"部件数量\"标题不存在");
                    }
                    if (!titleMap.containsKey("palletHeight")) {
                        eim.addMessage(code3 + "工作表\"托高\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_suliaomo")) {
                        eim.addMessage(code3 + "工作表\"塑料膜要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_baifang")) {
                        eim.addMessage(code3 + "工作表\"摆放要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_dabaodai")) {
                        eim.addMessage(code3 + "工作表\"打包带要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_biaoqian")) {
                        eim.addMessage(code3 + "工作表\"标签要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_xiaobiaoqian")) {
                        eim.addMessage(code3 + "工作表\"小标签要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_juanbiaoqian")) {
                        eim.addMessage(code3 + "工作表\"卷（箱）标签要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_tuobiaoqian")) {
                        eim.addMessage(code3 + "工作表\"托标签要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_chanrao")) {
                        eim.addMessage(code3 + "工作表\"缠绕要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_other")) {
                        eim.addMessage(code3 + "工作表\"其他要求\"标题不存在");
                    }
                    map.put("packagingList", packagingList);
                }
            }

            if (!eim.hasError()) {
                //把数据放入非套材包材BOM
                for (Map<String, Object> map : data) {
                    String code = MapUtils.getAsString(map, "code3");
                    String name = MapUtils.getAsString(map, "name3");
                    String version = MapUtils.getAsString(map, "version");
                    String productType = MapUtils.getAsString(map, "productType");
                    String consumerCode = MapUtils.getAsString(map, "consumerCode");
                    String bcTotalWeight = MapUtils.getAsString(map, "bcTotalWeight");
                    String rollDiameter = MapUtils.getAsString(map, "rollDiameter");
                    String palletLength = MapUtils.getAsString(map, "palletLength");
                    String palletWidth = MapUtils.getAsString(map, "palletWidth");
                    String rollsPerPallet = MapUtils.getAsString(map, "rollsPerPallet");
                    String palletHeight = MapUtils.getAsString(map, "palletHeight");
                    String requirement_suliaomo = MapUtils.getAsString(map, "requirement_suliaomo");
                    String requirement_baifang = MapUtils.getAsString(map, "requirement_baifang");
                    String requirement_dabaodai = MapUtils.getAsString(map, "requirement_dabaodai");
                    String requirement_biaoqian = MapUtils.getAsString(map, "requirement_biaoqian");
                    String requirement_xiaobiaoqian = MapUtils.getAsString(map, "requirement_xiaobiaoqian");
                    String requirement_juanbiaoqian = MapUtils.getAsString(map, "requirement_juanbiaoqian");
                    String requirement_tuobiaoqian = MapUtils.getAsString(map, "requirement_tuobiaoqian");
                    String requirement_chanrao = MapUtils.getAsString(map, "requirement_chanrao");
                    String requirement_other = MapUtils.getAsString(map, "requirement_other");
                    FtcBcBom ftcBcBom3 = new FtcBcBom();
                    ftcBcBom3.setCode(code);
                    ftcBcBom3.setName(name);
                    ftcBcBom3.setLevel(3);
                    ftcBcBom3.setPid(ftcBcBom.getId());
                    ftcBcBomDao.save(ftcBcBom3);
                    //添加非套材包材版本
                    FtcBcBomVersion ftcBcBomVersion = new FtcBcBomVersion();
                    ftcBcBomVersion.setBid(ftcBcBom3.getId());
                    ftcBcBomVersion.setVersion(version);
                    ftcBcBomVersion.setProductType(productType);
                    Map<String, Object> param = new HashMap<>();
                    param.put("consumerCode", consumerCode);
                    Consumer c = ftcBcBomDao.findUniqueByMap(Consumer.class, param);
                    ftcBcBomVersion.setConsumerId(c.getId());
                    ftcBcBomVersion.setBcTotalWeight(bcTotalWeight);
                    ftcBcBomVersion.setRollDiameter(rollDiameter);
                    ftcBcBomVersion.setPalletLength(palletLength);
                    ftcBcBomVersion.setPalletWidth(palletWidth);
                    ftcBcBomVersion.setRollsPerPallet(rollsPerPallet);
                    ftcBcBomVersion.setPalletHeight(palletHeight);
                    ftcBcBomVersion.setRequirement_suliaomo(requirement_suliaomo);
                    ftcBcBomVersion.setRequirement_baifang(requirement_baifang);
                    ftcBcBomVersion.setRequirement_dabaodai(requirement_dabaodai);
                    ftcBcBomVersion.setRequirement_biaoqian(requirement_biaoqian);
                    ftcBcBomVersion.setRequirement_xiaobiaoqian(requirement_xiaobiaoqian);
                    ftcBcBomVersion.setRequirement_juanbiaoqian(requirement_juanbiaoqian);
                    ftcBcBomVersion.setRequirement_tuobiaoqian(requirement_tuobiaoqian);
                    ftcBcBomVersion.setRequirement_chanrao(requirement_chanrao);
                    ftcBcBomVersion.setRequirement_other(requirement_other);
                    ftcBcBomVersion.setAttachmentId(fileId.intValue());
                    ftcBcBomDao.save(ftcBcBomVersion);
                    //添加非套材包材版本明细
                    @SuppressWarnings("unchecked")
                    List<Packaging> packagingList = (List<Packaging>) map.get("packagingList");
                    for (Packaging packaging : packagingList) {
                        FtcBcBomVersionDetail vd = new FtcBcBomVersionDetail();
                        vd.setPackVersionId(ftcBcBomVersion.getId());
                        vd.setPackMaterialCode(packaging.getMaterialCode());
                        vd.setPackStandardCode(packaging.getStandardCode());
                        vd.setPackMaterialName(packaging.getMaterialName());
                        vd.setPackMaterialModel(packaging.getSpecifications());
                        vd.setPackUnit(packaging.getUnit());
                        vd.setPackMaterialCount(packaging.getQuantity());
                        vd.setPackMemo(packaging.getRemarks());
                        ftcBcBomDao.save(vd);
                    }
                }
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动回滚事物
            }
        }
        return eim;
    }

    @Override
    public ExcelImportMessage doUpdateFtcBcBom(FtcBcBom ftcBcBom, Long fileId, StringBuffer info) throws Exception {
        ftcBcBomDao.update2(ftcBcBom);
        //存放错误消息
        ExcelImportMessage eim = new ExcelImportMessage();
        if (ftcBcBom.getId() != null && fileId != null) {
            Attachment att = ftcBcBomDao.findById(Attachment.class, fileId);
            String filePath = att.getFilePath();
            InputStream is = new FileInputStream(filePath);
            Workbook wb = WorkbookFactory.create(is);
            List<Map<String, Object>> data = new ArrayList<>();
            Sheet sheet1 = wb.getSheetAt(0);
            int fi = -1;
            for (int i = 0; i < sheet1.getLastRowNum(); i++) {
                Row row = sheet1.getRow(i);
                if (row != null && "文件说明".equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {
                    fi = i + 1;
                    continue;
                }
                if (fi != -1 && fi < i) {
                    if (row != null) {
                        boolean rowNotBlank = false;
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            if (StringUtils.isNotBlank(cell.toString())) {
                                rowNotBlank = true;
                            }
                        }
                        if (rowNotBlank) {
                            String str1 = row.getCell(12, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                            String str2 = row.getCell(15, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                            String str3 = row.getCell(17, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                            Map<String, Object> map = new HashMap<>();
                            if (StringUtils.isBlank(str1)) {
                                eim.addMessage(i + 1, 13, sheet1.getSheetName() + "工作表\"产品包装名称\"不能为空");
                            } else {
                                map.put("name3", str1);
                            }
                            if (StringUtils.isBlank(str2)) {
                                eim.addMessage(i + 1, 16, sheet1.getSheetName() + "工作表\"三级包装工艺代码\"不能为空");
                            } else {
                                map.put("code3", str2);
                            }
                            if (StringUtils.isBlank(str3)) {
                                eim.addMessage(i + 1, 18, sheet1.getSheetName() + "工作表\"版本\"不能为空");
                            } else {
                                map.put("version", str3);
                            }
                            data.add(map);
                        } else {
                            break;
                        }
                    }
                }
            }
            if (fi == -1) {
                eim.addMessage("Excel文件格式错误");
            }
            for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext(); ) {
                Map<String, Object> map = iterator.next();
                String code = MapUtils.getAsString(map, "code3");
                String name = MapUtils.getAsString(map, "name3");
                String version = MapUtils.getAsString(map, "version");
                Map<String, Object> param = new HashMap<>();
                param.put("code", code);
                param.put("name", name);
                param.put("pid", ftcBcBom.getId());
                List<FtcBcBom> fbbL = ftcBcBomDao.findListByMap(FtcBcBom.class, param);
                for (FtcBcBom ftcBcBom_3 : fbbL) {
                    param.clear();
                    param.put("bid", ftcBcBom_3.getId());
                    List<FtcBcBomVersion> fbbvL = findListByMap(FtcBcBomVersion.class, param);
                    for (FtcBcBomVersion ftcBcBomVersion : fbbvL) {
                        if (StringUtils.equals(ftcBcBomVersion.getVersion(), version)) {
                            info.append("三级包装：").append(name).append("/").append(code).append("存在重复版本:").append(version).append("(忽略导入)<br/>");
                            iterator.remove();
                            break;
                        }
                        if (ftcBcBomVersion.getAuditState() < 2) {
                            info.append("三级包装：").append(name).append("/").append(code).append("存在[未提交]、[不通过]或[审核中]的版本(忽略导入)<br/>");
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
            info.append("<hr/>本次导入版本为").append(data.size()).append("条");
            //Excel文件中sheet的个数
            int sheetCount = wb.getNumberOfSheets();
            Map<String, String> titleMap = new HashMap<>();
            for (Map<String, Object> map : data) {
                String code3 = MapUtils.getAsString(map, "code3");
                //从第2个sheet开始循环
                Sheet sheet = null;
                for (int i = 1; i < sheetCount; i++) {
                    Sheet sheet0 = wb.getSheetAt(i);
                    String sheetName = sheet0.getSheetName();
                    if (StringUtils.equalsIgnoreCase(code3, sheetName)) {
                        sheet = sheet0;
                        break;
                    }
                }
                if (sheet == null) {
                    eim.addMessage(code3 + "对应的sheet不存在");
                } else {
                    //包装材料 所在的行号
                    int fa = -1;
                    //包装要求 所在的行号
                    int fb = -1;
                    List<Packaging> packagingList = new ArrayList<>();
                    for (int i = 0; i < sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        if (row != null) {
                            for (int j = 0; j < row.getLastCellNum(); j++) {
                                Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                if (StringUtils.startsWith(cell.toString(), "包材重量")) {
                                    titleMap.put("bcTotalWeight", "包材重量");
                                    map.put("bcTotalWeight", row.getCell(j + 2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "产品类别")) {
                                    titleMap.put("productType", "产品类别");
                                    Cell cc = row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    if (StringUtils.isBlank(cc.toString())) {
                                        eim.addMessage(i + 1, j + 2, code3 + "工作表\"产品类别\"不能为空");
                                    } else if (!"常规产品".equals(cc.toString()) && !"变更试样".equals(cc.toString()) && !"新品试样".equals(cc.toString())) {
                                        eim.addMessage(i + 1, j + 2, code3 + "工作表\"产品类别\"不正确");
                                    } else {
                                        if ("常规产品".equals(cc.toString())) {
                                            map.put("productType", 0);
                                        } else if ("变更试样".equals(cc.toString())) {
                                            map.put("productType", 1);
                                        } else {
                                            map.put("productType", 2);
                                        }
                                    }
                                }
                                if (StringUtils.equals(cell.toString(), "客户代码")) {
                                    titleMap.put("consumerCode", "客户代码");
                                    Cell cc = row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    if (StringUtils.isBlank(cc.toString())) {
                                        eim.addMessage(i + 1, j + 2, code3 + "工作表\"客户代码\"不能为空");
                                    } else if (!StringUtils.startsWith(cc.toString(), "HS")) {
                                        eim.addMessage(i + 1, j + 2, code3 + "工作表\"客户代码\"必须以HS开头");
                                    } else {
                                        map.put("consumerCode", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                    }
                                }
                                if (StringUtils.startsWith(cell.toString(), "卷径")) {
                                    titleMap.put("rollDiameter", "卷径");
                                    map.put("rollDiameter", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.startsWith(cell.toString(), "托长")) {
                                    titleMap.put("palletLength", "托长");
                                    map.put("palletLength", row.getCell(j + 2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.startsWith(cell.toString(), "托宽")) {
                                    titleMap.put("palletWidth", "托宽");
                                    map.put("palletWidth", row.getCell(j + 2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.startsWith(cell.toString(), "部件数量")) {
                                    titleMap.put("rollsPerPallet", "部件数量");
                                    Cell cc = row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    if (StringUtils.isBlank(cc.toString())) {
                                        eim.addMessage(i + 1, j + 2, code3 + "工作表\"部件数量\"不能为空");
                                    } else {
                                        map.put("rollsPerPallet", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                    }
                                }
                                if (StringUtils.startsWith(cell.toString(), "托高")) {
                                    titleMap.put("palletHeight", "托高");
                                    map.put("palletHeight", row.getCell(j + 2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }

                                if (StringUtils.equals(cell.toString(), "塑料膜要求")) {
                                    titleMap.put("requirement_suliaomo", "塑料膜要求");
                                    map.put("requirement_suliaomo", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "摆放要求")) {
                                    titleMap.put("requirement_baifang", "摆放要求");
                                    map.put("requirement_baifang", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "打包带要求")) {
                                    titleMap.put("requirement_dabaodai", "打包带要求");
                                    map.put("requirement_dabaodai", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "标签要求")) {
                                    titleMap.put("requirement_biaoqian", "标签要求");
                                    map.put("requirement_biaoqian", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "小标签要求")) {
                                    titleMap.put("requirement_xiaobiaoqian", "小标签要求");
                                    map.put("requirement_xiaobiaoqian", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "卷（箱）标签要求")) {
                                    titleMap.put("requirement_juanbiaoqian", "卷（箱）标签要求");
                                    map.put("requirement_juanbiaoqian", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "托标签要求")) {
                                    titleMap.put("requirement_tuobiaoqian", "托标签要求");
                                    map.put("requirement_tuobiaoqian", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "缠绕要求")) {
                                    titleMap.put("requirement_chanrao", "缠绕要求");
                                    map.put("requirement_chanrao", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                                if (StringUtils.equals(cell.toString(), "其他要求")) {
                                    titleMap.put("requirement_other", "其他要求");
                                    map.put("requirement_other", row.getCell(j + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
                                }
                            }
                            if (StringUtils.equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "包装材料")) {
                                fa = i;
                                continue;
                            }
                            if (fa != -1 && fa + 1 < i && fb == -1) {//行号在 包装材料 和 包装要求 之间的数据
                                if (StringUtils.equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "包装要求")) {
                                    fb = i;
                                    continue;
                                }
                                Cell cell1 = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell2 = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell3 = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell4 = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell5 = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell6 = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell7 = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                if (StringUtils.isNotBlank(cell1.toString()) || StringUtils.isNotBlank(cell2.toString())
                                        || StringUtils.isNotBlank(cell3.toString()) || StringUtils.isNotBlank(cell4.toString())
                                        || StringUtils.isNotBlank(cell5.toString()) || StringUtils.isNotBlank(cell6.toString())
                                        || StringUtils.isNotBlank(cell7.toString())) {
                                    Packaging pack = new Packaging();
                                    if (StringUtils.isBlank(cell1.toString())) {
                                        eim.addMessage(i + 1, 1, "物料代码不能为空");
                                    } else {
                                        pack.setMaterialCode(cell1.toString());
                                    }
                                    pack.setStandardCode(cell2.toString());
                                    if (StringUtils.isBlank(cell3.toString())) {
                                        eim.addMessage(i + 1, 3, "材料名称不能为空");
                                    } else {
                                        pack.setMaterialName(cell3.toString());
                                    }
                                    pack.setSpecifications(cell4.toString());
                                    if (StringUtils.isBlank(cell5.toString())) {
                                        eim.addMessage(i + 1, 7, "单位不能为空");
                                    } else {
                                        pack.setUnit(cell5.toString());
                                    }

                                    if (StringUtils.isBlank(cell6.toString())) {
                                        eim.addMessage(i + 1, 8, "数量不能为空");
                                    } else if (cell6.getCellType() != CellType.NUMERIC) {
                                        eim.addMessage(i + 1, 8, "数量只能是数字");
                                    } else {
                                        pack.setQuantity(cell6.getNumericCellValue());
                                    }
                                    pack.setRemarks(cell7.toString());
                                    packagingList.add(pack);
                                }
                            }
                        }
                    }
                    if (fa == -1) {
                        eim.addMessage(code3 + "工作表\"包装材料\"标题不存在");
                    }
                    if (fb == -1) {
                        eim.addMessage(code3 + "工作表\"包装要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("bcTotalWeight")) {
                        eim.addMessage(code3 + "工作表\"包材重量\"标题不存在");
                    }
                    if (!titleMap.containsKey("productType")) {
                        eim.addMessage(code3 + "工作表\"产品类别\"标题不存在");
                    }
                    if (!titleMap.containsKey("consumerCode")) {
                        eim.addMessage(code3 + "工作表\"客户代码\"标题不存在");
                    }
                    if (!titleMap.containsKey("rollDiameter")) {
                        eim.addMessage(code3 + "工作表\"卷径\"标题不存在");
                    }
                    if (!titleMap.containsKey("palletLength")) {
                        eim.addMessage(code3 + "工作表\"托长\"标题不存在");
                    }
                    if (!titleMap.containsKey("palletWidth")) {
                        eim.addMessage(code3 + "工作表\"托宽\"标题不存在");
                    }
                    if (!titleMap.containsKey("rollsPerPallet")) {
                        eim.addMessage(code3 + "工作表\"部件数量\"标题不存在");
                    }
                    if (!titleMap.containsKey("palletHeight")) {
                        eim.addMessage(code3 + "工作表\"托高\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_suliaomo")) {
                        eim.addMessage(code3 + "工作表\"塑料膜要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_baifang")) {
                        eim.addMessage(code3 + "工作表\"摆放要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_dabaodai")) {
                        eim.addMessage(code3 + "工作表\"打包带要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_biaoqian")) {
                        eim.addMessage(code3 + "工作表\"标签要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_xiaobiaoqian")) {
                        eim.addMessage(code3 + "工作表\"小标签要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_juanbiaoqian")) {
                        eim.addMessage(code3 + "工作表\"卷（箱）标签要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_tuobiaoqian")) {
                        eim.addMessage(code3 + "工作表\"托标签要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_chanrao")) {
                        eim.addMessage(code3 + "工作表\"缠绕要求\"标题不存在");
                    }
                    if (!titleMap.containsKey("requirement_other")) {
                        eim.addMessage(code3 + "工作表\"其他要求\"标题不存在");
                    }
                    map.put("packagingList", packagingList);
                }
            }//end for sheet的循环
            if (!eim.hasError()) {
                //把数据放入非套材包材BOM
                for (Map<String, Object> map : data) {
                    String code = MapUtils.getAsString(map, "code3");
                    String name = MapUtils.getAsString(map, "name3");
                    String version = MapUtils.getAsString(map, "version");
                    String productType = MapUtils.getAsString(map, "productType");
                    String consumerCode = MapUtils.getAsString(map, "consumerCode");
                    String bcTotalWeight = MapUtils.getAsString(map, "bcTotalWeight");
                    String rollDiameter = MapUtils.getAsString(map, "rollDiameter");
                    String palletLength = MapUtils.getAsString(map, "palletLength");
                    String palletWidth = MapUtils.getAsString(map, "palletWidth");
                    String rollsPerPallet = MapUtils.getAsString(map, "rollsPerPallet");
                    String palletHeight = MapUtils.getAsString(map, "palletHeight");
                    String requirement_suliaomo = MapUtils.getAsString(map, "requirement_suliaomo");
                    String requirement_baifang = MapUtils.getAsString(map, "requirement_baifang");
                    String requirement_dabaodai = MapUtils.getAsString(map, "requirement_dabaodai");
                    String requirement_biaoqian = MapUtils.getAsString(map, "requirement_biaoqian");
                    String requirement_xiaobiaoqian = MapUtils.getAsString(map, "requirement_xiaobiaoqian");
                    String requirement_juanbiaoqian = MapUtils.getAsString(map, "requirement_juanbiaoqian");
                    String requirement_tuobiaoqian = MapUtils.getAsString(map, "requirement_tuobiaoqian");
                    String requirement_chanrao = MapUtils.getAsString(map, "requirement_chanrao");
                    String requirement_other = MapUtils.getAsString(map, "requirement_other");
                    Map<String, Object> param = new HashMap<>();
                    param.put("code", code);
                    param.put("name", name);
                    param.put("pid", ftcBcBom.getId());
                    List<FtcBcBom> fbbL = ftcBcBomDao.findListByMap(FtcBcBom.class, param);
                    boolean ftcBcBom3Exists = false;
                    FtcBcBom ftcBcBom3 = null;
                    for (FtcBcBom ftcBcBom_3 : fbbL) {
                        if (StringUtils.equals(ftcBcBom_3.getCode(), code)) {
                            ftcBcBom3 = ftcBcBom_3;
                            ftcBcBom3Exists = true;
                            break;
                        }
                    }
                    //3级BOM不存在就新建
                    if (!ftcBcBom3Exists) {
                        ftcBcBom3 = new FtcBcBom();
                        ftcBcBom3.setCode(code);
                        ftcBcBom3.setName(name);
                        ftcBcBom3.setLevel(3);
                        ftcBcBom3.setPid(ftcBcBom.getId());
                        ftcBcBomDao.save(ftcBcBom3);
                    }
                    param.clear();
                    param.put("bid", ftcBcBom3.getId());
                    List<FtcBcBomVersion> fbbvL = findListByMap(FtcBcBomVersion.class, param);
                    boolean ftcBcBomVersionExists = false;
                    boolean ftcBcBomVersionNotPass = false;
                    for (FtcBcBomVersion ftcBcBomVersion : fbbvL) {
                        if (StringUtils.equals(ftcBcBomVersion.getVersion(), version)) {
                            ftcBcBomVersionExists = true;
                        }
                        if (ftcBcBomVersion.getAuditState() < 2) {
                            ftcBcBomVersionNotPass = true;
                        }
                    }
                    //没有重复版本即可添加新版本
                    if (!ftcBcBomVersionExists && !ftcBcBomVersionNotPass) {
                        //添加非套材包材版本
                        FtcBcBomVersion ftcBcBomVersion = new FtcBcBomVersion();
                        ftcBcBomVersion.setBid(ftcBcBom3.getId());
                        ftcBcBomVersion.setVersion(version);
                        ftcBcBomVersion.setProductType(productType);
                        param.clear();
                        param.put("consumerCode", consumerCode);
                        Consumer c = ftcBcBomDao.findUniqueByMap(Consumer.class, param);
                        ftcBcBomVersion.setConsumerId(c.getId());
                        ftcBcBomVersion.setBcTotalWeight(bcTotalWeight);
                        ftcBcBomVersion.setRollDiameter(rollDiameter);
                        ftcBcBomVersion.setPalletLength(palletLength);
                        ftcBcBomVersion.setPalletWidth(palletWidth);
                        ftcBcBomVersion.setRollsPerPallet(rollsPerPallet);
                        ftcBcBomVersion.setPalletHeight(palletHeight);
                        ftcBcBomVersion.setRequirement_suliaomo(requirement_suliaomo);
                        ftcBcBomVersion.setRequirement_baifang(requirement_baifang);
                        ftcBcBomVersion.setRequirement_dabaodai(requirement_dabaodai);
                        ftcBcBomVersion.setRequirement_biaoqian(requirement_biaoqian);
                        ftcBcBomVersion.setRequirement_xiaobiaoqian(requirement_xiaobiaoqian);
                        ftcBcBomVersion.setRequirement_juanbiaoqian(requirement_juanbiaoqian);
                        ftcBcBomVersion.setRequirement_tuobiaoqian(requirement_tuobiaoqian);
                        ftcBcBomVersion.setRequirement_chanrao(requirement_chanrao);
                        ftcBcBomVersion.setRequirement_other(requirement_other);
                        ftcBcBomDao.save(ftcBcBomVersion);
                        //添加非套材包材版本明细
                        @SuppressWarnings("unchecked")
                        List<Packaging> packagingList = (List<Packaging>) map.get("packagingList");
                        for (Packaging packaging : packagingList) {
                            FtcBcBomVersionDetail vd = new FtcBcBomVersionDetail();
                            vd.setPackVersionId(ftcBcBomVersion.getId());
                            vd.setPackMaterialCode(packaging.getMaterialCode());
                            vd.setPackStandardCode(packaging.getStandardCode());
                            vd.setPackMaterialName(packaging.getMaterialName());
                            vd.setPackMaterialModel(packaging.getSpecifications());
                            vd.setPackUnit(packaging.getUnit());
                            vd.setPackMaterialCount(packaging.getQuantity());
                            vd.setPackMemo(packaging.getRemarks());
                            ftcBcBomDao.save(vd);
                        }
                    }
                }
            } else {
                //手动回滚事物
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        return eim;
    }

    public void savePdfFile(FtcBcBom ftcBcBom, Long fileId, ExcelImportMessage eim) throws Exception {
        if (ftcBcBom.getId() != null && fileId != null) {
            Attachment att = ftcBcBomDao.findById(Attachment.class, fileId);
            String filePath = att.getFilePath();
            // 读取文件
            InputStream is = new FileInputStream(filePath);
            Workbook wb = WorkbookFactory.create(is);
            List<Map<String, Object>> data = new ArrayList<>();
            Sheet sheet1 = wb.getSheetAt(0);
            List<String> versions = new ArrayList<>();
            int fi = -1;
            for (int i = 0; i < sheet1.getLastRowNum(); i++) {
                Row row = sheet1.getRow(i);
                if (row != null && "文件说明".equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {
                    fi = i + 1;
                    continue;
                }
                if (fi != -1 && fi < i) {
                    if (row != null) {
                        boolean rowNotBlank = false;
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            if (StringUtils.isNotBlank(cell.toString())) {
                                rowNotBlank = true;
                            }
                        }
                        if (rowNotBlank) {
                            String str1 = row.getCell(12, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                            String str2 = row.getCell(15, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                            String str3 = row.getCell(17, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                            Map<String, Object> map = new HashMap<>();
                            if (StringUtils.isBlank(str1)) {
                                eim.addMessage(i + 1, 13, sheet1.getSheetName() + "工作表\"产品包装名称\"不能为空");
                            } else {
                                map.put("name3", str1);
                            }

                            if (StringUtils.isBlank(str2)) {
                                eim.addMessage(i + 1, 16, sheet1.getSheetName() + "工作表\"三级包装工艺代码\"不能为空");
                            } else {
                                map.put("code3", str2);
                            }

                            if (StringUtils.isBlank(str3)) {
                                eim.addMessage(i + 1, 18, sheet1.getSheetName() + "工作表\"版本\"不能为空");
                            } else {
                                map.put("version", str3);
                                versions.add(str3);
                            }
                            data.add(map);
                        } else {
                            break;
                        }
                    }
                }
            }
            //String msg = ConvertPDFUtils.convert("FTCBC", ftcBcBom.getCode(), "", att.getFilePath());
            String msg = "";
            List<String> paths = new ArrayList<>();
            try {
                com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook(filePath);
                for (int i = 1; i < workbook.getWorksheets().getCount(); i++) {
                    workbook.getWorksheets().get(i).setVisible(false);
                }
                File file = new File(filePath);
                for (int j = 0; j < workbook.getWorksheets().getCount(); j++) {
                    String path = "";
                    Worksheet ws = workbook.getWorksheets().get(j);
                    if (j == 0) {
                        path = file.getParent() + File.separator + ftcBcBom.getCode() + "_" + ws.getName() + ".pdf";
                    } else {
                        String version = versions.get(j - 1);
                        path = file.getParent() + File.separator + ftcBcBom.getCode() + "_" + version + "_" + ws.getName() + "(" + version + ").pdf";
                    }
                    workbook.save(path);
                    path = path.substring(path.lastIndexOf("upload"));
                    path = path.replace("upload", "\\upload\\");
                    paths.add(path);
                    if (j < workbook.getWorksheets().getCount() - 1) {
                        workbook.getWorksheets().get(j + 1).setVisible(true);
                        workbook.getWorksheets().get(j).setVisible(false);
                    }
                }
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
                msg = "ERROR";
            }

            if (msg.contains("ERROR")) {
                eim.addMessage("pdf转换失败");
            } else {
//				String[] paths = msg.split("@");
//				Map<String, Object> param = new HashMap<>();
//				param.put("bomName", ftcBcBom.getName());
//				param.put("bcBomCode", ftcBcBom.getCode());
//				ftcBcBomDao.delete(BomFile.class, param);
//				BomFile bomFile = new BomFile();
//				bomFile.setBomName(ftcBcBom.getName());
//				bomFile.setBcBomCode(ftcBcBom.getCode());
//				bomFile.setBcbomVersion("");
//				bomFile.setFileName(att.getFileName());
//				bomFile.setFilePath(att.getFilePath());
//				ftcBcBomDao.save(bomFile);
//
//				BomFilePdf bomFilePdf = new BomFilePdf();
//				bomFilePdf.setBomFileId(bomFile.getId());
//				bomFilePdf.setPDFfilePath(paths[0]);
//				ftcBcBomDao.save(bomFilePdf);
//
//				for(int i=0;i<data.size();i++){
//					param.clear();
//					param.put("bomName", MapUtils.getAsString(data.get(i), "name3"));
//					param.put("bcBomCode", MapUtils.getAsString(data.get(i), "code3"));
//					param.put("bcbomVersion", MapUtils.getAsString(data.get(i), "version"));
//					ftcBcBomDao.delete(BomFile.class, param);
//					bomFile = new BomFile();
//					bomFile.setBomName(MapUtils.getAsString(data.get(i), "name3"));
//					bomFile.setBcBomCode(MapUtils.getAsString(data.get(i), "code3"));
//					bomFile.setBcbomVersion(MapUtils.getAsString(data.get(i), "version"));
//					bomFile.setFileName(att.getFileName());
//					bomFile.setFilePath(att.getFilePath());
//					ftcBcBomDao.save(bomFile);
//
//					bomFilePdf = new BomFilePdf();
//					bomFilePdf.setBomFileId(bomFile.getId());
//					bomFilePdf.setPDFfilePath(paths[i+1]);
//					ftcBcBomDao.save(bomFilePdf);
//				}
//			}
                Map<String, Object> param = new HashMap<>();
                param.put("bomName", ftcBcBom.getName());
                param.put("bcBomCode", ftcBcBom.getCode());
                List<BomFile> list = ftcBcBomDao.findListByMap(BomFile.class, param);
                BomFile bomFile;
                if (list.isEmpty()) {
                    bomFile = new BomFile();
                    bomFile.setBomName(ftcBcBom.getName());
                    bomFile.setBcBomCode(ftcBcBom.getCode());
                    bomFile.setBcbomVersion(String.join("_", versions));
                    bomFile.setFileName(att.getFileName());
                    bomFile.setFilePath(att.getFilePath());
                    bomFile.setUploadTime(new Date());
                    ftcBcBomDao.save(bomFile);
                } else {
                    bomFile = list.get(0);
                    bomFile.setBomName(ftcBcBom.getName());
                    bomFile.setBcBomCode(ftcBcBom.getCode());
                    bomFile.setBcbomVersion(String.join("_", versions));
                    bomFile.setFileName(att.getFileName());
                    bomFile.setFilePath(att.getFilePath());
                    bomFile.setUploadTime(new Date());
                    ftcBcBomDao.update(bomFile);
                }
                param.clear();
                param.put("bomFileId", bomFile.getId());
                ftcBcBomDao.delete(BomFilePdf.class, param);
                com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook(filePath);
                for (int i = 0; i < workbook.getWorksheets().getCount(); i++) {
                    if (workbook.getWorksheets().get(i).isVisible()) {
                        BomFilePdf bomFilePdf = new BomFilePdf();
                        bomFilePdf.setBomFileId(bomFile.getId());
                        bomFilePdf.setPDFfilePath(paths.get(i));
                        ftcBcBomDao.save(bomFilePdf);
                    }
                }
            }
        }
    }

    /**
     * 包装材料
     */
    private class Packaging {

        /**
         * 物料代码
         */
        private String materialCode;

        /**
         * 标准码
         */
        private String standardCode;
        /**
         * 材料名称
         */
        private String materialName;
        /**
         * 规格
         */
        private String specifications;
        /**
         * 单位
         */
        private String unit;
        /**
         * 数量
         */
        private Double quantity;
        /**
         * 备注
         */
        private String remarks;

        public String getMaterialCode() {
            return materialCode;
        }

        public void setMaterialCode(String materialCode) {
            this.materialCode = materialCode;
        }

        public String getStandardCode() {
            return standardCode;
        }

        public void setStandardCode(String standardCode) {
            this.standardCode = standardCode;
        }

        public String getMaterialName() {
            return materialName;
        }

        public void setMaterialName(String materialName) {
            this.materialName = materialName;
        }

        public String getSpecifications() {
            return specifications;
        }

        public void setSpecifications(String specifications) {
            this.specifications = specifications;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public Double getQuantity() {
            return quantity;
        }

        public void setQuantity(Double quantity) {
            this.quantity = quantity;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}
