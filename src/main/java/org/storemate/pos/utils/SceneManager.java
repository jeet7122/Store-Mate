package org.storemate.pos.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.storemate.pos.routing.AccessDenied;
import org.storemate.pos.routing.Router;
import org.storemate.pos.user_interface.RouterPageDemo;
import org.storemate.pos.user_interface.views.MainLayoutView;
import org.storemate.pos.utils.Session;
import org.storemate.pos.user_interface.EmployeeLoginFormView;

public class SceneManager {

    private static Stage primaryStage;
    private static MainLayoutView mainLayoutView;

    /** Initialize with primary Stage */
    public static void init(Stage stage) {
        primaryStage = stage;
        Router.init(stage);
    }

    /** Show the login form where user sets the current user */
    public static void showEmployeeLoginForm() {
        EmployeeLoginFormView view = new EmployeeLoginFormView();
        Scene scene = new Scene(view.getView(), 1024, 768);
        scene.getStylesheets().add(SceneManager.class.getResource("/css/index.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /** Show a generic form (Database connection / Registration / etc.) */
    public static void showForm(javafx.scene.Parent formView, int width, int height) {
        Scene scene = new Scene(formView, width, height);
        scene.getStylesheets().add(SceneManager.class.getResource("/css/index.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    /** Show the main application after successful login */
    public static void showHomePage() {
        if (Session.getInstance().getCurrentUser() == null) {
            System.out.println("No user is logged in! Redirecting to login form.");
            showEmployeeLoginForm();
            return;
        }
        mainLayoutView = new MainLayoutView();

        // Load main layout (RouterPageDemo or similar)
        Scene scene = new Scene(mainLayoutView.getView(), 1024, 768);
        scene.getStylesheets().add(SceneManager.class.getResource("/css/index.css").toExternalForm());
        scene.getStylesheets().add(SceneManager.class.getResource("/css/dashboard.css").toExternalForm());
        primaryStage.setScene(scene);

        // Default route after login
        Router.navigate("/dashboard");
    }

    /** Convenience method to navigate using Router */
    public static void navigate(String route) {
        Router.navigate(route);
    }

    /** Optional: Show Access Denied screen */
    public static void showAccessDenied() {
        Parent view = new AccessDenied().getView();
        Scene scene = new Scene(view, 800, 600);
        scene.getStylesheets().add(SceneManager.class.getResource("/css/index.css").toExternalForm());
        primaryStage.setScene(scene);
    }
    public static MainLayoutView getMainLayoutView(){
        return mainLayoutView;
    }
}
