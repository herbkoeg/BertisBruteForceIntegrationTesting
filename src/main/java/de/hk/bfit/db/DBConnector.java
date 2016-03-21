/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hk.bfit.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author palmherby
 */
public class DBConnector {
    
    private final String jdbcUrl = "jdbc:postgresql://localhost:5432/bertisDB";
    private final String DB_DRIVER =  "org.postgresql.Driver";
    
        public Connection getDBConnection() throws ClassNotFoundException, SQLException {
		Connection dbConnection = null;
		Class.forName(DB_DRIVER);
		String username = "berti";
		String passwort = "berti";
		dbConnection = DriverManager.getConnection(jdbcUrl, username, passwort);
		return dbConnection;
        }
}
