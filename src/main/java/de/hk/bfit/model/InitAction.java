package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitAction extends ExecutionAction {


    public InitAction() {
        this.description = "define your init commands here";
        this.sqlCommands = new ArrayList<>();
        this.rollBackOnError = false;
    }

}
