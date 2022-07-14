package com.bluebirdme.mes.common.service.impl;

import com.bluebirdme.mes.baseInfo.entity.FtcBomDetail;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsDetail;
import com.bluebirdme.mes.baseInfo.service.IBomService;
import com.bluebirdme.mes.common.dao.IFixDao;
import com.bluebirdme.mes.common.service.IFixService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetailPartCount;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.entity.SalesOrderDetailPartsCount;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import com.bluebirdme.mes.store.entity.TrayBarCode;
import com.bluebirdme.mes.store.service.IBarCodeService;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.ListUtils;
import org.xdemo.superutil.j2se.MathUtils;
import org.xdemo.superutil.j2se.ObjectUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 类注释
 *
 * @author Goofy
 * @Date 2017年7月13日 上午9:31:41
 */
@Service
@AnyExceptionRollback
public class FixServiceImpl extends BaseServiceImpl implements IFixService {
    @Resource
    IFixDao fixDao;

    @Resource
    IBarCodeService barcodeService;

    @Resource
    IBomService bomService;

    @Resource
    ISalesOrderService orderService;

    @Resource
    IWeavePlanService weaveService;

    @Override
    protected IBaseDao getBaseDao() {
        return fixDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return null;
    }

    public void fixRollNO(String[] batchCodes) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<WeavePlan> wList = new ArrayList<WeavePlan>();
        List<ProducePlanDetailPartCount> list = new ArrayList<ProducePlanDetailPartCount>();
        List<TcBomVersionPartsDetail> detailList = new ArrayList<TcBomVersionPartsDetail>();
        Set<String> ids = new HashSet<String>();
        for (String batch : batchCodes) {
            wList.clear();
            list.clear();
            detailList.clear();
            map.clear();
            map.put("batchCode", batch);
            wList.addAll(findListByMap(WeavePlan.class, map));
            map.clear();
            map.put("planDetailId", wList.get(0).getProducePlanDetailId());
            list.addAll(findListByMap(ProducePlanDetailPartCount.class, map));
            for (ProducePlanDetailPartCount pc : list) {
                map.clear();
                map.put("tcProcBomPartsId", pc.getPartId());
                detailList.addAll(findListByMap(TcBomVersionPartsDetail.class, map));
            }
            for (int i = 0; i < wList.size(); i++) {
                wList.get(i).setDrawNo(detailList.get(i).getDrawingNo());
                wList.get(i).setRollNo(detailList.get(i).getRollNo());
                wList.get(i).setLevelNo(detailList.get(i).getLevelNo());
                wList.get(i).setSorting(detailList.get(i).getSorting());
                wList.get(i).setPartId(detailList.get(i).getTcProcBomPartsId());
                update(wList.get(i));
            }
        }
    }

    public void fixRollCountInTray() {
        List<TrayBarCode> list = findAll(TrayBarCode.class);
        for (TrayBarCode tbc : list) {
            tbc.setRollCountPerTray(barcodeService.countRollsInTray(tbc.getBarcode()));
            update(tbc);
        }
    }

    public void produceToWeave(Long producePlanDetailId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        ProducePlanDetail _ppd = findById(ProducePlanDetail.class, producePlanDetailId);
        ProducePlan plan = findById(ProducePlan.class, _ppd.getProducePlanId());
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("producePlanId", plan.getId());
        // 查询出计划明细
        List<ProducePlanDetail> list = findListByMap(ProducePlanDetail.class, param);
        FinishedProduct fp;
        List<ProducePlanDetail> tcList = new ArrayList<ProducePlanDetail>();
        List<ProducePlanDetail> ftcList = new ArrayList<ProducePlanDetail>();
        List<FtcBomDetail> bomDetails;
        // 遍历计划明细
        for (ProducePlanDetail ppd : list) {
            if (ppd.getId().longValue() != producePlanDetailId.longValue()) continue;
            System.err.println("产品属性:" + (ppd.getProductIsTc() == 1) + "\t批次号：" + ppd.getBatchCode() + "\t车间：" + plan.getWorkshop().contains("编织"));
            System.out.println("关闭状态" + ppd.getClosed());
            if (!(ppd.getClosed() == null || ppd.getClosed() == 0)) continue;
            // 套材，拆分成非套材，生成编织计划和裁剪计划
            if (ppd.getProductIsTc() == 1 && plan.getWorkshop().contains("裁剪")) {// 如果是套材，到裁剪车间的，那么是非单向布的部件
                System.err.println("套裁,批次号：" + ppd.getBatchCode());
                HashMap<String, Object> part = new HashMap<String, Object>();
                part.put("tcProcBomVersoinId", ppd.getProcBomId());
                List<TcBomVersionParts> partList = bomService.findListByMap(TcBomVersionParts.class, part);
                List<TcBomVersionPartsDetail> tcBomList = null;
                for (TcBomVersionParts p : partList) {
                    if (tcBomList == null) {
                        part.clear();
                        part.put("tcProcBomPartsId", p.getId());
                        tcBomList = bomService.findListByMap(TcBomVersionPartsDetail.class, part);
                    } else {
                        part.clear();
                        part.put("tcProcBomPartsId", p.getId());
                        tcBomList.addAll(bomService.findListByMap(TcBomVersionPartsDetail.class, part));
                    }
                }
                if (tcBomList == null) {
                    return;
                }
            } else if (ppd.getProductIsTc() == 1 && plan.getWorkshop().contains("编织")) {// 如果是套材，且到编织车间的，是单向布
                System.err.println("单向布,批次号：" + ppd.getBatchCode());
                System.out.println("套裁BOM个数");
                // 获取套材下的部件id
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("planDetailId", ppd.getId());
                //生产计划需求数量
                List<ProducePlanDetailPartCount> ppdpcList = bomService.findListByMap(ProducePlanDetailPartCount.class, map);
                System.err.println("部件计划数量" + ppdpcList.size());
                // 获取所有的部件信息
                for (ProducePlanDetailPartCount ppdpc : ppdpcList) {
                    if (ppdpc.getPlanPartCount() == 0) {
                        continue;
                    }
                    if (!ppdpc.getPartType().equals("成品胚布")) continue;
                    // 部件ID
                    Long partId = ppdpc.getPartId();
                    String partName = ppdpc.getPartName();
                    // 部件数量,套数*BOM部件个数
                    int totalCount = ppdpc.getPlanPartCount() * ppdpc.getPartBomCount();
                    //生成胚布产品
                    map.clear();
                    map.put("tcProcBomPartsId", partId);
                    //套材部件明细
                    List<TcBomVersionPartsDetail> tcvpdList = bomService.findListByMap(TcBomVersionPartsDetail.class, map);
                    System.err.println("部件明细" + tcvpdList.size());
                    for (TcBomVersionPartsDetail tcvpd : tcvpdList) {
                        FinishedProduct tcfp = bomService.findById(FinishedProduct.class, tcvpd.getTcFinishedProductId());
                        //生成虚拟的生产计划明细
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
                        abstractPPD.setBatchCode(ppd.getBatchCode());
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
                        //计划数量
                        double orderCount = sodp.getPartCount() * ppdpc.getPartBomCount() * tcvpd.getTcProcBomFabricCount();
                        if (tcfp.getProductRollWeight() != null) {
                            orderCount = orderCount * tcfp.getProductRollWeight();
                        }
                        abstractPPD.setOrderCount(orderCount);
                        abstractPPD.setProcessBomCode(tcfp.getProductProcessCode());
                        abstractPPD.setProcessBomVersion(tcfp.getProductProcessBomVersion());
                        abstractPPD.setTotalRollCount((int) (totalCount * tcvpd.getTcProcBomFabricCount()));
                        abstractPPD.setTotalTrayCount(ppdpc.getPlanPartCount());
                        //总卷数
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
                        Double planWeight;
                        map.clear();
                        map.put("ftcBomVersionId", abstractPPD.getProcBomId());
                        bomDetails = findListByMap(FtcBomDetail.class, map);
                        Double bomWeight = 0D;
                        for (FtcBomDetail d : bomDetails) {
                            bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                        }
                        fp = orderService.findById(FinishedProduct.class, abstractPPD.getProductId());
                        //米长*门幅*单位面积克重*卷数
                        if (fp.getProductRollWeight() == null) {
                            planWeight = MathUtils.add(bomWeight * ppd.getRequirementCount() * fp.getProductWidth() * fp.getProductRollLength(), 0D, 2);
                            planWeight = MathUtils.div(planWeight, 1000000, 2);
                        } else {
                            planWeight = MathUtils.add(ppd.getRequirementCount() * fp.getProductRollWeight(), 0D, 2);
                        }
                        abstractPPD.setPlanTotalWeight(planWeight);
                        System.out.println(GsonTools.toJson(abstractPPD));
                        tcList.add(abstractPPD);
                    }
                }
            } else if (ppd.getProductIsTc() != 1) {
                ftcList.add(ppd);
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
    }
}
