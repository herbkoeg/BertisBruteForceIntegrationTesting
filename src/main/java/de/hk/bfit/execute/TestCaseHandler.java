package de.hk.bfit.execute;

import de.hk.bfit.db.DBConnector;
import de.hk.bfit.io.FileAdapter;
import de.hk.bfit.io.GenericXmlHandler;
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

public class TestCaseHandler {

    protected void assertSqlResult(String filename, Map<String, String> variables, boolean reset) throws IOException, JAXBException, SQLException, ClassNotFoundException {
        TestCase testCase = loadTestCase(filename);

        Iterator<ReferenceAction> itRef = testCase.getReferences().iterator();

        while (itRef.hasNext()) {
            ReferenceAction reference = itRef.next();
            System.out.println("executing '" + reference.getSelect() + "'");
            List<String> refResults = reference.getResults();
            if(!reference.getSelect().contains("order by")){
                System.err.println("Warning: SQL doesn't contain 'order by'");
            }
            List<String> actualResults = executeSelectSql(reference.getSelect(), variables);
            System.out.println(actualResults.size() + " results ");

            Assert.assertEquals("Result size differs", refResults.size(), actualResults.size());

            int length = refResults.size();

            for (int i = 0; i < length; i++) {
                Assert.assertEquals(setVariables(refResults.get(i), variables), actualResults.get(i));
            }
        }
        System.out.println("Success!");

        //        if (reset) {
//            List<String> resetList = testCase.getResetCommand;
//            if (resetList != null && resetList.size() > 0) {
//                System.out.println("resetting ...");
//                executeResetSqlList(resetList, variables);
//            }
//        }
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
        List<String> results = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        while (rs.next()) {
            StringBuilder sb = new StringBuilder();
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

    private TestCase loadTestCase(String filename) throws IOException, JAXBException {
        GenericXmlHandler genericXmlHandler = new GenericXmlHandler();
        String content = FileAdapter.readFile(filename);
        return genericXmlHandler.convertXMLToObject(TestCase.class, content);
    }

//    public void generateReferenzFile(String filename, String description, String sql) throws SQLException, ClassNotFoundException, IllegalArgumentException, JAXBException, IOException {
//        List<String> resultList = executeSelectSql(sql, null);
//        List<String> resetList = new ArrayList<>();
//        resetList.add("bitte hier sql - updates fuer reset eintragen");
//        SqlReferenzFile sqlReferenzFile;
//        sqlReferenzFile = new SqlReferenzFile(description, sql, resultList, resetList);
//        
//        GenericXmlHandler genericXmlHandler = new GenericXmlHandler();
//        String content = genericXmlHandler.convertObjectToXML(sqlReferenzFile);
//        FileAdapter.writeFile(filename, content);
//    }
    public void generateTestCase(String filename, String description, String sql) throws SQLException, ClassNotFoundException, IllegalArgumentException, JAXBException, IOException {
        List<String> resultList = executeSelectSql(sql, null);

        TestCase testCase = new TestCase(description, new ReferenceAction("jawoi",sql, resultList));

        GenericXmlHandler genericXmlHandler = new GenericXmlHandler();
        String content = genericXmlHandler.convertObjectToXML(testCase);
        FileAdapter.writeFile(filename, content);
    }

}
