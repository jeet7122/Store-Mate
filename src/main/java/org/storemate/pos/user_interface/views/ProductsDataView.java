package org.storemate.pos.user_interface.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.models.Product;
import org.storemate.pos.routing.Router;

public class ProductsDataView {

    private final VBox root;

    public ProductsDataView() {

        // Root container
        root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        // Title
        Label title = new Label("Products Management");
        title.getStyleClass().add("label-title");
        root.getChildren().add(title);

        // Action Button
        HBox actionBox = new HBox(12);
        actionBox.setAlignment(Pos.CENTER_LEFT);

        Button addProductBtn = new Button("Add Product");
        addProductBtn.getStyleClass().addAll("button", "button-connect");
        addProductBtn.setOnAction(e -> Router.navigate("/products/add"));

        Button deleteBtn = new Button("Delete");
        deleteBtn.getStyleClass().addAll("button", "button-reset");

        Button refreshBtn = new Button("Refresh");
        refreshBtn.getStyleClass().addAll("button", "button-reset");

        actionBox.getChildren().addAll(addProductBtn, deleteBtn, refreshBtn);
        root.getChildren().add(actionBox);

        // Table Card
        VBox tableCard = new VBox(10);
        tableCard.setPadding(new Insets(15));
        tableCard.getStyleClass().add("products-card");
        tableCard.setPrefWidth(Double.MAX_VALUE);

        // Product Table
        TableView<Product> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, Number> idCol = new TableColumn<>("Product-Id");
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        TableColumn<Product, Number> priceCol = new TableColumn<>("Price");
        TableColumn<Product, Number> stockCol = new TableColumn<>("Stock");

        table.getColumns().addAll(idCol, nameCol, priceCol, stockCol);

        tableCard.getChildren().add(table);
        root.getChildren().add(tableCard);

        // DELETE
        deleteBtn.setOnAction(e -> table.getItems().remove(table.getSelectionModel().getSelectedItem()));

        //  FILTER CARD
        VBox filterCard = new VBox(10);
        filterCard.setPadding(new Insets(15));
        filterCard.getStyleClass().add("products-card");
        filterCard.setPrefWidth(Double.MAX_VALUE);

        Label filterLabel = new Label("Filter Products");
        filterLabel.getStyleClass().add("label-subtitle");

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name...");
        searchField.getStyleClass().add("text-field");

        Button applyFilterBtn = new Button("Apply");
        applyFilterBtn.getStyleClass().addAll("button", "button-connect");

        filterBox.getChildren().addAll(searchField, applyFilterBtn);

        filterCard.getChildren().addAll(filterLabel, filterBox);
        root.getChildren().add(filterCard);

        // RESPONSIVE WIDTH
        root.widthProperty().addListener((o, oldV, newV) -> {
            tableCard.setPrefWidth(newV.doubleValue() - 40);
            filterCard.setPrefWidth(newV.doubleValue() - 40);
        });
    }

    public Parent getView() {
        return root;
    }
}
