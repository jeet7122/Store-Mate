package org.storemate.pos.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.storemate.pos.routing.AccessDenied;
import org.storemate.pos.routing.Router;
import org.storemate.pos.user_interface.DatabaseConnectionFormView;
import org.storemate.pos.user_interface.views.MainLayoutView;
import org.storemate.pos.user_interface.EmployeeLoginFormView;


/**
 * SceneManager is responsible for managing and switching JavaFX scenes in the application.
 * <p>
 * It provides utility methods to show login forms, database connection forms, the main application layout,
 * and access-denied pages. It also integrates with {@link Router} for route navigation.
 * @author Khushi, Jeet
 */
public class SceneManager {

    private static Stage primaryStage;
    private static MainLayoutView mainLayoutView;

    /**
     * Initializes the SceneManager with the primary Stage.
     * Must be called once at application startup.
     *
     * @param stage the primary JavaFX Stage
     */
    public static void init(Stage stage) {
        primaryStage = stage;
        Router.init(stage);
    }



    /**
     * Shows the employee login form.
     * Used when the application starts or the user logs out.
     */
    public static void showEmployeeLoginForm() {
        EmployeeLoginFormView view = new EmployeeLoginFormView();
        Scene scene = new Scene(view.getView(), 1024, 768);
        scene.getStylesheets().add(SceneManager.class.getResource("/css/index.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Shows the database connection form.
     * Useful when initial configuration or reconnection is required.
     */
    public static void showDatabaseConnectionForm(){
        DatabaseConnectionFormView view = new DatabaseConnectionFormView();
        Scene scene = new Scene(view.getView(), 1024,768);
        scene.getStylesheets().add(SceneManager.class.getResource("/css/index.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * Shows a generic form provided as a Parent node.
     *
     * @param formView the JavaFX Parent node representing the form
     * @param width    the width of the scene
     * @param height   the height of the scene
     */
    public static void showForm(javafx.scene.Parent formView, int width, int height) {
        Scene scene = new Scene(formView, width, height);
        scene.getStylesheets().add(SceneManager.class.getResource("/css/index.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    /**
     * Shows the main application layout after a successful login.
     * Ensures a user is logged in; otherwise redirects to the login form.
     */
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


    /**
     * Convenience method to navigate to a specific route using {@link Router}.
     *
     * @param route the route path (e.g., "/dashboard")
     */
    public static void navigate(String route) {
        Router.navigate(route);
    }

    /**
     * Shows Access Denied page.
     * Typically used when the current user does not have sufficient privileges.
     */
    public static void showAccessDenied() {
        Parent view = new AccessDenied().getView();
        Scene scene = new Scene(view, 800, 600);
        scene.getStylesheets().add(SceneManager.class.getResource("/css/index.css").toExternalForm());
        primaryStage.setScene(scene);
    }
    /**
     * Returns the main application layout view.
     *
     * @return the {@link MainLayoutView} instance
     */
    public static MainLayoutView getMainLayoutView(){
        return mainLayoutView;
    }
}
