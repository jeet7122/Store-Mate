package org.storemate.pos.user_interface.pages;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class ModifyCategoriesView {
    private final VBox root;
    public ModifyCategoriesView(){
        root = new VBox();
    }
    public Parent getView(){
        return root;
    }
}
