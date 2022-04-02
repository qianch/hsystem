package com.bluebirdme.mes.core.valid.annotations.Validator;

import com.bluebirdme.mes.core.valid.ValidException;
import com.bluebirdme.mes.core.valid.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdemo.superutil.j2se.PathUtils;

import java.io.File;
import java.util.Date;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class FutureValidator implements Validator {
    private static Logger log = LoggerFactory.getLogger(FutureValidator.class);

    public FutureValidator() {
    }

    public Class<?>[] supportClass() {
        return new Class[]{Date.class};
    }

    public <T> void doValid(T t, String desc, Object annotation) throws ValidException {
        Date date = (Date) t;
        Date now = new Date();
        if (t != null && date.after(now)) {
            throw new ValidException(desc + "必须在" + now.toLocaleString() + "之前");
        }
    }

    public static void main(String[] args) {
        String p = PathUtils.getClassDrivePath(FutureValidator.class);
        String v = "com.bluebirdme.mes.core.valid.annotations.Validator.";
        String k = "com.bluebirdme.mes.core.valid.annotations.";
        File f = new File(p);
        File pa = f.getParentFile();
        File[] vs = f.listFiles();
        File[] as = pa.listFiles();

        for (int i = 0; i < as.length; ++i) {
            log.debug("<entry key=\"" + k + as[i].getName().replace(".class", "") + "\" value=\"" + v + vs[i].getName().replace(".class", "") + "\"  />");
        }
    }
}
