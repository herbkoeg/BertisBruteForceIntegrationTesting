package de.hk.bfit.process;

import de.hk.bfit.db.PostgresDBConnector;
import de.hk.bfit.model.DefinedAction;
import de.hk.bfit.model.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefinedActionProcessingIT {

    TestCaseProcessor cut;

    @Before
    public void init() throws Exception {

        PostgresDBConnector dBConnector = new PostgresDBConnector(IBfiTest.JDBC_POSTGRESQL_LOCALHOST_5432_BERTIS_DB, IBfiTest.DB_USER, IBfiTest.DB_PASSWORD);
        Connection dbConnection = dBConnector.getDBConnection();

        cut = new TestCaseProcessor(dBConnector.getDBConnection());
    }

    @Test
    public void testDefinedActions() throws Exception {
        List<DefinedAction> definedActions = new ArrayList<>();
        Map<String, String> variables = new HashMap<>();

        DefinedAction definedAction = new DefinedAction("EXAMPLE");

        definedActions.add(definedAction);

        String filename = "src/test/resources/generated/definedActionTestcase.xml";
        TestCase definedActionTestcase = cut.generateExampleDefinedActionTestCase(filename, definedActions);

        cut.processDefinedAction(definedActionTestcase, "EXAMPLE");
    }

    @Test
    public void testLoadTestcaseWithDefinedActions() throws Exception {
        String filename = "src/test/resources/testcases/BfiFirstDefinedActionTestcase.xml";
        TestCase definedActionTestcase = cut.loadTestCase(filename);

        cut.processDefinedAction(definedActionTestcase, "EXAMPLE");
    }

    @Test
    public void testDefinedActions_when_ignoring_SQLException() throws Exception {
        List<DefinedAction> definedActions = new ArrayList<>();
        Map<String, String> variables = new HashMap<>();

        DefinedAction definedAction = new DefinedAction("EXAMPLE");

        definedActions.add(definedAction);

        String filename = "src/test/resources/generated/definedActionTestcase.xml";
        TestCase definedActionTestcase = cut.generateExampleDefinedActionTestCase(filename, definedActions);

        cut.processDefinedAction(definedActionTestcase, "EXAMPLE");
    }

    @Test(expected = PSQLException.class)
    public void testDefinedActions_when_NOT_ignoring_SQLException() throws Exception {
        List<DefinedAction> definedActions = new ArrayList<>();
        Map<String, String> variables = new HashMap<>();

        DefinedAction definedAction = new DefinedAction("EXAMPLE");

        definedActions.add(definedAction);

        String filename = "src/test/resources/generated/definedActionTestcase.xml";
        TestCase definedActionTestcase = cut.generateExampleDefinedActionTestCase(filename, definedActions);

        cut.processDefinedAction(definedActionTestcase, "EXAMPLE");
    }

}
