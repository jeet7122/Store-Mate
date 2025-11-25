package org.storemate.pos.user_interface.pages;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PurchaseOrderScreen {
    private final VBox root;
    public PurchaseOrderScreen(){
        root = new VBox();
        Label title = new Label("Place Purchase Orders");
        TextField searchByProductTF = new TextField();
        Label searchByProductLabel = new Label("Enter Product's Name: ");
        Button addToOrderBTN = new Button("Add To Order");


    }
    public Parent getView(){
        return root;
    }
}
