package org.storemate.pos.user_interface.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.storemate.pos.models.Purchase;
import org.storemate.pos.models.Returns;
import org.storemate.pos.models.Sale;
import org.storemate.pos.models.Transaction;

public class TransactionsDataView {

    private final SaleDAO saleDAO;
    private final ReturnDAO returnDAO;
    private final PurchaseDAO purchaseDAO;
    private final VBox root;

    private TableView<Transaction> tableView;
    private ObservableList<Transaction> allTransactions;
    private static final int ROWS_PER_PAGE = 20;
    private Pagination pagination;

    public TransactionsDataView() {
        // Initialize DAOs
        saleDAO = new SaleDAO();
        purchaseDAO = new PurchaseDAO();
        returnDAO = new ReturnDAO();

        root = new VBox(20);
        root.getStyleClass().add("root");

        // --- Title ---
        Label title = new Label("Transactions Dashboard");
        title.getStyleClass().add("label-title");
        root.getChildren().add(title);

        // --- PieCharts ---
        PieChart salesPie = createPieChartFromDB("Sales");
        PieChart purchasesPie = createPieChartFromDB("Purchases");
        PieChart returnsPie = createPieChartFromDB("Returns");

        HBox chartsBox = new HBox(20,
                createPieCard("Sales", salesPie),
                createPieCard("Purchases", purchasesPie),
                createPieCard("Returns", returnsPie)
        );
        chartsBox.setStyle("-fx-alignment: center; -fx-padding: 10;");
        root.getChildren().add(chartsBox);

        // --- Table and Pagination Card ---
        VBox tableCard = new VBox(10);
        tableCard.setStyle(
                "-fx-background-color: #ffffff; " +
                        "-fx-padding: 15; " +
                        "-fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);"
        );

        tableView = new TableView<>();
        setupTableColumns();
        tableView.setPrefHeight(400);

        pagination = new Pagination();
        pagination.setPageFactory(this::createPage);

        tableCard.getChildren().addAll(tableView, pagination);
        root.getChildren().add(tableCard);

        // --- Load Data ---
        loadTransactionsFromDB();
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
        ObservableList<Transaction> filtered = FXCollections.observableArrayList();
        for (Transaction t : allTransactions) {
            if (t.getType().equalsIgnoreCase(transactionType)
                    && t.getPaymentMethod().equalsIgnoreCase(paymentMethod)) {
                filtered.add(t);
            }
        }

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
        allTransactions = FXCollections.observableArrayList();

        // Sales
        for (Sale s : saleDAO.getAllSales()) {
            allTransactions.add(new Transaction(
                    "S-" + s.getSaleID(), "Sales", String.valueOf(s.getPaymentMethod()), s.getTotal_amount(), s.getSale_timestamp()
            ));
        }

        // Purchases
        for (Purchase p : purchaseDAO.getAllPurchases()) {
            allTransactions.add(new Transaction(
                    "P-" + p.getPurchaseID(), "Purchases", String.valueOf(p.getPaymentMethod()), p.getTotal_amount(), p.getPurchaseDate()
            ));
        }

        // Returns
        for (Returns r : returnDAO.getAllReturns()) {
            allTransactions.add(new Transaction(
                    "R-" + r.getReturnID(), "Returns", String.valueOf(r.getPaymentMethod()), r.getTotalAmount(), r.getReturnDate()
            ));
        }

        // Set first page
        tableView.setItems(FXCollections.observableArrayList(
                allTransactions.subList(0, Math.min(ROWS_PER_PAGE, allTransactions.size()))
        ));
        pagination.setPageCount((int) Math.ceil((double) allTransactions.size() / ROWS_PER_PAGE));
    }

    // --- PieChart Factory from DB Counts ---
    private PieChart createPieChartFromDB(String title) {
        int cashCount = 0, debitCount = 0, creditCount = 0;

        switch (title) {
            case "Sales" -> {
                cashCount = saleDAO.countByPaymentMethod("CASH");
                debitCount = saleDAO.countByPaymentMethod("DEBIT");
                creditCount = saleDAO.countByPaymentMethod("CREDIT");
            }
            case "Purchases" -> {
                cashCount = purchaseDAO.countByPaymentMethod("CASH");
                debitCount = purchaseDAO.countByPaymentMethod("DEBIT");
                creditCount = purchaseDAO.countByPaymentMethod("CREDIT");
            }
            case "Returns" -> {
                cashCount = returnDAO.countByPaymentMethod("CASH");
                debitCount = returnDAO.countByPaymentMethod("DEBIT");
                creditCount = returnDAO.countByPaymentMethod("CREDIT");
            }
        }

        return createInteractivePie(title, cashCount, debitCount, creditCount);
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
