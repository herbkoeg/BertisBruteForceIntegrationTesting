/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hk.bfit;

import de.hk.bfit.db.DBConnectorImpl;
import de.hk.bfit.io.TestCaseGenerator;
import de.hk.bfit.model.SelectCmd;
import de.hk.bfit.model.TestCase;
import de.hk.bfit.process.IBfiTest;
import de.hk.bfit.process.TestCaseProcessor;
import de.hk.bfit.process.enums.Replacements;
import org.junit.ComparisonFailure;

import java.sql.Connection;
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
class HelloWorld2WithArgs implements IBfiTest {

    /* @see initializeTestDb.sql
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
        DBConnectorImpl dBConnector = new DBConnectorImpl(args[0],args[1],args[2]);

        Connection dbConnection = dBConnector.getDBConnection();
        TestCaseProcessor tcp = new TestCaseProcessor(dBConnector.getDBConnection());
        TestCase assertTestcase = loadTestCase(BASE_PATH_TESTCASES + "BfiTestCaseWithEmptyResult.xml");

    }


}
