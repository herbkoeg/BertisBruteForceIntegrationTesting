package de.hk.bfit.process;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "ResetAction" )
public class ResetAction extends BaseAction{

    public ResetAction() {
        this.description = "define your reset commands here";
        this.commands = new ArrayList<>();
        this.commands.add("delete from xyz where ...");
        this.commands.add("update xyz where ...");
    }
    
    public ResetAction(String description) {
        this.description = description;
        this.commands = new ArrayList<>();
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement
    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
    
    
    
}
