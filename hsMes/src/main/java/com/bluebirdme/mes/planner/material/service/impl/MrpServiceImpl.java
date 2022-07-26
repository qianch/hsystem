package com.bluebirdme.mes.planner.material.service.impl;

import com.bluebirdme.mes.baseInfo.entity.*;
import com.bluebirdme.mes.baseInfo.entityMirror.FtcBomDetailMirror;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionPartsDetailMirror;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionPartsMirror;
import com.bluebirdme.mes.baseInfo.service.IBomService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.planner.material.dao.IMrpDao;
import com.bluebirdme.mes.planner.material.entity.BcMaterialRequirementPlan;
import com.bluebirdme.mes.planner.material.entity.FtcMaterialRequirementPlan;
import com.bluebirdme.mes.planner.material.service.IMrpService;
import com.bluebirdme.mes.planner.pack.entity.PackTask;
import com.bluebirdme.mes.planner.pack.service.IPackTaskService;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetailPartCount;
import com.bluebirdme.mes.planner.produce.service.IProducePlanService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.produce.service.IFinishedProductService;
import com.bluebirdme.mes.utils.ProductIsTc;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MathUtils;
import org.xdemo.superutil.j2se.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Goofy
 * @Date 2016年10月20日 上午10:20:43
 */
@Service
@AnyExceptionRollback
public class MrpServiceImpl extends BaseServiceImpl implements IMrpService {
    @Resource
    IProducePlanService ppService;

    @Resource
    IBomService bomService;

    @Resource
    IFinishedProductService productService;

    @Resource
    IMrpDao mrpDao;

    @Resource
    IPackTaskService ptService;

    @Override
    protected IBaseDao getBaseDao() {
        return mrpDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return null;
    }

    public Map<String, Object> findRequirementPlans(Long ids[]) {
        return mrpDao.findRequirementPlans(ids);
    }

    @Override
    public void createRequirementPlans(ProducePlan pp) throws Exception {
        // 默认制成率0.93
        final double pullRate = 0.93;
        Map<String, Object> param = new HashMap<>();
        param.put("producePlanId", pp.getId());
        // 查询计划明细
        List<ProducePlanDetail> details = ppService.findListByMap(ProducePlanDetail.class, param);
        List<BcMaterialRequirementPlan> bcMrps = new ArrayList<>();
        List<FtcMaterialRequirementPlan> ftcMrps = new ArrayList<>();
        // 依次生成物料需求和包材需求
        for (ProducePlanDetail _ppd : details) {
            if (null != _ppd.getMirrorProcBomId()) {
                if (_ppd.getIsTurnBagPlan() != null && _ppd.getIsTurnBagPlan().equals("翻包"))
                    continue;
                ProducePlanDetail ppd = new ProducePlanDetail();
                ObjectUtils.clone(_ppd, ppd);
                // 套材的物料需求
                if (ppd.getProductIsTc() == ProductIsTc.TC) {
                    // 查询套材物料需求计划
                    HashMap<String, Object> part = new HashMap<>();
                    part.put("tcProcBomVersoinId", ppd.getMirrorProcBomId());
                    part.put("salesOrderDetailId", ppd.getFromSalesOrderDetailId());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("planDetailId", _ppd.getId());
                    // 获取计划下达的数量详情
                    List<ProducePlanDetailPartCount> producePartCount = bomService.findListByMap(ProducePlanDetailPartCount.class, map);
                    // 获取套材的所有版本
                    List<TcBomVersionPartsMirror> partList = bomService.findListByMap(TcBomVersionPartsMirror.class, part);
                    // 实际下单的的情况
                    List<TcBomVersionPartsMirror> _partList = new ArrayList<>();
                    Map<Long, Integer> partCount = new HashMap<>();
                    // 根据下单情况，删除不需要的BOM部件信息
                    for (ProducePlanDetailPartCount ppp : producePartCount) {
                        partCount.put(ppp.getMirrorPartId(), ppp.getPlanPartCount());
                        for (TcBomVersionPartsMirror p1 : partList) {
                            if (p1.getId().longValue() == ppp.getMirrorPartId() && ppp.getPlanPartCount() != 0) {
                                _partList.add(p1);
                            }
                        }
                    }
                    // 套材BOM部件的明细
                    List<TcBomVersionPartsDetailMirror> tcDetails = null;
                    for (TcBomVersionPartsMirror p : _partList) {
                        part.clear();
                        part.put("tcProcBomPartsId", p.getId());
                        if (tcDetails == null) {
                            tcDetails = bomService.findListByMap(TcBomVersionPartsDetailMirror.class, part);
                        } else {
                            tcDetails.addAll(bomService.findListByMap(TcBomVersionPartsDetailMirror.class, part));
                        }
                    }

                    if (tcDetails == null) {
                        pp.setCreateFeedback("工艺代码：" + ppd.getProcessBomCode() + "下无胚布信息。");
                        update(pp);
                        return;
                    }
                    FinishedProductMirror product;
                    // 遍历套材部件的明细，获取所有的胚布，通过胚布的BOM信息，获取原料信息
                    for (TcBomVersionPartsDetailMirror d : tcDetails) {
                        product = productService.findById(FinishedProductMirror.class, d.getTcFinishedProductId());
                        if (product == null) {
                            return;
                        }
                        param.clear();
                        param.put("ftcBomVersionId", product.getProcBomId());
                        // 获取胚布非套材明细
                        List<FtcBomDetailMirror> ftcDetails = bomService.findListByMap(FtcBomDetailMirror.class, param);
                        FtcMaterialRequirementPlan ftcMrp;
                        for (FtcBomDetailMirror ftcBom : ftcDetails) {
                            param.clear();
                            param.put("materialModel", ftcBom.getFtcBomDetailModel());
                            ftcMrp = new FtcMaterialRequirementPlan();
                            ftcMrp.setMaterialName(ftcBom.getFtcBomDetailName());
                            ftcMrp.setMaterialModel(ftcBom.getFtcBomDetailModel());
                            Double totalWeight;
                            Double bomTotalWeight = mrpDao.sumMaterialTotalWeightPerSquarMirror(product.getProcBomId());
                            if (bomTotalWeight == null || bomTotalWeight == 0D) {
                                pp.setCreateFeedback("该产品单位面积总重为0，无法生产物料需求计划，使用工艺:" + ppd.getProcessBomCode());
                                update(pp);
                                return;
                            }
                            if (product.getProductRollWeight() != null) {
                                // 单位面积克重(g/m²)/总单位面积质量*卷重*每套卷数*计划部件套数
                                totalWeight = (ftcBom.getFtcBomDetailWeightPerSquareMetre() / bomTotalWeight) * product.getProductRollWeight() * d.getTcProcBomFabricCount() * partCount.get(d.getTcProcBomPartsId()) / pullRate;
                            } else {
                                // 门幅mm*卷长m*单位面积克重g/m²*每套卷数*计划部件套数
                                totalWeight = product.getProductWidth() * product.getProductRollLength() * ftcBom.getFtcBomDetailWeightPerSquareMetre() * d.getTcProcBomFabricCount() * partCount.get(d.getTcProcBomPartsId()) / 1000000 / pullRate;
                            }
                            ftcMrp.setMaterialTotalWeight(MathUtils.add(totalWeight, 0, 2));
                            ftcMrp.setProducePlanId(ppd.getProducePlanId());
                            ftcMrps.add(ftcMrp);
                        }
                    }
                } else {
                    // 查询非套材物料需求计划
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("ftcBomVersionId", ppd.getMirrorProcBomId());
                    List<FtcBomDetailMirror> ftcDetails = bomService.findListByMap(FtcBomDetailMirror.class, map);
                    Double bomTotalWeight = mrpDao.sumMaterialTotalWeightPerSquarMirror(ppd.getMirrorProcBomId());
                    double mrpWeight;
                    if (bomTotalWeight == null || bomTotalWeight == 0D) {
                        pp.setCreateFeedback("该产品单位面积总重为0，无法生产物料需求计划，使用工艺:" + ppd.getProcessBomCode());
                        update(pp);
                        return;
                    }
                    FtcMaterialRequirementPlan ftcMrp;
                    for (FtcBomDetailMirror ftcBom : ftcDetails) {
                        ftcMrp = new FtcMaterialRequirementPlan();
                        ftcMrp.setMaterialName(ftcBom.getFtcBomDetailName());
                        ftcMrp.setMaterialModel(ftcBom.getFtcBomDetailModel());
                        // 纱原材料计算公式=单位克重/总单位克重*需求重量/制成率
                        if (ppd.getProductLength() != null && ppd.getProductWidth() != null) {
                            // 门幅mm*卷长m*单位面积克重g/m²*卷数
                            mrpWeight = ftcBom.getFtcBomDetailWeightPerSquareMetre() * ppd.getProductLength() * ppd.getProductWidth() * ppd.getRequirementCount() / 1000 / 1000 / pullRate;
                        } else if (ppd.getProductWeight() != null) {
                            // 单位面积克重g/m²/总单位面积质量*卷重*卷数
                            if (ftcBom.getFtcBomDetailWeightPerSquareMetre() == null) {
                                pp.setCreateFeedback("工艺BOM中无胚布信息");
                            }
                            mrpWeight = ((ftcBom.getFtcBomDetailWeightPerSquareMetre() == null ? 0 : ftcBom.getFtcBomDetailWeightPerSquareMetre()) / bomTotalWeight) * ppd.getProductWeight() * ppd.getRequirementCount() / pullRate;
                        } else {
                            mrpWeight = 0D;
                        }
                        ftcMrp.setMaterialTotalWeight(MathUtils.add(mrpWeight, 0, 2));
                        ftcMrp.setProducePlanId(ppd.getProducePlanId());
                        ftcMrps.add(ftcMrp);
                    }
                    // 保存非套材物料需求计划
                    super.save(ftcMrps.toArray(new FtcMaterialRequirementPlan[]{}));
                }
                HashMap<String, Object> map = new HashMap<>();
                // 非套材包材需求
                if (_ppd.getProductIsTc() == 2) {
                    List<PackTask> tasks = ptService.findProduceTask(_ppd.getId());
                    List<FtcBcBomVersionDetail> list;
                    for (PackTask t : tasks) {
                        map.put("packVersionId", t.getVid());
                        list = ptService.findListByMap(FtcBcBomVersionDetail.class, map);
                        BcMaterialRequirementPlan mrp;
                        for (FtcBcBomVersionDetail d : list) {
                            mrp = new BcMaterialRequirementPlan();
                            mrp.setPackMaterialAttr("");
                            mrp.setPackMaterialModel(d.getPackMaterialModel());
                            mrp.setPackMaterialUnit(d.getPackUnit());
                            mrp.setProducePlanId(pp.getId());
                            mrp.setPackMateriaTotalCount(MathUtils.mul(t.getProduceTotalCount(), d.getPackMaterialCount(), 1));
                            mrp.setPackMaterialName(d.getPackMaterialName());
                            mrp.setPackUnit(d.getPackUnit());
                            mrp.setMtCode(d.getPackMaterialCode());
                            mrp.setStCode(d.getPackStandardCode());
                            bcMrps.add(mrp);
                        }
                    }
                } else {
                    map.put("packVersionId", ppd.getPackBomId());
                    List<BcBomVersionDetail> bcDetails = bomService.findListByMap(BcBomVersionDetail.class, map);
                    BcMaterialRequirementPlan bcMrp;
                    for (BcBomVersionDetail d : bcDetails) {
                        bcMrp = new BcMaterialRequirementPlan();
                        bcMrp.setPackMaterialAttr(d.getPackMaterialAttr());
                        bcMrp.setPackMaterialModel(d.getPackMaterialModel());
                        bcMrp.setPackMaterialName(d.getPackMaterialName());
                        bcMrp.setPackUnit(d.getPackUnit());
                        bcMrp.setPackMaterialUnit(d.getPackMaterialUnit());
                        double need = 0d;
                        if (d.getPackMaterialCount() != null) {
                            need = d.getPackMaterialCount();
                        }
                        bcMrp.setPackMateriaTotalCount(ppd.getRequirementCount() * need);
                        bcMrp.setProducePlanId(ppd.getProducePlanId());
                        bcMrps.add(bcMrp);
                    }
                }
            } else {
                if (_ppd.getIsTurnBagPlan() != null && _ppd.getIsTurnBagPlan().equals("翻包"))
                    continue;
                ProducePlanDetail ppd = new ProducePlanDetail();
                ObjectUtils.clone(_ppd, ppd);
                // 套材的物料需求
                if (ppd.getProductIsTc() == 1) {
                    // 查询套材物料需求计划
                    HashMap<String, Object> part = new HashMap<>();
                    part.put("tcProcBomVersoinId", ppd.getProcBomId());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("planDetailId", _ppd.getId());
                    // 获取计划下达的数量详情
                    List<ProducePlanDetailPartCount> producePartCount = bomService.findListByMap(ProducePlanDetailPartCount.class, map);
                    // 获取套材的所有版本
                    List<TcBomVersionParts> partList = bomService.findListByMap(TcBomVersionParts.class, part);
                    // 实际下单的的情况
                    List<TcBomVersionParts> _partList = new ArrayList<>();
                    Map<Long, Integer> partCount = new HashMap<>();
                    // 根据下单情况，删除不需要的BOM部件信息
                    for (ProducePlanDetailPartCount ppp : producePartCount) {
                        partCount.put(ppp.getPartId(), ppp.getPlanPartCount());
                        for (TcBomVersionParts p1 : partList) {
                            if (p1.getId().longValue() == ppp.getPartId() && ppp.getPlanPartCount() != 0) {
                                _partList.add(p1);
                            }
                        }
                    }
                    // 套材BOM部件的明细
                    List<TcBomVersionPartsDetail> tcDetails = null;
                    for (TcBomVersionParts p : _partList) {
                        part.clear();
                        part.put("tcProcBomPartsId", p.getId());
                        if (tcDetails == null) {
                            tcDetails = bomService.findListByMap(TcBomVersionPartsDetail.class, part);
                        } else {
                            tcDetails.addAll(bomService.findListByMap(TcBomVersionPartsDetail.class, part));
                        }
                    }

                    if (tcDetails == null) {
                        pp.setCreateFeedback("工艺代码：" + ppd.getProcessBomCode() + "下无胚布信息。");
                        update(pp);
                        return;
                    }
                    FinishedProduct product;
                    // 遍历套材部件的明细，获取所有的胚布，通过胚布的BOM信息，获取原料信息
                    for (TcBomVersionPartsDetail d : tcDetails) {
                        product = productService.findById(FinishedProduct.class, d.getTcFinishedProductId());
                        if (product == null)
                            return;
                        param.clear();
                        param.put("ftcBomVersionId", product.getProcBomId());
                        // 获取胚布非套材明细
                        List<FtcBomDetail> ftcDetails = bomService.findListByMap(FtcBomDetail.class, param);
                        FtcMaterialRequirementPlan ftcMrp;
                        for (FtcBomDetail ftcBom : ftcDetails) {
                            param.clear();
                            param.put("materialModel", ftcBom.getFtcBomDetailModel());
                            ftcMrp = new FtcMaterialRequirementPlan();
                            ftcMrp.setMaterialName(ftcBom.getFtcBomDetailName());
                            ftcMrp.setMaterialModel(ftcBom.getFtcBomDetailModel());
                            double totalWeight;
                            Double bomTotalWeight = mrpDao.sumMaterialTotalWeightPerSquar(product.getProcBomId());
                            if (bomTotalWeight == null || bomTotalWeight == 0D) {
                                pp.setCreateFeedback("该产品单位面积总重为0，无法生产物料需求计划，使用工艺:" + ppd.getProcessBomCode());
                                update(pp);
                                return;
                            }

                            if (product.getProductRollWeight() != null) {
                                // 单位面积克重(g/m²)/总单位面积质量*卷重*每套卷数*计划部件套数
                                totalWeight = (ftcBom.getFtcBomDetailWeightPerSquareMetre() / bomTotalWeight) * product.getProductRollWeight() * d.getTcProcBomFabricCount() * partCount.get(d.getTcProcBomPartsId()) / pullRate;
                            } else {
                                // 门幅mm*卷长m*单位面积克重g/m²*每套卷数*计划部件套数
                                totalWeight = product.getProductWidth() * product.getProductRollLength() * ftcBom.getFtcBomDetailWeightPerSquareMetre() * d.getTcProcBomFabricCount() * partCount.get(d.getTcProcBomPartsId()) / 1000000 / pullRate;
                            }
                            ftcMrp.setMaterialTotalWeight(MathUtils.add(totalWeight, 0, 2));
                            ftcMrp.setProducePlanId(ppd.getProducePlanId());
                            ftcMrps.add(ftcMrp);
                        }
                    }
                } else {
                    // 查询非套材物料需求计划
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("ftcBomVersionId", ppd.getProcBomId());
                    List<FtcBomDetail> ftcDetails = bomService.findListByMap(FtcBomDetail.class, map);
                    Double bomTotalWeight = mrpDao.sumMaterialTotalWeightPerSquar(ppd.getProcBomId());
                    double mrpWeight;
                    if (bomTotalWeight == null || bomTotalWeight == 0D) {
                        pp.setCreateFeedback("该产品单位面积总重为0，无法生产物料需求计划，使用工艺:" + ppd.getProcessBomCode());
                        update(pp);
                        return;
                    }
                    FtcMaterialRequirementPlan ftcMrp;
                    for (FtcBomDetail ftcBom : ftcDetails) {
                        ftcMrp = new FtcMaterialRequirementPlan();
                        ftcMrp.setMaterialName(ftcBom.getFtcBomDetailName());
                        ftcMrp.setMaterialModel(ftcBom.getFtcBomDetailModel());
                        // 纱原材料计算公式=单位克重/总单位克重*需求重量/制成率
                        if (ppd.getProductLength() != null && ppd.getProductWidth() != null) {
                            // 门幅mm*卷长m*单位面积克重g/m²*卷数
                            mrpWeight = ftcBom.getFtcBomDetailWeightPerSquareMetre() * ppd.getProductLength() * ppd.getProductWidth() * ppd.getRequirementCount() / 1000 / 1000 / pullRate;
                        } else if (ppd.getProductWeight() != null) {
                            // 单位面积克重g/m²/总单位面积质量*卷重*卷数
                            if (ftcBom.getFtcBomDetailWeightPerSquareMetre() == null) {
                                pp.setCreateFeedback("工艺BOM中无胚布信息");
                            }
                            mrpWeight = ((ftcBom.getFtcBomDetailWeightPerSquareMetre() == null ? 0 : ftcBom.getFtcBomDetailWeightPerSquareMetre()) / bomTotalWeight) * ppd.getProductWeight() * ppd.getRequirementCount() / pullRate;
                        } else {
                            mrpWeight = 0D;
                        }
                        ftcMrp.setMaterialTotalWeight(MathUtils.add(mrpWeight, 0, 2));
                        ftcMrp.setProducePlanId(ppd.getProducePlanId());
                        ftcMrps.add(ftcMrp);
                    }
                    // 保存非套材物料需求计划
                    super.save(ftcMrps.toArray(new FtcMaterialRequirementPlan[]{}));
                }
                HashMap<String, Object> map = new HashMap<>();
                // 非套材包材需求
                if (_ppd.getProductIsTc() == 2) {
                    List<PackTask> tasks = ptService.findProduceTask(_ppd.getId());
                    List<FtcBcBomVersionDetail> list;
                    for (PackTask t : tasks) {
                        map.put("packVersionId", t.getVid());
                        list = ptService.findListByMap(FtcBcBomVersionDetail.class, map);
                        BcMaterialRequirementPlan mrp;
                        for (FtcBcBomVersionDetail d : list) {
                            mrp = new BcMaterialRequirementPlan();
                            mrp.setPackMaterialAttr("");
                            mrp.setPackMaterialModel(d.getPackMaterialModel());
                            mrp.setPackMaterialUnit(d.getPackUnit());
                            mrp.setProducePlanId(pp.getId());
                            mrp.setPackMateriaTotalCount(MathUtils.mul(t.getProduceTotalCount(), d.getPackMaterialCount(), 1));
                            mrp.setPackMaterialName(d.getPackMaterialName());
                            mrp.setPackUnit(d.getPackUnit());
                            mrp.setMtCode(d.getPackMaterialCode());
                            mrp.setStCode(d.getPackStandardCode());
                            bcMrps.add(mrp);
                        }
                    }
                } else {
                    map.put("packVersionId", ppd.getPackBomId());
                    List<BcBomVersionDetail> bcDetails = bomService.findListByMap(BcBomVersionDetail.class, map);
                    BcMaterialRequirementPlan bcMrp;
                    for (BcBomVersionDetail d : bcDetails) {
                        bcMrp = new BcMaterialRequirementPlan();
                        bcMrp.setPackMaterialAttr(d.getPackMaterialAttr());
                        bcMrp.setPackMaterialModel(d.getPackMaterialModel());
                        bcMrp.setPackMaterialName(d.getPackMaterialName());
                        bcMrp.setPackUnit(d.getPackUnit());
                        bcMrp.setPackMaterialUnit(d.getPackMaterialUnit());
                        double need = 0d;
                        if (d.getPackMaterialCount() != null) {
                            need = d.getPackMaterialCount();
                        }
                        bcMrp.setPackMateriaTotalCount(ppd.getRequirementCount() * need);
                        bcMrp.setProducePlanId(ppd.getProducePlanId());
                        bcMrps.add(bcMrp);
                    }
                }
            }
        }
        save(bcMrps.toArray(new BcMaterialRequirementPlan[]{}));
        // 保存非套材物料需求计划
        save(ftcMrps.toArray(new FtcMaterialRequirementPlan[]{}));
        // 标记为已生成计划
        pp.setHasCreatedCutAndWeavePlan(1);
        pp.setCreateFeedback("");
        ppService.update(pp);
    }

    @Override
    public void deleteMrpByProduce(ProducePlan producePlan) throws SQLTemplateException {
        mrpDao.deleteMrpByProduce(producePlan);
    }

    public Map<String, Object> findFarbic(Long producePlanId) {
        return mrpDao.findFarbic(producePlanId);
    }
}
