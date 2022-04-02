package com.bluebirdme.mes.core.sql;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class SQLTemplateException extends Exception {
    private static final long serialVersionUID = 1L;

    public SQLTemplateException() {
    }

    public SQLTemplateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SQLTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLTemplateException(String message) {
        super(message);
    }

    public SQLTemplateException(Throwable cause) {
        super(cause);
    }
}
