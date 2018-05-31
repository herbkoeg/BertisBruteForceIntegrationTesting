package de.hk.bfit.process;

public class AssertResult {

    private String sql;
    private String actual;
    private String reference;
    private String difference;


    public AssertResult(String sql, int actual, int reference, String difference) {
        this.sql = sql;
        this.actual = String.valueOf(actual);
        this.reference = String.valueOf(reference);
        this.difference = difference;
    }

    public AssertResult(String sql, String actual, String reference, String difference) {
        this.sql = sql;
        this.actual = actual;
        this.reference = reference;
        this.difference = difference;
    }

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "AssertResult{" +
                "sql='" + sql + '\'' +
                ", actual='" + actual + '\'' +
                ", reference='" + reference + '\'' +
                ", difference='" + difference + '\'' +
                '}';
    }

}
