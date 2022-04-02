package com.bluebirdme.mes.core.sql;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class SQLCompatible {
    public SQLCompatible() {
    }

    public static String getSubQueryAsStatement(DBType dt) {
        String asStatement;
        switch (dt) {
            case ORACLE:
                asStatement = " x";
                break;
            default:
                asStatement = " as x";
        }
        return asStatement;
    }
}
