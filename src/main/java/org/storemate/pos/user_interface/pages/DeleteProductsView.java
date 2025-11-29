package org.storemate.pos.user_interface.pages;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.ProductDAO;
import org.storemate.pos.models.Product;
import org.storemate.pos.utils.Alerts;

import java.util.List;

public class DeleteProductsView {

    private final VBox root;
    private final ProductDAO productDAO = new ProductDAO();
    private final Alerts alerts = new Alerts();

    private ComboBox<Product> productSelector;
    private Button deleteButton;

    public DeleteProductsView() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("root");

        initializeView();
        loadProducts();
    }

    private void initializeView() {
        Label title = new Label("Delete Product");
        title.getStyleClass().add("label-title");

        productSelector = new ComboBox<>();
        productSelector.setPromptText("Select Product");
        productSelector.setOnAction(e -> updateInfoLabel());

        deleteButton = new Button("Delete Product");
        deleteButton.getStyleClass().add("button-connect");
        deleteButton.setOnAction(e -> deleteProduct());

        root.getChildren().addAll(title, productSelector, deleteButton);
    }

    private void loadProducts() {
        List<Product> products = productDAO.getAllProducts();
        productSelector.setItems(FXCollections.observableArrayList(products));
    }

    private void updateInfoLabel() {
        Product selected = productSelector.getValue();
        if (selected != null) {
            deleteButton.setText("Delete Product: " + selected.getProduct_name());
        } else {
            deleteButton.setText("Delete Product");
        }
    }

    private void deleteProduct() {
        Product selected = productSelector.getValue();
        if (selected == null) {
            alerts.showErrorAlert("Error", "Please select a product to delete.");
            return;
        }

        boolean success = productDAO.deleteProduct(selected.getProduct_id());
        if (success) {
            alerts.showSuccessAlert("Product Deleted", "Product '" + selected.getProduct_name() + "' deleted successfully.");
            loadProducts();
            productSelector.setValue(null);
            deleteButton.setText("Delete Product");
        } else {
            alerts.showErrorAlert("Error", "Failed to delete product.");
        }
    }

    public Parent getView() {
        return root;
    }
}
