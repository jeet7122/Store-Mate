package org.storemate.pos.user_interface.pages;

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
import org.storemate.pos.dao.ProductSupplierDAO;
import org.storemate.pos.dao.SuppliersDAO;
import org.storemate.pos.dto.PurchaseDTO;
import org.storemate.pos.models.EmailSettings;
import org.storemate.pos.models.Product;
import org.storemate.pos.models.Supplier;
import org.storemate.pos.models.enums.PaymentMethod;
import org.storemate.pos.models.enums.PurchaseStatus;
import org.storemate.pos.routing.Router;
import org.storemate.pos.services.EmailService;
import org.storemate.pos.services.PurchaseOrderService;
import org.storemate.pos.user_interface.components.PurchaseCartTable;
import org.storemate.pos.utils.EmailConfigLoader;

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
        root.setPadding(new Insets(15));
        root.getStyleClass().add("vbox-spaced");

        // ---------------- Title ----------------
        Label title = new Label("Purchase Orders");
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
            Router.navigate("/purchases");
        });
        topBar.getChildren().addAll(backButton, title);


        // ---------------- Suppliers ----------------
        SuppliersDAO supplierDAO = new SuppliersDAO();
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();

        Label supplierLbl = new Label("Supplier:");
        supplierLbl.getStyleClass().add("combo-label");

        ComboBox<Supplier> supplierCombo = new ComboBox<>();
        supplierCombo.getItems().addAll(suppliers);
        supplierCombo.setPromptText("Select Supplier");
        supplierCombo.setPrefWidth(300);
        supplierCombo.getStyleClass().add("combo-box");
        supplierCombo.valueProperty().addListener((obs, old, sel) -> {
            if (sel != null) purchaseCart.setSupplierID(sel.getSupplier_id());
        });

        Label statusLbl = new Label("Status:");
        statusLbl.getStyleClass().add("combo-label");

        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("PENDING", "RECEIVED", "CANCELLED");
        statusCombo.getSelectionModel().selectFirst();
        statusCombo.getStyleClass().add("combo-box");

        HBox supplierRow = new HBox(12, supplierLbl, supplierCombo, statusLbl, statusCombo);
        supplierRow.setPadding(new Insets(6));
        supplierRow.getStyleClass().add("hbox-spaced");

        // ---------------- Search Area ----------------
        Label productLbl = new Label("Product:");
        productLbl.getStyleClass().add("combo-label");

        TextField searchTF = new TextField();
        searchTF.setPromptText("Search product name or id");
        searchTF.getStyleClass().add("text-field");
        searchTF.setPrefWidth(250);

        TextField qtyTF = new TextField();
        qtyTF.setPromptText("Qty");
        qtyTF.getStyleClass().add("text-field");
        qtyTF.setPrefWidth(80);

        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().addAll("button", "button-search");
        searchBtn.setOnAction(e -> searchProduct(searchTF));

        Button addBtn = new Button("Add To Purchase");
        addBtn.getStyleClass().addAll("button", "button-update");
        addBtn.setOnAction(e -> addToPurchase(searchTF, qtyTF));

        HBox searchRow = new HBox(8, productLbl, searchTF, qtyTF, addBtn, searchBtn);
        searchRow.setPadding(new Insets(6));
        searchRow.getStyleClass().add("search-section");

        // ---------------- Totals ----------------
        subtotalLbl = new Label("Subtotal: $0.00");
        subtotalLbl.getStyleClass().add("label-total");

        taxLbl = new Label("Tax: $0.00");
        taxLbl.getStyleClass().add("label-total");

        totalLbl = new Label("Total: $0.00");
        totalLbl.getStyleClass().add("label-total-positive");

        HBox totalsRow = new HBox(12, subtotalLbl, taxLbl, totalLbl);
        totalsRow.setPadding(new Insets(6));
        totalsRow.getStyleClass().add("hbox-spaced");

        // ---------------- Action Buttons ----------------
        Button voidBtn = new Button("VOID");
        voidBtn.getStyleClass().addAll("button", "button-warning");
        voidBtn.setOnAction(e -> {
            purchaseCart.clear();
            purchaseTable.refresh();
            refreshTotals();
        });

        Button submitBtn = new Button("SUBMIT ORDER");
        submitBtn.getStyleClass().addAll("button", "button-connect");
        submitBtn.setOnAction(e -> submitPurchase(supplierDAO, statusCombo));

        HBox btnRow = new HBox(8, voidBtn, submitBtn);
        btnRow.setPadding(new Insets(6));
        btnRow.getStyleClass().add("hbox-spaced");

        // ---------------- Test Email ----------------
        Button testEmailBtn = new Button("Test Email");
        testEmailBtn.getStyleClass().addAll("button", "button-update");
        testEmailBtn.setOnAction(e -> testEmail());

        // ---------------- Add to Root ----------------
        root.getChildren().addAll(topBar, supplierRow, searchRow, purchaseTable.getTable(),
                totalsRow, btnRow, testEmailBtn);
    }

    // ---------------- Helper Methods ----------------

    private void searchProduct(TextField searchTF) {
        Product product = new ProductDAO().searchForProduct(searchTF.getText());
        if (product == null) {
            new Alert(Alert.AlertType.WARNING, "Product not found").show();
        } else {
            new Alert(Alert.AlertType.INFORMATION,
                    "Found: " + product.getProduct_name() + "\nPrice: " + product.getProduct_price())
                    .show();
        }
    }

    private void addToPurchase(TextField searchTF, TextField qtyTF) {
        if (purchaseCart.getSupplierID() <= 0) {
            new Alert(Alert.AlertType.WARNING, "Please select a supplier first").show();
            return;
        }

        Product product = new ProductDAO().searchForProduct(searchTF.getText());
        if (product == null) {
            new Alert(Alert.AlertType.WARNING, "Product not found").show();
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(qtyTF.getText());
        } catch (Exception ignored) {
            quantity = 1;
        }

        ProductSupplierDAO psdao = new ProductSupplierDAO();
        BigDecimal cost = psdao.getCostPriceForSupplier(product.getProduct_id(), purchaseCart.getSupplierID());
        final int q = quantity;

        if (cost == null) {
            Alert fallback = new Alert(Alert.AlertType.CONFIRMATION,
                    "No supplier cost found. Use product sale price as cost?",
                    ButtonType.YES, ButtonType.NO);
            fallback.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) {
                    purchaseCart.addItem(product, q, product.getProduct_price());
                    purchaseTable.refresh();
                    refreshTotals();
                }
            });
        } else {
            purchaseCart.addItem(product, quantity, cost);
            purchaseTable.refresh();
            refreshTotals();
        }
    }

    private void submitPurchase(SuppliersDAO supplierDAO, ComboBox<String> statusCombo) {
        if (purchaseCart.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Purchase cart is empty").show();
            return;
        }
        if (purchaseCart.getSupplierID() <= 0) {
            new Alert(Alert.AlertType.WARNING, "Select supplier").show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Create purchase order for supplier, update inventory, and send email?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                Supplier supplier = supplierDAO.getSupplierById(purchaseCart.getSupplierID());
                if (supplier == null) {
                    new Alert(Alert.AlertType.ERROR, "Supplier not found").show();
                    return;
                }

                PurchaseOrderService service = new PurchaseOrderService();
                int purchaseId = service.createAndSendOrder(purchaseCart, statusCombo.getValue(), supplier);

                if (purchaseId > 0) {
                    new Alert(Alert.AlertType.INFORMATION,
                            "Purchase Order Created and Email Sent! (ID: " + purchaseId + ")").show();
                    purchaseCart.clear();
                    purchaseTable.refresh();
                    refreshTotals();
                } else {
                    new Alert(Alert.AlertType.ERROR,
                            "Failed to create purchase order or send email").show();
                }
            }
        });
    }

    private void testEmail() {
        EmailSettings settings = EmailConfigLoader.loadFromResources();
        if (settings == null) {
            new Alert(Alert.AlertType.ERROR, "Email config not found").show();
            return;
        }

        EmailService svc = new EmailService(settings);
        TextInputDialog dlg = new TextInputDialog(settings.getFrom());
        dlg.setHeaderText("Enter recipient to send test email to:");
        dlg.showAndWait().ifPresent(recipient -> {
            try {
                svc.sendHtmlEmail(recipient, "Store-Mate Test Email",
                        "<b>This is a test from Store-Mate</b>");
                new Alert(Alert.AlertType.INFORMATION, "Test email sent").show();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Failed to send: " + ex.getMessage()).show();
                ex.printStackTrace();
            }
        });
    }

    private void refreshTotals() {
        subtotalLbl.setText("Subtotal: $" + purchaseCart.getSubtotal().setScale(2, BigDecimal.ROUND_HALF_UP));
        taxLbl.setText("Tax: $" + purchaseCart.getTax().setScale(2, BigDecimal.ROUND_HALF_UP));
        totalLbl.setText("Total: $" + purchaseCart.getTotal().setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    public Parent getView() {
        return root;
    }
}
