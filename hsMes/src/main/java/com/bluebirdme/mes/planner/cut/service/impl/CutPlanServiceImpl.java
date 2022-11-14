/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.cut.service.impl;

import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsDetail;
import com.bluebirdme.mes.baseInfo.entityMirror.FtcBomVersionMirror;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionPartsDetailMirror;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionPartsMirror;
import com.bluebirdme.mes.common.service.IProcessService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.cut.dao.ICutPlanDao;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.cut.entity.PlanUsers;
import com.bluebirdme.mes.planner.cut.service.ICutPlanService;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetailPartCount;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.sales.entity.SalesOrderDetailPartsCount;
import com.bluebirdme.mes.sales.entity.SalesOrderDetailTemp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MapUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author 宋黎明
 * @Date 2016-10-18 13:35:17
 */
@Service
@AnyExceptionRollback
public class CutPlanServiceImpl extends BaseServiceImpl implements ICutPlanService {
    private static final Logger log = LoggerFactory.getLogger(CutPlanServiceImpl.class);
    @Resource
    ICutPlanDao cutPlanDao;

    @Resource
    IProcessService processService;


    @Override
    protected IBaseDao getBaseDao() {
        return cutPlanDao;
    }

    @Override
    public void save(CutPlan cutPlan, Long[] userIds, String tcBomPartId) {
        //cutPlanDao.update(cutPlan);
        PlanUsers[] users = new PlanUsers[userIds.length];
        PlanUsers user = null;
        for (int i = 0; i < userIds.length; i++) {
            user = new PlanUsers();
            user.setUserId(userIds[i]);
            user.setTcBomPartId(Long.valueOf(tcBomPartId));
            user.setProducePlanDetailId(cutPlan.getId());
            users[i] = user;
        }
        cutPlanDao.save(users);
    }

    @Override
    public void updateUser(CutPlan cutPlan, Long[] userIds, String tcBomPartId) throws Exception {
        cutPlanDao.update2(cutPlan);
        cutPlanDao.deleteUserByPartId(Long.valueOf(tcBomPartId));
        PlanUsers[] users = new PlanUsers[userIds.length];
        PlanUsers user = null;
        for (int i = 0; i < userIds.length; i++) {
            user = new PlanUsers();
            user.setUserId(userIds[i]);
            user.setTcBomPartId(Long.valueOf(tcBomPartId));
            user.setProducePlanDetailId(cutPlan.getId());
            user.setPlanUserDate(DateFormat.getDateInstance().format(new Date()));
            users[i] = user;
        }
        cutPlanDao.save(users);
    }

    @Override
    public List<Map<String, Object>> userListById(String id) throws Exception {
        return cutPlanDao.userListById(id);
    }

    public void add(ProducePlan producePlan) throws Exception {
        List<ProducePlanDetail> producePlanDetail = producePlan.getList();
        List<CutPlan> cutPlanList = new ArrayList<CutPlan>();
        for (ProducePlanDetail ppd : producePlanDetail) {
            if (ppd.getProductIsTc() == 1) {
                CutPlan cutPlan = new CutPlan();
                cutPlan.setIsCreatWeave(0);
                cutPlan.setBcBomCode(ppd.getBcBomCode());
                cutPlan.setBcBomVersion(ppd.getBcBomVersion());
                cutPlan.setSalesOrderCode(ppd.getSalesOrderCode());
                cutPlan.setProducePlanDetailId(ppd.getId());
                cutPlan.setBatchCode(ppd.getBatchCode());
                cutPlan.setSort(ppd.getSort());
                cutPlan.setIsFinished(-1);
                cutPlan.setComment(ppd.getComment());
                cutPlan.setConsumerId(ppd.getConsumerId());
                cutPlan.setConsumerName(ppd.getConsumerName());
                cutPlan.setDeleveryDate(ppd.getDeleveryDate());
                cutPlan.setDeviceCode(ppd.getDeviceCode());
                cutPlan.setFromSalesOrderDetailId(ppd.getFromSalesOrderDetailId());
                cutPlan.setFromTcId(ppd.getFromTcId());
                cutPlan.setFromTcName(ppd.getFromTcName());
                cutPlan.setOrderCount(ppd.getOrderCount());
                cutPlan.setPackagedCount(ppd.getPackagedCount());
                //cutPlan.setPartId();
                cutPlan.setPlanCode(producePlan.getProducePlanCode());
                cutPlan.setProcessBomCode(ppd.getProcessBomCode());
                cutPlan.setProcessBomVersion(ppd.getProcessBomVersion());
                cutPlan.setProducedCount(ppd.getProducedCount());
                cutPlan.setProductId(ppd.getProductId());
                cutPlan.setProductLength(ppd.getProductLength());
                cutPlan.setProductModel(ppd.getProductModel());
                cutPlan.setProductName(ppd.getFactoryProductName());
                cutPlan.setProductType(ppd.getProductType());
                cutPlan.setProductWeight(ppd.getProductWeight());
                cutPlan.setProductWidth(ppd.getProductWidth());
                cutPlan.setRequirementCount(ppd.getRequirementCount());
                cutPlan.setTotalRollCount(ppd.getTotalRollCount());
                cutPlan.setTotalTrayCount(ppd.getTotalTrayCount());
                cutPlan.setProcBomId(ppd.getProcBomId());
                cutPlan.setPackBomId(ppd.getPackBomId());
                if (null != ppd.getMirrorProcBomId()) {
                    cutPlan.setMirrorProcBomId(ppd.getMirrorProcBomId());
                }
                cutPlanList.add(cutPlan);
                createSailOrder(cutPlan);
            }
        }
        cutPlanDao.save(cutPlanList.toArray(new CutPlan[]{}));
    }

    public void createSailOrder(CutPlan cutPlan) throws Exception {
        List<SalesOrderDetailTemp> detailTemps = getFabs(cutPlan);
        if (detailTemps != null) {
            cutPlanDao.save(detailTemps.toArray(new SalesOrderDetailTemp[]{}));
        }
    }

    public List<SalesOrderDetailTemp> getFabs(CutPlan plan) {
        List<SalesOrderDetailTemp> list = new ArrayList<SalesOrderDetailTemp>();
        if (null != plan.getMirrorProcBomId()) {
            HashMap<String, Object> part = new HashMap<String, Object>();
            part.put("tcProcBomVersoinId", plan.getMirrorProcBomId());
            //套材部件列表
            List<TcBomVersionPartsMirror> partList = cutPlanDao.findListByMap(TcBomVersionPartsMirror.class, part);
            //套材部件明细
            List<TcBomVersionPartsDetailMirror> tcBomList = new ArrayList<TcBomVersionPartsDetailMirror>();
            for (TcBomVersionPartsMirror p : partList) {
                if (p.getTcProcBomVersionPartsType().equals("成品胚布")) continue;
                part.clear();
                part.put("tcProcBomPartsId", p.getId());
                List<TcBomVersionPartsDetailMirror> list1 = cutPlanDao.findListByMap(TcBomVersionPartsDetailMirror.class, part);
                for (TcBomVersionPartsDetailMirror pdm : list1) {
                    pdm.setFromTcProcBomPartsId(p.getVersionPartsId());
                    update(pdm);
                    tcBomList.add(pdm);
                }
//				tcBomList.addAll(cutPlanDao.findListByMap(TcBomVersionPartsDetailMirror.class, part));
            }
            if (tcBomList.size() == 0) {
                return null;
            }

            ProducePlanDetail ppd = cutPlanDao.findById(ProducePlanDetail.class, plan.getProducePlanDetailId());
            part.clear();
            part.put("planDetailId", ppd.getId());
            List<ProducePlanDetailPartCount> ppdpcList = cutPlanDao.findListByMap(ProducePlanDetailPartCount.class, part);


            SalesOrderDetailTemp sd = null;
            TcBomVersionPartsMirror partInfo = null;

            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = tcBomList.size() - 1; i >= 0; i--) {
                double count = 0;
                for (ProducePlanDetailPartCount ppdpc : ppdpcList) {
                    if (ppdpc.getPartId().equals(tcBomList.get(i).getFromTcProcBomPartsId())) {
                        count = ppdpc.getPlanPartCount();
                    }
                }
                FinishedProductMirror fp = cutPlanDao.findById(FinishedProductMirror.class, tcBomList.get(i).getTcFinishedProductId());
                FtcBomVersionMirror ftcBomVersionMirror = cutPlanDao.findById(FtcBomVersionMirror.class, plan.getMirrorProcBomId());
                if (count * tcBomList.get(i).getTcProcBomFabricCount() > 0) {
                    sd = new SalesOrderDetailTemp();
                    sd.setProductId(fp.getProductId());
                    sd.setSalesOrderSubCode("cj-" + plan.getSalesOrderCode());
                    sd.setProductBatchCode(plan.getBatchCode());
                    sd.setConsumerProductName(fp.getConsumerProductName());
                    sd.setFactoryProductName(fp.getFactoryProductName());
                    sd.setProductWidth(fp.getProductWidth());
                    sd.setProductRollLength(fp.getProductRollLength());
                    sd.setProductRollWeight(fp.getProductRollWeight());
                    sd.setProductProcessCode(fp.getProductProcessCode());
                    sd.setProductProcessBomVersion(fp.getProductProcessBomVersion());
                    sd.setProductPackagingCode(fp.getProductPackagingCode());
                    sd.setProductPackageVersion(fp.getProductPackageVersion());
                    sd.setProductRollCode(fp.getProductRollCode());
                    sd.setProductBoxCode(fp.getProductBoxCode());
                    sd.setProductTrayCode(fp.getProductTrayCode());
                    sd.setProductModel(fp.getProductModel());
                    sd.setProductCount(count * tcBomList.get(i).getTcProcBomFabricCount());
                    sd.setProductIsTc(fp.getProductIsTc());
                    sd.setProcBomId(ftcBomVersionMirror.getFtcProcBomVersionId());
                    sd.setPackBomId(fp.getPackBomId());
                    /*新增图号，卷号，层号,部件ID*/
                    sd.setLevelNo(tcBomList.get(i).getLevelNo());
                    sd.setDrawNo(tcBomList.get(i).getDrawingNo());
                    sd.setRollNo(tcBomList.get(i).getRollNo());
                    sd.setPartId(tcBomList.get(i).getFromTcProcBomPartsId());
                    sd.setMirrorPartId(tcBomList.get(i).getTcProcBomPartsId());
                    sd.setMirrorFtcProcBomId(fp.getProcBomId());
                    sd.setMirrorProductId(fp.getId());
                    sd.setSorting(tcBomList.get(i).getSorting());
                    partInfo = cutPlanDao.findById(TcBomVersionPartsMirror.class, tcBomList.get(i).getTcProcBomPartsId());
                    sd.setPartName(partInfo.getTcProcBomVersionPartsName());
                    list.add(sd);
                }
            }
        } else {//兼容
            HashMap<String, Object> part = new HashMap<String, Object>();
            part.put("tcProcBomVersoinId", plan.getProcBomId());
            //套材部件列表
            List<TcBomVersionParts> partList = cutPlanDao.findListByMap(TcBomVersionParts.class, part);
            //套材部件明细
            List<TcBomVersionPartsDetail> tcBomList = new ArrayList<TcBomVersionPartsDetail>();
            for (TcBomVersionParts p : partList) {
                if (p.getTcProcBomVersionPartsType().equals("成品胚布")) continue;
                part.clear();
                part.put("tcProcBomPartsId", p.getId());
                tcBomList.addAll(cutPlanDao.findListByMap(TcBomVersionPartsDetail.class, part));
            }
            if (tcBomList.size() == 0) {
                return null;
            }

            ProducePlanDetail ppd = cutPlanDao.findById(ProducePlanDetail.class, plan.getProducePlanDetailId());
            part.clear();
            part.put("planDetailId", ppd.getId());
            List<ProducePlanDetailPartCount> ppdpcList = cutPlanDao.findListByMap(ProducePlanDetailPartCount.class, part);


            SalesOrderDetailTemp sd = null;
            TcBomVersionParts partInfo = null;

            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = tcBomList.size() - 1; i >= 0; i--) {
                double count = 0;
                for (ProducePlanDetailPartCount ppdpc : ppdpcList) {
                    if (ppdpc.getPartId().equals(tcBomList.get(i).getTcProcBomPartsId())) {
                        count = ppdpc.getPlanPartCount();
                    }
                }
                FinishedProduct fp = cutPlanDao.findById(FinishedProduct.class, tcBomList.get(i).getTcFinishedProductId());

                if (count * tcBomList.get(i).getTcProcBomFabricCount() > 0) {
                    sd = new SalesOrderDetailTemp();
                    sd.setProductId(fp.getId());
                    sd.setSalesOrderSubCode("cj-" + plan.getSalesOrderCode());
                    sd.setProductBatchCode(plan.getBatchCode());
                    sd.setConsumerProductName(fp.getConsumerProductName());
                    sd.setFactoryProductName(fp.getFactoryProductName());
                    sd.setProductWidth(fp.getProductWidth());
                    sd.setProductRollLength(fp.getProductRollLength());
                    sd.setProductRollWeight(fp.getProductRollWeight());
                    sd.setProductProcessCode(fp.getProductProcessCode());
                    sd.setProductProcessBomVersion(fp.getProductProcessBomVersion());
                    sd.setProductPackagingCode(fp.getProductPackagingCode());
                    sd.setProductPackageVersion(fp.getProductPackageVersion());
                    sd.setProductRollCode(fp.getProductRollCode());
                    sd.setProductBoxCode(fp.getProductBoxCode());
                    sd.setProductTrayCode(fp.getProductTrayCode());
                    sd.setProductModel(fp.getProductModel());
                    sd.setProductCount(count * tcBomList.get(i).getTcProcBomFabricCount());
                    sd.setProductIsTc(fp.getProductIsTc());
                    sd.setProcBomId(fp.getProcBomId());
                    sd.setPackBomId(fp.getPackBomId());
                    /*新增图号，卷号，层号,部件ID*/
                    sd.setLevelNo(tcBomList.get(i).getLevelNo());
                    sd.setDrawNo(tcBomList.get(i).getDrawingNo());
                    sd.setRollNo(tcBomList.get(i).getRollNo());
                    sd.setPartId(tcBomList.get(i).getTcProcBomPartsId());
                    sd.setSorting(tcBomList.get(i).getSorting());
                    partInfo = cutPlanDao.findById(TcBomVersionParts.class, tcBomList.get(i).getTcProcBomPartsId());
                    sd.setPartName(partInfo.getTcProcBomVersionPartsName());
                    list.add(sd);
                }
            }
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> planCodeCombobox() throws Exception {
        List<Map<String, Object>> planCodeList = cutPlanDao.planCodeCombobox();
        List<Map<String, Object>> PlanCodecomboList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < planCodeList.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("value", planCodeList.get(i).get("ID"));
            map.put("text", planCodeList.get(i).get("PRODUCEPLANCODE"));
            PlanCodecomboList.add(map);
        }
        return PlanCodecomboList;
    }

    @Override
    public Map<String, Object> dataList(Filter filter, Page page) throws Exception {
        return cutPlanDao.dataList(filter, page);
    }

    @Override
    public List<Map<String, Object>> findCutPlan(String planCode) throws Exception {
        List<Map<String, Object>> list = cutPlanDao.findCutPlan(planCode);
        Map<String, Long> bomPartCount = null;
        Map<String, Object> producedPartCount = null;
        int suitCount = Integer.MAX_VALUE;
        BigDecimal b1;
        BigDecimal b2;
        Long bomCount;
        Long producedCount;
        Iterator<Entry<String, Long>> it;
        Entry<String, Long> ent;

        for (Map<String, Object> detail : list) {
            //BOM需求数量
            bomPartCount = getPartCount(MapUtils.getAsLong(detail, "PRODUCEPLANDETAILID"));
            producedPartCount = processService.getProducedPartCount(MapUtils.getAsLong(detail, "PRODUCEPLANDETAILID"));
            it = bomPartCount.entrySet().iterator();
            while (it.hasNext()) {
                ent = it.next();
                bomCount = (Long) ent.getValue();
                producedCount = (Long) producedPartCount.get(ent.getKey());
                if (producedCount == null || producedCount == 0L) {
                    suitCount = 0;
                    break;
                } else {
                    b1 = new BigDecimal(bomCount);
                    b2 = new BigDecimal(producedCount);
                    if (b2.divide(b1, 0, BigDecimal.ROUND_DOWN).intValue() < suitCount) {
                        suitCount = b2.divide(b1, 0, BigDecimal.ROUND_DOWN).intValue();
                    }
                }
            }
            detail.put("TS", suitCount);
        }
        return list;
    }


    public Map<String, Long> getPartCount(Long planDetailId) {

        Map<String, Object> param = new HashMap<String, Object>();
        List<ProducePlanDetailPartCount> parts = null;
        param.clear();
        param.put("planDetailId", planDetailId);

        parts = findListByMap(ProducePlanDetailPartCount.class, param);

        Map<String, Long> ret = new HashMap<String, Long>();

        for (ProducePlanDetailPartCount part : parts) {
            ret.put(part.getPartName(), part.getPartBomCount().longValue());
        }
        return ret;
    }

    public Map<String, Long> getPlanPartCount(Long salesOrderDetailId) {

        Map<String, Object> param = new HashMap<String, Object>();
        List<SalesOrderDetailPartsCount> parts = null;
        param.clear();
        param.put("salesOrderDetailId", salesOrderDetailId);

        parts = findListByMap(SalesOrderDetailPartsCount.class, param);

        Map<String, Long> ret = new HashMap<String, Long>();

        for (SalesOrderDetailPartsCount part : parts) {
            ret.put(part.getPartName(), part.getPartBomCount().longValue());
        }

        return ret;
    }

    @Override
    public void deleteCutPlans(String ids) {
        cutPlanDao.deleteCutPlansUsers(ids);
        cutPlanDao.deleteCutPlans(ids);
    }

    @Override
    public List<HashMap<String, Object>> findCutPlanUserByUserID(Long userId) throws Exception {
        return cutPlanDao.findCutPlanUserByUserID(userId);
    }

    @Override
    public void updateState(String ids) throws Exception {
        String[] li = ids.split(",");
        List<CutPlan> list = new ArrayList<CutPlan>();
        for (int i = 0; i < li.length; i++) {
            CutPlan cutPlan = cutPlanDao.findById(CutPlan.class, Long.valueOf(li[i]));
            if (cutPlan.getIsFinished() == -1) {
                cutPlan.setIsFinished(1);
            } else {
                cutPlan.setIsFinished(-1);
            }

            list.add(cutPlan);
        }
        cutPlanDao.update2(list.toArray(new CutPlan[]{}));
    }

    @Override
    public Map<String, Object> findCutPageInfo(Filter filter, Page page) {
        Map<String, Object> ret = cutPlanDao.findCutPageInfo(filter, page);
        List<Map<String, Object>> list = (List<Map<String, Object>>) ret.get("rows");
        return ret;
    }
}

