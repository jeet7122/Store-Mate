package org.storemate.pos.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.storemate.pos.user_interface.DatabaseConnectionFormView;
import org.storemate.pos.user_interface.EmployeeLoginFormView;
import org.storemate.pos.user_interface.EmployeeRegistrationFormView;


public class SceneManager {

    private static Stage primaryStage;

    public static void init(Stage stage){
        primaryStage = stage;
    }

    public static void showRegistrationForm(){
        DatabaseConnectionFormView form = new DatabaseConnectionFormView();
        Scene scene = new Scene(form.getView(), 600, 500);
        scene.getStylesheets().add(SceneManager.class.getResource("/css/index.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public static void showEmployeeRegistrationForm(){
        EmployeeRegistrationFormView form = new EmployeeRegistrationFormView();
        Scene scene = new Scene(form.getView(), 600,500);
        scene.getStylesheets().add(SceneManager.class.getResource("/css/index.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public static void showEmployeeLoginForm(){
        EmployeeLoginFormView view = new EmployeeLoginFormView();
        Scene scene = new Scene(view.getView(), 600,500);
        scene.getStylesheets().add(SceneManager.class.getResource("/css/index.css").toExternalForm());
        primaryStage.setScene(scene);
    }
}
