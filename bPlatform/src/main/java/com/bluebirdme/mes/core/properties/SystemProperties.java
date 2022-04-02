package com.bluebirdme.mes.core.properties;

import org.xdemo.superutil.j2se.PathUtils;

import java.io.IOException;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class SystemProperties extends PropertiesLoader {
    public SystemProperties() throws IOException {
        super(PathUtils.getClassPath() + "/system.properties");
    }
}
