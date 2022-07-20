package com.bluebirdme.mes.mobile.produce.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.device.entity.WeightCarrier;
import com.bluebirdme.mes.device.service.IWeightCarrierService;
import com.bluebirdme.mes.mobile.base.MobileBaseController;
import com.bluebirdme.mes.mobile.produce.service.IWeighService;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.store.entity.BarCodeRegType;
import com.bluebirdme.mes.store.entity.BarCodeType;
import com.bluebirdme.mes.store.entity.Roll;
import com.bluebirdme.mes.store.entity.RollBarcode;
import com.bluebirdme.mes.store.service.IBarCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xdemo.superutil.j2se.MathUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 称重设备重量
 *
 * @author Goofy
 * @Date 2017年7月11日 下午6:33:48
 */
@RestController
@RequestMapping("/mobile/weight")
public class MobileWeightController extends MobileBaseController {
    @Resource
    IWeightCarrierService carrierService;

    @Resource
    IBarCodeService codeService;

    @Resource
    IWeighService wService;

    /**
     * 1.判断是否有未称重的卷
     * 2.该卷是否已产出登记
     * 3.理论重量
     * 4.称重规则
     *
     * @param barcode
     * @return
     * @throws ParseException
     */
    @Journal(name = "称重信息查询")
    @NoLogin
    @RequestMapping("info")
    public String info(String barcode, String deviceCode) throws ParseException {
        RollBarcode rbc = wService.findOne(RollBarcode.class, "barcode", barcode);
        if (rbc == null) {
            return ajaxError("无效的卷条码");
        }
        String codes = wService.hasWaitForWeighRoll(rbc.getPlanId(), deviceCode);

        if (codes != null && !codes.equals("")) {
            return ajaxError(codes + "尚未称重");
        }

        Roll roll = wService.findOne(Roll.class, "rollBarcode", barcode);
        if (roll != null) {
            return ajaxError("该卷已产出登记");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("theoryWeight", wService.getRollTheoryWeight(barcode));
        map.put("weighRule", wService.getRollWeighRule(barcode));
        map.put("avg", wService.getAvg(rbc.getPlanId(), deviceCode));
        map.put("index", wService.getWeaveRollIndex(rbc.getPlanId()));
        map.put("batchCode", rbc.getBatchCode());

        WeavePlan wp = wService.findById(WeavePlan.class, rbc.getPlanId());
        map.put("total", wp.getRequirementCount());
        map.put("shouldWeigh", wService.shouldWeigh(barcode, deviceCode));

        FinishedProduct fp = wService.findById(FinishedProduct.class, rbc.getSalesProductId());
        SalesOrderDetail salesOrderDetail = wService.findById(SalesOrderDetail.class, rbc.getSalesOrderDetailId());
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("productId", rbc.getSalesProductId());
        map1.put("procBomId", rbc.getMirrorProcBomId());
        map1.put("salesOrderDetailId", salesOrderDetail.getId());
        List<FinishedProductMirror> fpm = wService.findListByMap(FinishedProductMirror.class, map1);
        if (fpm.size() == 0) {
            map1.clear();
            map1.put("productId", rbc.getSalesProductId());
            map1.put("procBomId", rbc.getMirrorProcBomId());
            map1.put("salesOrderId", salesOrderDetail.getSalesOrderId());
            fpm = wService.findListByMap(FinishedProductMirror.class, map1);
        }

        if (fpm.size() > 0) {
            map.put("rollLength", fpm.get(0).getProductRollLength());
            map.put("rollWeight", fpm.get(0).getProductRollWeight());
            map.put("rollMaxWeight", fpm.get(0).getMaxWeight());
            map.put("rollMinWeight", fpm.get(0).getMinWeight());
            //TODO 定长定重判断
            map.put("fixLength", fpm.get(0).getProductRollWeight() == null ? true : false);
        } else {
            map.put("rollLength", fp.getProductRollLength());
            map.put("rollWeight", fp.getProductRollWeight());
            map.put("rollMaxWeight", fp.getMaxWeight());
            map.put("rollMinWeight", fp.getMinWeight());
            //TODO 定长定重判断
            map.put("fixLength", fp.getProductRollWeight() == null ? true : false);
        }
        return GsonTools.toJson(map);
    }

    @NoLogin
    @Journal(name = "载具信息查询")
    @RequestMapping("tare")
    public String getTareWeight(String rollBarcode, String carrierCode) {
        Map<String, Object> map = new HashMap();

        if (StringUtils.isBlank(rollBarcode) && !StringUtils.isBlank(carrierCode)) {
            WeightCarrier c = carrierService.findByCode(carrierCode);
            if (c == null) {
                return errorInfo("无效的载具编码");
            }
            map.put("w", c.getCarrierWeight());
            return GsonTools.toJson(c);
        }

        if (!StringUtils.isBlank(rollBarcode) && !StringUtils.isBlank(carrierCode)) {
            RollBarcode rbc = codeService.findBarcodeInfo(BarCodeType.ROLL, rollBarcode);
            if (rbc == null) {
                return errorInfo("无效的卷条码");
            }

            Roll roll = codeService.findBarCodeReg(BarCodeRegType.ROLL, rollBarcode);
            if (roll == null) {
                return errorInfo("该卷条码尚未产出登记");
            }

            FinishedProduct fp = codeService.findById(FinishedProduct.class, rbc.getSalesProductId());
            if (fp == null) {
                return errorInfo("找不到产品信息");
            }
            if (StringUtils.isBlank(fp.getCarrierCode())) {
                return errorInfo("请维护产品的纸管重量");
            }

            WeightCarrier c = carrierService.findByCode(carrierCode);
            if (c == null) {
                return errorInfo("无效的载具编码");
            }

            WeightCarrier c2 = carrierService.findByCode(fp.getCarrierCode());

            if (c2 == null) {
                return errorInfo("产品的纸管编码无效");
            }
            return weight(MathUtils.add(c.getCarrierWeight(), c2.getCarrierWeight(), 2) + "");
        }

        if (!StringUtils.isBlank(rollBarcode) && StringUtils.isBlank(carrierCode)) {
            RollBarcode rbc = codeService.findBarcodeInfo(BarCodeType.ROLL, rollBarcode);
            if (rbc == null) {
                return errorInfo("无效的卷条码");
            }
            Roll roll = codeService.findBarCodeReg(BarCodeRegType.ROLL, rollBarcode);
            if (roll == null) {
                return errorInfo("该卷条码尚未产出登记");
            }
            FinishedProduct fp = codeService.findById(FinishedProduct.class, rbc.getSalesProductId());
            if (fp == null) {
                return errorInfo("找不到产品信息");
            }
            if (StringUtils.isBlank(fp.getCarrierCode())) {
                return errorInfo("请维护产品的纸管编码");
            }

            WeightCarrier c2 = carrierService.findByCode(fp.getCarrierCode());
            if (c2 == null) {
                return errorInfo("产品的纸管编码无效");
            }
            return weight(MathUtils.add(0D, c2.getCarrierWeight(), 2) + "");
        }
        return ajaxError("参数不完整");
    }

    /**
     * @param code
     * @param weight
     * @return
     * @throws Exception
     */
    @NoLogin
    @Journal(name = "设备称重接口", logType = LogType.DB)
    @RequestMapping(value = "saveWeight")
    public String saveWeight(String code, Double weight, String qualityGrade) throws Exception {
        wService.saveWeight(code, weight, qualityGrade);
        return ajaxSuccess();
    }

    public String errorInfo(String msg) {
        Map<String, Object> map = new HashMap();
        map.put("ERROR", true);
        map.put("MSG", msg);
        return GsonTools.toJson(map);
    }

    public String weight(String weight) {
        Map<String, Object> map = new HashMap();
        map.put("ERROR", false);
        map.put("WEIGHT", weight);
        return GsonTools.toJson(map);
    }
}
