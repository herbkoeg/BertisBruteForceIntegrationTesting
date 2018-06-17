package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
abstract class ExecutionAction {

    protected String description;
    @JacksonXmlElementWrapper(localName = "sqlCommands")
    @JacksonXmlProperty(localName = "sqlCommand")
    protected List<String> sqlCommands;
    protected Boolean rollBackOnError;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
}