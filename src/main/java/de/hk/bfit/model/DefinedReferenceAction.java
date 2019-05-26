package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefinedReferenceAction extends ReferenceAction {

    private String name;


    public DefinedReferenceAction() {
        this.description = "define your init commands here";
    }

    public DefinedReferenceAction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
