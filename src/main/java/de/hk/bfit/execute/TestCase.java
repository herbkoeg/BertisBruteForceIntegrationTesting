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
    private String assertCommand;
    @XmlElement
    private List<String> testResult;
    @XmlElement
    private List<String> resetCommand;
    @XmlElement
    private List<String> initCommand;

    public SqlReferenzFile() {
    }

    public SqlReferenzFile(String description, String testSql, List<String> testResult, List<String> resetCommand) {
        this.description = description;
        this.assertCommand = testSql;
        this.testResult = testResult;
        this.resetCommand = resetCommand;
    }

    public String getTestSql() {
        return assertCommand;
    }

    public List<String> getTestResult() {
        return testResult;
    }

    public List<String> getResetCommand() {
        return resetCommand;
    }

    public SqlReferenzFile(String description, String assertCommand, List<String> testResult, List<String> resetCommand, List<String> initCommand) {
        this.description = description;
        this.assertCommand = assertCommand;
        this.testResult = testResult;
        this.resetCommand = resetCommand;
        this.initCommand = initCommand;
    }

    public String getDescription() {
        return description;
    }

    public String getAssertCommand() {
        return assertCommand;
    }

    public List<String> getInitCommand() {
        return initCommand;
    }

}
