package org.storemate.pos.user_interface.views;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.ReturnDAO;
import org.storemate.pos.models.Returns;
import org.storemate.pos.models.enums.PaymentMethod;
import org.storemate.pos.routing.Router;
import org.storemate.pos.utils.Alerts;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class ReturnsDataView {

    private final VBox root;
    private final ObservableList<Returns> data = FXCollections.observableArrayList();
    private final ReturnDAO returnDAO = new ReturnDAO();
    private final Alerts alerts = new Alerts();

    public ReturnsDataView() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("root");

        // Title
        Label title = new Label("Returns Data View");
        title.getStyleClass().add("label-title");

        // Search
        TextField searchTF = new TextField();
        searchTF.setPromptText("Search by Payment Method");
        searchTF.getStyleClass().add("text-field");

        Button searchBTN = new Button("Search");
        searchBTN.getStyleClass().add("button-search");

        Label searchLabel = new Label("Search:");
        searchLabel.getStyleClass().add("label-sub_head_title");

        HBox searchBox = new HBox(10, searchLabel, searchTF, searchBTN);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.getStyleClass().add("search-box");

        // Refresh
        Button refreshBTN = new Button("Refresh");
        refreshBTN.getStyleClass().add("button-reset");

        HBox topBox = new HBox(10, refreshBTN);
        topBox.setAlignment(Pos.CENTER_LEFT);

        // Table
        TableView<Returns> table = new TableView<>();
        table.setItems(data);
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // Return ID Column
        TableColumn<Returns, Integer> idCol = new TableColumn<>("Return ID");
        idCol.setCellValueFactory(v -> new ReadOnlyObjectWrapper<>(v.getValue().getReturnID()));

        // Payment Method Column
        TableColumn<Returns, String> payCol = new TableColumn<>("Payment Method");
        payCol.setCellValueFactory(v -> new ReadOnlyStringWrapper(v.getValue().getPaymentMethod().name()));

        // Total Amount Column
        TableColumn<Returns, BigDecimal> amountCol = new TableColumn<>("Total Amount");
        amountCol.setCellValueFactory(v -> new ReadOnlyObjectWrapper<>(v.getValue().getTotalAmount()));

        // Return Date Column
        TableColumn<Returns, String> dateCol = new TableColumn<>("Return Date");
        dateCol.setCellValueFactory(v -> {
            Timestamp ts = v.getValue().getReturnDate();
            String formatted = (ts != null)
                    ? new SimpleDateFormat("MMM dd, yyyy HH:mm").format(ts)
                    : "--";
            return new ReadOnlyStringWrapper(formatted);
        });

        table.getColumns().addAll(idCol, payCol, amountCol, dateCol);

        // Load data initially
        loadReturns();

        // Search button action
        searchBTN.setOnAction(e -> {
            String q = searchTF.getText().trim();
            if (!q.isEmpty()) {
                try {
                    PaymentMethod pm = PaymentMethod.valueOf(q.toUpperCase());
                    List<Returns> list = returnDAO.getAllReturns();
                    list.removeIf(r -> !r.getPaymentMethod().equals(pm));
                    updateTable(list);
                } catch (Exception ex) {
                    showAlert("Invalid payment method entered.");
                }
            } else {
                loadReturns();
            }
        });

        // Refresh button action
        refreshBTN.setOnAction(e -> loadReturns());

        root.getChildren().addAll(
                title,
                topBox,
                searchBox,
                table
        );
    }

    private void loadReturns() {
        List<Returns> returnsList = returnDAO.getAllReturns();
        updateTable(returnsList);
    }

    private void updateTable(List<Returns> list) {
        data.setAll(list);
    }

    private void showAlert(String message) {
        alerts.showErrorAlert("Invalid Input", message);
    }

    public Parent getView() {
        return root;
    }
}
