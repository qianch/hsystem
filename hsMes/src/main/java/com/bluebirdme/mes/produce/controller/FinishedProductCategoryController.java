/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.exception.BusinessException;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.produce.entity.FinishedProductCategory;
import com.bluebirdme.mes.produce.service.IFinishedProductCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 产品类别Controller
 *
 * @author king
 * @Date 2017-8-2 8:44:33
 */
@Controller
@RequestMapping("product/category")
@Journal(name = "产品类别")
public class FinishedProductCategoryController extends BaseController {
    /**
     * 成品类别管理页面
     */
    final String index = "produce/productCategory/finishedProductCategory";
    final String addOrEdit = "produce/productCategory/finishedProductCategoryAddOrEdit";

    @Resource
    IFinishedProductCategoryService productCategoryService;


    /**
     * 产品类别管理首页
     */
    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    /**
     * 产品类别管理列表
     *
     * @return list
     */
    @ResponseBody
    @Journal(name = "产品类别管理列表")
    @RequestMapping("list")
    public String getWeightCarrier(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(productCategoryService.findPageInfo(filter, page));
    }


    /**
     * 添加产品类别页面
     */
    @Journal(name = "添加产品类别页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(FinishedProductCategory finishedProductCategory) {
        return new ModelAndView(addOrEdit, model.addAttribute("finishedProductCategory", finishedProductCategory));
    }

    /**
     * 保存产品类别
     */
    @ResponseBody
    @Journal(name = "保存产品类别", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(FinishedProductCategory finishedProductCategory) throws Exception {
        finishedProductCategory.setCreateTime(new Date());
        finishedProductCategory.setCreater(session.getAttribute(Constant.CURRENT_USER_NAME).toString());
        //成品编号验证
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("categoryCode", finishedProductCategory.getCategoryCode());
        if (productCategoryService.isExist(FinishedProductCategory.class, map, true)) {
            throw new BusinessException("产品编码重复：" + finishedProductCategory.getCategoryCode());
        }
        productCategoryService.save(finishedProductCategory);
        return GsonTools.toJson(finishedProductCategory);
    }


    /**
     * 编辑产品类别页面
     */
    @Journal(name = "编辑产品类别页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(FinishedProductCategory finishedProductCategory) {
        finishedProductCategory = productCategoryService.findById(FinishedProductCategory.class, finishedProductCategory.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("finishedProductCategory", finishedProductCategory));
    }


    /**
     * 编辑产品类被
     */
    @ResponseBody
    @Journal(name = "编辑产品类别", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(FinishedProductCategory finishedProductCategory) throws Exception {
        finishedProductCategory.setModifyTime(new Date());
        finishedProductCategory.setModifyUser(session.getAttribute(Constant.CURRENT_USER_NAME).toString());
        //成品编号验证
        Map<String, Object> map = new HashMap<>();
        map.put("categoryCode", finishedProductCategory.getCategoryCode());
        if (productCategoryService.isExist(FinishedProductCategory.class, map, finishedProductCategory.getId(), true)) {
            throw new BusinessException("产品编码重复：" + finishedProductCategory.getCategoryCode());
        }
        productCategoryService.update2(finishedProductCategory);
        return GsonTools.toJson(finishedProductCategory);
    }


    /**
     * 产品类别编码验证
     */
    @ResponseBody
    @Journal(name = "产品类别编码验证", logType = LogType.DB)
    @RequestMapping(value = "isexist")
    public String isexist(String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryCode", code);
        if (productCategoryService.isExist(FinishedProductCategory.class, map, true)) {
            return "0";
        }
        return "1";
    }
}

		
