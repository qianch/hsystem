package com.bluebirdme.mes.mobile.base;

import com.bluebirdme.mes.core.base.controller.DateEditor;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Goofy
 * @Date 2016年10月28日 上午9:22:51
 */
public class MobileBaseController {
    private static final Logger log = LoggerFactory.getLogger(MobileBaseController.class);
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    protected String basePath;
    protected String projectRootPath;
    protected Boolean isAjax = false;
    protected String remoteIp;
    protected String requestPath;
    protected ModelMap model;

    @Resource
    IExceptionMessageService exceptionService;

    @SuppressWarnings("deprecation")
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
        this.projectRootPath = request.getRealPath("/");
        if (null != request.getHeader("X-Requested-With")) {
            this.isAjax = true;
        }
        this.remoteIp = request.getRemoteAddr();
        this.requestPath = request.getRequestURL().toString();
        this.model = model;
        this.basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        this.model = model;
    }

    // 注册数据转换器
    @InitBinder
    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    @ExceptionHandler
    public ModelAndView exception(HttpServletRequest request, Exception ex) {
        try {
            log.error(ex.getLocalizedMessage(), ex);
            String exceptionMsg = ex.getCause() == null ? ex.getMessage() : (ex.getCause() + "");
            if (ex instanceof NullPointerException) {
                exceptionMsg = ex.getClass().getName() + ":空指针异常";
            }
            model.addAttribute("error", exceptionMsg);
            StackTraceElement[] elements = ex.getStackTrace();
            // 检查是否开启系统日志
            if (RuntimeVariable.EXCEPTION_LOG) {
                ExceptionMessage msg = new ExceptionMessage();
                for (StackTraceElement e : elements) {
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
            if (null != request.getHeader("X-Requested-With")) {
                return new ModelAndView("forward:/error/ajaxError", model);
            }
            return new ModelAndView("forward:/error/500", model);
        } catch (Exception e) {
            return null;
        }
    }

    public String ajaxError(String msg) {
        try {
            PrintWriter writer = response.getWriter();
            writer.println(error(msg));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public String error(String msg) {
        return "{\"error\":\"" + msg + "\"}";
    }

    public String ajaxSuccess() {
        return "{}";
    }
}
