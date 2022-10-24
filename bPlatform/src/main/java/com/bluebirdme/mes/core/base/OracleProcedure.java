package com.bluebirdme.mes.core.base;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class OracleProcedure {
    private static final Logger logger = LoggerFactory.getLogger(OracleProcedure.class);
    private final boolean autoCommit;
    private final Connection connection;

    public OracleProcedure(Connection conn, boolean autoCommit) {
        this.autoCommit = autoCommit;
        this.connection = conn;
    }

    public Object[] execute(String procedureName, int[] inPositions, Object[] inValues, int[] outPositions, int[] outTypes) throws SQLException {
        return this.execute(procedureName, inPositions, inValues, outPositions, outTypes, true);
    }

    public Object[] execute(String procedureName, int[] inPositions, Object[] inValues, int[] outPositions, int[] outTypes, boolean autoCloseConnection) throws SQLException {
        String callSql = this.buildCallSql(procedureName, inPositions, outPositions);
        logger.debug("Calling Oracle Procedure [" + callSql + "] [" + (this.autoCommit ? "自动提交" : "手动提交") + "]");
        logger.debug("Parameters : " + GsonTools.toJson(inValues));
        Object[] objects = new Object[outPositions.length];

        try {
            connection.setAutoCommit(this.autoCommit);
            CallableStatement call = this.connection.prepareCall(callSql);

            int i;
            for (i = 0; i < inPositions.length; ++i) {
                if (inValues[i] instanceof Object[]) {
                    ArrayDescriptor des = ArrayDescriptor.createDescriptor("STR_ARY_TYPE", connection);
                    ARRAY array = new ARRAY(des, connection, inValues[i]);
                    call.setArray(inPositions[i], array);
                } else if (inValues[i] instanceof String) {
                    call.setString(inPositions[i], inValues[i].toString());
                } else if (inValues[i] instanceof Integer) {
                    call.setInt(inPositions[i], Integer.valueOf(inValues[i].toString()));
                } else if (inValues[i] instanceof Long) {
                    call.setLong(inPositions[i], Long.valueOf(inValues[i].toString()));
                } else {
                    call.setObject(inPositions[i], inValues[i]);
                }
            }
            for (i = 0; i < outPositions.length; ++i) {
                call.registerOutParameter(outPositions[i], outTypes[i]);
            }
            call.execute();
            for (i = 0; i < outPositions.length; ++i) {
                switch (outTypes[i]) {
                    case -5 -> objects[i] = call.getLong(outPositions[i]);
                    case 4 -> objects[i] = call.getInt(outPositions[i]);
                    case 12 -> objects[i] = call.getString(outPositions[i]);
                    case 2003 -> objects[i] = call.getArray(outPositions[i]);
                    default -> objects[i] = call.getObject(outPositions[i]);
                }
            }
            if (autoCommit) {
                commit();
            }
            if (autoCloseConnection) {
                close();
            }
            logger.debug("Success : " + GsonTools.toJson(objects));
        } catch (Exception ex) {
            this.rollback();
            logger.error(ex.getLocalizedMessage(), ex);
            throw ex;
        } finally {
            if (!this.connection.isClosed()) {
                this.close();
            }
        }
        return objects;
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void close() throws SQLException {
        connection.close();
        logger.debug("Connection has been closed successfully!");
    }

    public String buildCallSql(String procedureName, int[] inPositions, int[] outPositions) {
        StringBuffer buffer = new StringBuffer("{call " + procedureName + "(");

        for (int i = 0; i < (inPositions == null ? 0 : inPositions.length) + (outPositions == null ? 0 : outPositions.length); ++i) {
            buffer.append(i == 0 ? "?" : ",?");
        }
        buffer.append(") }");
        return buffer.toString();
    }
}
