package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.exception.BusinessException;
import com.bluebirdme.mes.core.schedule.Task;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.platform.entity.ScheduleTemplate;
import com.bluebirdme.mes.platform.service.IScheduleTemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Controller
@RequestMapping("/scheduleTemplate")
@Journal(name = "任务调度模板")
public class ScheduleTemplateController extends BaseController {
    /**
     * 任务调度模板页面
     */
    private static final  String INDEX = "platform/scheduleTemplate";
    private static final  String addOrEdit = "platform/scheduleTemplateAddOrEdit";

    @Resource
    IScheduleTemplateService scheduleTemplateService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return INDEX;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取任务调度模板列表信息")
    @RequestMapping("list")
    public String getScheduleTemplate(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(scheduleTemplateService.findPageInfo(filter, page));
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取调度实例列表信息")
    @RequestMapping("combo")
    public String combo(Filter filter, Page page) throws Exception {
        page.setAll(1);
        return GsonTools.toJson(scheduleTemplateService.findPageInfo(filter, page).get("rows"));
    }


    @Journal(name = "添加任务调度模板页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(ScheduleTemplate scheduleTemplate) {
        return new ModelAndView(addOrEdit, model.addAttribute("scheduleTemplate", scheduleTemplate));
    }

    @ResponseBody
    @Journal(name = "添加任务调度模板", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(ScheduleTemplate scheduleTemplate) throws Exception {
        scheduleTemplate.setCreateTime(new Date());
        scheduleTemplateService.save(scheduleTemplate);
        return GsonTools.toJson(scheduleTemplate);
    }

    @Journal(name = "编辑任务调度模板页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(ScheduleTemplate scheduleTemplate) {
        scheduleTemplate = scheduleTemplateService.findById(ScheduleTemplate.class, scheduleTemplate.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("scheduleTemplate", scheduleTemplate));
    }

    @ResponseBody
    @Journal(name = "编辑任务调度模板", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(ScheduleTemplate scheduleTemplate) throws Exception {
        scheduleTemplateService.update(scheduleTemplate);
        return GsonTools.toJson(scheduleTemplate);
    }

    @ResponseBody
    @Journal(name = "删除任务调度模板", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        String[] sId = ids.split(",");
        long[] lId = new long[sId.length];
        for (int i = 0; i < sId.length; i++) {
            lId[i] = Long.parseLong(sId[i]);
        }
        scheduleTemplateService.delete(ScheduleTemplate.class, lId);
        return Constant.AJAX_SUCCESS;
    }

    @Journal(name = "检查任务调度类")
    @ResponseBody
    @RequestMapping("check")
    public String check(ScheduleTemplate schedule) throws ClassNotFoundException, BusinessException {
        Class<?> clazz = Class.forName(schedule.getClazz());
        Task t = clazz.getAnnotation(Task.class);
        if (t == null) {
            throw new BusinessException("没有在" + clazz.getSimpleName() + "上找到@Task注解");
        }
        return "{\"name\":\"" + t.value() + "\"}";
    }
}
