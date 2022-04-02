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

    private static LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
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
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();

        Method method = ms.getMethod();
        Journal journal = method.getAnnotation(Journal.class);
        String userName = session.getAttribute(Constant.CURRENT_USER_NAME) == null ? "" : session.getAttribute(Constant.CURRENT_USER_NAME).toString();
        String loginName = userName.equals("") ? "-" : session.getAttribute(Constant.CURRENT_USER_LOGINNAME).toString();
        Long userId = userName.equals("") ? -999 : (Long) session.getAttribute(Constant.CURRENT_USER_ID);

        // 检查是否开启记录用户日志，对于要求记录在数据库的日志，要写入到日志表中
        StringBuffer buffer = new StringBuffer();
        if (RuntimeVariable.USER_LOG && journal.logType() == LogType.DB) {
            Log log = new Log();
            String beanName = StringUtils.firstCharToLowerCase(clazz.getSimpleName());
            HandlerMethod hm = new HandlerMethod(beanName, beanFactory, method);
            StringBuffer params = new StringBuffer();
            for (int i = 0; i < hm.getMethodParameters().length; i++) {
                hm.getMethodParameters()[i].initParameterNameDiscovery(discoverer);
                params.append((i == 0 ? "" : ",") + hm.getMethodParameters()[i].getParameterName());
            }
            log.setLogDate(new Date());
            log.setUserName(userName);
            log.setLoginName(loginName);
            log.setOperate(journal.name());
            log.setParams(params.toString());
            log.setUserId(userId);
            log.setParamsValue(GsonTools.toJson(os));
            String IP = request.getHeader("X-Real-IP");
            if (IP == null || IP.length() == 0 || "unknown".equalsIgnoreCase(IP)) {
                IP = request.getRemoteAddr();
            }
            log.setIp(IP);

            buffer.setLength(0);
            buffer.append("AppVersion:");
            buffer.append(request.getHeader("AppVersion"));
            buffer.append("|UserID:");
            buffer.append(request.getHeader("UserID"));
            buffer.append("|User-Agent:");
            buffer.append(request.getHeader("User-Agent"));
            log.setRequestIdentity(buffer.toString());
            logService.save(log);
        }

        buffer.setLength(0);
        buffer.append("\n[用户]\t");
        buffer.append(userName);
        buffer.append("\n[操作]\t");
        buffer.append(journal.name());
        buffer.append("\n[地址]\t");
        buffer.append(request.getRequestURL().toString());
        buffer.append("\n[后台]\t");
        buffer.append(clazz.getName());
        buffer.append(".");
        buffer.append(method.getName() + "\n");
        logger.debug(buffer.toString());
    }
}
