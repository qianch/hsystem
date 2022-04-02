/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.printer.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.planner.cut.entity.CutDailyPlanDetail;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.printer.service.IMergePrinterService;
import com.bluebirdme.mes.printer.service.IPrinterService;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.store.entity.BoxBarcode;
import com.bluebirdme.mes.store.entity.PartBarcode;
import com.bluebirdme.mes.store.entity.RollBarcode;
import com.bluebirdme.mes.store.entity.TrayBarCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 打印机信息Controller
 *
 * @author 徐波
 * @Date 2016-11-14 15:40:50
 */
@Controller
@RequestMapping("/individualprinter")
@Journal(name = "打印机信息")
public class IndividualPrinterController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(IndividualPrinterController.class);
    // 打印机信息页面
    final String individualizationprinitPage = "printer/individualPrinterBarcode";
    final String printerReplayBarcodePage = "printer/printerReplayBarcode";
    final String cutPrinterPage = "printer/individualTcPrinterBarcode";

    @Resource
    IPrinterService printerService;

    @Resource
    IMergePrinterService individualPrinterService;

    /**
     * @param weavePlanId
     * @param cutPlanId
     * @param count
     * @param pName
     * @param type           [roll,part,box,tray]
     * @param partName
     * @param departmentCode
     * @param trugPlanId
     * @param partId
     * @param devCode
     * @return 季晓龙新增-2020-04-30
     * @throws Exception
     */
    @NoLogin
    @ResponseBody
    @Journal(name = "根据打印机和订单号打印订单的指定产品的条码", logType = LogType.DB)
    @RequestMapping(value = "doIndividualPrintBarcode", method = RequestMethod.POST)
    public String doIndividualPrintBarcode(String weavePlanId, String cutPlanId, String count, String pName, String type, String partName, String departmentCode, String trugPlanId, Long partId, String btwfileId, String devCode,String copies) throws Exception {

        String lock = buildLock(pName);
        synchronized (lock) {
            if ("0".equals(btwfileId)) {
                switch (type) {
                    case "roll":
                    case "part":
                        return printerService.doPrintBarcodeByPage(weavePlanId, cutPlanId, count, pName, type, partName, departmentCode, trugPlanId, partId,copies);
                    case "tray":
                    case "box":
                    case "trayPart":
                        printerService.printBarcodeFirst("1", pName, type, Integer.parseInt(count.split("\\.")[0]),0);
                        return "";
                    default:
                        return "";
                }
            } else {
                switch (type) {
                    case "roll":
                    case "part":
                        return individualPrinterService.doIndividualPrintBarcode(weavePlanId, cutPlanId, count, pName, type, partName, departmentCode, trugPlanId, partId, btwfileId, devCode,copies);
                    default:
                        return "";
                }
            }
        }
    }


    @NoLogin
    @Journal(name = "展示打印页面")
    @RequestMapping(value = "showIndividualPrinterPage", method = RequestMethod.GET)
    public ModelAndView showIndividualPrinterPage(Long weaveId, Long cutId, String partName, String departmentCode, Integer batchcode, String devCode, String type) {
        Integer printCount = null;
        System.out.println(batchcode);
        if (weaveId != null) {
            // 获取编织日计划
            WeavePlan wdp = printerService.findById(WeavePlan.class, Long.valueOf(weaveId));
            printCount = wdp.getRequirementCount().intValue();
        } else {
            CutPlan cut = printerService.findById(CutPlan.class, Long.valueOf(cutId));
            printCount = cut.getTotalRollCount();
        }
        ModelAndView mode = new ModelAndView(individualizationprinitPage, model.addAttribute("weavePlanId", GsonTools.toJson(weaveId)).addAttribute("cutPlanId", cutId).addAttribute("devCode", devCode).addAttribute("type", type).addAttribute("count", printCount).addAttribute("partName", partName).addAttribute("departmentCode", departmentCode));
        return mode;
    }

    @NoLogin
    @Journal(name = "展示打印页面")
    @RequestMapping(value = "showReplayBarcodePrinterPage", method = RequestMethod.GET)
    public ModelAndView showReplayBarcodePrinterPage(String id, String type) throws Exception {
        String customerId = "";
        String barCode = "";
        switch (type) {
            case "roll":
                RollBarcode roll = printerService.findById(RollBarcode.class, Long.parseLong(id));
                barCode = roll.getBarcode();
                WeavePlan wp1 = printerService.findById(WeavePlan.class, roll.getPlanId());
                if (wp1.getPartId() != null) {
                    type = "roll_peibu";
                }
                SalesOrderDetail salesOrderDetail1 = printerService.findById(SalesOrderDetail.class, roll.getSalesOrderDetailId());
                SalesOrder sorder1 = printerService.findById(SalesOrder.class, salesOrderDetail1.getSalesOrderId());
                customerId = sorder1.getSalesOrderConsumerId().toString();
                break;
            case "box":
                BoxBarcode box = printerService.findById(BoxBarcode.class, Long.parseLong(id));
                barCode = box.getBarcode();
                SalesOrderDetail salesOrderDetail2 = printerService.findById(SalesOrderDetail.class, box.getSalesOrderDetailId());
                SalesOrder sorder2 = printerService.findById(SalesOrder.class, salesOrderDetail2.getSalesOrderId());
                customerId = sorder2.getSalesOrderConsumerId().toString();
                break;
            case "part":
                PartBarcode part = printerService.findById(PartBarcode.class, Long.parseLong(id));
                barCode = part.getBarcode();
                break;
            default:
                type = "tray";
                TrayBarCode traybarcode = printerService.findById(TrayBarCode.class, Long.parseLong(id));
                barCode = traybarcode.getBarcode();
                SalesOrderDetail salesOrderDetail3 = printerService.findById(SalesOrderDetail.class, traybarcode.getSalesOrderDetailId());
                SalesOrder sorder3 = printerService.findById(SalesOrder.class, salesOrderDetail3.getSalesOrderId());
                customerId = sorder3.getSalesOrderConsumerId().toString();
                break;
        }

        ModelAndView mode = new ModelAndView(printerReplayBarcodePage, model.addAttribute("id", id).addAttribute("type", type).addAttribute("customerId", customerId).addAttribute("barCode", barCode));
        return mode;
    }


    @NoLogin
    @Journal(name = "展示打印页面")
    @RequestMapping(value = "showCutPrinterPage", method = RequestMethod.GET)
    public ModelAndView showCutPrinterPage(Long cutPlanId, Long cutDailyPlanId, String departmentCode, String type) {

        CutPlan cutplan = printerService.findById(CutPlan.class, cutPlanId);
        long customerId = cutplan.getConsumerId();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("cutPlanId", cutPlanId);
        map.put("cutPlanDailyId", cutDailyPlanId);
        CutDailyPlanDetail cpd = printerService.findUniqueByMap(CutDailyPlanDetail.class, map);

        ModelAndView mode = new ModelAndView(cutPrinterPage, model.addAttribute("cutDailyPlanId", cutDailyPlanId).addAttribute("cutPlanId", cutPlanId).addAttribute("cutDailyPlanDetailId", cpd.getId()).addAttribute("departmentCode", departmentCode).addAttribute("customerId", customerId).addAttribute("type", type));
        return mode;
    }


    @NoLogin
    @ResponseBody
    @Journal(name = "根据打印机和订单号打印订单的指定产品的条码", logType = LogType.DB)
    @RequestMapping(value = "doReplayPrintBarcode", method = RequestMethod.POST)
    public String doReplayPrintBarcode(String id, String type, String departmentCode, String pName, String btwfileId) throws Exception {
        String lock = buildLock(pName);
        synchronized (lock) {
            return GsonTools.toJson(individualPrinterService.doReplayPrintBarcode(id, type, departmentCode, pName, btwfileId));
        }
    }


    @NoLogin
    @ResponseBody
    @Journal(name = "根据打印机和订单号打印订单的指定产品的条码rePrint", logType = LogType.DB)
    @RequestMapping(value = "rePrint", method = RequestMethod.POST)
    public String rePrint(String ids, String pName, String type) {
        String lock = buildLock(pName);
        synchronized (lock) {
            return GsonTools.toJson(individualPrinterService.rePrint(ids, pName, type));
        }
    }

    @NoLogin
    @ResponseBody
    @Journal(name = "根据打印机和订单号打印订单的指定产品的条码reIndividualPrint", logType = LogType.DB)
    @RequestMapping(value = "reIndividualPrint", method = RequestMethod.POST)
    public String reIndividualPrint(String id, String pName, String type, long btwfileId,int printCount) {
        String lock = buildLock(pName);
        synchronized (lock) {
            return GsonTools.toJson(individualPrinterService.reIndividualPrint(id, pName, type, btwfileId,printCount));
        }
    }

    private String buildLock(String str) {
        StringBuilder sb = new StringBuilder(str);
        String lock = sb.toString().intern();
        return lock;
    }
}
