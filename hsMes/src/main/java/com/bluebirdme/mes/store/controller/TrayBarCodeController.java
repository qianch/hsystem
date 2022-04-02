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
import com.bluebirdme.mes.store.entity.PartBarcode;
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
import com.bluebirdme.mes.store.entity.TrayBarCode;
import com.bluebirdme.mes.store.service.ITrayBarCodeService;

import org.xdemo.superutil.thirdparty.gson.GsonTools;

/**
 * 托条码Controller
 * @author 宋黎明
 * @Date 2016-11-8 14:59:26
 */
@Controller
@RequestMapping("/barcode")
@Journal(name="条码查询信息")
public class TrayBarCodeController extends BaseController {

	@Resource ITrayBarCodeService trayBarCodeService;
	final String index = "store/trayBarCode";
	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}

	@Resource
	IBtwFileService btwFileService;
	
	@NoAuth
	@ResponseBody
	@Journal(name="获取托条码列表信息")
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
		return GsonTools.toJson(trayBarCodeService.findPageInfo(filter, page));
	}
	@NoAuth
	@ResponseBody
	@Journal(name="通过卷条码查询订单信息")
	@RequestMapping("salesOrderByRollCode")
	public String salesOrderByRollCode(String code) throws Exception{
		return GsonTools.toJson(trayBarCodeService.findSalesOrderByRollcode(code));
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="通过箱条码查询订单信息")
	@RequestMapping("salesOrderByBoxCode")
	public String salesOrderByBoxCode(String code) throws Exception{
		return GsonTools.toJson(trayBarCodeService.findSalesOrderByBoxcode(code));
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="通过托条码查询订单信息")
	@RequestMapping("salesOrderByTrayCode")
	public String salesOrderByTrayCode(String code) throws Exception{
		return GsonTools.toJson(trayBarCodeService.findSalesOrderByTraycode(code));
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="通过卷条码查询产品信息")
	@RequestMapping("productByRollCode")
	public String productByRollCode(String code) throws Exception{
		return GsonTools.toJson(trayBarCodeService.findProductByRollcode(code));
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="通过箱条码查询订单信息")
	@RequestMapping("productByBoxCode")
	public String productByBoxCode(String code) throws Exception{
		return GsonTools.toJson(trayBarCodeService.findProductByBoxcode(code));
	}

	@ResponseBody
	@Journal(name = "清空条码信息", logType = LogType.DB)
	@RequestMapping(value = "clearTray", method = RequestMethod.POST)
	public String clearTray(String ids) throws Exception {
		trayBarCodeService.clearTray(ids);
		return Constant.AJAX_SUCCESS;
	}

	@ResponseBody
	@Journal(name="查询打印信息",logType=LogType.DB)
	@RequestMapping(value="FindPrints",method=RequestMethod.POST)
	@Valid
	public String FindPrints(Long id) throws Exception{
		TrayBarCode trayBarcode=trayBarCodeService.findById(TrayBarCode.class,id);
		Gson gson = new Gson();
		List<BarCodePrintRecord> list = gson.fromJson(trayBarcode.getIndividualOutPutString(), new TypeToken<List<BarCodePrintRecord>>(){}.getType());
		return GsonTools.toJson(list);
	}

	@ResponseBody
	@Journal(name = "修改条码")
	@RequestMapping(value = "editBacode", method = RequestMethod.POST)
	public  String editBacode(long id, Integer customerBarCodeRecord, Integer agentBarCodeRecord,long btwfileId) {

		TrayBarCode entity=btwFileService.findById(TrayBarCode.class,id);
		return GsonTools.toJson(btwFileService.editBacode(entity, customerBarCodeRecord, agentBarCodeRecord,btwfileId));
	}
	
}