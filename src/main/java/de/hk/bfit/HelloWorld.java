/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hk.bfit;

import de.hk.bfit.db.DBConnector;
import de.hk.bfit.io.FileAdapter;
import de.hk.bfit.process.TestCase;
import de.hk.bfit.process.TestCaseProcessor;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author palmherby
 */
public class HelloWorld {
    public static void main(String[] args) throws Exception{
        System.out.println("Hallo bla blub");
        DBConnector dBConnector = new DBConnector();
        Connection dbConnection = dBConnector.getDBConnection();
        System.out.println(dbConnection.getClientInfo());
        
        Map<String,String> variables = new HashMap<>();
        TestCaseProcessor testCaseProcessor = new TestCaseProcessor(DBConnector.getDBConnection());
        
        testCaseProcessor.generateExampleTestCase("BfitErsterTest.xml", "Dies ist ein neuer TestCase", "select * from Person");
        
//        TestCase testCase = FileAdapter.loadTestCase("jawoi");
//        testCaseProcessor.assertReferences(testCase, variables, true);
        
    }
}
