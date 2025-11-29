package org.storemate.pos.user_interface.pages.settings_pages;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ManageSecurityQuestionsView {
    private final VBox root;
    public ManageSecurityQuestionsView(){
        root = new VBox(10);
        Label title = new Label("Manage Security Questions");
        root.getChildren().add(title);
    }
    public Parent getView(){
        return root;
    }
}
