package de.hk.bfit.db;

import java.sql.Connection;
import java.sql.SQLException;

abstract class DBConnector {

    final String jdbcUrl;
    final String user;
    final String password;

    DBConnector(String jdbcUrl, String user, String password) {
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
    }

    abstract public Connection getDBConnection() throws ClassNotFoundException, SQLException;


}
