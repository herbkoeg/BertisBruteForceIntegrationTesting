package de.hk.bfit.process;

import de.hk.bfit.db.PostgresDBConnector;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestCaseProcessorTest {

    private static PostgresDBConnector dBConnector;
    private static Connection dbConnection;
    private static TestCaseProcessor cut;

    public TestCaseProcessorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        dBConnector = new PostgresDBConnector("jdbc:postgresql://localhost:5432/bertisDB", "berti", "berti");
        dbConnection = dBConnector.getDBConnection();
        cut = new TestCaseProcessor(dBConnector.getDBConnection());
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testGenerateExampleTestCase() throws Exception {
        Map<String, String> variables = new HashMap<>();
        List<String> sqlList = new ArrayList<>();
        cut.generateExampleTestCase("myTestcase", sqlList, variables);
    }

    @Test
    public void testAssertBefore() throws Exception {
    }

    @Test
    public void testAssertAfter() throws Exception {
    }

    @Test
    public void testProcessResetAction() throws Exception {
        List<String> sqlList = new ArrayList<>();
        Map<String, String> variables = new HashMap<>();
        TestCase testCase = cut.generateTestCase(sqlList, variables);
        testCase.setResetAction(new ResetAction());
        cut.processResetAction(testCase, variables);
    }

    @Test
    public void testProcessInitAction() throws Exception {
        List<String> sqlList = new ArrayList<>();
        Map<String, String> variables = new HashMap<>();
        TestCase testCase = cut.generateTestCase(sqlList, variables);
        testCase.setInitAction(new InitAction());
        cut.processInitAction(testCase, variables);
    }

}
