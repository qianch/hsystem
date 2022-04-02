/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.cut.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.planner.cut.service.ICutPlanService;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 编织计划Controller
 * @author 肖文彬
 * @Date 2016-10-18 13:37:59
 */
@Controller
@RequestMapping("planner/cut/turnbag")
@Journal(name="裁剪计划")
public class TurnBagCutPlanController extends BaseController {



	@Resource ICutPlanService cutPlanService;

	@NoAuth
	@ResponseBody
	@Journal(name="获取裁剪计划列表信息")
	@RequestMapping("list")
	public String getWeavePlan(String planCode) throws Exception{
		return GsonTools.toJson(cutPlanService.findCutPlan(planCode));
	}

	@NoAuth
	@ResponseBody
	@Journal(name="获取编织计划列表信息")
	@RequestMapping("cutList")
	public String getWeavePlans(Filter filter, Page page) throws Exception{
		if (filter.get("isFinish")==null) {
			filter.set("isFinish", "-1");
		}
		if (filter.get("closed")==null) {
			filter.set("closed", "0");
		}
		Map<String, Object> findPageInfo = cutPlanService.findCutPageInfo(filter, page);

		return GsonTools.toJson(findPageInfo);
	}
	
	}