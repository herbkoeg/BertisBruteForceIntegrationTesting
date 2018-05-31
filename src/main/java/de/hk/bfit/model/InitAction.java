package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitAction extends ExecutionAction {

    
    public InitAction() {
        this.description = "define your init commands here";
        this.sqlCommands = new ArrayList<String>();
        this.rollBackOnError = false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSqlCommands() {
        return sqlCommands;
    }

    public void setSqlCommands(List<String> initCommands) {
        this.sqlCommands = initCommands;
    }

    public void setRollBackOnError(boolean rollBackOnError) {
        this.rollBackOnError = rollBackOnError;
    }

}
