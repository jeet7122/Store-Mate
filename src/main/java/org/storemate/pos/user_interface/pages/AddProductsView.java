package org.storemate.pos.user_interface.pages;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class AddProductsView {
    private final VBox root;
    public AddProductsView(){
        root = new VBox();
    }
    public Parent getView(){
        return root;
    }
}
