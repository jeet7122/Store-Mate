package org.storemate.pos.user_interface.views;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CategoriesDataView {
    private final VBox root;
    public CategoriesDataView(){
        root = new VBox(10);
        Label title = new Label("Categories View");
        root.getChildren().add(title);
    }
    public Parent getView(){
        return root;
    }
}
