package com.bluebirdme.mes.core.annotation.support;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public enum RegexType {
    /**
     * 不处理
     */
    NONE,

    /**
     * 特殊字符
     */
    SPECIALCHAR,

    /**
     * 电子邮件
     */
    EMAIL,

    IP,
    
    NUMBER,

    /**
     * 电话号码
     */
    PHONENUMBER;
}
