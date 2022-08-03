/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.scheduleLog.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.scheduleLog.entity.ScheduleLog;
import com.bluebirdme.mes.scheduleLog.service.IScheduleLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;

/**
 * 调度日志Controller
 *
 * @author 徐秦冬
 * @Date 2018-2-8 10:50:23
 */
@Controller
@RequestMapping("/scheduleLog")
@Journal(name = "调度日志")
public class ScheduleLogController extends BaseController {
    /**
     * 调度日志页面
     */
    final String index = "scheduleLog/scheduleLog";
    final String addOrEdit = "scheduleLog/scheduleLogAddOrEdit";

    @Resource
    IScheduleLogService scheduleLogService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取调度日志列表信息")
    @RequestMapping("list")
    public String getScheduleLog(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(scheduleLogService.findPageInfo(filter, page));
    }


    @Journal(name = "添加调度日志页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(ScheduleLog scheduleLog) {
        return new ModelAndView(addOrEdit, model.addAttribute("scheduleLog", scheduleLog));
    }

    @ResponseBody
    @Journal(name = "保存调度日志", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(ScheduleLog scheduleLog) throws Exception {
        scheduleLogService.save(scheduleLog);
        return GsonTools.toJson(scheduleLog);
    }

    @Journal(name = "编辑调度日志页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(ScheduleLog scheduleLog) {
        scheduleLog = scheduleLogService.findById(ScheduleLog.class, scheduleLog.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("scheduleLog", scheduleLog));
    }

    @ResponseBody
    @Journal(name = "编辑调度日志", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(ScheduleLog scheduleLog) throws Exception {
        scheduleLogService.update(scheduleLog);
        return GsonTools.toJson(scheduleLog);
    }

    @ResponseBody
    @Journal(name = "删除调度日志", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        scheduleLogService.delete(ScheduleLog.class, ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @RequestMapping("clear")
    public String clear() {
        scheduleLogService.clearAll();
        return Constant.AJAX_SUCCESS;
    }
}