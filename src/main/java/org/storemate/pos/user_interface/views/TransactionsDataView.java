package org.storemate.pos.user_interface.views;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TransactionsDataView {
    private final VBox root;
    public TransactionsDataView(){
        root = new VBox(10);
        Label title = new Label("Transactions Data");
        root.getChildren().add(title);
    }
    public Parent getView(){
        return root;
    }
}
