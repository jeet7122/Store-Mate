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
import org.storemate.pos.dao.SuppliersDAO;
import org.storemate.pos.models.Supplier;
import org.storemate.pos.models.enums.Status;
import org.storemate.pos.routing.Router;

import java.util.List;

public class SuppliersDataView {

    private final VBox root;
    private final ObservableList<Supplier> data = FXCollections.observableArrayList();  // ObservableList for Suppliers
    private final SuppliersDAO supplierDAO = new SuppliersDAO();

    public SuppliersDataView() {

        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("root");

        // Title
        Label title = new Label("Suppliers Data View");
        title.getStyleClass().add("label-title");

        // Search
        TextField searchTF = new TextField();
        searchTF.setPromptText("Search Supplier...");
        searchTF.getStyleClass().add("text-field");

        Button searchBTN = new Button("Search");
        searchBTN.getStyleClass().add("button-connect");

        Label searchLabel = new Label("Search:");
        searchLabel.getStyleClass().add("label-sub_head_title");

        HBox searchBox = new HBox(10, searchLabel, searchTF, searchBTN);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.getStyleClass().add("search-box");

        // Buttons
        Button addSupplierBTN = new Button("Add Supplier");
        addSupplierBTN.getStyleClass().add("button-connect");
        addSupplierBTN.setOnAction(e -> Router.navigate("/suppliers/add"));

        Button refreshBTN = new Button("Refresh");
        refreshBTN.getStyleClass().add("button-reset");

        HBox topActionBox = new HBox(10, addSupplierBTN, refreshBTN);
        topActionBox.setAlignment(Pos.CENTER_LEFT);

        // Table for Suppliers
        TableView<Supplier> supplierTableView = new TableView<>();
        supplierTableView.setItems(data);
        supplierTableView.getStyleClass().add("table-view");
        supplierTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Define Columns for the Table
        TableColumn<Supplier, Integer> idColumn = new TableColumn<>("Supplier ID");
        idColumn.setCellValueFactory(v -> new ReadOnlyObjectWrapper<>(v.getValue().getSupplier_id()));

        TableColumn<Supplier, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(v -> new ReadOnlyStringWrapper(v.getValue().getSupplier_name()));

        TableColumn<Supplier, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(v -> new ReadOnlyStringWrapper(v.getValue().getSupplier_email()));

        TableColumn<Supplier, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(v -> new ReadOnlyStringWrapper(v.getValue().getSupplier_location()));

        TableColumn<Supplier, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(v -> new ReadOnlyStringWrapper(v.getValue().getSupplier_status().toString()));

        // Add columns to the table
        supplierTableView.getColumns().addAll(idColumn, nameColumn, emailColumn, locationColumn, statusColumn);

        loadSupplierData();

        searchBTN.setOnAction(e -> {
            String searchQuery = searchTF.getText().trim();
            if (!searchQuery.isEmpty()) {
                List<Supplier> suppliers = supplierDAO.searchForSupplier(searchQuery);
                updateSupplierTable(suppliers);
            } else {
                loadSupplierData();
            }
        });

        //  Refresh Button Action
        refreshBTN.setOnAction(e -> loadSupplierData());

        // Root layout
        root.getChildren().addAll(
                title,
                topActionBox,
                searchBox,
                supplierTableView
        );
    }

    private void loadSupplierData() {
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        updateSupplierTable(suppliers);
    }

    private void updateSupplierTable(List<Supplier> suppliers) {
        data.setAll(suppliers);
    }

    public Parent getView() {
        return root;
    }
}
