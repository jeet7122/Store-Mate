package org.storemate.pos.user_interface;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class RouterPageDemo {
    private final VBox root;
    public RouterPageDemo(){
        root= new VBox(10);
        Label title = new Label("Home Page");
        Button everyOne = new Button("Open For Everyone");
        Button adminOnly = new Button("Admin Only");

        root.getChildren().addAll(title, everyOne, adminOnly);
    }
    public Parent getView(){
        return root;
    }
}
