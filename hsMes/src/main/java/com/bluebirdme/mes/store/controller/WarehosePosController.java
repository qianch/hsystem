/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.controller;

import javax.annotation.Resource;

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
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.store.entity.WarehosePos;
import com.bluebirdme.mes.store.service.IWarehosePosService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库位管理Controller
 * @author 肖文彬
 * @Date 2016-9-29 16:26:04
 */
@Controller
@RequestMapping("/warehosePos")
@Journal(name="库位管理")
public class WarehosePosController extends BaseController {

	// 库位管理页面
	final String index = "store/warehousePos/warehosePos";
	final String addOrEdit="store/warehousePos/warehosePosAddOrEdit";

	@Resource IWarehosePosService warehosePosService;

	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}

	@NoAuth
	@ResponseBody
	@Journal(name="获取库位管理列表信息")
	@RequestMapping("list")
	public String getWarehosePos(Filter filter, Page page) throws Exception{
		return GsonTools.toJson(warehosePosService.findPageInfo(filter, page));
	}


	@Journal(name="添加库位管理页面")
	@RequestMapping(value="add",method=RequestMethod.GET)
	public ModelAndView _add(WarehosePos warehosePos){
		return new ModelAndView(addOrEdit,model.addAttribute("warehosePos", warehosePos));
	}

	@ResponseBody
	@Journal(name="保存库位管理",logType=LogType.DB)
	@RequestMapping(value="add",method=RequestMethod.POST)
	@Valid
	public String add(WarehosePos warehosePos) throws Exception{
		if(StringUtils.isBlank(warehosePos.getWarehousePosCode())){
			warehosePos.setWarehousePosCode(PinYinUtils.getPinYinHeadChar(warehosePos.getWarehousePosName()).toUpperCase());
		}
		Map<String, Object> map=new HashMap();
		String WAREHOUSEPOSCODE=warehosePos.getWarehousePosCode();
		String WAREHOUSEPOSNAME=warehosePos.getWarehousePosName();
		map.put("WAREHOUSEID",warehosePos.getWarehouseId());
		List<WarehosePos> warehosePosList = warehosePosService.findListByMap(WarehosePos.class, map);
		for (WarehosePos p:warehosePosList) {
			if(p.getWarehousePosCode().equals(WAREHOUSEPOSCODE)){
				return ajaxError("同一个仓库下，库位编码重复");
			}
			if (p.getWarehousePosName().equals(WAREHOUSEPOSNAME)){
				return ajaxError("同一个仓库下，库位名称重复");
			}
		}
		warehosePosService.save(warehosePos);
		return GsonTools.toJson("保存成功");
		//return GsonTools.toJson(warehosePos);
	}

	@Journal(name="编辑库位管理页面")
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public ModelAndView _edit(WarehosePos warehosePos){
		warehosePos=warehosePosService.findById(WarehosePos.class, warehosePos.getId());
		return new ModelAndView(addOrEdit, model.addAttribute("warehosePos", warehosePos));
	}

	@ResponseBody
	@Journal(name="编辑库位管理",logType=LogType.DB)
	@RequestMapping(value="edit",method=RequestMethod.POST)
	@Valid
	public String edit(WarehosePos warehosePos) throws Exception{
        Map<String, Object> map=new HashMap();
        map.put("WAREHOUSEID",warehosePos.getWarehouseId());
        map.put("WAREHOUSEPOSCODE",warehosePos.getWarehousePosCode());
        if (warehosePosService.has(WarehosePos.class, map,warehosePos.getId())){
            return ajaxError("同一个仓库下，库位编码重复");
        }
        map.clear();
        map.put("WAREHOUSEID",warehosePos.getWarehouseId());
        map.put("WAREHOUSEPOSNAME",warehosePos.getWarehousePosName());
        if (warehosePosService.has(WarehosePos.class, map,warehosePos.getId())){
            return ajaxError("同一个仓库下，库位名称重复");
        }
		warehosePosService.update(warehosePos);
		return GsonTools.toJson("保存成功");
		//return GsonTools.toJson(warehosePos);
	}

	@ResponseBody
	@Journal(name="删除库位管理",logType=LogType.DB)
	@RequestMapping(value="delete",method=RequestMethod.POST)
	public String edit(String ids) throws Exception{
		warehosePosService.delete(ids);
		return Constant.AJAX_SUCCESS;
	}
	@ResponseBody
	@Journal(name="作废库位管理",logType=LogType.DB)
	@RequestMapping(value="updateS",method=RequestMethod.POST)
	public String old(String ids) throws Exception{
		warehosePosService.updateS(ids);
		return Constant.AJAX_SUCCESS;
	}

}