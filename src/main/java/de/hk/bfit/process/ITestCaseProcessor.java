package de.hk.bfit.process;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ITestCaseProcessor {

    public void generateExampleTestCase(String filename, List<String> sqlListReferenceAction) throws SQLException, IllegalArgumentException, JAXBException, IOException ;
    public void assertBefore(TestCase testCase) throws  SQLException;
    public void assertBefore(TestCase testCase, Map<String, String> variables) throws SQLException ;
    public void assertAfter(TestCase testCase) throws SQLException;
    public void assertAfter(TestCase testCase, Map<String, String> variables) throws SQLException;
    public List<AssertResult> getDifferencesAfter(TestCase testcase) throws SQLException;
    public List<AssertResult> getDifferencesAfter(TestCase testcase, Map<String, String> variables) throws SQLException;
    public List<AssertResult> getDifferencesBefore(TestCase testcase) throws SQLException;
    public List<AssertResult> getDifferencesBefore(TestCase testcase, Map<String, String> variables) throws SQLException;
    public void processResetAction(TestCase testCase, Map<String, String> variables) throws SQLException;
    public void processInitAction(TestCase testCase, Map<String, String> variables) throws SQLException;
    public void execSql(String sql) throws SQLException;
    public void execSql(String sql, Map<String, String> variables, boolean autoCommit) throws SQLException;



}
