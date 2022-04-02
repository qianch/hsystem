package com.bluebirdme.mes.platform.login;

import com.bluebirdme.mes.core.exception.BusinessException;
import com.bluebirdme.mes.core.properties.SystemProperties;
import org.xdemo.superutil.j2se.MD5Utils;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class AdminLogin extends AbstractLogin {
    public AdminLogin(String loginName, String password, String successUrl, HttpSession session) {
        super(loginName, password, successUrl, session, "en_US");
    }

    @Override
    public void doLogin() throws NoSuchAlgorithmException, BusinessException, IOException {
        SystemProperties properties = new SystemProperties();
        String salt = "6d02d09506f651a26bdc3fef63494e5b";
        String key = "default_user_password";
        if (MD5Utils.getStringMD5(this.password, salt).equalsIgnoreCase(properties.getAsString(key))) {
            this.success();
        } else {
            this.error("用户名或者密码不正确");
        }
    }

    @Override
    public void success() {
        this.session.setAttribute("userId", -1L);
        this.session.setAttribute("userName", "administrator");
        this.session.setAttribute("loginName", "administrator");
    }
}
