package com.bluebirdme.mes.core.sql;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public enum DBType {
    ORACLE("oracle"),
    MYSQL("mysql"),
    MSSQL("mssql"),
    SQLITE("sqlite");

    private String type;

    private DBType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.type;
    }

    public static DBType getType(String value) {
        value = value.toLowerCase();
        if ("oracle".equals(value)) {
            return ORACLE;
        }

        if ("mysql".equals(value)) {
            return MYSQL;
        }

        if ("mssql".equals(value)) {
            return MSSQL;
        }

        if ("sqlite".equals(value)) {
            return SQLITE;
        }

        return MYSQL;
    }
}
