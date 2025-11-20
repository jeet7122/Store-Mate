package org.storemate.pos.user_interface.views;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.ProductDAO;
import org.storemate.pos.dto.CartDTO;
import org.storemate.pos.dto.SaleDAO;
import org.storemate.pos.models.Product;
import org.storemate.pos.user_interface.components.CartTable;
import org.storemate.pos.utils.Session;


public class ProcessSalesView {
    private final VBox root;
    private final CartDTO cartDTO;
    private final CartTable cartTable;

    public ProcessSalesView() {
        cartDTO = new CartDTO(Session.getInstance().getCurrentUser().getEmployeeID());
        Label subtotalLbl = new Label("Subtotal: $0.00");
        Label taxLbl = new Label("Total Tax: $0.00");
        Label totalLbl = new Label("Total Price: $0.00");
        cartTable = new CartTable(cartDTO, subtotalLbl, taxLbl, totalLbl);
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
        Label quantityLabel = new Label("Enter Quantity");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter Quantity");


        addToCartBTN.setOnAction(e -> {
            Product product = pdao.searchForProduct(searchTF.getText());
            if (product != null) {
                int quan = Integer.parseInt(quantityField.getText());
                cartDTO.addItem(product, quan);
                cartTable.refreshTable();
            }
        });

        searchBox.getChildren().addAll(searchLabel, searchTF, quantityLabel, quantityField, searchBTN, addToCartBTN);
        VBox cartContainer = new VBox(10);
        cartContainer.getChildren().addAll(
                cartTable.getCartTable(),
                subtotalLbl,
                taxLbl,
                totalLbl
        );

        Button voidBtn = new Button("VOID");
        voidBtn.setOnAction(e -> {
            cartDTO.clearCart();
            cartTable.refreshTable();
        });
        Button checkoutBtn = new Button("CHECKOUT");

        // styling optional
        voidBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 16;");
        checkoutBtn.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white; -fx-font-size: 16;");

        checkoutBtn.setOnAction(e -> {
            if (cartDTO.getCartItems().isEmpty()){
                new Alert(Alert.AlertType.WARNING).show();
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
                if (type == cashBtn) finalizeSale("CASH");
                else if (type == debitBtn) finalizeSale("DEBIT");
                else if (type == creditBtn) finalizeSale("CREDIT");
            });
        });

        root.getChildren().addAll(title, searchBox, cartContainer, voidBtn, checkoutBtn);
    }

    private void finalizeSale(String paymentType) {
        SaleDAO saleDAO = new SaleDAO();
        boolean success = saleDAO.saveSale(cartDTO, paymentType);
        if (success){
            new Alert(Alert.AlertType.INFORMATION, "Sale Completed!").show();
            cartDTO.clearCart();
            cartTable.refreshTable();
        }
        else {
            new Alert(Alert.AlertType.ERROR, "Failed to process sale").show();
        }
    }

    public Parent getView() {
        return root;
    }
}
