package org.storemate.pos.user_interface.pages.settings_pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.storemate.pos.routing.Router;
import org.storemate.pos.services.AuthService;
import org.storemate.pos.utils.Alerts;
import org.storemate.pos.utils.Session;

/**
 * UI view for modifying the current user's password.
 * <p>
 * Provides functionality to:
 * <ul>
 *     <li>Enter the old password for verification</li>
 *     <li>Enter a new password and confirm it</li>
 *     <li>Clear all fields</li>
 *     <li>Update the password if validation passes</li>
 * </ul>
 * <p>
 * Uses {@link AuthService} to verify and update credentials and {@link Alerts} to show user feedback.
 * Navigation back to the general settings page is provided via {@link Router}.
 * @author Khushi, Jeet
 */
public class ModifyPasswordPage {

    private final VBox root;
    private final AuthService authService;

    /**
     * Constructs the ModifyPasswordPage.
     * <p>
     * Sets up:
     * <ul>
     *     <li>Top bar with back button</li>
     *     <li>Existing username display</li>
     *     <li>Input fields for old password, new password, and confirm password</li>
     *     <li>Update and Clear buttons with their handlers</li>
     * </ul>
     * <p>
     * Update button logic:
     * <ol>
     *     <li>Checks if old password is correct using {@link AuthService#credentialsExists(String, String)}</li>
     *     <li>Checks if new password and confirm password match</li>
     *     <li>Updates the password via {@link AuthService#updatePass(int, String)}</li>
     *     <li>Shows success or error alerts</li>
     * </ol>
     */
    public ModifyPasswordPage() {
        Alerts alerts = new Alerts();
        authService = new AuthService();

        root = new VBox(18);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("vbox-spaced");

        Label title = new Label("Modify Password");
        title.getStyleClass().add("label-title");
        HBox topBar = new HBox(50);
        topBar.setPadding(new Insets(10, 0, 20, 0));
        topBar.setAlignment(Pos.CENTER_LEFT);

        FontIcon backIcon = new FontIcon(MaterialDesignA.ARROW_LEFT_BOLD_OUTLINE);
        backIcon.setIconSize(30);
        backIcon.setIconColor(Color.DARKCYAN);
        Label backButton = new Label("", backIcon);
        backButton.getStyleClass().add("back-button"); // your CSS class
        backButton.setOnMouseClicked(e -> {
            // Handle navigation (replace this with your page switching logic)
            System.out.println("Back clicked!");
            Router.navigate("/settings/general");
        });
        topBar.getChildren().addAll(backButton, title);

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

        root.getChildren().addAll(topBar, existingUsername, form, buttons);
    }

    /**
     * Returns the root container of this view.
     *
     * @return {@link Parent} representing the Modify Password page
     */
    public Parent getView() {
        return root;
    }
}
