package com.bluebirdme.mes.core.valid.annotations.Validator;

import com.bluebirdme.mes.core.valid.ValidException;
import com.bluebirdme.mes.core.valid.Validator;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class NoSpecialCharValidator implements Validator {
    private static final String SPECIALCHAR = "[一-龥]*[a-z]*[A-Z]*\\d*-*_*\\s*";

    public NoSpecialCharValidator() {
    }

    @Override
    public Class<?>[] supportClass() {
        return new Class[]{String.class};
    }

    @Override
    public <T> void doValid(T t, String desc, Object annotation) throws ValidException {
        if (t != null && ((String) t).replaceAll("[一-龥]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
            throw new ValidException(desc + "不能包含特殊字符");
        }
    }
}
