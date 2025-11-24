package org.storemate.pos.user_interface.pages;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class ModifyEmployeeView {
    private final VBox root;

    public ModifyEmployeeView(){
        root = new VBox();
    }

    public Parent getView() {
        return root;
    }
}
