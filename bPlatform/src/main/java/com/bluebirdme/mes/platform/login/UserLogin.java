package com.bluebirdme.mes.platform.login;
/**
 * @author qianchen
 * @date 2020/05/21
 */

import com.bluebirdme.mes.core.constant.RuntimeVariable;
import com.bluebirdme.mes.core.exception.BusinessException;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.service.IUserService;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.xdemo.superutil.j2se.MD5Utils;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public abstract class UserLogin extends AbstractLogin {
    public IUserService userService;
    public User user;
    public List<Map<String, Object>> menus;
    public List<Map<String, Object>> buttons;
    public List<String> urls;

    public UserLogin(String loginName, String password, String successUrl, HttpSession session, IUserService userService, String language) {
        super(loginName, password, successUrl, session, language);
        this.userService = userService;
    }

    @Override
    public void doLogin() throws BusinessException, NoSuchAlgorithmException {
        Locale loc = new Locale(this.language);
        this.session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, loc);
        Map<String, Object> params = new HashMap();
        params.put("loginName", this.loginName);
        String md5_password = MD5Utils.getStringMD5(this.password, "6d02d09506f651a26bdc3fef63494e5b");
        this.user = this.userService.findUniqueByMap(User.class, params);
        if (this.user == null || !this.user.getPassword().equals(md5_password)) {
            this.error("用户名或者密码错误");
        }

        if (this.user.getStatus() == -1) {
            this.error("用户已被禁用");
        }

        this.menus = this.userService.getMenuPermissions(this.user.getId());
        this.buttons = this.userService.getButtonPermissions(this.user.getId());
        this.urls = this.userService.getUrlPermissions(this.user.getId());
        this.success();
    }

    @Override
    public void success() {
        this.session.setAttribute("userId", this.user.getId());
        this.session.setAttribute("userName", this.user.getUserName());
        this.session.setAttribute("loginName", this.user.getLoginName());
        this.setPerssion();
    }

    public void setPerssion() {
        Map<String, List<String>> menuToButtons = new HashMap();
        Iterator iterator = this.menus.iterator();

        while (iterator.hasNext()) {
            Map<String, Object> next = (Map) iterator.next();
            String menuUrl = next.get("url") + "";
            String menuId = next.get("id") + "";
            List<String> buttonIds = new ArrayList();

            for (int i = this.buttons.size() - 1; i >= 0; --i) {
                Map buttonMap = this.buttons.get(i);
                String buttonMid = buttonMap.get("parentId") + "";
                String buttonCode = buttonMap.get("buttonCode") + "";
                if (buttonMid.equals(menuId)) {
                    buttonIds.add(buttonCode);
                }
            }

            menuToButtons.put(menuUrl, buttonIds);
        }

        this.session.setAttribute("currentRuntimeVersion", RuntimeVariable.RUNTIME_VERSION);
        this.session.setAttribute("url", this.urls);
        this.session.setAttribute("button", menuToButtons);
    }
}
