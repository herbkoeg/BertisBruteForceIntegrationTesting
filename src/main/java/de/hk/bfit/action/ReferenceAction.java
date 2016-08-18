package de.hk.bfit.action;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReferenceAction {
    private String description;
    private List<SelectAction> selectAction;
    
    public ReferenceAction() {
    }

    public ReferenceAction(String description, List<SelectAction> selectAction) {
        this.description = description;
        this.selectAction = selectAction;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement
    public List<SelectAction> getSelectAction() {
        return selectAction;
    }

    public void setSelectAction(List<SelectAction> selectAction) {
        this.selectAction = selectAction;
    }

}
