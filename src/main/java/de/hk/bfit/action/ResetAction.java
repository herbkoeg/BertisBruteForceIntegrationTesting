package de.hk.bfit.action;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResetAction extends BaseAction{

    public ResetAction() {
        this.description = "define your reset commands here";
        this.sqlCommmand = new ArrayList<String>();
        this.rollBackOnError = true;
    }
    
    public ResetAction(String description) {
        this.description = description;
        this.sqlCommmand = new ArrayList<String>();
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement
    public List<String> getSqlCommands() {
        return sqlCommmand;
    }

    public void setCommands(List<String> commands) {
        this.sqlCommmand = commands;
    }

    public boolean isRollBackOnError() {
        return rollBackOnError;
    }

    public void setRollBackOnError(boolean rollBackOnError) {
        this.rollBackOnError = rollBackOnError;
    }

}
