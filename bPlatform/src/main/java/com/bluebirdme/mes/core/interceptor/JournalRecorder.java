package com.bluebirdme.mes.core.interceptor;


import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.constant.RuntimeVariable;
import com.bluebirdme.mes.platform.entity.Log;
import com.bluebirdme.mes.platform.service.ILogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.xdemo.superutil.j2se.StringUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Aspect
@Component
public class JournalRecorder {
    private static final Logger logger = LoggerFactory.getLogger(JournalRecorder.class);
    @Resource
    ILogService logService;
    @Resource
    BeanFactory beanFactory;

    private static final LocalVariableTableParameterNameDiscoverer DISCOVERER = new LocalVariableTableParameterNameDiscoverer();
    ThreadLocal<Long> time = new ThreadLocal();

    public JournalRecorder() {
    }

    @Before("@annotation(com.bluebirdme.mes.core.annotation.Journal)")
    public void beforeExec(JoinPoint joinPoint) {
        this.time.set(System.currentTimeMillis());
    }

    @After("@annotation(com.bluebirdme.mes.core.annotation.Journal)")
    public void afterExec(JoinPoint joinPoint) {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Class<? extends Object> clazz = joinPoint.getTarget().getClass();
        Object[] os = joinPoint.getArgs();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        HttpSession session = request.getSession();

        Method method = ms.getMethod();
        Journal journal = method.getAnnotation(Journal.class);
        String userName = session.getAttribute(Constant.CURRENT_USER_NAME) == null ? "" : session.getAttribute(Constant.CURRENT_USER_NAME).toString();
        String loginName = "".equals(userName) ? "-" : session.getAttribute(Constant.CURRENT_USER_LOGINNAME).toString();
        Long userId = "".equals(userName) ? -999 : (Long) session.getAttribute(Constant.CURRENT_USER_ID);

        // 检查是否开启记录用户日志，对于要求记录在数据库的日志，要写入到日志表中
        StringBuilder builder = new StringBuilder();
        if (RuntimeVariable.USER_LOG && journal.logType() == LogType.DB) {
            Log log = new Log();
            String beanName = StringUtils.firstCharToLowerCase(clazz.getSimpleName());
            HandlerMethod hm = new HandlerMethod(beanName, beanFactory, method);
            StringBuilder params = new StringBuilder();
            for (int i = 0; i < hm.getMethodParameters().length; i++) {
                hm.getMethodParameters()[i].initParameterNameDiscovery(DISCOVERER);
                params.append(i == 0 ? "" : ",").append(hm.getMethodParameters()[i].getParameterName());
            }
            log.setLogDate(new Date());
            log.setUserName(userName);
            log.setLoginName(loginName);
            log.setOperate(journal.name());
            log.setParams(params.toString());
            log.setUserId(userId);
            log.setParamsValue(GsonTools.toJson(os));
            String ip = request.getHeader("X-Real-IP");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            log.setIp(ip);

            builder.setLength(0);
            builder.append("AppVersion:");
            builder.append(request.getHeader("AppVersion"));
            builder.append("|UserID:");
            builder.append(request.getHeader("UserID"));
            builder.append("|User-Agent:");
            builder.append(request.getHeader("User-Agent"));
            log.setRequestIdentity(builder.toString());
            logService.save(log);
        }

        builder.setLength(0);
        builder.append("\n[用户]\t");
        builder.append(userName);
        builder.append("\n[操作]\t");
        builder.append(journal.name());
        builder.append("\n[地址]\t");
        builder.append(request.getRequestURL().toString());
        builder.append("\n[后台]\t");
        builder.append(clazz.getName());
        builder.append(".");
        builder.append(method.getName()).append("\n");
        logger.debug(builder.toString());
    }
}
