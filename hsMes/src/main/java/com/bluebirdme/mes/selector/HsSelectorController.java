package com.bluebirdme.mes.selector;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.BcBom;
import com.bluebirdme.mes.baseInfo.entity.BcBomVersionDetail;
import com.bluebirdme.mes.baseInfo.entity.FtcBomDetail;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.baseInfo.service.IBCBomVersionService;
import com.bluebirdme.mes.baseInfo.service.IBcBomVersionDetailService;
import com.bluebirdme.mes.baseInfo.service.IFtcBomService;
import com.bluebirdme.mes.baseInfo.service.ITcBomService;
import com.bluebirdme.mes.common.service.ICommonService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.produce.service.IProducePlanService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;

/**
 * 选择器界面
 *
 * @author Goofy
 * @Date 2016年10月13日 下午2:12:27
 */
@Controller
@RequestMapping("/selector")
public class HsSelectorController extends BaseController {
    // 选择产品窗口
    private final String choose_product = "selector/product";
    // 选择客户信息窗口
    private final String choose_consumer = "selector/consumer";
    // 选择套材BOM窗口
    private final String choose_tcBom = "selector/tcBom";
    // 选择包材BOM窗口
    private final String choose_bcBom = "selector/packingBom";
    // 选择用户窗口
    private final String choose_User = "selector/user";
    private final String fragment_User = "fragment/user";
    // 选择设备窗口
    private final String choose_Device = "selector/device";
    // 选择生产计划明细窗口
    private final String choose_producePlanDetail = "selector/producePlanDetail";
    // 选择窗口
    private final String choose_salesOrder = "selector/salesOrder";
    // 选择生产计划
    private final String choose_producePlan = "selector/producePlan";
    // 选择库位信息
    private final String choose_warehousePos = "selector/warehousePos";
    private final String choose_complaint = "selector/complaint";
    // 查看非套材版本明细
    private final String choose_ftcView = "selector/ftcView";
    // 查看套材版本明细
    private final String choose_tcView = "selector/tcView";
    // 查看包材版本明细
    private final String choose_bcView = "selector/bcView";


    private final String selectorPrintTemplateUrl = "selector/selectorPrintTemplate";

    private final String selectorCutTcBomMainUrl = "selector/selectorCutTcBomMain";

    final String selectorBtwFilePrintUrl = "selector/selectorBtwFilePrint";

    // 添加任务工艺明细需求
    final String addReq = "selector/addReq";
    final String auditEdit = "selector/auditEdit";

    @Resource
    IFtcBomService ftcBomService;
    @Resource
    ITcBomService tcBomService;
    @Resource
    IBCBomVersionService bCBomVersionService;
    @Resource
    IBcBomVersionDetailService bcBomVersionDetailService;
    @Resource
    IProducePlanService producePlanService;

    @Resource
    ICommonService commonService;

    @RequestMapping(value = "view/bc", method = RequestMethod.GET)
    public ModelAndView bcDetail(Long packBomId) {
        BCBomVersion bCBomVersion = bCBomVersionService.findById(BCBomVersion.class, packBomId);
        if (bCBomVersion == null) {
            return null;
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("packVersionId", bCBomVersion.getId());
        List<BcBomVersionDetail> li = bcBomVersionDetailService.findListByMap(BcBomVersionDetail.class, map);
        BcBom bom = bCBomVersionService.findById(BcBom.class, bCBomVersion.getPackBomId());
        return new ModelAndView(choose_bcView, model.addAttribute("bCBomVersion", bCBomVersion).addAttribute("bom", GsonTools.toJson(bom)).addAttribute("details", GsonTools.toJson(li)).addAttribute("error", 0));
    }

    @RequestMapping(value = "view/tc", method = RequestMethod.GET)
    public ModelAndView tcDetail(Long procBomId) {
        TcBomVersion tcBomVersion = tcBomService.findById(TcBomVersion.class, procBomId);
        if (tcBomVersion == null)
            return null;
        return new ModelAndView(choose_tcView, model.addAttribute("tcBomVersion", tcBomVersion));
    }

    @RequestMapping(value = "view/ftc", method = RequestMethod.GET)
    public ModelAndView ftcDetail(Long procBomId) {
        FtcBomVersion ftcBomVersion = ftcBomService.findById(FtcBomVersion.class, procBomId);
        if (ftcBomVersion == null) {
            return null;
        }
        HashMap<String, Object> map = new HashMap();
        map.put("ftcBomVersionId", ftcBomVersion.getId());
        List<FtcBomDetail> li = ftcBomService.findListByMap(FtcBomDetail.class, map);
        return new ModelAndView(choose_ftcView, model.addAttribute("ftcBomVersion", ftcBomVersion).addAttribute("details", GsonTools.toJson(li)));
    }

    @Journal(name = "提交审核页面")
    @RequestMapping(value = "commitAudit", method = RequestMethod.GET)
    public ModelAndView commitAudit(String id) {
        return new ModelAndView(auditEdit, model.addAttribute("id", id));
    }

    @RequestMapping(value = "addReq", method = RequestMethod.GET)
    public ModelAndView _addReq(Long pid, Long productId) {
        if (pid == null) pid = -1L;
        if (productId == null) productId = -1L;
        ProducePlanDetail ppd = commonService.findById(ProducePlanDetail.class, pid);
        FinishedProduct product = commonService.findById(FinishedProduct.class, productId);
        String pack = "";
        String proc = "";

        if (product != null) {
            pack = product.getPackReq();
            proc = product.getProcReq();
        }

        if (ppd != null) {
            pack = ppd.getPackReq();
            proc = ppd.getProcReq();
        }

        return new ModelAndView(addReq, model.addAttribute("proc", proc).addAttribute("pack", pack));
    }

    @RequestMapping("warehousePos")
    public String chooseWarehousePos(String singleSelect) {
        model.addAttribute("singleSelect", singleSelect);
        return choose_warehousePos;
    }

    @RequestMapping("complaint")
    public String chooseComplaint(String singleSelect, String consumerName) {
        model.addAttribute("singleSelect", singleSelect);
        return choose_complaint;
    }

    @RequestMapping("salesOrder")
    public String chooseSalesOrder(String singleSelect, String consumerName) {
        model.addAttribute("singleSelect", singleSelect).addAttribute("consumerName", consumerName);
        return choose_salesOrder;
    }

    @RequestMapping("producePlan")
    public String chooseProducePlan(String singleSelect) {
        model.addAttribute("singleSelect", singleSelect);
        return choose_producePlan;
    }

    @RequestMapping("producePlanDetail")
    public String chooseProducePlanDetail(String singleSelect) {
        model.addAttribute("singleSelect", singleSelect);
        return choose_producePlanDetail;
    }

    @RequestMapping("device")
    public String chooseDevice(String singleSelect, String workShop) {
        model.addAttribute("singleSelect", singleSelect).addAttribute("workShop", workShop);
        return choose_Device;
    }

    @RequestMapping("cuser")
    public String chooseUser(String singleSelect, String did) {
        model.addAttribute("singleSelect", singleSelect).addAttribute("did", did);
        return choose_User;
    }

    @RequestMapping("user")
    public String fragmentUser(String singleSelect) {
        model.addAttribute("singleSelect", singleSelect);
        return fragment_User;
    }

    // isShow为是否查看裁剪添加的产品信息
    @RequestMapping("product")
    public String chooseProduct(String singleSelect, String showCode, Integer isShow) {
        model.addAttribute("singleSelect", singleSelect).addAttribute("showCode", showCode);
        if (isShow != null) {
            model.addAttribute("isShow", isShow);
        }
        return choose_product;
    }

    @RequestMapping("consumer")
    public String chooseConsumer(String singleSelect) {
        model.addAttribute("singleSelect", singleSelect);
        return choose_consumer;
    }

    @RequestMapping("tcBom")
    public String chooseTcBom() {
        return choose_tcBom;
    }

    @RequestMapping("packingBom")
    public String choosebcBom() {
        return choose_bcBom;
    }

    @RequestMapping("selectorPrintTemplate")
    public ModelAndView selectorPrintTemplate(String type) {
        return new ModelAndView(selectorPrintTemplateUrl, model.addAttribute("type", type));
    }

    @Journal(name = "挑选打印模版")
    @RequestMapping(value = "selectorBtwFilePrint", method = RequestMethod.GET)
    public ModelAndView selectorBtwFilePrint(Long btwFileId) throws Exception {

        return new ModelAndView(selectorBtwFilePrintUrl, model.addAttribute("btwFileId", btwFileId));
    }


    @RequestMapping("cutTcBomMain")
    public String selectorCutTcBomMain() {
        return selectorCutTcBomMainUrl;
    }

}

