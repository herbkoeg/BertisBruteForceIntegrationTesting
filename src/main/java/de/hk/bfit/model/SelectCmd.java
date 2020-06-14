package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectCmd {

    @JacksonXmlElementWrapper(localName = "filterexpressions")
    @JacksonXmlProperty(localName = "regex")
    private List filterExpressions;
    private String select;
    private String description;
    @JacksonXmlElementWrapper(localName = "results")
    @JacksonXmlProperty(localName = "result")
    private List<String> results;
    @JacksonXmlElementWrapper(localName = "ignoredColumns")
    @JacksonXmlProperty(localName = "column")
    private List<String> ignoredColumns;

    public SelectCmd() {
        this.select = "define your select here";
        List<String> result = new ArrayList<>();
        result.add("result one");
        result.add("result two");
    }

    public SelectCmd(String description, String select, List<String> results) {
        this.select = select;
        this.results = results;
        this.description = description;
    }

    public List getFilterExpressions() { return filterExpressions;}

    public void setFilterExpressions(List filterExpressions) {this.filterExpressions = filterExpressions;}

    public void addFilterExpression(String filterExpression) {
        if (filterExpressions == null) {
            filterExpressions = new ArrayList();
        }
        filterExpressions.add(filterExpression);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIgnoredColumns() {
        return ignoredColumns;
    }

    public void addIgnoredColumn(String column) {
        if(getIgnoredColumns() == null) {
            ignoredColumns = new ArrayList<>();
        }
        ignoredColumns.add(column);
    }

    public void setIgnoredColumns(List<String> ignoredColumns) {
        this.ignoredColumns = ignoredColumns;
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
