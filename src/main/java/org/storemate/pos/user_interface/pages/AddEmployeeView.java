package org.storemate.pos.user_interface.pages;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class AddEmployeeView {
    private final VBox root;
    public AddEmployeeView(){
        root = new VBox();
    }
    public Parent getView(){
        return root;
    }
}
