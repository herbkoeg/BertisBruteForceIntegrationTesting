/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hk.bfit;

import de.hk.bfit.db.DBConnectorImpl;
import de.hk.bfit.io.TestCaseGenerator;
import de.hk.bfit.io.TestCaseHandler;
import de.hk.bfit.model.DefinedExecutionAction;
import de.hk.bfit.model.SelectCmd;
import de.hk.bfit.model.TestCase;
import de.hk.bfit.process.IBfiTest;
import de.hk.bfit.process.TestCaseProcessor;
import de.hk.bfit.process.enums.Replacements;
import org.junit.ComparisonFailure;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.hk.bfit.HelloWorldHelper.createAsserTestCase;
import static de.hk.bfit.HelloWorldHelper.createInitTestCase;
import static de.hk.bfit.helper.BfiRegEx.TIMESTAMP;
import static de.hk.bfit.io.TestCaseHandler.loadTestCase;
import static de.hk.bfit.io.TestCaseHandler.writeTestcase;

/**
 * @author palmherby
 */
class HelloWorldWithArgs implements IBfiTest {

    /* you need a table 'testperson' to run this example ...
    CREATE TABLE TESTPERSON
    (
     id integer,
     name char(255),
     vorname char(255),
     adresse char(255),
     stadt char(255),
     message char(255)

     */

    public static void main(String[] args) throws Exception {
        // DBConnectorImpl dBConnector = new DBConnectorImpl(JDBC_POSTGRESQL_MY_DB,DB_USER,DB_PASSWORD);
        DBConnectorImpl dBConnector = new DBConnectorImpl(args[0],args[1],args[2]);

        Connection dbConnection = dBConnector.getDBConnection();
        TestCaseProcessor tcp = new TestCaseProcessor(dBConnector.getDBConnection());

        Map<String, String> variables = new HashMap<>();

        // generate init
        System.err.println("-----------> (1) generate and process initialisation testcase ...");
        TestCase generatedInitTestcase = createInitTestCase(dbConnection);
        writeTestcase(generatedInitTestcase, BASE_PATH_GENERATED + "initTestcase.xml");
        // now have a look on initTestcase.xml in your IDE: there are two actions: deletePersons and insertPersons !

        tcp.processDefinedAction(generatedInitTestcase, "deletePersons");
        tcp.processDefinedAction(generatedInitTestcase, "insertPersons");

        System.err.println("-----------> (2) generate assert testcase ...");
        String assertFilename = BASE_PATH_GENERATED+"assertTestCase.xml";
        // now have a look on assertTestCase.xml in your IDE: there are your selects and the results !

        TestCase generatedAssertTestcase = createAsserTestCase(dbConnection);
        writeTestcase(generatedAssertTestcase,assertFilename);

        System.err.println("-----------> (3) first assert -> everything is fine ...");
        TestCase assertTestcase = loadTestCase(assertFilename);
        tcp.assertAfter(assertTestcase);

        System.err.println("-----------> (4) update DB: timestamp changes");
        tcp.execSql("update testperson set message ='some timestamp: 2020-06-08 00:00:00.000' where id=102");

        // -> assert will fail
        try {
            tcp.assertAfter(assertTestcase);
        } catch (ComparisonFailure ex) {
            System.err.println(ex.getMessage());
        }
        // show me the differences
        System.err.println(tcp.getDifferencesAfter(assertTestcase));

        System.err.println("-----------> (5) ignore the timestamp using regex");
        // now we are setting an regex to ignore the timestamp
        SelectCmd selectCmd = assertTestcase.getReferenceActionAfter().getSelectCmds().get(0);
        selectCmd.addFilterExpression(TIMESTAMP);
        writeTestcase(assertTestcase,BASE_PATH_GENERATED+"assertTestcaseWithRegexFilter.xml");
        // now have a look on assertTestcaseWithRegexFilter.xml in your IDE: there is a filterexpression added !

        // -> no differences, all timestamps are filtered out
        tcp.assertAfter(assertTestcase);

        System.err.println("-----------> (6) update DB: Silberh[ö]rn -> Silberh[oe]rn");
        // change on DB: example replacements: Silberh[ö]rn -> Silberh[oe]rn
        tcp.execSql("update testperson set name = 'silberhoern' where id=100");

        // -> assert will fail because ö != oe
        System.err.println("(7) Found Differences ö!=oe: " + tcp.getDifferencesAfter(assertTestcase));
        try {
            tcp.assertAfter(assertTestcase);
        } catch (ComparisonFailure ex) {
            System.err.println("assert failed, because ö != oe: " + ex.getMessage());
        }

        System.err.println("(8) ignore umlaute  ");
        // no differences with replace UMLAUTE: Replacements are not part of the testcase, but set on runtime
        // using the testcaseprocessor:
        tcp.assertAfter(assertTestcase,null, Replacements.REPLACE_UMLAUTE);
        System.out.println(tcp.getDifferencesAfter(assertTestcase,null, Replacements.REPLACE_UMLAUTE));


        System.err.println("(9) ignore columns  ");
        tcp.processDefinedAction(generatedInitTestcase, "deletePersons");
        tcp.processDefinedAction(generatedInitTestcase, "insertPersons");
        tcp.execSql("update testperson set name = 'kasperl' where id=100");
        assertTestcase = loadTestCase(assertFilename);  // back to initial Tescase
        assertTestcase.getReferenceActionAfter().getSelectCmds().get(0).addIgnoredColumn("NAME");
        assertTestcase.getReferenceActionAfter().getSelectCmds().get(0).setResults(new ArrayList<String>());
        assertTestcase.getReferenceActionAfter().getSelectCmds().get(0).getResults().add("102;reinhard;maximiliansplatz;muenchen;some timestamp: 2020-06-08 12:12:12.121;");
        assertTestcase.getReferenceActionAfter().getSelectCmds().get(0).getResults().add("101;horst;goetestrasse;muenchen;mathe;");
        assertTestcase.getReferenceActionAfter().getSelectCmds().get(0).getResults().add("100;heinrich;schillerstrasse;muenchen;deutsch;");
        writeTestcase(assertTestcase,BASE_PATH_GENERATED+"assertTestcaseWithColumnIgnore.xml");
        // now have a look on assertTestcaseWithColumnIgnore.xml in your IDE: there is a column added in ignoredColumns!

        tcp.assertAfter(assertTestcase);
        System.out.println(tcp.getDifferencesAfter(assertTestcase));

    }


}
