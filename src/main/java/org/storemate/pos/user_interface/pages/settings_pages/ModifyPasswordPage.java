package org.storemate.pos.user_interface.pages.settings_pages;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.services.AuthService;
import org.storemate.pos.utils.Alerts;
import org.storemate.pos.utils.Session;

public class ModifyPasswordPage {

    private final VBox root;
    private final AuthService authService;

    public ModifyPasswordPage() {
        Alerts alerts = new Alerts();
        authService = new AuthService();

        root = new VBox(18);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("vbox-spaced");

        Label title = new Label("Modify Password");
        title.getStyleClass().add("label-title");

        Label existingUsername = new Label(Session.getInstance().getCurrentUser().getUsername());
        existingUsername.getStyleClass().add("label-subtitle");

        TextField oldTF = new TextField();
        oldTF.getStyleClass().add("text-field");
        oldTF.setPromptText("Enter old password");

        TextField newTF = new TextField();
        newTF.getStyleClass().add("text-field");
        newTF.setPromptText("Enter new password");

        TextField confirmTF = new TextField();
        confirmTF.getStyleClass().add("text-field");
        confirmTF.setPromptText("Confirm new password");

        VBox form = new VBox(15, oldTF, newTF, confirmTF);
        form.getStyleClass().add("form-grid");

        Button updateBtn = new Button("Update");
        updateBtn.getStyleClass().addAll("button", "button-update");

        Button clearBtn = new Button("Clear");
        clearBtn.getStyleClass().addAll("button", "button-reset");

        clearBtn.setOnAction(e -> {
            oldTF.clear();
            newTF.clear();
            confirmTF.clear();
        });

        HBox buttons = new HBox(10, updateBtn, clearBtn);
        buttons.getStyleClass().add("hbox-spaced");

        updateBtn.setOnAction(e -> {
            if (!authService.credentialsExists(Session.getInstance().getCurrentUser().getUsername(), oldTF.getText())) {
                alerts.showErrorAlert("Error", "Incorrect old password");
                return;
            }

            if (!confirmTF.getText().equals(newTF.getText())) {
                alerts.showErrorAlert("Error", "Passwords do not match");
                return;
            }

            boolean updated = authService.updatePass(
                    Session.getInstance().getCurrentUser().getEmployeeID(),
                    confirmTF.getText()
            );

            if (updated) alerts.showSuccessAlert("Success", "Password updated");
            else alerts.showErrorAlert("Error", "Update failed");
        });

        root.getChildren().addAll(title, existingUsername, form, buttons);
    }

    public Parent getView() {
        return root;
    }
}
