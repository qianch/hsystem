package com.bluebirdme.mes.core.valid.annotations.Validator;

import com.bluebirdme.mes.core.valid.ValidException;
import com.bluebirdme.mes.core.valid.Validator;

import java.util.Date;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class PastValidator implements Validator {
    public PastValidator() {
    }

    @Override
    public Class<?>[] supportClass() {
        return new Class[]{Date.class};
    }

    @Override
    public <T> void doValid(T t, String desc, Object annotation) throws ValidException {
        Date date = (Date) t;
        Date now = new Date();
        if (date.after(now)) {
            throw new ValidException(desc + "必须在" + now.toLocaleString() + "之前");
        }
    }
}
