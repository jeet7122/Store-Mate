package org.storemate.pos.user_interface.pages.settings_pages;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.storemate.pos.utils.Session;

public class ModifyPasswordPage {
    private final VBox root;
    public ModifyPasswordPage(){
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
        Label boxTitle = new Label("Enter Credentials");
        modifyPassBox.getChildren()
                        .addAll(boxTitle,existingPassTF, newPassTF, confirmNewPassTF);
        root.getChildren().addAll(title,existingUsername, modifyPassBox);
        root.setPadding(new Insets(10,10,10,10));
    }
    public Parent getView(){
        return root;
    }
}
