package com.bluebirdme.mes.mobile.stock.controller;

import com.bluebirdme.mes.baseInfo.entity.Material;
import com.bluebirdme.mes.baseInfo.service.IMaterialService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.mobile.base.MobileBaseController;
import com.bluebirdme.mes.platform.entity.ExceptionMessage;
import com.bluebirdme.mes.platform.service.IExceptionMessageService;
import com.bluebirdme.mes.stock.entity.*;
import com.bluebirdme.mes.stock.service.IMaterialStockService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.xdemo.superutil.j2se.StringUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用巨石原料的webservice接口
 *
 * @author Goofy
 * @Date 2016年12月2日 下午3:48:08
 */

@RestController
@RequestMapping("/mobile/stock/material")
public class MobileMaterialController extends MobileBaseController {
    private static Logger log = LoggerFactory.getLogger(MobileMaterialController.class);
    @Resource
    IExceptionMessageService msgService;
    @Resource
    IMaterialStockService materialStockService;
    @Resource
    IMaterialService materialService;

    @NoLogin
    @Journal(name = "巨石物料接口")
    @RequestMapping("jushi/{code}")
    public String getJuShiMaterialInfo(@PathVariable("code") String code) {
        // 通过巨石webservice获取原料信息
        String jushi = null;
        // jushi="Direct Roving,E6DR17-2400-381(W),1421701162027WX,923,2017-01-17 06:08:23,±3,48-12K1;0";
        // jushi="Assembled Roving,ER13-2400-162E,2121702111056XX,816,2017-02-11 12:26:56,无,48-16K1;0";
        // 注意：上下偏差可能为“无”，表示无偏差
        try {
            //10.0.1.61 实际访问IP，10.10.1.9:81 Ngingx代理之后的IP
            //Document doc=Jsoup.connect("http://10.0.1.61/HengShi/ExternalInterface.asmx/HSGetInfo").data("traybarcode", code).post();
            Document doc = Jsoup.connect("http://10.10.1.9:81/HengShi/ExternalInterface.asmx/HSGetInfo").data("traybarcode", code).post();
            jushi = doc.getElementsByTag("string").text();
            //jushi = "Direct Roving,E6DR13-300-380YF,1411612272099WX,1039.00,2016-12-28 01:13:14,-1~1,48-12K1;0";
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return error(e.getMessage());
        }

        // 解析结果
        if (jushi.endsWith(";0")) {
            String[] s = jushi.substring(0, jushi.length() - 2).split(",");
            System.out.println(GsonTools.toJson(s));
            Map<String, Object> info = new HashMap<String, Object>();
            Map<String, String> ret = new HashMap<String, String>();
            // 产品大类
            ret.put("produceCategory", s[0]);
            // 规格型号
            ret.put("materialModel", s[1]);
            // 托盘编码
            ret.put("palletCode", s[2]);
            // 重量
            ret.put("weight", s[3]);
            // 生产日期
            ret.put("produceDate", s[4]);
            String upperDeviation = s[5];
            int n = upperDeviation.length() - upperDeviation.replaceAll("±", "").length();
            if (n > 1) {
                return error("上偏差中（±）出现多个，请检查原料来源数据");
            }

            try {
                // 计算上下偏差
                if (!StringUtils.isBlank(upperDeviation)) {
                    if (upperDeviation.equals("无")) {
                        ret.put("lowerDeviation", "0");
                        ret.put("upperDeviation", "0");
                    } else {
                        if (upperDeviation.startsWith("±")) {
                            ret.put("lowerDeviation", upperDeviation.replace("±", "-"));
                            ret.put("upperDeviation", upperDeviation.replace("±", "+"));
                        } else {
                            ret.put("lowerDeviation", upperDeviation.split("~")[0]);
                            ret.put("upperDeviation", upperDeviation.split("~")[1]);
                        }
                    }
                } else {
                    ret.put("lowerDeviation", "0");
                    ret.put("upperDeviation", "0");
                }
            } catch (Exception e) {
                return error("巨石原料上下偏差异常，请检查");
            }
            ret.put("subWay", s[6]);
            // 查询原料信息
            Material mt = materialStockService.findMaterial(s[0], s[1]);
            if (mt == null)
                return error("未知的原料规格");

            MaterialStockState mss = materialStockService.findOne(MaterialStockState.class, "palletCode", s[2]);
            if (mss != null)
                return error("该托原料已入库");
            info.put("jushi", ret);
            info.put("materialInfo", mt);
            // 返回结果信息
            return GsonTools.toJson(info);
        } else if (jushi.endsWith(";-1")) {
            log("[巨石接口]原料接口异常,条码号:" + code, 0);
            return error("[巨石接口]原料接口异常,条码号:" + code);
        } else if (jushi.endsWith(";1")) {
            log("[巨石接口]找不到该原料信息,条码号:" + code, 0);
            return error("[巨石接口]找不到该原料信息,条码号:" + code);
        }
        return error("未知异常");
    }

    @NoLogin
    @Journal(name = "查询原料信息")
    @RequestMapping("{code}")
    public String getMaterialInfo(@PathVariable("code") String code) {
        System.out.println(parseMaterialCode(code));
        MaterialStockState mss = materialStockService.findOne(MaterialStockState.class, "palletCode", parseMaterialCode(code));
        if (mss == null) return error("该条码尚未入库");
        Material mt = materialStockService.findById(Material.class, mss.getMid());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("produceCategory", mt.getProduceCategory());
        map.put("materialModel", mt.getMaterialModel());
        map.put("barcode", mss.getPalletCode());
        map.put("productionDate", mss.getProductionDate());
        map.put("upperDeviation", mss.getRealUpperDeviation());
        map.put("lowerDeviation", mss.getRealLowerDeviation());
        map.put("subWay", mt.getSubWay());
        map.put("weight", mss.getWeight());
        map.put("palletCode", parseMaterialCode(code));
        map.put("isPass", mss.getIsPass());
        map.put("isLocked", mss.getIsLocked());
        map.put("state", mss.getState());
        map.put("stockState", mss.getStockState());
        map.put("id", mss.getId());
        map.put("workshop", materialStockService.getOutWorkShop(parseMaterialCode(code)));
        return GsonTools.toJson(map);
    }

    /**
     * 查询原料详细信息，包括库存状态，所在位置，其他信息等等
     *
     * @param code
     * @return
     */
    @NoLogin
    @Journal(name = "查询原料详细信息")
    @RequestMapping("info/{code}")
    public String materialInfo(@PathVariable("code") String code) {
        if (code.length() > 20) {
            code = parseMaterialCode(code);
        }
        return GsonTools.toJson(materialService.materialInfo(code));
    }


    public void log(String info, int lineNumber) {
        log.error(info);
        ExceptionMessage msg = new ExceptionMessage();
        msg.setClazz(MobileMaterialController.class.getName());
        msg.setLineNumber(lineNumber);
        msg.setMethod("getMaterialInfo");
        msg.setMsg(info);
        msg.setOccurDate(new Date());
        msgService.save(msg);
    }

    /**
     * 格式化原料条码
     *
     * @param code
     * @return
     */
    public static String parseMaterialCode(String code) {
        if (code.length() == 20) {
            return code;
        } else if (code.length() == 18) {
            return code;
        }
        return code.substring(4, 19);
    }


    /**
     * 原料入库
     *
     * @param mss
     * @param mir
     * @return
     */
    @Journal(name = "巨石原料入库", logType = LogType.DB)
    @RequestMapping("in")
    @ResponseBody
    @NoLogin
    public String in(MaterialStockState mss, MaterialInRecord mir) {
        materialStockService.mIn(mss, mir);
        return ajaxSuccess();
    }

    /**
     * 原料退库
     *
     * @param mir
     * @param palletCode
     */
    @NoLogin
    @ResponseBody
    @Journal(name = "车间退料", logType = LogType.DB)
    @RequestMapping("back")
    public String back(MaterialInRecord mir, String palletCode) {
        MaterialStockState mss = materialStockService.findOne(MaterialStockState.class, "palletCode", palletCode);
        if (mss == null) return error("该原料尚未入库");
        if (mss.getStockState() == 0) return error("该托原料未出库，无法退料");
        materialStockService.back(mir, palletCode);
        return ajaxSuccess();
    }

    /**
     * 车间领料出库
     * 总重累计、出库状态由hs_material_out_order_detail表上的触发器完成
     *
     * @param mso
     * @param mssIds
     * @return
     * @throws Exception
     */
    @NoLogin
    @ResponseBody
    @Journal(name = "原料出库", logType = LogType.DB)
    @RequestMapping("out")
    public synchronized String out(MaterialStockOut mso, Long[] mssIds) throws Exception {
        materialStockService.out(mso, mssIds);
        return ajaxSuccess();
    }

    /**
     * 原料判级
     *
     * @param state
     * @param mssId
     * @return
     */
    @NoLogin
    @ResponseBody
    @Journal(name = "原料判级", logType = LogType.DB)
    @RequestMapping("grade")
    public String grade(Integer state, Long mssId) {
        MaterialStockState mss = materialStockService.findById(MaterialStockState.class, mssId);
        if (mss.getStockState() == 1) {
            return ajaxError("原料已出库，无法判级");
        }
        mss.setState(state);
        materialStockService.update(mss);
        return ajaxSuccess();
    }

    /**
     * 异常退库（退回巨石）
     *
     * @param out
     * @return
     * @throws Exception
     * @throws IOException
     */
    @NoLogin
    @ResponseBody
    @Journal(name = "异常退库（退回巨石）", logType = LogType.DB)
    @RequestMapping("backToJuShi")
    public String backToJuShi(MaterialForceOutRecord out) throws Exception {
        materialStockService.backToJuShi(out);
        return ajaxSuccess();
    }

    /**
     * 移库
     *
     * @param ids
     * @param warehouseCode
     * @param warehousePosCode
     * @return
     */
    @NoLogin
    @ResponseBody
    @Journal(name = "移库", logType = LogType.DB)
    @RequestMapping("move")
    public String move(Long ids[], String warehouseCode, String warehousePosCode) {
        materialStockService.move(ids, warehouseCode, warehousePosCode);
        return ajaxSuccess();
    }

    /**
     * 根据仓库代码查询原料信息
     *
     * @param warehouseCode
     * @param warehousePosCode
     * @return
     */
    @NoLogin
    @ResponseBody
    @Journal(name = "根据仓库代码查询原料信息")
    @RequestMapping("list")
    public String list(String warehouseCode, String warehousePosCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("warehouseCode", warehouseCode);
        map.put("warehousePosCode", warehousePosCode);
        map.put("stockState", 0);
        return GsonTools.toJson(materialStockService.findListByMap(MaterialStockState.class, map));
    }

    /**
     * 原料盘库
     *
     * @param sc
     * @return
     */
    @NoLogin
    @ResponseBody
    @Journal(name = "原料盘库")
    @RequestMapping("check")
    public String checkResult(@RequestBody StockCheck sc) {
        materialStockService.checkResult(sc);
        return ajaxSuccess();
    }
}
