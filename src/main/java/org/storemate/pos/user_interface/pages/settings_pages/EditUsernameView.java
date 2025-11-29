package org.storemate.pos.user_interface.pages.settings_pages;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EditUsernameView {
    private final VBox root;
    public EditUsernameView(){
        root = new VBox(10);
        Label title = new Label("Edit username");
        root.getChildren().add(title);
    }
    public Parent getView(){
        return root;
    }
}
