package de.hk.bfit.process;

import de.hk.bfit.db.PostgresDBConnector;
import de.hk.bfit.model.*;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.util.*;

public class DefinedExecutionActionProcessingIT {

    private TestCaseProcessor cut;

    @Before
    public void init() throws Exception {

        PostgresDBConnector dBConnector = new PostgresDBConnector(IBfiTest.JDBC_POSTGRESQL_LOCALHOST_5432_BERTIS_DB, IBfiTest.DB_USER, IBfiTest.DB_PASSWORD);
        Connection dbConnection = dBConnector.getDBConnection();

        cut = new TestCaseProcessor(dBConnector.getDBConnection());
        BasicConfigurator.configure();
    }

    @Test
    public void testDefinedActions() throws Exception {
        List<DefinedExecutionAction> definedExecutionActions = new ArrayList<>();
        List<DefinedReferenceAction> definedReferenceActions = new ArrayList<>();

        Map<String, String> variables = new HashMap<>();

        DefinedExecutionAction definedExecutionAction = new DefinedExecutionAction("EXAMPlE");
        List<String> sqlList = new ArrayList<>();
        sqlList.add("insert into person (id,name,vorname,adresse,stadt) values (100,'hans','maier','maxstrasse','muenchen')");
        definedExecutionAction.setRollBackOnError(true);
        definedExecutionAction.setSqlCommands(sqlList);
        definedExecutionActions.add(definedExecutionAction);

        DefinedReferenceAction definedReferenceAction = new DefinedReferenceAction("HERBERT");
        SelectCmd selectCmd = new SelectCmd();
        selectCmd.setSelect("select * from person");
        definedReferenceAction.setSelectCmds(Arrays.asList(selectCmd));
        definedReferenceActions.add(definedReferenceAction);

        String filename = "src/test/resources/generated/definedActionTestcase.xml";
        TestCase definedActionTestcase = cut.generateExampleDefinedActionTestCase(filename, definedExecutionActions,definedReferenceActions);

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
        List<DefinedExecutionAction> definedExecutionActions = new ArrayList<>();
        Map<String, String> variables = new HashMap<>();

        DefinedExecutionAction definedExecutionAction = new DefinedExecutionAction();
        definedExecutionAction.setName("EXAMPLE");

        definedExecutionActions.add(definedExecutionAction);

        String filename = "src/test/resources/generated/definedActionTestcase.xml";
        TestCase definedActionTestcase = cut.generateExampleDefinedActionTestCase(filename, definedExecutionActions,null);

        cut.processDefinedAction(definedActionTestcase, "EXAMPLE");
    }

    @Test(expected = PSQLException.class)
    public void testDefinedActions_when_NOT_ignoring_SQLException() throws Exception {
        List<DefinedExecutionAction> definedExecutionActions = new ArrayList<>();
        Map<String, String> variables = new HashMap<>();

        DefinedExecutionAction definedExecutionAction = new DefinedExecutionAction();
        definedExecutionAction.setName("EXAMPLE");


        definedExecutionActions.add(definedExecutionAction);

        String filename = "src/test/resources/generated/definedActionTestcase.xml";
        TestCase definedActionTestcase = cut.generateExampleDefinedActionTestCase(filename, definedExecutionActions,null);

        cut.processDefinedAction(definedActionTestcase, "EXAMPLE");
    }

}
