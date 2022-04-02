/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.cut.cutSalesOrder.controller;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.cut.cutSalesOrder.entity.CutSalesOrder;
import com.bluebirdme.mes.cut.cutSalesOrder.service.ICutSalesOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;
import javax.annotation.Resource;

/**
 * 派工单订单Controller
 *
 * @author 季晓龙
 * @Date 2020-09-4 9:30:06
 */
@Controller
@RequestMapping("cutSalesOrder")
@Journal(name = "裁剪订单")
public class CutSalesOrderController extends BaseController {

    // 裁剪订单
    final String index = "cut/cutSalesOrder/cutSalesOrder";

    //新增修改页面
    final String addOrEdit = "cut/cutSalesOrder/cutSalesOrderAddOrEdit";


    @Resource
    ICutSalesOrderService cutSalesOrderService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }


    @Journal(name = "添加加载裁剪订单信息")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(CutSalesOrder cutSalesOrder) {
        return new ModelAndView(addOrEdit, model.addAttribute("cutSalesOrder", cutSalesOrder));
    }

    @Journal(name="编辑仓库管理页面")
    @RequestMapping(value="edit",method=RequestMethod.GET)
    public ModelAndView _edit(CutSalesOrder cutSalesOrder){
        cutSalesOrder=cutSalesOrderService.findById(CutSalesOrder.class, cutSalesOrder.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("cutSalesOrder", cutSalesOrder));
    }


    @Journal(name = "加载裁剪订单信息")
    @RequestMapping(value = "cutSalesOrderAddOrEditPage", method = RequestMethod.GET)
    public ModelAndView cutSalesOrderAddOrEditPage(CutSalesOrder cutSalesOrder) throws Exception {
        if (cutSalesOrder.getId() != null) {
            cutSalesOrder = cutSalesOrderService.findById(CutSalesOrder.class, cutSalesOrder.getId());
        }
        return new ModelAndView(addOrEdit, model.addAttribute("cutSalesOrder", cutSalesOrder));
    }

    @NoAuth
    @ResponseBody
    @Journal(name="获取裁剪订单管理列表信息")
    @RequestMapping("list")
    public String getCutSalesOrder(Filter filter, Page page) throws Exception{
        return GsonTools.toJson(cutSalesOrderService.findPageInfo(filter, page));
    }


    @ResponseBody
    @Journal(name = "保存裁剪订单信息", logType = LogType.DB)
    @RequestMapping(value = "saveCutSalesOrder", method = RequestMethod.POST)
    @Valid
    public String saveCutSalesOrder(@RequestBody CutSalesOrder cutSalesOrder) throws Exception {
        String userId = session.getAttribute(Constant.CURRENT_USER_ID).toString();
        String result = cutSalesOrderService.saveCutSalesOrder(cutSalesOrder, userId);
        return GsonTools.toJson(result);
    }

    @ResponseBody
    @Journal(name = "删除裁剪订单", logType = LogType.DB)
    @RequestMapping(value = "doDeleteCutSalesOrder", method = RequestMethod.POST)
    public String doDeleteCutSalesOrder(String ids) throws Exception {
        String result = cutSalesOrderService.doDeleteCutSalesOrder(ids);
        return GsonTools.toJson(result);
    }
}
