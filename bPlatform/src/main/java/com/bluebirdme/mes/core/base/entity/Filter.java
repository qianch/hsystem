package com.bluebirdme.mes.core.base.entity;

import com.bluebirdme.mes.core.base.LanguageProvider;
import org.xdemo.superutil.j2se.MapUtils;
import org.xdemo.superutil.j2se.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class Filter {
    private Map<String, String> filter = new HashMap<>();
    private String sort;
    private String order;

    public Filter() {
    }

    public Map<String, String> getFilter() {
        return this.filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    public String get(String key) {
        filter.put("_language", LanguageProvider.getLanguage());
        return this.filter.get(key) == null ? null : StringUtils.trim(filter.get(key));
    }

    public void set(String key, String value) {
        filter.put(key, StringUtils.isBlank(value) ? null : StringUtils.trim(value));
    }

    public void clear() {
        Iterator<Entry<String, String>> it = filter.entrySet().iterator();
        Entry<String, String> entry;
        ArrayList<Object> keyList = new ArrayList<>();
        while (it.hasNext()) {
            entry = it.next();
            if (StringUtils.isBlank(entry.getValue())) {
                keyList.add(entry.getKey());
            }
        }
        MapUtils.removeKey(filter, keyList.toArray(new Object[keyList.size()]));
    }

    public String getSort() {
        return this.sort;
    }

    public void setSort(String sort) {
        set("sort", sort);
        this.sort = sort;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.set("order", order);
        this.order = order;
    }
}
