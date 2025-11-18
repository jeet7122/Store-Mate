package org.storemate.pos.user_interface.views;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EmployeeDataView {
    private final VBox root;
    public EmployeeDataView(){
        root = new VBox(10);
        Label title = new Label("Employee Data View");
        root.getChildren().add(title);
    }
    public Parent getView(){
        return root;
    }
}
