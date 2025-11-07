package org.storemate.pos.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.storemate.pos.database.DBConstants.*;


public class DatabaseConnector {

    private static Connection connection;
    public static Connection getConnection(){
        try
                (
                        InputStream stream = DatabaseConnector.class.getResourceAsStream("credentials.properties")
                        )
        {



            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            if(connection != null){
                System.out.println("Database connection established");
            }
            else {
                System.out.println("Database connection not established");
            }
        }
        catch (SQLException ex){
            System.out.println("SQL EXCEPTION: " + ex.getMessage());
        }
        catch (Exception ex){
            System.out.println("Generic Exception: " + ex.getMessage());
        }
        return connection;
    }
}
