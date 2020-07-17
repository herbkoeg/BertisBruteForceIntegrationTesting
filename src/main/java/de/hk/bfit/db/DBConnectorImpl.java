package de.hk.bfit.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectorImpl extends DBConnector {

    public DBConnectorImpl(String jdbcUrl, String user, String password) {
        super(jdbcUrl, user, password);
    }

    public Connection getDBConnection() throws ClassNotFoundException, SQLException {
        Connection dbConnection = null;
        dbConnection = DriverManager.getConnection(jdbcUrl, user, password);
        return dbConnection;
    }

}
