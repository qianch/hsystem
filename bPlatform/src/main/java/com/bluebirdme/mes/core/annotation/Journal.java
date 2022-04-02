package com.bluebirdme.mes.core.annotation;

import com.bluebirdme.mes.core.annotation.support.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qianchen
 * @date 2020/05/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Journal {
    String name() default "";

    LogType logType() default LogType.CONSOLE;
}
