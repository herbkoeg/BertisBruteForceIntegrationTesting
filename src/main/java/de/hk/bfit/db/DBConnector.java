package de.hk.bfit.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    
    private static final String jdbcUrl = "jdbc:postgresql://localhost:5432/bertisDB";
    private static final String DB_DRIVER =  "org.postgresql.Driver";
    
        public static Connection getDBConnection() throws ClassNotFoundException, SQLException {
		Connection dbConnection = null;
		Class.forName(DB_DRIVER);
		String username = "berti";
		String passwort = "berti";
		dbConnection = DriverManager.getConnection(jdbcUrl, username, passwort);
		return dbConnection;
        }
}
