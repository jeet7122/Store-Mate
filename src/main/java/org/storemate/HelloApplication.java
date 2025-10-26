package org.storemate;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Label label = new Label("Hello World!");
        Scene scene = new Scene(label, 300, 300);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        String url = "jdbc:postgresql://localhost:5432/hms";
        String user = "postgres";
        String password = "Khushi@26";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            if(connection != null){
                System.out.println("Connected to database successfully");
            }
            else{
                System.out.println("Failed to connect to database");
            }
        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
        }
        catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }
    }
}
