package org.storemate.pos.user_interface;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.storemate.pos.models.Employee;
import org.storemate.pos.models.enums.Permission;
import org.storemate.pos.services.AuthorizationService;
import org.storemate.pos.utils.SceneManager;
import org.storemate.pos.utils.Session;

public class RouterPageDemo {

    private final VBox root;

    public RouterPageDemo() {
        root = new VBox(10);

        Employee employee = Session.getInstance().getCurrentUser();
        if (employee == null) {
            root.getChildren().add(new Label("No user logged in!"));
            return;
        }

        AuthorizationService authService = new AuthorizationService(employee.getRole());

        // --- Title ---
        Label title = new Label("Home Page");
        title.getStyleClass().add("title");

        // --- Buttons ---
        Button everyoneBtn = new Button("Open For Everyone");
        everyoneBtn.setMaxWidth(Double.MAX_VALUE);
        everyoneBtn.setOnAction(e -> SceneManager.navigate("/dashboard"));

        // Admin-only example button
        Button adminBtn = new Button("Admin Only");
        adminBtn.setMaxWidth(Double.MAX_VALUE);

        // Disable button if user lacks permission
        if (!authService.hasPermission(Permission.MANAGE_EMPLOYEES)) { // Use proper enum
            adminBtn.setDisable(true);
        }

        adminBtn.setOnAction(e -> SceneManager.navigate("/employees"));

        // Add buttons to the root
        root.getChildren().addAll(title, everyoneBtn, adminBtn);
    }

    public static Parent createInitialContent() {
        return new RouterPageDemo().getView();
    }

    public Parent getView() {
        return root;
    }
}
