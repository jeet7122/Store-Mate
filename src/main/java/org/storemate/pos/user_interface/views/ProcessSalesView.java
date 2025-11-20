package org.storemate.pos.user_interface.views;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.ProductDAO;
import org.storemate.pos.models.Product;

public class ProcessSalesView {
    private final VBox root;
    public ProcessSalesView(){
        ProductDAO pdao = new ProductDAO();
        root = new VBox(10);
        Label title = new Label("Process Sales View");
        HBox searchBox = new HBox(10);
        Label searchLabel = new Label("Search For Products: ");
        TextField searchTF = new TextField("Enter Product Name");
        Button searchBTN = new Button("Search");
        searchBTN.setOnAction(e -> {
            Product product = pdao.searchForProduct(searchTF.getText());
            System.out.println(product.toString());
        });
        Button addToCartBTN = new Button("Add To List");
        searchBox.getChildren().addAll(searchLabel, searchTF, searchBTN, addToCartBTN);

        VBox cartContainer = new VBox(20);
        GridPane cartGrid = new GridPane(10,10);


        root.getChildren().addAll(title, searchBox);
    }
    public Parent getView(){
        return root;
    }
}
