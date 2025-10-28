package org.storemate.pos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.storemate.pos.database.DatabaseConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException, SQLException {
        Label label = new Label("Hello World!");
        Scene scene = new Scene(label, 300, 300);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();



        // TODO : Discuss with professor about the connection string
        try {
            Connection connection = DatabaseConnector.getConnection();
        }
        catch (Exception e) {
            System.out.println("Connection Failed! Check output console" + e.toString());
        }
    }
}
