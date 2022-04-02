/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.mobile.common.controller;

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
import org.xdemo.superutil.j2se.RunTimeUtils;
import org.xdemo.superutil.j2se.StringUtils;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.exception.BusinessException;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.mobile.common.entity.AppVersion;
import com.bluebirdme.mes.mobile.common.service.IAppVersionService;
import com.google.gson.GsonBuilder;

import org.xdemo.superutil.thirdparty.gson.GsonTools;

/**
 * PDA终端版本Controller
 *
 * @author 高飞
 * @Date 2016-11-6 10:22:52
 */
@Controller
@RequestMapping("/app")
@Journal(name = "PDA终端版本")
public class AppVersionController extends BaseController {
    // PDA终端版本页面
    final String index = "app/version";
    final String addOrEdit = "app/versionAddOrEdit";

    @Resource
    IAppVersionService appVersionService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取PDA终端版本列表信息")
    @RequestMapping("list")
    public String getAppVersion(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(appVersionService.findPageInfo(filter, page));
    }

    @Journal(name = "添加PDA终端版本页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(AppVersion appVersion) {
        return new ModelAndView(addOrEdit, model.addAttribute("appVersion", appVersion));
    }

    @ResponseBody
    @Journal(name = "保存PDA终端版本", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(AppVersion appVersion) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("version", appVersion.getVersion());
        if (appVersionService.isExist(AppVersion.class, param, true)) {
            return ajaxError("版本号[" + appVersion.getVersion() + "]已存在");
        }
        appVersionService.save(appVersion);
        return GsonTools.toJson(appVersion);
    }

    @ResponseBody
    @Journal(name = "删除PDA终端版本", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        appVersionService.delete(AppVersion.class, ids);
        return Constant.AJAX_SUCCESS;
    }

    @NoLogin
    @Journal(name = "APP下载", logType = LogType.CONSOLE)
    @RequestMapping(value = "down.apk", method = RequestMethod.GET)
    public ModelAndView update() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("isLatest", 1);
        AppVersion ver = appVersionService.findUniqueByMap(AppVersion.class, param);
        if (ver == null) {
            throw new Exception("尚无版本信息");
        } else {
            return new ModelAndView("redirect:" + basePath + ver.getPath());
        }
    }

    @NoLogin
    @ResponseBody
    @Journal(name = "APP更新", logType = LogType.CONSOLE)
    @RequestMapping(value = "update", method = RequestMethod.GET)
    public String update(String version) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("isLatest", 1);
        AppVersion ver = appVersionService.findUniqueByMap(AppVersion.class, param);
        param.clear();
        param.put("hasNew", false);
        if (ver != null) {
            if (!version.equals(ver.getVersion())) {
                param.clear();
                param.put("hasNew", true);
                param.put("url", basePath + ver.getPath());
            }
        }
        return GsonTools.toJson(param);
    }
}