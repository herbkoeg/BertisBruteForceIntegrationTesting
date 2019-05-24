package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefinedExecutionAction extends ExecutionAction {

    private String name;


    public DefinedExecutionAction() {
        this.description = "define your init commands here";
        this.sqlCommands = new ArrayList<>();
        this.rollBackOnError = false;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
