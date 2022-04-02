package com.bluebirdme.mes.core.valid;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public interface Validator {
    Class<?>[] supportClass();

    <T> void doValid(T var1, String var2, Object var3) throws ValidException;
}
