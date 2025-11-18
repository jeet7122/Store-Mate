package org.storemate.pos.user_interface.views;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardViewForAdmin {
    private final VBox root;
    public DashboardViewForAdmin(){
        root = new VBox(10);
        Label title = new Label("Admin Dashboard");
        root.getChildren().add(title);
    }

    public Parent getView() {
        return root;
    }
}
