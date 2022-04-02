package com.bluebirdme.mes.mobile.produce.controller;

import com.bluebirdme.mes.baseInfo.entity.FtcBomDetail;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsDetail;
import com.bluebirdme.mes.baseInfo.entityMirror.FtcBomDetailMirror;
import com.bluebirdme.mes.baseInfo.service.IBomService;
import com.bluebirdme.mes.baseInfo.service.IQualityGradeService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.device.entity.Device;
import com.bluebirdme.mes.device.service.IDeviceService;
import com.bluebirdme.mes.mobile.base.MobileBaseController;
import com.bluebirdme.mes.mobile.produce.entity.FeedingRecord;
import com.bluebirdme.mes.mobile.produce.service.IFeedingRecordService;
import com.bluebirdme.mes.mobile.produce.service.IMobileProduceService;
import com.bluebirdme.mes.mobile.produce.service.IWeighService;
import com.bluebirdme.mes.mobile.stock.controller.MobileMaterialController;
import com.bluebirdme.mes.planner.cut.entity.*;
import com.bluebirdme.mes.planner.cut.service.ICutPlanService;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlanDevices;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.service.IFinishedProductService;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.stock.entity.MaterialStockState;
import com.bluebirdme.mes.stock.entity.ProductStockState;
import com.bluebirdme.mes.store.entity.PartBarcode;
import com.bluebirdme.mes.store.entity.Roll;
import com.bluebirdme.mes.store.entity.RollBarcode;
import com.bluebirdme.mes.store.service.IRollService;
import com.bluebirdme.mes.utils.ProductState;
import com.google.common.base.CharMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/mobile/produce")
public class MobileProduceController extends MobileBaseController {
    @Resource
    IDeviceService deviceService;
    @Resource
    IWeavePlanService weavePlanService;
    @Resource
    IFeedingRecordService feedingRecordService;
    @Resource
    IMobileProduceService moblieProduceService;
    @Resource
    IFinishedProductService finishProductService;
    @Resource
    IRollService rollService;
    @Resource
    IQualityGradeService qualityGradeService;
    @Resource
    IBomService bomService;
    @Resource
    ICutPlanService cutPlanService;
    @Resource
    IWeighService wService;

    @NoLogin
    @Journal(name = "选择机台需要完成的计划更新版", logType = LogType.DB)
    @RequestMapping(value = "deviceOrder1", method = RequestMethod.POST)
    public String deviceOrder1(String userId, String deviceCode) {
        HashMap<String, Object> map = new HashMap();
        map.put("deviceCode", deviceCode);
        Device device = deviceService.findUniqueByMap(Device.class, map);//根据设备传入的设备条码去找出Device（设备信息）
        if (device == null) {//判断机台信息是否存在
            return ajaxError("无效的机台条码");
        } else {
            return GsonTools.toJson(map);
        }
    }

    @NoLogin
    @Journal(name = "选择机台需要完成的计划", logType = LogType.DB)
    @RequestMapping(value = "deviceOrder", method = RequestMethod.POST)
    public String deviceOrder(String userId, String deviceCode) {
        HashMap<String, Object> map = new HashMap();
        if (deviceCode != null) {
            map.put("deviceCode", deviceCode);
            Device device = deviceService.findUniqueByMap(Device.class, map);

            if (device == null) {
                return ajaxError("无效的机台条码");
            }

            map.clear();
            map.put("deviceId", device.getId());
            List<WeavePlanDevices> wdList = deviceService.findListByMap(WeavePlanDevices.class, map);
            HashSet<Long> weavePlanIdSet = new HashSet();
            List<HashMap<String, String>> result = new ArrayList();
            for (WeavePlanDevices wd : wdList) {
                weavePlanIdSet.add(wd.getWeavePlanId());
            }

            for (Long weavePlanId : weavePlanIdSet) {
                HashMap<String, String> resultMap = new HashMap();
                WeavePlan wp = deviceService.findById(WeavePlan.class, weavePlanId);
                if (wp.getIsFinished() == 1) {
                    continue;
                }
                if (wp.getClosed() != null && wp.getClosed() == 1) {
                    continue;
                }
                resultMap.put("ID", wp.getId() + "");
                resultMap.put("TEXT", "订单：" + wp.getSalesOrderCode() + "<br>批次：" + wp.getBatchCode() + "<br>产品：" + wp.getProductName());
                result.add(resultMap);
            }
            return GsonTools.toJson(result);
        } else {
            // 根据人员取裁剪计划
            map.put("isClosed", 0);
            List<CutDailyPlan> cdp = deviceService.findListByMap(CutDailyPlan.class, map);
            List<HashMap<String, String>> result = new ArrayList();
            if (cdp == null) {
                return GsonTools.toJson("fail");
            }
            int a = 1;
            for (int i = 0; i < cdp.size(); i++) {
                map.clear();
                map.put("cutPlanDailyPlanId", cdp.get(i).getId());
                map.put("userId", Long.parseLong(userId));
                List<CutDailyPlanUsers> cpuList = deviceService.findListByMap(CutDailyPlanUsers.class, map);
                HashSet<Long> keyset = new HashSet<Long>();

                if (cpuList == null) {
                    return GsonTools.toJson("fail");
                }
                for (CutDailyPlanUsers cpu : cpuList) {
                    HashMap<String, String> resultMap = new HashMap<String, String>();
                    CutDailyPlanDetail cpd = deviceService.findById(CutDailyPlanDetail.class, cpu.getCutPlanId());
                    CutPlan cp = deviceService.findById(CutPlan.class, cpd.getCutPlanId());
                    if (keyset.contains(cp.getId())) {
                        continue;
                    }
                    keyset.add(cp.getId());
                    if (cp.getIsFinished() == 1) {
                        continue;
                    }
                    resultMap.put("ID", cp.getId() + "");
                    resultMap.put("TEXT", a + ". 订单号：" + cp.getSalesOrderCode() + ";<br>" + "批次号：" + cp.getBatchCode() + ";<br>厂内名称:" + cp.getProductName());
                    a++;
                    result.add(resultMap);
                }
            }
            return GsonTools.toJson(result);
        }
    }

    @NoLogin
    @Journal(name = "保存投料记录", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(FeedingRecord feedingRecord, String puid) throws Exception {
        HashMap<String, Object> map = new HashMap();
        if (feedingRecord.getMaterialCode() != null) {
            String materialCode = MobileMaterialController.parseMaterialCode(feedingRecord.getMaterialCode());
            feedingRecord.setMaterialCode(materialCode);
            map.put("materialCode", materialCode);
            map.put("deviceCode",feedingRecord.getDeviceCode());
        } else {
            map.put("rollCode", feedingRecord.getRollCode());
            map.put("deviceCode",feedingRecord.getDeviceCode());
        }
        FeedingRecord feedingRecord1 = feedingRecordService.findUniqueByMap(FeedingRecord.class, map);
        if (feedingRecord1 != null) {
            return GsonTools.toJson("该条码已投料");
        }

        HashMap<String, Object> param = new HashMap();
        if(feedingRecord.getRollCode() != null){
            param.put("barCode", feedingRecord.getRollCode());
            List<ProductStockState> psss = feedingRecordService.findListByMap(ProductStockState.class, param);
            //裁剪投料后卷条码改成不在库
            psss.get(psss.size() - 1).setStockState(0);
        }
        feedingRecord.setFeedingDate(new Date());
        feedingRecordService.save(feedingRecord);
        if (feedingRecord.getMaterialCode() != null) {
            HashMap<String, Object> feed = new HashMap<String, Object>();
            feed.put("palletCode", feedingRecord.getMaterialCode());
            List<MaterialStockState> listByMap = feedingRecordService.findListByMap(MaterialStockState.class, feed);
            listByMap.get(listByMap.size() - 1).setIsFeed(1);
        }
        return GsonTools.toJson("success");
    }

    @NoLogin
    @Journal(name = "保存投料记录", logType = LogType.DB)
    @RequestMapping(value = "add2", method = RequestMethod.POST)
    public String add2(FeedingRecord feedingRecord, String puid, String oldweaveId) {
        if (!"".equals(oldweaveId) && oldweaveId != null && !"undefined".equals(oldweaveId)) {
            //原料编号是否在原批次存在
            List<Map<String, Object>> list = moblieProduceService.queryYlbh(feedingRecord.getMaterialCode(), oldweaveId, feedingRecord.getDeviceCode());
            if (list.size() > 0) {
                //下架
                moblieProduceService.deleteYlbh(feedingRecord.getMaterialCode(), oldweaveId, feedingRecord.getDeviceCode());
                return GsonTools.toJson("success2");
            } else {
                //正常新增
                HashMap<String, Object> map = new HashMap();
                HashMap<String, Object> param = new HashMap();
                if (feedingRecord.getMaterialCode() != null) {
                    String materialCode = MobileMaterialController.parseMaterialCode(feedingRecord.getMaterialCode());
                    feedingRecord.setMaterialCode(materialCode);
                    map.put("materialCode", materialCode);
                } else {
                    map.put("rollCode", feedingRecord.getRollCode());
                }
                param.put("barCode", feedingRecord.getRollCode());
                List<ProductStockState> psss = feedingRecordService.findListByMap(ProductStockState.class, param);
                //裁剪投料后卷条码改成不在库
                psss.get(psss.size() - 1).setStockState(0);
                feedingRecord.setFeedingDate(new Date());
                feedingRecordService.save(feedingRecord);
                return GsonTools.toJson("success");
            }
        } else {
            HashMap<String, Object> map = new HashMap();
            HashMap<String, Object> param = new HashMap();
            if (feedingRecord.getMaterialCode() != null) {
                String materialCode = MobileMaterialController.parseMaterialCode(feedingRecord.getMaterialCode());
                feedingRecord.setMaterialCode(materialCode);
                map.put("materialCode", materialCode);
            } else {
                map.put("rollCode", feedingRecord.getRollCode());
            }
            param.put("barCode", feedingRecord.getRollCode());
            List<ProductStockState> psss = feedingRecordService.findListByMap(ProductStockState.class, param);
            //裁剪投料后卷条码改成不在库
            psss.get(psss.size() - 1).setStockState(0);
            feedingRecord.setFeedingDate(new Date());
            feedingRecordService.save(feedingRecord);
            return GsonTools.toJson("success");
        }
    }

    @NoLogin
    @Journal(name = "查下该机台上一批的信息", logType = LogType.CONSOLE)
    @RequestMapping(value = "querylist")
    public String querylist(String deviceCode) {
        List<Map<String, Object>> list = moblieProduceService.querylist(deviceCode);
        return GsonTools.toJson(list);
    }

    @NoLogin
    @Journal(name = "查下该机台上一批的信息", logType = LogType.CONSOLE)
    @RequestMapping(value = "querylist2")
    public String querylist2(String deviceCode) {
        List<Map<String, Object>> list = moblieProduceService.querylist2(deviceCode);
        return GsonTools.toJson(list);
    }

    @NoLogin
    @Journal(name = "变更投料记录", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String edit(FeedingRecord feedingRecord, String puid, String tlids) throws Exception {
        moblieProduceService.editInfo(feedingRecord.getWeaveId() + "", feedingRecord.getOperateUserId(), tlids);
        return GsonTools.toJson("success");
    }

    @NoLogin
    @Journal(name = "保存产出记录", logType = LogType.DB)
    @RequestMapping(value = "addroll", method = RequestMethod.POST)
    public synchronized String addRoll(String puname, Roll roll) throws Exception {
        String deviceCode = CharMatcher.anyOf("\r\n\t \u00A0 ").trimFrom(roll.getRollDeviceCode());
        roll.setRollDeviceCode(deviceCode);
        //质量等级默认为"A"
        if (roll.getRollQualityGradeCode().equals("null")) {
            roll.setRollQualityGradeCode("A");
        }
        roll.setIsAbnormalRoll(roll.getRollQualityGradeCode().equals("A") ? 0 : 1);
        Long produceDetailId;
        HashMap<String, Object> m = new HashMap();
        Boolean iss = true;
        m.put("barcode", roll.getRollBarcode());
        RollBarcode rbcode = rollService.findUniqueByMap(RollBarcode.class, m);
        m.clear();
        m.put("barcode", roll.getPartBarcode());
        PartBarcode partCode = rollService.findUniqueByMap(PartBarcode.class, m);
        if (rbcode == null && partCode == null) {
            throw new Exception("无效条码");
        }
        if (roll.getRollBarcode() != null) {
            m.clear();
            m.put("weavePlanId", rbcode.getPlanId());
            List<WeavePlanDevices> findListByMap = rollService.findListByMap(WeavePlanDevices.class, m);
            if (findListByMap.size() == 0) {
                return ajaxError("该条码订单暂无机台，请排产后产出");
            }
            for (int i = 0; i < findListByMap.size(); i++) {
                Long id = findListByMap.get(i).getDeviceId();
                Device device = rollService.findById(Device.class, id);
                if (device.getDeviceCode().equals(roll.getRollDeviceCode())) {
                    iss = false;
                }
            }
            if (iss) {
                return ajaxError("任务未分配在该机台不能产出");
            }
        }

        m.clear();
        m.put("rollBarcode", roll.getRollBarcode());
        m.put("partBarcode", roll.getPartBarcode());

        if (rollService.isExist(Roll.class, m, false)) {
            return ajaxError("该条码已登记");
        }

        if (roll.getRollBarcode() != null && roll.getRollBarcode().startsWith("R")) {
            HashMap<String, Object> map1 = new HashMap();
            map1.put("barcode", roll.getRollBarcode());
            RollBarcode rollBarcode;
            rollBarcode = rollService.findUniqueByMap(RollBarcode.class, map1);

            if (rollBarcode == null) {
                return GsonTools.toJson("不存在的条码");
            }

            produceDetailId = rollBarcode.getPlanId();
            map1.clear();
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("rollBarcode", roll.getRollBarcode());
            boolean r = rollService.isExist(Roll.class, map);
            if (r) {
                return GsonTools.toJson("exist");
            }
            WeavePlan w = weavePlanService.findById(WeavePlan.class, produceDetailId);
            ProducePlanDetail producePlanDetail = weavePlanService.findById(ProducePlanDetail.class, w.getProducePlanDetailId());
            moblieProduceService.updateRoll(roll, w, producePlanDetail);
        } else {
            PartBarcode rollBarcode;
            HashMap<String, Object> map1 = new HashMap();
            String barocde;
            if (roll.getRollBarcode() != null && roll.getPartBarcode() == null) {
                roll.setPartBarcode(roll.getRollBarcode());
                roll.setRollBarcode(null);
            }
            if (roll.getPartBarcode() != null) {
                barocde = roll.getPartBarcode();
            } else {
                barocde = roll.getRollBarcode();
            }
            map1.put("barcode", barocde);
            rollBarcode = rollService.findUniqueByMap(PartBarcode.class, map1);
            if (rollBarcode == null) {
                return GsonTools.toJson("不存在的条码");
            }
            produceDetailId = rollBarcode.getPlanId();
            map1.clear();
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("rollBarcode", barocde);
            boolean r = rollService.isExist(Roll.class, map);
            if (r) {
                return GsonTools.toJson("exist");
            }
            Iplan w = weavePlanService.findById(CutPlan.class, produceDetailId);
            if (w == null) {
                w = weavePlanService.findById(WeavePlan.class, produceDetailId);
            }
            ProducePlanDetail producePlanDetail = weavePlanService.findById(ProducePlanDetail.class, w.getProducePlanDetailId());
            moblieProduceService.updateRoll(roll, w, producePlanDetail);
        }
        return GsonTools.toJson("success");
    }

    @NoLogin
    @Journal(name = "保存产出记录_需称重", logType = LogType.DB)
    @RequestMapping(value = "addroll2", method = RequestMethod.POST)
    public synchronized String addRoll2(Roll roll) throws Exception {
        if (!wService.validDevice(roll.getRollBarcode(), roll.getRollDeviceCode())) {
            return ajaxError("机台无效，未在该机台排产");
        }
        roll.setState(ProductState.VALID);
        roll.setRollQualityGradeCode(null);
        roll.setRollWeight(null);
        roll.setRollAutoWeight(null);
        roll.setRollOutputTime(new Date());
        roll.setRollWeighState(1);
        moblieProduceService.save(roll);
        return GsonTools.toJson("success");
    }

    @NoLogin
    @Journal(name = "获取产品质量等级选项")
    @RequestMapping(value = "getQualityGradeSelections", method = RequestMethod.POST)
    public String getQualityGradeSelections() {
        return GsonTools.toJson(qualityGradeService.getQualitySelections());
    }

    @NoLogin
    @Journal(name = "获取产品工艺bom", logType = LogType.DB)
    @RequestMapping(value = "getBomDetails", method = RequestMethod.POST)
    public String getBomDetails(String weaveId) {
        WeavePlan weavePlan = weavePlanService.findById(WeavePlan.class, Long.parseLong(weaveId));
        SalesOrderDetail salesOrderDetail = weavePlanService.findById(SalesOrderDetail.class, weavePlan.getFromSalesOrderDetailId());
        HashSet<String> nameSet = new HashSet<>();
        Long procBomId = weavePlan.getProcBomId();
        HashMap<String, Object> map = new HashMap();
        if (null != salesOrderDetail.getMirrorProcBomVersionId()) {
            map.put("ftcBomVersionId", salesOrderDetail.getMirrorProcBomVersionId());
            map.put("salesOrderId", salesOrderDetail.getSalesOrderId());
            List<FtcBomDetailMirror> resultList = bomService.findListByMap(FtcBomDetailMirror.class, map);
            for (FtcBomDetailMirror fd : resultList) {
                nameSet.add(fd.getFtcBomDetailModel());
            }
        } else {
            map.clear();
            map.put("ftcBomVersionId", procBomId);
            List<FtcBomDetail> resultList = bomService.findListByMap(FtcBomDetail.class, map);
            for (FtcBomDetail fd : resultList) {
                nameSet.add(fd.getFtcBomDetailModel());
            }
        }
        return GsonTools.toJson(nameSet.toArray(new String[]{}));
    }

    @NoLogin
    @Journal(name = "判断裁剪工艺bom", logType = LogType.DB)
    @RequestMapping(value = "getCutBom", method = RequestMethod.POST)
    public String getCutBom(String cutId) {
        // 根据裁剪计划ID获取裁剪计划
        CutPlan cutPlan = cutPlanService.findById(CutPlan.class, Long.parseLong(cutId));
        // 根据裁剪计划获取对应的生产计划详情
        ProducePlanDetail ppd = finishProductService.findById(ProducePlanDetail.class, cutPlan.getProducePlanDetailId());
        // 获取生产计划详情中需要生产的对应产品
        FinishedProduct product = finishProductService.findById(FinishedProduct.class, ppd.getProductId());
        // 获取需要生产的产品的套材工艺列表
        List<TcBomVersionPartsDetail> resultList = bomService.getBomDetails(TcBomVersionPartsDetail.class, product.getProductProcessCode(), product.getProductProcessBomVersion());
        HashMap<String, Object> map = new HashMap();
        map.put("productUserId", product.getProductConsumerId());
        // 遍历套材工艺列表，判断投入的原料是否存在于列表
        HashSet<FinishedProduct> set = new HashSet();
        for (TcBomVersionPartsDetail fd : resultList) {
            // 根据套材工艺列表取到的只有套材需要的胚布规格，而产品中需要客户id和产品规格作为联合主键才能确认是否为正确的产品
            set.add(finishProductService.findById(FinishedProduct.class, fd.getTcFinishedProductId()));
        }
        map.put("productModel", set);
        return GsonTools.toJson(map);
    }

    @NoLogin
    @Journal(name = "获取卷对应的产品规格", logType = LogType.DB)
    @RequestMapping(value = "getRollProduct", method = RequestMethod.POST)
    public String getRollProduct(String salesProductId) {
        // 根据传入的卷标签产品ID获取投入的原料对应的非套材产品
        FinishedProduct roll = finishProductService.findById(FinishedProduct.class, Long.parseLong(salesProductId));
        return GsonTools.toJson(roll.getProductModel() + ";" + roll.getProductConsumerId());
    }

    @NoLogin
    @Journal(name = "条码作废", logType = LogType.DB)
    @RequestMapping(value = "deleteBarcode", method = RequestMethod.POST)
    public String deleteBarcode(String barcode) {
        return null;
    }
}
