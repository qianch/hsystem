/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.device.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.device.entity.Device;
import com.bluebirdme.mes.device.service.IDeviceService;

/**
 * 设备信息Controller
 * 
 * @author 宋黎明
 * @Date 2016-9-29 11:46:46
 */
@Controller
@RequestMapping("/device")
@Journal(name = "设备信息")
public class DeviceController extends BaseController {

	// 设备信息页面
	final String index = "device/device/device";
	final String addOrEdit = "device/device/deviceAddOrEdit";

	@Resource
	IDeviceService deviceService;

	@NoLogin
	@ResponseBody
	@RequestMapping(value = "getIp", method = { RequestMethod.GET, RequestMethod.POST })
	public String getIp(HttpServletRequest request) {

		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		
		ip=null==ip?"":ip;
		ip=ip.split(",")[0];
		
		System.out.println("+"+ip);

		return ip;
	}

	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}

	@NoAuth
	@ResponseBody
	@Journal(name = "获取设备信息列表信息")
	@RequestMapping("list")
	public String getDevice(Filter filter, Page page) throws Exception {
		return GsonTools.toJson(deviceService.findPageInfo(filter, page));
	}

	@Journal(name = "添加设备信息页面")
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public ModelAndView _add(Device device) {
		return new ModelAndView(addOrEdit, model.addAttribute("device", device));
	}

	@ResponseBody
	@Journal(name = "保存设备信息", logType = LogType.DB)
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@Valid
	public String add(Device device) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map.put("deviceCode", device.getDeviceCode());
		if (deviceService.isExist(Device.class, map)) {
			return ajaxError("该设备代码已存在！");
		}
		map1.put("machineScreenIp", device.getMachineScreenIp());
		if (deviceService.isExist(Device.class, map1)) {
			return ajaxError("该设备IP已存在！");
		}
		deviceService.save(device);
		return GsonTools.toJson(device);
	}

	@Journal(name = "编辑设备信息页面")
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public ModelAndView _edit(Device device) {
		device = deviceService.findById(Device.class, device.getId());
		return new ModelAndView(addOrEdit, model.addAttribute("device", device));
	}

	@ResponseBody
	@Journal(name = "编辑设备信息", logType = LogType.DB)
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@Valid
	public String edit(Device device) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		Device _device = deviceService.findById(Device.class, device.getId());
		map.put("deviceCode", device.getDeviceCode());
		// 判断产品编码是否唯一性
		if (!_device.getDeviceCode().equals(device.getDeviceCode()) && deviceService.isExist(Device.class, map)) {
			return ajaxError("该设备代码已存在");
		}
		map1.put("machineScreenIp", device.getMachineScreenIp());
		if (!_device.getMachineScreenIp().equals(device.getMachineScreenIp()) && deviceService.isExist(Device.class, map1)) {
			return ajaxError("该设备IP已存在");
		}
		deviceService.update2(device);
		return GsonTools.toJson(device);
	}

	@ResponseBody
	@Journal(name = "删除设备信息", logType = LogType.DB)
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public String edit(String ids) throws Exception {
		String id[] = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			List<Map<String, Object>> list = deviceService.find(id[i]);
			if (list.size() != 0) {
				return ajaxError("删除设备已被使用，无法删除！");
			}
		}
		deviceService.delete(ids);
		return Constant.AJAX_SUCCESS;
	}

}