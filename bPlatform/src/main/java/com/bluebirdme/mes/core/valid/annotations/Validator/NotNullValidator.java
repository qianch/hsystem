package com.bluebirdme.mes.core.valid.annotations.Validator;


import com.bluebirdme.mes.core.valid.ValidException;
import com.bluebirdme.mes.core.valid.Validator;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class NotNullValidator implements Validator {
    public NotNullValidator() {
    }

    @Override
    public Class<?>[] supportClass() {
        return new Class[0];
    }

    @Override
    public <T> void doValid(T t, String desc, Object annotation) throws ValidException {
        if (t == null) {
            throw new ValidException(desc + "不能为空");
        }
    }
}
