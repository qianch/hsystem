package com.bluebirdme.mes.core.valid.annotations.Validator;

import com.bluebirdme.mes.core.valid.ValidException;
import com.bluebirdme.mes.core.valid.Validator;
import com.bluebirdme.mes.core.valid.annotations.Regex;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class RegexValidator implements Validator {
    public RegexValidator() {
    }

    @Override
    public Class<?>[] supportClass() {
        return new Class[]{String.class};
    }

    @Override
    public <T> void doValid(T t, String desc, Object annotation) throws ValidException {
        if (t != null && t instanceof String && !((String) t).matches(((Regex) annotation).value())) {
            throw new ValidException(desc + "不符合要求");
        }
    }
}
