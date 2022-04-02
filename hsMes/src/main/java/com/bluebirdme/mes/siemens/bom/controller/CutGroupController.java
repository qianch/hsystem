/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.bom.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.siemens.bom.entity.CutGroup;
import com.bluebirdme.mes.siemens.bom.service.ICutGroupService;

/**
 * 组别管理Controller
 * @author 高飞
 * @Date 2017-7-25 10:39:08
 */
@Controller
@RequestMapping("/cutGroup")
@Journal(name="组别管理")
public class CutGroupController extends BaseController {

	// 组别管理页面
	final String index = "siemens/group/cutGroup";
	final String addOrEdit="siemens/group/cutGroupAddOrEdit";

	@Resource ICutGroupService cutGroupService;

	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}
	
	@ResponseBody
	@Journal(name="获取组别管理列表信息")
	@RequestMapping("list")
	public String getCutGroup(Filter filter, Page page) throws Exception{
		return GsonTools.toJson(cutGroupService.findPageInfo(filter, page));
	}
	
	@ResponseBody
	@Journal(name="获取组别管理裁剪人员列表信息")
	@RequestMapping("workshop/cut/users")
	public String getCutUsers() throws Exception{
		return GsonTools.toJson(cutGroupService.findCutWorkshopUsers());
	}
	

	@Journal(name="添加组别管理页面")
	@RequestMapping(value="add",method=RequestMethod.GET)
	public ModelAndView _add(CutGroup cutGroup){
		return new ModelAndView(addOrEdit,model.addAttribute("cutGroup", cutGroup));
	}
	
	@ResponseBody
	@Journal(name="保存组别管理",logType=LogType.DB)
	@RequestMapping(value="add",method=RequestMethod.POST)
	@Valid
	public String add(CutGroup cutGroup) throws Exception{
		cutGroup.setCreateUser(session.getAttribute("userName")+"");
		cutGroup.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		cutGroupService.save(cutGroup);
		return GsonTools.toJson(cutGroup);
	}
	
	@Journal(name="编辑组别管理页面")
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public ModelAndView _edit(CutGroup cutGroup){
		cutGroup=cutGroupService.findById(CutGroup.class, cutGroup.getId());
		return new ModelAndView(addOrEdit, model.addAttribute("cutGroup", cutGroup));
	}
	
	@ResponseBody
	@Journal(name="编辑组别管理",logType=LogType.DB)
	@RequestMapping(value="edit",method=RequestMethod.POST)
	@Valid
	public String edit(CutGroup cutGroup) throws Exception{
		cutGroupService.update(cutGroup);
		return GsonTools.toJson(cutGroup);
	}

	@ResponseBody
	@Journal(name="删除组别管理",logType=LogType.DB)
	@RequestMapping(value="delete",method=RequestMethod.POST)
	public String edit(String ids) throws Exception{
		cutGroupService.delete(CutGroup.class,ids);
		return Constant.AJAX_SUCCESS;
	}

}