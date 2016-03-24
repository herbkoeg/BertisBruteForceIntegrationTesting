package de.hk.bfit.process;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "InitAction" )
public class InitAction extends BaseAction{

    
    public InitAction() {
        this.description = "define your init commands here";
        this.commands = new ArrayList<>();
        this.commands.add("insert into xyz ...");
        this.commands.add("update xyz where ...");
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

    public void setCommands(List<String> initCommands) {
        this.commands = initCommands;
    }
    
    
}
