package de.hk.bfit.process;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReferenceAction {
    private String description;
    private String select;
    private List<String> results;
    
    public ReferenceAction() {
    }
    
    public ReferenceAction(String description, String select, List<String> results) {
        this.description = description;
        this.select = select;
        this.results = results;
    }

    @XmlElement
    public List<String> getResults() {
        return results;
    }

    @XmlElement
    public String getSelect() {
        return select;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
