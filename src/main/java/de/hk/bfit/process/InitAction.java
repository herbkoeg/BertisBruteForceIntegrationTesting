package de.hk.bfit.process;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InitAction extends BaseAction{

    
    public InitAction() {
        this.description = "define your init commands here";
        this.sqlCommmand = new ArrayList<String>();
        this.rollBackOnError = false;
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
