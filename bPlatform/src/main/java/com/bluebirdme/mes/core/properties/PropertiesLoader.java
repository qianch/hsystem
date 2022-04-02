package com.bluebirdme.mes.core.properties;


import org.xdemo.superutil.j2se.PropertiesUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public abstract class PropertiesLoader {
    public String propFile;
    public Properties properties;

    public PropertiesLoader(String propFile) throws IOException {
        this.propFile = propFile;
        this.properties = PropertiesUtils.readProperties(propFile);
    }

    public Object get(String key) {
        return this.properties.get(key);
    }

    public String getAsString(String key) {
        Object object = this.properties.get(key);
        return object == null ? null : object.toString();
    }

    public boolean getAsBoolean(String key) {
        Object object = this.properties.get(key);
        return object == null ? false : Boolean.parseBoolean(object.toString());
    }

    public Integer getAsInt(String key) {
        Object object = this.properties.get(key);
        return object == null ? null : Integer.parseInt(object.toString());
    }

    public Long getAsLong(String key) {
        Object object = this.properties.get(key);
        return object == null ? null : Long.parseLong(object.toString());
    }

    public Float getAsFloat(String key) {
        Object object = this.properties.get(key);
        return object == null ? null : Float.parseFloat(object.toString());
    }

    public Date getAsDate(String key, String format) throws ParseException {
        Object object = this.properties.get(key);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return object == null ? null : sdf.parse(object.toString());
    }
}
