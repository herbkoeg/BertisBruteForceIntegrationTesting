package de.hk.bfit.process;

import de.hk.bfit.io.TestCaseHandler;
import de.hk.bfit.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import static de.hk.bfit.process.StubGeneration.*;

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

    public void generateExampleTestCase(String filename, List<String> sqlListReferenceAction) throws SQLException, IllegalArgumentException, IOException {
        TestCase newTestCase = generateTestCase(sqlListReferenceAction);
        TestCaseHandler.writeTestcase(newTestCase, filename);
    }

    @Override
    public void assertBefore(TestCase testCase) throws SQLException {
        assertBefore(testCase, null);
    }

    @Override
    public void assertBefore(TestCase testCase, Map<String, String> variables) throws SQLException {
        logger.info("asserting before ...");
        assertAction(testCase.getReferenceActionBefore(), variables);
    }

    @Override
    public void assertAfter(TestCase testCase) throws SQLException {
        assertAfter(testCase, null);
    }

    @Override
    public void assertAfter(TestCase testCase, Map<String, String> variables) throws SQLException {
        logger.info("asserting after ...");
        assertAction(testCase.getReferenceActionAfter(), variables);
    }

    @Override
    public List<AssertResult> getDifferencesAfter(TestCase testcase) throws SQLException {
        logger.info("getDiffencesAfter ...");
        return getDifferences(testcase.getReferenceActionAfter(), null);
    }

    @Override
    public List<AssertResult> getDifferencesAfter(TestCase testcase, Map<String, String> variables) throws SQLException {
        logger.info("getDiffencesAfter ...");
        return getDifferences(testcase.getReferenceActionAfter(), variables);
    }

    @Override
    public List<AssertResult> getDifferencesBefore(TestCase testcase) throws SQLException {
        logger.info("getDiffencesBefore ...");
        return getDifferences(testcase.getReferenceActionBefore(), null);
    }

    @Override
    public List<AssertResult> getDifferencesBefore(TestCase testcase, Map<String, String> variables) throws SQLException {
        logger.info("getDiffencesBefore ...");
        return getDifferences(testcase.getReferenceActionBefore(), variables);
    }

    /**
     * Processes the resetAction definded in the testcase
     *
     * @param testCase
     * @param variables
     * @throws SQLException
     */
    @Override
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
    @Override
    public void processInitAction(TestCase testCase, Map<String, String> variables) throws SQLException {
        processCommandList(testCase.getInitAction().getSqlCommands(), variables, testCase.getInitAction().isRollBackOnError());
    }

    private List<String> processCommand(String sql, List<String> ignoredColumns, Map<String, String> variables) throws SQLException {
        sql = setVariables(sql, variables);
        ResultSet rs = createResultset(sql);
        return createResultList(rs, ignoredColumns);
    }

    /**
     * Executes one of the following sql commands: insert, update, delete
     * Other commands (for exapmle select) will be ignored
     *
     * @param sql
     * @throws SQLException
     */
    @Override
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
    @Override
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

    private List<String> createResultList(ResultSet rs, List<String> ignoredColumns) throws SQLException {
        List<String> results = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        while (rs.next()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= columnCount; i++) {
                //               System.err.println("########### -> " + rs.getMetaData().getColumnName(i));
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

        List<IgnorableSqlCommand> definedSqlCommands = new ArrayList<>();
        for (String sqlComand : sqlCommands) {
            definedSqlCommands.add(new IgnorableSqlCommand(sqlComand));
        }
        processIgnorableSqlCommandList(definedSqlCommands, variables, rollbackOnError);
    }


    private void processIgnorableSqlCommandList(List<IgnorableSqlCommand> ignorableSqlCommands, Map<String, String> variables, Boolean rollbackOnError) throws SQLException {

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
                    logger.info("... executing " + definedSqlCommand);
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

    @Override
    public Properties getClientInfo() throws SQLException {
        Properties clientInfo;
        clientInfo = connection.getClientInfo();
        return clientInfo;
    }

    public TestCase generateTestCase(List<String> sqls) throws SQLException, IllegalArgumentException {
        List<SelectCmd> selectCmd = new ArrayList<>();

        for (String sql : sqls) {
            logger.error("processing: " + sql);
            List<String> resultList = processCommand(sql, null, null);
            selectCmd.add(new SelectCmd("describe me!", sql, resultList));
        }
        TestCase testCase = new TestCase("This is a new testcase, generated by TestCaseProcessor.generate ...", new ReferenceAction("This is a new referenceAction ...", selectCmd));
        if (StubGeneration.NOTHING == INIT_AND_RESET_ACTION || StubGeneration.NOTHING == INITACTION) {
            testCase.getInitAction().getSqlCommands().add("insert ...");
            testCase.getInitAction().getSqlCommands().add("update ...");
            testCase.getInitAction().getSqlCommands().add("delete ...");
        } else {
            testCase.setInitAction(null);
        }
        if (StubGeneration.NOTHING == INIT_AND_RESET_ACTION || StubGeneration.NOTHING == RESETACTION) {
            testCase.getResetAction().getSqlCommands().add("insert ...");
            testCase.getResetAction().getSqlCommands().add("update ...");
            testCase.getResetAction().getSqlCommands().add("delete ...");
        }
        return testCase;
    }

    /**
     * Determines the differences between reference and actual results for a selectCmd
     *
     * @param selectCmd
     * @param variables
     * @param actualResults
     * @return
     */
    List<AssertResult> determineDifferences(SelectCmd selectCmd, Map<String, String> variables, List<String> actualResults) {
        ArrayList assertResults = new ArrayList();

        logger.info("-- determine differences: " + selectCmd.getDescription());
        List<String> resultList = selectCmd.getResults();

        if (resultList == null) {
            assertResults.add(new AssertResult(selectCmd.getSelect(), actualResults.size(), 0, "No Result expected"));
        } else {

            if (actualResults.size() != resultList.size()) {
                assertResults.add(new AssertResult(selectCmd.getSelect(), actualResults.size(), resultList.size(), "Result size differs for " + selectCmd.getSelect()));
            }
            int length = resultList.size();

            for (int i = 0; i < length; i++) {
                String reference = setVariables(resultList.get(i), variables);
                String actual = actualResults.get(i);
                String diff = StringUtils.difference(reference, actual);
                if (!StringUtils.isEmpty(diff)) {
                    assertResults.add(new AssertResult(selectCmd.getSelect(), actual, reference, diff));
                }
            }
        }
        logger.info("Differences found: " + assertResults.size());

        return assertResults;
    }

    /**
     * Determines all differences for all selectCmds of a referenceAction
     *
     * @param referenceAction
     * @param variables
     * @return
     * @throws SQLException
     */
    private List<AssertResult> getDifferences(ReferenceAction referenceAction, Map<String, String> variables) throws SQLException {
        logger.info("Find differences for referenceAction: " + referenceAction.getDescription());

        List<AssertResult> assertResults = new ArrayList();

        if (referenceAction == null || referenceAction.getSelectCmds() == null) {
            return null;
        }

        for (SelectCmd selectCmd : referenceAction.getSelectCmds()) {
            List<String> refResults = selectCmd.getResults();
            logNoOrderWarning(selectCmd);

            List<String> actualResults = processCommand(selectCmd.getSelect(), selectCmd.getIgnoredColumns(), variables);

            assertResults.addAll(determineDifferences(selectCmd, variables, actualResults));
        }
        logger.info("Differences found: " + assertResults.size());

        return assertResults;
    }

    private void logNoOrderWarning(SelectCmd selectCmd) {
        if (!selectCmd.getSelect().contains("order by")) {
            logger.info("Warning: SQL doesn't contain 'order by'");
        }
    }

    private void assertAction(ReferenceAction referenceAction, Map<String, String> variables) throws SQLException {
        if (referenceAction == null || referenceAction.getSelectCmds() == null) {
            return;
        }

        for (SelectCmd selectCmd : referenceAction.getSelectCmds()) {
            logger.info("Asserting: " + selectCmd.getDescription()==null?"noDescription set":selectCmd.getDescription());
            logger.info("SelectCmd: " + selectCmd.getSelect());

            List<String> refResults = selectCmd.getResults();
            logNoOrderWarning(selectCmd);

            List<String> actualResults = processCommand(selectCmd.getSelect(), selectCmd.getIgnoredColumns(), variables);

            logger.info(actualResults.size() + " results ");

            if (refResults == null) {
                Assert.assertEquals("No Result expected ", 0, actualResults.size());
            } else {
                Assert.assertEquals("Result size differs for " + selectCmd.getSelect() + " -> ", refResults.size(), actualResults.size());

                int length = refResults.size();

                for (int i = 0; i < length; i++) {
                    Assert.assertEquals(selectCmd.getAssertFailedMessage(), setVariables(refResults.get(i), variables), actualResults.get(i));
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

    public TestCase generateExampleDefinedActionTestCase(String filename, List<DefinedAction> definedActions) throws IllegalArgumentException, IOException {
        TestCase newTestCase = new TestCase();
        newTestCase.setDescription(filename);
        newTestCase.setDefinedActions(definedActions);

        TestCaseHandler.writeTestcase(newTestCase, filename);
        return newTestCase;
    }

    public void processDefinedAction(TestCase testCase, String name) throws SQLException {
        processDefinedAction(testCase, name, null);
    }

    private void processDefinedAction(TestCase testCase, String name, Map<String, String> variables) throws SQLException {

        for (DefinedAction definedAction : testCase.getDefinedActions()) {
            if (definedAction.getName().equals(name)) {
                processIgnorableSqlCommandList(definedAction.getIgnorableSqlCommands(), variables, definedAction.isRollBackOnError());
            }
        }

    }
}
