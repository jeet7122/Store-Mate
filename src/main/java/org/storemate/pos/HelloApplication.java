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

     /*  // try(
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement(DBTable.CREATE_PRODUCT_SUPPLIER_TABLE)
                )
        {
            int rows = statement.executeUpdate();
            if (rows > 0){
                System.out.println("Shift Table Added successfully");
            }
            else {
                System.out.println("Error");
            }*/

        SceneManager.init(stage);
        SceneManager.showEmployeeLoginForm();
    }
}
