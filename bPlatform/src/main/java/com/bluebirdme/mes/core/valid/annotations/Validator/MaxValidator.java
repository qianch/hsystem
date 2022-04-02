package com.bluebirdme.mes.core.valid.annotations.Validator;

import com.bluebirdme.mes.core.valid.ValidException;
import com.bluebirdme.mes.core.valid.Validator;
import com.bluebirdme.mes.core.valid.annotations.Max;

import java.math.BigDecimal;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public class MaxValidator implements Validator {
    public MaxValidator() {
    }

    public Class<?>[] supportClass() {
        return new Class[]{Integer.class, Short.class, Long.class, Double.class, Float.class, BigDecimal.class};
    }

    public <T> void doValid(T t, String desc, Object annotation) throws ValidException {
        Max m = (Max) annotation;
        if (t != null && (t instanceof Integer || t instanceof Short || t instanceof Long || t instanceof Double || t instanceof Float || t instanceof BigDecimal) && m.value() < Double.valueOf(t.toString())) {
            throw new ValidException(desc + "不能大于" + m.value());
        }
    }
}
