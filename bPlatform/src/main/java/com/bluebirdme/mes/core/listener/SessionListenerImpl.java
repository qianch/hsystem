package com.bluebirdme.mes.core.listener;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class SessionListenerImpl implements HttpSessionListener {
    public static HashMap<String, HttpSession> userSessions = new HashMap();

    public SessionListenerImpl() {
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int onlineCount = (Integer) (se.getSession().getServletContext().getAttribute("online_count") == null ? 0 : se.getSession().getServletContext().getAttribute("online_count"));
        if (se.getSession().getAttribute("userId") != null) {
            ServletContext context = se.getSession().getServletContext();
            --onlineCount;
            context.setAttribute("online_count", onlineCount);
        }
        delSession(se.getSession());
    }

    public static synchronized void delSession(HttpSession session) {
        if (session != null && session.getAttribute("loginName") != null) {
            userSessions.get(session.getAttribute("loginName")).invalidate();
        }
    }

    public static synchronized void putSession(HttpSession session) {
        if (session != null && session.getAttribute("loginName") != null) {
            userSessions.put((String) session.getAttribute("loginName"), session);
        }
    }

    public static synchronized void forceInvalidate(String loginName) {
        try {
            userSessions.get(loginName).invalidate();
        } catch (Exception ex) {
        }
        userSessions.remove(loginName);
    }

    public static synchronized boolean hasLogin(String loginName) {
        return userSessions.get(loginName) != null;
    }
}
