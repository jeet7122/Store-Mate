package org.storemate.pos.user_interface.pages.settings_pages;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ForgotPasswordView {
    private final VBox root;
    public ForgotPasswordView(){
        root = new VBox(10);
        Label title = new Label("Forgot password");
        root.getChildren().add(title);
    }
    public Parent getView(){
        return root;
    }
}
