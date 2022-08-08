/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.barcode.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.siemens.barcode.service.ISuitErrorLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;

/**
 * 条码扫描错误记录Controller
 *
 * @author 高飞
 * @Date 2017-8-9 16:30:35
 */
@Controller
@RequestMapping("/siemens/barcode/suitErrorLog")
@Journal(name = "条码扫描错误记录")
public class SuitErrorLogController extends BaseController {
    // 条码扫描错误记录页面
    final String index = "siemens/barcode/suitErrorLog";

    @Resource
    ISuitErrorLogService suitErrorLogService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取条码扫描错误记录列表信息")
    @RequestMapping("list")
    public String getSuitErrorLog(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(suitErrorLogService.findPageInfo(filter, page));
    }
}