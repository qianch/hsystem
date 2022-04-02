package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.platform.entity.ScheduleInstance;
import com.bluebirdme.mes.platform.service.IScheduleInstanceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Controller
@RequestMapping({"/scheduleInstance"})
@Journal(name = "调度实例")
public class ScheduleInstanceController extends BaseController {
    private static final  String INDEX = "platform/scheduleInstance";
    private static final  String ADD_OR_EDIT = "platform/scheduleInstanceAddOrEdit";
    private static final  String CRON = "platform/cron";
    @Resource
    IScheduleInstanceService scheduleInstanceService;

    public ScheduleInstanceController() {
    }

    @Journal(name = "首页")
    @RequestMapping(method = {RequestMethod.GET})
    public String index() {
        return "platform/scheduleInstance";
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取调度实例列表信息")
    @RequestMapping({"list"})
    public String getScheduleInstance(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(this.scheduleInstanceService.findPageInfo(filter, page));
    }

    @Journal(name = "添加调度实例页面")
    @RequestMapping(value = {"add"}, method = {RequestMethod.GET})
    public ModelAndView _add(ScheduleInstance scheduleInstance) {
        return new ModelAndView("platform/scheduleInstanceAddOrEdit", model.addAttribute("scheduleInstance", scheduleInstance));
    }

    @ResponseBody
    @Journal(name = "保存调度实例", logType = LogType.DB)
    @RequestMapping(value = {"add"}, method = {RequestMethod.POST})
    @Valid
    public String add(ScheduleInstance scheduleInstance) throws Exception {
        scheduleInstance.setStatus("STOP");
        scheduleInstanceService.save(new Object[]{scheduleInstance});
        return GsonTools.toJson(scheduleInstance);
    }

    @Journal(name = "编辑调度实例页面")
    @RequestMapping(value = {"edit"}, method = {RequestMethod.GET})
    public ModelAndView _edit(ScheduleInstance scheduleInstance) {
        scheduleInstance = scheduleInstanceService.findById(ScheduleInstance.class, scheduleInstance.getId());
        return new ModelAndView("platform/scheduleInstanceAddOrEdit",
                model.addAttribute("scheduleInstance", scheduleInstance));
    }

    @ResponseBody
    @Journal(name = "编辑调度实例", logType = LogType.DB)
    @RequestMapping(value = {"edit"}, method = {RequestMethod.POST})
    @Valid
    public String edit(ScheduleInstance scheduleInstance) throws Exception {
        scheduleInstanceService.update(new Object[]{scheduleInstance});
        return GsonTools.toJson(scheduleInstance);
    }

    @ResponseBody
    @Journal(name = "删除调度实例", logType = LogType.DB)
    @RequestMapping(value = {"delete"}, method = {RequestMethod.POST})
    public String edit(String ids) throws Exception {
        String[] _ids = ids.split(",");
        long[] __ids = new long[_ids.length];

        for (int i = 0; i < _ids.length; ++i) {
            __ids[i] = Long.parseLong(_ids[i]);
        }
        scheduleInstanceService.delete(ScheduleInstance.class, new Serializable[]{__ids});
        return "{}";
    }

    @ResponseBody
    @Journal(name = "启动调度实例", logType = LogType.DB)
    @RequestMapping(value = {"start"}, method = {RequestMethod.POST})
    public String start(ScheduleInstance inst) throws Exception {
        scheduleInstanceService.start(inst);
        return "{}";
    }

    @ResponseBody
    @Journal(name = "停止调度实例", logType = LogType.DB)
    @RequestMapping(value = {"stop"}, method = {RequestMethod.POST})
    public String stop(ScheduleInstance inst) throws Exception {
        scheduleInstanceService.stop(inst);
        return "{}";
    }

    @NoLogin
    @Journal(name = "Quartz在线生成")
    @RequestMapping(value = {"cron"}, method = {RequestMethod.GET})
    public String cron() {
        return "platform/scheduleInstanceCron";
    }
}
