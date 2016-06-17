package de.hk.bfit.process;

import de.hk.bfit.io.FileAdapter;
import de.hk.bfit.io.GenericXmlHandler;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import reactor.core.action.ForEachAction;

public class TestCaseProcessor {

    private final Logger logger = Logger.getLogger(TestCaseProcessor.class);
    private final Connection connection;

    public TestCaseProcessor(Connection connection) {
        this.connection = connection;
    }

    public TestCase loadTestCase(String filename) throws IOException, JAXBException {
        return FileAdapter.loadTestCase(filename);
    }

    public void generateExampleTestCase(String filename, List<String> sqlListReferenceAction) throws SQLException, ClassNotFoundException, IllegalArgumentException, JAXBException, IOException {
        TestCase newTestCase = generateTestCase(sqlListReferenceAction);
        GenericXmlHandler genericXmlHandler = new GenericXmlHandler();
        String content = genericXmlHandler.convertObjectToXML(newTestCase);
        FileAdapter.writeFile(filename, content);
    }

    TestCase generateTestCase(List<String> sqlListReferenceAction) throws SQLException, ClassNotFoundException, IllegalArgumentException, JAXBException, IOException {
        List<SelectAction> selectAction = new ArrayList<SelectAction>();

        for (String sql : sqlListReferenceAction) {
            List<String> resultList = processReferenceAction(sql, null);
            selectAction.add(new SelectAction("describe me!", sql, resultList));
        }
        TestCase testCase = new TestCase("This is a new testcase, generated by TestCaseProcessor.generate ...", new ReferenceAction("This is a new referenceAction ...", selectAction));
        testCase.getInitAction().getSqlCommands().add("insert into xyz ...");
        testCase.getInitAction().getSqlCommands().add("update into xyz ...");
        testCase.getInitAction().getSqlCommands().add("delete into xyz ...");
        testCase.getResetAction().getSqlCommands().add("insert into xyz ...");
        testCase.getResetAction().getSqlCommands().add("update into xyz ...");
        testCase.getResetAction().getSqlCommands().add("delete into xyz ...");
        return testCase;
    }

    public void assertBefore(TestCase testCase) throws IOException, JAXBException, SQLException, ClassNotFoundException {
        assertBefore(testCase, null);
    }

    public void assertBefore(TestCase testCase, Map<String, String> variables) throws IOException, JAXBException, SQLException, ClassNotFoundException {
        logger.info("asserting before ...");
        assertAction(testCase.getReferenceActionBefore(), variables);
    }

    public void assertAfter(TestCase testCase) throws IOException, JAXBException, SQLException, ClassNotFoundException {
        assertAfter(testCase, null);
    }

    public void assertAfter(TestCase testCase, Map<String, String> variables) throws IOException, JAXBException, SQLException, ClassNotFoundException {
        logger.info("asserting after ...");
        assertAction(testCase.getReferenceActionAfter(), variables);
    }

    private void assertAction(ReferenceAction referenceAction, Map<String, String> variables) throws IOException, JAXBException, SQLException, ClassNotFoundException {
        if (referenceAction == null || referenceAction.getSelectAction() == null) {
            return;
        }
        Iterator<SelectAction> selectActionIt = referenceAction.getSelectAction().iterator();

        while (selectActionIt.hasNext()) {

            SelectAction selectAction = selectActionIt.next();

            logger.info("Asserting: " + selectAction.getDescription());
            logger.info("-> executing '" + selectAction.getSelect() + "'");

            List<String> refResults = selectAction.getResult();
            if (!selectAction.getSelect().contains("order by")) {
                logger.info("Warning: SQL doesn't contain 'order by'");
            }
            List<String> actualResults = processReferenceAction(selectAction.getSelect(), variables);
            logger.info(actualResults.size() + " results ");

            if (refResults == null) {
                Assert.assertEquals("No Result expected ", 0, actualResults.size());
            } else {
                Assert.assertEquals("Result size differs", refResults.size(), actualResults.size());

                int length = refResults.size();

                for (int i = 0; i < length; i++) {
                    Assert.assertEquals(setVariables(refResults.get(i), variables), actualResults.get(i));
                }
            }
        }
        logger.info("Success!");
    }
   
    public void processResetAction(TestCase testCase, Map<String, String> variables) throws ClassNotFoundException, SQLException {
        processCommandList(testCase.getResetAction().getSqlCommands(), variables, testCase.getResetAction().isRollBackOnError());
    }

    public void processInitAction(TestCase testCase, Map<String, String> variables) throws ClassNotFoundException, SQLException {
        processCommandList(testCase.getInitAction().getSqlCommands(), variables, testCase.getInitAction().isRollBackOnError());
    }

    private List<String> processReferenceAction(String sql, Map<String, String> variables) throws SQLException, ClassNotFoundException {
        sql = setVariables(sql, variables);
        ResultSet rs = createResultset(sql);
        return createResultList(rs);
    }

    String setVariables(String sql, Map<String, String> variables) {
        if (variables != null) {
            Iterator<String> it = variables.keySet().iterator();
            String[] searchList = new String[variables.size()];
            String[] replacementList = new String[variables.size()];
            int counter = 0;
            while (it.hasNext()) {
                String variable = it.next();
                searchList[counter] = "$" + variable;
                replacementList[counter] = variables.get(variable);
                counter ++;
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

    private ResultSet createResultset(String sql) throws SQLException {
        return connection.createStatement().executeQuery(sql);
    }

    private void processCommandList(List<String> sqlList, Map<String, String> variables, boolean rollbackOnError) throws ClassNotFoundException, SQLException {

        if (sqlList == null || sqlList.isEmpty()) {
            return;
        }

        connection.setAutoCommit(!rollbackOnError);
        Statement statement = connection.createStatement();
        Iterator<String> it = sqlList.iterator();

        try {
            while (it.hasNext()) {
                String sql = it.next();
                sql = setVariables(sql, variables);
                if (isExecutableCommand(sql)) {
                    logger.info("reset: execute " + sql);
                    statement.execute(sql);
                } else {
                    logger.info("reset: ignoring " + sql);
                }
            }
            connection.commit();
        } catch (SQLException e) {
            if (rollbackOnError) {
                connection.rollback();
            }
            throw e;
        } finally {
            connection.close();
        }
    }

    private boolean isExecutableCommand(String sql) {
        return sql.toLowerCase().startsWith("update ")
                || sql.toLowerCase().startsWith("insert ")
                || sql.toLowerCase().startsWith("delete ");
    }

}
