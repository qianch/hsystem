/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsDetail;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsFinishedWeightEmbryoCloth;
import com.bluebirdme.mes.baseInfo.service.ITcBomVersionPartsFinishedWeightEmbryoClothService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;

/**
 * 部件成品重量胚布信息Controller
 * @author 徐秦冬
 * @Date 2017-11-27 18:57:36
 */
@Controller
@RequestMapping("/bom/tcBomVersionPartsFinishedWeightEmbryoCloth")
@Journal(name="部件成品重量胚布信息")
public class TcBomVersionPartsFinishedWeightEmbryoClothController extends BaseController {

	// 部件成品重量胚布信息页面
	final String index = "baseInfo/tcBomVersionPartsFinishedWeightEmbryoCloth";
	final String addOrEdit="baseInfo/tcBomVersionPartsFinishedWeightEmbryoClothAddOrEdit";

	@Resource ITcBomVersionPartsFinishedWeightEmbryoClothService tcBomVersionPartsFinishedWeightEmbryoClothService;

	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="获取部件成品重量胚布信息列表信息")
	@RequestMapping("list")
	public String getTcBomVersionPartsDetail(Filter filter, Page page) throws Exception{
		return GsonTools.toJson(tcBomVersionPartsFinishedWeightEmbryoClothService.findPageInfo(filter, page));
	}

	

	@Journal(name="添加部件成品重量胚布信息页面")
	@RequestMapping(value="add",method=RequestMethod.GET)
	public ModelAndView _add(TcBomVersionPartsFinishedWeightEmbryoCloth tcBomVersionPartsFinishedWeightEmbryoCloth){
		return new ModelAndView(addOrEdit,model.addAttribute("tcBomVersionPartsFinishedWeightEmbryoCloth", tcBomVersionPartsFinishedWeightEmbryoCloth));
	}
	
	@ResponseBody
	@Journal(name="保存部件成品重量胚布信息",logType=LogType.DB)
	@RequestMapping(value="add",method=RequestMethod.POST)
	@Valid
	public String add(TcBomVersionPartsFinishedWeightEmbryoCloth tcBomVersionPartsFinishedWeightEmbryoCloth) throws Exception{
		tcBomVersionPartsFinishedWeightEmbryoClothService.save(tcBomVersionPartsFinishedWeightEmbryoCloth);
		return GsonTools.toJson(tcBomVersionPartsFinishedWeightEmbryoCloth);
	}
	
	@Journal(name="编辑部件成品重量胚布信息页面")
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public ModelAndView _edit(TcBomVersionPartsFinishedWeightEmbryoCloth tcBomVersionPartsFinishedWeightEmbryoCloth){
		tcBomVersionPartsFinishedWeightEmbryoCloth=tcBomVersionPartsFinishedWeightEmbryoClothService.findById(TcBomVersionPartsFinishedWeightEmbryoCloth.class, tcBomVersionPartsFinishedWeightEmbryoCloth.getId());
		return new ModelAndView(addOrEdit, model.addAttribute("tcBomVersionPartsFinishedWeightEmbryoCloth", tcBomVersionPartsFinishedWeightEmbryoCloth));
	}
	
	@ResponseBody
	@Journal(name="编辑部件成品重量胚布信息",logType=LogType.DB)
	@RequestMapping(value="edit",method=RequestMethod.POST)
	@Valid
	public String edit(TcBomVersionPartsFinishedWeightEmbryoCloth tcBomVersionPartsFinishedWeightEmbryoCloth) throws Exception{
		tcBomVersionPartsFinishedWeightEmbryoClothService.update(tcBomVersionPartsFinishedWeightEmbryoCloth);
		return GsonTools.toJson(tcBomVersionPartsFinishedWeightEmbryoCloth);
	}

	@ResponseBody
	@Journal(name="删除部件成品重量胚布信息",logType=LogType.DB)
	@RequestMapping(value="delete",method=RequestMethod.POST)
	public String edit(String ids) throws Exception{
		if(ids.length()>0){
		String id[] = ids.split(",");
			TcBomVersionPartsFinishedWeightEmbryoCloth tcBomVersionPartsFinishedWeightEmbryoCloth =tcBomVersionPartsFinishedWeightEmbryoClothService.findById(TcBomVersionPartsFinishedWeightEmbryoCloth.class, Long.parseLong(id[0]));
			TcBomVersionParts tcBomVersionParts = tcBomVersionPartsFinishedWeightEmbryoClothService
					.findById(TcBomVersionParts.class, tcBomVersionPartsFinishedWeightEmbryoCloth.getTcProcBomPartsId());
			TcBomVersion tcBomVersion = tcBomVersionPartsFinishedWeightEmbryoClothService.findById(TcBomVersion.class,
					tcBomVersionParts.getTcProcBomVersoinId());
			if (tcBomVersion.getAuditState() > 0) {
				return ajaxError("不能修改审核中或已通过的数据");
			}
		}
		
		tcBomVersionPartsFinishedWeightEmbryoClothService.delete(ids);
		return Constant.AJAX_SUCCESS;
	}

	@NoAuth
	@ResponseBody
	@Journal(name="获取部件成品重量胚布信息列表信息")
	@RequestMapping("mirrorList")
	public String getTcBomVersionPartsDetailMirror(Filter filter, Page page) throws Exception{
		return GsonTools.toJson(tcBomVersionPartsFinishedWeightEmbryoClothService.findPageInfo1(filter, page));
	}


}