package com.bluebirdme.mes.core.valid.annotations.Validator;

import com.bluebirdme.mes.core.valid.ValidException;
import com.bluebirdme.mes.core.valid.Validator;
import com.bluebirdme.mes.core.valid.annotations.Length;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public class LengthValidator implements Validator {
    public LengthValidator() {
    }

    public Class<?>[] supportClass() {
        return new Class[]{String.class};
    }

    public <T> void doValid(T t, String desc, Object annotation) throws ValidException {
        Length len = (Length) annotation;
        if (t != null) {
            if (len.max() != -1 && ((String) t).length() > len.max()) {
                throw new ValidException(desc + "长度不能超过" + len.max());
            }

            if (len.min() != -1 && ((String) t).length() < len.min()) {
                throw new ValidException(desc + "长度小于" + len.min());
            }
        }

    }
}
