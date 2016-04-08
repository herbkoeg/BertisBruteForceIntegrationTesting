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
        Map<String, String> variables = new HashMap<String,String>();
        List<String> sqlList = new ArrayList<String>();
        cut.generateExampleTestCase("myTestcase", sqlList);
    }

    @Test
    public void testAssertBefore() throws Exception {
    }

    @Test
    public void testAssertAfter() throws Exception {
    }

    @Test
    public void testProcessResetAction() throws Exception {
        List<String> sqlList = new ArrayList<String>();
        Map<String, String> variables = new HashMap<String,String>();
        TestCase testCase = cut.generateTestCase(sqlList);
        testCase.setResetAction(new ResetAction());
        cut.processResetAction(testCase, variables);
    }

    @Test
    public void testProcessInitAction() throws Exception {
        List<String> sqlList = new ArrayList<String>();
        Map<String, String> variables = new HashMap<String,String>();
        TestCase testCase = cut.generateTestCase(sqlList);
        testCase.setInitAction(new InitAction());
        cut.processInitAction(testCase, variables);
    }
    
    @Test
    public void integerationTest() throws Exception {
            PostgresDBConnector dBConnector = 
                new PostgresDBConnector("jdbc:postgresql://localhost:5432/bertisDB", "berti", "berti");
        
        Map<String,String> variables = new HashMap<String,String>();
        TestCaseProcessor testCaseProcessor = new TestCaseProcessor(dBConnector.getDBConnection());
        String filename = "BfitFirstTest.xml";
        List<String> sqlListReferenceAction = new ArrayList<String>();
        boolean add = sqlListReferenceAction.add("select id from Person");
        sqlListReferenceAction.add("select name from Person");
                
        
        testCaseProcessor.generateExampleTestCase(filename, sqlListReferenceAction);
        
        TestCase testCase = testCaseProcessor.loadTestCase(filename);
        testCaseProcessor.assertAfter(testCase);
    }
    
    @Test
    public void integerationTestOnlyResetAction() throws Exception {
            PostgresDBConnector dBConnector = 
                new PostgresDBConnector("jdbc:postgresql://localhost:5432/bertisDB", "berti", "berti");
        
        Map<String,String> variables = new HashMap<String,String>();
        TestCaseProcessor testCaseProcessor = new TestCaseProcessor(dBConnector.getDBConnection());
        
        String filename = "BfitFirstTestOnlyReset.xml";
        TestCase testCase = testCaseProcessor.loadTestCase(filename);
        testCaseProcessor.assertAfter(testCase);
    }

        @Test
    public void integerationTestNoResultToCompareAction() throws Exception {
            PostgresDBConnector dBConnector = 
                new PostgresDBConnector("jdbc:postgresql://localhost:5432/bertisDB", "berti", "berti");
        
        Map<String,String> variables = new HashMap<String,String>();
        TestCaseProcessor testCaseProcessor = new TestCaseProcessor(dBConnector.getDBConnection());
        
        String filename = "BfitFirstTestNoResult.xml";
        TestCase testCase = testCaseProcessor.loadTestCase(filename);
        testCaseProcessor.assertAfter(testCase);
    }


}
