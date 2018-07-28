package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResetAction extends ExecutionAction {

    public ResetAction() {
        this.description = "define your reset commands here";
        this.sqlCommands = new ArrayList<>();
        this.rollBackOnError = true;
    }

    public ResetAction(String description) {
        this.description = description;
        this.sqlCommands = new ArrayList<>();
    }

}
