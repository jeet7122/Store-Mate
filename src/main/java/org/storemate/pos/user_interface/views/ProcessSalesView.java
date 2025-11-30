package org.storemate.pos.user_interface.views;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.storemate.pos.dao.ProductDAO;
import org.storemate.pos.dao.ReturnDAO;
import org.storemate.pos.dto.CartDTO;
import org.storemate.pos.dao.SaleDAO;
import org.storemate.pos.models.Product;
import org.storemate.pos.models.enums.ReturnReason;
import org.storemate.pos.user_interface.components.CartTable;
import org.storemate.pos.utils.Session;

import java.util.List;

public class ProcessSalesView {
    private final VBox root;
    private final CartDTO cartDTO;
    private final CartTable cartTable;
    private final ComboBox<ReturnReason> reasonCombo;

    public ProcessSalesView() {
        // --- Initialize Cart ---
        cartDTO = new CartDTO(Session.getInstance().getCurrentUser().getEmployeeID());

        Label subtotalLbl = new Label("Subtotal: $0.00");
        subtotalLbl.getStyleClass().add("label-total");

        Label taxLbl = new Label("Total Tax: $0.00");
        taxLbl.getStyleClass().add("label-total");

        Label totalLbl = new Label("Total Price: $0.00");
        totalLbl.getStyleClass().add("label-total");

        cartTable = new CartTable(cartDTO, subtotalLbl, taxLbl, totalLbl);
        cartTable.getCartTable().getStyleClass().add("table-view");

        ProductDAO pdao = new ProductDAO();
        List<Product> products = pdao.getAllProducts(); // fetch all products

        root = new VBox(15);
        root.setPadding(new Insets(10));

        // --- Title ---
        Label title = new Label("Process Sales View");
        title.getStyleClass().add("label-title");

        // --- Search Area ---
        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(10,0,10,0));

        Label searchLabel = new Label("Search For Products: ");
        searchLabel.getStyleClass().add("label-subtitle");

        TextField searchTF = new TextField();
        searchTF.setPromptText("Enter Product Name");
        searchTF.getStyleClass().add("text-field");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");
        quantityField.getStyleClass().add("text-field");

        Button searchBTN = new Button("Search");
        searchBTN.getStyleClass().addAll("button", "button-connect");
        searchBTN.setOnAction(e -> {
            Product product = pdao.searchForProduct(searchTF.getText());
            if (product != null) {
                new Alert(Alert.AlertType.INFORMATION, "Product found: " + product.getProduct_name()).show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Product not found").show();
            }
        });

        Button addToCartBTN = new Button("Add To List");
        addToCartBTN.getStyleClass().addAll("button", "button-connect");
        addToCartBTN.setOnAction(e -> {
            Product product = pdao.searchForProduct(searchTF.getText());
            if (product != null) {
                int quantity = 1;
                try { quantity = Integer.parseInt(quantityField.getText()); } catch (Exception ignored) {}
                cartDTO.addItem(product, quantity);
                cartTable.refreshTable();
            }
        });

        searchBox.getChildren().addAll(searchLabel, searchTF, quantityField, searchBTN, addToCartBTN);
        Label reasonLabel = new Label("Return Reason:");
        reasonCombo = new ComboBox<>();
        reasonCombo.getItems().addAll(ReturnReason.values());
        reasonCombo.getSelectionModel().selectFirst();

        // --- Left Side: Cart + Totals + Void/Checkout Buttons ---
        VBox leftSide = new VBox(15);
        leftSide.setPrefWidth(500);
        leftSide.getChildren().addAll(cartTable.getCartTable(), subtotalLbl, taxLbl, totalLbl, reasonLabel, reasonCombo);

        Button voidBtn = new Button("VOID");
        voidBtn.getStyleClass().addAll("button", "button-reset");
        voidBtn.setOnAction(e -> {
            cartDTO.clearCart();
            cartTable.refreshTable();
        });

        //----------CHECKOUT BTN-------------//
        Button checkoutBtn = new Button("CHECKOUT");
        checkoutBtn.getStyleClass().addAll("button", "button-connect");
        checkoutBtn.setOnAction(e -> checkout());

        //----------Return-BTN--------------//
        // ---------- Return Mode Toggle ----------
        ToggleButton returnToggle = new ToggleButton("RETURN");
        returnToggle.getStyleClass().addAll("button", "button-warning");

        returnToggle.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            cartDTO.setReturnMode(isSelected);
            if (isSelected) {
                new Alert(Alert.AlertType.INFORMATION, "Return Mode Activated! Add items to cart for return.").show();
                returnToggle.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #ff6b6b, #d63031); -fx-text-fill: white;");
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Sales Mode Activated!").show();
                returnToggle.setStyle("-fx-background-color: #b0bec5; -fx-text-fill: #2d3436;");
            }
        });


        HBox btnBox = new HBox(10, voidBtn, checkoutBtn, returnToggle);
        btnBox.setPadding(new Insets(10,0,0,0));
        leftSide.getChildren().add(btnBox);

        // --- Right Side: Product Grid ---
        GridPane productGrid = new GridPane();
        productGrid.setHgap(10);
        productGrid.setVgap(10);

        int columns = 5;
        int row = 0, col = 0;

        for (Product product : products) {
            Button prodBtn = new Button(product.getProduct_name());
            prodBtn.getStyleClass().add("product-button");
            prodBtn.setPrefSize(120, 100);
            prodBtn.setOnAction(e -> {
                boolean added = cartDTO.addItem(product, 1);
                if (!added) new Alert(Alert.AlertType.WARNING, "Cannot mix sales and returns");
                else cartTable.refreshTable();
            });

            productGrid.add(prodBtn, col, row);
            col++;
            if (col >= columns) {
                col = 0;
                row++;
            }
        }

        ScrollPane productScroll = new ScrollPane(productGrid);
        productScroll.setFitToWidth(true);
        productScroll.setPrefViewportWidth(650);
        productScroll.setPrefViewportHeight(600);

        VBox rightSide = new VBox(productScroll);
        rightSide.setPrefWidth(650);

        // --- Main Content ---
        HBox mainContent = new HBox(20, leftSide, rightSide);
        mainContent.setPadding(new Insets(10));


        root.setPadding(new Insets(10,10,10,10));
        root.getChildren().addAll(title, searchBox, mainContent);
    }

    private void checkout() {
        if (cartDTO.getCartItems().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Cart is empty").show();
            return;
        }

        Alert paymentAlert = new Alert(Alert.AlertType.CONFIRMATION);
        paymentAlert.setTitle("Select Payment Method");
        paymentAlert.setHeaderText("Choose Payment Method");

        ButtonType cashBtn = new ButtonType("Cash");
        ButtonType debitBtn = new ButtonType("Debit");
        ButtonType creditBtn = new ButtonType("Credit");
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        paymentAlert.getButtonTypes().setAll(cashBtn, debitBtn, creditBtn, cancelBtn);

        paymentAlert.showAndWait().ifPresent(type -> {
            String method = null;
            if (type == cashBtn) method = "CASH";
            else if (type == debitBtn) method = "DEBIT";
            else if (type == creditBtn) method = "CREDIT";

            if (method != null){
                if (cartDTO.isReturnMode()){
                    ReturnReason reason = reasonCombo.getSelectionModel().getSelectedItem();
                    finalizeReturn(method, reason);
                }
                else finalizeSale(method);
            }
        });
    }

    private void finalizeReturn(String method, ReturnReason returnReason) {
        ReturnDAO returnDAO = new ReturnDAO();
        boolean success = returnDAO.processReturn(cartDTO, Session.getInstance().getCurrentUser().getEmployeeID(), method, returnReason.name());
        if (success) {
            new Alert(Alert.AlertType.INFORMATION, "Return Completed!").show();
            cartDTO.clearCart();
            cartTable.refreshTable();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to process return").show();
        }
    }

    private void finalizeSale(String paymentType) {
        SaleDAO saleDAO = new SaleDAO();
        boolean success = saleDAO.saveSale(cartDTO, paymentType);
        if (success) {
            new Alert(Alert.AlertType.INFORMATION, "Sale Completed!").show();
            cartDTO.clearCart();
            cartTable.refreshTable();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to process sale").show();
        }
    }

    public Parent getView() {
        return root;
    }
}
