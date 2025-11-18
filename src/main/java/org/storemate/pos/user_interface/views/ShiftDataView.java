package org.storemate.pos.user_interface.views;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ShiftDataView {
    private final VBox root;
    public ShiftDataView(){
        root = new VBox(10);
        Label title = new Label("Shift Data View");
        root.getChildren().add(title);
    }
    public Parent getView(){
        return root;
    }
}
