/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.printer.controller;

import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsDetail;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.planner.cut.entity.CutDailyPlanDetail;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.printer.PrintBarCode;
import com.bluebirdme.mes.printer.entity.Printer;
import com.bluebirdme.mes.printer.service.IPrinterService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 打印机信息Controller
 *
 * @author 徐波
 * @Date 2016-11-14 15:40:50
 */
@Controller
@RequestMapping("/printer")
@Journal(name = "打印机信息")
public class PrinterController extends BaseController {
    // 打印机信息页面
    final String index = "printer/printer";
    final String addOrEdit = "printer/printerAddOrEdit";
    final String printPage = "printer/printerBarcode";
    final String printPageList = "printer/printerBarcodeList";
    final String printPage1 = "printer/tcPrinterBarcode";
    final String budaPage = "printer/buda";

    @Resource
    IPrinterService printerService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取打印机信息列表信息")
    @RequestMapping("list")
    public String getPrinter(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(printerService.findPageInfo(filter, page));
    }

    @Journal(name = "添加打印机信息页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(Printer printer) {
        return new ModelAndView(addOrEdit, model.addAttribute("printer", printer));
    }

    @ResponseBody
    @Journal(name = "保存打印机信息", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(Printer printer) throws Exception {
        printerService.save(printer);
        return GsonTools.toJson(printer);
    }

    @Journal(name = "编辑打印机信息页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(Printer printer) {
        printer = printerService.findById(Printer.class, printer.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("printer", printer));
    }

    @ResponseBody
    @Journal(name = "编辑打印机信息", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(Printer printer) throws Exception {
        printerService.update(printer);
        return GsonTools.toJson(printer);
    }

    @ResponseBody
    @Journal(name = "删除打印机信息", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        printerService.delete(Printer.class, ids);
        return Constant.AJAX_SUCCESS;
    }

    /**
     * @param weavePlanId
     * @param cutPlanId
     * @param count
     * @param pName
     * @param type           [roll,part,box,tray]
     * @param partName
     * @param departmentName
     * @param trugPlanId
     * @param partId
     * @return
     * @throws Exception
     */
    @NoLogin
    @ResponseBody
    @Journal(name = "根据打印机和订单号打印订单的指定产品的条码", logType = LogType.DB)
    @RequestMapping(value = "doPrintBarcode", method = RequestMethod.POST)
    public String doPrintBarcodeByPage(String weavePlanId, String cutPlanId, String count, String pName, String type, String partName, String departmentName, String trugPlanId, Long partId,String copies) throws Exception {
        String lock = buildLock(pName);
        synchronized (lock) {
            return printerService.doPrintBarcodeByPage(weavePlanId, cutPlanId, count, pName, type, partName, departmentName, trugPlanId, partId,copies);
        }
    }

    @NoLogin
    @ResponseBody
    @Journal(name = "根据打印机和订单号打印订单的指定产品的条码doPrintBarcodeByPageList", logType = LogType.DB)
    @RequestMapping(value = "doPrintBarcodeList", method = RequestMethod.POST)
    public String doPrintBarcodeByPageList(String ids, String cutPlanId, String count, String pName, String type, String partName, String departmentCode, String trugPlanId, Long partId) throws Exception {
        String lock = buildLock(pName);
        synchronized (lock) {
            return printerService.doPrintBarcodeByPageList(ids, cutPlanId, count, pName, type, partName, departmentCode, trugPlanId, partId);
        }
    }

    private String buildLock(String str) {
        StringBuilder sb = new StringBuilder(str);
        String lock = sb.toString().intern();
        return lock;
    }


    @NoLogin
    @ResponseBody
    @Journal(name = "获取打印机名字的select选项")
    @RequestMapping(value = "getPrinterInfo", method = RequestMethod.POST)
    public String getPrinterInfo() {
        Long id = (Long) session.getAttribute(Constant.CURRENT_USER_ID);
        List<Printer> plist;
        List<Printer> plist2;
        if (id != -1) {
            User user = printerService.findById(User.class, id);
            HashMap<String, Object> map1 = new HashMap<String, Object>();
            map1.put("departmentId", user.getDid());
            plist = printerService.findListByMap(Printer.class, map1);
            plist2 = printerService.findAll(Printer.class);
            plist2.removeAll(plist);
            plist.addAll(plist2);
        } else {
            plist = printerService.findAll(Printer.class);
        }
        List<HashMap<String, String>> outInfo = new ArrayList<HashMap<String, String>>();
        for (Printer p : plist) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("text", p.getPrinterTxtName());
            map.put("value", p.getPrinterName());
            outInfo.add(map);
        }
        return GsonTools.toJson(outInfo);
    }

    @NoLogin
    @ResponseBody
    @Journal(name = "获取成品胚布名称和ID", logType = LogType.DB)
    @RequestMapping(value = "getPrinterPart", method = RequestMethod.POST)
    public String getPrinterPartNameInfo(Long weaveId) {
        List<TcBomVersionParts> finalPart = new ArrayList<TcBomVersionParts>();
        List<HashMap<String, Object>> outInfo = new ArrayList<HashMap<String, Object>>();
        if (weaveId != null) {
            // 获取编织日计划
            WeavePlan wdp = printerService.findById(WeavePlan.class, Long.valueOf(weaveId));
            if (wdp.getPartName() != null && wdp.getPartId() != null) {
                HashMap<String, Object> rmap = new HashMap<String, Object>();
                rmap.put("text", wdp.getPartName());
                rmap.put("value", wdp.getPartId());
                outInfo.add(rmap);
                return GsonTools.toJson(outInfo);
            }
            // 如果是编织计划，判断是否是胚布，如果是胚布，判断BOM明细，遍历Bom明细，获得bom部件，判断bom部件是否是成品胚布，如果是成品胚布，取出它的bomID，通过编织计划订单ID，获取裁剪计划，通过裁剪计划获取裁剪计划的产品工艺bom，取出相同的bomID
            FinishedProduct fp = printerService.findById(FinishedProduct.class, wdp.getProductId());
            if (fp.getProductIsTc() == -1) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("tcFinishedProductId", fp.getId());
                List<TcBomVersionPartsDetail> tvpdList = printerService.findListByMap(TcBomVersionPartsDetail.class, map);
                HashSet<Long> partIdSet = new HashSet<Long>();
                for (TcBomVersionPartsDetail tvpd : tvpdList) {
                    partIdSet.add(tvpd.getTcProcBomPartsId());
                }
                // bom版本的ID
                HashMap<Long, Long> partIdBomId = new HashMap<Long, Long>();
                HashSet<Long> bomIdSet = new HashSet<Long>();
                HashMap<Long, TcBomVersionParts> partsMap = new HashMap<Long, TcBomVersionParts>();
                for (Long partId : partIdSet) {
                    TcBomVersionParts tvp = printerService.findById(TcBomVersionParts.class, partId);
                    if (tvp.getTcProcBomVersionPartsType().equals("成品胚布")) {
                        partsMap.put(partId, tvp);
                        partIdBomId.put(partId, tvp.getTcProcBomVersoinId());
                        bomIdSet.add(tvp.getTcProcBomVersoinId());
                    }
                }
                if (bomIdSet.size() > 0) {
                    SalesOrderDetail sod = printerService.findById(SalesOrderDetail.class, wdp.getFromSalesOrderDetailId());
                    Long salesOrderId = sod.getSalesOrderId();
                    // 获取订单的ID，获得裁剪计划
                    HashMap<String, Object> map1 = new HashMap<String, Object>();
                    map1.put("weaveSalesOrderId", salesOrderId);
                    CutPlan cp = printerService.findUniqueByMap(CutPlan.class, map1);
                    if (cp != null) {
                        // 如果含有部件明细
                        if (bomIdSet.contains(cp.getProcBomId())) {
                            // 获取部件名称
                            for (Long pid : partIdBomId.keySet()) {
                                // 如果bomId相同，取出部件ID
                                if (partIdBomId.get(pid) == cp.getProcBomId()) {
                                    finalPart.add(partsMap.get(pid));
                                }
                            }
                        }
                    }
                }
            }
        }

        if (finalPart.size() > 0) {
            for (TcBomVersionParts p : finalPart) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("text", p.getTcProcBomVersionPartsName());
                map.put("value", p.getId());
                outInfo.add(map);
            }
        } else {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("text", "非成品胚布");
            map.put("value", -1);
            outInfo.add(map);
        }
        return GsonTools.toJson(outInfo);
    }

    @NoLogin
    @Journal(name = "展示打印页面")
    @RequestMapping(value = "showPrinterPage", method = RequestMethod.GET)
    public ModelAndView showPrinterPage(Long weaveId, Long cutId, String partName, String departmentName, Integer batchcode) {
        // String salesOrderCode = null;
        // Long consumerId = null;
        // Long productId = null;
        // String batchCode = null;
        // Long partId = null;
        Integer printCount = null;
        System.out.println(batchcode);
        if (weaveId != null) {
            // 获取编织日计划
            WeavePlan wdp = printerService.findById(WeavePlan.class, Long.valueOf(weaveId));
            // 如果是编织计划，判断是否是胚布，如果是胚布，判断BOM明细，遍历Bom明细，获得bom部件，判断bom部件是否是成品胚布，如果是成品胚布，取出它的bomID，通过编织计划订单ID，获取裁剪计划，通过裁剪计划获取裁剪计划的产品工艺bom，取出相同的bomID
            // FinishedProduct fp =
            // printerService.findById(FinishedProduct.class,
            // wdp.getProductId());
            // if (fp.getProductIsTc() == -1) {
            // HashMap<String, Object> map = new HashMap<String, Object>();
            // map.put("tcFinishedProductId", fp.getId());
            // List<TcBomVersionPartsDetail> tvpdList =
            // printerService.findListByMap(TcBomVersionPartsDetail.class, map);
            // HashSet<Long> partIdSet=new HashSet<Long>();
            // for(TcBomVersionPartsDetail tvpd:tvpdList){
            // partIdSet.add(tvpd.getTcProcBomPartsId());
            // }
            // //bom版本的ID
            // HashMap<Long,Long> partIdBomId=new HashMap<Long,Long>();
            // HashSet<Long> bomIdSet=new HashSet<Long>();
            // HashMap<Long,TcBomVersionParts> partsMap=new
            // HashMap<Long,TcBomVersionParts>();
            // for(Long partId:partIdSet){
            // TcBomVersionParts
            // tvp=printerService.findById(TcBomVersionParts.class, partId);
            // if(tvp.getTcProcBomVersionPartsType().equals("成品胚布")){
            // partsMap.put(partId, tvp);
            // partIdBomId.put(partId, tvp.getTcProcBomVersoinId());
            // bomIdSet.add(tvp.getTcProcBomVersoinId());
            // }
            // }
            // if(bomIdSet.size()>0){
            // SalesOrderDetail
            // sod=printerService.findById(SalesOrderDetail.class,
            // wdp.getFromSalesOrderDetailId());
            // Long salesOrderId=sod.getSalesOrderId();
            // //获取订单的ID，获得裁剪计划
            // HashMap<String,Object> map1=new HashMap<String,Object>();
            // map1.put("weaveSalesOrderId", salesOrderId);
            // CutPlan cp=printerService.findUniqueByMap(CutPlan.class, map1);
            // List<TcBomVersionParts> finalPart=new
            // ArrayList<TcBomVersionParts>();
            // //如果含有部件明细
            // if(bomIdSet.contains(cp.getProcBomId())){
            // //获取部件名称
            // for(Long pid:partIdBomId.keySet()){
            // //如果bomId相同，取出部件ID
            // if(partIdBomId.get(pid)==cp.getProcBomId()){
            // finalPart.add(partsMap.get(pid));
            // }
            // }
            // }
            // if(finalPart.size()>0){
            // partName=GsonTools.toJson(finalPart);
            // }
            // }
            //
            //
            // }
            printCount = wdp.getRequirementCount().intValue();
        } else {
            CutPlan cut = printerService.findById(CutPlan.class, Long.valueOf(cutId));
            printCount = cut.getTotalRollCount();
        }
        ModelAndView mode = new ModelAndView(printPage, model.addAttribute("weavePlanId", GsonTools.toJson(weaveId)).addAttribute("cutPlanId", cutId).addAttribute("count", printCount).addAttribute("partName", partName).addAttribute("departmentName", departmentName));
        return mode;
    }

    @NoLogin
    @Journal(name = "展示打印页面")
    @RequestMapping(value = "showPrinterPageList", method = RequestMethod.GET)
    public ModelAndView showPrinterPageList(String ids, String departmentCode) {
        String str[] = ids.split(",");
        Integer printCount = null;
        // 获取编织日计划
        WeavePlan wdp = printerService.findById(WeavePlan.class, Long.valueOf(str[0]));
        printCount = wdp.getRequirementCount().intValue();
        ModelAndView mode = new ModelAndView(printPageList, model.addAttribute("ids", ids).addAttribute("count", printCount).addAttribute("departmentCode", departmentCode));
        return mode;
    }

    @NoLogin
    @Journal(name = "展示打印页面")
    @RequestMapping(value = "showPrinterPage1", method = RequestMethod.GET)
    public ModelAndView showPrinterPage1(Long cutPlanId, Long cutDailyPlanId, String departmentName) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("cutPlanId", cutPlanId);
        map.put("cutPlanDailyId", cutDailyPlanId);
        CutDailyPlanDetail cpd = printerService.findUniqueByMap(CutDailyPlanDetail.class, map);
        ModelAndView mode = new ModelAndView(printPage1, model.addAttribute("cutDailyPlanId", cutDailyPlanId).addAttribute("cutPlanId", cpd.getId()).addAttribute("departmentName", departmentName));
        return mode;
    }

    @NoLogin
    @ResponseBody
    @Journal(name = "根据打印机和订单号打印订单的指定产品的条码", logType = LogType.DB)
    @RequestMapping(value = "rePrint", method = RequestMethod.POST)
    public String rePrint(String ids, String pName, String type) {
		/*DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		PrintService[] psZebras = PrintServiceLookup.lookupPrintServices(flavor, null);
		boolean isContinue = false;
		for (PrintService pz : psZebras) {
			if (pz.getName().equals(pName)) {
				isContinue = true;
				break;
			}
		}
		if (!isContinue) {
			return GsonTools.toJson("找不到打印机");
		}*/
        return GsonTools.toJson(printerService.rePrint(ids, pName, type));
    }

    @NoLogin
    @ResponseBody
    @Journal(name = "打印空的盒条码和托条码和套条码", logType = LogType.DB)
    @RequestMapping(value = "printBarcodeFirst", method = RequestMethod.POST)
    public String printBarcodeFirst(String departmentType, String pName, Integer trayCount, Integer trayPartCount, Integer boxCount,Integer copies) throws Exception {
       String message="";
        if (boxCount != null && boxCount > 0) {
            message=printerService.printBarcodeFirst(departmentType, pName, "box", boxCount,copies);
        }

        if (trayCount != null && trayCount > 0) {
            message= printerService.printBarcodeFirst(departmentType, pName, "tray", trayCount,copies);
        }

        if (trayPartCount != null && trayPartCount > 0) {
            message=printerService.printBarcodeFirst(departmentType, pName, "trayPart", trayPartCount,copies);
        }

        if (message.equals("打印成功")) {
            return ajaxSuccess();
        } else {
            return ajaxError(message);
        }
    }

    @NoLogin
    @ResponseBody
    @Journal(name = "根据打印机和订单号打印订单的指定产品的条码", logType = LogType.DB)
    @RequestMapping(value = "buda", method = RequestMethod.POST)
    public String buda(String ids, String pName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        String result = printerService.buda(ids, pName);
        if (result.equals("打印成功")) {
            return ajaxSuccess();
        } else {
            return ajaxError(result);
        }
    }

    @NoLogin
    @Journal(name = "展示打印页面")
    @RequestMapping(value = "showbudaPage", method = RequestMethod.GET)
    public ModelAndView showbudaPage(String ids) {
        ModelAndView mode = new ModelAndView(budaPage, model.addAttribute("ids", ids));
        return mode;
    }
}
