package org.storemate.pos.user_interface.pages.settings_pages;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.storemate.pos.services.AuthService;
import org.storemate.pos.utils.Alerts;
import org.storemate.pos.utils.Session;

public class EditUsernameView {
    private final VBox root;

    public EditUsernameView() {
        Alerts alerts = new Alerts();
        AuthService authService = new AuthService();

        root = new VBox(15);
        root.getStyleClass().add("vbox-spaced");

        Label title = new Label("Edit Username");
        title.getStyleClass().add("label-title");

        Label existingUsername = new Label("Existing Username");
        Label existingVal = new Label(Session.getInstance().getCurrentUser().getUsername());

        Label newUserNameLabel = new Label("Enter New Username");

        TextField usernameTF = new TextField();
        usernameTF.setPromptText("Enter new username");
        usernameTF.getStyleClass().add("text-field");

        Button updateBTN = new Button("Update");
        updateBTN.getStyleClass().addAll("button", "button-update");

        updateBTN.setOnAction(e -> {
            boolean updated = authService.updateUsername(
                    usernameTF.getText(),
                    Session.getInstance().getCurrentUser().getEmployeeID()
            );
            if (updated) alerts.showSuccessAlert("Success", "Username updated successfully");
            else alerts.showErrorAlert("Error", "Error updating username");
        });

        GridPane grid = new GridPane();
        grid.getStyleClass().add("form-grid");
        grid.setHgap(10);
        grid.setVgap(15);

        grid.add(existingUsername, 0, 0);
        grid.add(existingVal, 1, 0);
        grid.add(newUserNameLabel, 0, 1);
        grid.add(usernameTF, 1, 1);
        grid.add(updateBTN, 0, 2, 2, 1);

        root.getChildren().addAll(title, grid);
    }

    public Parent getView() {
        return root;
    }
}
