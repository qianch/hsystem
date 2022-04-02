package com.bluebirdme.mes.core.exception;

import com.bluebirdme.mes.core.base.LanguageProvider;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class ExceptionParser {
    public ExceptionParser() {
    }

    public static String getMessage(Exception e) {
        return LanguageProvider.getMessage(e.getClass().getSimpleName()) + ":" + e.getMessage();
    }
}
