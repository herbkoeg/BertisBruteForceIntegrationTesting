package de.hk.bfit.process;

import de.hk.bfit.io.TestCaseHandler;
import de.hk.bfit.model.DefinedAction;
import de.hk.bfit.model.TestCase;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DefinedTestCaseProcessor extends TestCaseProcessor {

    private final Logger logger = Logger.getLogger(DefinedTestCaseProcessor.class);


    public DefinedTestCaseProcessor(Connection connection) {
        super(connection);
    }

    public TestCase generateExampleDefinedActionTestCase(String filename, List<DefinedAction> definedActions) throws SQLException, IllegalArgumentException, JAXBException, IOException {
        TestCase newTestCase = new TestCase();
        newTestCase.setDescription(filename);
        newTestCase.setDefinedActions(definedActions);

        TestCaseHandler.writeTestcase(newTestCase, filename);
        return newTestCase;
    }

    public void processDefinedAction(TestCase testCase, String name) throws SQLException {
        processDefinedAction(testCase, name, null);
    }


    public void processDefinedAction(TestCase testCase, String name, Map<String, String> variables) throws SQLException {

        for (DefinedAction definedAction : testCase.getDefinedActions()) {
            if (definedAction.getName().equals(name)) {
                processDefinedSqlCommandList(definedAction.getDefinedSqlCommands(), variables, definedAction.isRollBackOnError());
            }
        }

    }


}
