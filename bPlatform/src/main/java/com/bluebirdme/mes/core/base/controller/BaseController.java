package com.bluebirdme.mes.core.base.controller;

import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.constant.RuntimeVariable;
import com.bluebirdme.mes.core.exception.BusinessException;
import com.bluebirdme.mes.platform.entity.ExceptionMessage;
import com.bluebirdme.mes.platform.service.IExceptionMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public abstract class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    protected String basePath;
    protected String projectRootPath;
    protected Boolean isAjax = false;
    protected String remoteIp;
    protected String requestPath;
    protected ModelMap model;
    protected String language;
    protected Locale locale;

    @Resource
    IExceptionMessageService exceptionService;

    @SuppressWarnings("deprecation")
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.request = request;
        this.response = response;
        this.session = request.getSession(true);
        this.projectRootPath = request.getRealPath("/");
        if (null != request.getHeader("X-Requested-With")) {
            this.isAjax = true;
        }
        this.remoteIp = request.getRemoteAddr();
        this.requestPath = request.getRequestURL().toString();
        this.model = model;

        if (session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME) == null) {
            session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, request.getLocale());
        }
        locale = (Locale) session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);

        if (locale == null) {
            language = "zh_CN";
        } else {
            language = locale.getLanguage();
        }
        // 判断是否是80端口，80端口默认不加端口号
        this.basePath = request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 ? "" : (":" + request.getServerPort())) + request.getContextPath() + "/";
        // 装配访问目标页面的按钮的编码
        Map<String, List<String>> menuToButtons = (Map<String, List<String>>) this.session.getAttribute(Constant.CURRENT_USER_MENU_TO_BUTTONS);
        if (menuToButtons != null) {
            model.addAttribute(Constant.CURRENT_PAGE_BUTTONS, menuToButtons.get(requestPath.replace(basePath, "")));
        }
    }

    // 注册数据转换器
    @InitBinder
    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    @ExceptionHandler
    public ModelAndView exception(HttpServletRequest request, Exception ex) {
        try {
            logger.error(ex.getLocalizedMessage(), ex);
            String exceptionMsg = ex.getCause() == null ? ex.getMessage() : (ex.getCause() + "");
            if (ex instanceof NullPointerException) {
                exceptionMsg = ex.getClass().getName() + ":空指针异常";
            }
            model.addAttribute("error", exceptionMsg);
            StackTraceElement[] stes = ex.getStackTrace();
            // 检查是否开启系统日志
            if (RuntimeVariable.EXCEPTION_LOG) {
                ExceptionMessage msg = new ExceptionMessage();
                for (StackTraceElement e : stes) {
                    if (e.getClassName().startsWith("com.bluebirdme.mes") && e.getLineNumber() != -1) {
                        msg.setClazz(e.getClassName());
                        msg.setMethod(e.getMethodName());
                        msg.setLineNumber(e.getLineNumber());
                        msg.setOccurDate(new Date());
                        msg.setMsg(exceptionMsg);
                    }
                }
                exceptionService.save(msg);
            }
            Map<String, Object> error = new HashMap();
            if (RuntimeVariable.DEBUG) {
                error.put("error", exceptionMsg);
            } else {
                if (ex instanceof BusinessException) {
                    error.put("error", exceptionMsg);
                } else {
                    error.put("error", "系统错误，请联系管理员查看异常信息");
                }
            }
            try {
                if (null != request.getHeader("X-Requested-With")) {
                    return new ModelAndView("error/ajaxError", model);
                }
                return new ModelAndView("error/500", model);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
                return new ModelAndView("error/500");
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public String ajaxError(Object msg) {
        Map<String, Object> map = new HashMap();
        map.put("error", msg);
        return GsonTools.toJson(map);
    }

    public String ajaxSuccess() {
        return "{}";
    }

    public String ajaxSuccess(Object msg) {
        Map<String, Object> map = new HashMap();
        map.put("msg", msg);
        return GsonTools.toJson(map);
    }

    public ModelAndView error(String msg) {
        return new ModelAndView("/error/500").addObject("error", msg);
    }
}
