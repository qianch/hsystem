/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service.impl;

import com.aspose.cells.Worksheet;
import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.baseInfo.dao.IFtcBomDao;
import com.bluebirdme.mes.baseInfo.entity.*;
import com.bluebirdme.mes.baseInfo.service.IFtcBomService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.entity.Attachment;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MapUtils;
import org.xdemo.superutil.j2se.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * @author 宋黎明
 * @Date 2016-10-8 13:36:52
 */
@Service
@AnyExceptionRollback
public class FtcBomServiceImpl extends BaseServiceImpl implements IFtcBomService {
    private static final Logger logger = LoggerFactory.getLogger(FtcBomServiceImpl.class);
    @Resource
    IFtcBomDao fTc_BomDao;

    @Override
    protected IBaseDao getBaseDao() {
        return fTc_BomDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return fTc_BomDao.findPageInfo(filter, page);
    }

    // 删除工艺BOM及它的所有节点
    public void delete(String ids) {
        fTc_BomDao.delete(ids);
    }

    // 删除BOM明细
    public void deleteDetail(String ids) {
        fTc_BomDao.deleteDetail(ids);
    }

    // 删除BOM版本及它的明细
    public void deleteBomVersion(String ids) {
        fTc_BomDao.deleteBomVersion(ids);
    }

    // 获取工艺BOM数据并封装树数据
    public List<Map<String, Object>> getFtcBomJson(String data) {
        List<Map<String, Object>> listMap;
        try {
            listMap = fTc_BomDao.getFtcBomJson(data);
        } catch (SQLTemplateException e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<>();
        for (Map<String, Object> map : listMap) {
            ret = new HashMap<>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "ftcProcBomName".toUpperCase()) + "/" + MapUtils.getAsString(map, "ftcProcBomCode".toUpperCase()));
            ret.put("state", "closed");
            map.put("nodeType", "bom");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    // 获取工艺BOM数据并封装树数据
    public List<Map<String, Object>> getFtcBomJsonTest(String data) {
        List<Map<String, Object>> listMap;
        try {
            listMap = fTc_BomDao.getFtcBomJsonTest(data);
        } catch (SQLTemplateException e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<>();
        for (Map<String, Object> map : listMap) {
            ret = new HashMap<>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "ftcProcBomName".toUpperCase()) + "/" + MapUtils.getAsString(map, "ftcProcBomCode".toUpperCase()));
            ret.put("state", "closed");
            map.put("nodeType", "bom");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    // 获取BOM版本数据并封装树数据
    public List<Map<String, Object>> getFtcBomByVersionJson(String id) {
        List<Map<String, Object>> listMap;
        try {
            listMap = fTc_BomDao.getFtcBomByVersionJson(id);
        } catch (SQLTemplateException e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<>();
        for (Map<String, Object> map : listMap) {
            ret = new HashMap<>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "ftcProcBomVersionCode".toUpperCase()));
            map.put("nodeType", "version");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    // 复制BOM版本
    @Override
    public void toCompliteCopy(String id) throws Exception {
        // 根据id查询需要复制的版本对象
        FtcBomVersion ftcBomVersion = findById(FtcBomVersion.class, Long.parseLong(id));
        // 根据字段查询对象的集合
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("ftcBomVersionId", ftcBomVersion.getId());
        List<FtcBomDetail> li = fTc_BomDao.findListByMap(FtcBomDetail.class, map);
        ArrayList<FtcBomDetail> saveLi = new ArrayList<FtcBomDetail>();
        // 保存新的version对象
        FtcBomVersion newVersion = new FtcBomVersion();
        ObjectUtils.clone(ftcBomVersion, newVersion);
        newVersion.setFtcProcBomVersionCode(newVersion.getFtcProcBomVersionCode() + "(复制)");
        newVersion.setFtcConsumerVersionCode(newVersion.getFtcConsumerVersionCode() + "(复制)");
        newVersion.setFtcProcBomVersionDefault(-1);
        // 复制版本时，将审核状态都改为未审核
        newVersion.setAuditState(AuditConstant.RS.SUBMIT);
        fTc_BomDao.save(newVersion);
        for (FtcBomDetail d : li) {
            FtcBomDetail saveDetail = new FtcBomDetail();
            ObjectUtils.clone(d, saveDetail);
            saveDetail.setFtcBomVersionId(newVersion.getId());
            saveLi.add(saveDetail);
        }
        fTc_BomDao.save(saveLi.toArray(new FtcBomDetail[]{}));
    }

    /**
     * 批量更新非套材的bom信息
     */
    public void updateFtcInfos(int value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("productIsTc", value);
        List<FinishedProduct> fpList = findListByMap(FinishedProduct.class, map);
        for (FinishedProduct fp : fpList) {
            Long procBomId = fp.getProcBomId();
            Long packBomId = fp.getPackBomId();
            if (procBomId != null) {
                FtcBomVersion fv = findById(FtcBomVersion.class, procBomId);
                if (fv == null) {
                    fp.setProductProcessCode(null);
                    fp.setProductProcessBomVersion(null);
                    fp.setProductConsumerBomVersion(null);
                } else {
                    FtcBom fb = findById(FtcBom.class, fv.getFtcProcBomId());
                    fp.setProductProcessCode(fb.getFtcProcBomName() + "/" + fb.getFtcProcBomCode());
                    fp.setProductProcessBomVersion(fv.getFtcProcBomVersionCode());
                    fp.setProductConsumerBomVersion(fv.getFtcConsumerVersionCode());
                }
            } else {
                fp.setProductProcessCode(null);
                fp.setProductProcessBomVersion(null);
                fp.setProductConsumerBomVersion(null);
            }
            if (packBomId != null) {
                BCBomVersion bv = findById(BCBomVersion.class, packBomId);
                if (bv == null) {
                    continue;
                }
                BcBom bb = findById(BcBom.class, bv.getPackBomId());
                fp.setProductPackagingCode(bb.getPackBomName() + "/" + bb.getPackBomCode());
                fp.setProductPackageVersion(bv.getPackVersion());
            }
            update(fp);
        }
    }

    @Override
    public List<Map<String, Object>> getFtcBomJsonTest1(String data) {
        List<Map<String, Object>> listMap;
        try {
            listMap = fTc_BomDao.getFtcBomJsonTest1(data);
        } catch (SQLTemplateException e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<>();
        for (Map<String, Object> map : listMap) {
            ret = new HashMap<>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "ftcProcBomName".toUpperCase()) + "/" + MapUtils.getAsString(map, "ftcProcBomCode".toUpperCase()));
            ret.put("state", "closed");
            map.put("nodeType", "bom");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    @Override
    public ExcelImportMessage doAddFtcBom(FtcBom ftcBom, FtcBomVersion ftcBomVersion, Long fileId) throws Exception {
        fTc_BomDao.save(ftcBom);
        //存放错误消息
        ExcelImportMessage eim = new ExcelImportMessage();
        if (ftcBom.getId() != null) {
            String fpbvc = ftcBomVersion.getFtcProcBomVersionCode();
            if (fpbvc != null && !fpbvc.equals("")) {
                //添加版本号
                ftcBomVersion.setFtcProcBomId(ftcBom.getId());
                ftcBomVersion.setFtcProcBomVersionDefault(1);
                ftcBomVersion.setFtcProcBomVersionEnabled(1);
                ftcBomVersion.setAuditState(AuditConstant.RS.SUBMIT);
                if (fileId != null) {
                    ftcBomVersion.setAttachmentId(fileId.intValue());
                }
                fTc_BomDao.save(ftcBomVersion);
                //判断有Excel文件要解析？
                if (fileId != null) {
                    Attachment att = fTc_BomDao.findById(Attachment.class, fileId);
                    savePdfFile(ftcBomVersion, att, eim);
                }
            }
        }
        return eim;
    }

    @Override
    public ExcelImportMessage doUpdateFtcBomVersion(FtcBomVersion ftcBomVersion, Long fileId) throws Exception {
        fTc_BomDao.update2(ftcBomVersion);
        //存放错误消息
        ExcelImportMessage eim = new ExcelImportMessage();
        //判断有Excel文件要解析？
        if (fileId != null) {
            Attachment att = fTc_BomDao.findById(Attachment.class, fileId);
            savePdfFile(ftcBomVersion, att, eim);
        }
        return eim;
    }

    @Override
    public Map<String, Object> findPageInfo1(Filter filter, Page page) {
        return fTc_BomDao.findPageInfo1(filter, page);
    }

    public void savePdfFile(FtcBomVersion ftcBomVersion, Attachment att, ExcelImportMessage eim) throws Exception {
        FtcBom ftcBom = fTc_BomDao.findById(FtcBom.class, ftcBomVersion.getFtcProcBomId());
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
                String path = file.getParent() + File.separator + ftcBom.getFtcProcBomCode() + "_" + ftcBomVersion.getFtcProcBomVersionCode() + "_" + ws.getName() + "(" + ftcBomVersion.getFtcProcBomVersionCode() + ").pdf";
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
            param.put("bomName", ftcBom.getFtcProcBomName());
            param.put("processBomCode", ftcBom.getFtcProcBomCode());
            param.put("processBomVersion", ftcBomVersion.getFtcProcBomVersionCode());
            List<BomFile> list = fTc_BomDao.findListByMap(BomFile.class, param);
            BomFile bomFile;
            if (list.isEmpty()) {
                bomFile = new BomFile();
                bomFile.setBomName(ftcBom.getFtcProcBomName());
                bomFile.setProcessBomCode(ftcBom.getFtcProcBomCode());
                bomFile.setProcessBomVersion(ftcBomVersion.getFtcProcBomVersionCode());
                bomFile.setFileName(att.getFileName());
                bomFile.setFilePath(att.getFilePath());
                bomFile.setUploadTime(new Date());
                fTc_BomDao.save(bomFile);
            } else {
                bomFile = list.get(0);
                bomFile.setBomName(ftcBom.getFtcProcBomName());
                bomFile.setProcessBomCode(ftcBom.getFtcProcBomCode());
                bomFile.setProcessBomVersion(ftcBomVersion.getFtcProcBomVersionCode());
                bomFile.setFileName(att.getFileName());
                bomFile.setFilePath(att.getFilePath());
                bomFile.setUploadTime(new Date());
                fTc_BomDao.update(bomFile);
            }
            param.clear();
            param.put("bomFileId", bomFile.getId());
            fTc_BomDao.delete(BomFilePdf.class, param);

            com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook(att.getFilePath());
            for (int i = 0; i < workbook.getWorksheets().getCount(); i++) {
                if (workbook.getWorksheets().get(i).isVisible()) {
                    BomFilePdf bomFilePdf = new BomFilePdf();
                    bomFilePdf.setBomFileId(bomFile.getId());
                    bomFilePdf.setPDFfilePath(paths.get(i));
                    fTc_BomDao.save(bomFilePdf);
                }
            }
        }
    }

    /**
     * 判断字符串是不是数字
     *
     */
    private boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 结构参数
     */
    private class ConstructionParameter {

        /**
         * 织物结构
         */
        private String productFeature;

        /**
         * 原料规格
         */
        private String rawMaterial;

        /**
         * 物料代码
         */
        private String itemNumber;

        /**
         * 单位面积质量
         */
        private String nominalAreaWeight;

        /**
         * 钢筘规格
         */
        private String reed;

        /**
         * 导纱针规格
         */
        private String guideNeedle;

        /**
         * 备注
         */
        private String remark;

        public String getProductFeature() {
            return productFeature;
        }

        public void setProductFeature(String productFeature) {
            this.productFeature = productFeature;
        }

        public String getRawMaterial() {
            return rawMaterial;
        }

        public void setRawMaterial(String rawMaterial) {
            this.rawMaterial = rawMaterial;
        }

        public String getItemNumber() {
            return itemNumber;
        }

        public void setItemNumber(String itemNumber) {
            this.itemNumber = itemNumber;
        }

        public String getNominalAreaWeight() {
            return nominalAreaWeight;
        }

        public void setNominalAreaWeight(String nominalAreaWeight) {
            this.nominalAreaWeight = nominalAreaWeight;
        }

        public String getReed() {
            return reed;
        }

        public void setReed(String reed) {
            this.reed = reed;
        }

        public String getGuideNeedle() {
            return guideNeedle;
        }

        public void setGuideNeedle(String guideNeedle) {
            this.guideNeedle = guideNeedle;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
