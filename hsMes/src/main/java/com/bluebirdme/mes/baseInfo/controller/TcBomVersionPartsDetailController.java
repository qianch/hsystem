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
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsDetail;
import com.bluebirdme.mes.baseInfo.service.ITcBomVersionPartsDetailService;
import com.google.gson.GsonBuilder;

import org.xdemo.superutil.thirdparty.gson.GsonTools;

/**
 * 部件明细Controller
 *
 * @author 肖文彬
 * @Date 2016-10-9 13:49:36
 */
@Controller
@RequestMapping("/bom/tcBomVersionPartsDetail")
@Journal(name = "部件明细")
public class TcBomVersionPartsDetailController extends BaseController {
    /**
     * 部件明细页面
     */
    final String index = "baseInfo/tcBomVersionPartsDetail";
    final String addOrEdit = "baseInfo/tcBomVersionPartsDetailAddOrEdit";

    @Resource
    ITcBomVersionPartsDetailService tcBomVersionPartsDetailService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取部件明细列表信息")
    @RequestMapping("list")
    public String getTcBomVersionPartsDetail(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(tcBomVersionPartsDetailService.findPageInfo(filter, page));
    }

    @Journal(name = "添加部件明细页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(TcBomVersionPartsDetail tcBomVersionPartsDetail) {
        return new ModelAndView(addOrEdit, model.addAttribute("tcBomVersionPartsDetail", tcBomVersionPartsDetail));
    }

    @ResponseBody
    @Journal(name = "保存部件明细", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(TcBomVersionPartsDetail tcBomVersionPartsDetail) throws Exception {
        tcBomVersionPartsDetailService.save(tcBomVersionPartsDetail);
        return GsonTools.toJson(tcBomVersionPartsDetail);
    }

    @Journal(name = "编辑部件明细页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(TcBomVersionPartsDetail tcBomVersionPartsDetail) {
        tcBomVersionPartsDetail = tcBomVersionPartsDetailService.findById(TcBomVersionPartsDetail.class, tcBomVersionPartsDetail.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("tcBomVersionPartsDetail", tcBomVersionPartsDetail));
    }

    @ResponseBody
    @Journal(name = "编辑部件明细", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(TcBomVersionPartsDetail tcBomVersionPartsDetail) throws Exception {
        tcBomVersionPartsDetailService.update(tcBomVersionPartsDetail);
        return GsonTools.toJson(tcBomVersionPartsDetail);
    }

    @ResponseBody
    @Journal(name = "删除部件明细", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        if (ids.length() > 0) {
            String id[] = ids.split(",");
            TcBomVersionPartsDetail tcBomVersionPartsDetail = tcBomVersionPartsDetailService.findById(TcBomVersionPartsDetail.class, Long.parseLong(id[0]));
            TcBomVersionParts tcBomVersionParts = tcBomVersionPartsDetailService
                    .findById(TcBomVersionParts.class, tcBomVersionPartsDetail.getTcProcBomPartsId());
            TcBomVersion tcBomVersion = tcBomVersionPartsDetailService.findById(TcBomVersion.class,
                    tcBomVersionParts.getTcProcBomVersoinId());
            if (tcBomVersion.getAuditState() > 0) {
                return ajaxError("不能修改审核中或已通过的数据");
            }
        }
        tcBomVersionPartsDetailService.delete(ids);
        return Constant.AJAX_SUCCESS;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取部件明细列表信息")
    @RequestMapping("mirrorList")
    public String getTcBomVersionPartsDetailMirror(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(tcBomVersionPartsDetailService.findPageInfo1(filter, page));
    }
}