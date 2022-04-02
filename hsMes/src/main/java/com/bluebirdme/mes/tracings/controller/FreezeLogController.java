/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.tracings.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.tracings.service.IFreezeLogService;

/**
 * 冻结解冻列Controller
 * 
 * @author 宋黎明
 * @Date 2016-12-06 14:03:19
 */
@Controller
@RequestMapping("/tracingLog/freezeLog")
@Journal(name = "冻结解冻记录表")
public class FreezeLogController extends BaseController {
	//冻结解冻记录页面
	final String index = "tracings/freezeLog/freezeLog";

	@Resource
	IFreezeLogService freezeLogService;

	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}

	@NoAuth
	@ResponseBody
	@Journal(name = "查看冻结解冻记录")
	@RequestMapping("list")
	public String getFreezeLog(Filter filter, Page page) throws Exception {
		return GsonTools.toJson(freezeLogService.findPageInfo(filter, page));
	}
}