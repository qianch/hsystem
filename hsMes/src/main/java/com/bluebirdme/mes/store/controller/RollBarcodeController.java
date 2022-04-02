/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.controller;

import com.bluebirdme.mes.btwManager.entity.BtwFile;
import com.bluebirdme.mes.btwManager.service.IBtwFileService;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.printer.entity.BarCodePrintRecord;
import com.bluebirdme.mes.utils.FilterRules;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bluebirdme.mes.core.annotation.Journal;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.StringUtils;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.exception.BusinessException;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.store.entity.RollBarcode;
import com.bluebirdme.mes.store.service.IRollBarcodeService;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

/**
 * 卷条码Controller
 * @author 徐波
 * @Date 2016-11-14 14:37:30
 */
@Controller
@RequestMapping("/rollBarcode")
@Journal(name="卷条码")
public class RollBarcodeController extends BaseController {

	// 卷条码页面
	final String index = "store/rollBarcode";
	final String addOrEdit="store/rollBarcodeAddOrEdit";

	@Resource IRollBarcodeService rollBarcodeService;

	@Resource
	IBtwFileService btwFileService;

	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="获取卷条码列表信息")
	@RequestMapping("list")
	public String getRollBarcode(String filterRules,Filter filter, Page page) throws Exception{
		if (!StringUtils.isEmpty(filterRules)) {
			JsonParser parser = new JsonParser();
			JsonArray array = parser.parse(filterRules).getAsJsonArray();
			FilterRules rule = null;
			Gson gson = new Gson();
			for (JsonElement obj : array) {
				rule = gson.fromJson(obj, FilterRules.class);
				filter.set(rule.getField(), "like:" + rule.getValue());
			}
		}
		return GsonTools.toJson(rollBarcodeService.findPageInfo(filter, page));
	}
	

	@Journal(name="添加卷条码页面")
	@RequestMapping(value="add",method=RequestMethod.GET)
	public ModelAndView _add(RollBarcode rollBarcode){
		return new ModelAndView(addOrEdit,model.addAttribute("rollBarcode", rollBarcode));
	}
	
	@ResponseBody
	@Journal(name="保存卷条码",logType=LogType.DB)
	@RequestMapping(value="add",method=RequestMethod.POST)
	@Valid
	public String add(RollBarcode rollBarcode) throws Exception{
		rollBarcodeService.save(rollBarcode);
		return GsonTools.toJson(rollBarcode);
	}
	
	@Journal(name="编辑卷条码页面")
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public ModelAndView _edit(RollBarcode rollBarcode){
		rollBarcode=rollBarcodeService.findById(RollBarcode.class, rollBarcode.getId());
		return new ModelAndView(addOrEdit, model.addAttribute("rollBarcode", rollBarcode));
	}
	
	@ResponseBody
	@Journal(name="编辑卷条码",logType=LogType.DB)
	@RequestMapping(value="edit",method=RequestMethod.POST)
	@Valid
	public String edit(RollBarcode rollBarcode) throws Exception{
		rollBarcodeService.update(rollBarcode);
		return GsonTools.toJson(rollBarcode);
	}

	@ResponseBody
	@Journal(name="编辑卷条码",logType=LogType.DB)
	@RequestMapping(value="editBarCode",method=RequestMethod.POST)
	@Valid
	public String editBarCode(RollBarcode rollBarcode) throws Exception{
		rollBarcodeService.update(rollBarcode);
		return GsonTools.toJson(rollBarcode);
	}

	@ResponseBody
	@Journal(name="删除卷条码",logType=LogType.DB)
	@RequestMapping(value="delete",method=RequestMethod.POST)
	public String edit(String ids) throws Exception{
		rollBarcodeService.delete(RollBarcode.class,ids);
		return Constant.AJAX_SUCCESS;
	}

	@ResponseBody
	@Journal(name = "清空条码信息", logType = LogType.DB)
	@RequestMapping(value = "clearRoll", method = RequestMethod.POST)
	public String clearRoll(String ids) throws Exception {
		rollBarcodeService.clearRoll(ids);
		return Constant.AJAX_SUCCESS;
	}


	@ResponseBody
	@Journal(name="查询打印信息",logType=LogType.DB)
	@RequestMapping(value="FindPrints",method=RequestMethod.POST)
	@Valid
	public String FindPrints(Long id) throws Exception{
		RollBarcode rollBarcode=rollBarcodeService.findById(RollBarcode.class,id);
		Gson gson = new Gson();
		List<BarCodePrintRecord> list = gson.fromJson(rollBarcode.getIndividualOutPutString(), new TypeToken<List<BarCodePrintRecord>>(){}.getType());
		return GsonTools.toJson(list);
	}


	@ResponseBody
	@Journal(name = "修改条码")
	@RequestMapping(value = "editBacode", method = RequestMethod.POST)
	public  String editBacode(long id, Integer customerBarCodeRecord, Integer agentBarCodeRecord,long btwfileId) {

		RollBarcode entity=btwFileService.findById(RollBarcode.class,id);
		return GsonTools.toJson(btwFileService.editBacode(entity, customerBarCodeRecord, agentBarCodeRecord,btwfileId));
	}

}