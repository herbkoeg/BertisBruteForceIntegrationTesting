package de.hk.bfit.process;

import de.hk.bfit.action.ReferenceAction;
import de.hk.bfit.action.SelectAction;
import de.hk.bfit.io.FileAdapter;
import de.hk.bfit.io.GenericXmlHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class TestCaseProcessorNew implements ITestCaseProcessor{

    private final Logger logger = Logger.getLogger(TestCaseProcessorNew.class);

    @Override
    public void generateExampleTestCase(String filename, List<String> sqlListReferenceAction) throws SQLException, IllegalArgumentException, JAXBException, IOException {

    }

    @Override
    public void assertBefore(TestCase testCase) throws SQLException {

    }

    @Override
    public void assertBefore(TestCase testCase, Map<String, String> variables) throws SQLException {

    }

    @Override
    public void assertAfter(TestCase testCase) throws SQLException {

    }

    @Override
    public void assertAfter(TestCase testCase, Map<String, String> variables) throws SQLException {

    }

    @Override
    public List<AssertResult> getDifferencesAfter(TestCase testcase) throws SQLException {
        return null;
    }

    @Override
    public List<AssertResult> getDifferencesAfter(TestCase testcase, Map<String, String> variables) throws SQLException {
        return null;
    }

    @Override
    public List<AssertResult> getDifferencesBefore(TestCase testcase) throws SQLException {
        return null;
    }

    @Override
    public List<AssertResult> getDifferencesBefore(TestCase testcase, Map<String, String> variables) throws SQLException {
        return null;
    }

    @Override
    public void processResetAction(TestCase testCase, Map<String, String> variables) throws SQLException {

    }

    @Override
    public void processInitAction(TestCase testCase, Map<String, String> variables) throws SQLException {

    }

    @Override
    public void execSql(String sql) throws SQLException {

    }

    @Override
    public void execSql(String sql, Map<String, String> variables, boolean autoCommit) throws SQLException {

    }
}
