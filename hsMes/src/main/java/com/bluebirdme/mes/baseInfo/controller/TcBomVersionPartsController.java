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


import java.util.HashMap;
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
import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.baseInfo.service.ITcBomVersionPartsService;
import com.google.gson.GsonBuilder;

import org.xdemo.superutil.thirdparty.gson.GsonTools;

/**
 * 版本部件Controller
 *
 * @author 肖文彬
 * @Date 2016-10-9 16:11:41
 */
@Controller
@RequestMapping("/bom/tcBomVersionParts")
@Journal(name = "版本部件")
public class TcBomVersionPartsController extends BaseController {
    /**
     * 版本部件页面
     */
    final String index = "baseInfo/tcBomVersionParts";
    final String addOrEdit = "baseInfo/tcBomVersionPartsAddOrEdit";

    @Resource
    ITcBomVersionPartsService tcBomVersionPartsService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取版本部件列表信息")
    @RequestMapping("list")
    public String getTcBomVersionParts(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(tcBomVersionPartsService.findPageInfo(filter, page));
    }

    @Journal(name = "添加版本部件页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(TcBomVersionParts tcBomVersionParts) {
        return new ModelAndView(addOrEdit, model.addAttribute("tcBomVersionParts", tcBomVersionParts));
    }

    @ResponseBody
    @Journal(name = "保存版本部件", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(TcBomVersionParts tcBomVersionParts) throws Exception {
        tcBomVersionPartsService.save(tcBomVersionParts);
        return GsonTools.toJson(tcBomVersionParts);
    }

    @Journal(name = "编辑版本部件页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(TcBomVersionParts tcBomVersionParts) {
        tcBomVersionParts = tcBomVersionPartsService.findById(TcBomVersionParts.class, tcBomVersionParts.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("tcBomVersionParts", tcBomVersionParts));
    }

    @ResponseBody
    @Journal(name = "编辑版本部件", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(TcBomVersionParts tcBomVersionParts) throws Exception {
        tcBomVersionPartsService.update(tcBomVersionParts);
        return GsonTools.toJson(tcBomVersionParts);
    }

    @ResponseBody
    @Journal(name = "删除版本部件", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        String split[] = ids.split(",");
        long longs[] = new long[split.length];
        for (int i = 0; i < split.length; i++) {
            longs[i] = Long.parseLong(split[i]);
        }
        tcBomVersionPartsService.delete(TcBomVersionParts.class, longs);
        return Constant.AJAX_SUCCESS;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取版本部件列表镜像信息")
    @RequestMapping("mirrorList")
    public String getTcBomVersionPartsMirror(Filter filter, Page page) {
        return GsonTools.toJson(tcBomVersionPartsService.findPageInfo1(filter, page));
    }
}