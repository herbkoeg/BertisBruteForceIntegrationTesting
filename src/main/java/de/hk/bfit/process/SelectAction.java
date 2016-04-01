package de.hk.bfit.process;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SelectAction {

    private String description;
    private String select;
    private List<String> result;

    public SelectAction() {
        this.select = "define your select here";
        List<String> result =  new ArrayList<String>() ;
        result.add("result one");
        result.add("result two");
    }

    public SelectAction(String description, String select, List<String> result) {
        this.select = select;
        this.result = result;
        this.description = description;
    }
    
    @XmlElement
    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    @XmlElement
    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    

    
}
