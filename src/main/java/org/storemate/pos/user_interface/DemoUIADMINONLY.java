package org.storemate.pos.user_interface;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DemoUIADMINONLY {
    private final VBox root;
    public DemoUIADMINONLY(){
        root = new VBox(10);
        Label title = new Label("Everyone can access");
        Button everyOne = new Button("Open For Everyone");
        Button home = new Button("Home");

        root.getChildren().addAll(title, everyOne, home);
    }
    public Parent getView(){
        return root;
    }
}
