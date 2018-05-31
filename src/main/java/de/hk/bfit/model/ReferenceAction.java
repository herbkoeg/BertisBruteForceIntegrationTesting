package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferenceAction {
    private String description;

    @JacksonXmlElementWrapper(localName = "selectActions")
    @JacksonXmlProperty(localName = "selectAction")
    private List<SelectAction> selectActions;
    
    public ReferenceAction() {
    }

    public ReferenceAction(String description, List<SelectAction> selectActions) {
        this.description = description;
        this.selectActions = selectActions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<SelectAction> getSelectActions() {
        return selectActions;
    }

    public void setSelectActions(List<SelectAction> selectActions) {
        this.selectActions = selectActions;
    }
}
