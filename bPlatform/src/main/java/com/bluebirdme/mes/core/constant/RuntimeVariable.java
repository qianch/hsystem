package com.bluebirdme.mes.core.constant;

import com.bluebirdme.mes.core.sql.DBType;

import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class RuntimeVariable {
    public static String WEB_PATH;
    public static int RUN_TYPE = 0;
    public static String RUNTIME_VERSION = "16c61fc1-7132-5837-5734-c89c3973589b";
    public static boolean USER_LOG = false;
    public static boolean EXCEPTION_LOG = false;
    public static boolean DEBUG = false;
    public static DBType DBTYPE;
    public static String UPLOAD_PATH;
    public static Map<String, String> JDBC;
}
