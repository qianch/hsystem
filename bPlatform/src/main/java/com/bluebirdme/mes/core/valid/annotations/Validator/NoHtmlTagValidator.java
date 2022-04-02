package com.bluebirdme.mes.core.valid.annotations.Validator;

import com.bluebirdme.mes.core.valid.ValidException;
import com.bluebirdme.mes.core.valid.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class NoHtmlTagValidator implements Validator {
    private static final String HTML = "<[^>]+>";

    public NoHtmlTagValidator() {
    }

    public Class<?>[] supportClass() {
        return new Class[]{String.class};
    }

    public <T> void doValid(T t, String desc, Object annotation) throws ValidException {
        if (t != null) {
            Pattern p = Pattern.compile("<[^>]+>", 2);
            Matcher m = p.matcher((String) t);
            if (!t.equals(m.replaceAll(""))) {
                throw new ValidException(desc + "不能含有Html标签");
            }
        }
    }
}
