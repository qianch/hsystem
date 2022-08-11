/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.stock.entity.StockCheckResult;
import com.bluebirdme.mes.stock.service.IStockCheckResultService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;

/**
 * 盘库结果表Controller
 *
 * @author 肖文彬
 * @Date 2016-11-8 15:24:59
 */
@Controller
@RequestMapping("stock/stockCheckResult")
@Journal(name = "盘库结果表")
public class StockCheckResultController extends BaseController {
    // 盘库结果表页面
    final String index = "stock/stockCheckResult";
    final String addOrEdit = "stock/stockCheckResultAddOrEdit";

    @Resource
    IStockCheckResultService stockCheckResultService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取盘库结果表列表信息")
    @RequestMapping("list")
    public String getStockCheckResult(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(stockCheckResultService.findPageInfo(filter, page));
    }


    @Journal(name = "添加盘库结果表页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(StockCheckResult stockCheckResult) {
        return new ModelAndView(addOrEdit, model.addAttribute("stockCheckResult", stockCheckResult));
    }

    @ResponseBody
    @Journal(name = "保存盘库结果表", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(StockCheckResult stockCheckResult) throws Exception {
        stockCheckResultService.save(stockCheckResult);
        return GsonTools.toJson(stockCheckResult);
    }

    @Journal(name = "编辑盘库结果表页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(StockCheckResult stockCheckResult) {
        stockCheckResult = stockCheckResultService.findById(StockCheckResult.class, stockCheckResult.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("stockCheckResult", stockCheckResult));
    }

    @ResponseBody
    @Journal(name = "编辑盘库结果表", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(StockCheckResult stockCheckResult) throws Exception {
        stockCheckResultService.update(stockCheckResult);
        return GsonTools.toJson(stockCheckResult);
    }

    @ResponseBody
    @Journal(name = "删除盘库结果表", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        stockCheckResultService.delete(StockCheckResult.class, ids);
        return Constant.AJAX_SUCCESS;
    }
}