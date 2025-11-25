package org.storemate.pos.user_interface.pages;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class DeleteProductsView {
    private final VBox root;
    public DeleteProductsView(){
        root = new VBox();
    }
    public Parent getView(){
        return root;
    }
}
