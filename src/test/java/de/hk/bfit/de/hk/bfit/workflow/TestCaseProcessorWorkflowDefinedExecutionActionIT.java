package de.hk.bfit.de.hk.bfit.workflow;

import de.hk.bfit.db.PostgresDBConnector;
import de.hk.bfit.io.TestCaseHandler;
import de.hk.bfit.model.DefinedExecutionAction;
import de.hk.bfit.model.TestCase;
import de.hk.bfit.process.IBfiTest;
import de.hk.bfit.process.TestCaseProcessor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCaseProcessorWorkflowDefinedExecutionActionIT implements IBfiTest {

    private static TestCaseProcessor tcp;

    @BeforeClass
    public static void setUpClass() throws Exception {
        PostgresDBConnector dBConnector = new PostgresDBConnector("jdbc:postgresql://localhost:5432/bertisDB", "berti", "berti");
        Connection dbConnection = dBConnector.getDBConnection();
        tcp = new TestCaseProcessor(dBConnector.getDBConnection());
    }


    @Test
    public void testGenerateExampleTestCase() throws Exception {
        Map<String, String> variables = new HashMap<>();
        List<String> sqlList = new ArrayList<>();
        tcp.generateExampleTestCase(BASE_PATH_GENERATED + "myTestcase", sqlList);
        tcp.generateExampleTestCase(BASE_PATH_GENERATED + "myTestcase2", sqlList);
        tcp.generateExampleTestCase(BASE_PATH_GENERATED + "myTestcase3", sqlList);

        String filename = "workflowtest_ignored_by_git.xml";
        TestCase newTestCase = new TestCase();

        List<DefinedExecutionAction> definedExecutionActions = new ArrayList<>();


        TestCaseHandler.writeTestcase(newTestCase, filename);


        //cut.generateExampleTestCase("myTestcase", sqlList, false, false);
    }


}
