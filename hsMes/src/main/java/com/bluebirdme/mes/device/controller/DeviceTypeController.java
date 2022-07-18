/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.device.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.device.entity.Device;
import com.bluebirdme.mes.device.entity.DeviceType;
import com.bluebirdme.mes.device.service.IDeviceTypeService;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备类别Controller
 *
 * @author 宋黎明
 * @Date 2016-9-28 11:24:46
 */

@Controller
@RequestMapping("/deviceType")
@Journal(name = "设备类别管理")
public class DeviceTypeController extends BaseController {
    /**
     * 设备类别管理页面
     */
    final String index = "device/deviceType/deviceType";
    final String addOrEdit = "device/deviceType/deviceTypeAddOrEdit";

    @Resource
    IDeviceTypeService deviceTypeService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String deviceType() {
        return index;
    }

    @NoAuth
    @Journal(name = "查询设备类别数据")
    @RequestMapping(value = "list")
    @ResponseBody
    public String list(Filter filter, Page page) throws Exception {
        return new GsonBuilder().serializeNulls().create().toJson(deviceTypeService.findPageInfo(filter, page));
    }

    @Journal(name = "进入设备类别添加页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(DeviceType deviceType) {
        return new ModelAndView(addOrEdit, model.addAttribute("deviceType", deviceType));
    }

    @ResponseBody
    @Journal(name = "保存设备类别数据")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(DeviceType deviceType) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("categoryName", deviceType.getCategoryName());
        if (!deviceTypeService.isExist(DeviceType.class, map)) {
            deviceTypeService.save(deviceType);
        } else {
            return ajaxError("该名称已存在！");
        }
        return GsonTools.toJson(deviceType);
    }

    @Journal(name = "进入设备类别编辑页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(DeviceType deviceType) {
        deviceType = deviceTypeService.findById(DeviceType.class, deviceType.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("deviceType", deviceType));
    }

    @ResponseBody
    @Journal(name = "保存编辑后的设备类别数据")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(DeviceType deviceType) {
        deviceTypeService.update(deviceType);
        return GsonTools.toJson(deviceType);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "批量更新设备类型层级关系")
    @RequestMapping(value = "batchUpdateDeviceTypeLevel", method = RequestMethod.POST)
    public String batchUpdateDepartmentLevel(@RequestBody List<DeviceType> list) throws Exception {
        deviceTypeService.batchUpdateDeviceTypeLevel(list);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "删除设备类别")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String delete(String ids) {
        String[] id = ids.split(",");
        List<Device> device = null;
        for (String s : id) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("deviceCatetoryId", Long.valueOf(s));
            device = deviceTypeService.findListByMap(Device.class, map);
            if (!device.isEmpty()) {
                return ajaxError("该类别下有设备信息，请先删除设备信息后在删除该类别！");
            }
        }
        deviceTypeService.delete(ids);
        return Constant.AJAX_SUCCESS;
    }
}
