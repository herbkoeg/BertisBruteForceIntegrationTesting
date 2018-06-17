package de.hk.bfit.process;

import de.hk.bfit.db.PostgresDBConnector;
import de.hk.bfit.model.DefinedAction;
import de.hk.bfit.model.DefinedSqlCommand;
import de.hk.bfit.model.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefinedTestCaseProcessorIT {

    private static PostgresDBConnector dBConnector;
    private static Connection dbConnection;

    DefinedTestCaseProcessor cut;

    @Before
    public void init() throws Exception {

        dBConnector = new PostgresDBConnector("jdbc:postgresql://localhost:5432/bertisDB", "berti", "berti");
        dbConnection = dBConnector.getDBConnection();

        cut = new DefinedTestCaseProcessor(dBConnector.getDBConnection());
    }

    @Test
    public void testDefinedActions() throws Exception {
        List<DefinedAction> definedActions = new ArrayList<>();
        Map<String, String> variables = new HashMap<>();

        DefinedAction definedAction = new DefinedAction("EXAMPLE");

        definedAction.getDefinedSqlCommands().add(new DefinedSqlCommand(true, "delete from person where id=11111"));

        definedActions.add(definedAction);

        String filename = "src/test/resources/generated/definedActionTestcase.xml";
        TestCase definedActionTestcase = cut.generateExampleDefinedActionTestCase(filename, definedActions);

        cut.processDefinedAction(definedActionTestcase, "EXAMPLE");
    }

    @Test
    public void testDefinedActions_when_ignoring_PSQException() throws Exception {
        List<DefinedAction> definedActions = new ArrayList<>();
        Map<String, String> variables = new HashMap<>();

        DefinedAction definedAction = new DefinedAction("EXAMPLE");

        definedAction.getDefinedSqlCommands().add(new DefinedSqlCommand(true, "select * from bla"));

        definedActions.add(definedAction);

        String filename = "src/test/resources/generated/definedActionTestcase.xml";
        TestCase definedActionTestcase = cut.generateExampleDefinedActionTestCase(filename, definedActions);

        cut.processDefinedAction(definedActionTestcase, "EXAMPLE");
    }

    @Test(expected = PSQLException.class)
    public void testDefinedActions_when_NOT_ignoring_PSQException() throws Exception {
        List<DefinedAction> definedActions = new ArrayList<>();
        Map<String, String> variables = new HashMap<>();

        DefinedAction definedAction = new DefinedAction("EXAMPLE");

        definedAction.getDefinedSqlCommands().add(new DefinedSqlCommand(false, "update * from bla"));

        definedActions.add(definedAction);

        String filename = "src/test/resources/generated/definedActionTestcase.xml";
        TestCase definedActionTestcase = cut.generateExampleDefinedActionTestCase(filename, definedActions);

        cut.processDefinedAction(definedActionTestcase, "EXAMPLE");
    }

}