package de.hk.bfit.process;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InitAction extends BaseAction{

    
    public InitAction() {
        this.description = "define your init commands here";
        this.sqlCommmands = new ArrayList<>();
        this.sqlCommmands.add("insert into xyz ...");
        this.sqlCommmands.add("update xyz where ...");
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

    public void setSqlCommands(List<String> initCommands) {
        this.sqlCommmands = initCommands;
    }
    
    
}
