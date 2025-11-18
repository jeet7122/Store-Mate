package org.storemate.pos.user_interface.views;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PurchaseDataView {
    private final VBox root;
    public PurchaseDataView(){
        root = new VBox(10);
        Label title = new Label("Purchases");
        root.getChildren().add(title);
    }
    public Parent getView(){
        return root;
    }
}
