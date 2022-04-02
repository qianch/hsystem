/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.controller;

import com.bluebirdme.mes.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bluebirdme.mes.core.annotation.Journal;




import java.util.HashMap;
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
import com.bluebirdme.mes.stock.entity.StockCheck;
import com.bluebirdme.mes.stock.service.IStockCheckService;
import com.google.gson.GsonBuilder;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

/**
 * 盘库记录表Controller
 * @author 肖文彬
 * @Date 2016-11-8 15:25:19
 */
@Controller
@RequestMapping("stock/stockCheck")
@Journal(name="盘库记录表")
public class StockCheckController extends BaseController {

	// 盘库记录表页面
	final String index = "stock/stockCheck";
	final String addOrEdit="stock/stockCheckAddOrEdit";

	@Resource IStockCheckService stockCheckService;

	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="获取盘库记录表列表信息")
	@RequestMapping("list")
	public String getStockCheck(Filter filter, Page page) throws Exception{
		return GsonTools.toJson(stockCheckService.findPageInfo(filter, page));
	}
	

	@Journal(name="添加盘库记录表页面")
	@RequestMapping(value="add",method=RequestMethod.GET)
	public ModelAndView _add(StockCheck stockCheck){
		return new ModelAndView(addOrEdit,model.addAttribute("stockCheck", stockCheck));
	}
	
	@ResponseBody
	@Journal(name="保存盘库记录表",logType=LogType.DB)
	@RequestMapping(value="add",method=RequestMethod.POST)
	@Valid
	public String add(StockCheck stockCheck) throws Exception{
		stockCheckService.save(stockCheck);
		return GsonTools.toJson(stockCheck);
	}
	
	@Journal(name="编辑盘库记录表页面")
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public ModelAndView _edit(StockCheck stockCheck){
		stockCheck=stockCheckService.findById(StockCheck.class, stockCheck.getId());
		return new ModelAndView(addOrEdit, model.addAttribute("stockCheck", stockCheck));
	}
	
	@ResponseBody
	@Journal(name="编辑盘库记录表",logType=LogType.DB)
	@RequestMapping(value="edit",method=RequestMethod.POST)
	@Valid
	public String edit(StockCheck stockCheck) throws Exception{
		stockCheckService.update(stockCheck);
		return GsonTools.toJson(stockCheck);
	}

	@ResponseBody
	@Journal(name="删除盘库记录表",logType=LogType.DB)
	@RequestMapping(value="delete",method=RequestMethod.POST)
	public String edit(String ids) throws Exception{
		stockCheckService.delete(StockCheck.class,ids);
		return Constant.AJAX_SUCCESS;
	}
	
	@ResponseBody
	@Journal(name="根据记录查询结果")
	@RequestMapping(value="findR",method=RequestMethod.POST)
	@Valid
	public String finds(String id) throws Exception{
		stockCheckService.findR(id);
		return Constant.AJAX_SUCCESS;
	}

}