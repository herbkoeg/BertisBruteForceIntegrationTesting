package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IgnorableSqlCommand {
    private Boolean ignoreUnsetVariables;
    private String sqlCommand;

    public IgnorableSqlCommand(String sqlCommand) {
        this.sqlCommand = sqlCommand;
        this.setIgnoreUnsetVariables(false);
    }

    public IgnorableSqlCommand(Boolean isIgnoreSqlException, String sqlCommand) {
        this.ignoreUnsetVariables = isIgnoreSqlException;
        this.sqlCommand = sqlCommand;
    }

    public Boolean isIgnoreSqlException() {
        return ignoreUnsetVariables;
    }

    private void setIgnoreUnsetVariables(Boolean ignoreUnsetVariables) {
        this.ignoreUnsetVariables = ignoreUnsetVariables;
    }

    public String getSqlCommand() {
        return sqlCommand;
    }

    public void setSqlCommand(String sqlCommand) {
        this.sqlCommand = sqlCommand;
    }
}
