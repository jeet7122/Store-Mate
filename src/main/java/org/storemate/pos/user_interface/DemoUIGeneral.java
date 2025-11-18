package org.storemate.pos.user_interface;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DemoUIGeneral {
    private final VBox root;
    public DemoUIGeneral(){
        root = new VBox(10);
        Label title = new Label("Everyone can access");
        Button adminOnly = new Button("Admin Only");
        Button home = new Button("Home");

        root.getChildren().addAll(title, adminOnly, home);
    }
    public Parent getView(){
        return root;
    }
}
