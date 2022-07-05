/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.audit.service.impl;

import java.util.*;

import javax.annotation.Resource;

import com.bluebirdme.mes.baseInfo.dao.IFtcBcBomVersionDao;
import com.bluebirdme.mes.baseInfo.dao.ITcBomVersionDao;
import com.bluebirdme.mes.baseInfo.entity.*;
import com.bluebirdme.mes.baseInfo.entityMirror.*;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import com.bluebirdme.mes.planner.produce.dao.IProducePlanDao;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.sales.dao.ISalesOrderDao;
import com.bluebirdme.mes.sales.entity.SalesOrderDetailPartsCount;
import com.bluebirdme.mes.sales.entity.SalesOrderDetailTemp;
import com.bluebirdme.mes.utils.ProductIsTc;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.bluebirdme.mes.audit.dao.IAuditInstanceDao;
import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.audit.entity.AuditInstance;
import com.bluebirdme.mes.audit.entity.AuditProcessSetting;
import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.common.service.IMessageCreateService;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.cut.entity.CutDailyPlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.produce.service.IProducePlanService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;

/**
 * @author 高飞
 * @Date 2016-10-25 13:52:50
 */
@Service
@AnyExceptionRollback
public class AuditInstanceServiceImpl extends BaseServiceImpl implements IAuditInstanceService {
    @Resource
    IAuditInstanceDao auditInstanceDao;
    @Resource
    IProducePlanService producePlanService;
    @Resource
    IMessageCreateService msgService;
    @Resource
    ITcBomVersionDao tcBomVersionDao;
    @Resource
    IFtcBcBomVersionDao ftcBcBomVersionDao;
    @Resource
    IProducePlanDao producePlanDao;

    @Override
    protected IBaseDao getBaseDao() {
        return auditInstanceDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return auditInstanceDao.findPageInfo(filter, page);
    }

    @Override
    public <T> void submitAudit(String auditTitle, String auditCode, Long submitUserId, String formUrl, Long formId, Class<T> clazz) throws Exception {
        Map<String, Object> condition = new HashMap();
        condition.put("entityJavaClass", clazz.getName());
        condition.put("formId", formId);

        List<AuditInstance> list = findListByMap(AuditInstance.class, condition);
        for (AuditInstance ai : list) {
            if (ai.getFirstAuditResult() == null) {
                throw new Exception("已在审核中，无需重复提交!");
            }
        }

        condition.clear();
        if (submitUserId == -1L)
            return;

        AuditInstance ai = new AuditInstance();
        ai.setAuditCode(auditCode);
        ai.setAuditTitle(auditTitle);
        ai.setCreateTime(new Date());
        ai.setCreateUserId(submitUserId);
        ai.setFormId(formId);
        ai.setFormUrl(formUrl);
        ai.setEntityJavaClass(clazz.getName());
        ai.setCurrentAuditProcessNode(AuditConstant.LEVEL.ONE);
        this.save(ai);

        condition.put("id", ai.getFormId());
        Map<String, Object> values = new HashMap();
        values.put("auditState", AuditConstant.RS.AUDITING);
        updateByCondition(clazz, condition, values);
    }

    @Override
    public Map<String, Object> auditTask(Filter filter, Page page) {
        return auditInstanceDao.auditTask(filter, page);
    }

    @Override
    public Map<String, Object> myAuditTask(Filter filter, Page page) {
        return auditInstanceDao.myAuditTask(filter, page);
    }

    @Override
    public Map<String, Object> finishedAuditTask(Filter filter, Page page) {
        return auditInstanceDao.finishedAuditTask(filter, page);
    }

    public void audit(AuditInstance audit, AuditProcessSetting aps, Long uid, Integer level, String msg, Integer result) throws Exception {
        // 审核不通过
        if (result.intValue() == AuditConstant.RS.REJECT) {
            // 表单数据，审核状态改为待提交
            Class clazz = Class.forName(audit.getEntityJavaClass());

            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("id", audit.getFormId());

            Map<String, Object> values = new HashMap<String, Object>();
            values.put("auditState", AuditConstant.RS.REJECT);

            updateByCondition(clazz, condition, values);

            if (level.intValue() == 1) {
                audit.setFirstAuditMsg(msg);
                audit.setFirstAuditResult(result);
                audit.setFirstAuditTime(new Date());
                audit.setFirstRealAuditUserId(uid);
            } else {
                audit.setSecondAuditMsg(msg);
                audit.setSecondAuditResult(result);
                audit.setSecondAuditTime(new Date());
                audit.setSecondRealAuditUserId(uid);
            }
            audit.setFinalResult(result);
            audit.setIsCompleted(AuditConstant.STATE.FINISHED);
        } else {// 审核通过
            if (level.intValue() == 1) {
                audit.setFirstAuditMsg(msg);
                audit.setFirstAuditResult(result);
                audit.setFirstAuditTime(new Date());
                audit.setFirstRealAuditUserId(uid);
                audit.setCurrentAuditProcessNode(2);
                // 如果审核级别是1，那么审核就结束了
                if (aps.getAuditLevel().intValue() == 1) {
                    audit.setFinalResult(result);
                    audit.setIsCompleted(AuditConstant.STATE.FINISHED);
                    // 更改产品状态，通过
                    Class clazz = Class.forName(audit.getEntityJavaClass());
                    Map<String, Object> condition = new HashMap<String, Object>();
                    condition.put("id", audit.getFormId());
                    Map<String, Object> values = new HashMap<String, Object>();
                    values.put("auditState", AuditConstant.RS.PASS);
                    updateByCondition(clazz, condition, values);
                }
            } else {// 如果是二级审核结果，那么就结束该审核
                audit.setSecondAuditMsg(msg);
                audit.setSecondAuditResult(result);
                audit.setSecondAuditTime(new Date());
                audit.setSecondRealAuditUserId(uid);
                audit.setFinalResult(result);
                audit.setIsCompleted(AuditConstant.STATE.FINISHED);
                audit.setCurrentAuditProcessNode(2);
                // 更改产品状态，通过
                Class clazz = Class.forName(audit.getEntityJavaClass());
                Map<String, Object> condition = new HashMap<String, Object>();
                condition.put("id", audit.getFormId());
                Map<String, Object> values = new HashMap<String, Object>();
                values.put("auditState", AuditConstant.RS.PASS);
                updateByCondition(clazz, condition, values);
            }
        }
        update(audit);
        msgService.createAuditState(audit);

        // 完全通过的审核
        if (audit.getIsCompleted() != null && audit.getIsCompleted() == 1 && audit.getFirstAuditResult() != null && audit.getFirstAuditResult() == AuditConstant.RS.PASS && (audit.getSecondAuditResult() == null || audit.getSecondAuditResult() == AuditConstant.RS.PASS)) {
            // 如果是生产任务的审核，那么改变订单的已分配数量
            if (audit.getAuditCode().equals("SC")) {
                // udpateSalesOrderAssignedCount(audit.getFormId());
                // 生产任务通过时，改变产品的工艺明细和包装明细
                //producePlanService.updateProductInfo(audit.getFormId());
                List<Map<String, Object>> list = producePlanDao.selectByFormId(audit.getFormId());
                for (Map<String, Object> map : list) {
                    //查询检测状态不为空的计划明细集合
                    List<ProducePlanDetail> pList = producePlanDao.findListGroupByMap(map.get("BATCHCODE").toString(), map.get("PRODUCTMODEL").toString());
                    if (pList.size() > 0) {
                        List<ProducePlanDetail> pdpList = findListByMap(ProducePlanDetail.class, map);
                        for (ProducePlanDetail p : pdpList) {
                            p.setProductStatus(pList.get(0).getProductStatus());//送样检测状态
                            auditInstanceDao.update(p);
                        }
                    }
                }
            }
            if (audit.getAuditCode().startsWith("RJH")) {
                setWeaveProducePlanIsSettled(audit.getFormId());
            }
            if (audit.getAuditCode().equals("CRJH")) {
                setCutProducePlanIsSettled(audit.getFormId());
            }
            // 如果是包材bom审核，改变使用这个bom的产品的信息
            if (audit.getAuditCode().startsWith("BC")) {
                Long bcBomVersionId = audit.getFormId();
                BCBomVersion bcv = findById(BCBomVersion.class, bcBomVersionId);
                BcBom bb = findById(BcBom.class, bcv.getPackBomId());
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("packBomId", bcBomVersionId);
                List<FinishedProduct> fpList = findListByMap(FinishedProduct.class, map);
                for (FinishedProduct fp : fpList) {
                    fp.setProductPackagingCode(bb.getPackBomGenericName() + "/" + bb.getPackBomCode());
                    fp.setProductPackageVersion(bcv.getPackVersion());
                    save(fp);
                }
            }
            // 如果是套材bom审核，改变使用这个bom的产品的信息
            if (audit.getAuditCode().startsWith("TC")) {
                Long tcBomVersionId = audit.getFormId();
                TcBomVersion tcv = findById(TcBomVersion.class, tcBomVersionId);
                TcBom tb = findById(TcBom.class, tcv.getTcProcBomId());
                HashMap<String, Object> map = new HashMap();
                map.put("procBomId", tcBomVersionId);
                map.put("productIsTc", 1);
                List<FinishedProduct> fpList = findListByMap(FinishedProduct.class, map);
                for (FinishedProduct fp : fpList) {
                    fp.setProductProcessCode(tb.getTcProcBomName() + "/" + tb.getTcProcBomCode());
                    fp.setProductProcessBomVersion(tcv.getTcProcBomVersionCode());
                    fp.setProductConsumerBomVersion(tcv.getTcConsumerVersionCode());
                    update(fp);
                }
            }
            // 如果是非套材bom审核，改变使用这个bom的产品的信息
            if (audit.getAuditCode().startsWith("FTC") && !audit.getAuditCode().startsWith("FTCBC")) {
                Long ftcBomVersionId = audit.getFormId();
                FtcBomVersion ftcv = findById(FtcBomVersion.class, ftcBomVersionId);
                FtcBom ftcb = findById(FtcBom.class, ftcv.getFtcProcBomId());
                HashMap<String, Object> map = new HashMap();
                map.put("procBomId", ftcBomVersionId);
                map.put("productIsTc", 2);
                List<FinishedProduct> fpList = findListByMap(FinishedProduct.class, map);
                for (FinishedProduct fp : fpList) {
                    fp.setProductProcessCode(ftcb.getFtcProcBomName() + "/" + ftcb.getFtcProcBomCode());
                    fp.setProductProcessBomVersion(ftcv.getFtcProcBomVersionCode());
                    fp.setProductConsumerBomVersion(ftcv.getFtcConsumerVersionCode());
                    update(fp);
                }
                map.put("productIsTc", -1);
                fpList = findListByMap(FinishedProduct.class, map);
                for (FinishedProduct fp : fpList) {
                    fp.setProductProcessCode(ftcb.getFtcProcBomName() + "/" + ftcb.getFtcProcBomCode());
                    fp.setProductProcessBomVersion(ftcv.getFtcProcBomVersionCode());
                    fp.setProductConsumerBomVersion(ftcv.getFtcConsumerVersionCode());
                    update(fp);
                }
            }

            // 如果是非套材包材bom审核，改变使用这个bom的产品的信息
            if (audit.getAuditCode().startsWith("FTCBC")) {
                Long ftcBcBomVersionId = audit.getFormId();
                FtcBcBomVersion fbcv = findById(FtcBcBomVersion.class, ftcBcBomVersionId);
                FtcBcBom bb = findById(FtcBcBom.class, fbcv.getBid());

                //找到同bom下的版本并设置为不可用
                Map<String, Object> param = new HashMap<>();
                param.put("bid", bb.getId());
                param.put("productType", fbcv.getProductType());
                param.put("auditState", 2);
                List<FtcBcBomVersion> list = findListByMap(FtcBcBomVersion.class, param);
                for (FtcBcBomVersion f : list) {
                    if (f.getId().longValue() != fbcv.getId()) {
                        f.setEnabled(1);
                        auditInstanceDao.update(f);
                    }
                }
            }

            if (audit.getAuditCode().startsWith("XS1") || audit.getAuditCode().startsWith("XS2") || audit.getAuditCode().startsWith("XS3") || audit.getAuditCode().startsWith("PBOrder")) {
                //全部审核完成通过订单信息保存bom的镜像数据
                HashMap<String, Object> map = new HashMap<>();
                map.put("salesOrderId", audit.getFormId());
                List<SalesOrderDetail> SalesOrderDetailList = findListByMap(SalesOrderDetail.class, map);
                //保存成品信息镜像数据
                for (SalesOrderDetail salesOrderDetail : SalesOrderDetailList) {
                    FinishedProduct finishedProduct = findById(FinishedProduct.class, salesOrderDetail.getProductId());
                    FinishedProductMirror finishedProductMirror = new FinishedProductMirror();
                    BeanUtils.copyProperties(finishedProduct, finishedProductMirror);
                    finishedProductMirror.setProductId(finishedProduct.getId());
                    finishedProductMirror.setGmtCreate(audit.getSecondAuditTime());
                    finishedProductMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                    finishedProductMirror.setSalesOrderDetailId(salesOrderDetail.getId());
                    save(finishedProductMirror);
                    if (salesOrderDetail.getProductIsTc() == ProductIsTc.TC) {//套材
                        TcBomVersion tcBomVersion = tcBomVersionDao.findById(TcBomVersion.class, finishedProduct.getProcBomId());
                        //保存套材bom镜像
                        TcBom tcBom = findById(TcBom.class, tcBomVersion.getTcProcBomId());
                        TcBomMirror tcBomMirror = new TcBomMirror();
                        BeanUtils.copyProperties(tcBom, tcBomMirror);
                        tcBomMirror.setTcProcBomId(tcBom.getId());
                        tcBomMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                        tcBomMirror.setGmtCreate(audit.getSecondAuditTime());
                        save(tcBomMirror);

                        //保存套材bom版本镜像
                        TcBomVersionMirror tcBomVersionMirror = new TcBomVersionMirror();
                        BeanUtils.copyProperties(tcBomVersion, tcBomVersionMirror);
                        tcBomVersionMirror.setGmtCreate(audit.getSecondAuditTime());
                        tcBomVersionMirror.setTcProcBomVersionId(tcBomVersion.getId());
                        tcBomVersionMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                        tcBomVersionMirror.setTcProcBomId(tcBomMirror.getId());
                        save(tcBomVersionMirror);
                        //镜像成品信息添加镜像版本id
                        finishedProductMirror.setProcBomId(tcBomVersionMirror.getId());
                        update(finishedProductMirror);

                        salesOrderDetail.setMirrorProcBomVersionId(tcBomVersionMirror.getId());
                        salesOrderDetail.setMirrorProductId(finishedProductMirror.getId());
                        update(salesOrderDetail);

                        map.clear();
                        map.put("salesOrderDetailId", salesOrderDetail.getId());
                        List<SalesOrderDetailPartsCount> salesOrderDetailPartsCountList = findListByMap(SalesOrderDetailPartsCount.class, map);

                        for (SalesOrderDetailPartsCount s : salesOrderDetailPartsCountList) {
                            TcBomVersionParts tcBomVersionParts = findById(TcBomVersionParts.class, s.getPartId());
                            //保存套材版本部件镜像
                            TcBomVersionPartsMirror tcBomVersionPartsMirror = new TcBomVersionPartsMirror();
                            BeanUtils.copyProperties(tcBomVersionParts, tcBomVersionPartsMirror);
                            tcBomVersionPartsMirror.setVersionPartsId(tcBomVersionParts.getId());
                            tcBomVersionPartsMirror.setGmtCreate(audit.getSecondAuditTime());
                            tcBomVersionPartsMirror.setTcProcBomVersoinId(tcBomVersionMirror.getId());
                            tcBomVersionPartsMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                            tcBomVersionPartsMirror.setSalesOrderDetailId(salesOrderDetail.getId());
                            save(tcBomVersionPartsMirror);
                            //部件明细镜像
                            map.clear();
                            map.put("tcProcBomPartsId", tcBomVersionParts.getId());
                            List<TcBomVersionPartsDetail> tcBomVersionPartsDetailList = findListByMap(TcBomVersionPartsDetail.class, map);
                            for (TcBomVersionPartsDetail t : tcBomVersionPartsDetailList) {
                                FinishedProduct tcfp = findById(FinishedProduct.class, t.getTcFinishedProductId());
                                FinishedProductMirror finishedProductMirror1 = new FinishedProductMirror();
                                BeanUtils.copyProperties(tcfp, finishedProductMirror1);
                                finishedProductMirror1.setProductId(tcfp.getId());
                                finishedProductMirror1.setGmtCreate(audit.getSecondAuditTime());
                                finishedProductMirror1.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                                finishedProductMirror1.setMirrorPartId(tcBomVersionParts.getId());
                                finishedProductMirror1.setSalesOrderDetailId(salesOrderDetail.getId());
                                save(finishedProductMirror1);

                                FtcBomVersion ftcBomVersion = ftcBcBomVersionDao.findById(FtcBomVersion.class, tcfp.getProcBomId());
                                FtcBom ftcBom = findById(FtcBom.class, ftcBomVersion.getFtcProcBomId());
                                if (ftcBomVersion.getAuditState() != 2) {
                                    throw new Exception("工艺代码：" + ftcBom.getFtcProcBomCode() + "没有审核通过");
                                }
                                FtcBomMirror ftcBomMirror = new FtcBomMirror();
                                BeanUtils.copyProperties(ftcBom, ftcBomMirror);
                                ftcBomMirror.setFtcProcBomID(ftcBom.getId());
                                ftcBomMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                                ftcBomMirror.setGmtCreate(audit.getSecondAuditTime());
                                save(ftcBomMirror);

                                FtcBomVersionMirror ftcBomVersionMirror = new FtcBomVersionMirror();
                                BeanUtils.copyProperties(ftcBomVersion, ftcBomVersionMirror);
                                ftcBomVersionMirror.setFtcProcBomVersionId(ftcBomVersion.getId());
                                ftcBomVersionMirror.setGmtCreate(audit.getSecondAuditTime());
                                ftcBomVersionMirror.setFtcProcBomId(ftcBomMirror.getId());
                                ftcBomVersionMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                                save(ftcBomVersionMirror);

                                finishedProductMirror1.setProcBomId(ftcBomVersionMirror.getId());
                                update(finishedProductMirror1);

                                map.clear();
                                map.put("ftcBomVersionId", ftcBomVersion.getId());
                                List<FtcBomDetail> bomDetails = findListByMap(FtcBomDetail.class, map);
                                for (FtcBomDetail b : bomDetails) {
                                    FtcBomDetailMirror ftcBomDetailMirror = new FtcBomDetailMirror();
                                    BeanUtils.copyProperties(b, ftcBomDetailMirror);
                                    ftcBomDetailMirror.setFtcBomVersionDetailId(b.getId());
                                    ftcBomDetailMirror.setGmtCreate(audit.getSecondAuditTime());
                                    ftcBomDetailMirror.setFtcBomVersionId(ftcBomVersionMirror.getId());
                                    ftcBomDetailMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                                    save(ftcBomDetailMirror);
                                }
                                TcBomVersionPartsDetailMirror tcBomVersionPartsDetailMirror = new TcBomVersionPartsDetailMirror();
                                BeanUtils.copyProperties(t, tcBomVersionPartsDetailMirror);
                                tcBomVersionPartsDetailMirror.setVersionPartsDetailMirrorId(t.getId());
                                tcBomVersionPartsDetailMirror.setGmtCreate(audit.getSecondAuditTime());
                                tcBomVersionPartsDetailMirror.setTcProcBomPartsId(tcBomVersionPartsMirror.getId());
                                tcBomVersionPartsDetailMirror.setTcFinishedProductId(finishedProductMirror1.getId());
                                tcBomVersionPartsDetailMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                                save(tcBomVersionPartsDetailMirror);
                            }

                            //部件成品重量胚布信息镜像
                            map.clear();
                            map.put("tcProcBomPartsId", tcBomVersionParts.getId());
                            List<TcBomVersionPartsFinishedWeightEmbryoCloth> tcvpfwList = findListByMap(TcBomVersionPartsFinishedWeightEmbryoCloth.class, map);
                            for (TcBomVersionPartsFinishedWeightEmbryoCloth tc : tcvpfwList) {
                                TcBomVersionPartsFinishedWeightEmbryoClothMirror tcMirror = new TcBomVersionPartsFinishedWeightEmbryoClothMirror();
                                BeanUtils.copyProperties(tc, tcMirror);
                                tcMirror.setEmbryoClothMirrorId(tc.getId());
                                tcMirror.setGmtCreate(audit.getSecondAuditTime());
                                tcMirror.setTcProcBomPartsId(tcBomVersionPartsMirror.getId());
                                tcMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                                save(tcMirror);
                            }
                            map.clear();
                            map.put("salesOrderDetailId", salesOrderDetail.getId());
                            map.put("partId", tcBomVersionParts.getId());
                            SalesOrderDetailPartsCount salesOrderDetailPartsCount = findUniqueByMap(SalesOrderDetailPartsCount.class, map);
                            salesOrderDetailPartsCount.setMirrorPartId(tcBomVersionPartsMirror.getId());
                            salesOrderDetailPartsCount.setMirrorTcProcBomVersionId(tcBomVersionMirror.getId());
                            update(salesOrderDetailPartsCount);
                        }
                    } else if (salesOrderDetail.getProductIsTc() == ProductIsTc.FTC) {//非套材或者胚布
                        FtcBomVersion ftcBomVersion = ftcBcBomVersionDao.findById(FtcBomVersion.class, salesOrderDetail.getProcBomId());
                        FtcBom ftcBom = findById(FtcBom.class, ftcBomVersion.getFtcProcBomId());
                        if (ftcBomVersion.getAuditState() != 2) {
                            throw new Exception("工艺代码：" + ftcBom.getFtcProcBomCode() + "没有审核通过");
                        }
                        FtcBomMirror ftcBomMirror = new FtcBomMirror();
                        BeanUtils.copyProperties(ftcBom, ftcBomMirror);
                        ftcBomMirror.setFtcProcBomID(ftcBom.getId());
                        ftcBomMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                        ftcBomMirror.setGmtCreate(audit.getSecondAuditTime());
                        save(ftcBomMirror);
                        FtcBomVersionMirror ftcBomVersionMirror = new FtcBomVersionMirror();
                        BeanUtils.copyProperties(ftcBomVersion, ftcBomVersionMirror);
                        ftcBomVersionMirror.setFtcProcBomVersionId(ftcBomVersion.getId());
                        ftcBomVersionMirror.setGmtCreate(ftcBomMirror.getGmtCreate());
                        ftcBomVersionMirror.setFtcProcBomId(ftcBomMirror.getId());
                        ftcBomVersionMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                        save(ftcBomVersionMirror);
                        //镜像成品信息添加镜像版本id
                        finishedProductMirror.setProcBomId(ftcBomVersionMirror.getId());
                        update(finishedProductMirror);
                        salesOrderDetail.setMirrorProcBomVersionId(ftcBomVersionMirror.getId());
                        salesOrderDetail.setMirrorProductId(finishedProductMirror.getId());
                        update(salesOrderDetail);

                        map.clear();
                        map.put("ftcBomVersionId", finishedProduct.getProcBomId());
                        List<FtcBomDetail> bomDetail = findListByMap(FtcBomDetail.class, map);
                        for (FtcBomDetail t : bomDetail) {
                            FtcBomDetailMirror ftcBomDetailMirror = new FtcBomDetailMirror();
                            BeanUtils.copyProperties(t, ftcBomDetailMirror);
                            ftcBomDetailMirror.setFtcBomVersionDetailId(t.getId());
                            ftcBomDetailMirror.setGmtCreate(ftcBomMirror.getGmtCreate());
                            ftcBomDetailMirror.setFtcBomVersionId(ftcBomVersionMirror.getId());
                            ftcBomDetailMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                            save(ftcBomDetailMirror);
                        }
                    } else if (salesOrderDetail.getProductIsTc() == ProductIsTc.PB) {
                        map.clear();
                        map.put("productBatchCode", salesOrderDetail.getProductBatchCode());
                        map.put("productId", salesOrderDetail.getProductId());
                        List<SalesOrderDetailTemp> sodList = ftcBcBomVersionDao.findListByMap(SalesOrderDetailTemp.class, map);
                        if (sodList.size() > 0 && null != salesOrderDetail.getMirrorProcBomVersionId()) {
                            FtcBomVersionMirror ftcBomVersionMirror = ftcBcBomVersionDao.findById(FtcBomVersionMirror.class, salesOrderDetail.getMirrorProcBomVersionId());
                            FtcBomMirror ftcBomMirror = findById(FtcBomMirror.class, ftcBomVersionMirror.getFtcProcBomId());
                            FtcBomMirror ftcBomMirror1 = new FtcBomMirror();
                            BeanUtils.copyProperties(ftcBomMirror, ftcBomMirror1);
                            ftcBomMirror1.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                            ftcBomMirror1.setGmtCreate(audit.getFirstAuditTime());
                            save(ftcBomMirror1);

                            FtcBomVersionMirror ftcBomVersionMirror1 = new FtcBomVersionMirror();
                            BeanUtils.copyProperties(ftcBomVersionMirror, ftcBomVersionMirror1);
                            ftcBomVersionMirror1.setGmtCreate(ftcBomMirror1.getGmtCreate());
                            ftcBomVersionMirror1.setFtcProcBomId(ftcBomMirror1.getId());
                            ftcBomVersionMirror1.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                            save(ftcBomVersionMirror1);

                            //镜像成品信息添加镜像版本id
                            finishedProductMirror.setGmtCreate(audit.getFirstAuditTime());
                            finishedProductMirror.setProcBomId(ftcBomVersionMirror1.getId());
                            update(finishedProductMirror);
                            salesOrderDetail.setMirrorProcBomVersionId(ftcBomVersionMirror1.getId());
                            salesOrderDetail.setMirrorProductId(finishedProductMirror.getId());
                            update(salesOrderDetail);

                            map.clear();
                            map.put("ftcBomVersionId", ftcBomVersionMirror.getId());
                            List<FtcBomDetailMirror> bomDetail = findListByMap(FtcBomDetailMirror.class, map);
                            for (FtcBomDetailMirror t : bomDetail) {
                                FtcBomDetailMirror ftcBomDetailMirror1 = new FtcBomDetailMirror();
                                BeanUtils.copyProperties(t, ftcBomDetailMirror1);
                                ftcBomDetailMirror1.setGmtCreate(ftcBomMirror1.getGmtCreate());
                                ftcBomDetailMirror1.setFtcBomVersionId(ftcBomVersionMirror1.getId());
                                ftcBomDetailMirror1.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                                save(ftcBomDetailMirror1);
                            }

                            TcBomVersionPartsMirror tcBomVersionPartsMirror = findById(TcBomVersionPartsMirror.class, salesOrderDetail.getMirrorPartId());
                            TcBomVersionMirror tcBomVersionMirror = findById(TcBomVersionMirror.class, tcBomVersionPartsMirror.getTcProcBomVersoinId());
                            TcBomMirror tcBomMirror = findById(TcBomMirror.class, tcBomVersionMirror.getTcProcBomId());
                            TcBomMirror tcBomMirror1 = new TcBomMirror();
                            BeanUtils.copyProperties(tcBomMirror, tcBomMirror1);
                            tcBomMirror1.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                            tcBomMirror1.setGmtCreate(audit.getSecondAuditTime());
                            save(tcBomMirror1);

                            //保存套材bom版本镜像
                            TcBomVersionMirror tcBomVersionMirror1 = new TcBomVersionMirror();
                            BeanUtils.copyProperties(tcBomVersionMirror, tcBomVersionMirror1);
                            tcBomVersionMirror1.setGmtCreate(audit.getSecondAuditTime());
                            tcBomVersionMirror1.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                            tcBomVersionMirror1.setTcProcBomId(tcBomMirror1.getId());
                            save(tcBomVersionMirror1);

                            //保存套材版本部件镜像
                            TcBomVersionPartsMirror tcBomVersionPartsMirror1 = new TcBomVersionPartsMirror();
                            BeanUtils.copyProperties(tcBomVersionPartsMirror, tcBomVersionPartsMirror1);
                            tcBomVersionPartsMirror1.setGmtCreate(audit.getSecondAuditTime());
                            tcBomVersionPartsMirror1.setTcProcBomVersoinId(tcBomVersionMirror1.getId());
                            tcBomVersionPartsMirror1.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                            tcBomVersionPartsMirror1.setSalesOrderDetailId(salesOrderDetail.getId());
                            save(tcBomVersionPartsMirror1);

                            salesOrderDetail.setMirrorPartId(tcBomVersionPartsMirror1.getId());
                            update(salesOrderDetail);
                        } else {
                            FtcBomVersion ftcBomVersion = ftcBcBomVersionDao.findById(FtcBomVersion.class, salesOrderDetail.getProcBomId());
                            FtcBom ftcBom = findById(FtcBom.class, ftcBomVersion.getFtcProcBomId());
                            if (ftcBomVersion.getAuditState() != 2) {
                                throw new Exception("工艺代码：" + ftcBom.getFtcProcBomCode() + "没有审核通过");
                            }
                            FtcBomMirror ftcBomMirror = new FtcBomMirror();
                            BeanUtils.copyProperties(ftcBom, ftcBomMirror);
                            ftcBomMirror.setFtcProcBomID(ftcBom.getId());
                            ftcBomMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                            if (salesOrderDetail.getProductIsTc() == ProductIsTc.FTC) {
                                ftcBomMirror.setGmtCreate(audit.getSecondAuditTime());
                            } else if (salesOrderDetail.getProductIsTc() == ProductIsTc.PB) {
                                ftcBomMirror.setGmtCreate(audit.getFirstAuditTime());
                                finishedProductMirror.setGmtCreate(audit.getFirstAuditTime());
                            }
                            save(ftcBomMirror);
                            FtcBomVersionMirror ftcBomVersionMirror = new FtcBomVersionMirror();
                            BeanUtils.copyProperties(ftcBomVersion, ftcBomVersionMirror);
                            ftcBomVersionMirror.setFtcProcBomVersionId(ftcBomVersion.getId());
                            ftcBomVersionMirror.setGmtCreate(ftcBomMirror.getGmtCreate());
                            ftcBomVersionMirror.setFtcProcBomId(ftcBomMirror.getId());
                            ftcBomVersionMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                            save(ftcBomVersionMirror);
                            //镜像成品信息添加镜像版本id
                            finishedProductMirror.setProcBomId(ftcBomVersionMirror.getId());
                            update(finishedProductMirror);
                            salesOrderDetail.setMirrorProcBomVersionId(ftcBomVersionMirror.getId());
                            salesOrderDetail.setMirrorProductId(finishedProductMirror.getId());
                            update(salesOrderDetail);

                            map.clear();
                            map.put("ftcBomVersionId", finishedProduct.getProcBomId());
                            List<FtcBomDetail> bomDetail = findListByMap(FtcBomDetail.class, map);
                            for (FtcBomDetail t : bomDetail) {
                                FtcBomDetailMirror ftcBomDetailMirror = new FtcBomDetailMirror();
                                BeanUtils.copyProperties(t, ftcBomDetailMirror);
                                ftcBomDetailMirror.setFtcBomVersionDetailId(t.getId());
                                ftcBomDetailMirror.setGmtCreate(ftcBomMirror.getGmtCreate());
                                ftcBomDetailMirror.setFtcBomVersionId(ftcBomVersionMirror.getId());
                                ftcBomDetailMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                                save(ftcBomDetailMirror);
                            }

                            if (null != salesOrderDetail.getPartId()) {
                                TcBomVersionParts tcBomVersionParts = findById(TcBomVersionParts.class, salesOrderDetail.getPartId());
                                TcBomVersion tcBomVersion = findById(TcBomVersion.class, tcBomVersionParts.getTcProcBomVersoinId());
                                TcBom tcBom = findById(TcBom.class, tcBomVersion.getTcProcBomId());
                                TcBomMirror tcBomMirror = new TcBomMirror();
                                BeanUtils.copyProperties(tcBom, tcBomMirror);
                                tcBomMirror.setTcProcBomId(tcBom.getId());
                                tcBomMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                                tcBomMirror.setGmtCreate(audit.getSecondAuditTime());
                                save(tcBomMirror);

                                //保存套材bom版本镜像
                                TcBomVersionMirror tcBomVersionMirror = new TcBomVersionMirror();
                                BeanUtils.copyProperties(tcBomVersion, tcBomVersionMirror);
                                tcBomVersionMirror.setGmtCreate(audit.getSecondAuditTime());
                                tcBomVersionMirror.setTcProcBomVersionId(tcBomVersion.getId());
                                tcBomVersionMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                                tcBomVersionMirror.setTcProcBomId(tcBomMirror.getId());
                                save(tcBomVersionMirror);

                                //保存套材版本部件镜像
                                TcBomVersionPartsMirror tcBomVersionPartsMirror = new TcBomVersionPartsMirror();
                                BeanUtils.copyProperties(tcBomVersionParts, tcBomVersionPartsMirror);
                                tcBomVersionPartsMirror.setVersionPartsId(tcBomVersionParts.getId());
                                tcBomVersionPartsMirror.setGmtCreate(audit.getSecondAuditTime());
                                tcBomVersionPartsMirror.setTcProcBomVersoinId(tcBomVersionMirror.getId());
                                tcBomVersionPartsMirror.setSalesOrderId(salesOrderDetail.getSalesOrderId());
                                tcBomVersionPartsMirror.setSalesOrderDetailId(salesOrderDetail.getId());
                                save(tcBomVersionPartsMirror);

                                salesOrderDetail.setMirrorPartId(tcBomVersionPartsMirror.getId());
                                update(salesOrderDetail);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 更新编织日计划关联的生产计划的已分配标志
     *
     * @param weaveDailyPlanId
     */
    public void setWeaveProducePlanIsSettled(Long weaveDailyPlanId) {
        producePlanService.setIsSettled(weaveDailyPlanId, null);
    }

    /**
     * 更新裁剪日计划关联的生产计划的已分配标志
     *
     * @param cutDailyPlanId
     */
    public void setCutProducePlanIsSettled(Long cutDailyPlanId) {
        producePlanService.setIsSettled(null, cutDailyPlanId);
    }

    public <T> void updateByCondition(Class<T> clazz, Map<String, Object> condition, Map<String, Object> values) {
        auditInstanceDao.updateByCondition(clazz, condition, values);
    }

    @Override
    public void reloadAudit(Long id, Integer type) {
        HashMap<String, Object> map = new HashMap();
        List<AuditInstance> ailist;
        switch (type) {
            case 1:
                map.put("formId", id);
                map.put("entityJavaClass", "com.bluebirdme.mes.sales.entity.SalesOrder");
                ailist = findListByMap(AuditInstance.class, map);
                for (AuditInstance ai : ailist) {
                    ai.setIsCompleted(1);
                    update(ai);
                }
                SalesOrder so = findById(SalesOrder.class, id);
                so.setAuditState(0);
                update(so);
                break;
            case 2:
                map.put("formId", id);
                map.put("entityJavaClass", "com.bluebirdme.mes.planner.produce.entity.ProducePlan");
                ailist = findListByMap(AuditInstance.class, map);
                for (AuditInstance ai : ailist) {
                    ai.setIsCompleted(1);
                    update(ai);
                }
                ProducePlan producePlan = findById(ProducePlan.class, id);
                producePlan.setAuditState(0);
                update(producePlan);
                break;
            case 3:
                break;
            case 4:
                map.put("formId", id);
                map.put("entityJavaClass", "com.bluebirdme.mes.planner.weave.entity.CutDailyPlan");
                ailist = findListByMap(AuditInstance.class, map);
                for (AuditInstance ai : ailist) {
                    ai.setIsCompleted(1);
                    update(ai);
                }
                CutDailyPlan cutPlan = findById(CutDailyPlan.class, id);
                cutPlan.setIsClosed(1);
                update(cutPlan);
                break;
        }
    }
}
