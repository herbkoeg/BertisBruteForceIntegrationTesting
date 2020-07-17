package de.hk.bfit.process;

import de.hk.bfit.db.DBConnectorImpl;
import de.hk.bfit.io.TestCaseGenerator;
import de.hk.bfit.model.InitAction;
import de.hk.bfit.model.ResetAction;
import de.hk.bfit.model.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class TestCaseProcessorIT implements IBfiTest {

    private static TestCaseProcessor cut;
    private static TestCaseGenerator tcg;

    public TestCaseProcessorIT() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        DBConnectorImpl dBConnector = new DBConnectorImpl(IBfiTest.JDBC_POSTGRESQL_MY_DB, IBfiTest.DB_USER, IBfiTest.DB_PASSWORD);
        Connection dbConnection = dBConnector.getDBConnection();
        cut = new TestCaseProcessor(dBConnector.getDBConnection());
        tcg = new TestCaseGenerator(dBConnector.getDBConnection());
    }

    @Test
    public void testGenerateExampleTestCase() throws Exception {
        Map<String, String> variables = new HashMap<>();
        List<String> sqlList = new ArrayList<>();
        tcg.generateTestCaseWithReferenceAfter(BASE_PATH_GENERATED + "myTestcase", sqlList);
  //      cut.generateExampleTestCase(BASE_PATH_GENERATED + "myTestcase2", sqlList, INITACTION, RESETACTION);
  //      cut.generateExampleTestCase(BASE_PATH_GENERATED + "myTestcase3", sqlList,INITACTION);
    }


    @Test
    public void testProcessResetAction() throws Exception {
        List<String> sqlList = new ArrayList<>();
        Map<String, String> variables = new HashMap<>();
        TestCase testCase = tcg.generateTestCaseWithReferenceAfter(sqlList);
        testCase.setResetAction(new ResetAction());
        cut.processResetAction(testCase, variables);
    }

    @Test
    public void testProcessInitAction() throws Exception {
        List<String> sqlList = new ArrayList<>();
        Map<String, String> variables = new HashMap<>();
        TestCase testCase = tcg.generateTestCaseWithReferenceAfter(sqlList);
        testCase.setInitAction(new InitAction());
        cut.processInitAction(testCase, variables);
    }

    @Test
    public void integerationTest() throws Exception {
        DBConnectorImpl dBConnector = getPostgresDBConnector();

        Map<String, String> variables = new HashMap<>();
        TestCaseProcessor testCaseProcessor = new TestCaseProcessor(dBConnector.getDBConnection());
        String filename = "BfitFirstTest.xml";

        List<String> sqlListReferenceAction = new ArrayList<>(Arrays.asList("select id from Person", "select name from Person"));

        tcg.generateTestCaseWithReferenceAfter(filename, sqlListReferenceAction);

        TestCase testCase = testCaseProcessor.loadTestCase(filename);
        testCaseProcessor.getDifferencesAfter(testCase);
        testCaseProcessor.assertAfter(testCase);

    }

    @Test
    public void integerationTestOnlyResetAction() throws Exception {
        DBConnectorImpl dBConnector = getPostgresDBConnector();

        Map<String, String> variables = new HashMap<>();
        TestCaseProcessor testCaseProcessor = new TestCaseProcessor(dBConnector.getDBConnection());

        String filename = BASE_PATH_TESTCASES + "BfitFirstTestOnlyReset.xml";
        TestCase testCase = testCaseProcessor.loadTestCase(filename);
        testCaseProcessor.assertAfter(testCase);
    }

    @Test
    public void variablesInitAction() throws Exception {
        DBConnectorImpl dBConnector = getPostgresDBConnector();

        Map<String, String> variables = new HashMap<>();
        TestCaseProcessor testCaseProcessor = new TestCaseProcessor(dBConnector.getDBConnection());

        String filename = BASE_PATH_TESTCASES + "BfitFirstTestOnlyReset.xml";
        TestCase testCase = testCaseProcessor.loadTestCase(filename);
        testCaseProcessor.assertAfter(testCase);
    }


    @Test
    public void integerationTestNoResultToCompareAction() throws Exception {
        DBConnectorImpl dBConnector = getPostgresDBConnector();

        Map<String, String> variables = new HashMap<>();
        TestCaseProcessor testCaseProcessor = new TestCaseProcessor(dBConnector.getDBConnection());

        String filename = BASE_PATH_TESTCASES + "BfitFirstTestNoResult.xml";
        TestCase testCase = testCaseProcessor.loadTestCase(filename);
        testCaseProcessor.assertAfter(testCase);
    }

    private DBConnectorImpl getPostgresDBConnector() {
        return new DBConnectorImpl("jdbc:postgresql://localhost:5432/bertisDB", "berti", "berti");
    }

    @Test
    public void getClientInfo() throws SQLException {
        System.out.println(cut.getClientInfo());
    }

    @Test
    public void execSql() throws SQLException {
        cut.execSql("select * from personxx");
//        ResultSet rs = cut.execSql("bla");
    }




}
