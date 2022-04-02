package com.bluebirdme.mes.core.sql;


import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.xdemo.superutil.j2se.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class SQLBuilder {
    public SQLBuilder() {
    }

    public static String buildConditions(boolean preWhere, String rule, Filter filter, Page page) throws Exception {
        StringBuilder builder = new StringBuilder(preWhere ? " where 1=1 " : " ");
        JsonObject json = (new JsonParser()).parse(rule).getAsJsonObject();
        JsonObject andCondition = json.getAsJsonObject("and");
        JsonObject orCondition = json.getAsJsonObject("or");

        if (json.get("order") != null) {
            json.get("order").getAsString();
        }

        if (json.get("sort") != null) {
            json.get("sort").getAsString();
        }

        JsonElement groupCondition = json.get("group");
        if (andCondition != null) {
            Iterator it = andCondition.entrySet().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                String key = (String) entry.getKey();
                String operator = ((JsonElement) entry.getValue()).getAsString();
                if (operator.equalsIgnoreCase("between")) {
                    if (StringUtils.isBlank(filter.get("start" + key)) && StringUtils.isBlank(filter.get("end" + key))) {
                        continue;
                    }

                    if (StringUtils.isBlank(filter.get("start" + key)) && !StringUtils.isBlank(filter.get("end" + key))) {
                        builder.append(" and " + key + "<=:end" + key);
                        continue;
                    }

                    if (!StringUtils.isBlank(filter.get("start" + key)) && StringUtils.isBlank(filter.get("end" + key))) {
                        builder.append(" and " + key + ">=:start" + key);
                        continue;
                    }
                } else {
                    if (operator.equalsIgnoreCase("isnull")) {
                        builder.append(" and " + key + " is null ");
                        continue;
                    }

                    if (operator.equalsIgnoreCase("isnotnull")) {
                        builder.append(" and " + key + " is not null ");
                        continue;
                    }

                    if (StringUtils.isBlank(filter.get(key))) {
                        continue;
                    }
                }

                builder.append(" and (" + getOperator(operator, key) + ")");
            }
        }

        if (orCondition != null) {
            Iterator it = orCondition.entrySet().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                String key = (String) entry.getKey();
                String operator = ((JsonElement) entry.getValue()).getAsString();
                if (operator.equalsIgnoreCase("between")) {
                    if (StringUtils.isBlank(filter.get("start" + key)) && StringUtils.isBlank(filter.get("end" + key))) {
                        continue;
                    }

                    if (StringUtils.isBlank(filter.get("start" + key)) && !StringUtils.isBlank(filter.get("end" + key))) {
                        builder.append(" or " + key + "<=:end" + key);
                        continue;
                    }

                    if (!StringUtils.isBlank(filter.get("start" + key)) && StringUtils.isBlank(filter.get("end" + key))) {
                        builder.append(" or " + key + ">=:start" + key);
                        continue;
                    }
                } else {
                    if (operator.equalsIgnoreCase("isnull")) {
                        builder.append(" or " + key + " is null ");
                        continue;
                    }

                    if (operator.equalsIgnoreCase("isnotnull")) {
                        builder.append(" or " + key + " is not null ");
                        continue;
                    }

                    if (StringUtils.isBlank(filter.get(key))) {
                        continue;
                    }
                }

                builder.append(" or (" + getOperator(operator, key) + ")");
            }
        }

        if (groupCondition != null) {
            builder.append(" group  by " + groupCondition.getAsString());
        }

        return builder.toString();
    }

    public static String buildConditions(boolean preWhere, String rule, Filter filter) throws Exception {
        StringBuilder builder = new StringBuilder(preWhere ? " where 1=1 " : " ");
        JsonObject json = (new JsonParser()).parse(rule).getAsJsonObject();
        JsonObject andCondition = json.getAsJsonObject("and");
        JsonObject orCondition = json.getAsJsonObject("or");
        String order = json.get("order") == null ? "" : json.get("order").getAsString();
        String sort = json.get("sort") == null ? "" : json.get("sort").getAsString();
        JsonElement groupCondition = json.get("group");

        if (andCondition != null) {
            Iterator it = andCondition.entrySet().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                String key = (String) entry.getKey();
                String operator = ((JsonElement) entry.getValue()).getAsString();
                if (operator.equalsIgnoreCase("between")) {
                    if (StringUtils.isBlank(filter.get("start" + key)) && StringUtils.isBlank(filter.get("end" + key))) {
                        continue;
                    }

                    if (StringUtils.isBlank(filter.get("start" + key)) && !StringUtils.isBlank(filter.get("end" + key))) {
                        builder.append(" and " + key + "<=:end" + key);
                        continue;
                    }

                    if (!StringUtils.isBlank(filter.get("start" + key)) && StringUtils.isBlank(filter.get("end" + key))) {
                        builder.append(" and " + key + ">=:start" + key);
                        continue;
                    }
                } else {
                    if (operator.equalsIgnoreCase("isnull")) {
                        builder.append(" and " + key + " is null ");
                        continue;
                    }

                    if (operator.equalsIgnoreCase("isnotnull")) {
                        builder.append(" and " + key + " is not null ");
                        continue;
                    }

                    if (StringUtils.isBlank(filter.get(key))) {
                        continue;
                    }
                }

                builder.append(" and (" + getOperator(operator, key) + ")");
            }
        }

        if (orCondition != null) {
            Iterator it = orCondition.entrySet().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                String key = (String) entry.getKey();
                String operator = ((JsonElement) entry.getValue()).getAsString();
                if (operator.equalsIgnoreCase("between")) {
                    if (StringUtils.isBlank(filter.get("start" + key)) && StringUtils.isBlank(filter.get("end" + key))) {
                        continue;
                    }

                    if (StringUtils.isBlank(filter.get("start" + key)) && !StringUtils.isBlank(filter.get("end" + key))) {
                        builder.append(" or " + key + "<=:end" + key);
                        continue;
                    }

                    if (!StringUtils.isBlank(filter.get("start" + key)) && StringUtils.isBlank(filter.get("end" + key))) {
                        builder.append(" or " + key + ">=:start" + key);
                        continue;
                    }
                } else {
                    if (operator.equalsIgnoreCase("isnull")) {
                        builder.append(" or " + key + " is null ");
                        continue;
                    }

                    if (operator.equalsIgnoreCase("isnotnull")) {
                        builder.append(" or " + key + " is not null ");
                        continue;
                    }

                    if (StringUtils.isBlank(filter.get(key))) {
                        continue;
                    }
                }

                builder.append(" or (" + getOperator(operator, key) + ")");
            }
        }

        if (!StringUtils.isBlank(filter.get("order")) && !StringUtils.isBlank(filter.get("sort"))) {
            builder.append(" order by " + filter.get("sort") + " " + filter.get("order"));
        }

        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            builder.append(" order by " + order + " " + sort);
        }

        if (groupCondition != null) {
            builder.append(" group  by " + groupCondition.getAsString());
        }

        return builder.toString();
    }

    public static String getOperator(String operator, String key) throws Exception {
        if (operator.equalsIgnoreCase("like")) {
            return key + " like :" + key;
        }

        if (!operator.equalsIgnoreCase("eq") && !operator.equalsIgnoreCase("equal")) {
            if (!operator.equalsIgnoreCase("ne") && !operator.equalsIgnoreCase("notequal")) {
                if (operator.equalsIgnoreCase("lt")) {
                    return key + " <:" + key;
                }
                if (operator.equalsIgnoreCase("gt")) {
                    return key + " >:" + key;
                }
                if (operator.equalsIgnoreCase("in")) {
                    return key + " in (:" + key + ")";
                }
                if (operator.equalsIgnoreCase("notin")) {
                    return key + " not in (:" + key + ")";
                }
                if (operator.equalsIgnoreCase("between")) {
                    return key + " between :start" + key + " and :end" + key;
                }
                throw new Exception("未识别的操作符:" + operator);
            }

            return key + " <>:" + key;
        }

        return key + " =:" + key;
    }

    public static Map<String, String> getKeyOperator(String rule) {
        JsonObject json = (new JsonParser()).parse(rule).getAsJsonObject();
        JsonObject andCondition = json.getAsJsonObject("and");
        JsonObject orCondition = json.getAsJsonObject("or");
        Map<String, String> map = new HashMap();

        if (orCondition != null || andCondition != null) {
            Iterator it = orCondition.entrySet().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                String key = (String) entry.getKey();
                String operator = ((JsonElement) entry.getValue()).getAsString();
                if (operator.equalsIgnoreCase("between")) {
                    map.put("start" + key, operator);
                    map.put("end" + key, operator);
                } else {
                    map.put(key, operator);
                }
            }
        }
        return map;
    }

    public static void main(String[] args) throws Exception {
        String sql = "select a.xxx,b.yyy from A a left join B b on b.id=a.bid";
        String rule = "{and:{a.q:like,a.w:eq,a.e:ne,a.r:between,a.t:in,a.y:isnull,a.u:isnotnull,a.i:notin,a.o:lt,a.p:gt},or:{a.p:gt},group:a.xxx}";
        Filter filter = new Filter();
        filter.set("sort", "a.q");
        filter.set("order", "desc");
        filter.set("starta.r", "1");
        filter.set("enda.r", "4");
        filter.set("a.q", "FFX");
        filter.set("a.t", "1,2,3");
        filter.set("a.i", "2,2,3,3,4");
        filter.set("a.p", "2");
    }
}
