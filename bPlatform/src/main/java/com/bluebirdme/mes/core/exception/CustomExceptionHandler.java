package com.bluebirdme.mes.core.exception;

import com.bluebirdme.mes.core.constant.RuntimeVariable;
import com.bluebirdme.mes.core.spring.SpringCtx;
import com.bluebirdme.mes.platform.entity.ExceptionMessage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class CustomExceptionHandler implements HandlerExceptionResolver {
    private static Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    public CustomExceptionHandler() {
    }

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ex.printStackTrace();
        String exception = ex.getCause() == null ? (ex.getMessage() == null ? "未知异常" : ex.getMessage()) : ex.getCause().toString();
        log.error("访问 [" + request.getRequestURI() + "] 发生异常:" + exception);
        StackTraceElement[] stes = ex.getStackTrace();
        if (RuntimeVariable.EXCEPTION_LOG && !(ex instanceof BusinessException) && stes != null && stes.length != 0) {
            ExceptionMessage msg = new ExceptionMessage();
            StackTraceElement ste = stes[0];
            SessionFactory factory = SpringCtx.getSessionFactory();
            Session session = factory.openSession();
            msg.setClazz(ste.getClassName());
            msg.setMethod(ste.getMethodName());
            msg.setLineNumber(ste.getLineNumber());
            msg.setOccurDate(new Date());
            msg.setMsg(exception);
            session.save(msg);
            session.flush();
            SessionFactoryUtils.closeSession(session);
        }

        Map<String, Object> model = new HashMap();
        if (RuntimeVariable.DEBUG) {
            model.put("error", exception);
        } else if (ex instanceof BusinessException) {
            model.put("error", exception);
        } else {
            model.put("error", "系统异常，请联系管理员");
        }

        return null != request.getHeader("X-Requested-With") ? new ModelAndView("error/ajaxError", model) : new ModelAndView("error/500", model);
    }
}
