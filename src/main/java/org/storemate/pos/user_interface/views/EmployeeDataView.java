package org.storemate.pos.user_interface.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.*;
import org.storemate.pos.models.Employee;
import org.storemate.pos.models.enums.Role;
import org.storemate.pos.routing.Router;
import org.storemate.pos.services.AuthService;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class EmployeeDataView {
    private final VBox root;
    private final AuthService authService;
    private TableView<Employee> employeeTable;
    private ObservableList<Employee> employeeData;

    public EmployeeDataView() {
        authService = new AuthService();
        root = new VBox(20);
        root.setPadding(new Insets(10));
        initializeView();
        loadRealData();
    }

    private void initializeView() {
        // Title
        Label title = new Label("Employee Management Dashboard");
        title.getStyleClass().add("dashboard-title");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));

        // Quick Stats Cards
        HBox statsContainer = createStatsCards();

        // Action Buttons Section
        HBox actionButtons = createActionButtons();

        // Employee Table
        VBox tableSection = createEmployeeTable();

        // Charts Section
        HBox chartsContainer = createChartsSection();

        // Main content layout
        VBox mainContent = new VBox(20);
        mainContent.getChildren().addAll(statsContainer, actionButtons, tableSection, chartsContainer);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.getStyleClass().add("dashboard-scroll-pane");

        root.getChildren().addAll(title, scrollPane);
    }

    private HBox createStatsCards() {
        HBox statsContainer = new HBox(15);
        statsContainer.setAlignment(Pos.CENTER);

        // Get real data from service
        int totalEmployees = authService.getTotalEmployeesCount();
        int activeToday = authService.getActiveEmployeesToday();
        int newThisMonth = authService.getNewEmployeesThisMonth();
        Map<String, Integer> roleDistribution = authService.getRoleDistribution();

        // Total Employees Card
        VBox totalEmployeesCard = createStatCard(
                new FontIcon(MaterialDesignA.ACCOUNT_MULTIPLE),
                "Total Employees",
                String.valueOf(totalEmployees),
                "employee-stat-card"
        );

        // Active Today Card
        VBox activeTodayCard = createStatCard(
                new FontIcon(MaterialDesignC.CHECK_CIRCLE_OUTLINE),
                "Active Today",
                String.valueOf(activeToday),
                "active-stat-card"
        );

        // New This Month Card
        VBox newThisMonthCard = createStatCard(
                new FontIcon(MaterialDesignA.ACCOUNT_PLUS),
                "New This Month",
                String.valueOf(newThisMonth),
                "new-stat-card"
        );

        // Roles Distribution Card
        VBox rolesCard = createStatCard(
                new FontIcon(MaterialDesignA.ACCOUNT_COG),
                "Roles",
                roleDistribution.size() + " Types",
                "role-stat-card"
        );

        statsContainer.getChildren().addAll(totalEmployeesCard, activeTodayCard, newThisMonthCard, rolesCard);
        return statsContainer;
    }

    private VBox createStatCard(FontIcon icon, String title, String value, String styleClass) {
        VBox card = new VBox(10);
        card.getStyleClass().addAll("stat-card", styleClass);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefSize(180, 120);

        icon.setIconSize(30);
        icon.setIconColor(Color.DARKCYAN);

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-value");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("stat-title");

        card.getChildren().addAll(icon, valueLabel, titleLabel);
        return card;
    }

    private HBox createActionButtons() {
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER);
        actionButtons.getStyleClass().add("action-buttons-container");

        // Add Employee Button
        VBox addEmployeeBtn = createActionButton(
                new FontIcon(MaterialDesignP.PLUS),
                "Add Employee",
                "Add new employee to the system",
                "add-employee-btn"
        );
        addEmployeeBtn.setOnMouseClicked(e -> Router.navigate("/employees/add"));

        // Modify Employee Button
        VBox editEmployeeBtn = createActionButton(
                new FontIcon(MaterialDesignA.ACCOUNT_EDIT),
                "Modify Employee",
                "Edit existing employee details",
                "edit-employee-btn"
        );
        editEmployeeBtn.setOnMouseClicked(e -> Router.navigate("/employees/modify"));

        // Delete Employee Button
        VBox deleteEmployeeBtn = createActionButton(
                new FontIcon(MaterialDesignT.TRASH_CAN_OUTLINE),
                "Remove Employee",
                "Remove employee from system",
                "delete-employee-btn"
        );
        deleteEmployeeBtn.setOnMouseClicked(e -> Router.navigate("/employees/delete"));

        // Export Data Button
        VBox exportBtn = createActionButton(
                new FontIcon(MaterialDesignF.FILE_EXPORT),
                "Export Data",
                "Export employee data to CSV",
                "export-btn"
        );
        exportBtn.setOnMouseClicked(e -> exportEmployeeData());

        actionButtons.getChildren().addAll(addEmployeeBtn, editEmployeeBtn, deleteEmployeeBtn, exportBtn);
        return actionButtons;
    }

    private VBox createActionButton(FontIcon icon, String title, String tooltipText, String styleClass) {
        VBox button = new VBox(8);
        button.getStyleClass().addAll("action-button", styleClass);
        button.setAlignment(Pos.CENTER);
        button.setPadding(new Insets(15));
        button.setPrefSize(140, 100);

        icon.setIconSize(28);
        icon.setIconColor(Color.DARKCYAN);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("action-button-label");
        titleLabel.setWrapText(true);
        titleLabel.setAlignment(Pos.CENTER);

        // Tooltip
        Tooltip tooltip = new Tooltip(tooltipText);
        Tooltip.install(button, tooltip);

        button.getChildren().addAll(icon, titleLabel);
        return button;
    }

    private VBox createEmployeeTable() {
        VBox tableSection = new VBox(10);
        tableSection.getStyleClass().add("table-section");

        Label sectionTitle = new Label("Recent Employees");
        sectionTitle.getStyleClass().add("section-title");

        // Create table
        employeeTable = new TableView<>();
        employeeTable.getStyleClass().add("employee-table");
        employeeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create columns
        TableColumn<Employee, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("employeeID"));

        TableColumn<Employee, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Employee, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Employee, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        // FIXED: Use Timestamp instead of String for the date column
        TableColumn<Employee, java.sql.Timestamp> joinDateCol = new TableColumn<>("Join Date");
        joinDateCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        // FIXED: Proper cell factory for Timestamp
        joinDateCol.setCellFactory(column -> new TableCell<Employee, java.sql.Timestamp>() {
            private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

            @Override
            protected void updateItem(java.sql.Timestamp item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormat.format(item));
                }
            }
        });

        employeeTable.getColumns().addAll(idCol, usernameCol, emailCol, roleCol, joinDateCol);

        // Search and filter section
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search employees...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(250);

        ComboBox<String> roleFilter = new ComboBox<>();
        roleFilter.setPromptText("Filter by Role");
        roleFilter.getItems().addAll("All", "ADMIN", "MANAGER", "CASHIER", "STOCK_CLERK");

        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().addAll("button", "button-search");
        searchBtn.setOnAction(e -> filterEmployees(searchField.getText(), roleFilter.getValue()));

        Button refreshBtn = new Button("Refresh");
        refreshBtn.getStyleClass().addAll("button", "button-search");
        refreshBtn.setOnAction(e -> loadRealData());

        searchBox.getChildren().addAll(searchField, roleFilter, searchBtn, refreshBtn);

        tableSection.getChildren().addAll(sectionTitle, searchBox, employeeTable);
        return tableSection;
    }

    private HBox createChartsSection() {
        HBox chartsContainer = new HBox(20);
        chartsContainer.setAlignment(Pos.CENTER);

        // Role Distribution Pie Chart
        VBox pieChartContainer = createChartContainer("Role Distribution", createRolePieChart());

        // Employee Join Timeline Bar Chart
        VBox barChartContainer = createChartContainer("Monthly Joining", createJoinBarChart());

        chartsContainer.getChildren().addAll(pieChartContainer, barChartContainer);
        return chartsContainer;
    }

    private VBox createChartContainer(String title, javafx.scene.Node chart) {
        VBox container = new VBox(10);
        container.getStyleClass().add("chart-container");
        container.setPrefSize(400, 300);

        Label chartTitle = new Label(title);
        chartTitle.getStyleClass().add("chart-title");

        container.getChildren().addAll(chartTitle, chart);
        return container;
    }

    private PieChart createRolePieChart() {
        PieChart pieChart = new PieChart();
        pieChart.getStyleClass().add("pie-chart");

        Map<String, Integer> roleDistribution = authService.getRoleDistribution();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : roleDistribution.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        pieChart.setData(pieChartData);
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
        return pieChart;
    }

    private BarChart<String, Number> createJoinBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.getStyleClass().add("bar-chart");

        xAxis.setLabel("Months");
        yAxis.setLabel("Employees Joined");

        // Set tick label styles programmatically
        xAxis.setTickLabelRotation(0);
        xAxis.setTickLabelFont(javafx.scene.text.Font.font("Segoe UI", FontWeight.BOLD, 12));
        yAxis.setTickLabelFont(javafx.scene.text.Font.font("Segoe UI", FontWeight.BOLD, 12));

        Map<String, Integer> monthlyStats = authService.getMonthlyJoinStats();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Employee Joining");

        for (Map.Entry<String, Integer> entry : monthlyStats.entrySet()) {
            // Format month from "2024-01" to "Jan 2024"
            String[] parts = entry.getKey().split("-");
            String monthName = getMonthName(Integer.parseInt(parts[1]));
            String monthYear = monthName + " " + parts[0];
            series.getData().add(new XYChart.Data<>(monthYear, entry.getValue()));
        }

        barChart.getData().add(series);
        barChart.setLegendVisible(false);

        // Add some padding
        barChart.setCategoryGap(20);

        return barChart;
    }

    private String getMonthName(int month) {
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month - 1];
    }

    private void loadRealData() {
        List<Employee> employees = authService.getAllEmployees();
        employeeData = FXCollections.observableArrayList(employees);
        employeeTable.setItems(employeeData);
    }

    private void filterEmployees(String searchText, String roleFilter) {
        ObservableList<Employee> filteredList = FXCollections.observableArrayList();

        for (Employee employee : employeeData) {
            boolean matchesSearch = searchText.isEmpty() ||
                    employee.getUsername().toLowerCase().contains(searchText.toLowerCase()) ||
                    employee.getEmail().toLowerCase().contains(searchText.toLowerCase());

            boolean matchesRole = "All".equals(roleFilter) ||
                    employee.getRole().equals(Role.valueOf(roleFilter));

            if (matchesSearch && matchesRole) {
                filteredList.add(employee);
            }
        }

        employeeTable.setItems(filteredList);
    }

    private void exportEmployeeData() {
        // Implement export functionality
        System.out.println("Exporting employee data...");
        // You can implement CSV export logic here
    }

    public Parent getView() {
        return root;
    }
}