package com.bluebirdme.mes.siemens.mobile.controller;

import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.mobile.base.MobileBaseController;
import com.bluebirdme.mes.siemens.barcode.entity.FragmentBarcode;
import com.bluebirdme.mes.siemens.barcode.entity.SuitErrorLog;
import com.bluebirdme.mes.siemens.barcode.service.IFragmentBarcodeService;
import com.bluebirdme.mes.siemens.order.entity.CutTask;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrder;
import com.bluebirdme.mes.siemens.order.entity.CutTaskSuit;
import com.bluebirdme.mes.siemens.order.entity.PartSuit;
import com.bluebirdme.mes.siemens.order.service.ICutTaskOrderService;
import com.bluebirdme.mes.siemens.order.service.ICutTaskService;
import com.bluebirdme.mes.store.entity.BarCodeType;
import com.bluebirdme.mes.store.entity.PartBarcode;
import com.bluebirdme.mes.store.service.IBarCodeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.j2se.ReflectUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * PAD组套校验
 *
 * @author Goofy
 * @Date 2017年8月8日 下午2:20:39
 */
@Controller
@RequestMapping("/mobile/siemens")
public class MobileSuitController extends MobileBaseController {
    @Resource
    ICutTaskOrderService ctoService;
    @Resource
    ICutTaskService ctService;
    @Resource
    IBarCodeService barcodeService;
    @Resource
    IFragmentBarcodeService fbcService;

    @NoLogin
    @Journal(name = "查询派工单条码信息")
    @ResponseBody
    @RequestMapping("suit/info")
    public String getSuitInfo(String ctoCode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        CutTaskOrder cto = ctoService.findOne(CutTaskOrder.class, "ctoCode", ctoCode);
        if (cto == null)
            return ajaxError("派工单编码无效");

        if (cto.getIsClosed() == 1)
            return ajaxError("派工单已关闭，无法组套");
        if (cto.getIsComplete() == 1)
            return ajaxError("派工单已完成，无法组套");

        TcBomVersionParts part = ctoService.findById(TcBomVersionParts.class, cto.getPartId());
        // 返回组套信息，顺序校验，国内/国外
        // 组套信息
        List<CutTaskSuit> suitList = ctService.find(CutTaskSuit.class, "ctId", cto.getCtId());
        List<CutTaskSuit> suitListCopy = new ArrayList<CutTaskSuit>();
        for (CutTaskSuit suit : suitList) {
            // 如果组套数量大于1，那么拆分一样的成多条数据
            CutTaskSuit cts = null;
            for (int i = 0; i < suit.getSuitCount() - 1; i++) {
                cts = new CutTaskSuit();
                clone(suit, cts);
                suitListCopy.add(cts);
            }
        }
        suitList.addAll(suitListCopy);
        // 排序
        Collections.sort(suitList);
        Map<String, Object> ret = new HashMap<>();
        ret.put("list", suitList);
        ret.put("sort", part.getNeedSort());
        ret.put("consumerCategory", cto.getConsumerCategory());
        return GsonTools.toJson(ret);
    }

    @NoLogin
    @Journal(name = "查询小部件条码信息")
    @ResponseBody
    @RequestMapping("suit/fragment/info")
    public String getFragmentBarcodeInfo(String fragmentBarcode) {
        FragmentBarcode fb = ctService.findOne(FragmentBarcode.class, "barcode", fragmentBarcode);
        if (fb == null) {
            return ajaxError("条码不存在");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("fragmentBarcode", fragmentBarcode);
        if (ctService.has(PartSuit.class, map)) {
            return ajaxError("该条码已被打包");
        }
        fb.setConsumerCategory(fb.getConsumerCategory().equals("国内") ? "1" : "2");
        return GsonTools.toJson(fb);
    }

    @NoLogin
    @Journal(name = "记录错误信息")
    @ResponseBody
    @RequestMapping("suit/errorLog")
    public String errorLog(String partBarcode, String fragmentBarcode, String ctoCode, String msg, String user) {
        FragmentBarcode fb = ctService.findOne(FragmentBarcode.class, "barcode", fragmentBarcode);
        CutTaskOrder cto = ctoService.findOne(CutTaskOrder.class, "ctoCode", ctoCode);
        CutTask ct = ctService.findById(CutTask.class, cto.getCtId());

        SuitErrorLog sel = new SuitErrorLog();
        sel.setErrorMsg(msg);
        sel.setFragmentBarcode(fb.getBarcode());
        sel.setFragmentBatchCode(fb.getBatchCode());
        sel.setFragmentConsumerCategory(fb.getConsumerCategory());
        sel.setFragmentConsumerSimpleName(fb.getConsumerName());
        sel.setFragmentCtCode(fb.getCtCode());
        sel.setFragmentCtoCode(fb.getCtoCode());
        sel.setFragmentName(fb.getFragmentName());
        sel.setFragmentOrderCode(fb.getOrderCode());
        sel.setFragmentPrintType(fb.getPrintType());
        sel.setPartBarcode(partBarcode);
        sel.setPartName(fb.getPartName());
        sel.setScanTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        sel.setSuitBatchCode(cto.getBatchCode());
        sel.setSuitCtCode(ct.getTaskCode());
        sel.setSuitCtoCode(cto.getCtoCode());
        sel.setSuitOrderCode(cto.getOrderCode());
        sel.setScanUser(user);

        ctService.save(sel);
        return ajaxSuccess();
    }

    @NoLogin
    @Journal(name = "查询部件条码信息")
    @ResponseBody
    @RequestMapping("suit/part/info")
    public String getPartInfo(String partBarcode, String ctoCode) throws IllegalArgumentException, SecurityException {
        PartBarcode barcode = barcodeService.findBarcodeInfo(BarCodeType.PART, partBarcode);
        if (barcode == null)
            return ajaxError("无效的部件条码");
        CutTaskOrder cto = ctoService.findOne(CutTaskOrder.class, "ctoCode", ctoCode);
        if (cto == null)
            return ajaxError("无效的部件条码");
        CutTask ct = ctService.findById(CutTask.class, cto.getCtId());
        if (ct == null)
            return ajaxError("无效的部件条码");
        if (ct.getCutPlanId().longValue() != barcode.getPlanId()) {
            return ajaxError("部件和派工单不是来自同一个裁剪计划");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("partBarcode", partBarcode);
        if (barcodeService.has(PartSuit.class, map))
            return ajaxError("该部件已组套，无法继续组套");

        if (barcode.getPartId().longValue() != cto.getPartId()) {
            return ajaxError("部件信息不符");
        }
        map.clear();
        map.put("partName", barcode.getPartName());
        return GsonTools.toJson(map);
    }

    @NoLogin
    @Journal(name = "组套")
    @ResponseBody
    @RequestMapping("suit")
    public String suit(String ctoCode, String part, String fragments, String device, String user) throws Exception {
        fbcService.suit(ctoCode, part, fragments, user, device);
        return ajaxSuccess();
    }


    /**
     * 对象克隆
     *
     * @param t1 旧的对象
     * @param t2 新的对象
     */
    public static <T> void clone(T t1, T t2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        List<Method> methods = ReflectUtils.getMethods(t1.getClass(), true);
        for (Method m : methods) {
            if (m.getName().startsWith("set")) {
                m.invoke(t2, new Object[]{t1.getClass().getMethod(m.getName().replaceFirst("s", "g"), null).invoke(t1, null)});
            }
        }
    }
}
