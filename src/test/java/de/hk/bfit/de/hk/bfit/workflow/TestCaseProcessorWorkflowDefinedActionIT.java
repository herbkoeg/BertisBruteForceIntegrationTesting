package de.hk.bfit.de.hk.bfit.workflow;

import de.hk.bfit.db.PostgresDBConnector;
import de.hk.bfit.io.TestCaseHandler;
import de.hk.bfit.model.DefinedAction;
import de.hk.bfit.model.TestCase;
import de.hk.bfit.process.IBfiTest;
import de.hk.bfit.process.TestCaseProcessor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCaseProcessorWorkflowDefinedActionIT implements IBfiTest {

    private static PostgresDBConnector dBConnector;
    private static Connection dbConnection;
    private static TestCaseProcessor tcp;

    @BeforeClass
    public static void setUpClass() throws Exception {
        dBConnector = new PostgresDBConnector("jdbc:postgresql://localhost:5432/bertisDB", "berti", "berti");
        dbConnection = dBConnector.getDBConnection();
        tcp = new TestCaseProcessor(dBConnector.getDBConnection());
    }


    @Before
    public void setUp() {
    }

    @Test
    public void testGenerateExampleTestCase() throws Exception {
        Map<String, String> variables = new HashMap<String,String>();
        List<String> sqlList = new ArrayList<String>();
        tcp.generateExampleTestCase(BASE_PATH_GENERATED + "myTestcase", sqlList);
        tcp.generateExampleTestCase(BASE_PATH_GENERATED + "myTestcase2",sqlList);
        tcp.generateExampleTestCase(BASE_PATH_GENERATED + "myTestcase3",sqlList);

        String filename = "workflowtest_ignored_by_git.xml";
        TestCase newTestCase = new TestCase();

        List<DefinedAction> definedActions = new ArrayList<>();

        


        TestCaseHandler.writeTestcase(newTestCase,filename);


        //cut.generateExampleTestCase("myTestcase", sqlList, false, false);
    }



}