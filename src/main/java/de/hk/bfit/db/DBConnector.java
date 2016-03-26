package de.hk.bfit.db;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DBConnector {
    
    protected String jdbcUrl;
    protected String user;
    protected String password;
    
    abstract public Connection getDBConnection() throws ClassNotFoundException, SQLException;

    public DBConnector(String jdbcUrl, String user, String password) {
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
    }
    
    
}
