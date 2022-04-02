package com.bluebirdme.mes.tracing.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.tracing.service.ITracingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;

@Controller
@RequestMapping("/tracing")
@Journal(name = "产品追溯")
public class TracingController extends BaseController {
	@Resource
	ITracingService tracingService;
	// 根据产品条码追溯
	final String index = "tracing/tracing";

	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}

	@ResponseBody
	@Journal(name = "根据产品条码查询")
	@RequestMapping(value = "{code}")
	public String barcode(@PathVariable("code") String code) throws Exception {
		return GsonTools.toJson(tracingService.tracing(code));
	}
}
