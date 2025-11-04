package org.storemate.pos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.user_interface.DatabaseConnectionFormView;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException, SQLException {
        DatabaseConnectionFormView databaseConnectionFormView = new DatabaseConnectionFormView();
        Scene scene = databaseConnectionFormView.createScene();
        stage.setScene(scene);
        stage.show();
    }
}
