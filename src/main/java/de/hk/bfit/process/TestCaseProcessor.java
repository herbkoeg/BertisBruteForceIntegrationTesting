package de.hk.bfit.process;

import de.hk.bfit.io.TestCaseHandler;
import de.hk.bfit.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class TestCaseProcessor {

    private final Logger logger = Logger.getLogger(TestCaseProcessor.class);
    private Connection connection = null;
    private  SqlProzessor sqlProzessor = null;

    public TestCaseProcessor() {
        sqlProzessor = new SqlProzessor(connection);
    }

    public TestCaseProcessor(Connection connection) {
        this.connection = connection;
        sqlProzessor = new SqlProzessor(connection);
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }

    public TestCase loadTestCase(String filename) throws IOException {
        return TestCaseHandler.loadTestCase(filename);
    }

    public TestCase loadTestCase(String filename, String charsetName) throws IOException {
        return TestCaseHandler.loadTestCase(filename,charsetName);
    }

    /*
    #############################
    ASSERT BEFORE
    #############################
     */
    public void assertBefore(TestCase testCase) throws SQLException {
        assertBefore(testCase, null);
    }

    public void assertBefore(TestCase testCase, Map<String, String> variables) throws SQLException {
        logger.info("asserting before ...");
        assertAction(testCase.getReferenceActionBefore(), variables,null);
    }

    public void assertBefore(TestCase testCase, Map<String, String> variables, Map<String,String> replaceMap) throws SQLException {
        logger.info("asserting before ...");
        assertAction(testCase.getReferenceActionBefore(), variables, replaceMap);
    }

    /*
    #############################
    ASSERT AFTER
    #############################
     */
    public void assertAfter(TestCase testCase) throws SQLException {
        assertAfter(testCase, null);
    }

    public void assertAfter(TestCase testCase, Map<String, String> variables) throws SQLException {
        logger.info("asserting after ...");
        assertAction(testCase.getReferenceActionAfter(), variables, null);
    }

    public void assertAfter(TestCase testCase, Map<String, String> variables, Map<String,String> replacements) throws SQLException {
        logger.info("asserting after ...");
        assertAction(testCase.getReferenceActionAfter(), variables, replacements);
    }

    /*
    #############################
    GET DIFFERENCES AFTER
    #############################
     */
    public List<AssertResult> getDifferencesAfter(TestCase testcase) throws SQLException {
        logger.info("getDiffencesAfter ...");
        return getDifferences(testcase.getReferenceActionAfter(), null);
    }

    public List<AssertResult> getDifferencesAfter(TestCase testcase, Map<String, String> variables) throws SQLException {
        logger.info("getDiffencesAfter ...");
        return getDifferences(testcase.getReferenceActionAfter(), variables);
    }

    /*
    #############################
    GET DIFFERENCES BEFORE
    #############################
     */
    public List<AssertResult> getDifferencesBefore(TestCase testcase) throws SQLException {
        logger.info("getDiffencesBefore ...");
        return getDifferences(testcase.getReferenceActionBefore(), null);
    }

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
    public void processResetAction(TestCase testCase, Map<String, String> variables) throws SQLException {
        sqlProzessor.processCommandList(testCase.getResetAction().getSqlCommands(), variables, testCase.getResetAction().isRollBackOnError());
    }

    /**
     * Processes the initAction defined in the testcase
     *
     * @param testCase
     * @param variables
     * @throws SQLException
     */
    public void processInitAction(TestCase testCase, Map<String, String> variables) throws SQLException {
        sqlProzessor.processCommandList(testCase.getInitAction().getSqlCommands(), variables, testCase.getInitAction().isRollBackOnError());
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
        sqlList.add(sqlProzessor.setVariables(sql, variables));
        sqlProzessor.processCommandList(sqlList, new HashMap<String, String>(), autoCommit);
    }

    public Properties getClientInfo() throws SQLException {
        Properties clientInfo;
        clientInfo = connection.getClientInfo();
        return clientInfo;
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
                String reference = sqlProzessor.setVariables(resultList.get(i), variables);
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

            List<String> actualResults = sqlProzessor.processCommand(selectCmd.getSelect(), selectCmd.getIgnoredColumns(), variables);

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

    String replaceStrings(String input, Map<String,String> replaceMap) {
        if(replaceMap==null) {
            return input;
        }
        Set<String> keySet = replaceMap.keySet();
        String newString = input;
        for (String key: keySet) {
            newString = newString.replace(key,replaceMap.get(key));
        }
        return newString;
    }

    private void assertAction(ReferenceAction referenceAction, Map<String, String> variablesMap, Map<String,String> replaceMap) throws SQLException {
        if (referenceAction == null || referenceAction.getSelectCmds() == null) {
            return;
        }

        for (SelectCmd selectCmd : referenceAction.getSelectCmds()) {
            logger.info("Asserting: " + selectCmd.getDescription()==null?"noDescription set":selectCmd.getDescription());
            logger.info("SelectCmd: " + selectCmd.getSelect());

            List<String> refResults = selectCmd.getResults();
            logNoOrderWarning(selectCmd);

            List<String> actualResults = sqlProzessor.processCommand(selectCmd.getSelect(), selectCmd.getIgnoredColumns(), variablesMap);

            logger.info(actualResults.size() + " results ");

            if (refResults == null) {
                Assert.assertEquals("No Result expected ", 0, actualResults.size());
            } else {
                Assert.assertEquals("Result size differs for " + selectCmd.getSelect() + " -> ", refResults.size(), actualResults.size());

                int length = refResults.size();

                for (int i = 0; i < length; i++) {
                    Assert.assertEquals(
                            selectCmd.getAssertFailedMessage(),replaceStrings(sqlProzessor.setVariables(refResults.get(i), variablesMap),replaceMap)
                            , replaceStrings(actualResults.get(i),replaceMap));
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

    public TestCase generateExampleDefinedActionTestCase(String filename, List<DefinedExecutionAction> definedExecutionActions, List<DefinedReferenceAction> definedReferenceActions) throws IllegalArgumentException, IOException {
        TestCase newTestCase = new TestCase();
        newTestCase.setDescription(filename);
        newTestCase.setDefinedExecutionActions(definedExecutionActions);
        newTestCase.setDefinedReferenceActions(definedReferenceActions);
        TestCaseHandler.writeTestcase(newTestCase, filename);
        return newTestCase;
    }

    public void processDefinedAction(TestCase testCase, String name) throws SQLException {
        processDefinedAction(testCase, name, null);
    }

    private void processDefinedAction(TestCase testCase, String name, Map<String, String> variables) throws SQLException {
        for (DefinedExecutionAction definedExecutionAction : testCase.getDefinedExecutionActions()) {
            if (definedExecutionAction.getName().equals(name)) {
                sqlProzessor.processCommandList(definedExecutionAction.getSqlCommands(),variables, definedExecutionAction.isRollBackOnError());
            }
        }
    }

}
