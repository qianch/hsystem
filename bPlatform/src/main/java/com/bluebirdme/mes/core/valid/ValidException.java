package com.bluebirdme.mes.core.valid;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class ValidException extends Exception {
    private static final long serialVersionUID = 1L;

    public ValidException() {
    }

    public ValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidException(String message) {
        super(message);
    }

    public ValidException(Throwable cause) {
        super(cause);
    }
}
