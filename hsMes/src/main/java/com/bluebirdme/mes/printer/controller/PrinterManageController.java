package com.bluebirdme.mes.printer.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.printer.entity.Printer;
import com.bluebirdme.mes.printer.service.IPrinterManageSerivice;

@Controller
@RequestMapping("printerManage")
@Journal(name = "打印机管理")
public class PrinterManageController extends BaseController{
	
	@Resource
	IPrinterManageSerivice printerManageSerivice ;
	
	final String index="printer/printerManage";
	final String addOrEdit = "printer/printerManageAddOrEdit";
	
	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}

	@NoAuth
	@ResponseBody
	@Journal(name = "获取打印机信息列表信息")
	@RequestMapping("list")
	public String getPrinter(Filter filter, Page page) throws Exception {
		return GsonTools.toJson(printerManageSerivice.findPageInfo(filter, page));
	}

	@Journal(name = "添加打印机信息页面")
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public ModelAndView _add(Printer printer) {
		return new ModelAndView(addOrEdit, model.addAttribute("printer", printer));
	}

	@ResponseBody
	@Journal(name = "保存打印机信息", logType = LogType.DB)
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@Valid
	public String add(Printer printer) throws Exception {
		printer.setPrinterState(1);
		printerManageSerivice.save(printer);
		return GsonTools.toJson(printer);
	}

	@Journal(name = "编辑打印机信息页面")
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public ModelAndView _edit(Printer printer) {
		printer = printerManageSerivice.findById(Printer.class, printer.getId());
		return new ModelAndView(addOrEdit, model.addAttribute("printer", printer));
	}

	@ResponseBody
	@Journal(name = "编辑打印机信息", logType = LogType.DB)
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@Valid
	public String edit(Printer printer) throws Exception {
		printer.setPrinterState(1);
		printerManageSerivice.update(printer);
		return GsonTools.toJson(printer);
	}

	@ResponseBody
	@Journal(name = "删除打印机信息", logType = LogType.DB)
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public String edit(String ids) throws Exception {
		printerManageSerivice.delete(Printer.class, ids);
		return Constant.AJAX_SUCCESS;
	}

	@NoAuth
	@ResponseBody
	@Journal(name = "获取所有部门信息")
	@RequestMapping(value = "departmentCombo", method = RequestMethod.POST)
	public String departmentCombo() throws Exception {
		return GsonTools.toJson(printerManageSerivice.findALLDepartment());
	}
	
}
