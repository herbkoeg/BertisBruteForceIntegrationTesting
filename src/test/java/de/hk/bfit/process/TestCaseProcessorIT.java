package de.hk.bfit.process;

import de.hk.bfit.db.PostgresDBConnector;
import de.hk.bfit.model.InitAction;
import de.hk.bfit.model.ResetAction;
import de.hk.bfit.model.TestCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class TestCaseProcessorIT implements IBfiTest {

    private static TestCaseProcessor cut;

    public TestCaseProcessorIT() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        PostgresDBConnector dBConnector = new PostgresDBConnector(IBfiTest.JDBC_POSTGRESQL_LOCALHOST_5432_BERTIS_DB, IBfiTest.DB_USER, IBfiTest.DB_PASSWORD);
        Connection dbConnection = dBConnector.getDBConnection();
        cut = new TestCaseProcessor(dBConnector.getDBConnection());
    }

    @Test
    public void testGenerateExampleTestCase() throws Exception {
        Map<String, String> variables = new HashMap<>();
        List<String> sqlList = new ArrayList<>();
        cut.generateExampleTestCase(BASE_PATH_GENERATED + "myTestcase", sqlList);
        cut.generateExampleTestCase(BASE_PATH_GENERATED + "myTestcase2", sqlList);
        cut.generateExampleTestCase(BASE_PATH_GENERATED + "myTestcase3", sqlList);

        //cut.generateExampleTestCase("myTestcase", sqlList, false, false);
    }

    @Test
    public void testProcessResetAction() throws Exception {
        List<String> sqlList = new ArrayList<>();
        Map<String, String> variables = new HashMap<>();
        TestCase testCase = cut.generateTestCase(sqlList);
        testCase.setResetAction(new ResetAction());
        cut.processResetAction(testCase, variables);
    }

    @Test
    public void testProcessInitAction() throws Exception {
        List<String> sqlList = new ArrayList<>();
        Map<String, String> variables = new HashMap<>();
        TestCase testCase = cut.generateTestCase(sqlList);
        testCase.setInitAction(new InitAction());
        cut.processInitAction(testCase, variables);
    }

    @Test
    public void integerationTest() throws Exception {
        PostgresDBConnector dBConnector = getPostgresDBConnector();

        Map<String, String> variables = new HashMap<>();
        TestCaseProcessor testCaseProcessor = new TestCaseProcessor(dBConnector.getDBConnection());
        String filename = "BfitFirstTest.xml";

        List<String> sqlListReferenceAction = new ArrayList<>(Arrays.asList("select id from Person", "select name from Person"));

        testCaseProcessor.generateExampleTestCase(filename, sqlListReferenceAction);

        TestCase testCase = testCaseProcessor.loadTestCase(filename);
        testCaseProcessor.getDifferencesAfter(testCase);
        testCaseProcessor.assertAfter(testCase);

    }

    @Test
    public void integerationTestOnlyResetAction() throws Exception {
        PostgresDBConnector dBConnector = getPostgresDBConnector();

        Map<String, String> variables = new HashMap<>();
        TestCaseProcessor testCaseProcessor = new TestCaseProcessor(dBConnector.getDBConnection());

        String filename = BASE_PATH_TESTCASES + "BfitFirstTestOnlyReset.xml";
        TestCase testCase = testCaseProcessor.loadTestCase(filename);
        testCaseProcessor.assertAfter(testCase);
    }

    @Test
    public void variablesInitAction() throws Exception {
        PostgresDBConnector dBConnector = getPostgresDBConnector();

        Map<String, String> variables = new HashMap<>();
        TestCaseProcessor testCaseProcessor = new TestCaseProcessor(dBConnector.getDBConnection());

        String filename = BASE_PATH_TESTCASES + "BfitFirstTestOnlyReset.xml";
        TestCase testCase = testCaseProcessor.loadTestCase(filename);
        testCaseProcessor.assertAfter(testCase);
    }


    @Test
    public void integerationTestNoResultToCompareAction() throws Exception {
        PostgresDBConnector dBConnector = getPostgresDBConnector();

        Map<String, String> variables = new HashMap<>();
        TestCaseProcessor testCaseProcessor = new TestCaseProcessor(dBConnector.getDBConnection());

        String filename = BASE_PATH_TESTCASES + "BfitFirstTestNoResult.xml";
        TestCase testCase = testCaseProcessor.loadTestCase(filename);
        testCaseProcessor.assertAfter(testCase);
    }

    @Test
    public void integerationTestManyVariables() throws Exception {
        PostgresDBConnector dBConnector = getPostgresDBConnector();

        Map<String, String> variables = new HashMap<>();
        variables.put("VNR", "12345");
        variables.put("NAME", "Berti");

        TestCaseProcessor cut = new TestCaseProcessor(dBConnector.getDBConnection());
        String sql = "select $VNR $NAME from contract where vnr = $VNR";

        Assert.assertEquals("select 12345 Berti from contract where vnr = 12345", cut.setVariables(sql, variables));
    }

    private PostgresDBConnector getPostgresDBConnector() {
        return new PostgresDBConnector("jdbc:postgresql://localhost:5432/bertisDB", "berti", "berti");
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
