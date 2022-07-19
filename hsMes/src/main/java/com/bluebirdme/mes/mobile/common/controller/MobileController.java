package com.bluebirdme.mes.mobile.common.controller;

import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.common.service.ICommonService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.mobile.base.MobileBaseController;
import com.bluebirdme.mes.mobile.common.entity.AppVersion;
import com.bluebirdme.mes.mobile.common.service.IAppVersionService;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.service.IUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xdemo.superutil.j2se.MD5Utils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Goofy
 * @Date 2016年10月28日 上午9:24:57
 */
@RestController
@RequestMapping("/mobile")
public class MobileController extends MobileBaseController {
    @Resource
    IUserService userService;
    @Resource
    IAppVersionService appVersionService;
    @Resource
    ICommonService commonService;
    @Resource
    IAuditInstanceService auditService;

    @NoLogin
    @Journal(name = "PDA用户登陆", logType = LogType.DB)
    @RequestMapping("login")
    public String login(String userName, String password) throws NoSuchAlgorithmException {
        Map<String, Object> param = new HashMap();
        param.put("loginName", userName);
        param.put("password", MD5Utils.getStringMD5(password, Constant.MD5_SALT));
        User user = userService.findUniqueByMap(User.class, param);
        if (user == null) {
            //return ajaxError("用户名或者密码错误");
            return GsonTools.toJson("error");
        } else {
            session.setAttribute(Constant.CURRENT_USER_ID, user.getId());
            session.setAttribute(Constant.CURRENT_USER_NAME, user.getUserName());
            session.setAttribute(Constant.CURRENT_USER_LOGINNAME, user.getLoginName());
            Department dept = userService.findById(Department.class, user.getDid());
            user.setT_1(dept.getName());
            return GsonTools.toJson(user);
        }
    }

    @NoLogin
    @ResponseBody
    @Journal(name = "APP更新", logType = LogType.CONSOLE)
    @RequestMapping(value = "update", method = RequestMethod.GET)
    public String update(String version) throws Exception {
        Map<String, Object> param = new HashMap();
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

    @NoLogin
    @RequestMapping("msg")
    public String getMsg() {
        Map<String, Object> map = new HashMap();
        map.put("notice", commonService.getMsg());
        Filter filter = new Filter();
        filter.getFilter().put("uid", session.getAttribute(Constant.CURRENT_USER_ID) + "");
        map.put("task", auditService.auditTask(filter, new Page()));
        return GsonTools.toJson(map);
    }
}
