package com.bluebirdme.mes.core.valid.annotations;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public @interface In {
    int[] i() default {};

    boolean[] b() default {};

    String[] s() default {};
}
