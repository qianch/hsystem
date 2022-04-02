package com.bluebirdme.mes.core.exception;


import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class BusinessException extends Exception {
    private static final long serialVersionUID = 1L;

    public BusinessException() {
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public String getMessage() {
        return super.getMessage();
    }

    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    public synchronized Throwable getCause() {
        return super.getCause();
    }

    public synchronized Throwable initCause(Throwable cause) {
        return super.initCause(cause);
    }

    public String toString() {
        return super.toString();
    }

    public void printStackTrace() {
        super.printStackTrace();
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
    }

    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
    }

    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }

    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    public void setStackTrace(StackTraceElement[] stackTrace) {
        super.setStackTrace(stackTrace);
    }
}
