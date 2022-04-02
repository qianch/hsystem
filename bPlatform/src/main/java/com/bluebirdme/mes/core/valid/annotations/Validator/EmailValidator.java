package com.bluebirdme.mes.core.valid.annotations.Validator;

import com.bluebirdme.mes.core.valid.ValidException;
import com.bluebirdme.mes.core.valid.Validator;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class EmailValidator implements Validator {
    static final String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    public EmailValidator() {
    }

    public Class<?>[] supportClass() {
        return new Class[]{String.class};
    }

    public <T> void doValid(T t, String desc, Object annotation) throws ValidException {
        if (t != null && !((String) t).matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
            throw new ValidException(desc + "邮箱格式不正确");
        }
    }
}
