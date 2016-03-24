package de.hk.bfit.process;

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
import org.apache.log4j.Logger;
import org.junit.Assert;

public class TestCaseProcessor {

    private Logger logger;

    public TestCaseProcessor() {
        this.logger = Logger.getLogger(TestCaseProcessor.class);
    }
    
    public void assertReferences(TestCase testCase, Map<String, String> variables, boolean reset) throws IOException, JAXBException, SQLException, ClassNotFoundException {
        Iterator<ReferenceAction> itRef = testCase.getReferenceActions().iterator();

        while (itRef.hasNext()) {
            ReferenceAction reference = itRef.next();
            logger.info("executing '" + reference.getSelect() + "'");
            List<String> refResults = reference.getResults();
            if(!reference.getSelect().contains("order by")){
                logger.info("Warning: SQL doesn't contain 'order by'");
            }
            List<String> actualResults = executeSelectSql(reference.getSelect(), variables);
            logger.info(actualResults.size() + " results ");
            
            Assert.assertEquals("Result size differs", refResults.size(), actualResults.size());

            int length = refResults.size();

            for (int i = 0; i < length; i++) {
                Assert.assertEquals(setVariables(refResults.get(i), variables), actualResults.get(i));
            }
        }
        logger.info("Success!");

        //     if (reset) {
        //     processReset()
    }

    protected void processReset() {
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

    private void processResetSqlList(List<String> sqlList, Map<String, String> variables) throws ClassNotFoundException, SQLException {
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
                    logger.info("reset: execute " + sql);
                    statement.execute(sql);
                } else {
                    logger.info("reset: ignoring " + sql);
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

    public void generateExampleTestCase(String filename, String description, String sql) throws SQLException, ClassNotFoundException, IllegalArgumentException, JAXBException, IOException {
        List<String> resultList = executeSelectSql(sql, null);

        TestCase testCase = new TestCase(description, new ReferenceAction("jawoi",sql, resultList));

        testCase.getInitAction().getCommands().add("tralala");
        testCase.getInitAction().getCommands().add("blafasl");
        
        testCase.getReferenceActions().add(new ReferenceAction("jamei", "select tralala", new ArrayList<>()));
        
        
        GenericXmlHandler genericXmlHandler = new GenericXmlHandler();
        String content = genericXmlHandler.convertObjectToXML(testCase);
        FileAdapter.writeFile(filename, content);
    }

}
