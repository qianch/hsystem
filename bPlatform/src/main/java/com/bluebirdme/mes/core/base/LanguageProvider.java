package com.bluebirdme.mes.core.base;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public abstract class LanguageProvider {
    public LanguageProvider() {
    }

    public static Locale getLocale() {
        HttpServletRequest request = getRequest();
        HttpSession session = request.getSession();
        if (session == null) {
            return new Locale("zh", "CN");
        } else {
            Locale locale = (Locale) session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
            return locale;
        }
    }

    public static String getLanguage() {
        Locale local = getLocale();
        return local.getLanguage() + "_" + local.getCountry();
    }

    public static String getMessage(String code) {
        return getMessage(getRequest(), (Object[]) null, code);
    }

    public static String getMessage(String code, Object[] args) {
        return getMessage(getRequest(), args, code);
    }

    public static String getMessage(HttpServletRequest request, Object[] args, String code) {
        WebApplicationContext ac = RequestContextUtils.findWebApplicationContext(request);
        return ac.getMessage(code, args, getLocale());
    }

    private static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
