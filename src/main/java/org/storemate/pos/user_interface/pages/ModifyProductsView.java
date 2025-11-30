package org.storemate.pos.user_interface.pages;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.storemate.pos.dao.ProductDAO;
import org.storemate.pos.models.Product;
import org.storemate.pos.models.enums.ProductStatus;
import org.storemate.pos.models.enums.TaxCategory;
import org.storemate.pos.routing.Router;
import org.storemate.pos.utils.Alerts;

import java.math.BigDecimal;
import java.util.List;

public class ModifyProductsView {

    private final VBox root;
    private final ProductDAO productDAO = new ProductDAO();
    private final Alerts alerts = new Alerts();

    private ComboBox<Product> productSelector;
    private TextField nameField, descriptionField, priceField, stockField;
    private ComboBox<TaxCategory> taxCategoryCombo;
    private ComboBox<ProductStatus> availabilityCombo;
    private Button modifyButton;

    public ModifyProductsView() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("root");

        initializeView();
        loadProducts();
    }

    private void initializeView() {
        Label title = new Label("Modify Product");
        title.getStyleClass().add("label-title");
        HBox topBar = new HBox(50);
        topBar.setPadding(new Insets(10, 0, 20, 0));
        topBar.setAlignment(Pos.CENTER_LEFT);

        FontIcon backIcon = new FontIcon(MaterialDesignA.ARROW_LEFT_BOLD_OUTLINE);
        backIcon.setIconSize(30);
        backIcon.setIconColor(Color.DARKCYAN);
        Label backButton = new Label("", backIcon);
        backButton.getStyleClass().add("back-button"); // your CSS class
        backButton.setOnMouseClicked(e -> {
            // Handle navigation (replace this with your page switching logic)
            System.out.println("Back clicked!");
            Router.navigate("/products");
        });
        topBar.getChildren().addAll(backButton, title);

        productSelector = new ComboBox<>();
        productSelector.setPromptText("Select Product");
        productSelector.setOnAction(e -> loadProductDetails());

        nameField = new TextField();
        nameField.setPromptText("Product Name");

        descriptionField = new TextField();
        descriptionField.setPromptText("Product Description");

        priceField = new TextField();
        priceField.setPromptText("Price");

        stockField = new TextField();
        stockField.setPromptText("Stock Quantity");

        taxCategoryCombo = new ComboBox<>();
        taxCategoryCombo.setItems(FXCollections.observableArrayList(TaxCategory.values()));
        taxCategoryCombo.setPromptText("Select Tax Category");

        availabilityCombo = new ComboBox<>();
        availabilityCombo.setItems(FXCollections.observableArrayList(ProductStatus.values()));
        availabilityCombo.setPromptText("Select Availability");

        modifyButton = new Button("Modify Product");
        modifyButton.getStyleClass().add("button-connect");
        modifyButton.setOnAction(e -> modifyProduct());

        root.getChildren().addAll(topBar, productSelector, nameField, descriptionField, priceField, stockField, taxCategoryCombo, availabilityCombo, modifyButton);
    }

    private void loadProducts() {
        List<Product> products = productDAO.getAllProducts();
        productSelector.setItems(FXCollections.observableArrayList(products));
    }

    private void loadProductDetails() {
        Product selectedProduct = productSelector.getValue();
        if (selectedProduct != null) {
            nameField.setText(selectedProduct.getProduct_name());
            descriptionField.setText(selectedProduct.getProduct_description());
            priceField.setText(selectedProduct.getProduct_price().toString());
            stockField.setText(String.valueOf(selectedProduct.getProduct_quantity()));
            taxCategoryCombo.setValue(selectedProduct.getTaxCategory());
            availabilityCombo.setValue(selectedProduct.getAvailability());
        }
    }

    private void modifyProduct() {
        Product selectedProduct = productSelector.getValue();
        if (selectedProduct == null) {
            alerts.showErrorAlert("Error", "Please select a product to modify.");
            return;
        }

        try {
            String name = nameField.getText();
            String description = descriptionField.getText();
            BigDecimal price = new BigDecimal(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            TaxCategory taxCategory = taxCategoryCombo.getValue();
            ProductStatus availability = availabilityCombo.getValue();

            if (name.isEmpty() || description.isEmpty() || priceField.getText().isEmpty() || stockField.getText().isEmpty()) {
                alerts.showErrorAlert("Error", "All fields must be filled.");
                return;
            }

            Product modifiedProduct = new Product(
                    selectedProduct.getProduct_id(),
                    name,
                    description,
                    selectedProduct.getCategory_id(),
                    taxCategory,
                    price,
                    stock,
                    availability
            );

            boolean success = productDAO.updateProduct(modifiedProduct);
            if (success) {
                alerts.showSuccessAlert("Product Modified", "Product '" + modifiedProduct.getProduct_name() + "' updated successfully.");
            } else {
                alerts.showErrorAlert("Error", "Failed to update product.");
            }

        } catch (NumberFormatException e) {
            alerts.showErrorAlert("Error", "Invalid input. Please check your fields.");
        }
    }

    public Parent getView() {
        return root;
    }
}