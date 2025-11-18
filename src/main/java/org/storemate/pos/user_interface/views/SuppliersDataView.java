package org.storemate.pos.user_interface.views;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SuppliersDataView {
    private final VBox root;
    public SuppliersDataView(){
        root = new VBox(10);
        Label title = new Label("Supplier Data View");
        root.getChildren().add(title);
    }
    public Parent getView(){
        return root;
    }
}
