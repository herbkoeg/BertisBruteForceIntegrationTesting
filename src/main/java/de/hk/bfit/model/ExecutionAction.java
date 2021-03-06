package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
abstract class ExecutionAction extends BfiAction {

    @JacksonXmlElementWrapper(localName = "sqlCommands")
    @JacksonXmlProperty(localName = "sqlCommand")
    List<String> sqlCommands;
    Boolean rollBackOnError;

    public List<String> getSqlCommands() {
        return sqlCommands;
    }

    public void setSqlCommands(List<String> sqlCommands) {
        this.sqlCommands = sqlCommands;
    }

    public Boolean isRollBackOnError() {
        return rollBackOnError;
    }

    public void setRollBackOnError(Boolean rollBackOnError) {
        this.rollBackOnError = rollBackOnError;
    }

    public void addSqlCommand(String sqlCommand) {
        if(getSqlCommands() == null) {
            sqlCommands = new ArrayList<>();
        }
        sqlCommands.add(sqlCommand);
    }

}
