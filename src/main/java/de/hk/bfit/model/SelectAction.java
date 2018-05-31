package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectAction {

    protected String description;
    protected String select;

    @JacksonXmlElementWrapper(localName = "results")
    @JacksonXmlProperty(localName = "result")
    protected List<String> results;

    @JacksonXmlElementWrapper(localName = "ignoredColumns")
    @JacksonXmlProperty(localName = "column")
    protected List<String> ignoredColumns;

    public SelectAction() {
        this.select = "define your select here";
        List<String> result = new ArrayList<String>();
        result.add("result one");
        result.add("result two");
    }

    public SelectAction(String description, String select, List<String> results) {
        this.select = select;
        this.results = results;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

    @JsonIgnore
    public String getAssertFailedMessage() {
        return getDescription() + " -> " + getSelect() + " failed:\n";
    }

}
