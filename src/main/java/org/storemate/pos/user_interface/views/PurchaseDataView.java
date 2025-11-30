package org.storemate.pos.user_interface.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.PurchaseDAO;
import org.storemate.pos.models.Purchase;
import org.storemate.pos.models.enums.Status;
import org.storemate.pos.routing.Router;

import java.util.List;

public class PurchaseDataView {

    private final VBox root;
    private TableView<Purchase> purchaseTable;
    private ObservableList<Purchase> purchaseData;
    private final PurchaseDAO purchaseDAO;

    public PurchaseDataView() {
        purchaseDAO = new PurchaseDAO();
        root = new VBox(15);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("page-root");
        initializeView();
        loadPurchases();
    }

    private void initializeView() {
        Label title = new Label("Purchase Orders");
        title.getStyleClass().add("label-title");

        // ONLY ADD PURCHASE BUTTON
        Button addBtn = new Button("Add Purchase Order");
        addBtn.getStyleClass().add("button-connect");
        addBtn.setOnAction(e -> Router.navigate("/purchases/order"));

        HBox actionBox = new HBox(addBtn);
        actionBox.setAlignment(Pos.CENTER_LEFT);
        actionBox.setSpacing(10);
        actionBox.getStyleClass().add("section-divider");

        // Search Row
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> sortByPaymentBox = new ComboBox<>();
        sortByPaymentBox.getItems().addAll("CASH","DEBIT","CREDIT");

        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("button-search");
        searchBtn.setOnAction(e -> searchPurchase(sortByPaymentBox.getValue()));

        Button refreshBtn = new Button("Refresh");
        refreshBtn.getStyleClass().add("button-reset");
        refreshBtn.setOnAction(e -> loadPurchases());
        searchBox.getStyleClass().add("section-divider");

        searchBox.getChildren().addAll(sortByPaymentBox, searchBtn, refreshBtn);

        // TableView setup
        purchaseTable = new TableView<>();
        purchaseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Purchase, Integer> idCol = new TableColumn<>("Purchase ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("purchaseID"));

        TableColumn<Purchase, Integer> supplierCol = new TableColumn<>("Supplier ID");
        supplierCol.setCellValueFactory(new PropertyValueFactory<>("supplierID"));

        TableColumn<Purchase, Double> taxCol = new TableColumn<>("Tax Amount");
        taxCol.setCellValueFactory(new PropertyValueFactory<>("tax_amount"));

        TableColumn<Purchase, Double> totalCol = new TableColumn<>("Total Amount");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total_amount"));

        TableColumn<Purchase, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory((new PropertyValueFactory<>("purchaseStatus")));

        TableColumn<Purchase, String> payCol = new TableColumn<>("Payment Method");
        payCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        TableColumn<Purchase, String> dateCol = new TableColumn<>("Purchase Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));

        purchaseTable.getColumns().addAll(
                idCol, supplierCol, taxCol, totalCol, statusCol, payCol, dateCol
        );

        root.getChildren().addAll(title, actionBox, searchBox, purchaseTable);
    }

    private void loadPurchases() {
        List<Purchase> purchases = purchaseDAO.getAllPurchasesForTable();
        purchaseData = FXCollections.observableArrayList(purchases);
        purchaseTable.setItems(purchaseData);
    }

    private void searchPurchase(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            loadPurchases();
            return;
        }

        List<Purchase> result = purchaseDAO.searchPurchaseByPaymentMethod(keyword);
        if (result != null) {
            purchaseData = FXCollections.observableArrayList(result);
        } else {
            purchaseData = FXCollections.observableArrayList();
        }
        purchaseTable.setItems(purchaseData);
    }

    public Parent getView() {
        return root;
    }
}
