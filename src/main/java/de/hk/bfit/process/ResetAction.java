package de.hk.bfit.process;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "ResetAction" )
public class ResetAction extends BaseAction{

    public ResetAction() {
        this.description = "define your reset commands here";
        this.sqlCommmands = new ArrayList<>();
        this.sqlCommmands.add("delete from xyz where ...");
        this.sqlCommmands.add("update xyz where ...");
    }
    
    public ResetAction(String description) {
        this.description = description;
        this.sqlCommmands = new ArrayList<>();
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
        return sqlCommmands;
    }

    public void setCommands(List<String> commands) {
        this.sqlCommmands = commands;
    }
    
    
    
}
