package org.storemate.pos.user_interface.pages;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.storemate.pos.dao.CategoryDAO;
import org.storemate.pos.dao.ProductDAO;
import org.storemate.pos.models.Category;
import org.storemate.pos.models.Product;
import org.storemate.pos.models.enums.ProductStatus;
import org.storemate.pos.models.enums.TaxCategory;
import org.storemate.pos.routing.Router;

import java.math.BigDecimal;
import java.util.List;

public class AddProductsView {

    private final VBox root;
    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;

    public AddProductsView() {
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
        root = new VBox(15);
        root.setPadding(new Insets(20));
        initializeView();
    }

    private void initializeView() {
        // Title
        Label title = new Label("Add New Product");
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

        // Form Fields
        TextField productIDTF = new TextField();
        productIDTF.setPromptText("Enter Product ID");
        productIDTF.setMinWidth(300);


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

        ComboBox<String> categoryBox = new ComboBox<>();
        List<Category> categories = categoryDAO.getAllCategories();
        List<String> categoriesSTR_LIST = categories.stream()
                .map(Category::getCategory_name)
                .toList();
        categoryBox.setItems(FXCollections.observableArrayList(categoriesSTR_LIST));
        categoryBox.setPromptText("Select Product's Category");
        categoryBox.setMinWidth(300);


        // Button to submit form
        Button addButton = new Button("Add Product");
        addButton.getStyleClass().add("button-connect");
        addButton.setOnAction(e -> handleAddProduct(productIDTF,nameField, descriptionField, priceField, quantityField, taxCategoryComboBox, availabilityComboBox, categoryBox,categories));

        // Layout
        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.getChildren().addAll(
                productIDTF,
                nameField, descriptionField, priceField, quantityField,
                taxCategoryComboBox, availabilityComboBox, categoryBox , addButton
        );

        root.getChildren().addAll(topBar, formLayout);
    }

    private void handleAddProduct(TextField productID ,TextField nameField, TextArea descriptionField, TextField priceField,
                                  TextField quantityField, ComboBox<TaxCategory> taxCategoryComboBox,
                                  ComboBox<ProductStatus> availabilityComboBox, ComboBox<String> categoryBox, List<Category> categories) {
        try {
            // Gather input values
            String productIDSTR = productID.getText();
            String productName = nameField.getText();
            String productDescription = descriptionField.getText();
            String categorySTR = categoryBox.getValue();
            BigDecimal productPrice = new BigDecimal(priceField.getText());
            int productQuantity = Integer.parseInt(quantityField.getText());
            TaxCategory taxCategory = taxCategoryComboBox.getValue();
            ProductStatus availability = availabilityComboBox.getValue();

            // Validate input
            if (productIDSTR.isEmpty() || productName.isEmpty() || productDescription.isEmpty() || productPrice.compareTo(BigDecimal.ZERO) <= 0 || productQuantity <= 0 || taxCategory == null || availability == null || categorySTR == null) {
                showAlert("Please fill all fields with valid data.");
                return;
            }

            Category selectedCat = categories.stream()
                    .filter(c -> c.getCategory_name().equalsIgnoreCase(categorySTR))
                    .findFirst().orElse(null);
            if (selectedCat == null){
                showAlert("No category found!");
                return;
            }

            // Create new product
            Product newProduct = new Product();
            newProduct.setProduct_id(productIDSTR);
            newProduct.setProduct_name(productName);
            newProduct.setProduct_description(productDescription);
            newProduct.setProduct_price(productPrice);
            newProduct.setProduct_quantity(productQuantity);
            newProduct.setTaxCategory(taxCategory);
            newProduct.setAvailability(availability);
            newProduct.setCategory_id(selectedCat.getCategory_id());

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
