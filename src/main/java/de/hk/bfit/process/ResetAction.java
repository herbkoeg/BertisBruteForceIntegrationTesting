package de.hk.bfit.process;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlAccessorType (XmlAccessType.FIELD)
public class ResetAction extends BaseAction{

    public ResetAction() {
        this.description = "define your reset commands here";
        this.sqlCommmand = new ArrayList<>();
        this.sqlCommmand.add("delete from xyz where ...");
        this.sqlCommmand.add("update xyz where ...");
        this.rollBackOnError = true;
    }
    
    public ResetAction(String description) {
        this.description = description;
        this.sqlCommmand = new ArrayList<>();
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
