package org.storemate.pos.user_interface.pages;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class DeleteEmployeeView {
    private final VBox root;
    public DeleteEmployeeView(){
        root = new VBox();
    }
    public Parent getView(){
        return root;
    }

}
