/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.audit.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.audit.entity.AuditProcessSetting;
import com.bluebirdme.mes.audit.service.IAuditProcessSettingService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;

/**
 * 流程设置Controller
 *
 * @author 高飞
 * @Date 2016-10-24 14:51:44
 */
@Controller
@RequestMapping("/audit/setting")
@Journal(name = "流程设置")
public class AuditProcessSettingController extends BaseController {
    /**
     * 流程设置页面
     */
    final String index = "audit/auditProcessSetting";
    final String addOrEdit = "audit/auditProcessSettingAddOrEdit";

    @Resource
    IAuditProcessSettingService auditProcessSettingService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取流程设置列表信息")
    @RequestMapping("list")
    public String getAuditProcessSetting(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(auditProcessSettingService.findPageInfo(filter, page));
    }


    @Journal(name = "添加流程设置页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(AuditProcessSetting auditProcessSetting) {
        return new ModelAndView(addOrEdit, model.addAttribute("auditProcessSetting", auditProcessSetting));
    }

    @ResponseBody
    @Journal(name = "保存流程设置", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(AuditProcessSetting auditProcessSetting) throws Exception {
        auditProcessSettingService.save(auditProcessSetting);
        return GsonTools.toJson(auditProcessSetting);
    }

    @Journal(name = "编辑流程设置页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(AuditProcessSetting auditProcessSetting) {
        auditProcessSetting = auditProcessSettingService.findById(AuditProcessSetting.class, auditProcessSetting.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("auditProcessSetting", auditProcessSetting));
    }

    @ResponseBody
    @Journal(name = "编辑流程设置", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String edit(AuditProcessSetting auditProcessSetting) throws Exception {
        auditProcessSettingService.saveAuditSetting(auditProcessSetting);
        return GsonTools.toJson(auditProcessSetting);
    }

    @ResponseBody
    @Journal(name = "删除流程设置", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        auditProcessSettingService.delete(AuditProcessSetting.class, ids);
        return Constant.AJAX_SUCCESS;
    }
}