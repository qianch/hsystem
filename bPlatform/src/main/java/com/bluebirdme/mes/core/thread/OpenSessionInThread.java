package com.bluebirdme.mes.core.thread;

import com.bluebirdme.mes.core.spring.SpringCtx;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.InvocationTargetException;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class OpenSessionInThread {
    public OpenSessionInThread() {
    }

    public static void invoke(ThreadContext tc) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Session session = SpringCtx.getSessionFactory().openSession();
        SessionHolder sessionHolder = new SessionHolder(session);
        TransactionSynchronizationManager.bindResource(SpringCtx.getSessionFactory(), sessionHolder);
        tc.run();
        sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(SpringCtx.getSessionFactory());
        SessionFactoryUtils.closeSession(sessionHolder.getSession());
    }
}
