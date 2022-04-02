package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.constant.RuntimeVariable;
import com.bluebirdme.mes.core.listener.SessionListenerImpl;
import com.bluebirdme.mes.core.properties.SystemProperties;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.service.ISystemInfoService;
import com.bluebirdme.mes.platform.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.xdemo.superutil.j2se.MD5Utils;
import org.xdemo.superutil.j2se.MapUtils;
import org.xdemo.superutil.j2se.PropertiesUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.*;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Journal(name = "登录模块")
@Controller
@RequestMapping({"/login"})
public class LoginController extends BaseController {
    @Resource
    IUserService userService;
    @Resource
    ISystemInfoService systemInfo;
    private List<Map<String, Object>> menus;
    private List<Map<String, Object>> buttons;

    public LoginController() {
    }

    @Journal(name = "跳转登录页面")
    @RequestMapping(method = {RequestMethod.GET})
    public String login() {
        return "login";
    }

    @Valid
    @Journal(name = "用户登录", logType = LogType.DB)
    @RequestMapping(method = {RequestMethod.POST})
    public ModelAndView doLogin(String loginName, String password, @RequestParam String locale) throws Exception {
        this.session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.forLanguageTag(locale));
        if (loginName.equalsIgnoreCase("administrator")) {
            SystemProperties system = new SystemProperties();
            if (MD5Utils.getStringMD5(password, "6d02d09506f651a26bdc3fef63494e5b").equalsIgnoreCase(system.getAsString("default_user_password") == null ? "" : system.getAsString("default_user_password"))) {
                if (SessionListenerImpl.hasLogin(loginName)) {
                    SessionListenerImpl.forceInvalidate(loginName);
                    session = request.getSession(true);
                }
                session.setAttribute("userId", -1L);
                session.setAttribute("userName", "administrator");
                session.setAttribute("loginName", "administrator");
                SessionListenerImpl.putSession(session);
                request.getServletContext().setAttribute("user_count", systemInfo.findUserCount());
                request.getServletContext().setAttribute("sys_version", system.getAsString("sys_version"));
                request.getServletContext().setAttribute("version", system.getAsString("version"));
                return new ModelAndView("redirect:/");
            }
        }

        User user;
        Map<String, Object> params = new HashMap();
        params.put("loginName", loginName);
        String md5_password = MD5Utils.getStringMD5(password, "6d02d09506f651a26bdc3fef63494e5b");
        user = userService.findUniqueByMap(User.class, params);
        if (user != null && user.getPassword().equals(md5_password)) {
            if (user.getStatus() == -1) {
                return (new ModelAndView("login")).addObject("error", "该账户已被禁用");
            }

            if (SessionListenerImpl.hasLogin(loginName)) {
                SessionListenerImpl.forceInvalidate(loginName);
                session = request.getSession(true);
            }
            menus = userService.getMenuPermissions(user.getId());
            buttons = userService.getButtonPermissions(user.getId());
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getUserName());
            session.setAttribute("loginName", user.getLoginName());
            SessionListenerImpl.putSession(session);
            setPerssion();
            int onlineCount = (Integer) (request.getServletContext().getAttribute("online_count") == null ? 0 : request.getServletContext().getAttribute("online_count"));
            ServletContext servletContext = request.getServletContext();
            ++onlineCount;
            servletContext.setAttribute("online_count", onlineCount);
            request.getServletContext().setAttribute("user_count", systemInfo.findUserCount());
            String rootPath = getClass().getResource("/").getFile();
            request.getServletContext().setAttribute("sys_version", PropertiesUtils.readProperties(rootPath + "system.properties").getProperty("sys_version"));
            request.getServletContext().setAttribute("version", PropertiesUtils.readProperties(rootPath + "system.properties").getProperty("version"));
            return new ModelAndView("redirect:/");
        }

        return (new ModelAndView("login")).addObject("error", "用户名或者密码不正确");
    }

    public void setPerssion() {
        Map<String, List<String>> menuToButtons = new HashMap();
        List<String> buttonIds;
        String menuUrl;
        String menuId;
        String buttonCode;
        String buttonMid;
        Iterator iterator = this.menus.iterator();

        while (iterator.hasNext()) {
            Map<String, Object> m = (Map) iterator.next();
            new ArrayList();
            menuUrl = MapUtils.getAsStringIgnoreCase(m, "URL") + "";
            menuId = MapUtils.getAsStringIgnoreCase(m, "ID") + "";
            buttonIds = new ArrayList();

            for (int i = this.buttons.size() - 1; i >= 0; --i) {
                Map<String, Object> buttonMap = (Map) this.buttons.get(i);
                buttonMid = MapUtils.getAsStringIgnoreCase(buttonMap, "parentId") + "";
                buttonCode = MapUtils.getAsStringIgnoreCase(buttonMap, "buttonCode") + "";
                if (buttonMid.equals(menuId)) {
                    buttonIds.add(buttonCode);
                }
            }
            menuToButtons.put(menuUrl, buttonIds);
        }
        session.setAttribute("currentRuntimeVersion", RuntimeVariable.RUNTIME_VERSION);
        session.setAttribute("button", menuToButtons);
    }

    @Journal(name = "用户退出", logType = LogType.DB)
    @RequestMapping(value = {"logout"}, method = {RequestMethod.GET})
    public ModelAndView doLogout() {
        SessionListenerImpl.forceInvalidate((String) this.session.getAttribute("loginName"));
        return new ModelAndView("redirect:/login");
    }
}
