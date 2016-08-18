/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hk.bfit;

import de.hk.bfit.db.PostgresDBConnector;
import de.hk.bfit.io.FileAdapter;
import de.hk.bfit.process.TestCase;
import de.hk.bfit.process.TestCaseProcessor;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author palmherby
 */
public class HelloWorld {
    public static void main(String[] args) throws Exception{
        System.out.println("Hallo bla blub");
        PostgresDBConnector dBConnector = 
                new PostgresDBConnector("jdbc:postgresql://localhost:5432/bertisDB", "berti", "berti");
        
        Connection dbConnection = dBConnector.getDBConnection();
        System.out.println(dbConnection.getClientInfo());
        
        
        for (int i = 0; i < 10; i++) {
            
        Map<String,String> variables = new HashMap<String,String>();
        TestCaseProcessor testCaseProcessor = new TestCaseProcessor(dBConnector.getDBConnection());
        
        String filename = "BfitFirstTest.xml";
        List<String> sqlList = new ArrayList<String>();
        sqlList.add("select id from Person");
        sqlList.add("select name from Person");
        
        
        testCaseProcessor.generateExampleTestCase(filename, sqlList);
        
        TestCase testCase = FileAdapter.loadTestCase(filename);

        testCaseProcessor.assertAfter(testCase,variables);

        testCaseProcessor.closeConnection();
        
        System.out.println(testCase.getDescription());
        }
        
    }
}
