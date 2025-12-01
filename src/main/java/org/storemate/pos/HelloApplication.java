package org.storemate.pos;

import javafx.application.Application;
import javafx.stage.Stage;
import org.storemate.pos.database.DBTable;
import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.user_interface.CredentialManagement;
import org.storemate.pos.utils.SceneManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Suppliers/Products/Returns/Categories
 * All Tested And now works fine
 */



public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException, SQLException {
        // 1️⃣ Initialize SceneManager with primary stage
        SceneManager.init(stage);

        CredentialManagement cm = CredentialManagement.getInstance();

        // 2️⃣ Check credentials
        if (!cm.credentialsExist()) {
            System.out.println("Credentials file does not exist. Setup required.");
            SceneManager.showDatabaseConnectionForm(); // show DB setup form
            return; // stop further execution
        }

        // 3️⃣ Connect to DB if credentials exist
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(DBTable.CREATE_SHIFT_TABLE)) {

            int rows = statement.executeUpdate();
            if (rows > 0){
                System.out.println("Shift Table Added successfully");
            } else {
                System.out.println("Error creating Shift table");
            }

        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }

        // 4️⃣ Show login form only if credentials exist and DB is ready
        SceneManager.showEmployeeLoginForm();
    }

}
