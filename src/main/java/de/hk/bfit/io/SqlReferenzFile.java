/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hk.bfit.io;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement
public class SqlReferenzFile {
    @XmlElement
    private String description;
    @XmlElement
    private String testSql;
    @XmlElement
    private List<String> testResult;
    @XmlElement
    private List<String> resetCommand;

    public SqlReferenzFile() {
    }

    public SqlReferenzFile(String description, String testSql, List<String> testResult, List<String> resetCommand) {
        this.description = description;
        this.testSql = testSql;
        this.testResult = testResult;
        this.resetCommand = resetCommand;
    }


    
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
