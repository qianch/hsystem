package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.schedule.BaseSchedule;
import com.bluebirdme.mes.core.exception.BusinessException;
import com.bluebirdme.mes.core.schedule.Task;
import com.bluebirdme.mes.platform.entity.Schedule;
import com.bluebirdme.mes.platform.service.IScheduleService;
import com.google.gson.GsonBuilder;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 * @deprecated
 */
@Deprecated
public class ScheduleController extends BaseController {
    @Resource
    IScheduleService scheduleService;

    public ScheduleController() {
    }

    @Journal(name = "进入调度业务页面")
    @RequestMapping(method = {RequestMethod.GET})
    public String get() throws Exception {
        return "platform/schedule";
    }

    @NoAuth
    @ResponseBody
    @RequestMapping({"list"})
    public String list(Filter filter, Page page) throws Exception {
        return (new GsonBuilder()).serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(this.scheduleService.findPageInfo(filter, page));
    }

    @Journal(name = "检查任务调度类")
    @NoAuth
    @ResponseBody
    @RequestMapping({"check"})
    public String check(Schedule schedule) throws ClassNotFoundException, BusinessException {
        Class<?> clazz = Class.forName(schedule.getClazz());
        if (clazz.getSuperclass() != null && clazz.getSuperclass().getSimpleName().equals(BaseSchedule.class.getSimpleName())) {
            Task t = clazz.getAnnotation(Task.class);
            if (t == null) {
                throw new BusinessException("没有在" + clazz.getSimpleName() + "上找到@Task注解");
            } else {
                return "{\"name\":\"" + t.value() + "\"}";
            }
        } else {
            throw new BusinessException(clazz.getSimpleName() + "未继承" + BaseSchedule.class.getSimpleName());
        }
    }

    @Journal(name = "到添加任务页面")
    @RequestMapping(value = {"add"}, method = {RequestMethod.GET})
    public ModelAndView _add(Schedule schedule) {
        return new ModelAndView("platform/scheduleAddOrEdit", model.addAttribute("action", "add"));
    }

    @Journal(name = "添加新的调度任务")
    @ResponseBody
    @RequestMapping(value = {"add"}, method = {RequestMethod.POST})
    public String add(Schedule schedule) throws ClassNotFoundException, SchedulerException, ParseException, InstantiationException, IllegalAccessException, BusinessException {
        schedule.setCreateTime(new Date());
        Map<String, Object> map = new HashMap<>();
        map.put("clazz", schedule.getClazz());
        if (scheduleService.isExist(Schedule.class, map)) {
            throw new BusinessException("重复的调度任务");
        } else {
            schedule.setStatus(0);
            scheduleService.addSchedule(schedule);
            return "{}";
        }
    }

    @Journal(name = "编辑调度任务页面")
    @RequestMapping(value = {"edit"}, method = {RequestMethod.GET})
    public ModelAndView _edit(Schedule schedule) throws Exception {
        schedule = scheduleService.findById(Schedule.class, schedule.getId());
        if (schedule.getStatus() != 3) {
            throw new Exception("请先停止该任务");
        } else {
            return new ModelAndView("platform/scheduleAddOrEdit",
                    model.addAttribute("schedule", schedule)
                            .addAttribute("action", "edit"));
        }
    }

    @Journal(name = "保存编辑的调度任务")
    @ResponseBody
    @RequestMapping(value = {"edit"}, method = {RequestMethod.POST})
    public String edit(Schedule schedule) throws Exception {
        schedule.setCreateTime(new Date());
        Schedule s = scheduleService.findById(Schedule.class, schedule.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("clazz", schedule.getClazz());
        if (!schedule.getClazz().equalsIgnoreCase(s.getClazz()) && scheduleService.isExist(Schedule.class, map)) {
            throw new BusinessException("重复的调度任务");
        } else {
            schedule.setStatus(s.getStatus());
            this.scheduleService.update2(schedule);
            return "{}";
        }
    }

    @Journal(name = "暂停任务")
    @ResponseBody
    @RequestMapping(value = {"pause"}, method = {RequestMethod.POST})
    public String pause(Schedule schedule) throws SchedulerException, BusinessException {
        schedule = scheduleService.findById(Schedule.class, schedule.getId());
        if (schedule.getStatus() == 3) {
            throw new BusinessException("该任务已停止，无法暂停");
        } else {
            this.scheduleService.pauseSchedule(schedule);
            return "{}";
        }
    }

    @Journal(name = "启动任务")
    @ResponseBody
    @RequestMapping(value = {"start"}, method = {RequestMethod.POST})
    public String start(Schedule schedule) throws SchedulerException, ClassNotFoundException {
        schedule = scheduleService.findById(Schedule.class, schedule.getId());
        scheduleService.startSchedule(schedule);
        return "{}";
    }

    @Journal(name = "删除任务")
    @ResponseBody
    @RequestMapping(value = {"delete"}, method = {RequestMethod.POST})
    public String delete(Schedule schedule) throws Exception {
        schedule = scheduleService.findById(Schedule.class, schedule.getId());
        if (schedule.getStatus() != 3) {
            throw new Exception("请先停止该任务");
        } else {
            scheduleService.deleteSchedule(schedule, true);
            return "{}";
        }
    }

    @Journal(name = "停止任务")
    @ResponseBody
    @RequestMapping(value = {"stop"}, method = {RequestMethod.POST})
    public String stop(Schedule schedule) throws Exception {
        schedule = this.scheduleService.findById(Schedule.class, schedule.getId());
        if (schedule.getStatus() == 3) {
            throw new Exception("该任务已停止");
        } else {
            this.scheduleService.deleteSchedule(schedule, false);
            return "{}";
        }
    }
}
