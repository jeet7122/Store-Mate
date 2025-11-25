package org.storemate.pos.user_interface.pages;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.ProductDAO;
import org.storemate.pos.dao.ProductSupplierDAO;
import org.storemate.pos.dao.PurchaseDAO;
import org.storemate.pos.dao.SuppliersDAO;
import org.storemate.pos.dto.PurchaseDTO;
import org.storemate.pos.models.Product;
import org.storemate.pos.models.Supplier;
import org.storemate.pos.user_interface.components.PurchaseCartTable;

import java.math.BigDecimal;
import java.util.List;

public class PurchaseOrderScreen {
    private final VBox root;
    private final PurchaseDTO purchaseCart;
    private final PurchaseCartTable purchaseTable;
    private final Label subtotalLbl;
    private final Label taxLbl;
    private final Label totalLbl;

    public PurchaseOrderScreen() {
        purchaseCart = new PurchaseDTO();
        purchaseTable = new PurchaseCartTable(purchaseCart);

        root = new VBox(12);
        root.setPadding(new Insets(10));

        Label title = new Label("Purchase Orders");
        title.getStyleClass().add("label-title");

        // Suppliers
        SuppliersDAO supplierDAO = new SuppliersDAO();
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        ComboBox<Supplier> supplierCombo = new ComboBox<>();
        supplierCombo.getItems().addAll(suppliers);
        supplierCombo.setPromptText("Select Supplier");
        supplierCombo.setPrefWidth(300);
        supplierCombo.valueProperty().addListener((obs, old, sel) -> {
            if (sel != null) purchaseCart.setSupplierID(sel.getSupplier_id());
        });

        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("Pending", "Ordered", "Received");
        statusCombo.getSelectionModel().selectFirst();

        HBox supplierRow = new HBox(12, new Label("Supplier:"), supplierCombo, new Label("Status:"), statusCombo);
        supplierRow.setPadding(new Insets(6));

        // Search area
        TextField searchTF = new TextField();
        searchTF.setPromptText("Search product name or id");

        TextField qtyTF = new TextField();
        qtyTF.setPromptText("Qty");
        qtyTF.setPrefWidth(80);

        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> {

            Product product = new ProductDAO().searchForProduct(searchTF.getText());
            if (product == null) {
                new Alert(Alert.AlertType.WARNING, "Product not found").show();
                return;
            }
            // show minimal info
            new Alert(Alert.AlertType.INFORMATION, "Found: " + product.getProduct_name() + "\nPrice: " + product.getProduct_price()).show();
        });

        Button addBtn = new Button("Add To Purchase");
        addBtn.setOnAction(e -> {
            if (purchaseCart.getSupplierID() <= 0) {
                new Alert(Alert.AlertType.WARNING, "Please select a supplier first").show();
                return;
            }

            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.searchForProduct(searchTF.getText());

            if (product == null) {
                new Alert(Alert.AlertType.WARNING, "Product not found").show();
                return;
            }

            int q;
            try {
                q = Integer.parseInt(qtyTF.getText());
            } catch (Exception ignored) {
                q = 1;
            }

            final Product finalProduct = product;
            final int finalQ = q;

            ProductSupplierDAO psdao = new ProductSupplierDAO();
            BigDecimal cost = psdao.getCostPriceForSupplier(product.getProduct_id(), purchaseCart.getSupplierID());

            if (cost == null) {
                Alert fallback = new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "No supplier cost found for selected supplier. Use product sale price as cost?",
                        ButtonType.YES, ButtonType.NO
                );

                fallback.showAndWait().ifPresent(bt -> {
                    if (bt == ButtonType.YES) {
                        purchaseCart.addItem(finalProduct, finalQ, finalProduct.getProduct_price());
                        purchaseTable.refresh();
                        refreshTotals();
                    }
                });
            } else {
                purchaseCart.addItem(finalProduct, finalQ, cost);
                purchaseTable.refresh();
                refreshTotals();
            }
        });


        HBox searchRow = new HBox(8, new Label("Product:"), searchTF, qtyTF, addBtn, searchBtn);
        searchRow.setPadding(new Insets(6));

        // totals
        subtotalLbl = new Label("Subtotal: $0.00");
        taxLbl = new Label("Tax: $0.00");
        totalLbl = new Label("Total: $0.00");

        HBox totalsRow = new HBox(12, subtotalLbl, taxLbl, totalLbl);
        totalsRow.setPadding(new Insets(6));

        Button voidBtn = new Button("VOID");
        voidBtn.setOnAction(e -> {
            purchaseCart.clear();
            purchaseTable.refresh();
            refreshTotals();
        });

        Button submitBtn = new Button("SUBMIT ORDER");
        submitBtn.setOnAction(e -> {
            if (purchaseCart.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Purchase cart is empty").show();
                return;
            }
            if (purchaseCart.getSupplierID() <= 0) {
                new Alert(Alert.AlertType.WARNING, "Select supplier").show();
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Create purchase order for supplier and update inventory?",
                    ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) {
                    PurchaseDAO purchaseDAO = new PurchaseDAO();
                    int purchaseId = purchaseDAO.createPurchase(purchaseCart, statusCombo.getValue(), "");
                    if (purchaseId > 0) {
                        new Alert(Alert.AlertType.INFORMATION, "Purchase Order Created (ID: " + purchaseId + ")").show();
                        purchaseCart.clear();
                        purchaseTable.refresh();
                        refreshTotals();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to create purchase order").show();
                    }
                }
            });
        });

        HBox btnRow = new HBox(8, voidBtn, submitBtn);
        btnRow.setPadding(new Insets(6));

        root.getChildren().addAll(title, supplierRow, searchRow, purchaseTable.getTable(), totalsRow, btnRow);
    }

    private void refreshTotals() {
        subtotalLbl.setText("Subtotal: $" + purchaseCart.getSubtotal().setScale(2, java.math.RoundingMode.HALF_UP));
        taxLbl.setText("Tax: $" + purchaseCart.getTax().setScale(2, java.math.RoundingMode.HALF_UP));
        totalLbl.setText("Total: $" + purchaseCart.getTotal().setScale(2, java.math.RoundingMode.HALF_UP));
    }

    public Parent getView() { return root; }
}
