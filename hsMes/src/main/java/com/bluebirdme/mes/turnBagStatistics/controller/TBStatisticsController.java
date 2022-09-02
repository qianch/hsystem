/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.turnBagStatistics.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.planner.produce.service.IProducePlanService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;

/**
 * 综合统计Controller
 *
 * @author 徐波
 * @Date 2016-11-26 14:44:04
 */
@Controller
@RequestMapping("/tbStatistics")
@Journal(name = "翻包订单统计")
public class TBStatisticsController extends BaseController {
    /**
     * 综合统计页面
     */
    final String index1 = "statistics/turnBagStatistics";
    final String index2 = "statistics/turnBagStatistics2";

    @Resource
    IProducePlanService producePlanService;

    @Journal(name = "首页")
    @RequestMapping(value = "index1", method = RequestMethod.GET)
    public String index1() {
        return index1;
    }

    @Journal(name = "首页")
    @RequestMapping(value = "index2", method = RequestMethod.GET)
    public String index2() {
        return index2;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取翻包订单信息")
    @RequestMapping("list")
    public String getTBStatistics(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(producePlanService.findTBPageInfo(filter, page));
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取被翻包订单信息")
    @RequestMapping("list2")
    public String getTBStatistics2(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(producePlanService.findTBPageInfo2(filter, page));
    }
}