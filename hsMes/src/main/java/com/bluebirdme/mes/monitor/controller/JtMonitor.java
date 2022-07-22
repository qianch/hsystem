package com.bluebirdme.mes.monitor.controller;

import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.device.entity.Device;
import com.bluebirdme.mes.device.service.IDeviceService;
import com.bluebirdme.mes.utils.HttpUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 机台屏幕
 *
 * @author Goofy
 * @Date 2016年12月13日 下午1:50:23
 */
@Controller
@RequestMapping("/jt")
public class JtMonitor extends BaseController {
    @Resource
    IDeviceService deviceService;

    @NoLogin
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView jtFrame() {
        return new ModelAndView("monitor/jtFrame");
    }

    @NoLogin
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public ModelAndView jtView() throws java.lang.Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("machineScreenIp", HttpUtils.getIpAddr(request));
        Device device = deviceService.findUniqueByMap(Device.class, map);
        if (device == null) {
            throw new java.lang.Exception("找不到IP为" + remoteIp + "的机台");
        }
        return new ModelAndView("monitor/jt").addObject("jtId", device.getId()).addObject("code", device.getDeviceCode());
    }

    @NoLogin
    @ResponseBody
    @RequestMapping(value = "view/{jtId}", method = RequestMethod.GET)
    public String jtTask(@PathVariable("jtId") Long jtId) {
        return GsonTools.toJson(deviceService.findDevicePlans(jtId));
    }

    @NoLogin
    @ResponseBody
    @RequestMapping(value = "viewComp", method = RequestMethod.GET)
    public String jtTask(String jtId) {
        Long id = Long.parseLong(jtId);
        return GsonTools.toJson(deviceService.findDevicePlans(id));
    }
}
