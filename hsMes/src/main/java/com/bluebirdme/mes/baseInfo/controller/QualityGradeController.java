/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.controller;

import com.bluebirdme.mes.core.base.controller.BaseController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bluebirdme.mes.core.annotation.Journal;


import com.bluebirdme.mes.core.annotation.NoLogin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.StringUtils;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.exception.BusinessException;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.store.entity.Warehouse;
import com.bluebirdme.mes.baseInfo.entity.QualityGrade;
import com.bluebirdme.mes.baseInfo.service.IQualityGradeService;
import com.google.gson.GsonBuilder;

import org.xdemo.superutil.thirdparty.gson.GsonTools;

/**
 * 质量等级Controller
 *
 * @author 高飞
 * @Date 2016-10-12 10:34:41
 */
@Controller
@RequestMapping("/qualityGrade")
@Journal(name = " 质量等级")
public class QualityGradeController extends BaseController {
    /**
     * 质量等级页面
     */
    final String index = "baseInfo/qualityGrade";
    final String addOrEdit = "baseInfo/qualityGradeAddOrEdit";
    @Resource
    IQualityGradeService qualityGradeService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取 质量等级列表信息")
    @RequestMapping("list")
    public String getQualityGrade(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(qualityGradeService.findPageInfo(filter, page));
    }

    @Journal(name = "添加 质量等级页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(QualityGrade qualityGrade) {
        return new ModelAndView(addOrEdit, model.addAttribute("qualityGrade", qualityGrade));
    }

    @ResponseBody
    @Journal(name = "保存 质量等级", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(QualityGrade qualityGrade) throws Exception {
        qualityGradeService.save(qualityGrade);
        return GsonTools.toJson(qualityGrade);
    }

    @Journal(name = "编辑 质量等级页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(QualityGrade qualityGrade) {
        qualityGrade = qualityGradeService.findById(QualityGrade.class, qualityGrade.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("qualityGrade", qualityGrade));
    }

    @ResponseBody
    @Journal(name = "编辑 质量等级", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(QualityGrade qualityGrade) throws Exception {
        qualityGradeService.update(qualityGrade);
        return GsonTools.toJson(qualityGrade);
    }

    @ResponseBody
    @Journal(name = "删除 质量等级", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        qualityGradeService.delete(QualityGrade.class, ids);
        return Constant.AJAX_SUCCESS;
    }

    @NoLogin
    @ResponseBody
    @Journal(name = "获取所有质量等级")
    @RequestMapping(value = "getQualityGrade", method = RequestMethod.POST)
    public String getQualityGrade() {
        List<QualityGrade> plist = qualityGradeService.findAll(QualityGrade.class);
        List<HashMap<String, String>> outInfo = new ArrayList<HashMap<String, String>>();
        for (QualityGrade p : plist) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("gradename", p.getGradeName());
            map.put("gradedesc", p.getGradeDesc());
            outInfo.add(map);
        }
        return GsonTools.toJson(outInfo);
    }
}