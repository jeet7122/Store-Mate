package org.storemate.pos.user_interface.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.SalesReportDAO;
import org.storemate.pos.dto.SalesReportDTO;

import java.time.LocalDate;
import java.util.List;

public class SalesDataView {

    private final VBox root;
    private final TableView<SalesReportDTO> table;

    private final ObservableList<SalesReportDTO> data = FXCollections.observableArrayList();

    private final SalesReportDAO salesReportDAO = new SalesReportDAO();

    // Pagination
    private int limit = 20;     // rows per page
    private int offset = 0;     // current offset

    public SalesDataView() {
        root = new VBox(15);
        root.setPadding(new Insets(20));

        Label title = new Label("Sales Data View");

        // -------------------------
        // Search and Filter UI
        // -------------------------

        TextField searchTF = new TextField();
        searchTF.setPromptText("Enter ID");

        ComboBox<String> optionBox = new ComboBox<>(FXCollections.observableArrayList(
                "Product ID", "Employee ID"
        ));
        optionBox.setValue("Product ID");

        Button searchBTN = new Button("Search");

        HBox searchBox = new HBox(10,
                new Label("Search: "), searchTF,
                new Label("By:"), optionBox,
                searchBTN
        );

        // Date Filter
        DatePicker startDate = new DatePicker();
        DatePicker endDate = new DatePicker();
        Button filterBTN = new Button("Filter");

        HBox dateFilterBox = new HBox(10,
                new Label("From: "), startDate,
                new Label("To: "), endDate,
                filterBTN
        );

        // -------------------------
        // TableView setup
        // -------------------------
        table = new TableView<>();
        table.setItems(data);

        TableColumn<SalesReportDTO, Integer> saleIDCol = new TableColumn<>("Sale ID");
        saleIDCol.setCellValueFactory(v -> v.getValue().getSaleID());

        TableColumn<SalesReportDTO, Integer> empIDCol = new TableColumn<>("Employee");
        empIDCol.setCellValueFactory(v -> v.getValue().getEmployeeID());

        TableColumn<SalesReportDTO, String> empNameCol = new TableColumn<>("Employee Name");
        empNameCol.setCellValueFactory(v -> v.getValue().getEmployeeName());

        TableColumn<SalesReportDTO, String> productIDCol = new TableColumn<>("Product ID");
        productIDCol.setCellValueFactory(v -> v.getValue().getProductID());

        TableColumn<SalesReportDTO, String> productNameCol = new TableColumn<>("Product Name");
        productNameCol.setCellValueFactory(v -> v.getValue().getProductName());

        TableColumn<SalesReportDTO, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(v -> v.getValue().getQuantity());

        TableColumn<SalesReportDTO, Double> priceCol = new TableColumn<>("Unit Price");
        priceCol.setCellValueFactory(v -> v.getValue().getUnitPrice());

        TableColumn<SalesReportDTO, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(v -> v.getValue().getSaleTotal());

        TableColumn<SalesReportDTO, String> paymentCol = new TableColumn<>("Payment");
        paymentCol.setCellValueFactory(v -> v.getValue().getPaymentMethod());

        TableColumn<SalesReportDTO, LocalDate> dateCol = new TableColumn<>("Sale Date");
        dateCol.setCellValueFactory(v -> v.getValue().getSaleDateAndTime());

        table.getColumns().addAll(
                saleIDCol, empIDCol, empNameCol,
                productIDCol, productNameCol,
                qtyCol, priceCol, totalCol, paymentCol, dateCol
        );

        // -------------------------
        // Pagination Buttons
        // -------------------------
        Button prevBTN = new Button("Previous");
        Button nextBTN = new Button("Next");

        HBox paginationBox = new HBox(10, prevBTN, nextBTN);

        // -------------------------
        // Load initial data
        // -------------------------
        loadPage();

        // -------------------------
        // Event Listeners
        // -------------------------
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
            if (offset >= limit) {
                offset -= limit;
            }
            loadPage();
        });

        root.getChildren().addAll(
                title,
                searchBox,
                dateFilterBox,
                table,
                paginationBox
        );
    }

    // -------------------------
    // Pagination Loader
    // -------------------------
    private void loadPage() {
        List<SalesReportDTO> list = salesReportDAO.getPaginatedSales(limit, offset);

        if (list.isEmpty() && offset > 0) {
            offset -= limit;
            return;
        }

        data.setAll(list);
    }

    public Parent getView() {
        return root;
    }
}
