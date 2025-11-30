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
    public ModifyPasswordPage(){
        Alerts alerts = new Alerts();
        authService = new AuthService();
        root = new VBox(10);
        Label title = new Label("Modify Password");
        Label existingUsername = new Label(Session.getInstance().getCurrentUser().getUsername());
        VBox modifyPassBox = new VBox(10);
        TextField existingPassTF = new TextField();
        existingPassTF.setPromptText("enter existing password");
        TextField newPassTF = new TextField();
        newPassTF.setPromptText("enter new password");
        TextField confirmNewPassTF = new TextField();
        confirmNewPassTF.setPromptText("re-enter new password");
        HBox buttons = new HBox(5);
        Button updateBTN = new Button("Update");
        updateBTN.setOnAction(e -> {
            if (!authService.credentialsExists(Session.getInstance().getCurrentUser().getUsername(), existingPassTF.getText())){
                alerts.showErrorAlert("Error", "Incorrect old pass");
                return;
            }
            if (!confirmNewPassTF.getText().equals(newPassTF.getText())){
                alerts.showErrorAlert("Error", "Passwords should match in both fields ");
                return;
            }
            boolean updated = authService.updatePass(Session.getInstance().getCurrentUser().getEmployeeID(), confirmNewPassTF.getText());

            if (updated){
                alerts.showSuccessAlert(
                        "Success",
                        "Password Modified successfully"
                );
            }
            else {
                alerts.showErrorAlert(
                        "Error",
                        "Error updating password"
                );
            }

        });

        Button clearBTN = new Button("Clear");
        buttons.getChildren().addAll(updateBTN, clearBTN);
        Label boxTitle = new Label("Enter Credentials");
        modifyPassBox.getChildren()
                        .addAll(boxTitle,existingPassTF, newPassTF, confirmNewPassTF);
        root.getChildren().addAll(title,existingUsername, modifyPassBox, buttons);
        root.setPadding(new Insets(10,10,10,10));
    }
    public Parent getView(){
        return root;
    }
}
