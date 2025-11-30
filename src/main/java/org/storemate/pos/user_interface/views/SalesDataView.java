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
import org.storemate.pos.dao.SalesReportDAO;
import org.storemate.pos.dto.SalesReportDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SalesDataView {

    private final VBox root;
    private final ObservableList<SalesReportDTO> data = FXCollections.observableArrayList();
    private final SalesReportDAO salesReportDAO = new SalesReportDAO();

    private final int limit = 20; // rows per page
    private int offset = 0;

    public SalesDataView() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("root");

        // ---------- Title ----------
        Label title = new Label("Sales Data View");
        title.getStyleClass().add("label-title");

        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setMaxWidth(Double.MAX_VALUE);

        // ---------- Search & Filter ----------
        TextField searchTF = new TextField();
        searchTF.setPromptText("Enter ID");

        ComboBox<String> optionBox = new ComboBox<>(FXCollections.observableArrayList(
                "Product ID", "Employee ID"
        ));
        optionBox.setValue("Product ID");

        Button searchBTN = new Button("Search");
        searchBTN.getStyleClass().add("button-connect");
        Label searchLabel = new Label("Search:");
        Label byLabel = new Label("By:");
        searchLabel.getStyleClass().add("label-sub_head_title");
        byLabel.getStyleClass().add("label-sub_head_title");

        HBox searchBox = new HBox(10,
                searchLabel, searchTF,
                byLabel, optionBox,
                searchBTN
        );
        searchBox.getStyleClass().add("section-divider");

        DatePicker startDate = new DatePicker();
        DatePicker endDate = new DatePicker();
        Button filterBTN = new Button("Filter");
        filterBTN.getStyleClass().add("button-connect");
        Label fromLabel = new Label("From:");
        Label endLabel = new Label("To:");
        fromLabel.getStyleClass().add("label-sub_head_title");
        endLabel.getStyleClass().add("label-sub_head_title");

        HBox dateFilterBox = new HBox(10,
                fromLabel, startDate,
                endLabel, endDate,
                filterBTN
        );
        dateFilterBox.getStyleClass().add("section-divider");

        // ---------- Table ----------
        TableView<SalesReportDTO> table = new TableView<>();
        table.setItems(data);
        table.getStyleClass().add("table-view");

        TableColumn<SalesReportDTO, Integer> saleIDCol = new TableColumn<>("Sale ID");
        saleIDCol.setCellValueFactory(v ->
                new ReadOnlyObjectWrapper<>(v.getValue().getSaleID()));

        TableColumn<SalesReportDTO, Integer> empIDCol = new TableColumn<>("Employee");
        empIDCol.setCellValueFactory(v ->
                new ReadOnlyObjectWrapper<>(v.getValue().getEmployeeID()));

        TableColumn<SalesReportDTO, String> empNameCol = new TableColumn<>("Employee Name");
        empNameCol.setCellValueFactory(v ->
                new ReadOnlyStringWrapper(v.getValue().getEmployeeName()));

        TableColumn<SalesReportDTO, String> productIDCol = new TableColumn<>("Product ID");
        productIDCol.setCellValueFactory(v ->
                new ReadOnlyStringWrapper(v.getValue().getProductID()));

        TableColumn<SalesReportDTO, String> productNameCol = new TableColumn<>("Product Name");
        productNameCol.setCellValueFactory(v ->
                new ReadOnlyStringWrapper(v.getValue().getProductName()));

        TableColumn<SalesReportDTO, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(v ->
                new ReadOnlyObjectWrapper<>(v.getValue().getQuantity()));

        TableColumn<SalesReportDTO, Double> priceCol = new TableColumn<>("Unit Price");
        priceCol.setCellValueFactory(v ->
                new ReadOnlyObjectWrapper<>(v.getValue().getUnitPrice()));

        TableColumn<SalesReportDTO, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(v ->
                new ReadOnlyObjectWrapper<>(v.getValue().getSaleTotal()));

        TableColumn<SalesReportDTO, String> paymentCol = new TableColumn<>("Payment");
        paymentCol.setCellValueFactory(v ->
                new ReadOnlyStringWrapper(v.getValue().getPaymentMethod()));

        TableColumn<SalesReportDTO, LocalDateTime> dateCol = new TableColumn<>("Sale Date");
        dateCol.setCellValueFactory(v ->
                new ReadOnlyObjectWrapper<>(v.getValue().getSaleDateAndTime().toLocalDateTime()));

        table.getColumns().addAll(
                saleIDCol, empIDCol, empNameCol,
                productIDCol, productNameCol,
                qtyCol, priceCol, totalCol, paymentCol, dateCol
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // ---------- Pagination ----------
        Button prevBTN = new Button("Previous");
        prevBTN.getStyleClass().add("pagination-button");

        Button nextBTN = new Button("Next");
        nextBTN.getStyleClass().add("pagination-button");

        HBox paginationBox = new HBox(10, prevBTN, nextBTN);

        // ---------- Load Initial Page ----------
        loadPage();

        // ---------- Events ----------
        searchBTN.setOnAction(e -> {
            String value = searchTF.getText().trim();
            if (value.isEmpty()) return;

            boolean isEmployee = optionBox.getValue().equals("Employee ID");
            Integer empID = isEmployee ? Integer.parseInt(value) : null;
            String productID = !isEmployee ? value : null;

            List<SalesReportDTO> filtered =
                    salesReportDAO.filterSales(empID, productID, null, null);
            data.setAll(filtered);
        });

        filterBTN.setOnAction(e -> {
            LocalDate from = startDate.getValue();
            LocalDate to = endDate.getValue();

            List<SalesReportDTO> filtered =
                    salesReportDAO.filterSales(null, null, from, to);
            data.setAll(filtered);
        });

        nextBTN.setOnAction(e -> {
            offset += limit;
            loadPage();
        });

        prevBTN.setOnAction(e -> {
            if (offset >= limit) offset -= limit;
            loadPage();
        });

        // ---------- Build Root ----------
        root.getChildren().addAll(
                titleBox,
                searchBox,
                dateFilterBox,
                table,
                paginationBox
        );
        root.setPadding(new Insets(10,10,10,10));
    }

    private void loadPage() {
        List<SalesReportDTO> list = salesReportDAO.getPaginatedSales(limit, offset);
        if (list.isEmpty() && offset > 0) offset -= limit;
        data.setAll(list);
    }

    public Parent getView() {
        return root;
    }
}
