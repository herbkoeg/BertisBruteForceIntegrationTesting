package de.hk.bfit.process;

import de.hk.bfit.model.TestCase;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public interface ITestCaseProcessor {

    void assertBefore(TestCase testCase) throws SQLException;

    void assertBefore(TestCase testCase, Map<String, String> variables) throws SQLException;

    void assertAfter(TestCase testCase) throws SQLException;

    void assertAfter(TestCase testCase, Map<String, String> variables) throws SQLException;

    List<AssertResult> getDifferencesAfter(TestCase testcase) throws SQLException;

    List<AssertResult> getDifferencesAfter(TestCase testcase, Map<String, String> variables) throws SQLException;

    List<AssertResult> getDifferencesBefore(TestCase testcase) throws SQLException;

    List<AssertResult> getDifferencesBefore(TestCase testcase, Map<String, String> variables) throws SQLException;

    void processResetAction(TestCase testCase, Map<String, String> variables) throws SQLException;

    void processInitAction(TestCase testCase, Map<String, String> variables) throws SQLException;

    void execSql(String sql) throws SQLException;

    void execSql(String sql, Map<String, String> variables, boolean autoCommit) throws SQLException;

    Properties getClientInfo() throws SQLException;


}
