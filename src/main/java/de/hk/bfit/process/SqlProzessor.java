package de.hk.bfit.process;

import de.hk.bfit.model.IgnorableSqlCommand;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static de.hk.bfit.helper.ReplacementUtils.setVariables;
import static org.apache.commons.lang3.StringUtils.replaceEachRepeatedly;

public class SqlProzessor {

    private final Logger logger = Logger.getLogger(SqlProzessor.class);
    private Connection connection = null;

    public SqlProzessor(Connection connection) {
        this.connection = connection;
    }

    protected void processIgnorableSqlCommandList(List<IgnorableSqlCommand> ignorableSqlCommands, Map<String, String> variables, Boolean rollbackOnError) throws SQLException {

        if (ignorableSqlCommands == null || ignorableSqlCommands.isEmpty()) {
            return;
        }

        connection.setAutoCommit(rollbackOnError != null && !rollbackOnError);
        Statement statement = connection.createStatement();
        String sqlWithSetVars = "";
        try {
            for (IgnorableSqlCommand definedSqlCommand : ignorableSqlCommands) {
                sqlWithSetVars = setVariables(definedSqlCommand.getSqlCommand(), variables);
                if (isExecutableCommand(definedSqlCommand.getSqlCommand())) {
                    logger.info(definedSqlCommand.getSqlCommand());
                    if (!definedSqlCommand.isIgnoreSqlException()) {
                        statement.execute(sqlWithSetVars);
                    } else {
                        try {
                            statement.execute(sqlWithSetVars);
                        } catch (Exception sqlException) {
                            logger.info("ignoring SQLException: " + sqlException.getMessage() + " due to isIgnoreSqlException");
                        }
                    }
                } else {
                    logger.info("... ignoring not updating Command: " + definedSqlCommand);
                }
            }
            connection.commit();
        } catch (SQLException e) {

            String sb = "Command could not be executed: " + sqlWithSetVars + "\n" +
                    "Exception Message: " + e.getMessage() + "\n" +
                    "SQLState         : " + e.getSQLState() + "\n" +
                    "ErrorCode        : " + e.getErrorCode() + "\n" +
                    connection.getClientInfo();
            logger.error(sb);
            if (rollbackOnError == null ? false : rollbackOnError) {
                connection.rollback();
                logger.error("... rollback");
            }
            throw e;
        }
    }

    private boolean isExecutableCommand(String sql) {
        return sql.toLowerCase().startsWith("update")
                || sql.toLowerCase().startsWith("insert")
                || sql.toLowerCase().startsWith("delete");
    }
/*
    protected String setVariables(String sql) {
        return setVariables(sql,null);
    }

    protected String setVariables(String sql, Map<String, String> variables) {
        if (variables != null) {
            Iterator<String> it = variables.keySet().iterator();
            String[] searchList = new String[variables.size()];
            String[] replacementList = new String[variables.size()];
            int counter = 0;
            while (it.hasNext()) {
                String variable = it.next();
                searchList[counter] = "$" + variable;
                replacementList[counter] = variables.get(variable);
                counter++;
            }

            return replaceEachRepeatedly(sql, searchList, replacementList);
        }
        return sql;
    }

 */

    public List<String> createResultList(ResultSet rs) throws SQLException {
        return createResultList(rs,null);
    }

    public List<String> processCommand(String sql) throws SQLException {
        return processCommand(sql,null,null);
    }

    public List<String> processCommand(String sql, List<String> ignoredColumns, Map<String, String> variables) throws SQLException {
        sql = setVariables(sql, variables);
        ResultSet rs =  connection.createStatement().executeQuery(sql);
        return createResultList(rs, ignoredColumns);
    }

    private List<String> createResultList(ResultSet rs, List<String> ignoredColumns) throws SQLException {
        List<String> results = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        while (rs.next()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= columnCount; i++) {
                // System.err.println("########### -> " + rs.getMetaData().getColumnName(i));
                if(ignoredColumns!=null && ignoredColumns.contains(rs.getMetaData().getColumnName(i))) {
                    // do nothing
                } else {
                    if (rs.getString(i) != null) {
                        sb.append(rs.getString(i).trim()).append(";");
                    } else {
                        sb.append("null;");
                    }
                }
            }
            results.add(sb.toString());
        }

        return results;
    }

     void processCommandList(List<String> sqlCommands, Map<String, String> variables, boolean rollbackOnError) throws SQLException {

        List<IgnorableSqlCommand> definedSqlCommands = new ArrayList<>();
        for (String sqlComand : sqlCommands) {
            definedSqlCommands.add(new IgnorableSqlCommand(sqlComand));
        }
        processIgnorableSqlCommandList(definedSqlCommands, variables, rollbackOnError);
    }


}
