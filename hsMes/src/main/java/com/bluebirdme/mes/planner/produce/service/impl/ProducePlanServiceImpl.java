/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.produce.service.impl;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.baseInfo.entity.FtcBomDetail;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsDetail;
import com.bluebirdme.mes.baseInfo.entityMirror.FtcBomDetailMirror;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionPartsDetailMirror;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionPartsMirror;
import com.bluebirdme.mes.baseInfo.service.IBomService;
import com.bluebirdme.mes.common.service.IProcessService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.cut.service.ICutPlanService;
import com.bluebirdme.mes.planner.material.service.IMrpService;
import com.bluebirdme.mes.planner.pack.entity.PackTask;
import com.bluebirdme.mes.planner.pack.service.IPackTaskService;
import com.bluebirdme.mes.planner.produce.dao.IProducePlanDao;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetailPartCount;
import com.bluebirdme.mes.planner.produce.service.IProducePlanService;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlanDetails;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;
import com.bluebirdme.mes.platform.service.IDepartmentService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.sales.entity.SalesOrderDetailPartsCount;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import com.bluebirdme.mes.utils.ProductIsTc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.ListUtils;
import org.xdemo.superutil.j2se.MapUtils;
import org.xdemo.superutil.j2se.MathUtils;
import org.xdemo.superutil.j2se.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author 高飞
 * @Date 2016-11-28 21:25:48
 */
@Service
@AnyExceptionRollback
public class ProducePlanServiceImpl extends BaseServiceImpl implements IProducePlanService {
    private static final Logger log = LoggerFactory.getLogger(ProducePlanServiceImpl.class);

    @Resource
    IProducePlanDao producePlanDao;
    @Resource
    ICutPlanService cutService;
    @Resource
    IWeavePlanService weaveService;
    @Resource
    IBomService bomService;
    @Resource
    IProcessService processService;
    @Resource
    ISalesOrderService orderService;
    @Resource
    IAuditInstanceService auditService;
    @Resource
    IMrpService mrpService;
    @Resource
    IPackTaskService ptService;
    @Resource
    IDepartmentService departmentService;

    @Override
    protected IBaseDao getBaseDao() {
        return producePlanDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return producePlanDao.findPageInfo(filter, page);
    }

    public Map<String, Object> findOrderPageInfo(Filter filter, Page page) throws Exception {
        return producePlanDao.findOrderPageInfo(filter, page);
    }

    @Override
    public void savePlan(ProducePlan plan) {
        super.save(plan);
        List<ProducePlanDetail> ppdList = plan.getList();
        SalesOrderDetail order;
        Map<String, Object> map = new HashMap();
        FinishedProductMirror productm;
        FinishedProduct product;
        Double orderWeight;
        TcBomVersionPartsMirror tvpm;
        TcBomVersionParts tvp;
        for (ProducePlanDetail ppd : ppdList) {
            orderWeight = 0D;
            SalesOrderDetail salesOrderDetail = findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
            if (null != salesOrderDetail.getMirrorProductId()) {
                productm = findById(FinishedProductMirror.class, salesOrderDetail.getMirrorProductId());
                // 套材订单
                if (ppd.getProductIsTc() == ProductIsTc.TC) {
                    for (ProducePlanDetailPartCount c : ppd.getList()) {
                        map.clear();
                        map.put("versionPartsId", c.getPartId());
                        map.put("salesOrderDetailId", c.getSalesOrderDetailId());
                        List<TcBomVersionPartsMirror> tcBomVersionPartsMirrorList = findListByMap(TcBomVersionPartsMirror.class, map);
                        tvpm = tcBomVersionPartsMirrorList.get(0);
                        orderWeight += tvpm.getTcProcBomVersionPartsWeight() * c.getPlanPartCount();
                    }
                    ppd.setMirrorProcBomId(productm.getProcBomId());
                    ppd.setMirrorFromTcId(productm.getId());
                } else if (ppd.getProductIsTc() == ProductIsTc.FTC) {// 非套材订单
                    if (productm.getProductRollWeight() != null) {//卷重不等于零
                        orderWeight = MathUtils.add(ppd.getRequirementCount() * productm.getProductRollWeight(), 0D, 2);
                    } else {
                        List<FtcBomDetailMirror> bomDetails = null;
                        map.clear();
                        map.put("ftcBomVersionId", productm.getProcBomId());
                        bomDetails = findListByMap(FtcBomDetailMirror.class, map);
                        Double bomWeight = 0D;
                        for (FtcBomDetailMirror d : bomDetails) {
                            bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                        }
                        // 米长*门幅*单位面积克重*卷数
                        orderWeight = MathUtils.add(bomWeight * ppd.getRequirementCount() * ppd.getProductWidth() * ppd.getProductLength(), 0D, 2);
                        orderWeight = MathUtils.div(orderWeight, 1000000, 2);
                    }
                    ppd.setMirrorProcBomId(productm.getProcBomId());
                    ppd.setMirrorFromTcId(productm.getId());
                } else {// 胚布订单
                    List<FtcBomDetailMirror> bomDetails = null;
                    map.clear();
                    map.put("ftcBomVersionId", productm.getProcBomId());
                    bomDetails = findListByMap(FtcBomDetailMirror.class, map);
                    if (bomDetails.size() != 0) {
                        Double bomWeight = 0D;
                        for (FtcBomDetailMirror d : bomDetails) {
                            bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                        }
                        // 米长*门幅*单位面积克重*卷数
                        orderWeight = MathUtils.add(bomWeight * ppd.getRequirementCount() * ppd.getProductWidth() * ppd.getProductLength(), 0D, 2);
                        orderWeight = MathUtils.div(orderWeight, 1000000, 2);
                        ppd.setMirrorProcBomId(bomDetails.get(0).getFtcBomVersionId());
                        ppd.setMirrorFromTcId(productm.getId());
                    } else {
                        log.info("成品id为：" + productm.getProductId() + "；成品镜像id为：" + productm.getId() + "的产品为外购产品；");
                        continue;
                    }
                }
                ppd.setProducePlanId(plan.getId());
                ppd.setPlanCode(plan.getProducePlanCode());
                ppd.setPlanTotalWeight(orderWeight);
                ppd.setBatchCode(ppd.getBatchCode().trim());//季晓龙 add 批次号去掉空格
                save(ppd);
                order = orderService.findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
                order.setAllocateCount(MathUtils.add((order.getAllocateCount() == null ? 0 : order.getAllocateCount()) + orderWeight, 0D, 2));
                order.setIsPlaned(1);
                update(order);

                for (ProducePlanDetailPartCount c : ppd.getList()) {
                    map.clear();
                    map.put("versionPartsId", c.getPartId());
                    map.put("tcProcBomVersoinId", ppd.getMirrorProcBomId());
                    map.put("salesOrderDetailId", c.getSalesOrderDetailId());
                    List<TcBomVersionPartsMirror> tcBomVersionPartsMirrorList = findListByMap(TcBomVersionPartsMirror.class, map);
                    c.setPlanDetailId(ppd.getId());
                    if (null != tcBomVersionPartsMirrorList.get(0)) {
                        c.setMirrorPartId(tcBomVersionPartsMirrorList.get(0).getId());
                    }
                    save(c);
                }
            } else {//与原来数据兼容
                orderWeight = 0D;
                // 套材订单
                if (ppd.getProductIsTc() == 1) {
                    for (ProducePlanDetailPartCount c : ppd.getList()) {
                        tvp = findById(TcBomVersionParts.class, c.getPartId());
                        orderWeight += tvp.getTcProcBomVersionPartsWeight() * c.getPlanPartCount();
                    }
                } else if (ppd.getProductIsTc() == 2) {// 非套材订单
                    product = findById(FinishedProduct.class, ppd.getProductId());
                    if (product.getProductRollWeight() != null) {//卷重不等于零
                        orderWeight = MathUtils.add(ppd.getRequirementCount() * product.getProductRollWeight(), 0D, 2);
                    } else {
                        List<FtcBomDetail> bomDetails;
                        map.clear();
                        map.put("ftcBomVersionId", ppd.getProcBomId());
                        bomDetails = findListByMap(FtcBomDetail.class, map);
                        Double bomWeight = 0D;
                        for (FtcBomDetail d : bomDetails) {
                            bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                        }
                        // 米长*门幅*单位面积克重*卷数
                        orderWeight = MathUtils.add(bomWeight * ppd.getRequirementCount() * ppd.getProductWidth() * ppd.getProductLength(), 0D, 2);
                        orderWeight = MathUtils.div(orderWeight, 1000000, 2);
                    }
                } else {// 胚布订单
                    List<FtcBomDetail> bomDetails;
                    map.clear();
                    map.put("ftcBomVersionId", ppd.getProcBomId());
                    bomDetails = findListByMap(FtcBomDetail.class, map);
                    Double bomWeight = 0D;
                    for (FtcBomDetail d : bomDetails) {
                        bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                    }
                    // 米长*门幅*单位面积克重*卷数
                    orderWeight = MathUtils.add(bomWeight * ppd.getRequirementCount() * ppd.getProductWidth() * ppd.getProductLength(), 0D, 2);
                    orderWeight = MathUtils.div(orderWeight, 1000000, 2);
                }

                ppd.setProducePlanId(plan.getId());
                ppd.setPlanCode(plan.getProducePlanCode());
                ppd.setPlanTotalWeight(orderWeight);
                ppd.setBatchCode(ppd.getBatchCode().trim());//季晓龙 add 批次号去掉空格
                save(ppd);

                order = orderService.findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
                order.setAllocateCount(MathUtils.add((order.getAllocateCount() == null ? 0 : order.getAllocateCount()) + orderWeight, 0D, 2));
                order.setIsPlaned(1);
                update(order);

                for (ProducePlanDetailPartCount c : ppd.getList()) {
                    c.setPlanDetailId(ppd.getId());
                    save(c);
                }
            }
        }
    }

    public void updatePlan(ProducePlan plan) throws Exception {
        super.update(plan);
        Map<String, Object> param = new HashMap();
        param.put("producePlanId", plan.getId());
        // 前端传过来的计划明细
        List<ProducePlanDetail> ppdList = plan.getList();
        // 数据库保存的计划明细
        List<ProducePlanDetail> dbList = producePlanDao.findListByMap(ProducePlanDetail.class, param);
        List<Long> oldIds = new ArrayList();
        List<Long> newIds = new ArrayList();
        SalesOrderDetail order;
        // 还原数量
        for (ProducePlanDetail ppd : dbList) {
            order = findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
            order.setAllocateCount((order.getAllocateCount() == null ? 0 : order.getAllocateCount()) - (ppd.getPlanTotalWeight() == null ? 0 : ppd.getPlanTotalWeight()));
            if (order.getAllocateCount() < 0)
                order.setAllocateCount(0D);
            update(order);
            oldIds.add(ppd.getId());
        }
        delete(ProducePlanDetail.class, param);
        Map<String, Object> map = new HashMap();
        FinishedProductMirror productm;
        FinishedProduct product;
        Double orderWeight;
        TcBomVersionPartsMirror tvpm;
        TcBomVersionParts tvp;
        List<TurnBagPlanDetails> list;
        // 重新计算数量
        for (ProducePlanDetail ppd : ppdList) {
            List<PackTask> tasks = new ArrayList();
            if (ppd.getId() != null) {
                newIds.add(ppd.getId());
                map.clear();
                map.put("ppdId", ppd.getId());
                tasks = ptService.findListByMap(PackTask.class, map);
            }
            map.clear();
            map.put("producePlanDetailId", ppd.getId());
            list = findListByMap(TurnBagPlanDetails.class, map);
            ppd.setBatchCode(ppd.getBatchCode().trim());//季晓龙 add 批次号去掉空格
            save(ppd);
            // 重新关联翻包任务到新的计划ID
            for (TurnBagPlanDetails detail : list) {
                detail.setProducePlanDetailId(ppd.getId());
                update(detail);
            }
            //重新关联新的计划ID
            for (PackTask pt : tasks) {
                pt.setPpdId(ppd.getId());
                update(pt);
            }
            // 删除被删除掉的计划的翻包任务
            orderWeight = 0D;
            SalesOrderDetail salesOrderDetail = findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
            if (null != ppd.getMirrorProcBomId()) {
                productm = findById(FinishedProductMirror.class, salesOrderDetail.getMirrorProductId());
                // 套材订单
                if (ppd.getProductIsTc() == ProductIsTc.TC) {
                    for (ProducePlanDetailPartCount c : ppd.getList()) {
                        map.clear();
                        map.put("versionPartsId", c.getPartId());
                        map.put("salesOrderDetailId", c.getSalesOrderDetailId());
                        List<TcBomVersionPartsMirror> tcBomVersionPartsMirrorList = findListByMap(TcBomVersionPartsMirror.class, map);
                        tvpm = tcBomVersionPartsMirrorList.get(0);
                        orderWeight += tvpm.getTcProcBomVersionPartsWeight() * c.getPlanPartCount();
                    }
                    ppd.setMirrorProcBomId(productm.getProcBomId());
                    ppd.setMirrorFromTcId(productm.getId());
                } else if (ppd.getProductIsTc() == ProductIsTc.FTC) {// 非套材订单
                    if (productm.getProductRollWeight() != null) {
                        orderWeight = MathUtils.add(ppd.getRequirementCount() * productm.getProductRollWeight(), 0D, 2);
                    } else {
                        List<FtcBomDetailMirror> bomDetails;
                        map.clear();
                        map.put("ftcBomVersionId", productm.getProcBomId());
                        bomDetails = findListByMap(FtcBomDetailMirror.class, map);
                        Double bomWeight = 0D;
                        for (FtcBomDetailMirror d : bomDetails) {
                            bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                        }
                        // 米长*门幅*单位面积克重*卷数
                        orderWeight = MathUtils.add(bomWeight * ppd.getRequirementCount() * ppd.getProductWidth() * ppd.getProductLength(), 0D, 2);
                        orderWeight = MathUtils.div(orderWeight, 1000000, 2);
                    }
                    ppd.setMirrorProcBomId(productm.getProcBomId());
                    ppd.setMirrorFromTcId(productm.getId());
                } else {// 胚布订单
                    List<FtcBomDetailMirror> bomDetails;
                    map.clear();
                    map.put("ftcBomVersionId", productm.getProcBomId());
                    bomDetails = findListByMap(FtcBomDetailMirror.class, map);
                    Double bomWeight = 0D;
                    for (FtcBomDetailMirror d : bomDetails) {
                        bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                    }
                    // 米长*门幅*单位面积克重*卷数
                    orderWeight = MathUtils.add(bomWeight * ppd.getRequirementCount() * ppd.getProductWidth() * ppd.getProductLength(), 0D, 2);
                    orderWeight = MathUtils.div(orderWeight, 1000000, 2);
                    ppd.setMirrorFromTcId(productm.getId());
                    ppd.setMirrorProcBomId(bomDetails.get(0).getFtcBomVersionId());
                }
                ppd.setProducePlanId(plan.getId());
                ppd.setPlanCode(plan.getProducePlanCode());
                ppd.setPlanTotalWeight(orderWeight);
                update(ppd);

                order = orderService.findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
                order.setAllocateCount(MathUtils.add((order.getAllocateCount() == null ? 0 : order.getAllocateCount()) + orderWeight, 0D, 2));
                update(order);

                param.clear();
                param.put("planDetailId", ppd.getId());
                // 删除计划的部件明细
                delete(ProducePlanDetailPartCount.class, param);

                for (ProducePlanDetailPartCount c : ppd.getList()) {
                    map.clear();
                    map.put("versionPartsId", c.getPartId());
                    map.put("tcProcBomVersoinId", ppd.getMirrorProcBomId());
                    map.put("salesOrderDetailId", c.getSalesOrderDetailId());
                    List<TcBomVersionPartsMirror> tcBomVersionPartsMirrorList = findListByMap(TcBomVersionPartsMirror.class, map);
                    c.setPlanDetailId(ppd.getId());
                    c.setSalesOrderId(order.getId());
                    if (null != tcBomVersionPartsMirrorList.get(0)) {
                        c.setMirrorPartId(tcBomVersionPartsMirrorList.get(0).getId());
                    }
                    // 保存订单明细的部件个数
                    save(c);
                }
            } else {
                // 套材订单
                if (ppd.getProductIsTc() == ProductIsTc.TC) {
                    for (ProducePlanDetailPartCount c : ppd.getList()) {
                        tvp = findById(TcBomVersionParts.class, c.getPartId());
                        orderWeight += tvp.getTcProcBomVersionPartsWeight() * c.getPlanPartCount();
                    }
                } else if (ppd.getProductIsTc() == ProductIsTc.FTC) {// 非套材订单
                    product = findById(FinishedProduct.class, ppd.getProductId());
                    if (product.getProductRollWeight() != null) {
                        orderWeight = MathUtils.add(ppd.getRequirementCount() * product.getProductRollWeight(), 0D, 2);
                    } else {
                        List<FtcBomDetail> bomDetails;
                        map.clear();
                        map.put("ftcBomVersionId", ppd.getProcBomId());
                        bomDetails = findListByMap(FtcBomDetail.class, map);
                        Double bomWeight = 0D;
                        for (FtcBomDetail d : bomDetails) {
                            bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                        }
                        // 米长*门幅*单位面积克重*卷数
                        orderWeight = MathUtils.add(bomWeight * ppd.getRequirementCount() * ppd.getProductWidth() * ppd.getProductLength(), 0D, 2);
                        orderWeight = MathUtils.div(orderWeight, 1000000, 2);
                    }
                } else {// 胚布订单
                    List<FtcBomDetail> bomDetails;
                    map.clear();
                    map.put("ftcBomVersionId", ppd.getProcBomId());
                    bomDetails = findListByMap(FtcBomDetail.class, map);
                    Double bomWeight = 0D;
                    for (FtcBomDetail d : bomDetails) {
                        bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                    }
                    // 米长*门幅*单位面积克重*卷数
                    orderWeight = MathUtils.add(bomWeight * ppd.getRequirementCount() * ppd.getProductWidth() * ppd.getProductLength(), 0D, 2);
                    orderWeight = MathUtils.div(orderWeight, 1000000, 2);
                }
                ppd.setProducePlanId(plan.getId());
                ppd.setPlanCode(plan.getProducePlanCode());
                ppd.setPlanTotalWeight(orderWeight);

                order = orderService.findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
                order.setAllocateCount(MathUtils.add((order.getAllocateCount() == null ? 0 : order.getAllocateCount()) + orderWeight, 0D, 2));
                update(order);

                param.clear();
                param.put("planDetailId", ppd.getId());
                // 删除计划的部件明细
                delete(ProducePlanDetailPartCount.class, param);

                for (ProducePlanDetailPartCount c : ppd.getList()) {
                    c.setPlanDetailId(ppd.getId());
                    c.setSalesOrderId(order.getId());
                    // 保存订单明细的部件个数
                    save(c);
                }
            }
        }
        oldIds.removeAll(newIds);

        for (Long id : oldIds) {
            param.clear();
            param.put("producePlanDetailId", id);
            // 删除无效的翻包领料任务
            delete(TurnBagPlanDetails.class, param);

            List<PackTask> tasks = ptService.findProduceTask(id);
            param.clear();
            if (id == null) throw new Exception("计划ID不能为空");
            // 删除包装任务
            param.put("ppdId", id);
            // 先删除相应的任务
            this.delete(PackTask.class, param);
            // 更新包装任务的剩余数量
            ptService.updateLeftCount(tasks);
        }
    }

    /**
     * 根据计划里面的产品，创建编织和裁剪计划 1. 如果产品是套材，那么拆分为非套材，计算出所需的各个非套材的对应的总数，乘以数量
     *
     * @throws Exception
     */
    @Override
    public void createCutAndWeavePlans(ProducePlan plan) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("producePlanId", plan.getId());
        // 查询出计划明细
        List<ProducePlanDetail> list = producePlanDao.findListByMap(ProducePlanDetail.class, param);
        FinishedProductMirror fpm;
        FinishedProduct fp;
        List<ProducePlanDetail> tcList = new ArrayList();
        List<ProducePlanDetail> ftcList = new ArrayList();
        List<FtcBomDetailMirror> bomDetailList;
        List<FtcBomDetail> bomDetails;
        List<Map<String, Object>> listD = departmentService.queryAllDepartmentByType("weave");
        List<String> listc = new ArrayList<>();
        for (int i = 0; i < listD.size(); i++) {
            listc.add((String) listD.get(i).get("CODE"));
        }
        // 遍历计划明细
        for (ProducePlanDetail ppd : list) {
            if (null != ppd.getMirrorProcBomId()) {
                if (!(ppd.getClosed() == null || ppd.getClosed() == 0)) {
                    continue;
                }
                // 套材，拆分成非套材，生成编织计划和裁剪计划
                if (ppd.getProductIsTc() == ProductIsTc.TC && (!listc.contains(plan.getWorkShopCode()))) {// 如果是套材，到裁剪车间的，那么是非单向布的部件
                    HashMap<String, Object> part = new HashMap();
                    part.put("tcProcBomVersoinId", ppd.getMirrorProcBomId());
                    List<TcBomVersionPartsMirror> partList = bomService.findListByMap(TcBomVersionPartsMirror.class, part);
                    List<TcBomVersionPartsDetailMirror> tcBomList = null;
                    for (TcBomVersionPartsMirror p : partList) {
                        part.clear();
                        part.put("tcProcBomPartsId", p.getId());
                        if (tcBomList == null) {
                            tcBomList = bomService.findListByMap(TcBomVersionPartsDetailMirror.class, part);
                        } else {
                            tcBomList.addAll(bomService.findListByMap(TcBomVersionPartsDetailMirror.class, part));
                        }
                    }
                    if (tcBomList == null) {
                        return;
                    }
                } else if (ppd.getProductIsTc() == ProductIsTc.TC && (listc.contains(plan.getWorkShopCode()))) {// 如果是套材，且到编织车间的，是单向布
                    // 获取套材下的部件id
                    HashMap<String, Object> map = new HashMap();
                    map.put("planDetailId", ppd.getId());
                    // 生产计划需求数量
                    List<ProducePlanDetailPartCount> ppdpcList = bomService.findListByMap(ProducePlanDetailPartCount.class, map);
                    // 获取所有的部件信息
                    for (ProducePlanDetailPartCount ppdpc : ppdpcList) {
                        if (ppdpc.getPlanPartCount() == 0) {
                            continue;
                        }
                        if (!ppdpc.getPartType().equals("成品胚布")) {
                            continue;
                        }
                        Long partId = ppdpc.getMirrorPartId();
                        String partName = ppdpc.getPartName();
                        // 部件数量,套数*BOM部件个数
                        int totalCount = ppdpc.getPlanPartCount() * ppdpc.getPartBomCount();
                        // 生成胚布产品
                        map.clear();
                        map.put("tcProcBomPartsId", partId);
                        // 套材部件明细
                        List<TcBomVersionPartsDetailMirror> tcvpdList = bomService.findListByMap(TcBomVersionPartsDetailMirror.class, map);
                        for (TcBomVersionPartsDetailMirror tcvpd : tcvpdList) {
//                            TcBomVersionPartsDetail tcBomVersionPartsDetail = findById(TcBomVersionPartsDetail.class, tcvpd.getVersionPartsDetailMirrorId());
                            TcBomVersionPartsMirror tcBomVersionPartsMirror = findById(TcBomVersionPartsMirror.class, tcvpd.getTcProcBomPartsId());
                            //过滤掉非下单的部件
                            if (tcvpd.getTcProcBomPartsId().longValue() != ppdpc.getMirrorPartId().longValue()) {
                                continue;
                            }
                            FinishedProductMirror tcfp = bomService.findById(FinishedProductMirror.class, tcvpd.getTcFinishedProductId());
                            FinishedProduct finishedProduct = findById(FinishedProduct.class, tcfp.getProductId());
                            // 生成虚拟的生产计划明细
                            ProducePlanDetail abstractPPD = new ProducePlanDetail();

                            abstractPPD.setRollNo(tcvpd.getRollNo());
                            abstractPPD.setDrawNo(tcvpd.getDrawingNo());
                            abstractPPD.setLevelNo(tcvpd.getLevelNo());
                            abstractPPD.setSorting(tcvpd.getSorting());
                            abstractPPD.setPartId(tcBomVersionPartsMirror.getVersionPartsId());
                            abstractPPD.setPartName(partName);
                            abstractPPD.setId(ppd.getId());
                            abstractPPD.setProductId(tcfp.getProductId());
                            abstractPPD.setProducePlanId(plan.getId());
                            abstractPPD.setPlanCode(plan.getProducePlanCode());
                            abstractPPD.setConsumerId(ppd.getConsumerId());
                            abstractPPD.setConsumerName(ppd.getConsumerName());
                            abstractPPD.setSalesOrderCode(ppd.getSalesOrderCode());
                            abstractPPD.setSalesOrderSubcodePrint(ppd.getSalesOrderSubcodePrint());
                            abstractPPD.setBatchCode(ppd.getBatchCode().trim());
                            abstractPPD.setFactoryProductName(ppd.getFactoryProductName());
                            abstractPPD.setConsumerProductName(tcfp.getConsumerProductName());
                            abstractPPD.setProductModel(tcfp.getProductModel());
                            abstractPPD.setProductWidth(tcfp.getProductWidth());
                            abstractPPD.setProductWeight(tcfp.getProductRollWeight());
                            abstractPPD.setProductLength(tcfp.getProductRollLength());
                            abstractPPD.setMirrorProcBomId(tcfp.getProcBomId());

                            map.clear();
                            map.put("salesOrderDetailId", ppd.getFromSalesOrderDetailId());
                            map.put("mirrorPartId", partId);
                            SalesOrderDetailPartsCount sodp = bomService.findUniqueByMap(SalesOrderDetailPartsCount.class, map);
                            // 计划数量
                            double orderCount = sodp.getPartCount() * ppdpc.getPartBomCount() * tcvpd.getTcProcBomFabricCount();
                            if (tcfp.getProductRollWeight() != null) {
                                orderCount = orderCount * tcfp.getProductRollWeight();
                            }
                            abstractPPD.setOrderCount(orderCount);
                            abstractPPD.setProcessBomCode(tcfp.getProductProcessCode());
                            abstractPPD.setProcessBomVersion(tcfp.getProductProcessBomVersion());
                            abstractPPD.setTotalRollCount((int) (totalCount * tcvpd.getTcProcBomFabricCount()));
                            abstractPPD.setTotalTrayCount(ppdpc.getPlanPartCount());
                            // 总卷数
                            abstractPPD.setRequirementCount((double) (totalCount * tcvpd.getTcProcBomFabricCount().intValue()));
                            abstractPPD.setProducedCount(0.0);
                            abstractPPD.setProducedRolls(0);
                            abstractPPD.setPackagedCount(0);
                            abstractPPD.setDeleveryDate(ppd.getDeleveryDate());
                            abstractPPD.setDeviceCode(ppd.getDeviceCode());
                            abstractPPD.setComment(ppd.getComment());
                            abstractPPD.setIsFinished(0);
                            abstractPPD.setFromTcId(ppd.getProductId());
                            abstractPPD.setFromTcName(ppd.getFromTcName());
                            abstractPPD.setFromSalesOrderDetailId(ppd.getFromSalesOrderDetailId());
                            abstractPPD.setProductIsTc(-1);
                            abstractPPD.setPackReq(ppd.getPackReq());
                            abstractPPD.setProcReq(ppd.getProcReq());
                            abstractPPD.setProcBomId(finishedProduct.getProcBomId());
                            abstractPPD.setClosed(0);
                            Double planWeight = 0D;
                            map.clear();
                            map.put("ftcBomVersionId", tcfp.getProcBomId());
                            bomDetailList = findListByMap(FtcBomDetailMirror.class, map);
                            Double bomWeight = 0D;
                            for (FtcBomDetailMirror d : bomDetailList) {
                                bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                            }
                            map.clear();
                            map.put("id", tcvpd.getTcFinishedProductId());
                            map.put("salesOrderId", tcvpd.getSalesOrderId());
                            fpm = orderService.findUniqueByMap(FinishedProductMirror.class, map);
                            // 米长*门幅*单位面积克重*卷数
                            if (fpm.getProductRollWeight() == null) {
                                planWeight = MathUtils.add(bomWeight * ppd.getRequirementCount() * fpm.getProductWidth() * fpm.getProductRollLength(), 0D, 2);
                                planWeight = MathUtils.div(planWeight, 1000000, 2);
                            } else {
                                planWeight = MathUtils.add(ppd.getRequirementCount() * fpm.getProductRollWeight(), 0D, 2);
                            }

                            abstractPPD.setPlanTotalWeight(planWeight);
                            tcList.add(abstractPPD);
                        }
                    }
                } else if (ppd.getProductIsTc() != 1) {
                    SalesOrderDetail salesOrderDetail = findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
                    ppd.setMirrorProcBomId(salesOrderDetail.getMirrorProcBomVersionId());
                    ftcList.add(ppd);
                }
            } else {//兼容
                if (!(ppd.getClosed() == null || ppd.getClosed() == 0)) {
                    continue;
                }
                // 套材，拆分成非套材，生成编织计划和裁剪计划
                if (ppd.getProductIsTc() == 1 && (!listc.contains(plan.getWorkShopCode()))) {// 如果是套材，到裁剪车间的，那么是非单向布的部件
                    HashMap<String, Object> part = new HashMap();
                    part.put("tcProcBomVersoinId", ppd.getProcBomId());
                    List<TcBomVersionParts> partList = bomService.findListByMap(TcBomVersionParts.class, part);
                    List<TcBomVersionPartsDetail> tcBomList = null;
                    for (TcBomVersionParts p : partList) {
                        part.clear();
                        part.put("tcProcBomPartsId", p.getId());
                        if (tcBomList == null) {
                            tcBomList = bomService.findListByMap(TcBomVersionPartsDetail.class, part);
                        } else {
                            tcBomList.addAll(bomService.findListByMap(TcBomVersionPartsDetail.class, part));
                        }
                    }
                    if (tcBomList == null) {
                        return;
                    }
                    //00107裁剪一车间 00108裁剪二车间 00109裁剪三车间
                } else if (ppd.getProductIsTc() == 1 && (listc.contains(plan.getWorkShopCode()))) {// 如果是套材，且到编织车间的，是单向布
                    // 获取套材下的部件id
                    HashMap<String, Object> map = new HashMap();
                    map.put("planDetailId", ppd.getId());
                    // 生产计划需求数量
                    List<ProducePlanDetailPartCount> ppdpcList = bomService.findListByMap(ProducePlanDetailPartCount.class, map);
                    // 获取所有的部件信息
                    for (ProducePlanDetailPartCount ppdpc : ppdpcList) {
                        if (ppdpc.getPlanPartCount() == 0) {
                            continue;
                        }
                        if (!ppdpc.getPartType().equals("成品胚布")) {
                            continue;
                        }
                        // 部件ID
                        Long partId = ppdpc.getPartId();
                        String partName = ppdpc.getPartName();
                        // 部件数量,套数*BOM部件个数
                        int totalCount = ppdpc.getPlanPartCount() * ppdpc.getPartBomCount();
                        // 生成胚布产品
                        map.clear();
                        map.put("tcProcBomPartsId", partId);
                        // 套材部件明细
                        List<TcBomVersionPartsDetail> tcvpdList = bomService.findListByMap(TcBomVersionPartsDetail.class, map);
                        for (TcBomVersionPartsDetail tcvpd : tcvpdList) {
                            //过滤掉非下单的部件
                            if (tcvpd.getTcProcBomPartsId().longValue() != ppdpc.getPartId().longValue()) {
                                continue;
                            }
                            FinishedProduct tcfp = bomService.findById(FinishedProduct.class, tcvpd.getTcFinishedProductId());
                            // 生成虚拟的生产计划明细
                            ProducePlanDetail abstractPPD = new ProducePlanDetail();

                            abstractPPD.setRollNo(tcvpd.getRollNo());
                            abstractPPD.setDrawNo(tcvpd.getDrawingNo());
                            abstractPPD.setLevelNo(tcvpd.getLevelNo());
                            abstractPPD.setSorting(tcvpd.getSorting());
                            abstractPPD.setPartId(tcvpd.getTcProcBomPartsId());
                            abstractPPD.setPartName(partName);

                            abstractPPD.setId(ppd.getId());
                            abstractPPD.setProductId(tcfp.getId());
                            abstractPPD.setProducePlanId(plan.getId());
                            abstractPPD.setPlanCode(plan.getProducePlanCode());
                            abstractPPD.setConsumerId(ppd.getConsumerId());
                            abstractPPD.setConsumerName(ppd.getConsumerName());
                            abstractPPD.setSalesOrderCode(ppd.getSalesOrderCode());
                            abstractPPD.setSalesOrderSubcodePrint(ppd.getSalesOrderSubcodePrint());
                            abstractPPD.setBatchCode(ppd.getBatchCode().trim());
                            abstractPPD.setFactoryProductName(ppd.getFactoryProductName());
                            abstractPPD.setConsumerProductName(tcfp.getConsumerProductName());
                            abstractPPD.setProductModel(tcfp.getProductModel());
                            abstractPPD.setProductWidth(tcfp.getProductWidth());
                            abstractPPD.setProductWeight(tcfp.getProductRollWeight());
                            abstractPPD.setProductLength(tcfp.getProductRollLength());

                            map.clear();
                            map.put("salesOrderDetailId", ppd.getFromSalesOrderDetailId());
                            map.put("partId", partId);
                            SalesOrderDetailPartsCount sodp = bomService.findUniqueByMap(SalesOrderDetailPartsCount.class, map);
                            // 计划数量
                            double orderCount = sodp.getPartCount() * ppdpc.getPartBomCount() * tcvpd.getTcProcBomFabricCount();
                            if (tcfp.getProductRollWeight() != null) {
                                orderCount = orderCount * tcfp.getProductRollWeight();
                            }
                            abstractPPD.setOrderCount(orderCount);
                            abstractPPD.setProcessBomCode(tcfp.getProductProcessCode());
                            abstractPPD.setProcessBomVersion(tcfp.getProductProcessBomVersion());
                            abstractPPD.setTotalRollCount((int) (totalCount * tcvpd.getTcProcBomFabricCount()));
                            abstractPPD.setTotalTrayCount(ppdpc.getPlanPartCount());
                            // 总卷数
                            abstractPPD.setRequirementCount((double) (totalCount * tcvpd.getTcProcBomFabricCount().intValue()));
                            abstractPPD.setProducedCount(0.0);
                            abstractPPD.setProducedRolls(0);
                            abstractPPD.setPackagedCount(0);
                            abstractPPD.setDeleveryDate(ppd.getDeleveryDate());
                            abstractPPD.setDeviceCode(ppd.getDeviceCode());
                            abstractPPD.setComment(ppd.getComment());
                            abstractPPD.setIsFinished(0);
                            abstractPPD.setFromTcId(ppd.getProductId());
                            abstractPPD.setFromTcName(ppd.getFromTcName());
                            abstractPPD.setFromSalesOrderDetailId(ppd.getFromSalesOrderDetailId());
                            abstractPPD.setProductIsTc(-1);
                            abstractPPD.setPackReq(ppd.getPackReq());
                            abstractPPD.setProcReq(ppd.getProcReq());
                            abstractPPD.setProcBomId(tcfp.getProcBomId());
                            abstractPPD.setClosed(0);

                            Double planWeight = 0D;
                            map.clear();
                            map.put("ftcBomVersionId", abstractPPD.getProcBomId());
                            bomDetails = findListByMap(FtcBomDetail.class, map);
                            Double bomWeight = 0D;
                            for (FtcBomDetail d : bomDetails) {
                                bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                            }
                            fp = orderService.findById(FinishedProduct.class, abstractPPD.getProductId());
                            // 米长*门幅*单位面积克重*卷数
                            if (fp.getProductRollWeight() == null) {
                                planWeight = MathUtils.add(bomWeight * ppd.getRequirementCount() * fp.getProductWidth() * fp.getProductRollLength(), 0D, 2);
                                planWeight = MathUtils.div(planWeight, 1000000, 2);
                            } else {
                                planWeight = MathUtils.add(ppd.getRequirementCount() * fp.getProductRollWeight(), 0D, 2);
                            }
                            abstractPPD.setPlanTotalWeight(planWeight);
                            tcList.add(abstractPPD);
                        }
                    }
                } else if (ppd.getProductIsTc() != 1) {
                    ftcList.add(ppd);
                }
            }
        }

        if (!ListUtils.isEmpty(tcList)) {
            ProducePlan tcMockProducePlan = new ProducePlan();
            // 复制对象
            ObjectUtils.clone(plan, tcMockProducePlan);
            tcMockProducePlan.setList(tcList);
            weaveService.saveWeavePlan(tcMockProducePlan);
        }

        if (!ListUtils.isEmpty(ftcList)) {
            plan.setList(ftcList);
            weaveService.saveWeavePlan(plan);
        }

        if (!listc.contains(plan.getWorkShopCode())) {
            plan.setList(list);
            cutService.add(plan);
        }
        mrpService.createRequirementPlans(plan);
    }

    /**
     * 将成品转化为计划明细
     *
     * @param fp
     * @return
     */
    private ProducePlanDetail translateFinishedProductToProducePlanDetail(ProducePlan plan, ProducePlanDetail ppd, FinishedProduct fp, Map<Long, Double> count) {
        ProducePlanDetail ret = new ProducePlanDetail();
        ret.setBatchCode(ppd.getBatchCode().trim());
        ret.setBcBomCode(fp.getProductPackagingCode());
        ret.setBcBomVersion(fp.getProductPackageVersion());
        ret.setComment("");
        ret.setConsumerId(ppd.getConsumerId());
        ret.setConsumerName(ppd.getConsumerName());
        ret.setDeleveryDate(ppd.getDeleveryDate());
        ret.setDeviceCode(ppd.getDeviceCode());
        ret.setFromSalesOrderDetailId(ppd.getFromSalesOrderDetailId());
        ret.setFromTcId(ppd.getFromTcId());
        ret.setFromTcName(ppd.getFromTcName());
        ret.setIsFinished(ppd.getIsFinished());
        ret.setOrderCount(count.get(fp.getId()));
        ret.setPackagedCount(0);
        ret.setPlanCode(plan.getProducePlanCode());
        ret.setProcessBomCode(fp.getProductProcessCode());
        ret.setProcessBomVersion(fp.getProductProcessBomVersion());
        ret.setProducedCount(0D);
        ret.setProducePlanId(ppd.getProducePlanId());
        ret.setId(ppd.getId());
        ret.setProductId(fp.getId());
        ret.setProductIsTc(0);
        ret.setProductLength(fp.getProductRollLength());
        ret.setProductModel(fp.getProductModel());
        ret.setFactoryProductName(fp.getFactoryProductName());
        ret.setProductType(null);
        ret.setProductWeight(fp.getProductRollWeight());
        ret.setProductWidth(fp.getProductWidth());
        ret.setRequirementCount(ppd.getRequirementCount());
        ret.setSalesOrderCode(ppd.getSalesOrderCode());
        ret.setSort(0L);
        ret.setTotalRollCount(0);
        ret.setTotalTrayCount(0);
        ret.setPackBomId(fp.getPackBomId());
        ret.setProcBomId(fp.getProcBomId());
        return ret;
    }

    public String getSerial(String workShopCode) throws Exception {
        return producePlanDao.getSerial(workShopCode);
    }

    @Override
    public Map<String, Object> dataList(Filter filter, Page page) throws Exception {
        return producePlanDao.dataList(filter, page);
    }

    public void deletePlan(String ids) {
        ProducePlan pp;
        Map<String, Object> param = new HashMap();
        List<ProducePlanDetail> details;
        SalesOrderDetail order;

        for (String id : ids.split(",")) {
            pp = findById(ProducePlan.class, Long.parseLong(id));
            // 删除计划
            delete(pp);

            param.clear();
            param.put("producePlanId", pp.getId());
            // 查询明细
            details = findListByMap(ProducePlanDetail.class, param);
            for (final ProducePlanDetail ppd : details) {
                // 订单明细
                order = findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
                // 回复订单明细分配的数量
                order.setAllocateCount(MathUtils.add((order.getAllocateCount() == null ? 0 : order.getAllocateCount()) - ppd.getPlanTotalWeight(), 0D, 2));
                update(order);

                param.clear();
                param.put("producePlanDetailId", ppd.getId());
                delete(TurnBagPlanDetails.class, param);
                // 删除计划明细
                delete(ppd);

                param.clear();
                List<PackTask> list = ptService.findProduceTask(ppd.getId());
                // 删除包装任务
                param.put("ppdId", ppd.getId());
                // 先删除相应的任务
                this.delete(PackTask.class, param);
                // 更新包装任务的剩余数量
                ptService.updateLeftCount(list);
            }
        }
    }

    @Override
    public void setIsSettled(Long weaveDailyId, Long cutDailyId) {
        producePlanDao.setIsSettled(weaveDailyId, cutDailyId);
    }

    public void updateProductInfo(Long producePlanId) {
        HashMap<String, Object> map = new HashMap();
        map.put("producePlanId", producePlanId);
        List<ProducePlanDetail> producePlanDetailList = findListByMap(ProducePlanDetail.class, map);
        for (ProducePlanDetail ppd : producePlanDetailList) {
            String newProcReq = ppd.getProcReq();
            String newPackReq = ppd.getPackReq();
            if ((newProcReq == null && newPackReq == null) || ("".equals(newProcReq) && "".equals(newPackReq))) {
                continue;
            }
            Long productId = ppd.getProductId();
            FinishedProduct fp = findById(FinishedProduct.class, productId);
            String oldProcReq = fp.getProcReq();
            String oldPackReq = fp.getPackReq();
            boolean isSave = false;
            if (newProcReq != null && (!"".equals(newProcReq)) && (!newProcReq.equals(oldProcReq))) {
                fp.setProcReq(newProcReq);
                isSave = true;
            }
            if (newPackReq != null && (!"".equals(newPackReq)) && (!newPackReq.equals(oldPackReq))) {
                fp.setPackReq(newPackReq);
                isSave = true;
            }

            if (isSave) {
                update(fp);
            }
        }
    }

    @Override
    public List<Map<String, Object>> checkBatchCode(String batchCode, Long producePlanId) {
        return producePlanDao.checkBatchCode(batchCode, producePlanId);
    }

    public List<Map<String, Object>> details(Long planId) {
        List<Map<String, Object>> list = producePlanDao.details(planId);
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).get("YX") != null) {
                    String yx = list.get(i).get("YX").toString();
                    Integer index = yx.indexOf("叶型为：");
                    if (index != -1) {
                        list.get(i).put("CONSUMERSIMPLENAME", list.get(i).get("CONSUMERSIMPLENAME").toString() + "(" + yx.substring(index + 4) + ")");
                    }
                }
            }
        }
        Map<String, Long> bomPartCount;
        Map<String, Object> producedPartCount;
        int suitCount = Integer.MAX_VALUE;
        BigDecimal b1;
        BigDecimal b2;
        Long bomCount;
        Long producedCount;
        Iterator<Entry<String, Long>> it;
        Entry<String, Long> ent;
        for (Map<String, Object> detail : list) {
            if (MapUtils.getAsInt(detail, "PRODUCTISTC") != 1) {
                continue;
            }
            bomPartCount = getPartCount(MapUtils.getAsLong(detail, "ID"));
            producedPartCount = processService.getProducedPartCount(MapUtils.getAsLong(detail, "ID"));
            it = bomPartCount.entrySet().iterator();
            while (it.hasNext()) {
                ent = it.next();
                bomCount = ent.getValue();
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

    @Override
    public List<Map<String, Object>> detailsMirror(Long planId) {
        List<Map<String, Object>> list = producePlanDao.detailsMirror(planId);
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).get("YX") != null) {
                    String yx = list.get(i).get("YX").toString();
                    Integer index = yx.indexOf("叶型为：");
                    if (index != -1) {
                        list.get(i).put("CONSUMERSIMPLENAME", list.get(i).get("CONSUMERSIMPLENAME").toString() + "(" + yx.substring(index + 4) + ")");
                    }
                }
            }
        }
        Map<String, Long> bomPartCount;
        Map<String, Object> producedPartCount;
        int suitCount = Integer.MAX_VALUE;
        BigDecimal b1;
        BigDecimal b2;
        Long bomCount;
        Long producedCount;

        Iterator<Entry<String, Long>> it;
        Entry<String, Long> ent;

        for (Map<String, Object> detail : list) {
            if (MapUtils.getAsInt(detail, "PRODUCTISTC") != 1) {
                continue;
            }
            bomPartCount = getPartCount(MapUtils.getAsLong(detail, "ID"));
            producedPartCount = processService.getProducedPartCount(MapUtils.getAsLong(detail, "ID"));
            it = bomPartCount.entrySet().iterator();
            while (it.hasNext()) {
                ent = it.next();
                bomCount = ent.getValue();
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
        Map<String, Object> param = new HashMap();
        List<ProducePlanDetailPartCount> parts;
        param.clear();
        param.put("planDetailId", planDetailId);
        parts = findListByMap(ProducePlanDetailPartCount.class, param);
        Map<String, Long> ret = new HashMap();
        for (ProducePlanDetailPartCount part : parts) {
            ret.put(part.getPartName(), part.getPartBomCount().longValue());
        }
        return ret;
    }

    public Map<String, Long> getOrderPartCount(Long salesOrderDetailId) {
        Map<String, Object> param = new HashMap();
        List<SalesOrderDetailPartsCount> parts;
        param.clear();
        param.put("salesOrderDetailId", salesOrderDetailId);
        parts = findListByMap(SalesOrderDetailPartsCount.class, param);
        Map<String, Long> ret = new HashMap();
        for (SalesOrderDetailPartsCount part : parts) {
            ret.put(part.getPartName(), part.getPartBomCount().longValue());
        }
        return ret;
    }

    public Map<String, Object> findOrderPageInfo2(Filter filter, Page page) throws Exception {
        Map<String, Object> ret = producePlanDao.findOrderPageInfo2(filter, page);
        List<Map<String, Object>> list = (List<Map<String, Object>>) ret.get("rows");
        Map<String, Long> bomPartCount;
        List<Map<String, Object>> orderPartCounts;
        Map<String, Long> orderPartCount;
        Map<String, Object> producedRollsAndTrays;
        int suitCount = Integer.MAX_VALUE;
        BigDecimal b1;
        BigDecimal b2;
        Long bomCount;
        Long producedCount;
        Iterator<Entry<String, Long>> it;
        Entry<String, Long> ent;

        for (Map<String, Object> sod : list) {
            producedRollsAndTrays = orderService.countRollsAndTrays(MapUtils.getAsLong(sod, "ID"));
            sod.put("RC", producedRollsAndTrays.get("RC"));
            sod.put("TC", producedRollsAndTrays.get("TC"));
            if (MapUtils.getAsInt(sod, "PRODUCTISTC") != 1) {
                continue;
            }
            bomPartCount = getOrderPartCount(MapUtils.getAsLong(sod, "ID"));// 获取订单中的部件明细
            orderPartCounts = orderService.getOrderPartCount(MapUtils.getAsLong(sod, "ID"));
            orderPartCount = new HashMap();
            for (Map<String, Object> m : orderPartCounts) {
                orderPartCount.put(MapUtils.getAsString(m, "PARTNAME"), MapUtils.getAsLong(m, "COUNT"));
            }
            it = bomPartCount.entrySet().iterator();
            while (it.hasNext()) {
                ent = it.next();
                bomCount = ent.getValue();
                producedCount = orderPartCount.get(ent.getKey());
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
            sod.put("TS", suitCount);
        }
        return ret;
    }

    @Override
    public List<Map<String, Object>> searchbox(String searchbox) {
        return producePlanDao.searchbox(searchbox);
    }

    /**
     * 强制变更
     */
    public void forceEdit(ProducePlan plan, Long userId) throws Exception {
        Map<String, Object> param = new HashMap();
        param.put("producePlanId", plan.getId());
        // 前端传过来的计划明细
        List<ProducePlanDetail> ppdList = plan.getList();
        // 数据库保存的计划明细
        List<ProducePlanDetail> dbList = producePlanDao.findListByMap(ProducePlanDetail.class, param);
        SalesOrderDetail order;
        // 还原数量
        for (ProducePlanDetail ppd : dbList) {
            order = findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
            order.setAllocateCount(MathUtils.add(order.getAllocateCount() - ppd.getRequirementCount(), 0D, 2));
            update(order);
            update(ppd);
        }

        for (int i = 0; i < dbList.size(); i++) {
            ProducePlanDetail p = dbList.get(i);
            p.setProcReq(ppdList.get(i).getProcReq());
            p.setPackReq(ppdList.get(i).getPackReq());
            p.setComment(ppdList.get(i).getComment());
            update(p);
        }

        ProducePlanDetail detail;
        // 重新计算数量
        for (ProducePlanDetail ppd : ppdList) {
            detail = findById(ProducePlanDetail.class, ppd.getId());
            detail.setRequirementCount(ppd.getRequirementCount());
            detail.setTotalRollCount(ppd.getTotalRollCount());
            detail.setTotalTrayCount(ppd.getTotalTrayCount());
            detail.setDeleveryDate(ppd.getDeleveryDate());

            order = orderService.findById(SalesOrderDetail.class, ppd.getFromSalesOrderDetailId());
            order.setAllocateCount((order.getAllocateCount() == null ? 0D : order.getAllocateCount()) + ppd.getRequirementCount());
            update(order);
            update(detail);

            param.clear();
            param.put("planDetailId", ppd.getId());
            // 删除计划的部件明细
            delete(ProducePlanDetailPartCount.class, param);
            for (ProducePlanDetailPartCount c : ppd.getList()) {
                c.setPlanDetailId(ppd.getId());
                c.setSalesOrderId(order.getId());
                // 保存订单明细的部件个数
                save(c);
            }
        }
        String auditCode = AuditConstant.CODE.SC;
        auditService.submitAudit("生产计划变更，计划编号:" + plan.getProducePlanCode(), auditCode, userId, "planner/produce/audit?id=" + plan.getId(), plan.getId(), ProducePlan.class);
        plan = findById(ProducePlan.class, plan.getId());
        plan.setAuditState(1);
        update2(plan);
    }

    @Override
    public Map<String, Object> findfinished(Filter filter, Page page) {
        return producePlanDao.findfinished(filter, page);
    }

    public void noClose(String ids) {
        String[] _ids = ids.split(",");
        for (String id : _ids) {
            ProducePlanDetail ppd = findById(ProducePlanDetail.class, Long.parseLong(id));
            ppd.setClosed(0);
            update(ppd);
        }
    }

    @Override
    public Map<String, Object> listOrders(Filter filter, Page page) throws Exception {
        return producePlanDao.listOrders(filter, page);
    }

    @Override
    public Map<String, Object> findSchedule(Filter filter, Page page) throws Exception {
        return producePlanDao.findSchedule(filter, page);
    }

    @Override
    public Map<String, Object> findScheduleWeight(Filter filter, Page page) throws Exception {
        return producePlanDao.findScheduleWeight(filter, page);
    }

    @Override
    public String getSdeviceCode(Long id) {
        return producePlanDao.getSdeviceCode(id);
    }

    public String hasPackTask(Long id) {
        return producePlanDao.hasPackTask(id);
    }

    public Map<String, Object> findTBPageInfo(Filter filter, Page page) throws Exception {
        return producePlanDao.findTBPageInfo(filter, page);
    }

    public Map<String, Object> findTBPageInfo2(Filter filter, Page page) throws Exception {
        return producePlanDao.findTBPageInfo2(filter, page);
    }


    @Override
    public Double getFbslByDdh(String ddh) throws Exception {
        return producePlanDao.getFbslByDdh(ddh);
    }

    @Override
    public Double getFbslByDdhs(Map<String, Object> ddhs) throws Exception {
        return producePlanDao.getFbslByDdhs(ddhs);
    }

    @Override
    public List<Map<String, Object>> findProductListByMap(FinishedProduct finishProduct) {
        List<Map<String, Object>> list = producePlanDao.findProductListByMap(finishProduct.getId());
        return list;
    }
}



