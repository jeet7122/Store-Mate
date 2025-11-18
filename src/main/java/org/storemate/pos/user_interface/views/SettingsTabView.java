package org.storemate.pos.user_interface.views;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SettingsTabView {
    private final VBox root;
    public SettingsTabView(){
        root = new VBox(10);
        Label title = new Label("Settings Panel");
        root.getChildren().add(title);
    }
    public Parent getView(){
        return root;
    }
}
