package com.bluebirdme.mes.core.annotation;

import com.bluebirdme.mes.core.annotation.support.RegexType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qianchen
 * @date 2020/05/20
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Valid {
    boolean nullable() default false;

    int maxLength() default 0;

    int minLength() default 0;

    RegexType regexType() default RegexType.NONE;

    String regexExpression() default "";

    String description() default "";
}
