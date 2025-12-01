package org.storemate.pos.database;

import org.storemate.pos.user_interface.CredentialManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Utility class to manage database connections.
 *
 * <p>This class provides a single method {@link #getConnection()} to obtain
 * a {@link Connection} object to the configured database using credentials
 * provided by {@link org.storemate.pos.user_interface.CredentialManagement}.
 *
 * <p>Note: Each call to {@link #getConnection()} currently creates a new connection.
 * Connection pooling is not implemented.
 * @author Jeet
 */
public class DatabaseConnector {
    private static Connection connection;

    /**
     * Returns a {@link Connection} to the database.
     *
     * <p>Uses credentials (URL, database name, username, password) from
     * {@link org.storemate.pos.user_interface.CredentialManagement}.
     *
     * @return a {@link Connection} object if successful; may return null if connection fails
     */
    public static Connection getConnection() {
        try {

            String url = CredentialManagement.getInstance().getDBUrl();
            String dbName = CredentialManagement.getInstance().getDBName();
            String username = CredentialManagement.getInstance().getDBUsername();
            String password = CredentialManagement.getInstance().getDBPassword();
            String dbUrl = url + "/" + dbName;
            System.out.println("Url: " + url);
            System.out.println("DBNAME: " + dbName);
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("DB_FULL_URL: " + dbUrl);
            connection = DriverManager.getConnection(dbUrl, username, password);
            if (connection != null) {
                System.out.println("Database connection established");
            } else {
                System.out.println("Database connection not established");
            }
        } catch (
                SQLException ex) {
            System.out.println("SQL EXCEPTION: " + ex.getMessage());
        } catch (
                Exception ex) {
            System.out.println("Generic Exception: " + ex.getMessage());
        }
        return connection;
    }
}
