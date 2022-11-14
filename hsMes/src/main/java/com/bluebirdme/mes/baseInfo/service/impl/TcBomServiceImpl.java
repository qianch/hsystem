/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service.impl;

import com.aspose.cells.Worksheet;
import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.baseInfo.dao.ITcBomDao;
import com.bluebirdme.mes.baseInfo.dao.ITcBomVersionDao;
import com.bluebirdme.mes.baseInfo.dao.ITcBomVersionPartsDao;
import com.bluebirdme.mes.baseInfo.entity.*;
import com.bluebirdme.mes.baseInfo.service.ITcBomService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.planner.cut.entity.PlanUsers;
import com.bluebirdme.mes.platform.entity.Attachment;
import com.bluebirdme.mes.platform.entity.Log;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.service.ILogService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.entity.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.xdemo.superutil.j2se.MapUtils;
import org.xdemo.superutil.j2se.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @author 肖文彬
 * @Date 2016-10-9 9:19:51
 */
@Service
@AnyExceptionRollback
public class TcBomServiceImpl extends BaseServiceImpl implements ITcBomService {
    private static final Logger logger = LoggerFactory.getLogger(TcBomServiceImpl.class);

    @Resource
    ITcBomDao tcBomDao;
    @Resource
    ITcBomVersionDao tcBomVersionDao;
    @Resource
    ITcBomVersionPartsDao tcBomVersionPartsDao;
    @Resource
    ILogService logService;

    @Override
    protected IBaseDao getBaseDao() {
        return tcBomDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return tcBomDao.findPageInfo(filter, page);
    }

    public void delete(String ids) {
        tcBomDao.delete(ids);
    }

    @Override
    public List<Map<String, Object>> findBom(String data, String state) throws SQLTemplateException {
        List<Map<String, Object>> listMap = tcBomDao.findBom(data, state);
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<>();
        for (Map<String, Object> map : listMap) {
            map.put("status", "1");
            ret = new HashMap<>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "TCPROCBOMNAME") + "/" + MapUtils.getAsString(map, "TCPROCBOMCODE"));
            ret.put("state", "closed");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    @Override
    public List<Map<String, Object>> getTcBomJsonTest(String data, String state) throws SQLTemplateException {
        List<Map<String, Object>> listMap = tcBomDao.getTcBomJsonTest(data, state);
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<>();
        for (Map<String, Object> map : listMap) {
            map.put("status", "1");
            ret = new HashMap<String, Object>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "TCPROCBOMNAME") + "/" + MapUtils.getAsString(map, "TCPROCBOMCODE"));
            ret.put("state", "closed");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    @Override
    public List<Map<String, Object>> getTcBomJsonTest1(String data, String state) throws SQLTemplateException {
        List<Map<String, Object>> listMap = tcBomDao.getTcBomJsonTest1(data, state);
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<>();
        for (Map<String, Object> map : listMap) {
            map.put("status", "1");
            ret = new HashMap<>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "TCPROCBOMNAME") + "/" + MapUtils.getAsString(map, "TCPROCBOMCODE"));
            ret.put("state", "closed");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    @Override
    public List<Map<String, Object>> findV(String id) {
        List<Map<String, Object>> listMap = tcBomDao.findV(id);
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<>();
        for (Map<String, Object> map : listMap) {
            ret = new HashMap<>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "tcProcBomVersionCode".toUpperCase()));
            map.put("status", "2");
            ret.put("state", "closed");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    @Override
    public List<Map<String, Object>> findP(String id) {
        List<Map<String, Object>> listMap = tcBomDao.findP(id);
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<>();
        for (Map<String, Object> map : listMap) {
            map.put("status", "3");
            map.put("vId", id);
            ret = new HashMap<>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "TCPROCBOMVERSIONPARTSNAME"));
            ret.put("state", "closed");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    @Override
    public void deleteP(String ids) {
        fakeDelete(TcBomVersionParts.class, ids);
    }

    public Set<Long> deleteParts(List<Long> list, Set<Long> result) {
        List<TcBomVersionParts> list1 = tcBomDao.findAP(list);
        List<Long> parts = new ArrayList<>();

        for (TcBomVersionParts tcBomVersionParts : list1) {
            parts.add(tcBomVersionParts.getId());
        }
        result.addAll(parts);
        if (list1.size() != 0) {
            this.deleteParts(parts, result);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> findPC(String id, String vId) {
        List<Map<String, Object>> listMap = tcBomDao.findPC(id);
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<>();
        for (Map<String, Object> map : listMap) {
            map.put("status", "3");
            map.put("vId", vId);
            ret = new HashMap<>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "TCPROCBOMVERSIONPARTSNAME"));
            ret.put("state", "closed");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    @Override
    public void toCopy(String ids) throws Exception {
        TcBomVersion tcBomVersion = tcBomDao.findById(TcBomVersion.class, Long.valueOf(ids));
        TcBomVersion tcBomV = new TcBomVersion();
        ObjectUtils.clone(tcBomVersion, tcBomV);
        tcBomV.setTcProcBomVersionCode(tcBomVersion.getTcProcBomVersionCode() + "(复制)");
        tcBomV.setTcConsumerVersionCode(tcBomVersion.getTcConsumerVersionCode() + "(复制)");
        tcBomV.setAuditState(0);
        if (tcBomVersion.getTcProcBomVersionDefault() == 1) {
            tcBomVersion.setTcProcBomVersionDefault(-1);
        }
        tcBomVersionDao.save(tcBomV);
        List<TcBomVersionParts> list = tcBomDao.findPP(tcBomVersion.getId());

        ArrayList<Long> list1 = new ArrayList<>();
        HashMap map1 = new HashMap();
        for (TcBomVersionParts parts : list) {
            TcBomVersionParts tcBomP = new TcBomVersionParts();
            ObjectUtils.clone(parts, tcBomP);
            tcBomP.setTcProcBomVersoinId(tcBomV.getId());
            tcBomVersionPartsDao.save(tcBomP);
            savePartsD(tcBomP.getId(), parts.getId());
            list1.add(parts.getId());
            map1.put(parts.getId(), tcBomP.getId());
        }
        if (list.size() != 0) {
            this.saveParts(list1, map1, tcBomV);
        }

    }

    public void saveParts(ArrayList<Long> list1, Map<Long, Long> map1, TcBomVersion tcBomV) throws Exception {
        Map<Long, Long> map = new HashMap<>();
        ArrayList<Long> list = new ArrayList<>();
        List<TcBomVersionParts> listP = tcBomDao.findParts(list1);
        for (TcBomVersionParts tcBomVersionParts : listP) {
            TcBomVersionParts tcBomPN = new TcBomVersionParts();
            ObjectUtils.clone(tcBomVersionParts, tcBomPN);
            tcBomPN.setTcProcBomVersionParentParts(map1.get(tcBomVersionParts.getTcProcBomVersionParentParts()));
            tcBomPN.setTcProcBomVersoinId(tcBomV.getId());
            tcBomVersionPartsDao.save(tcBomPN);
            savePartsD(tcBomPN.getId(), tcBomVersionParts.getId());
            list.add(tcBomVersionParts.getId());
            map.put(tcBomVersionParts.getId(), tcBomPN.getId());
        }
        if (listP.size() != 0) {
            this.saveParts(list, map, tcBomV);
        }
    }

    public void savePartsD(Long id, Long partsId) throws Exception {
        List<TcBomVersionPartsDetail> listD = tcBomDao.findPartsDetais(partsId);
        if (listD.size() != 0) {
            for (TcBomVersionPartsDetail mapD : listD) {
                TcBomVersionPartsDetail detail = new TcBomVersionPartsDetail();
                ObjectUtils.clone(mapD, detail);
                detail.setTcProcBomPartsId(id);
                if (detail.getSorting() == null) {
                    detail.setSorting(0);
                }
                tcBomVersionPartsDao.save(detail);
            }
        }

        List<TcBomVersionPartsFinishedWeightEmbryoCloth> listF = tcBomDao.findPartsFinishedWeightEmbryoCloths(partsId);
        if (listF.size() != 0) {
            for (TcBomVersionPartsFinishedWeightEmbryoCloth mapD : listF) {
                TcBomVersionPartsFinishedWeightEmbryoCloth embryoCloth = new TcBomVersionPartsFinishedWeightEmbryoCloth();
                ObjectUtils.clone(mapD, embryoCloth);
                embryoCloth.setTcProcBomPartsId(id);
                tcBomVersionPartsDao.save(embryoCloth);
            }
        }
    }

    @Override
    public void deleteB(String id) {
        tcBomDao.delete(TcBom.class, id);
        tcBomDao.deleteV(id);
        tcBomDao.deleteAP();
        tcBomDao.deleteAPD();
    }

    @Override
    public void deleteV(String id) {
        tcBomDao.delete(TcBomVersion.class, id);
        tcBomDao.deleteAP();
        tcBomDao.deleteAPD();
    }

    @Override
    public List<Map<String, Object>> find1(String id) {
        List<Map<String, Object>> tcBomVersion = tcBomDao.findP(id);
        Map<String, Object> ret;
        List<Map<String, Object>> listMap1 = new ArrayList<>();
        for (Map<String, Object> map : tcBomVersion) {
            ret = new HashMap<>();
            ret.put("id", MapUtils.getAsString(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "TCPROCBOMVERSIONPARTSNAME"));
            // 裁剪计划中查询部件所对应的人员并封装到tree
            Map<String, Object> map2 = new HashMap<>();
            map2.put("tcBomPartId", Long.valueOf(MapUtils.getAsString(map, "ID")));
            List<PlanUsers> planUsers = tcBomDao.findListByMap(PlanUsers.class, map2);
            StringBuilder userId = new StringBuilder();
            StringBuilder userName = new StringBuilder();
            User user;
            for (int a = 0; a < planUsers.size(); a++) {
                user = tcBomDao.findById(User.class, planUsers.get(a).getUserId());
                userName.append(user.getUserName()).append(a == planUsers.size() - 1 ? "" : ",");
                userId.append(planUsers.get(a).getUserId()).append(a == planUsers.size() - 1 ? "" : ",");
            }
            long level = 1L;
            ret.put("level", level);
            ret.put("userId", userId.toString());
            ret.put("userName", userName.toString());
            ret.put("children", children(MapUtils.getAsLong(map, "ID"), ++level));
            listMap1.add(ret);
        }
        return listMap1;
    }

    private List<Map<String, Object>> children(Long id, Long level) {
        List<Map<String, Object>> listMap2 = new ArrayList<>();
        Map<String, Object> ret;
        List<Map<String, Object>> list = tcBomDao.findPC(String.valueOf(id));
        if (list.size() == 0) {
            for (Map<String, Object> map : list) {
                ret = new HashMap<>();
                ret.put("id", MapUtils.getAsString(map, "ID"));
                ret.put("text", MapUtils.getAsString(map, "TCPROCBOMVERSIONPARTSNAME"));
                ret.put("level", level);
                listMap2.add(ret);
            }
        } else {
            for (Map<String, Object> map : list) {
                ret = new HashMap<>();
                ret.put("id", MapUtils.getAsString(map, "ID"));
                ret.put("text", MapUtils.getAsString(map, "TCPROCBOMVERSIONPARTSNAME"));
                Map<String, Object> mm = new HashMap<String, Object>();
                mm.put("tcBomPartId", Long.valueOf(MapUtils.getAsString(map, "ID")));
                List<PlanUsers> planUsers = tcBomDao.findListByMap(PlanUsers.class, mm);
                StringBuilder userId = new StringBuilder();
                StringBuilder userName = new StringBuilder();
                User user;
                for (int a = 0; a < planUsers.size(); a++) {
                    user = tcBomDao.findById(User.class, planUsers.get(a).getUserId());
                    userName.append(user.getUserName()).append(a == planUsers.size() - 1 ? "" : ",");
                    userId.append(planUsers.get(a).getUserId()).append(a == planUsers.size() - 1 ? "" : ",");
                }
                ret.put("level", level);
                ret.put("userId", userId.toString());
                ret.put("userName", userName.toString());
                ret.put("children", children(MapUtils.getAsLong(map, "ID"), ++level));
                listMap2.add(ret);
            }
        }
        return listMap2;
    }

    @Override
    public Map<String, Object> findFtc(Filter filter, Page page) {
        return tcBomDao.findFtc(filter, page);
    }

    @Override
    public List<Map<String, Object>> findConsumer(String id) {
        return tcBomDao.findConsumer(id);
    }

    @Override
    public void toCopyBom(String id) throws Exception {
        TcBom tb = tcBomDao.findById(TcBom.class, Long.parseLong(id));
        TcBom newbom = new TcBom();
        ObjectUtils.clone(tb, newbom);
        newbom.setTcProcBomName(tb.getTcProcBomName() + "(复制)");
        tcBomDao.save(newbom);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("tcProcBomId", tb.getId());
        List<TcBomVersion> tbvList = tcBomDao.findListByMap(TcBomVersion.class, map);
        for (TcBomVersion tcBomVersion : tbvList) {
            TcBomVersion tcBomV = new TcBomVersion();
            ObjectUtils.clone(tcBomVersion, tcBomV);
            tcBomV.setTcProcBomId(newbom.getId());
            tcBomV.setAuditState(0);
            if (tcBomVersion.getTcProcBomVersionDefault() == 1) {
                tcBomVersion.setTcProcBomVersionDefault(-1);
            }
            tcBomVersionDao.save(tcBomV);
            List<TcBomVersionParts> list = tcBomDao.findPP(tcBomVersion.getId());
            ArrayList<Long> list1 = new ArrayList<>();
            Map<Long, Long> map1 = new HashMap<>();
            for (TcBomVersionParts parts : list) {
                parts.setIsDeleted(0);
                parts.setNeedSort(1);
                TcBomVersionParts tcBomP = new TcBomVersionParts();
                ObjectUtils.clone(parts, tcBomP);
                tcBomP.setTcProcBomVersoinId(tcBomV.getId());
                tcBomVersionPartsDao.save(tcBomP);
                savePartsD(tcBomP.getId(), parts.getId());
                list1.add(parts.getId());
                map1.put(parts.getId(), tcBomP.getId());
            }
            if (list.size() != 0) {
                this.saveParts(list1, map1, tcBomV);
            }
        }
    }

    @Override
    public ExcelImportMessage doAddTcBom(TcBom tcBom, TcBomVersion tcBomVersion, Long fileId) throws Exception {
        tcBomDao.save(tcBom);
        Consumer consumer = tcBomDao.findById(Consumer.class, tcBom.getTcProcBomConsumerId().longValue());
        ExcelImportMessage eim = new ExcelImportMessage();
        if (tcBom.getId() != null) {
            String tpbvc = tcBomVersion.getTcProcBomVersionCode();
            if (StringUtils.isNotBlank(tpbvc)) {
                //添加版本号
                tcBomVersion.setTcProcBomId(tcBom.getId());
                tcBomVersion.setTcProcBomVersionDefault(1);
                tcBomVersion.setTcProcBomVersionEnabled(1);
                tcBomVersion.setAuditState(AuditConstant.RS.SUBMIT);
                if (fileId != null) {
                    tcBomVersion.setAttachmentId(fileId.intValue());
                }
                tcBomVersionDao.save(tcBomVersion);
                //判断有Excel文件要解析？
                if (fileId != null) {
                    Attachment att = tcBomDao.findById(Attachment.class, fileId);
                    String filePath = att.getFilePath();
                    // 读取文件
                    InputStream is = new FileInputStream(filePath);
                    Workbook wb = WorkbookFactory.create(is);
                    List<PartsDesc> data = new ArrayList<>();
                    //从第一个sheet中读取部件名称、判断部件类型、读取工艺代码
                    Sheet sheet1 = wb.getSheetAt(0);
                    //部件物料号、部件物料名称、子工艺代码所在的行号
                    int fi = -1;
                    for (int i = 0; i < sheet1.getLastRowNum(); i++) {
                        Row row = sheet1.getRow(i);
                        if (row != null && "文件说明".equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {
                            fi = i + 3;
                            continue;
                        }
                        if (fi != -1 && fi < i) {
                            if (row != null) {
                                PartsDesc pdesc = new PartsDesc();
                                String str1 = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                                String custommerMaterialCode = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                                String str2 = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                                String str3 = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                                //套重量
                                Cell cell4 = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                if (StringUtils.isNotBlank(custommerMaterialCode) || StringUtils.isNotBlank(str1) || StringUtils.isNotBlank(str2) || StringUtils.isNotBlank(str3) || StringUtils.isNotBlank(cell4.toString())) {
                                    if (StringUtils.isBlank(str1)) {
                                        eim.addMessage(i + 1, 1, "部件物料号不能为空");
                                    } else {
                                        pdesc.setMaterialNumber(str1);
                                    }

                                    if (!StringUtils.isBlank(custommerMaterialCode)) {
                                        pdesc.setCustomerMaterialCode(custommerMaterialCode);
                                    }

                                    if (StringUtils.isBlank(str2)) {
                                        eim.addMessage(i + 1, 3, "部件物料名称不能为空");
                                    } else if (str2.split("\\s+").length == 2) {
                                        String[] ss = str2.split("\\s+");
                                        if (StringUtils.isNotBlank(ss[0]) && StringUtils.isNotBlank(ss[1])) {
                                            pdesc.setPartsName(ss[1]);
                                            if (ss[0].startsWith("TC-CJ")) {
                                                pdesc.setPartsType("常规部件");
                                            } else if (ss[0].startsWith("TC-CP")) {
                                                pdesc.setPartsType("成品胚布");
                                            } else {
                                                eim.addMessage(i + 1, 3, "部件物料名称格式错误");
                                            }
                                        }
                                    } else {
                                        eim.addMessage(i + 1, 3, "部件物料名称格式错误");
                                    }

                                    if (StringUtils.isBlank(str3)) {
                                        eim.addMessage(i + 1, 6, "子工艺代码不能为空");
                                    } else {
                                        pdesc.setPartsCutCode(str3);
                                    }

                                    if (StringUtils.isBlank(cell4.toString())) {
                                        eim.addMessage(i + 1, 7, "部件套重量不能为空");
                                    } else if (cell4.getCellType() != CellType.NUMERIC) {
                                        eim.addMessage(i + 1, 7, "部件套重量只能是数字");
                                    } else {
                                        pdesc.setPartsWeight(cell4.getNumericCellValue());
                                    }

                                    data.add(pdesc);
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                    if (fi == -1) {
                        eim.addMessage("Excel文件格式错误");
                    }
                    //Excel文件中sheet的个数
                    int sheetCount = wb.getNumberOfSheets();
                    for (PartsDesc partsDesc : data) {
                        String partsName = partsDesc.getPartsName();
                        //从第2个sheet开始循环
                        Sheet sheet = null;
                        for (int i = 1; i < sheetCount; i++) {
                            Sheet sheet0 = wb.getSheetAt(i);
                            String sheetName = sheet0.getSheetName();
                            if (StringUtils.equals(partsName, sheetName)) {
                                sheet = sheet0;
                                break;
                            }
                        }
                        if (sheet == null) {
                            eim.addMessage(partsName + "对应的sheet不存在");
                        } else {
                            int fc = -1;    //成品信息(卷/套)所在的行号
                            int fd = -1;    //图纸文件信息所在的行号
                            int fe = -1;    //原材料信息(物料代码)所在的行号
                            int ff = -1;    //成品重量信息所在的行号
                            int fg = -1;    //包装工艺信息所在的行号
                            double total = 0;//成品信息(卷/套)之和
                            List<RawMaterial> rawMaterialList = new ArrayList<>();
                            List<FinishedWeight> finishedWeightList = new ArrayList<>();
                            for (int j = 0; j < sheet.getLastRowNum(); j++) {
                                Row row = sheet.getRow(j);
                                if (row != null && "成品信息".equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {
                                    fc = j + 2;
                                    continue;
                                }
                                if (row != null && "原材料信息".equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {
                                    fe = j + 1;
                                    continue;
                                }
                                //行号j在fc找到"卷/套"之后和在fd找到"图纸文件信息"之前这段区间进行"卷/套"的累加
                                if (fc != -1 && fc < j && fd == -1) {
                                    if (row != null && "图纸文件信息".equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {
                                        fd = j;
                                        continue;
                                    }
                                    if (row != null) {
                                        boolean rowNotBlank = false;
                                        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                                            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                            if (StringUtils.isNotBlank(cell.toString())) {
                                                rowNotBlank = true;
                                            }
                                        }
                                        if (rowNotBlank) {
                                            Cell cell = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                            if (cell.getCellType() == CellType.NUMERIC) {
                                                double d = cell.getNumericCellValue();
                                                total += d;
                                            } else {
                                                eim.addMessage(j + 1, 7, partsName + "工作表\"卷/套\"格式错误");
                                            }
                                        }
                                    }
                                }
                                if (fe == j) {
                                    Cell cell1 = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    Cell cell2 = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    Cell cell3 = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    Cell cell4 = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    Cell cell5 = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    Cell cell6 = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    Cell cell7 = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    Cell cell8 = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    Cell cell9 = row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    if (!StringUtils.equals("图纸文件代码", cell1.toString())) {
                                        eim.addMessage(j + 1, 1, "\"" + partsName + "\"工作表此处标题应为\"图纸文件代码\"");
                                    }
                                    if (!StringUtils.equals("产品名称/层号", cell2.toString())) {
                                        eim.addMessage(j + 1, 2, "\"" + partsName + "\"工作表此处标题应为\"产品名称/层号\"");
                                    }
                                    if (!StringUtils.equals("坯布名称", cell3.toString())) {
                                        eim.addMessage(j + 1, 3, "\"" + partsName + "\"工作表此处标题应为\"坯布名称\"");
                                    }
                                    if (!StringUtils.equals("物料代码", cell4.toString())) {
                                        eim.addMessage(j + 1, 4, "\"" + partsName + "\"工作表此处标题应为\"物料代码\"");
                                    }
                                    if (!StringUtils.equals("工艺代码", cell5.toString())) {
                                        eim.addMessage(j + 1, 6, "\"" + partsName + "\"工作表此处标题应为\"工艺代码\"");
                                    }
                                    if (!StringUtils.equals("门幅", cell6.toString())) {
                                        eim.addMessage(j + 1, 7, "\"" + partsName + "\"工作表此处标题应为\"门幅\"");
                                    }
                                    if (!StringUtils.equals("米长", cell7.toString())) {
                                        eim.addMessage(j + 1, 8, "\"" + partsName + "\"工作表此处标题应为\"米长\"");
                                    }
                                    if (!StringUtils.equals("卷号", cell8.toString())) {
                                        eim.addMessage(j + 1, 9, "\"" + partsName + "\"工作表此处标题应为\"卷号\"");
                                    }

                                    if (!StringUtils.equals("套/卷", cell9.toString())) {
                                        eim.addMessage(j + 1, 10, "\"" + partsName + "\"工作表此处标题应为\"套/卷\"");
                                    }
                                }
                                if (fe != -1 && fe < j && ff == -1) {//行号在原材料信息(物料代码)和成品重量信息之间的数据
                                    if (row != null && "成品重量信息".equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {
                                        ff = j + 1;
                                        continue;
                                    }
                                    if (row != null) {
                                        Cell cell1 = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                        Cell cell2 = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                        Cell cell3 = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                        Cell cell4 = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                        Cell cell5 = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                        Cell cell6 = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                        Cell cell7 = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                        Cell cell8 = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                        Cell cell9 = row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                        if (StringUtils.isNotBlank(cell1.toString()) || StringUtils.isNotBlank(cell2.toString())
                                                || StringUtils.isNotBlank(cell3.toString()) || StringUtils.isNotBlank(cell4.toString())
                                                || StringUtils.isNotBlank(cell5.toString()) || StringUtils.isNotBlank(cell6.toString())
                                                || StringUtils.isNotBlank(cell7.toString()) || StringUtils.isNotBlank(cell8.toString())
                                                || StringUtils.isNotBlank(cell9.toString())) {
                                            RawMaterial rm = new RawMaterial();
                                            FinishedProduct fp = null;
                                            if ("常规部件".equals(partsDesc.getPartsType())) {
                                                //图号必须验证
                                                if (StringUtils.isBlank(cell1.toString())) {
                                                    eim.addMessage(j + 1, 1, partsName + "工作表\"图纸文件代码\"不能为空");
                                                } else {
                                                    rm.setDrawingNo(cell1.toString());
                                                }
                                                //层数
                                                if (StringUtils.isBlank(cell2.toString())) {
                                                    eim.addMessage(j + 1, 2, partsName + "工作表\"产品名称/层号\"不能为空");
                                                } else {
                                                    rm.setLevelNo(cell2.toString());
                                                }
                                                //卷号
                                                if (StringUtils.isBlank(cell8.toString())) {
                                                    eim.addMessage(j + 1, 9, partsName + "工作表\"卷号\"不能为空");
                                                } else {
                                                    rm.setRollNo(cell8.toString());
                                                }
                                            } else if ("成品胚布".equals(partsDesc.getPartsType())) {
                                                //图号
                                                if (StringUtils.isBlank(cell1.toString())) {
                                                    eim.addMessage(j + 1, 1, partsName + "工作表\"图纸文件代码\"不能为空");
                                                } else {
                                                    rm.setDrawingNo(cell1.toString());
                                                }
                                                //层数、卷号必须验证
                                                if (StringUtils.isBlank(cell2.toString())) {
                                                    eim.addMessage(j + 1, 2, partsName + "工作表\"产品名称/层号\"不能为空");
                                                } else {
                                                    rm.setLevelNo(cell2.toString());
                                                }
                                                if (StringUtils.isBlank(cell8.toString())) {
                                                    eim.addMessage(j + 1, 9, partsName + "工作表\"卷号\"不能为空");
                                                } else {
                                                    rm.setRollNo(cell8.toString());
                                                }
                                            }

                                            String materialCode = cell4.toString();
                                            if (StringUtils.isBlank(materialCode)) {
                                                eim.addMessage(j + 1, 4, partsName + "工作表\"物料代码\"不能为空");
                                            } else {
                                                rm.setMaterialCode(materialCode);
                                                Map<String, Object> param = new HashMap<>();
                                                param.put("materielCode", materialCode);                //物料编码
                                                param.put("productConsumerId", consumer.getId());    //成品客户信息ID
                                                param.put("obsolete", null);                            //使用状态		废弃：1,正常：null
                                                List<FinishedProduct> fps = tcBomVersionPartsDao.findListByMap(FinishedProduct.class, param);
                                                if (fps != null && fps.size() > 0) {
                                                    fp = fps.get(0);
                                                } else {
                                                    eim.addMessage(j + 1, 4, partsName + "工作表 在\"成品信息\"中找不到对应的\"物料代码:" + materialCode + "\"和\"客户信息:" + consumer.getId() + "\"");
                                                }
                                            }

                                            if (StringUtils.isBlank(cell3.toString())) {
                                                eim.addMessage(j + 1, 3, partsName + "工作表\"坯布名称\"不能为空");
                                            } else {
                                                String pn = cell3.toString();
                                                if (fp != null && !StringUtils.startsWith(fp.getProductProcessName(), pn)) {
                                                    eim.addMessage(j + 1, 3, partsName + "工作表\"坯布名称\"与\"成品信息\"中不一致");
                                                } else {
                                                    rm.setProcessName(pn);
                                                }
                                            }

                                            if (StringUtils.isBlank(cell5.toString())) {
                                                eim.addMessage(j + 1, 6, partsName + "工作表\"工艺代码\"不能为空");
                                            } else {
                                                String pc = cell5.toString();
                                                if (fp != null && !StringUtils.equals(pc, fp.getProductProcessCode())) {
                                                    eim.addMessage(j + 1, 6, partsName + "工作表\"工艺代码\"与\"成品信息\"中不一致");
                                                    Log log = new Log();
                                                    log.setLogDate(new Date());
                                                    log.setOperate("工艺代码校验");
                                                    log.setLoginName("");
                                                    log.setUserId((long) -999);
                                                    log.setParamsValue("工艺代码:" + pc + ",成品信息:" + fp.getProductProcessCode() + ",成品ID:" + fp.getId());
                                                    logService.save(log);
                                                } else {
                                                    rm.setProcessCode(pc);
                                                }
                                            }

                                            if (StringUtils.isBlank(cell6.toString())) {
                                                eim.addMessage(j + 1, 7, partsName + "工作表\"门幅\"不能为空");
                                            } else if (cell6.getCellType() != CellType.NUMERIC) {
                                                eim.addMessage(j + 1, 7, partsName + "工作表\"门幅\"只能为数字");
                                            } else {
                                                double width = cell6.getNumericCellValue();
                                                if (fp != null && null == fp.getProductWidth()) {
                                                    eim.addMessage(j + 1, 7, partsName + "的物料代码为" + fp.getMaterielCode() + "成品信息中门幅未填");
                                                } else {
                                                    if (fp != null && width != fp.getProductWidth()) {
                                                        eim.addMessage(j + 1, 7, partsName + "工作表\"门幅\"与\"成品信息\"中不一致");
                                                    } else {
                                                        rm.setWidth(width);
                                                    }
                                                }
                                            }

                                            if (StringUtils.isBlank(cell7.toString())) {
                                                eim.addMessage(j + 1, 8, partsName + "工作表\"米长\"不能为空");
                                            } else if (cell7.getCellType() != CellType.NUMERIC) {
                                                eim.addMessage(j + 1, 8, partsName + "工作表\"米长\"只能为数字");
                                            } else {
                                                double length = cell7.getNumericCellValue();
                                                if (fp != null && length != fp.getProductRollLength()) {
                                                    eim.addMessage(j + 1, 8, partsName + "工作表\"米长\"与\"成品信息\"中不一致");
                                                } else {
                                                    rm.setMlength(length);
                                                }
                                            }

                                            if (StringUtils.isBlank(cell9.toString())) {
                                                eim.addMessage(j + 1, 10, partsName + "工作表\"套/卷\"不能为空");
                                            } else if (cell9.getCellType() != CellType.NUMERIC) {
                                                eim.addMessage(j + 1, 10, partsName + "工作表\"套/卷\"只能为数字");
                                            } else {
                                                rm.setJpc(cell9.getNumericCellValue());
                                            }
                                            rawMaterialList.add(rm);
                                        }
                                    }
                                }

                                if (ff != -1 && ff < j && fg == -1) {//行号在成品重量信息(裁片总重物料号)和包装工艺信息之间的数据
                                    if (row != null && "包装工艺信息".equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {
                                        fg = j;
                                        continue;
                                    }
                                    if (row != null) {
                                        Cell cell1 = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                        Cell cell2 = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                        Cell cell3 = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                        if (StringUtils.isNotBlank(cell1.toString()) || StringUtils.isNotBlank(cell2.toString()) || StringUtils.isNotBlank(cell3.toString())) {
                                            FinishedWeight fw = new FinishedWeight();
                                            if (StringUtils.isBlank(cell1.toString())) {
                                                eim.addMessage(j + 1, 1, partsName + "工作表\"裁片总重物料号\"不能为空");
                                            } else {
                                                fw.setCutpartsTotalWeightMaterialNo(cell1.toString());
                                            }

                                            if (StringUtils.isBlank(cell2.toString())) {
                                                eim.addMessage(j + 1, 3, partsName + "工作表\"裁片坯布规格\"不能为空");
                                            } else {
                                                fw.setCutpartsSpec(cell2.toString());
                                            }

                                            if (StringUtils.isBlank(cell3.toString())) {
                                                eim.addMessage(j + 1, 4, partsName + "工作表\"裁片总重\"不能为空");
                                            } else if (cell3.getCellType() != CellType.NUMERIC) {
                                                eim.addMessage(j + 1, 4, partsName + "工作表\"裁片总重\"只能为数字");
                                            } else {
                                                fw.setCutpartsTotalWeight(cell3.getNumericCellValue());
                                            }
                                            finishedWeightList.add(fw);
                                        }
                                    }
                                }

                            }//end for inner
                            if (fc == -1) {
                                eim.addMessage(partsName + "工作表\"成品信息\"标题不存在");
                            }
                            if (fd == -1) {
                                eim.addMessage(partsName + "工作表\"图纸文件信息\"标题不存在");
                            }
                            if (fe == -1) {
                                eim.addMessage(partsName + "工作表\"原材料信息\"标题不存在");
                            }
                            if (ff == -1) {
                                eim.addMessage(partsName + "工作表\"成品重量信息\"标题不存在");
                            }
                            if (fg == -1) {
                                eim.addMessage(partsName + "工作表\"包装工艺信息\"标题不存在");
                            }
                            partsDesc.setPartsSubsCount(total);
                            partsDesc.setRawMaterialList(rawMaterialList);
                            partsDesc.setFinishedWeight(finishedWeightList);

                        }

                    }//end for outer

                    if (!eim.hasError()) {
                        //把数据放入套材BOM版本部件列表
                        for (PartsDesc fd : data) {
                            String partsName = fd.getPartsName();
                            String customerMaterialCode = fd.getCustomerMaterialCode();
                            String partType = fd.getPartsType();
                            String partNo = fd.getMaterialNumber();
                            Double partsWeight = fd.getPartsWeight();
                            String processCode = fd.getPartsCutCode();
                            Double jpt = fd.getPartsSubsCount();

                            TcBomVersionParts detail = new TcBomVersionParts();
                            detail.setIsDeleted(0);
                            detail.setTcProcBomVersoinId(tcBomVersion.getId());//BOM版本ID
                            detail.setTcProcBomVersionPartsName(partsName);//部件名称
                            detail.setCustomerMaterialCode(customerMaterialCode);//客户物料号
                            detail.setTcProcBomVersionPartsType(partType);//部件类型
                            detail.setTcProcBomVersionPartsCutCode(processCode);//部件裁剪包装工艺代码
                            detail.setTcProcBomVersionPartsCount(1d);//部件数量
                            detail.setTcProcBomVersionPartsSubsCount(jpt);//小部件个数
                            detail.setTcProcBomVersionPartsWeight(partsWeight);    //部件重量
                            detail.setTcProcBomVersionMaterialNumber(partNo);    //部件物料号
                            detail.setNeedSort(1);    //小部件是否需要排序
                            tcBomVersionPartsDao.save(detail);

                            //添加部件明细
                            List<RawMaterial> rms = fd.getRawMaterialList();
                            for (int i = 0; i < rms.size(); i++) {
                                RawMaterial rm = rms.get(i);
                                TcBomVersionPartsDetail pd = new TcBomVersionPartsDetail();
                                if (fd.getPartsType().equals("常规部件")) {//常规部件
                                    pd.setDrawingNo(rm.getDrawingNo());//图号
                                    pd.setRollNo(rm.getRollNo());    //卷号
                                    pd.setLevelNo(rm.getLevelNo());//层数
                                } else {    //成品胚布
                                    pd.setDrawingNo(rm.getDrawingNo());//图号
                                    pd.setRollNo(rm.getRollNo());    //卷号
                                    pd.setLevelNo(rm.getLevelNo());//层数
                                }
                                pd.setTcProcBomPartsId(detail.getId());
                                pd.setSorting(i + 1);                //排序号
                                pd.setLength(rm.getMlength());    //米长
                                Double jpc = rm.getJpc();            //套/卷
                                Double cpj = 0D;
                                if (jpc != 0) {
                                    cpj = 1 / jpc;        //倒数
                                }
                                pd.setTcProcBomFabricCount(cpj);//数量(卷/套)


                                Map<String, Object> param = new HashMap<>();
                                param.put("materielCode", rm.getMaterialCode());
                                param.put("obsolete", null);
                                param.put("productConsumerId", tcBom.getTcProcBomConsumerId().longValue());
                                System.out.println(param);
                                List<FinishedProduct> fpList = tcBomVersionPartsDao.findListByMap(FinishedProduct.class, param);
                                if (fpList != null && fpList.size() > 0) {
                                    FinishedProduct fp = fpList.get(0);
                                    pd.setTcFinishedProductId(fp.getId());//成品ID
                                }
                                tcBomVersionPartsDao.save(pd);
                            }//end for inner

                            //添加部件成品重量胚布信息
                            List<FinishedWeight> fws = fd.getFinishedWeight();
                            for (int i = 0; i < fws.size(); i++) {
                                FinishedWeight fw = fws.get(i);
                                TcBomVersionPartsFinishedWeightEmbryoCloth fwec = new TcBomVersionPartsFinishedWeightEmbryoCloth();
                                fwec.setTcProcBomPartsId(detail.getId());
                                fwec.setMaterialNumber(fw.getCutpartsTotalWeightMaterialNo());    //物料代码
                                fwec.setEmbryoClothName(fw.getCutpartsSpec());                    //胚布名称
                                fwec.setWeight(fw.getCutpartsTotalWeight());                    //重量

                                tcBomVersionPartsDao.save(fwec);
                            }//end for inner

                        }//end for outer

                    } else {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();    //手动回滚事物
                    }
                }//end if
            }//end if
        }//end if
        return eim;
    }

    @Override
    public ExcelImportMessage doUpdateTcBomVersion(TcBomVersion tcBomVersion, Long fileId) throws Exception {
        tcBomVersionDao.update2(tcBomVersion);
        TcBom tcBom = tcBomDao.findById(TcBom.class, tcBomVersion.getTcProcBomId());
        Consumer consumer = tcBomDao.findById(Consumer.class, tcBom.getTcProcBomConsumerId().longValue());
        ExcelImportMessage eim = new ExcelImportMessage();

        //判断有Excel文件要解析？
        if (fileId != null) {
            Attachment att = tcBomDao.findById(Attachment.class, fileId);
            String filePath = att.getFilePath();
            // 读取文件
            InputStream is = new FileInputStream(filePath);
            Workbook wb = WorkbookFactory.create(is);
            List<PartsDesc> data = new ArrayList<>();

            //从第一个sheet中读取部件名称、判断部件类型、读取工艺代码
            Sheet sheet1 = wb.getSheetAt(0);

            int fi = -1;    //部件物料号、部件物料名称、子工艺代码所在的行号
            for (int i = 0; i < sheet1.getLastRowNum(); i++) {
                Row row = sheet1.getRow(i);
                if (row != null && "文件说明".equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {
                    fi = i + 3;
                    continue;
                }

                if (fi != -1 && fi < i) {
                    if (row != null) {
                        PartsDesc pdesc = new PartsDesc();
                        String str1 = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                        String custommerMaterialCode = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                        String str2 = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                        String str3 = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) + "";
                        Cell cell4 = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);        //套重量
                        if (StringUtils.isNotBlank(custommerMaterialCode) || StringUtils.isNotBlank(str1) || StringUtils.isNotBlank(str2) || StringUtils.isNotBlank(str3) || StringUtils.isNotBlank(cell4.toString())) {
                            if (StringUtils.isBlank(str1)) {
                                eim.addMessage(i + 1, 1, "部件物料号不能为空");
                            } else {
                                pdesc.setMaterialNumber(str1);
                            }

                            if (!StringUtils.isBlank(custommerMaterialCode)) {
                                pdesc.setCustomerMaterialCode(custommerMaterialCode);
                            }

                            if (StringUtils.isBlank(str2)) {
                                eim.addMessage(i + 1, 3, "部件物料名称不能为空");
                            } else if (str2.split("\\s+").length == 2) {
                                String[] ss = str2.split("\\s+");
                                if (StringUtils.isNotBlank(ss[0]) && StringUtils.isNotBlank(ss[1])) {
                                    pdesc.setPartsName(ss[1]);
                                    if (ss[0].startsWith("TC-CJ")) {
                                        pdesc.setPartsType("常规部件");
                                    } else if (ss[0].startsWith("TC-CP")) {
                                        pdesc.setPartsType("成品胚布");
                                    } else {
                                        eim.addMessage(i + 1, 3, "部件物料名称格式错误");
                                    }

                                }
                            } else {
                                eim.addMessage(i + 1, 3, "部件物料名称格式错误");
                            }

                            if (StringUtils.isBlank(str3)) {
                                eim.addMessage(i + 1, 6, "子工艺代码不能为空");
                            } else {
                                pdesc.setPartsCutCode(str3);
                            }

                            if (StringUtils.isBlank(cell4.toString())) {
                                eim.addMessage(i + 1, 7, "部件套重量不能为空");
                            } else if (cell4.getCellType() != CellType.NUMERIC) {
                                eim.addMessage(i + 1, 7, "部件套重量只能是数字");
                            } else {
                                pdesc.setPartsWeight(cell4.getNumericCellValue());
                            }
                            data.add(pdesc);
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }//end for
            if (fi == -1) {
                eim.addMessage("Excel文件格式错误");
            }

            int sheetCount = wb.getNumberOfSheets();        //Excel文件中sheet的个数
            for (PartsDesc partsDesc : data) {
                String partsName = partsDesc.getPartsName();
                //从第2个sheet开始循环
                Sheet sheet = null;
                for (int i = 1; i < sheetCount; i++) {
                    Sheet sheet0 = wb.getSheetAt(i);
                    String sheetName = sheet0.getSheetName();
                    if (StringUtils.equals(partsName, sheetName)) {
                        sheet = sheet0;
                        break;
                    }
                }//end for inner

                if (sheet == null) {
                    eim.addMessage(partsName + "对应的sheet不存在");
                } else {
                    int fc = -1;    //成品信息(卷/套)所在的行号
                    int fd = -1;    //图纸文件信息所在的行号
                    int fe = -1;    //原材料信息(物料代码)所在的行号
                    int ff = -1;    //成品重量信息所在的行号
                    int fg = -1;    //包装工艺信息所在的行号
                    double total = 0;//成品信息(卷/套)之和
                    List<RawMaterial> rawMaterialList = new ArrayList<>();
                    List<FinishedWeight> finishedWeightList = new ArrayList<>();
                    for (int j = 0; j < sheet.getLastRowNum(); j++) {
                        Row row = sheet.getRow(j);
                        if (row != null && "成品信息".equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {
                            fc = j + 2;
                            continue;
                        }

                        if (row != null && "原材料信息".equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {
                            fe = j + 1;
                            continue;
                        }

                        if (fc != -1 && fc < j && fd == -1) {//行号j在fc找到"卷/套"之后和在fd找到"图纸文件信息"之前这段区间进行"卷/套"的累加
                            if (row != null && "图纸文件信息".equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {
                                fd = j;
                                continue;
                            }
                            if (row != null) {
                                boolean rowNotBlank = false;
                                for (int i = 0; i < row.getLastCellNum(); i++) {
                                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    if (StringUtils.isNotBlank(cell.toString())) {
                                        rowNotBlank = true;
                                    }
                                }
                                if (rowNotBlank) {
                                    Cell cell = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    if (cell.getCellType() == CellType.NUMERIC) {
                                        double d = cell.getNumericCellValue();
                                        total += d;
                                    } else {
                                        eim.addMessage(j + 1, 7, partsName + "工作表\"卷/套\"格式错误");
                                    }
                                }
                            }

                        }
                        if (fe == j) {
                            Cell cell1 = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            Cell cell2 = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            Cell cell3 = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            Cell cell4 = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            Cell cell5 = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            Cell cell6 = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            Cell cell7 = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            Cell cell8 = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            Cell cell9 = row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            if (!StringUtils.equals("图纸文件代码", cell1.toString())) {
                                eim.addMessage(j + 1, 1, "\"" + partsName + "\"工作表此处标题应为\"图纸文件代码\"");
                            }
                            if (!StringUtils.equals("产品名称/层号", cell2.toString())) {
                                eim.addMessage(j + 1, 2, "\"" + partsName + "\"工作表此处标题应为\"产品名称/层号\"");
                            }
                            if (!StringUtils.equals("坯布名称", cell3.toString())) {
                                eim.addMessage(j + 1, 3, "\"" + partsName + "\"工作表此处标题应为\"坯布名称\"");
                            }
                            if (!StringUtils.equals("物料代码", cell4.toString())) {
                                eim.addMessage(j + 1, 4, "\"" + partsName + "\"工作表此处标题应为\"物料代码\"");
                            }
                            if (!StringUtils.equals("工艺代码", cell5.toString())) {
                                eim.addMessage(j + 1, 6, "\"" + partsName + "\"工作表此处标题应为\"工艺代码\"");
                            }
                            if (!StringUtils.equals("门幅", cell6.toString())) {
                                eim.addMessage(j + 1, 7, "\"" + partsName + "\"工作表此处标题应为\"门幅\"");
                            }
                            if (!StringUtils.equals("米长", cell7.toString())) {
                                eim.addMessage(j + 1, 8, "\"" + partsName + "\"工作表此处标题应为\"米长\"");
                            }
                            if (!StringUtils.equals("卷号", cell8.toString())) {
                                eim.addMessage(j + 1, 9, "\"" + partsName + "\"工作表此处标题应为\"卷号\"");
                            }

                            if (!StringUtils.equals("套/卷", cell9.toString())) {
                                eim.addMessage(j + 1, 10, "\"" + partsName + "\"工作表此处标题应为\"套/卷\"");
                            }
                        }
                        if (fe != -1 && fe < j && ff == -1) {//行号在原材料信息(物料代码)和成品重量信息之间的数据
                            if (row != null && "成品重量信息".equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {
                                ff = j + 1;
                                continue;
                            }
                            if (row != null) {
                                Cell cell1 = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell2 = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell3 = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell4 = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell5 = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell6 = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell7 = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell8 = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell9 = row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                if (StringUtils.isNotBlank(cell1.toString()) || StringUtils.isNotBlank(cell2.toString())
                                        || StringUtils.isNotBlank(cell3.toString()) || StringUtils.isNotBlank(cell4.toString())
                                        || StringUtils.isNotBlank(cell5.toString()) || StringUtils.isNotBlank(cell6.toString())
                                        || StringUtils.isNotBlank(cell7.toString()) || StringUtils.isNotBlank(cell8.toString())
                                        || StringUtils.isNotBlank(cell9.toString())) {
                                    RawMaterial rm = new RawMaterial();
                                    FinishedProduct fp = null;
                                    if ("常规部件".equals(partsDesc.getPartsType())) {
                                        //图号必须验证
                                        if (StringUtils.isBlank(cell1.toString())) {
                                            eim.addMessage(j + 1, 1, partsName + "工作表\"图纸文件代码\"不能为空");
                                        } else {
                                            rm.setDrawingNo(cell1.toString());
                                        }

                                    } else if ("成品胚布".equals(partsDesc.getPartsType())) {
                                        //层数、卷号必须验证
                                        if (StringUtils.isBlank(cell2.toString())) {
                                            eim.addMessage(j + 1, 2, partsName + "工作表\"产品名称/层号\"不能为空");
                                        } else {
                                            rm.setLevelNo(cell2.toString());
                                        }
                                        if (StringUtils.isBlank(cell8.toString())) {
                                            eim.addMessage(j + 1, 9, partsName + "工作表\"卷号\"不能为空");
                                        } else {
                                            rm.setRollNo(cell8.toString());
                                        }
                                    }

                                    String materialCode = cell4.toString();
                                    if (StringUtils.isBlank(materialCode)) {
                                        eim.addMessage(j + 1, 4, partsName + "工作表\"物料代码\"不能为空");
                                    } else {
                                        rm.setMaterialCode(materialCode);
                                        Map<String, Object> param = new HashMap<>();
                                        param.put("materielCode", materialCode);                //物料编码
                                        param.put("productConsumerId", consumer.getId());    //成品客户信息ID
                                        param.put("obsolete", null);                            //使用状态		废弃：1,正常：null
                                        List<FinishedProduct> fps = tcBomVersionPartsDao.findListByMap(FinishedProduct.class, param);
                                        if (fps != null && fps.size() > 0) {
                                            fp = fps.get(0);
                                        } else {
                                            eim.addMessage(j + 1, 4, partsName + "工作表 在\"成品信息\"中找不到对应的\"物料代码:" + materialCode + "\"和\"客户信息:" + consumer.getId() + "\"");
                                        }
                                    }

                                    if (StringUtils.isBlank(cell3.toString())) {
                                        eim.addMessage(j + 1, 3, partsName + "工作表\"坯布名称\"不能为空");
                                    } else {
                                        String pn = cell3.toString();
                                        if (fp != null && !StringUtils.startsWith(fp.getProductProcessName(), pn)) {
                                            eim.addMessage(j + 1, 3, partsName + "工作表\"坯布名称\"与\"成品信息\"中不一致");
                                        } else {
                                            rm.setProcessName(pn);
                                        }
                                    }

                                    if (StringUtils.isBlank(cell5.toString())) {
                                        eim.addMessage(j + 1, 6, partsName + "工作表\"工艺代码\"不能为空");
                                    } else {
                                        String pc = cell5.toString();
                                        if (fp != null && !StringUtils.equals(pc, fp.getProductProcessCode())) {
                                            eim.addMessage(j + 1, 6, partsName + "工作表\"工艺代码\"与\"成品信息\"中不一致");
                                            Log log = new Log();
                                            log.setLogDate(new Date());
                                            log.setOperate("工艺代码校验");
                                            log.setLoginName("");
                                            log.setUserId((long) -999);
                                            log.setParamsValue("工艺代码:" + pc + ",成品信息:" + fp.getProductProcessCode() + ",成品ID:" + fp.getId());
                                            logService.save(log);
                                        } else {
                                            rm.setProcessCode(pc);
                                        }
                                    }

                                    if (StringUtils.isBlank(cell6.toString())) {
                                        eim.addMessage(j + 1, 7, partsName + "工作表\"门幅\"不能为空");
                                    } else if (cell6.getCellType() != CellType.NUMERIC) {
                                        eim.addMessage(j + 1, 7, partsName + "工作表\"门幅\"只能为数字");
                                    } else {
                                        double width = cell6.getNumericCellValue();
                                        if (fp != null && null == fp.getProductWidth()) {
                                            eim.addMessage(j + 1, 7, partsName + "的物料代码为" + fp.getMaterielCode() + "成品信息中门幅未填");
                                        } else {
                                            if (fp != null && width != fp.getProductWidth()) {
                                                eim.addMessage(j + 1, 7, partsName + "工作表\"门幅\"与\"成品信息\"中不一致");
                                            } else {
                                                rm.setWidth(width);
                                            }
                                        }
                                    }

                                    if (StringUtils.isBlank(cell7.toString())) {
                                        eim.addMessage(j + 1, 8, partsName + "工作表\"米长\"不能为空");
                                    } else if (cell7.getCellType() != CellType.NUMERIC) {
                                        eim.addMessage(j + 1, 8, partsName + "工作表\"米长\"只能为数字");
                                    } else {
                                        double length = cell7.getNumericCellValue();
                                        if (fp != null && length != fp.getProductRollLength()) {
                                            eim.addMessage(j + 1, 8, partsName + "工作表\"米长\"与\"成品信息\"中不一致");
                                        } else {
                                            rm.setMlength(length);
                                        }
                                    }

                                    if (StringUtils.isBlank(cell9.toString())) {
                                        eim.addMessage(j + 1, 10, partsName + "工作表\"套/卷\"不能为空");
                                    } else if (cell9.getCellType() != CellType.NUMERIC) {
                                        eim.addMessage(j + 1, 10, partsName + "工作表\"套/卷\"只能为数字");
                                    } else {
                                        rm.setJpc(cell9.getNumericCellValue());
                                    }

                                    rawMaterialList.add(rm);
                                }
                            }
                        }

                        if (ff != -1 && ff < j && fg == -1) {//行号在成品重量信息(裁片总重物料号)和包装工艺信息之间的数据
                            if (row != null && "包装工艺信息".equals(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {
                                fg = j;
                                continue;
                            }
                            if (row != null) {
                                Cell cell1 = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell2 = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                Cell cell3 = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                if (StringUtils.isNotBlank(cell1.toString()) || StringUtils.isNotBlank(cell2.toString()) || StringUtils.isNotBlank(cell3.toString())) {
                                    FinishedWeight fw = new FinishedWeight();
                                    if (StringUtils.isBlank(cell1.toString())) {
                                        eim.addMessage(j + 1, 1, partsName + "工作表\"裁片总重物料号\"不能为空");
                                    } else {
                                        fw.setCutpartsTotalWeightMaterialNo(cell1.toString());
                                    }

                                    if (StringUtils.isBlank(cell2.toString())) {
                                        eim.addMessage(j + 1, 3, partsName + "工作表\"裁片坯布规格\"不能为空");
                                    } else {
                                        fw.setCutpartsSpec(cell2.toString());
                                    }

                                    if (StringUtils.isBlank(cell3.toString())) {
                                        eim.addMessage(j + 1, 4, partsName + "工作表\"裁片总重\"不能为空");
                                    } else if (cell3.getCellType() != CellType.NUMERIC) {
                                        eim.addMessage(j + 1, 4, partsName + "工作表\"裁片总重\"只能为数字");
                                    } else {
                                        fw.setCutpartsTotalWeight(cell3.getNumericCellValue());
                                    }
                                    finishedWeightList.add(fw);
                                }
                            }
                        }

                    }//end for inner
                    if (fc == -1) {
                        eim.addMessage(partsName + "工作表\"成品信息\"标题不存在");
                    }
                    if (fd == -1) {
                        eim.addMessage(partsName + "工作表\"图纸文件信息\"标题不存在");
                    }
                    if (fe == -1) {
                        eim.addMessage(partsName + "工作表\"原材料信息\"标题不存在");
                    }
                    if (ff == -1) {
                        eim.addMessage(partsName + "工作表\"成品重量信息\"标题不存在");
                    }
                    if (fg == -1) {
                        eim.addMessage(partsName + "工作表\"包装工艺信息\"标题不存在");
                    }
                    partsDesc.setPartsSubsCount(total);
                    partsDesc.setRawMaterialList(rawMaterialList);
                    partsDesc.setFinishedWeight(finishedWeightList);

                }

            }//end for outer

            if (!eim.hasError()) {
                //删除以前的套材BOM版本部件
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("tcProcBomVersoinId", tcBomVersion.getId());
                List<TcBomVersionParts> tcBomVersionPartsList = tcBomVersionPartsDao.findListByMap(TcBomVersionParts.class, map);
                for (TcBomVersionParts tcBomVersionParts : tcBomVersionPartsList) {
                    map.clear();
                    map.put("tcProcBomPartsId", tcBomVersionParts.getId());
                    tcBomVersionPartsDao.deleteParts(tcBomVersionParts.getId());
                }

                //把数据放入套材BOM版本部件列表
                for (PartsDesc fd : data) {
                    String partsName = fd.getPartsName();
                    String customerMaterialCode = fd.getCustomerMaterialCode();
                    String partType = fd.getPartsType();
                    String partNo = fd.getMaterialNumber();
                    Double partsWeight = fd.getPartsWeight();
                    String processCode = fd.getPartsCutCode();
                    Double jpt = fd.getPartsSubsCount();

                    TcBomVersionParts detail = new TcBomVersionParts();
                    detail.setIsDeleted(0);
                    detail.setTcProcBomVersoinId(tcBomVersion.getId());//BOM版本ID
                    detail.setTcProcBomVersionPartsName(partsName);//部件名称
                    detail.setCustomerMaterialCode(customerMaterialCode);//客户物料号
                    detail.setTcProcBomVersionPartsType(partType);//部件类型
                    detail.setTcProcBomVersionPartsCutCode(processCode);//部件裁剪包装工艺代码
                    detail.setTcProcBomVersionPartsCount(1d);//部件数量
                    detail.setTcProcBomVersionPartsSubsCount(jpt);//小部件个数
                    detail.setTcProcBomVersionPartsWeight(partsWeight);    //部件重量
                    detail.setTcProcBomVersionMaterialNumber(partNo);    //部件物料号
                    detail.setNeedSort(1);    //小部件是否需要排序
                    tcBomVersionPartsDao.save(detail);

                    //添加部件明细
                    List<RawMaterial> rms = fd.getRawMaterialList();
                    for (int i = 0; i < rms.size(); i++) {
                        RawMaterial rm = rms.get(i);
                        TcBomVersionPartsDetail pd = new TcBomVersionPartsDetail();
                        if (fd.getPartsType().equals("常规部件")) {//常规部件
                            pd.setDrawingNo(rm.getDrawingNo());//图号
                        } else {    //成品胚布
                            pd.setDrawingNo("");//图号
                            pd.setRollNo(rm.getRollNo());    //卷号
                            pd.setLevelNo(rm.getLevelNo());//层数
                        }
                        pd.setTcProcBomPartsId(detail.getId());
                        pd.setSorting(i + 1);                //排序号
                        pd.setLength(rm.getMlength());    //米长
                        Double jpc = rm.getJpc();            //套/卷
                        Double cpj = 0D;
                        if (jpc != 0) {
                            cpj = 1 / jpc;        //倒数
                        }
                        pd.setTcProcBomFabricCount(cpj);//数量(卷/套)


                        Map<String, Object> param = new HashMap<>();
                        param.put("materielCode", rm.getMaterialCode());
                        param.put("productConsumerId", consumer.getId());    //成品客户信息ID
                        param.put("obsolete", null);                            //使用状态		废弃：1,正常：null
                        System.out.println(param);
                        List<FinishedProduct> fpList = tcBomVersionPartsDao.findListByMap(FinishedProduct.class, param);
                        if (fpList != null && fpList.size() > 0) {
                            FinishedProduct fp = fpList.get(0);
                            pd.setTcFinishedProductId(fp.getId());//成品ID
                        }
                        tcBomVersionPartsDao.save(pd);
                    }//end for inner

                    //添加部件成品重量胚布信息
                    List<FinishedWeight> fws = fd.getFinishedWeight();
                    for (int i = 0; i < fws.size(); i++) {
                        FinishedWeight fw = fws.get(i);
                        TcBomVersionPartsFinishedWeightEmbryoCloth fwec = new TcBomVersionPartsFinishedWeightEmbryoCloth();
                        fwec.setTcProcBomPartsId(detail.getId());
                        fwec.setMaterialNumber(fw.getCutpartsTotalWeightMaterialNo());    //物料代码
                        fwec.setEmbryoClothName(fw.getCutpartsSpec());                    //胚布名称
                        fwec.setWeight(fw.getCutpartsTotalWeight());                    //重量

                        tcBomVersionPartsDao.save(fwec);
                    }//end for inner
                }//end for outer

            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();    //手动回滚事物
            }

        }//end if
        return eim;
    }

    public void savePdfFile(TcBom tcBom, TcBomVersion tcBomVersion, Long fileId, ExcelImportMessage eim) throws Exception {
        if (fileId != null) {
            Attachment att = tcBomDao.findById(Attachment.class, fileId);
            //String msg = ConvertPDFUtils.convert("TC", tcBom.getTcProcBomCode(), tcBomVersion.getTcProcBomVersionCode(), att.getFilePath());
            String msg = "";
            List<String> paths = new ArrayList<>();
            try {
                com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook(att.getFilePath());
                for (int i = 1; i < workbook.getWorksheets().getCount(); i++) {
                    Worksheet ws = workbook.getWorksheets().get(i);
                    if (ws.isVisible()) {
                        ws.setVisible(false);
                    }
                }
                File file = new File(att.getFilePath());
                for (int j = 0; j < workbook.getWorksheets().getCount(); j++) {
                    Worksheet ws = workbook.getWorksheets().get(j);
                    String path = file.getParent() + File.separator + tcBom.getTcProcBomCode() + "_" + tcBomVersion.getTcProcBomVersionCode() + "_" + ws.getName() + "(" + tcBomVersion.getTcProcBomVersionCode() + ").pdf";
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
                logger.error(e.getLocalizedMessage(), e);
                msg = "ERROR";
            }

            if (msg.contains("ERROR")) {
                eim.addMessage("pdf转换失败");
            } else {
                Map<String, Object> param = new HashMap<>();
                param.put("bomName", tcBom.getTcProcBomName());
                param.put("processBomCode", tcBom.getTcProcBomCode());
                param.put("processBomVersion", tcBomVersion.getTcProcBomVersionCode());
                List<BomFile> list = tcBomVersionPartsDao.findListByMap(BomFile.class, param);
                BomFile bomFile;
                if (list.isEmpty()) {
                    bomFile = new BomFile();
                    bomFile.setBomName(tcBom.getTcProcBomName());
                    bomFile.setProcessBomCode(tcBom.getTcProcBomCode());
                    bomFile.setProcessBomVersion(tcBomVersion.getTcProcBomVersionCode());
                    bomFile.setFileName(att.getFileName());
                    bomFile.setFilePath(att.getFilePath());
                    bomFile.setUploadTime(new Date());
                    tcBomVersionPartsDao.save(bomFile);
                } else {
                    bomFile = list.get(0);
                    bomFile.setBomName(tcBom.getTcProcBomName());
                    bomFile.setProcessBomCode(tcBom.getTcProcBomCode());
                    bomFile.setProcessBomVersion(tcBomVersion.getTcProcBomVersionCode());
                    bomFile.setFileName(att.getFileName());
                    bomFile.setFilePath(att.getFilePath());
                    bomFile.setUploadTime(new Date());
                    tcBomVersionPartsDao.update(bomFile);
                }
                param.clear();
                param.put("bomFileId", bomFile.getId());
                tcBomVersionPartsDao.delete(BomFilePdf.class, param);

                com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook(att.getFilePath());
                for (int i = 0; i < workbook.getWorksheets().getCount(); i++) {
                    if (workbook.getWorksheets().get(i).isVisible()) {
                        BomFilePdf bomFilePdf = new BomFilePdf();
                        bomFilePdf.setBomFileId(bomFile.getId());
                        bomFilePdf.setPDFfilePath(paths.get(i));
                        tcBomVersionPartsDao.save(bomFilePdf);
                    }
                }
            }
        }
    }

    /**
     * 部件描述
     */
    private class PartsDesc {

        /**
         * 部件名称
         */
        private String partsName;

        /**
         * 部件名称
         */
        private String customerMaterialCode;

        /**
         * 部件类型
         */
        private String partsType;

        /**
         * 部件裁剪包装工艺代码
         */
        private String partsCutCode;

        /**
         * 小部件个数
         */
        private Double partsSubsCount;

        /**
         * 部件重量
         */
        private Double partsWeight;

        /**
         * 部件物料号
         */
        private String materialNumber;

        /**
         * 原材料信息
         */
        private List<RawMaterial> rawMaterialList;

        /**
         * 成品重量信息
         */
        private List<FinishedWeight> finishedWeight;

        public String getPartsName() {
            return partsName;
        }

        public void setPartsName(String partsName) {
            this.partsName = partsName;
        }

        public String getPartsType() {
            return partsType;
        }

        public void setPartsType(String partsType) {
            this.partsType = partsType;
        }

        public String getPartsCutCode() {
            return partsCutCode;
        }

        public void setPartsCutCode(String partsCutCode) {
            this.partsCutCode = partsCutCode;
        }

        public Double getPartsSubsCount() {
            return partsSubsCount;
        }

        public void setPartsSubsCount(Double partsSubsCount) {
            this.partsSubsCount = partsSubsCount;
        }

        public Double getPartsWeight() {
            return partsWeight;
        }

        public void setPartsWeight(Double partsWeight) {
            this.partsWeight = partsWeight;
        }

        public String getMaterialNumber() {
            return materialNumber;
        }

        public void setMaterialNumber(String materialNumber) {
            this.materialNumber = materialNumber;
        }

        public List<RawMaterial> getRawMaterialList() {
            return rawMaterialList;
        }

        public void setRawMaterialList(List<RawMaterial> rawMaterialList) {
            this.rawMaterialList = rawMaterialList;
        }

        public List<FinishedWeight> getFinishedWeight() {
            return finishedWeight;
        }

        public void setFinishedWeight(List<FinishedWeight> finishedWeight) {
            this.finishedWeight = finishedWeight;
        }

        public String getCustomerMaterialCode() {
            return customerMaterialCode;
        }

        public void setCustomerMaterialCode(String customerMaterialCode) {
            this.customerMaterialCode = customerMaterialCode;
        }
    }

    /**
     * 原材料信息
     */
    private class RawMaterial {
        /**
         * 图纸文件代码
         */
        private String drawingNo;

        /**
         * 产品名称/层号
         */
        private String levelNo;

        /**
         * 工艺名称
         */
        private String processName;

        /**
         * 物料代码
         */
        private String materialCode;

        /**
         * 工艺代码
         */
        private String processCode;

        /**
         * 门幅
         */
        private Double width;

        /**
         * 米长
         */
        private Double mlength;

        /**
         * 卷号
         */
        private String rollNo;

        /**
         * 套/卷
         */
        private Double jpc;

        public String getDrawingNo() {
            return drawingNo;
        }


        public void setDrawingNo(String drawingNo) {
            this.drawingNo = drawingNo;
        }

        public String getLevelNo() {
            return levelNo;
        }

        public void setLevelNo(String levelNo) {
            this.levelNo = levelNo;
        }

        public String getProcessName() {
            return processName;
        }

        public void setProcessName(String processName) {
            this.processName = processName;
        }

        public String getMaterialCode() {
            return materialCode;
        }

        public void setMaterialCode(String materialCode) {
            this.materialCode = materialCode;
        }

        public String getProcessCode() {
            return processCode;
        }

        public void setProcessCode(String processCode) {
            this.processCode = processCode;
        }

        public Double getWidth() {
            return width;
        }

        public void setWidth(Double width) {
            this.width = width;
        }

        public Double getMlength() {
            return mlength;
        }

        public void setMlength(Double mlength) {
            this.mlength = mlength;
        }

        public String getRollNo() {
            return rollNo;
        }

        public void setRollNo(String rollNo) {
            this.rollNo = rollNo;
        }

        public Double getJpc() {
            return jpc;
        }

        public void setJpc(Double jpc) {
            this.jpc = jpc;
        }

    }

    /**
     * 成品重量信息
     */
    private class FinishedWeight {
        /**
         * 裁片总重物料号
         */
        private String cutpartsTotalWeightMaterialNo;

        /**
         * 裁片坯布规格
         */
        private String cutpartsSpec;

        /**
         * 裁片总重
         */
        private Double cutpartsTotalWeight;

        public String getCutpartsTotalWeightMaterialNo() {
            return cutpartsTotalWeightMaterialNo;
        }

        public void setCutpartsTotalWeightMaterialNo(String cutpartsTotalWeightMaterialNo) {
            this.cutpartsTotalWeightMaterialNo = cutpartsTotalWeightMaterialNo;
        }

        public String getCutpartsSpec() {
            return cutpartsSpec;
        }

        public void setCutpartsSpec(String cutpartsSpec) {
            this.cutpartsSpec = cutpartsSpec;
        }

        public Double getCutpartsTotalWeight() {
            return cutpartsTotalWeight;
        }

        public void setCutpartsTotalWeight(Double cutpartsTotalWeight) {
            this.cutpartsTotalWeight = cutpartsTotalWeight;
        }

    }

}

