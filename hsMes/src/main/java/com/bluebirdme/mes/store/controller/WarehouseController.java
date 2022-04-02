/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.platform.entity.Dict;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.StringUtils;
import org.xdemo.superutil.thirdparty.PinYinUtils;
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
import com.bluebirdme.mes.store.entity.Warehouse;
import com.bluebirdme.mes.store.service.IWarehouseService;

/**
 * 仓库管理Controller
 * @author 肖文彬
 * @Date 2016-9-29 15:45:32
 */
@Controller
@RequestMapping(value={"/warehouse","/mobile/warehouse"})
@Journal(name="仓库管理")
public class WarehouseController extends BaseController {

	// 仓库管理页面
	final String index = "store/warehouse/warehouse";
	final String addOrEdit="store/warehouse/warehouseAddOrEdit";

	@Resource IWarehouseService warehouseService;

	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="获取仓库管理列表信息")
	@RequestMapping("list")
	public String getWarehouse(Filter filter, Page page) throws Exception{
		return GsonTools.toJson(warehouseService.findPageInfo(filter, page));
	}
	

	@Journal(name="添加仓库管理页面")
	@RequestMapping(value="add",method=RequestMethod.GET)
	public ModelAndView _add(Warehouse warehouse){
		return new ModelAndView(addOrEdit,model.addAttribute("warehouse", warehouse));
	}
	
	@ResponseBody
	@Journal(name="保存仓库管理",logType=LogType.DB)
	@RequestMapping(value="add",method=RequestMethod.POST)
	@Valid
	public String add(Warehouse warehouse) throws Exception{
		if(StringUtils.isBlank(warehouse.getWarehouseCode())){
			warehouse.setWarehouseCode(PinYinUtils.getPinYinHeadChar(warehouse.getWarehouseName()).toUpperCase());
		}
		Map<String, Object> map=new HashMap();
		map.put("WAREHOUSECODE",warehouse.getWarehouseCode());
		if (warehouseService.has(Warehouse.class, map)){
			return ajaxError("仓库编码重复");
		}
		map.clear();
		map.put("WAREHOUSENAME",warehouse.getWarehouseName());
		if (warehouseService.has(Warehouse.class, map)){
			return ajaxError("仓库名称重复");
		}
		warehouseService.save(warehouse);
		return GsonTools.toJson("保存成功");
		//return GsonTools.toJson(warehouse);
	}
	
	@Journal(name="编辑仓库管理页面")
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public ModelAndView _edit(Warehouse warehouse){
		warehouse=warehouseService.findById(Warehouse.class, warehouse.getId());
		return new ModelAndView(addOrEdit, model.addAttribute("warehouse", warehouse));
	}
	
	@ResponseBody
	@Journal(name="编辑仓库管理",logType=LogType.DB)
	@RequestMapping(value="edit",method=RequestMethod.POST)
	@Valid
	public String edit(Warehouse warehouse) throws Exception{
		Map<String, Object> map=new HashMap();
		map.put("WAREHOUSECODE",warehouse.getWarehouseCode());
		if (warehouseService.has(Warehouse.class, map,warehouse.getId())){
			return ajaxError("仓库编码重复");
		}
		map.clear();
		map.put("WAREHOUSENAME",warehouse.getWarehouseName());
		if (warehouseService.has(Warehouse.class, map,warehouse.getId())){
			return ajaxError("仓库名称重复");
		}
		warehouseService.update(warehouse);
		return GsonTools.toJson("保存成功");
		//return GsonTools.toJson(warehouse);
	}

	@ResponseBody
	@Journal(name="删除仓库管理",logType=LogType.DB)
	@RequestMapping(value="delete",method=RequestMethod.POST)
	public String edit(String ids) throws Exception{
		warehouseService.delete(ids);
		return Constant.AJAX_SUCCESS;
	}

	@ResponseBody
	@Journal(name="作废仓库管理",logType=LogType.DB)
	@RequestMapping(value="updateS",method=RequestMethod.POST)
	public String old(String ids) throws Exception{
		warehouseService.updateS(ids);
		return Constant.AJAX_SUCCESS;
	}

	@ResponseBody
	@Journal(name="查询库房信息")
	@RequestMapping(value="combobox",method=RequestMethod.POST)
	public String combobox() {
		return GsonTools.toJson(warehouseService.warehouse());
	}
	
	@NoLogin
	@ResponseBody
	@Journal(name = "获取仓库的select选项")
	@RequestMapping(value = "getWarehouseInfo", method = RequestMethod.POST)
	public String getWarehouseInfo(String type) {
		List<Warehouse> plist = warehouseService.findAll(Warehouse.class);
		List<HashMap<String, String>> outInfo = new ArrayList<HashMap<String, String>>();
		for (Warehouse p : plist) {
			HashMap<String, String> map = new HashMap<String, String>();
			if (p.getIsCancellation() !=  null && p.getIsCancellation()==1){
				continue;
			} else if ("ycl".equals(type)) {
				if (p.getWarehouseCode().startsWith(type)) {
					map.put("warehouseCode", p.getWarehouseCode());
					map.put("warehouseName", p.getWarehouseName());
					map.put("originWarehouseCode", p.getWarehouseCode());
					map.put("ORIGINWAREHOUSENAME", p.getWarehouseName());
					outInfo.add(map);
				}
			} else if ("cp".equals(type)) {
				if (p.getWarehouseCode().startsWith(type)
						|| p.getWarehouseCode().startsWith("cjp")|| p.getWarehouseCode().startsWith("cjd")
						|| p.getWarehouseCode().startsWith("bz")|| p.getWarehouseCode().startsWith("cj2p")) {
					map.put("warehouseCode", p.getWarehouseCode());
					map.put("warehouseName", p.getWarehouseName());
					map.put("originWarehouseCode", p.getWarehouseCode());
					map.put("originWarehouseName", p.getWarehouseName());
					outInfo.add(map);
				}
			}else if("pb".equals(type)){
				if(p.getWareType() != null && p.getWareType().startsWith(type)){
					map.put("warehouseCode", p.getWarehouseCode());
					map.put("warehouseName", p.getWarehouseName());
					map.put("originWarehouseCode", p.getWarehouseCode());
					map.put("originWarehouseName", p.getWarehouseName());
					outInfo.add(map);
				}
			} else {
				map.put("warehouseCode", p.getWarehouseCode());
				map.put("warehouseName", p.getWarehouseName());
				map.put("originWarehouseCode", p.getWarehouseCode());
				map.put("originWarehouseName", p.getWarehouseName());
				outInfo.add(map);
			}
		}
		return GsonTools.toJson(outInfo);
	}

	@NoLogin
	@ResponseBody
	@Journal(name = "获取仓库的select选项")
	@RequestMapping(value = "queryWarehousebyType", method = RequestMethod.GET)
	@NoAuth
	public String queryWarehousebyType(String waretype) throws Exception {
		List<Map<String, Object>> list = warehouseService.queryWarehousebyType(waretype);
		List<Map<String, Object>> combobox = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		for (Map<String, Object> m : list) {
			map = new HashMap<String, Object>();
			map.put("t", m.get("WAREHOUSENAME"));
			map.put("v", m.get("WAREHOUSECODE"));
			combobox.add(map);
		}
		return GsonTools.toJson(combobox);
	}

	@NoLogin
	@ResponseBody
	@Journal(name = "获取仓库类型的select选项")
	@RequestMapping(value = "queryWarehouseType", method = RequestMethod.GET)
	@NoAuth
	public String queryWarehouseType(String rootcode) throws Exception {
		Map<String,Object> dictMap = new HashMap<>();
		dictMap.put("rootCode",rootcode);
		List<Dict> dicts = warehouseService.findListByMap(Dict.class, dictMap);
		List<Map<String, Object>> combobox = new ArrayList<Map<String, Object>>();
		for (Dict d : dicts){
			if(!d.getCode().startsWith("cp")){
				continue;
			}
			Map<String, Object> map = new HashMap();
			map.put("t", d.getName_zh_CN());
			map.put("v", d.getCode());
			combobox.add(map);
		}
		return GsonTools.toJson(combobox);
	}
}