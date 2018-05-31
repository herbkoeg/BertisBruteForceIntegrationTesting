package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefinedAction extends ExecutionAction {


    public DefinedAction() {
        this.description = "define your init commands here";
        this.sqlCommands = new ArrayList<String>();
        this.rollBackOnError = false;
    }

    public DefinedAction(String name) {
        this.description = "define your init commands here";
        this.sqlCommands = new ArrayList<String>();
        this.rollBackOnError = false;
        this.name = name;
    }

    public String name;

    public String getDescription() {
        return description;
    }

    public List<String> getSqlCommands() {
        return sqlCommands;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSqlCommands(List<String> initCommands) {
        this.sqlCommands = initCommands;
    }

    public void setRollBackOnError(boolean rollBackOnError) {
        this.rollBackOnError = rollBackOnError;
    }


}
