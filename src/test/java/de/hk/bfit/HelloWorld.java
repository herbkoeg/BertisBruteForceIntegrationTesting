/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hk.bfit;

import de.hk.bfit.db.PostgresDBConnector;
import de.hk.bfit.helper.BfiRegEx;
import de.hk.bfit.io.TestCaseGenerator;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.hk.bfit.helper.BfiRegEx.TIMESTAMP;
import static de.hk.bfit.io.TestCaseHandler.loadTestCase;
import static de.hk.bfit.io.TestCaseHandler.writeTestcase;
import static java.lang.System.currentTimeMillis;

/**
 * @author palmherby
 */
class HelloWorld implements IBfiTest {

    /* @see initializeTestDb.sql
    CREATE TABLE public.person
            (
                    id integer,
                    name character varying(255) COLLATE pg_catalog."default",
                    vorname character varying(255) COLLATE pg_catalog."default",
                    adresse character varying(255) COLLATE pg_catalog."default",
                    stadt character varying(255) COLLATE pg_catalog."default",
                    message character varying(255) COLLATE pg_catalog."default"
            )
     */

    public static void main(String[] args) throws Exception {
        PostgresDBConnector dBConnector =
                new PostgresDBConnector(JDBC_POSTGRESQL_MY_DB, DB_USER, DB_PASSWORD);

        Connection dbConnection = dBConnector.getDBConnection();
        TestCaseProcessor tcp = new TestCaseProcessor(dBConnector.getDBConnection());

        Map<String, String> variables = new HashMap<>();

        // generate init
        TestCase generatedInitTestcase = createInitTestCase(dbConnection);
        writeTestcase(generatedInitTestcase, BASE_PATH_GENERATED + "initTestcase.xml");

        tcp.processDefinedAction(generatedInitTestcase, "deletePersons");
        tcp.processDefinedAction(generatedInitTestcase, "insertPersons");

        // generate assert
        String assertFilename = BASE_PATH_GENERATED+"assertTestCase.xml";

        TestCase generatedAssertTestcase = createAsserTestCase(dbConnection);
        writeTestcase(generatedAssertTestcase,assertFilename);

        // assert
        TestCase assertTestcase = loadTestCase(assertFilename);
        tcp.assertAfter(assertTestcase);

        // change timestamp on DB
        tcp.execSql("update person set message ='some timestamp: 2020-06-08 00:00:00.000' where id=102");

        // -> assert will fail
        try {
            tcp.assertAfter(assertTestcase);
        } catch (ComparisonFailure ex) {
            System.out.println(ex.getMessage());
        }
        // show me the differences without an exception
        System.out.println(tcp.getDifferencesAfter(assertTestcase));

        // now we are setting an regex to ignore the timestamp
        SelectCmd selectCmd = assertTestcase.getReferenceActionAfter().getSelectCmds().get(0);
        selectCmd.addFilterExpression(TIMESTAMP);

        // -> no differences, all timestamps are filtered out
        tcp.assertAfter(assertTestcase);

        // change on DB: example replacements: Silberh[ö]rn -> Silberh[oe]rn
        tcp.execSql("update person set name = 'silberhoern' where id=100");

        // -> assert will fail because ö != oe
        try {
            tcp.assertAfter(assertTestcase);
        } catch (ComparisonFailure ex) {
            System.out.println(ex.getMessage());
        }

        // no differences with replace UMLAUTE
        tcp.assertAfter(assertTestcase,null, Replacements.REPLACE_UMLAUTE);
        System.out.println(tcp.getDifferencesAfter(assertTestcase,null, Replacements.REPLACE_UMLAUTE));
    }

    static TestCase createInitTestCase(Connection dbConnection) throws IOException, SQLException {
        TestCase testCase = new TestCase();
        TestCaseGenerator testCaseGenerator = new TestCaseGenerator(dbConnection);

        // delete
        DefinedExecutionAction deletePersonsAction = new DefinedExecutionAction("deletePersons");
        deletePersonsAction.addSqlCommand("delete from person");
        deletePersonsAction.setRollBackOnError(true);
        testCase.addDefinedExecutionAction(deletePersonsAction);

        // insert
        DefinedExecutionAction initPersonsAction = new DefinedExecutionAction("insertPersons");
        initPersonsAction.addSqlCommand("insert into person (id,name,vorname,adresse,stadt,message) " +
                "values (100,'silberhörn','heinrich','schillerstrasse','muenchen','deutsch')");
        initPersonsAction.addSqlCommand("insert into person (id,name,vorname,adresse,stadt,message) " +
                "values (101,'stippel','horst','goetestrasse','muenchen','mathe')");
        initPersonsAction.addSqlCommand("insert into person (id,name,vorname,adresse,stadt,message) " +
                "values (102,'rosenbeck','reinhard','maximiliansplatz','muenchen','some timestamp: 2020-06-08 12:12:12.121')");
        initPersonsAction.setRollBackOnError(true);
        testCase.addDefinedExecutionAction(initPersonsAction);

        return testCase;
    }

    static TestCase createAsserTestCase(Connection dbConnection) throws IOException, SQLException {
        TestCaseGenerator testCaseGenerator = new TestCaseGenerator(dbConnection);

        List sqlList = new ArrayList();
        sqlList.add("select * from person order by id desc");
        TestCase testCase = testCaseGenerator.generateTestCase(sqlList, null);

        return testCase;
    }

}
