/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hk.bfit;

import de.hk.bfit.db.DBConnector;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author palmherby
 */
public class HelloWorld {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        System.out.println("Hallo bla blub");
        DBConnector dBConnector = new DBConnector();
        Connection dbConnection = dBConnector.getDBConnection();
        System.out.println(dbConnection.getClientInfo());
        
    }
}
