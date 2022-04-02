package com.bluebirdme.mes.core.spring;

import com.alibaba.druid.pool.DruidDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import javax.servlet.ServletContext;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class SpringCtx extends WebApplicationObjectSupport implements ServletContextAware {
    private static ApplicationContext applicationContext = null;
    private static SessionFactory sessionFactory = null;
    private DruidDataSource dataSource = null;

    public SpringCtx() {
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        SpringCtx.sessionFactory = sessionFactory;
    }

    public void setDataSource(DruidDataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected void initServletContext(ServletContext servletContext) {
        super.initServletContext(servletContext);
    }

    protected void initApplicationContext(ApplicationContext context) throws BeansException {
        super.initApplicationContext(context);
        if (applicationContext == null) {
            applicationContext = context;
        }
    }

    public static ApplicationContext getSpringContext() {
        return applicationContext;
    }

    public static Object getBean(String id) {
        return getSpringContext().getBean(id);
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public DruidDataSource getDataSource() {
        return this.dataSource;
    }
}
