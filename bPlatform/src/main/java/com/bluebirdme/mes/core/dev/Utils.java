package com.bluebirdme.mes.core.dev;


import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public Utils() {
    }

    private static Configuration getFreemarkerConfig() throws IOException {
        Configuration config = new Configuration();
        config.setClassForTemplateLoading(Utils.class, "/template/");
        config.setEncoding(Locale.CHINA, "utf-8");
        return config;
    }

    public static void generateToFile(String ftlName, String outputFile, Object data) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputFile);
        try (fos; OutputStreamWriter out = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            Template tpl = getFreemarkerConfig().getTemplate(ftlName);
            tpl.setEncoding("UTF-8");
            tpl.process(data, out);
            out.flush();
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }
    }

    public static String firstCharToLowerCase(String str) {
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            char[] cs = str.toCharArray();
            cs[0] = (char) (cs[0] + 32);
            return String.valueOf(cs);
        }
    }
}
