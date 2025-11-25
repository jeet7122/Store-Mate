package org.storemate.pos.user_interface.pages;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class DeleteSuppliersView {
    private final VBox root;
    public DeleteSuppliersView(){
        root = new VBox();
    }
    public Parent getView(){
        return root;
    }
}
