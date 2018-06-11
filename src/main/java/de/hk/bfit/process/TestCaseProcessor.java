package de.hk.bfit.process;

import de.hk.bfit.model.ReferenceAction;
import de.hk.bfit.model.SelectAction;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.xml.bind.JAXBException;

import de.hk.bfit.io.TestCaseHandler;
import de.hk.bfit.model.TestCase;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;

public class TestCaseProcessor implements ITestCaseProcessor {

    private final Logger logger = Logger.getLogger(TestCaseProcessor.class);
    private Connection connection = null;

    public TestCaseProcessor() {
    }

    public TestCaseProcessor(Connection connection) {
        this.connection = connection;
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }

    public TestCase loadTestCase(String filename) throws IOException {
        return TestCaseHandler.loadTestCase(filename);
    }

    @Override
    public void generateExampleTestCase(String filename, List<String> sqlListReferenceAction) throws SQLException, IllegalArgumentException, JAXBException, IOException {
        TestCase newTestCase = generateTestCase(sqlListReferenceAction);
        TestCaseHandler.writeTestcase(newTestCase,filename);
    }

    TestCase generateTestCase(List<String> sqlListReferenceAction) throws SQLException, IllegalArgumentException, JAXBException, IOException {
        return generateTestCase(sqlListReferenceAction, false, false);
    }

    public void assertBefore(TestCase testCase) throws SQLException {
        assertBefore(testCase, null);
    }

    public void assertBefore(TestCase testCase, Map<String, String> variables) throws SQLException {
        logger.info("asserting before ...");
        assertAction(testCase.getReferenceActionBefore(), variables);
    }

    public void assertAfter(TestCase testCase) throws SQLException {
        assertAfter(testCase, null);
    }

    public void assertAfter(TestCase testCase, Map<String, String> variables) throws  SQLException {
        logger.info("asserting after ...");
        assertAction(testCase.getReferenceActionAfter(), variables);
    }

    public List<AssertResult> getDifferencesAfter(TestCase testcase) throws SQLException {
        logger.info("getDiffencesAfter ...");
        return getDifferences(testcase.getReferenceActionAfter(),null);
    }

    public List<AssertResult> getDifferencesAfter(TestCase testcase, Map<String, String> variables) throws SQLException {
        logger.info("getDiffencesAfter ...");
        return getDifferences(testcase.getReferenceActionAfter(),variables);
    }

    public List<AssertResult> getDifferencesBefore(TestCase testcase) throws SQLException {
        logger.info("getDiffencesBefore ...");
        return getDifferences(testcase.getReferenceActionBefore(),null);
    }

    public List<AssertResult> getDifferencesBefore(TestCase testcase, Map<String, String> variables) throws  SQLException {
        logger.info("getDiffencesBefore ...");
        return getDifferences(testcase.getReferenceActionBefore(),variables);
    }

    /**
     * Processes the resetAction definded in the testcase
     *
     * @param testCase
     * @param variables
     * @throws SQLException
     */
    public void processResetAction(TestCase testCase, Map<String, String> variables) throws SQLException {
        processCommandList(testCase.getResetAction().getSqlCommands(), variables, testCase.getResetAction().isRollBackOnError());
    }

    /**
     * Processes the initAction defined in the testcase
     *
     * @param testCase
     * @param variables
     * @throws SQLException
     */
    public void processInitAction(TestCase testCase, Map<String, String> variables) throws SQLException {
        processCommandList(testCase.getInitAction().getSqlCommands(), variables, testCase.getInitAction().isRollBackOnError());
    }

    private List<String> processReferenceAction(String sql, Map<String, String> variables) throws SQLException {
        sql = setVariables(sql, variables);
        ResultSet rs = createResultset(sql);
        return createResultList(rs);
    }

    /**
     * Executes one of the following sql commands: insert, update, delete
     * Other commands (for exapmle select) will be ignored
     *
     * @param sql
     * @throws SQLException
     */
    public void execSql(String sql) throws SQLException {
        execSql(sql, new HashMap<String, String>(), true);
    }

    /**
     * Executes one of the following sql commands: insert, update, delete
     * Other commands (for exapmle select) will be ignored
     *
     * @param sql
     * @param variables
     * @param autoCommit
     * @throws SQLException
     */
    public void execSql(String sql, Map<String, String> variables, boolean autoCommit) throws SQLException {
        List<String> sqlList = new ArrayList<>();
        sqlList.add(setVariables(sql, variables));
        processCommandList(sqlList, new HashMap<String, String>(), autoCommit);
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
                counter++;
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

    private ResultSet createResultset(String sql) throws SQLException {
        return connection.createStatement().executeQuery(sql);
    }

    private void processCommandList(List<String> sqlCommands, Map<String, String> variables, boolean rollbackOnError) throws SQLException {

        if (sqlCommands == null || sqlCommands.isEmpty()) {
            return;
        }

        connection.setAutoCommit(!rollbackOnError);
        Statement statement = connection.createStatement();
        Iterator<String> it = sqlCommands.iterator();
        String sqlWithSetVars = "";
        try {
            for (String sqlCommand:sqlCommands) {
                sqlWithSetVars = setVariables(sqlCommand, variables);
                if (isExecutableCommand(sqlCommand)) {
                    logger.info("... executing " + sqlCommand);
                    statement.execute(sqlWithSetVars);
                } else {
                    logger.info("... ignoring " + sqlCommand);
                }
            }
            connection.commit();
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Command could not be executed: ").append(sqlWithSetVars).append("\n");
            sb.append("Exception Message: ").append(e.getMessage()).append("\n");
            sb.append("SQLState         : ").append(e.getSQLState()).append("\n");
            sb.append("ErrorCode        : ").append(e.getErrorCode()).append("\n");

            sb.append(connection.getClientInfo());
            logger.error(sb.toString());
            if (rollbackOnError) {
                connection.rollback();
                logger.error("... rollback");
            }
            throw e;
        }
    }

    public Properties getClientInfo() throws SQLException {
        final Properties clientInfo = connection.getClientInfo();
        return clientInfo;
    }

    TestCase generateTestCase(List<String> sqlListReferenceAction, boolean withInit, boolean withReset) throws SQLException, IllegalArgumentException {
        List<SelectAction> selectAction = new ArrayList<>();

        for (String sql : sqlListReferenceAction) {
            logger.error("processing: " + sql);
            List<String> resultList = processReferenceAction(sql, null);
            selectAction.add(new SelectAction("describe me!", sql, resultList));
        }
        TestCase testCase = new TestCase("This is a new testcase, generated by TestCaseProcessor.generate ...", new ReferenceAction("This is a new referenceAction ...", selectAction));
        if (withInit) {
            testCase.getInitAction().getSqlCommands().add("insert ...");
            testCase.getInitAction().getSqlCommands().add("update ...");
            testCase.getInitAction().getSqlCommands().add("delete ...");
        } else
            testCase.setInitAction(null);
        if (withReset) {
            testCase.getResetAction().getSqlCommands().add("insert ...");
            testCase.getResetAction().getSqlCommands().add("update ...");
            testCase.getResetAction().getSqlCommands().add("delete ...");
        }
        return testCase;
    }

    /**
     * Determines the differences between reference and actual results for a selectAction
     * @param selectAction
     * @param variables
     * @param actualResults
     * @return
     */
     List<AssertResult> determineDifferences(SelectAction selectAction, Map<String, String> variables, List<String> actualResults) {
        List<AssertResult> assertResults = new ArrayList();

            logger.info("-- determine differences: " + selectAction.getDescription());
            List<String> resultList = selectAction.getResults();

            if (resultList == null) {
                assertResults.add(new AssertResult(selectAction.getSelect(),actualResults.size(),0,"No Result expected"));
            } else {

                if(actualResults.size() != resultList.size()) {
                    assertResults.add(new AssertResult(selectAction.getSelect(), actualResults.size(), resultList.size(), "Result size differs"));
                }
                int length = resultList.size();

                for (int i = 0; i < length; i++) {
                    String reference = setVariables(resultList.get(i), variables);
                    String actual = actualResults.get(i);
                    String diff = StringUtils.difference(reference, actual);
                    if(!StringUtils.isEmpty(diff)) {
                        assertResults.add(new AssertResult(selectAction.getSelect(), actual, reference, diff));
                    }
                }
        }
        logger.info("Differences found: " + assertResults.size());

        return assertResults;
    }

    /**
     * Determines all differences for all selectActions of a referenceAction
     * @param referenceAction
     * @param variables
     * @return
     * @throws SQLException
     */
    private List<AssertResult> getDifferences(ReferenceAction referenceAction, Map<String, String> variables) throws SQLException {
        logger.info("Find differences for referenceAction: " + referenceAction.getDescription());

        List<AssertResult> assertResults = new ArrayList();

        if (referenceAction == null || referenceAction.getSelectActions() == null) {
            return null;
        }

        for (SelectAction selectAction : referenceAction.getSelectActions()){
            List<String> refResults = selectAction.getResults();
            logNoOrderWarning(selectAction);

            List<String> actualResults = processReferenceAction(selectAction.getSelect(), variables);

            assertResults.addAll(determineDifferences(selectAction,variables,actualResults));
        }
        logger.info("Differences found: " + assertResults.size());

        return assertResults;
    }

    private void logNoOrderWarning(SelectAction selectAction) {
        if (!selectAction.getSelect().contains("order by")) {
            logger.info("Warning: SQL doesn't contain 'order by'");
        }
    }

    protected void assertAction(ReferenceAction referenceAction, Map<String, String> variables) throws SQLException {
        if (referenceAction == null || referenceAction.getSelectActions() == null) {
            return;
        }

        for (SelectAction selectAction:referenceAction.getSelectActions()) {
            logger.info("Asserting: " + selectAction.getDescription());

            List<String> refResults = selectAction.getResults();
            logNoOrderWarning(selectAction);

            List<String> actualResults = processReferenceAction(selectAction.getSelect(), variables);

            logger.info(actualResults.size() + " results ");

            if (refResults == null) {
                Assert.assertEquals("No Result expected ", 0, actualResults.size());
            } else {
                Assert.assertEquals("Result size differs", refResults.size(), actualResults.size());

                int length = refResults.size();

                for (int i = 0; i < length; i++) {
                    Assert.assertEquals(selectAction.getAssertFailedMessage(), setVariables(refResults.get(i), variables), actualResults.get(i));
                }
            }
        }
        logger.info("Success!");
    }

    private boolean isExecutableCommand(String sql) {
        return sql.toLowerCase().startsWith("update")
                || sql.toLowerCase().startsWith("insert")
                || sql.toLowerCase().startsWith("delete");
    }

}
