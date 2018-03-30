package de.hk.bfit.process;

import de.hk.bfit.action.ReferenceAction;
import de.hk.bfit.action.SelectAction;
import de.hk.bfit.io.FileAdapter;
import de.hk.bfit.io.GenericXmlHandler;

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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;

public class TestCaseProcessor  {

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
        return generateTestCase(sqlListReferenceAction, false, false);
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

    public List<AssertResult> getDifferencesAfter(TestCase testcase) throws ClassNotFoundException, SQLException, JAXBException, IOException {
        logger.info("getDiffencesAfter ...");
        return getDifferences(testcase.getReferenceActionAfter(),null);
    }

    public List<AssertResult> getDifferencesAfter(TestCase testcase, Map<String, String> variables) throws ClassNotFoundException, SQLException, JAXBException, IOException {
        logger.info("getDiffencesAfter ...");
        return getDifferences(testcase.getReferenceActionAfter(),variables);
    }

    public List<AssertResult> getDifferencesBefore(TestCase testcase) throws ClassNotFoundException, SQLException, JAXBException, IOException {
        logger.info("getDiffencesBefore ...");
        return getDifferences(testcase.getReferenceActionBefore(),null);
    }

    public List<AssertResult> getDifferencesBefore(TestCase testcase, Map<String, String> variables) throws ClassNotFoundException, SQLException, JAXBException, IOException {
        logger.info("getDiffencesBefore ...");
        return getDifferences(testcase.getReferenceActionBefore(),variables);
    }

    /**
     * Processes the resetAction definded in the testcase
     *
     * @param testCase
     * @param variables
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void processResetAction(TestCase testCase, Map<String, String> variables) throws ClassNotFoundException, SQLException {
        processCommandList(testCase.getResetAction().getSqlCommands(), variables, testCase.getResetAction().isRollBackOnError());
    }

    /**
     * Processes the initAction defined in the testcase
     *
     * @param testCase
     * @param variables
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void processInitAction(TestCase testCase, Map<String, String> variables) throws ClassNotFoundException, SQLException {
        processCommandList(testCase.getInitAction().getSqlCommands(), variables, testCase.getInitAction().isRollBackOnError());
    }

    private List<String> processReferenceAction(String sql, Map<String, String> variables) throws SQLException, ClassNotFoundException {
        sql = setVariables(sql, variables);
        ResultSet rs = createResultset(sql);
        return createResultList(rs);
    }

    /**
     * Executes one of the following sql commands: insert, update, delete
     * Other commands (for exapmle select) will be ignored
     *
     * @param sql
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void execSql(String sql) throws ClassNotFoundException, SQLException {
        execSql(sql, null, true);
    }

    /**
     * Executes one of the following sql commands: insert, update, delete
     * Other commands (for exapmle select) will be ignored
     *
     * @param sql
     * @param variables
     * @param autoCommit
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void execSql(String sql, Map<String, String> variables, boolean autoCommit) throws ClassNotFoundException, SQLException {
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

    public void processCommandList(List<String> sqlList, Map<String, String> variables, boolean rollbackOnError) throws ClassNotFoundException, SQLException {

        if (sqlList == null || sqlList.isEmpty()) {
            return;
        }

        connection.setAutoCommit(!rollbackOnError);
        Statement statement = connection.createStatement();
        Iterator<String> it = sqlList.iterator();
        String sql = "";
        try {
            while (it.hasNext()) {
                sql = it.next();
                sql = setVariables(sql, variables);
                if (isExecutableCommand(sql)) {
                    logger.info("... execute " + sql);
                    statement.execute(sql);
                } else {
                    logger.info("... ignore " + sql);
                }
            }
            connection.commit();
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Command could not be executed: ").append(sql).append("\n");
            sb.append("Exception Message: ").append(e.getMessage()).append("\n");
            sb.append("SQLState         : ").append(e.getSQLState()).append("\n");
            sb.append("ErrorCode        : ").append(e.getErrorCode()).append("\n");

            sb.append("").append(connection.getClientInfo());
            logger.error(sb.toString());
            if (rollbackOnError) {
                connection.rollback();
                logger.error("... rollback");
            }
//            connection.close()
            throw e;
        } finally {
//            connection.close();
        }
    }

    public Properties getClientInfo() throws SQLException {
        final Properties clientInfo = connection.getClientInfo();
        return clientInfo;
    }

    TestCase generateTestCase(List<String> sqlListReferenceAction, boolean withInit, boolean withReset) throws SQLException, ClassNotFoundException, IllegalArgumentException, JAXBException, IOException {
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
            List<String> resultList = selectAction.getResult();

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
     * @throws IOException
     * @throws JAXBException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<AssertResult> getDifferences(ReferenceAction referenceAction, Map<String, String> variables) throws IOException, JAXBException, SQLException, ClassNotFoundException {
        logger.info("Find differences for referenceAction: " + referenceAction.getDescription());

        List<AssertResult> assertResults = new ArrayList();

        if (referenceAction == null || referenceAction.getSelectAction() == null) {
            return null;
        }

        for (SelectAction selectAction : referenceAction.getSelectAction()){
            List<String> refResults = selectAction.getResult();
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

    protected void assertAction(ReferenceAction referenceAction, Map<String, String> variables) throws IOException, JAXBException, SQLException, ClassNotFoundException {
        if (referenceAction == null || referenceAction.getSelectAction() == null) {
            return;
        }
        Iterator<SelectAction> selectActionIt = referenceAction.getSelectAction().iterator();

        while (selectActionIt.hasNext()) {

            SelectAction selectAction = selectActionIt.next();

            logger.info("Asserting: " + selectAction.getDescription());

            List<String> refResults = selectAction.getResult();
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

    protected boolean isExecutableCommand(String sql) {
        return sql.toLowerCase().startsWith("update")
                || sql.toLowerCase().startsWith("insert")
                || sql.toLowerCase().startsWith("delete");
    }

}
