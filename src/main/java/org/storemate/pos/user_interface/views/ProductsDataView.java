package org.storemate.pos.user_interface.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.ProductDAO;
import org.storemate.pos.models.Product;
import org.storemate.pos.routing.Router;

import java.util.List;

public class ProductsDataView {

    private final VBox root;
    private TableView<Product> productTable;
    private ObservableList<Product> productData;
    private final ProductDAO productDAO;

    public ProductsDataView() {
        productDAO = new ProductDAO();
        root = new VBox(15);
        root.setPadding(new Insets(10,10,10,10));
        root.setAlignment(Pos.TOP_CENTER);
        initializeView();
        loadProducts();
    }

    private void initializeView() {
        Label title = new Label("Product Management Data View");
        title.getStyleClass().add("label-title");

        // Action buttons
        HBox actionButtons = createActionButtons();

        // Table setup
        productTable = new TableView<>();
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Product, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("product_id"));

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("product_name"));

        TableColumn<Product, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("product_description"));

        TableColumn<Product, Number> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("product_price"));

        TableColumn<Product, Number> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("product_quantity"));

        TableColumn<Product, String> statusCol = new TableColumn<>("Availability");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("availability"));

        productTable.getColumns().addAll(idCol, nameCol, descCol, priceCol, stockCol, statusCol);

        // Search section
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search products...");

        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("button-search");
        searchBtn.setOnAction(e -> searchProducts(searchField.getText()));

        Button refreshBtn = new Button("Refresh");
        refreshBtn.getStyleClass().add("button-reset");
        refreshBtn.setOnAction(e -> loadProducts());

        searchBox.getChildren().addAll(searchField, searchBtn, refreshBtn);

        root.getChildren().addAll(title, actionButtons, searchBox, productTable);
    }

    private HBox createActionButtons() {
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER_LEFT);

        // Add button
        Button addBtn = new Button("Add Product");
        addBtn.getStyleClass().add("button-connect");
        addBtn.setOnAction(e -> Router.navigate("/products/add"));

        // Modify button
        Button modifyBtn = new Button("Modify Product");
        modifyBtn.getStyleClass().add("button-update");
        modifyBtn.setOnAction(e -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Router.navigate("/products/modify");
            } else {
                showAlert("Select a product to modify!");
            }
        });

        // Delete button
        Button deleteBtn = new Button("Delete Product");
        deleteBtn.getStyleClass().add("button-delete");
        deleteBtn.setOnAction(e -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to delete product: " + selected.getProduct_name() + "?",
                        ButtonType.YES, ButtonType.NO);
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        boolean success = productDAO.deleteProduct(selected.getProduct_id());
                        if (success) {
                            productTable.getItems().remove(selected);
                            showAlert("Product deleted successfully!");
                        } else {
                            showAlert("Failed to delete the product!");
                        }
                    }
                });
            } else {
                showAlert("Select a product to delete!");
            }
        });

        actionButtons.getChildren().addAll(addBtn, modifyBtn, deleteBtn);
        return actionButtons;
    }

    private void loadProducts() {
        List<Product> products = productDAO.getAllProducts();
        productData = FXCollections.observableArrayList(products);
        productTable.setItems(productData);
    }

    private void searchProducts(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            loadProducts();
            return;
        }
        Product result = productDAO.searchForProduct(keyword);
        if (result != null) {
            productData = FXCollections.observableArrayList(result);
        } else {
            productData = FXCollections.observableArrayList();
        }
        productTable.setItems(productData);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Parent getView() {
        return root;
    }
}
