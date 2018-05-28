package de.hk.bfit.action;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class DefinedAction extends BaseAction{


    public DefinedAction() {
        this.description = "define your init commands here";
        this.sqlCommmand = new ArrayList<String>();
        this.rollBackOnError = false;
    }

    public DefinedAction(String name) {
        this.description = "define your init commands here";
        this.sqlCommmand = new ArrayList<String>();
        this.rollBackOnError = false;
        this.name = name;
    }

    @XmlElement
    public String name;

    @XmlElement
    public String getDescription() {
        return description;
    }

    @XmlElement
    public List<String> getSqlCommands() {
        return sqlCommmand;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSqlCommands(List<String> initCommands) {
        this.sqlCommmand = initCommands;
    }

    public boolean isRollBackOnError() {
        return rollBackOnError;
    }

    public void setRollBackOnError(boolean rollBackOnError) {
        this.rollBackOnError = rollBackOnError;
    }


}
