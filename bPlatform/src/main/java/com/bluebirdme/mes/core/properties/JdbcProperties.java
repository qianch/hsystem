package com.bluebirdme.mes.core.properties;

import org.xdemo.superutil.j2se.PathUtils;

import java.io.IOException;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class JdbcProperties extends PropertiesLoader {
    public JdbcProperties() throws IOException {
        super(PathUtils.getClassPath() + "/jdbc.properties");
    }
}
