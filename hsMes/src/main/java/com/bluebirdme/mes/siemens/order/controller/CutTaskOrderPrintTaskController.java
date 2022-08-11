/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.order.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrderPrintTask;
import com.bluebirdme.mes.siemens.order.service.ICutTaskOrderPrintTaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;

/**
 * 西门子裁剪车间机台打印任务Controller
 *
 * @author 高飞
 * @Date 2019-4-15 18:44:42
 */
@Controller
@RequestMapping("/cutTaskOrderPrintTask")
@Journal(name = "西门子裁剪车间机台打印任务")
public class CutTaskOrderPrintTaskController extends BaseController {
    /**
     * 西门子裁剪车间机台打印任务页面
     */
    final String index = "order/cutTaskOrderPrintTask";
    final String addOrEdit = "order/cutTaskOrderPrintTaskAddOrEdit";

    @Resource
    ICutTaskOrderPrintTaskService cutTaskOrderPrintTaskService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取西门子裁剪车间机台打印任务列表信息")
    @RequestMapping("list")
    public String getCutTaskOrderPrintTask(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(cutTaskOrderPrintTaskService.findPageInfo(filter, page));
    }


    @Journal(name = "添加西门子裁剪车间机台打印任务页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(CutTaskOrderPrintTask cutTaskOrderPrintTask) {
        return new ModelAndView(addOrEdit, model.addAttribute("cutTaskOrderPrintTask", cutTaskOrderPrintTask));
    }

    @ResponseBody
    @Journal(name = "保存西门子裁剪车间机台打印任务", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(CutTaskOrderPrintTask cutTaskOrderPrintTask) throws Exception {
        cutTaskOrderPrintTaskService.save(cutTaskOrderPrintTask);
        return GsonTools.toJson(cutTaskOrderPrintTask);
    }

    @Journal(name = "编辑西门子裁剪车间机台打印任务页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(CutTaskOrderPrintTask cutTaskOrderPrintTask) {
        cutTaskOrderPrintTask = cutTaskOrderPrintTaskService.findById(CutTaskOrderPrintTask.class, cutTaskOrderPrintTask.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("cutTaskOrderPrintTask", cutTaskOrderPrintTask));
    }

    @ResponseBody
    @Journal(name = "编辑西门子裁剪车间机台打印任务", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(CutTaskOrderPrintTask cutTaskOrderPrintTask) throws Exception {
        cutTaskOrderPrintTaskService.update(cutTaskOrderPrintTask);
        return GsonTools.toJson(cutTaskOrderPrintTask);
    }

    @ResponseBody
    @Journal(name = "删除西门子裁剪车间机台打印任务", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        cutTaskOrderPrintTaskService.delete(CutTaskOrderPrintTask.class, ids);
        return Constant.AJAX_SUCCESS;
    }
}