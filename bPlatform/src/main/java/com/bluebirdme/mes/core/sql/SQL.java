package com.bluebirdme.mes.core.sql;

import com.bluebirdme.mes.core.constant.RuntimeVariable;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdemo.superutil.j2se.MapUtils;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public class SQL {
    private static Logger log = LoggerFactory.getLogger(SQL.class);
    public static String[] IGNORE_KEYS = new String[]{"sort", "order", "_language"};
    private static Map<String, String> sql = new HashMap();
    private static Configuration cfg;

    public SQL() {
    }

    public static String get(String id) {
        if (RuntimeVariable.DEBUG) {
            log.debug("[SQL] " + sql.get(id));
        }
        return sql.get(id);
    }

    public static <T> String get(Map<String, T> params, String id) throws SQLTemplateException {
        try {
            if (cfg == null) {
                cfg = new Configuration();
                cfg.setDefaultEncoding("UTF-8");
            }
            StringTemplateLoader tplLoader = new StringTemplateLoader();
            tplLoader.putTemplate(id, sql.get(id));
            cfg.setTemplateLoader(tplLoader);
            Template tpl = cfg.getTemplate(id);
            Map<String, Object> map = new HashMap();
            map.put("key", params);
            StringWriter writer = new StringWriter();
            tpl.process(map, writer);
            return writer.toString();
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
            throw new SQLTemplateException(ex);
        }
    }

    public static <T> String get(Map<String, T> params, String id, String[] isNotParameter) throws SQLTemplateException {
        MapUtils.removeKey(params, isNotParameter);
        return get(params, id);
    }

    public static void setSql(Map<String, String> sql) {
        SQL.sql = sql;
    }
}
