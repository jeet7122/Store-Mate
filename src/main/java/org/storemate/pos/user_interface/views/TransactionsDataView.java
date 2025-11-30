package org.storemate.pos.user_interface.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.PurchaseDAO;
import org.storemate.pos.dao.ReturnDAO;
import org.storemate.pos.dao.SaleDAO;
import org.storemate.pos.dao.TransactionsDAO;
import org.storemate.pos.models.Purchase;
import org.storemate.pos.models.Returns;
import org.storemate.pos.models.Sale;
import org.storemate.pos.models.Transaction;

import java.util.List;

public class TransactionsDataView {
    private final TransactionsDAO transactionsDAO;
    private final VBox root;

    private TableView<Transaction> tableView;
    private ObservableList<Transaction> allTransactions;
    private static final int ROWS_PER_PAGE = 10;
    private Pagination pagination;

    public TransactionsDataView() {
        transactionsDAO = new TransactionsDAO();

        root = new VBox(20);
        root.getStyleClass().add("page-root");

        // --- Title ---
        HBox titleBox = new HBox();
        titleBox.setMaxWidth(Double.MAX_VALUE);
        titleBox.setAlignment(Pos.CENTER);

        Label title = new Label("Transactions Dashboard");
        title.getStyleClass().add("label-title");
        titleBox.getChildren().add(title);
        root.getChildren().add(titleBox);

        // --- Table and Pagination Card ---
        tableView = new TableView<>();
        setupTableColumns();
        tableView.setPrefHeight(400);

        pagination = new Pagination();
        pagination.setPageFactory(this::createPage);

        VBox tableCard = new VBox(10, tableView, pagination);
//        tableCard.setStyle(
//                "-fx-background-color: #ffffff; " +
//                        "-fx-padding: 15; " +
//                        "-fx-background-radius: 12; " +
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);"
//        );
        tableCard.getStyleClass().add("section-box");
        root.getChildren().add(tableCard);

        // --- Load Data AFTER tableView and pagination exist ---
        loadTransactionsFromDB();

        // --- PieCharts ---
        PieChart salesPie = createPieChartFromList("Sales");
        PieChart purchasesPie = createPieChartFromList("Purchases");
        PieChart returnsPie = createPieChartFromList("Returns");

        HBox chartsBox = new HBox(20,
                createPieCard("Sales", salesPie),
                createPieCard("Purchases", purchasesPie),
                createPieCard("Returns", returnsPie)
        );
        chartsBox.setStyle("-fx-alignment: center; -fx-padding: 10;");
        root.getChildren().add(chartsBox);
        root.setPadding(new Insets(10,10,10,10));
    }

    // --- Modular Pie Chart Card ---
    private VBox createPieCard(String title, PieChart chart) {
        Label lbl = new Label(title);
        lbl.getStyleClass().add("label-subtitle");

        VBox card = new VBox(8, lbl, chart);
        card.setStyle(
                "-fx-background-color: #ffffff; " +
                        "-fx-padding: 12; " +
                        "-fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 2);"
        );
        card.getStyleClass().add("pie-card");
        return card;
    }

    // --- Setup Table Columns ---
    private void setupTableColumns() {
        TableColumn<Transaction, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Transaction, String> paymentCol = new TableColumn<>("Payment");
        paymentCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        TableColumn<Transaction, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tableView.getColumns().addAll(idCol, typeCol, paymentCol, amountCol);
        tableView.getStyleClass().add("table-view");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    // --- Pagination Page Factory ---
    private VBox createPage(int pageIndex) {
        if (allTransactions == null) return new VBox();
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allTransactions.size());
        tableView.setItems(FXCollections.observableArrayList(allTransactions.subList(fromIndex, toIndex)));
        return new VBox(tableView);
    }

    // --- Filter Table based on Pie Click ---
    private void filterTable(String transactionType, String paymentMethod) {
        List<Transaction> filtered = allTransactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase(transactionType) && t.getPaymentMethod().equalsIgnoreCase(paymentMethod))
                .toList();

        if (filtered.isEmpty()) {
            tableView.setItems(FXCollections.observableArrayList());
            pagination.setPageCount(1);
            return;
        }

        tableView.setItems(FXCollections.observableArrayList(
                filtered.subList(0, Math.min(ROWS_PER_PAGE, filtered.size()))
        ));
        pagination.setPageCount((int) Math.ceil((double) filtered.size() / ROWS_PER_PAGE));
    }

    // --- Load Transactions from DB ---
    private void loadTransactionsFromDB() {
        allTransactions = FXCollections.observableArrayList(transactionsDAO.getAllTransactions());
        // Set first page
        tableView.setItems(FXCollections.observableArrayList(
                allTransactions.subList(0, Math.min(ROWS_PER_PAGE, allTransactions.size()))
        ));
        pagination.setPageCount((int) Math.ceil((double) allTransactions.size() / ROWS_PER_PAGE));
    }

        private PieChart createPieChartFromList(String transactionType){
        int cash = 0, debit = 0, credit = 0;
        for (Transaction t : allTransactions){
            if (!t.getType().equalsIgnoreCase(transactionType)) continue;
            switch (t.getPaymentMethod().toUpperCase()){
                case "CASH" -> cash++;
                case "DEBIT" -> debit++;
                case "CREDIT" -> credit++;
            }
        }
        return createInteractivePie(transactionType, cash, debit, credit);
    }

    // --- Create Interactive PieChart ---
    private PieChart createInteractivePie(String title, int cash, int debit, int credit) {
        PieChart pie = new PieChart();
        pie.setTitle(title);

        PieChart.Data cashData = new PieChart.Data("Cash", cash);
        PieChart.Data debitData = new PieChart.Data("Debit", debit);
        PieChart.Data creditData = new PieChart.Data("Credit", credit);

        pie.getData().addAll(cashData, debitData, creditData);

        // Pie slice colors
        cashData.getNode().setStyle("-fx-pie-color: #4CAF50;");
        debitData.getNode().setStyle("-fx-pie-color: #2196F3;");
        creditData.getNode().setStyle("-fx-pie-color: #FF9800;");

        // Click events
        cashData.getNode().setOnMouseClicked(e -> filterTable(title, "Cash"));
        debitData.getNode().setOnMouseClicked(e -> filterTable(title, "Debit"));
        creditData.getNode().setOnMouseClicked(e -> filterTable(title, "Credit"));

        return pie;
    }

    public Parent getView() {
        return root;
    }
}
