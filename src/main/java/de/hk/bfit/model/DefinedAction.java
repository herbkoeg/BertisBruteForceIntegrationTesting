package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefinedAction extends ExecutionAction {

    public String name;
    @JacksonXmlElementWrapper(localName = "ignorableSqlCommands")
    @JacksonXmlProperty(localName = "ignorableSqlCommand")
    protected List<IgnorableSqlCommand> ignorableSqlCommands;


    public DefinedAction() {
        this.description = "Describe your defined action here";
    }

    public DefinedAction(String name) {
        super();
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IgnorableSqlCommand> getIgnorableSqlCommands() {
        if (ignorableSqlCommands == null) {
            ignorableSqlCommands = new ArrayList<>();
        }
        return ignorableSqlCommands;
    }

    public void setIgnorableSqlCommands(List<IgnorableSqlCommand> definedSqlCommands) {
        this.ignorableSqlCommands = definedSqlCommands;
    }
}
