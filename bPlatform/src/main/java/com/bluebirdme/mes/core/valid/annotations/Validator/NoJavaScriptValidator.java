package com.bluebirdme.mes.core.valid.annotations.Validator;

import com.bluebirdme.mes.core.valid.ValidException;
import com.bluebirdme.mes.core.valid.Validator;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class NoJavaScriptValidator implements Validator {
    private static final String SCRIPT = "<script[^>]*?>[\\s\\S]*?<\\/script>";

    public NoJavaScriptValidator() {
    }

    public Class<?>[] supportClass() {
        return new Class[]{String.class};
    }

    public <T> void doValid(T t, String desc, Object annotation) throws ValidException {
        if (t != null && ((String) t).matches("<script[^>]*?>[\\s\\S]*?<\\/script>")) {
            throw new ValidException(desc + "不能含有JavaScript标签");
        }
    }
}
