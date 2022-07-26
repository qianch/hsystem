/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.produce.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.planner.produce.entity.PackageTask;
import com.bluebirdme.mes.planner.produce.service.IPackageTaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.ObjectUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.List;

/**
 * 包装任务列表Controller
 *
 * @author 高飞
 * @Date 2017-5-26 10:04:05
 */
@Controller
@RequestMapping("/planner/produce/pack")
@Journal(name = "包装任务列表")
public class PackageTaskController extends BaseController {
    final String addOrEdit = "planner/produce/pack/packageTaskAddOrEdit";
    final String printPage = "planner/produce/pack/packageTaskPrint";
    final String turnbagPage = "printer/tcPrinterBarcode1";
    @Resource
    IPackageTaskService packageTaskService;


    /**
     * 获取包装任务列表
     *
     * @param planDetailId 生产计划ID
     */
    @NoAuth
    @ResponseBody
    @Journal(name = "获取包装任务列表")
    @RequestMapping("list")
    public String getPackageTask(Long planDetailId) {
        return GsonTools.toJson(packageTaskService.findTasks(planDetailId));
    }

    @Journal(name = "添加包装任务列表页面")
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView add(Long producePlanDetailId, Long bcBomId, Boolean print) {
        model.addAttribute("pid", producePlanDetailId);
        if (!ObjectUtils.isNull(bcBomId))
            model.addAttribute("codes", GsonTools.toJson(packageTaskService.findPakcageInfo(bcBomId)));
        if (!ObjectUtils.isNull(producePlanDetailId))
            model.addAttribute("packageTask", GsonTools.toJson(packageTaskService.findTasks(producePlanDetailId)));
        if (print != null && print) {
            return new ModelAndView(printPage, model);
        }
        return new ModelAndView(addOrEdit, model);
    }

    @Journal(name = "翻包打印页面")
    @RequestMapping(method = RequestMethod.GET, value = "turnbag")
    public ModelAndView turnbagView(Long fromSalesOrderDetailId, Long producePlanDetailId, Long cutPlanId, String departmentName, String trugPlanId) {
        model.addAttribute("fromSalesOrderDetailId", fromSalesOrderDetailId);
        model.addAttribute("producePlanDetailId", producePlanDetailId);
        model.addAttribute("cutPlanId", cutPlanId);
        model.addAttribute("departmentName", departmentName);
        model.addAttribute("trugPlanId", trugPlanId);
        return new ModelAndView(turnbagPage, model);
    }

    @ResponseBody
    @Journal(name = "保存包装任务")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@RequestBody List<PackageTask> list) {
        packageTaskService.saveTask(list);
        return ajaxSuccess();
    }
}