package org.storemate.pos;

import javafx.application.Application;
import javafx.stage.Stage;
import org.storemate.pos.database.DBTable;
import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.utils.SceneManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException, SQLException {

        /*
        // Uncomment this section if you want to create the table on startup
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(DBTable.CREATE_SHIFT_TABLE)) {
            int rows = statement.executeUpdate();
            if (rows > 0) {
                System.out.println("EMPLOYEE Table Created successfully");
            } else {
                System.out.println("Error creating shift Table");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
*/
        // Initialize SceneManager and show the login form
        SceneManager.init(stage);
        SceneManager.showEmployeeLoginForm();
    }
}

