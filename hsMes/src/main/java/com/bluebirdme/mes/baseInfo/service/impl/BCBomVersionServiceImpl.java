/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service.impl;

import com.aspose.cells.Worksheet;
import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.baseInfo.dao.IBCBomVersionDao;
import com.bluebirdme.mes.baseInfo.dao.IBcBomVersionDetailDao;
import com.bluebirdme.mes.baseInfo.entity.*;
import com.bluebirdme.mes.baseInfo.service.IBCBomVersionService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.entity.Attachment;
import com.bluebirdme.mes.utils.ImportExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xdemo.superutil.j2se.MapUtils;
import org.xdemo.superutil.j2se.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author 徐波
 * @Date 2016-10-8 16:53:24
 */
@Service(value = "bCBomVersionServiceImpl")
@AnyExceptionRollback
public class BCBomVersionServiceImpl extends BaseServiceImpl implements IBCBomVersionService {
    private static Logger log = LoggerFactory.getLogger(BCBomVersionServiceImpl.class);
    @Resource
    IBCBomVersionDao bCBomVersionDao;
    @Resource
    IBcBomVersionDetailDao bcBomVersionDetailDao;

    @Override
    protected IBaseDao getBaseDao() {
        return bCBomVersionDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return bCBomVersionDao.findPageInfo(filter, page);
    }

    // 根据id删除版本和对应版本下面的明细
    @Override
    /**
     * delete 方法的简述.
     * 根据传入的包材bom版本的id删除对应的包材bom版本和明细<br>
     * @param ids 类型:String，多个id用‘,’号分割
     * @return 无
     */
    public void deleteAll(String ids) throws Exception {
        bCBomVersionDao.delete(ids);
        bcBomVersionDetailDao.deleteByPid();
    }

    // 根据id获取bom版本的json

    /**
     * getBcBomJson 方法的简述. 根据传入的包材bom版本的id获得对应的json数据用于创建treeview节点<br>
     *
     * @param id 类型:String，单个id
     * @return List<Map < String, Object>>
     */
    public List<Map<String, Object>> getBcBomJson(String id) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        try {
            listMap = bCBomVersionDao.getBcBomJson(id);
        } catch (SQLTemplateException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : listMap) {
            ret = new HashMap<String, Object>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
//			String s = "";
//			if (MapUtils.getAsInt(map, "PACKISDEFAULT") == 1) {
//				s = "[默认-";
//			} else {
//				s += "[";
//			}
//			if (MapUtils.getAsInt(map, "PACKENABLED") == 1) {
//				s += "启用]";
//			} else if (MapUtils.getAsInt(map, "PACKENABLED") == 0) {
//				s += "改版]";
//			} else {
//				s += "停用]";
//			}
            ret.put("text", MapUtils.getAsString(map, "packVersion".toUpperCase()));
            // + "/"
            // + MapUtils.getAsString(map,
            // "packCode".toUpperCase()));

            map.put("nodeType", "version");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    // 复制版本节点
    @Override
    /**
     * toCompliteCopy 方法的简述.
     * 根据传入的包材bom版本的id复制包材bom版本<br>
     * @param ids 类型:String，单个id
     * @return 无
     */
    public void toCompliteCopy(String id) throws Exception {
        // 根据id查询需要复制的版本对象
        BCBomVersion bmVersion = findById(BCBomVersion.class, Long.parseLong(id));
        // 根据字段查询对象的集合
        HashMap map = new HashMap();
        map.put("packVersionId", bmVersion.getId());
        List<BcBomVersionDetail> li = bcBomVersionDetailDao.findListByMap(BcBomVersionDetail.class, map);
        ArrayList<BcBomVersionDetail> saveLi = new ArrayList<BcBomVersionDetail>();
        // 保存新的version对象
        BCBomVersion newVersion = new BCBomVersion();
        ObjectUtils.clone(bmVersion, newVersion);
        if (bmVersion.getPackIsDefault() == 1) {
            newVersion.setPackIsDefault(-1);
        }
        newVersion.setPackVersion(newVersion.getPackVersion() + "(复制)");
        newVersion.setAuditState(AuditConstant.RS.SUBMIT);
        bCBomVersionDao.save(newVersion);
        for (BcBomVersionDetail d : li) {
            BcBomVersionDetail saveDetail = new BcBomVersionDetail();
            ObjectUtils.clone(d, saveDetail);
            saveDetail.setPackVersionId(newVersion.getId());
            saveLi.add(saveDetail);
        }
        bcBomVersionDetailDao.save(saveLi.toArray(new BcBomVersionDetail[]{}));
    }

    public void toCompliteCopy(String id, String versionName) throws Exception {
        // 根据id查询需要复制的版本对象
        BCBomVersion bmVersion = findById(BCBomVersion.class, Long.parseLong(id));
        // 根据字段查询对象的集合
        HashMap map = new HashMap();
        map.put("packVersionId", bmVersion.getId());
        List<BcBomVersionDetail> li = bcBomVersionDetailDao.findListByMap(BcBomVersionDetail.class, map);
        ArrayList<BcBomVersionDetail> saveLi = new ArrayList<BcBomVersionDetail>();
        // 保存新的version对象
        BCBomVersion newVersion = new BCBomVersion();
        ObjectUtils.clone(bmVersion, newVersion);
        if (bmVersion.getPackIsDefault() == 1) {
            newVersion.setPackIsDefault(-1);
            newVersion.setPackVersion(versionName);
        }
//		newVersion.setPackVersion(newVersion.getPackVersion() + "(复制)");
        newVersion.setAuditState(AuditConstant.RS.SUBMIT);
        bCBomVersionDao.save(newVersion);
        for (BcBomVersionDetail d : li) {
            BcBomVersionDetail saveDetail = new BcBomVersionDetail();
            ObjectUtils.clone(d, saveDetail);
            saveDetail.setPackVersionId(newVersion.getId());
            saveLi.add(saveDetail);
        }
        bcBomVersionDetailDao.save(saveLi.toArray(new BcBomVersionDetail[]{}));
    }

    @Override
    /**
     * toCompliteCopy 方法的简述.
     * 根据传入的包材bom版本的id复制包材bom版本<br>
     * @param ids 类型:String，单个id
     * @return 无
     */
    public void toCompliteCopyBom(String id) throws Exception {
        // 根据id查询需要复制的版本对象
        BcBom bom = findById(BcBom.class, Long.parseLong(id));
        BcBom newbom = new BcBom();
        ObjectUtils.clone(bom, newbom);
        newbom.setPackBomGenericName(bom.getPackBomGenericName() + "(复制)");
        bCBomVersionDao.save(newbom);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("packBomId", bom.getId());
        List<BCBomVersion> bmVersionList = findListByMap(BCBomVersion.class, map);
        // 根据字段查询对象的集合
        for (BCBomVersion bmVersion : bmVersionList) {
            map.clear();
            map.put("packVersionId", bmVersion.getId());
            List<BcBomVersionDetail> li = bcBomVersionDetailDao.findListByMap(BcBomVersionDetail.class, map);
            ArrayList<BcBomVersionDetail> saveLi = new ArrayList<BcBomVersionDetail>();
            // 保存新的version对象
            BCBomVersion newVersion = new BCBomVersion();
            ObjectUtils.clone(bmVersion, newVersion);
            if (bmVersion.getPackIsDefault() == 1) {
                newVersion.setPackIsDefault(-1);
            }
            newVersion.setPackVersion(newVersion.getPackVersion());
            newVersion.setAuditState(AuditConstant.RS.SUBMIT);
            newVersion.setPackBomId(newbom.getId());
            bCBomVersionDao.save(newVersion);

            for (BcBomVersionDetail d : li) {
                BcBomVersionDetail saveDetail = new BcBomVersionDetail();
                ObjectUtils.clone(d, saveDetail);
                saveDetail.setPackVersionId(newVersion.getId());
                saveLi.add(saveDetail);
            }
            bcBomVersionDetailDao.save(saveLi.toArray(new BcBomVersionDetail[]{}));
        }
    }

    public void savePdfFile(BCBomVersion bcBomVersion, Long fileId, ExcelImportMessage eim) throws Exception {
        if (fileId != null) {
            bcBomVersion.setAttachmentId(fileId.intValue());
            update2(bcBomVersion);
            Attachment att = bCBomVersionDao.findById(Attachment.class, fileId);
            BcBom bcBom = bCBomVersionDao.findById(BcBom.class, bcBomVersion.getPackBomId());
            //String msg = ConvertPDFUtils.convert("BC", bcBom.getPackBomCode(),bcBomVersion.getPackVersion(), att.getFilePath());
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
                    String path = "";
                    if (j == 0) {
                        path = file.getParent() + File.separator + bcBom.getPackBomCode() + "_" + ws.getName() + ".pdf";
                    } else {
                        path = file.getParent() + File.separator + bcBom.getPackBomCode() + "_" + bcBomVersion.getPackVersion() + "_" + ws.getName() + "(" + bcBomVersion.getPackVersion() + ").pdf";
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
                Map<String, Object> param = new HashMap<>();
                param.put("bomName", bcBom.getPackBomModel());
                param.put("bcBomCode", bcBom.getPackBomCode());
                param.put("bcbomVersion", bcBomVersion.getPackVersion());
                List<BomFile> list = bCBomVersionDao.findListByMap(BomFile.class, param);
                BomFile bomFile;
                if (list.isEmpty()) {
                    bomFile = new BomFile();
                    bomFile.setBomName(bcBom.getPackBomModel());
                    bomFile.setBcBomCode(bcBom.getPackBomCode());
                    bomFile.setBcbomVersion(bcBomVersion.getPackVersion());
                    bomFile.setFileName(att.getFileName());
                    bomFile.setFilePath(att.getFilePath());
                    bomFile.setUploadTime(new Date());
                    bCBomVersionDao.save(bomFile);
                } else {
                    bomFile = list.get(0);
                    bomFile.setBomName(bcBom.getPackBomModel());
                    bomFile.setBcBomCode(bcBom.getPackBomCode());
                    bomFile.setBcbomVersion(bcBomVersion.getPackVersion());
                    bomFile.setFileName(att.getFileName());
                    bomFile.setFilePath(att.getFilePath());
                    bomFile.setUploadTime(new Date());
                    bCBomVersionDao.update(bomFile);
                }
                param.clear();
                param.put("bomFileId", bomFile.getId());
                bCBomVersionDao.delete(BomFilePdf.class, param);

                com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook(att.getFilePath());
                for (int i = 0; i < workbook.getWorksheets().getCount(); i++) {
                    if (workbook.getWorksheets().get(i).isVisible()) {
                        BomFilePdf bomFilePdf = new BomFilePdf();
                        bomFilePdf.setBomFileId(bomFile.getId());
                        bomFilePdf.setPDFfilePath(paths.get(i));
                        bCBomVersionDao.save(bomFilePdf);
                    }
                }
            }
        }
    }


    @Override
    public String importBcBomMainUploadFile(MultipartFile file, String userId,String packVersionId) throws IOException {
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

        String tcProcBomCodeVersion = "";
        if (arrayList.size() > 0) {
            if (arrayList.get(0).length < 6) {
                return "文件格式";

            }
            List<BcBomVersionDetail> bcs = new ArrayList<>();
            String packRequire = null;
            for (int i = 2; i < arrayList.size(); i++) {
                BcBomVersionDetail bcbd = new BcBomVersionDetail();
                String packMateraalName = arrayList.get(i)[0];
                bcbd.setPackMaterialName(packMateraalName);
                String packMaterialModel = arrayList.get(i)[1];
                bcbd.setPackMaterialModel(packMaterialModel);

                String packMaterialCount = arrayList.get(i)[3];
                bcbd.setPackMaterialCount(Double.parseDouble(packMaterialCount));
                String packMaterialUnit = arrayList.get(i)[4];
                bcbd.setPackMaterialUnit(packMaterialUnit);
                String packMemo = arrayList.get(i)[5];
                bcbd.setPackMemo(packMemo);
               if(packRequire==null) {
                   packRequire = arrayList.get(i)[6];
               }
                bcbd.setPackRequire(packRequire);

                bcs.add(bcbd);
                bcbd.setPackVersionId(Long.parseLong(packVersionId));
            }
            bCBomVersionDao.saveList(bcs);
        }
        return "";
    }
}
