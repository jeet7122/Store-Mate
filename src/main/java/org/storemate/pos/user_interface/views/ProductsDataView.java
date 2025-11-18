package org.storemate.pos.user_interface.views;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ProductsDataView {
    private final VBox root;
    public ProductsDataView(){
        root = new VBox(10);
        Label title = new Label("Products Data View");
        root.getChildren().add(title);
    }
    public Parent getView(){
        return root;
    }
}
