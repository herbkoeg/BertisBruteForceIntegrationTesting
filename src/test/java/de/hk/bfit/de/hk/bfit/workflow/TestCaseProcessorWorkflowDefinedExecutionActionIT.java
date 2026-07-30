package de.hk.bfit.de.hk.bfit.workflow;

import de.hk.bfit.db.DBConnectorImpl;
import de.hk.bfit.io.TestCaseGenerator;
import de.hk.bfit.io.TestCaseHandler;
import de.hk.bfit.model.DefinedExecutionAction;
import de.hk.bfit.model.TestCase;
import de.hk.bfit.process.IBfiTest;
import de.hk.bfit.process.TestCaseProcessor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCaseProcessorWorkflowDefinedExecutionActionIT implements IBfiTest {

    private static TestCaseProcessor tcp;
    private static TestCaseGenerator tcg;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "b....R....123+Postgres" TODO in Variable auslagern
        );

        tcp = new TestCaseProcessor(con);
        tcg = new TestCaseGenerator(con);
    }


    @Test
    public void testGenerateExampleTestCase() throws Exception {
        Map<String, String> variables = new HashMap<>();
        List<String> sqlList = new ArrayList<>();
        tcg.generateTestCaseWithReferenceAfter(BASE_PATH_GENERATED + "myTestcase.xml", sqlList);
        tcg.generateTestCaseWithReferenceAfter(BASE_PATH_GENERATED + "myTestcase2.xml", sqlList);
        tcg.generateTestCaseWithReferenceAfter(BASE_PATH_GENERATED + "myTestcase3.xml", sqlList);

        String filename = "workflowtest_ignored_by_git.xml";
        TestCase newTestCase = new TestCase();

        List<DefinedExecutionAction> definedExecutionActions = new ArrayList<>();


        TestCaseHandler.writeTestcase(newTestCase, filename);


        //cut.generateExampleTestCase("myTestcase", sqlList, false, false);
    }


}
