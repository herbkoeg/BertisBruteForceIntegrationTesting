package de.hk.bfit.execute;

import de.hk.bfit.db.DBConnector;
import de.hk.bfit.io.FileAdapter;
import de.hk.bfit.io.GenericXmlHandler;
import de.hk.bfit.io.SqlReferenzFile;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

public class ResultHandler {

    protected void assertSqlResult(String filename, Map<String, String> variables, boolean reset) throws IOException, JAXBException, SQLException, ClassNotFoundException {
        SqlReferenzFile sqlReferenzFile = getSqlReferenzFile(filename);
        if (!sqlReferenzFile.getTestSql().contains("order by")) {
            System.err.println("Warning: SQL enthaelt kein 'order by'");
        }
        System.out.println("executing '" + sqlReferenzFile.getTestSql() + "'");
        List<String> refResults = sqlReferenzFile.getTestResult();
        List<String> actualResults = executeSelectSql(sqlReferenzFile.getTestSql(), variables);
        System.out.println(actualResults.size() + " results ");

        if (reset) {
            List<String> resetList = sqlReferenzFile.getResetCommand();
            if (resetList != null && resetList.size() > 0) {
                System.out.println("resetting ...");
                executeResetSqlList(resetList, variables);
            }
        }

        Assert.assertEquals("Unterschiedlich viele Treffer", refResults.size(), actualResults.size());

        int length = refResults.size();

        for (int i = 0; i < length; i++) {
            Assert.assertEquals(setVariables(refResults.get(i), variables), actualResults.get(i));
        }
        System.out.println("Success :-)");
    }

    private String setVariables(String sql, Map<String, String> variables) {
        if (variables != null) {

            Iterator<String> it = variables.keySet().iterator();
            String[] searchList = new String[variables.size()];
            String[] replacementList = new String[variables.size()];
            int counter = 0;
            while (it.hasNext()) {
                String variable = it.next();
                searchList[counter] = "$" + variable;
                replacementList[counter] = variables.get(variable);
            }

            return StringUtils.replaceEachRepeatedly(sql, searchList, replacementList);
        }
        return sql;
    }

    private List<String> createResultList(ResultSet rs) throws SQLException {
        List<String> results = new ArrayList<String>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        while (rs.next()) {
            StringBuffer sb = new StringBuffer();
            for (int i = 1; i <= columnCount; i++) {
                if (rs.getString(i) != null) {
                    sb.append(rs.getString(i).trim()).append(";");
                } else {
                    sb.append("null;");
                }
            }
            results.add(sb.toString());
        }
        return results;
    }

    private List<String> executeSelectSql(String sql, Map<String, String> variables) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnector.getDBConnection();
        if (variables != null) {
            sql = setVariables(sql, variables);
        }
        ResultSet rs = createResultset(connection, sql);
        return createResultList(rs);
    }

    private ResultSet createResultset(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    private void executeResetSqlList(List<String> sqlList, Map<String, String> variables) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnector.getDBConnection();
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        Iterator<String> it = sqlList.iterator();

        try {
            while (it.hasNext()) {
                String sql = it.next();
                if (variables != null) {
                    sql = setVariables(sql, variables);
                }
                if (sql.toLowerCase().startsWith("update ") || sql.toLowerCase().startsWith("insert ") || sql.toLowerCase().startsWith("delete ")) {
                    System.out.println("reset: execute " + sql);
                    statement.execute(sql);
                } else {
                    System.out.println("reset: ignoring " + sql);
                }
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }
    }

    private SqlReferenzFile getSqlReferenzFile(String filename) throws IOException, JAXBException {
        GenericXmlHandler genericXmlHandler = new GenericXmlHandler();
        String content = FileAdapter.readFile(filename);
        return genericXmlHandler.convertXMLToObject(SqlReferenzFile.class, content);
    }
}
