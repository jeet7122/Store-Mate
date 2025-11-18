package org.storemate.pos.user_interface.views;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SalesDataView {
    private final VBox root;
    public SalesDataView(){
        root = new VBox();
        Label title = new Label("Sales Data View");
        root.getChildren().add(title);
    }

    public Parent getView() {
        return root;
    }
}
