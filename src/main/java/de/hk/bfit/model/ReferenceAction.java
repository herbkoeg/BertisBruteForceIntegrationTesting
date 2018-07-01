package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferenceAction extends BfiAction {

    @JacksonXmlElementWrapper(localName = "selectCmds")
    @JacksonXmlProperty(localName = "selectCmd")
    private List<SelectCmd> selectCmds;

    public ReferenceAction() {
    }

    public ReferenceAction(String description, List<SelectCmd> selectCmds) {
        this.description = description;
        this.selectCmds = selectCmds;
    }

    public List<SelectCmd> getSelectCmds() {
        return selectCmds;
    }

    public void setSelectCmds(List<SelectCmd> selectCmds) {
        this.selectCmds = selectCmds;
    }
}
