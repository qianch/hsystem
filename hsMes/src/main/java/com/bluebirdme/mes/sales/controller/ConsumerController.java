/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.sales.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.service.IConsumerService;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import com.bluebirdme.mes.utils.FilterRules;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.StringUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户管理Controller
 *
 * @author 肖文彬
 * @Date 2016-9-28 11:24:46
 */
@Controller
@RequestMapping("/consumer")
@Journal(name = "客户管理")
public class ConsumerController extends BaseController {
    /**
     * 客户管理页面
     */
    final String index = "sales/consumer/consumer";
    final String addOrEdit = "sales/consumer/consumerAddOrEdit";

    @Resource
    IConsumerService consumerService;
    @Resource
    ISalesOrderService salesOrderService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取客户管理列表信息")
    @RequestMapping("list")
    public String getConsumer(String filterRules, Page page) throws Exception {
        Filter filter = new Filter();
        if (!StringUtils.isEmpty(filterRules)) {
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(filterRules).getAsJsonArray();
            FilterRules rule;
            Gson gson = new Gson();
            for (JsonElement obj : array) {
                rule = gson.fromJson(obj, FilterRules.class);
                if (rule.getField().equals("CONSUMERGRADE")) {
                    switch (rule.getValue().toUpperCase()) {
                        case "A" -> filter.set(rule.getField(), "like:1");
                        case "B" -> filter.set(rule.getField(), "like:2");
                        case "C" -> filter.set(rule.getField(), "like:3");
                        default -> filter.set(rule.getField(), "like:-1");
                    }
                } else {
                    filter.set(rule.getField(), "like:" + rule.getValue());
                }
            }
        }
        return GsonTools.toJson(consumerService.findPageInfo(filter, page));
    }

    @Journal(name = "添加客户管理页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(Consumer consumer) {
        return new ModelAndView(addOrEdit, model.addAttribute("consumer", consumer));
    }

    @ResponseBody
    @Journal(name = "保存客户管理", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(Consumer consumer) throws Exception {
        consumerService.save(consumer);
        return GsonTools.toJson(consumer);
    }

    @Journal(name = "编辑客户管理页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(Consumer consumer) {
        consumer = consumerService.findById(Consumer.class, consumer.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("consumer", consumer));
    }

    @ResponseBody
    @Journal(name = "编辑客户管理", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(Consumer consumer) throws Exception {
        consumerService.update(consumer);
        return GsonTools.toJson(consumer);
    }

    @ResponseBody
    @Journal(name = "删除客户管理", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            Map<String, Object> map = new HashMap<>();
            map.put("salesOrderConsumerId", Long.parseLong(id));
            List<SalesOrder> orders = salesOrderService.findListByMap(SalesOrder.class, map);
            if (orders.size() > 0) {
                return ajaxError("已经有订单关联客户信息，只能修改，不允许删除。");
            }
        }
        consumerService.delete(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "作废客户管理", logType = LogType.DB)
    @RequestMapping(value = "old", method = RequestMethod.POST)
    public String old(String ids) throws Exception {
        consumerService.old(ids);
        return Constant.AJAX_SUCCESS;
    }
}