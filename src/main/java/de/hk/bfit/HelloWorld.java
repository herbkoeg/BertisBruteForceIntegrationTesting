/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hk.bfit;

import de.hk.bfit.db.DBConnector;
import de.hk.bfit.io.FileAdapter;
import de.hk.bfit.process.TestCaseProcessor;
import java.sql.Connection;

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
        
        TestCaseProcessor testCaseProcessor = new TestCaseProcessor();
        
        testCaseProcessor.generateExampleTestCase("jawoi", "ein Test", "select * from Person");
        
//        TestCase testCase = FileAdapter.loadTestCase("jawoi");
//        testCaseProcessor.assertReferences(testCase, variables, true);
        
    }
}
