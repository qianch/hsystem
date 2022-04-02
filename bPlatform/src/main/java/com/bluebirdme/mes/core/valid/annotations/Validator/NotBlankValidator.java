package com.bluebirdme.mes.core.valid.annotations.Validator;

import com.bluebirdme.mes.core.valid.ValidException;
import com.bluebirdme.mes.core.valid.Validator;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class NotBlankValidator implements Validator {
    public NotBlankValidator() {
    }

    public Class<?>[] supportClass() {
        return new Class[]{String.class};
    }

    public <T> void doValid(T t, String desc, Object annotation) throws ValidException {
        String str = (String) t;
        String e = "不能是空或者空格";
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (Character.isWhitespace(str.charAt(i))) {
                    throw new ValidException(desc + e);
                }
            }
        } else {
            throw new ValidException(desc + e);
        }
    }
}
