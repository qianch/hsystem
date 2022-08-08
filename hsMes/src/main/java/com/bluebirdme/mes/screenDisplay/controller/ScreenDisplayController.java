/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.screenDisplay.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.device.entity.Device;
import com.bluebirdme.mes.screenDisplay.service.IScreenDisplayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大屏展示Controller
 *
 * @author 宋黎明
 * @Date 2016-11-25 11:25:40
 */
@Controller
@RequestMapping("/screen")
@Journal(name = "大屏展示")
public class ScreenDisplayController extends BaseController {
    // 机台大屏展示
    final String machineScreen = "screenDisplay/machineDisplay";
    // 机台大屏展示(车间)
    final String _machineScreen = "screenDisplay/machineScreenDisplay";

    @Resource
    IScreenDisplayService screenDisplayService;

    @NoLogin
    @RequestMapping(value = "machineView", method = RequestMethod.GET)
    public ModelAndView machineView(String ip) {
        Map<String, Object> map = new HashMap<>();
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        map.put("machineScreenIp", ip);
        List<Device> device = screenDisplayService.findListByMap(Device.class, map);
        if (device.size() == 0) {
            return new ModelAndView(machineScreen);
        }
        return new ModelAndView(machineScreen, model.addAttribute("device", device.get(0)));
    }


    @NoLogin
    @RequestMapping(value = "machineViewInfo", method = RequestMethod.POST)
    @ResponseBody
    public String innerInfo(String ip) throws Exception {
        if (StringUtils.isBlank(ip)) return GsonTools.toJson(new ArrayList<Map<String, Object>>());
        List<Map<String, Object>> machineDisplay = screenDisplayService.findProductInfo(ip);
        for (Map<String, Object> stringObjectMap : machineDisplay) {
            if (stringObjectMap.get("TOTALROLLCOUNT") == null && stringObjectMap.get("REQUIREMENTCOUNT") == null) {
                return ajaxError("销售订单为:" + stringObjectMap.get("SALESORDERCODE") + "该条记录的总卷数或总重量为空，请联系管理员！");
            }
        }
        return GsonTools.toJson(machineDisplay);
    }

    @NoLogin
    @RequestMapping(value = "machineProduceNum", method = RequestMethod.POST)
    @ResponseBody
    public String innerProduceNum(Long productId, Long deviceId) throws Exception {
        return GsonTools.toJson(screenDisplayService.findProduceNum(productId, deviceId));
    }

    @NoLogin
    @Journal(name = "机台ipCombobox")
    @RequestMapping(value = "combobox", method = RequestMethod.POST)
    @ResponseBody
    public String combobox() throws Exception {
        return GsonTools.toJson(screenDisplayService.initCombox());
    }

    @NoLogin
    @RequestMapping(value = "machineScreenView", method = RequestMethod.GET)
    public ModelAndView _machineView() {
        Map<String, Object> map = new HashMap<>();
        String ip = request.getRemoteAddr();
        map.put("machineScreenIp", ip);
        List<Device> device = screenDisplayService.findListByMap(Device.class, map);
        if (device.size() == 0) {
            return new ModelAndView(_machineScreen);
        }
        return new ModelAndView(_machineScreen, model.addAttribute("device", device.get(0)));
    }


    @NoLogin
    @RequestMapping(value = "machineScreenViewInfo", method = RequestMethod.POST)
    @ResponseBody
    public String _innerInfo(String ip) throws Exception {
        if (StringUtils.isBlank(ip)) return GsonTools.toJson(new ArrayList<Map<String, Object>>());
        //String ip=request.getRemoteAddr();
        List<Map<String, Object>> machineDisplay = screenDisplayService.findProductInfo(ip);
        for (int i = 0; i < machineDisplay.size(); i++) {
            if (machineDisplay.get(i).get("TOTALROLLCOUNT") == null && machineDisplay.get(i).get("REQUIREMENTCOUNT") == null) {
                return ajaxError("销售订单为:" + machineDisplay.get(i).get("SALESORDERCODE") + " 该条记录的总卷数或总重量为空，请联系管理员！");
            }
        }
        return GsonTools.toJson(machineDisplay);
    }

    @NoLogin
    @RequestMapping(value = "machineScreenViewFirstInfo", method = RequestMethod.POST)
    @ResponseBody
    public String _innerFirstInfo(String ip) throws Exception {
        if (StringUtils.isBlank(ip)) return GsonTools.toJson(new ArrayList<Map<String, Object>>());
        //String ip=request.getRemoteAddr();
        List<Map<String, Object>> machineDisplay = screenDisplayService.findFirstProductInfo(ip);
        for (int i = 0; i < machineDisplay.size(); i++) {
            if (machineDisplay.get(i).get("TOTALROLLCOUNT") == null && machineDisplay.get(i).get("REQUIREMENTCOUNT") == null) {
                return ajaxError("销售订单为:" + machineDisplay.get(i).get("SALESORDERCODE") + " 该条记录的总卷数或总重量为空，请联系管理员！");
            }
        }
        return GsonTools.toJson(machineDisplay);
    }

    @NoLogin
    @RequestMapping(value = "machineScreenProduceNum", method = RequestMethod.POST)
    @ResponseBody
    public String _innerProduceNum(Long productId, Long deviceId) throws Exception {
        return GsonTools.toJson(screenDisplayService.findProduceNum(productId, deviceId));
    }


}