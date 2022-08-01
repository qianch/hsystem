/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.sales.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.sales.service.ISalesOrderStockService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;

/**
 * 订单库存表Controller
 *
 * @author 高飞
 * @Date 2016-12-15 18:28:22
 */
@Controller
@RequestMapping("/order/stock")
@Journal(name = "订单库存表")
public class SalesOrderStockController extends BaseController {
    /**
     * 订单库存表页面
     */
    final String index = "sales/salesOrderStock";

    @Resource
    ISalesOrderStockService salesOrderStockService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取订单库存表列表信息")
    @RequestMapping("{code}")
    public String getSalesOrderStock(@PathVariable("code") String code) throws Exception {
        Filter filter = new Filter();
        filter.set("code", code);
        Page page = new Page();
        page.setAll(1);
        return GsonTools.toJson(salesOrderStockService.findPageInfo(filter, page));
    }
}