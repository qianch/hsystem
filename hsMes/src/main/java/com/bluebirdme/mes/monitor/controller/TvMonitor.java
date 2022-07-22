package com.bluebirdme.mes.monitor.controller;

import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 电视机屏幕
 *
 * @author Goofy
 * @Date 2016年12月13日 下午1:50:23
 */
@Controller
@RequestMapping("/tv")
public class TvMonitor extends BaseController {
    @Resource
    IWeavePlanService wpService;

    /**
     * 电视机展示页面
     */
    private final static String TV_PAGE = "monitor/tv";

    @NoLogin
    @RequestMapping(value = "bz{no}", method = RequestMethod.GET)
    public ModelAndView weavePlanFrame(@PathVariable(value = "no") Integer no) {
        return new ModelAndView("monitor/tvFrame").addObject("no", no);
    }

    @NoLogin
    @RequestMapping(value = "bz{no}/page", method = RequestMethod.GET)
    public ModelAndView weavePlanPage(@PathVariable(value = "no") Integer no) {
        return new ModelAndView(TV_PAGE).addObject("no", no);
    }

    /**
     * 加载编织车间的打包任务
     */
    @NoLogin
    @ResponseBody
    @RequestMapping(value = "bz{no}", method = RequestMethod.POST)
    public String weavePlan(@PathVariable(value = "no") Integer no) {
        List<Map<String, Object>> wps = wpService.ledWeavePlan(no);
        return GsonTools.toJson(wps);
    }
}
