/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hk.bfit.io;

import java.util.List;

/**
 *
 * @author palmherby
 */
public class SqlReferenzFile {
    
    private String testSql;
    private List<String> testResult;
    private List<String> resetCommand;
    
    
    public String getTestSql() {
        return testSql;
    }

    public List<String> getTestResult() {
        return testResult;
    }

    public List<String> getResetCommand() {
        return resetCommand;
    }
    
    
}
