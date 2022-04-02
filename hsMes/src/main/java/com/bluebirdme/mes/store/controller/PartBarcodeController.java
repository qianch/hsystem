/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.controller;

import com.bluebirdme.mes.btwManager.service.IBtwFileService;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.printer.entity.BarCodePrintRecord;
import com.bluebirdme.mes.store.entity.BoxBarcode;
import com.bluebirdme.mes.store.entity.RollBarcode;
import com.bluebirdme.mes.utils.FilterRules;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bluebirdme.mes.core.annotation.Journal;




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
import com.bluebirdme.mes.store.entity.PartBarcode;
import com.bluebirdme.mes.store.service.IPartBarcodeService;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

/**
 * 部件条码Controller
 * @author 徐波
 * @Date 2016-11-14 14:37:30
 */
@Controller
@RequestMapping("/partBarcode")
@Journal(name="部件条码")
public class PartBarcodeController extends BaseController {

	// 部件条码页面
	final String index = "store/partBarcode";
	final String addOrEdit="store/partBarcodeAddOrEdit";

	@Resource IPartBarcodeService partBarcodeService;

	@Resource
	IBtwFileService btwFileService;

	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="获取部件条码列表信息")
	@RequestMapping("list")
	public String getPartBarcode(String filterRules,Filter filter, Page page) throws Exception{
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
		return GsonTools.toJson(partBarcodeService.findPageInfo(filter, page));
	}
	

	@Journal(name="添加部件条码页面")
	@RequestMapping(value="add",method=RequestMethod.GET)
	public ModelAndView _add(PartBarcode partBarcode){
		return new ModelAndView(addOrEdit,model.addAttribute("partBarcode", partBarcode));
	}
	
	@ResponseBody
	@Journal(name="保存部件条码",logType=LogType.DB)
	@RequestMapping(value="add",method=RequestMethod.POST)
	@Valid
	public String add(PartBarcode partBarcode) throws Exception{
		partBarcodeService.save(partBarcode);
		return GsonTools.toJson(partBarcode);
	}
	
	@Journal(name="编辑部件条码页面")
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public ModelAndView _edit(PartBarcode partBarcode){
		partBarcode=partBarcodeService.findById(PartBarcode.class, partBarcode.getId());
		return new ModelAndView(addOrEdit, model.addAttribute("partBarcode", partBarcode));
	}
	
	@ResponseBody
	@Journal(name="编辑部件条码",logType=LogType.DB)
	@RequestMapping(value="edit",method=RequestMethod.POST)
	@Valid
	public String edit(PartBarcode partBarcode) throws Exception{
		partBarcodeService.update(partBarcode);
		return GsonTools.toJson(partBarcode);
	}

	@ResponseBody
	@Journal(name="删除部件条码",logType=LogType.DB)
	@RequestMapping(value="delete",method=RequestMethod.POST)
	public String edit(String ids) throws Exception{
		partBarcodeService.delete(PartBarcode.class,ids);
		return Constant.AJAX_SUCCESS;
	}

	@ResponseBody
	@Journal(name = "清空条码信息", logType = LogType.DB)
	@RequestMapping(value = "clearPart", method = RequestMethod.POST)
	public String clearPart(String ids) throws Exception {
		partBarcodeService.clearPart(ids);
		return Constant.AJAX_SUCCESS;
	}

	@ResponseBody
	@Journal(name="查询打印信息",logType=LogType.DB)
	@RequestMapping(value="FindPrints",method=RequestMethod.POST)
	@Valid
	public String FindPrints(Long id) throws Exception{
		PartBarcode partBarcode=partBarcodeService.findById(PartBarcode.class,id);
		Gson gson = new Gson();
		List<BarCodePrintRecord> list = gson.fromJson(partBarcode.getIndividualOutPutString(), new TypeToken<List<BarCodePrintRecord>>(){}.getType());
		return GsonTools.toJson(list);
	}

	@ResponseBody
	@Journal(name = "修改条码")
	@RequestMapping(value = "editBacode", method = RequestMethod.POST)
	public  String editBacode(long id, Integer customerBarCodeRecord, Integer agentBarCodeRecord,long btwfileId) {

		PartBarcode entity=btwFileService.findById(PartBarcode.class,id);
		return GsonTools.toJson(btwFileService.editBacode(entity, customerBarCodeRecord, agentBarCodeRecord,btwfileId));
	}

}