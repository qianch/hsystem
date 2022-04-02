/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.weave.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlanDevices;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;

/**
 * 编织计划Controller
 * @author 肖文彬
 * @Date 2016-10-18 13:37:59
 */
@Controller
@RequestMapping("planner/weave/turnbag")
@Journal(name="编织计划")
public class TurnBagWeavePlanController extends BaseController {

	// 编织计划页面
	final String index = "planner/weave/turnbag/turnbagWeave";
	//裁剪计划页面
	final String index1 = "planner/weave/turnbag/turnbagCut";


	@Resource IWeavePlanService weavePlanService;
	

	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}
	@Journal(name = "裁剪计划页面")
	@RequestMapping(method = RequestMethod.GET,value = "cut")
	public String index1() {
		return index1;
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="获取编织计划列表信息")
	@RequestMapping("list")
	public String getWeavePlan(String planCode) throws Exception{
		return GsonTools.toJson(weavePlanService.findWeavePlan(planCode));
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="获取编织计划列表信息")
	@RequestMapping("weaveList")
	public String getWeavePlans(Filter filter, Page page) throws Exception{
		if (filter.get("isFinish")==null) {
			filter.set("isFinish", "-1");
		}
		if (filter.get("closed")==null) {
			filter.set("closed", "0");
		}
		Map<String, Object> findPageInfo = weavePlanService.findWeavePageInfo(filter, page);
		
		return GsonTools.toJson(findPageInfo);
	}
	
	}