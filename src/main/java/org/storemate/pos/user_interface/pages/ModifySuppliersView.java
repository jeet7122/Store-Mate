package org.storemate.pos.user_interface.pages;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class ModifySuppliersView {
    private final VBox root;
    public ModifySuppliersView(){
        root = new VBox();
    }
    public Parent getView(){
        return root;
    }
}
