package com.bluebirdme.mes.produce.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

/**
 * 生产任务单
 *
 * @author Goofy
 * @Date 2016年11月28日 上午12:00:47
 */
@Controller
@RequestMapping("/produce")
public class ProduceController extends BaseController {
    private final static String ORDER = "produce/order/salesOrder";
    private final static String checkProcMaterialDetail = "produce/order/checkProcMaterialDetails";

    @RequestMapping(value = "order", method = RequestMethod.GET)
    private String order() {
        return ORDER;
    }

    @Journal(name = "查看生产物料需求页面")
    @RequestMapping(value = "checkProcMaterialDetail", method = RequestMethod.GET)
    public ModelAndView checkProcMaterialDetail(String ids) {
        return new ModelAndView(checkProcMaterialDetail, model.addAttribute("ids", ids));
    }

    @Journal(name = "查看生产物料需求")
    @RequestMapping(value = "procMaterials", method = RequestMethod.POST)
    public String procMaterials(String ids) {
        //根据销售订单ID遍历需要生产的产品内容，计算生产物料需求
        return GsonTools.toJson("");
    }
}
