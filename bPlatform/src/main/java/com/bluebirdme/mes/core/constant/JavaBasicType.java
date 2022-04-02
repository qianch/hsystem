package com.bluebirdme.mes.core.constant;

import com.alibaba.druid.sql.visitor.functions.Char;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public enum JavaBasicType {
    /**
     * 整数
     */
    INT(Integer.class),
    SHORT(Short.class),
    LONG(Long.class),
    CHAR(Char.class),
    STRING(String.class),
    BYTE(Byte.class),
    FLOAT(Float.class),
    DOUBLE(Double.class);

    private Class<?> clazz;

    private JavaBasicType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getValue() {
        return this.clazz;
    }
}
