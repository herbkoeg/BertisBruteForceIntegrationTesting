package de.hk.bfit.process;

import de.hk.bfit.io.TestCaseHandler;
import de.hk.bfit.model.TestCase;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DefinedTestCaseProcessor extends TestCaseProcessor{

    private final Logger logger = Logger.getLogger(DefinedTestCaseProcessor.class);

    public void generateExampleTestCase(String filename, List<String> sqlListReferenceAction) throws SQLException, IllegalArgumentException, JAXBException, IOException {
        TestCase newTestCase = generateTestCase(sqlListReferenceAction);
        TestCaseHandler.writeTestcase(newTestCase,filename);
    }



}
