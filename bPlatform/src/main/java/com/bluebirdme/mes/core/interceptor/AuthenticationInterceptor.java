package com.bluebirdme.mes.core.interceptor;


import com.bluebirdme.mes.core.annotation.NoLogin;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    String base = null;
    String loginPath = null;
    String forbidden = null;
    String ajaxPath = null;
    boolean isAjax = false;

    public AuthenticationInterceptor() {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.base = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        this.loginPath = this.base + "login";
        this.forbidden = this.base + "error/403";
        this.ajaxPath = this.base + "error/ajaxError";
        this.isAjax = null != request.getHeader("X-Requested-With");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        NoLogin noLogin = method.getAnnotation(NoLogin.class);
        if (noLogin != null) {
            return true;
        }

        if (request.getSession().getAttribute("userId") != null) {
            return true;
        }

        if (this.isAjax) {
            request.getRequestDispatcher("/error/expired").forward(request, response);
        } else {
            response.sendRedirect(this.loginPath);
        }

        return false;
    }
}
