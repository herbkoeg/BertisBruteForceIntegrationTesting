package de.hk.bfit;

import de.hk.bfit.io.TestCaseGenerator;
import de.hk.bfit.model.DefinedExecutionAction;
import de.hk.bfit.model.TestCase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HelloWorldHelper {

    static TestCase createInitTestCase(Connection dbConnection) throws IOException, SQLException {
        TestCase testCase = new TestCase();
        TestCaseGenerator testCaseGenerator = new TestCaseGenerator(dbConnection);

        // delete
        DefinedExecutionAction deletePersonsAction = new DefinedExecutionAction("deletePersons");
        deletePersonsAction.addSqlCommand("delete from testperson");
        deletePersonsAction.setRollBackOnError(true);
        testCase.addDefinedExecutionAction(deletePersonsAction);

        // insert
        DefinedExecutionAction initPersonsAction = new DefinedExecutionAction("insertPersons");
        initPersonsAction.addSqlCommand("insert into testperson (id,name,vorname,adresse,stadt,message) " +
                "values (100,'silberhörn','heinrich','schillerstrasse','muenchen','deutsch')");
        initPersonsAction.addSqlCommand("insert into testperson (id,name,vorname,adresse,stadt,message) " +
                "values (101,'stippel','horst','goetestrasse','muenchen','mathe')");
        initPersonsAction.addSqlCommand("insert into testperson (id,name,vorname,adresse,stadt,message) " +
                "values (102,'rosenbeck','reinhard','maximiliansplatz','muenchen','some timestamp: 2020-06-08 12:12:12.121')");
        initPersonsAction.setRollBackOnError(true);
        testCase.addDefinedExecutionAction(initPersonsAction);

        return testCase;
    }

    static TestCase createAsserTestCase(Connection dbConnection) throws IOException, SQLException {
        TestCaseGenerator testCaseGenerator = new TestCaseGenerator(dbConnection);

        List sqlList = new ArrayList();
        sqlList.add("select * from testperson order by id desc");
        TestCase testCase = testCaseGenerator.generateTestCaseWithReferenceAfter(sqlList, null);

        return testCase;
    }

}
