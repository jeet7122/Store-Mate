package org.storemate.pos.user_interface.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.storemate.pos.dao.ProductDAO;
import org.storemate.pos.models.Product;
import org.storemate.pos.models.enums.ProductStatus;
import org.storemate.pos.models.enums.TaxCategory;
import org.storemate.pos.routing.Router;

import java.math.BigDecimal;

public class AddProductsView {

    private final VBox root;
    private final ProductDAO productDAO;

    public AddProductsView() {
        productDAO = new ProductDAO();
        root = new VBox(15);
        root.setPadding(new Insets(20));
        initializeView();
    }

    private void initializeView() {
        // Title
        Label title = new Label("Add New Product");
        title.getStyleClass().add("label-title");

        // Form Fields
        TextField nameField = new TextField();
        nameField.setPromptText("Enter product name");
        nameField.setMinWidth(300);

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Enter product description");
        descriptionField.setMinWidth(300);
        descriptionField.setMaxHeight(100);

        TextField priceField = new TextField();
        priceField.setPromptText("Enter product price");
        priceField.setMinWidth(300);

        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter product quantity");
        quantityField.setMinWidth(300);

        ComboBox<TaxCategory> taxCategoryComboBox = new ComboBox<>();
        taxCategoryComboBox.getItems().setAll(TaxCategory.values());
        taxCategoryComboBox.setPromptText("Select tax category");
        taxCategoryComboBox.setMinWidth(300);

        ComboBox<ProductStatus> availabilityComboBox = new ComboBox<>();
        availabilityComboBox.getItems().setAll(ProductStatus.values());
        availabilityComboBox.setPromptText("Select availability status");
        availabilityComboBox.setMinWidth(300);

        // Button to submit form
        Button addButton = new Button("Add Product");
        addButton.getStyleClass().add("button-connect");
        addButton.setOnAction(e -> handleAddProduct(nameField, descriptionField, priceField, quantityField, taxCategoryComboBox, availabilityComboBox));

        // Layout
        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.getChildren().addAll(
                nameField, descriptionField, priceField, quantityField,
                taxCategoryComboBox, availabilityComboBox, addButton
        );

        root.getChildren().addAll(title, formLayout);
    }

    private void handleAddProduct(TextField nameField, TextArea descriptionField, TextField priceField,
                                  TextField quantityField, ComboBox<TaxCategory> taxCategoryComboBox,
                                  ComboBox<ProductStatus> availabilityComboBox) {
        try {
            // Gather input values
            String productName = nameField.getText();
            String productDescription = descriptionField.getText();
            BigDecimal productPrice = new BigDecimal(priceField.getText());
            int productQuantity = Integer.parseInt(quantityField.getText());
            TaxCategory taxCategory = taxCategoryComboBox.getValue();
            ProductStatus availability = availabilityComboBox.getValue();

            // Validate input
            if (productName.isEmpty() || productDescription.isEmpty() || productPrice.compareTo(BigDecimal.ZERO) <= 0 || productQuantity <= 0 || taxCategory == null || availability == null) {
                showAlert("Please fill all fields with valid data.");
                return;
            }

            // Create new product
            Product newProduct = new Product();
            newProduct.setProduct_name(productName);
            newProduct.setProduct_description(productDescription);
            newProduct.setProduct_price(productPrice);
            newProduct.setProduct_quantity(productQuantity);
            newProduct.setTaxCategory(taxCategory);
            newProduct.setAvailability(availability);

            // Save product to database
            boolean isAdded = productDAO.addProduct(newProduct);

            if (isAdded) {
                showAlert("Product added successfully!");
                Router.navigate("/products");  // Navigate back to the product list view
            } else {
                showAlert("Failed to add product.");
            }
        } catch (NumberFormatException ex) {
            showAlert("Please provide valid numeric input for price and quantity.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Parent getView() {
        return root;
    }
}
