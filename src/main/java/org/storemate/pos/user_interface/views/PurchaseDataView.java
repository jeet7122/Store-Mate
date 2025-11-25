package org.storemate.pos.user_interface.views;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.storemate.pos.routing.Router;

public class PurchaseDataView {
    private final VBox root;
    public PurchaseDataView(){
        root = new VBox(10);
        Label title = new Label("Purchases");
        root.getChildren().add(title);
        Button navigate = new Button("Add Purchase Order");
        navigate.setOnAction(e -> Router.navigate("/purchases/order"));
        root.getChildren().add(navigate);
    }
    public Parent getView(){
        return root;
    }
}
