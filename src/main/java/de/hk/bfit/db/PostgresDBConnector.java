package de.hk.bfit.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDBConnector extends DBConnector {
    
    private static final String DB_DRIVER =  "org.postgresql.Driver";

    public PostgresDBConnector(String jdbcUrl, String user, String password) {
        super(jdbcUrl, user, password);
    }
    
        public  Connection getDBConnection() throws ClassNotFoundException, SQLException {
		Connection dbConnection = null;
		Class.forName(DB_DRIVER);
		dbConnection = DriverManager.getConnection(jdbcUrl, user, password);
		return dbConnection;
        }
        
        
}
