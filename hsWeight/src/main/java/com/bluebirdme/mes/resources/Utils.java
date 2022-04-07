/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.resources;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Dev工具类
 *
 * @author Goofy
 * @Date 2016年4月6日 下午12:33:52
 */
public class Utils {
    private static final String CHARSET = "UTF-8";

    private static Configuration getFreemarkerConfig() throws IOException {
        @SuppressWarnings("deprecation")
        Configuration config = new Configuration();
        config.setClassForTemplateLoading(Utils.class, "/com/bluebirdme/mes/resources/");
        config.setEncoding(Locale.CHINA, "utf-8");
        return config;
    }

    public static String generateToString(String ftlName, Object data) throws IOException {
        try {
            Template tpl = getFreemarkerConfig().getTemplate(ftlName);
            tpl.setEncoding(CHARSET);

            String html = null;
            StringWriter writer = new StringWriter();

            tpl.process(data, writer);
            writer.flush();
            html = writer.toString();
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    public static String firstCharToLowerCase(String str) {
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            char[] cs = str.toCharArray();
            cs[0] += 32;
            return String.valueOf(cs);
        }
    }
}
