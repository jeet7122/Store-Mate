package org.storemate.pos.user_interface.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.*;

import org.storemate.pos.dao.CategoryDAO;
import org.storemate.pos.dao.PurchaseDAO;
import org.storemate.pos.dao.ShiftDAO;

import org.storemate.pos.models.Category;
import org.storemate.pos.routing.Router;
import org.storemate.pos.user_interface.components.Cards;
import org.storemate.pos.user_interface.components.Graphs;

import java.util.List;
import java.util.Map;

public class DashboardViewForAdmin {

    private final VBox root;

    public DashboardViewForAdmin() {

        // --------------- INITIAL SETUP ---------------
        Graphs graphs = new Graphs();
        Cards cards = new Cards();

        root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Admin Dashboard");
        title.getStyleClass().add("label-subtitle");
        root.getChildren().add(title);

        // ========================================================================
        // --------------- TOP NAVIGATION BOXES
        // ========================================================================
        HBox topContainer = new HBox(120);
        topContainer.getStyleClass().add("top-container");
        topContainer.setAlignment(Pos.TOP_CENTER);

        // PRODUCTS
        HBox productsBox = new HBox(10);
        FontIcon productIcon = new FontIcon(MaterialDesignP.PACKAGE_VARIANT);
        productIcon.setIconColor(Color.DARKCYAN);
        productIcon.setIconSize(50);

        Label productsLabel = new Label("Products");
        productsBox.getChildren().addAll(productIcon, productsLabel);
        productsBox.setSpacing(30);
        productsBox.getStyleClass().add("dashboard-box");
        productsBox.setOnMouseClicked(e -> Router.navigate("/products"));

        // CATEGORIES
        HBox categoriesBox = new HBox(10);
        FontIcon categoryIcon = new FontIcon(MaterialDesignL.LABEL_VARIANT_OUTLINE);
        categoryIcon.setIconColor(Color.DARKCYAN);
        categoryIcon.setIconSize(50);

        Label categoryLabel = new Label("Categories");
        categoriesBox.getChildren().addAll(categoryIcon, categoryLabel);
        categoriesBox.setSpacing(30);
        categoriesBox.getStyleClass().add("dashboard-box");
        categoriesBox.setOnMouseClicked(e -> Router.navigate("/categories"));

        // EMPLOYEES
        HBox employeeBox = new HBox(10);
        FontIcon employeeIcon = new FontIcon(MaterialDesignA.ACCOUNT_GROUP_OUTLINE);
        employeeIcon.setIconColor(Color.DARKCYAN);
        employeeIcon.setIconSize(50);

        Label employeeLabel = new Label("Staff");
        employeeBox.getChildren().addAll(employeeIcon, employeeLabel);
        employeeBox.setSpacing(30);
        employeeBox.getStyleClass().add("dashboard-box");
        employeeBox.setOnMouseClicked(e -> Router.navigate("/employees"));

        // SUPPLIERS
        HBox supplierBox = new HBox(10);
        FontIcon supplierIcon = new FontIcon(MaterialDesignT.TRUCK_OUTLINE);
        supplierIcon.setIconColor(Color.DARKCYAN);
        supplierIcon.setIconSize(50);

        Label supplierLabel = new Label("Suppliers");
        supplierBox.getChildren().addAll(supplierIcon, supplierLabel);
        supplierBox.setSpacing(30);
        supplierBox.getStyleClass().add("dashboard-box");
        supplierBox.setOnMouseClicked(e -> Router.navigate("/suppliers"));

        // ADD TOP BOXES
        topContainer.getChildren().addAll(productsBox, categoriesBox, employeeBox, supplierBox);
        root.getChildren().add(topContainer);

        // ========================================================================
        // --------------- MID-LEVEL: SALES CHART + EMPLOYEE HOURS RINGS
        // ========================================================================
        HBox midGraphBox = new HBox(10);

        // SALES LINE CHART
        LineChart<String, Number> salesChart = graphs.createSalesLineChart();
        salesChart.setStyle("-fx-background-color: #d3d3d3;");
        midGraphBox.getChildren().add(salesChart);

        // EMPLOYEE WEEKLY HOURS
        ShiftDAO shiftDAO = new ShiftDAO();
        Map<String, Integer> weeklyHoursMap = shiftDAO.getWeeklyHours();

        Label employeesMetricsLabel = new Label("Weekly Working Hours");
        employeesMetricsLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #111;");

        HBox ringsContainer = new HBox(20);
        ringsContainer.setAlignment(Pos.CENTER);

        weeklyHoursMap.forEach((emp, hours) ->
                ringsContainer.getChildren().add(
                        graphs.createEmployeeHoursRingChart(emp, hours, 40)
                )
        );

        VBox ringGraphBox = new VBox(10);
        ringGraphBox.setAlignment(Pos.CENTER);
        ringGraphBox.setPrefWidth(650);
        ringGraphBox.setStyle("-fx-background-color: #d3d3d3; -fx-padding: 10;");
        ringGraphBox.getChildren().addAll(employeesMetricsLabel, ringsContainer);

        midGraphBox.getChildren().add(ringGraphBox);
        root.getChildren().add(midGraphBox);

        // ========================================================================
        // --------------- LOWER CONTAINER: PURCHASE CHART + CATEGORY LIST + TRANSACTION RINGS
        // ========================================================================
        HBox lowerContainer = new HBox(10);

        // PURCHASE DATA AREA CHART
        PurchaseDAO purchaseDAO = new PurchaseDAO();
        Map<String, Double> purchaseMap = purchaseDAO.getPurchasesLast30Days();
        AreaChart<String, Number> purchaseChart = graphs.createPurchaseFillChart(purchaseMap);

        // PURCHASE CHART COVER BOX ✔ (new)
        VBox purchaseCover = new VBox(10);
        purchaseCover.setAlignment(Pos.CENTER);
        purchaseCover.setStyle("-fx-background-color: #d3d3d3; -fx-padding: 10;");

        Label purchaseLabel = new Label("Purchases - Last 30 Days");
        purchaseLabel.setStyle("-fx-font-size: 18px; -fx-font-family: 'Segoe UI';");

        purchaseCover.getChildren().addAll(purchaseLabel, purchaseChart);
        purchaseCover.setPrefWidth(450);

        // CATEGORY LIST
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> categoryList = categoryDAO.getAllCategories();

        Parent catContainer = cards.createCategoryList(categoryList);
        catContainer.setStyle("-fx-pref-height: 300; -fx-pref-width: 200;");

        VBox catCover = new VBox(10);
        catCover.setAlignment(Pos.CENTER);
        catCover.setStyle("-fx-background-color: #d3d3d3; -fx-padding: 10;");
        Label catLabel = new Label("Categories");
        catLabel.setStyle("-fx-font-size: 18px; -fx-font-family: 'Segoe UI';");
        catCover.getChildren().addAll(catLabel, catContainer);

        // TRANSACTIONS RINGS
        VBox transContainer = new VBox(60);
        transContainer.setAlignment(Pos.CENTER);
        transContainer.setStyle("-fx-padding: 10; -fx-background-color: #d3d3d3; -fx-pref-width: 650;");

        Label transTitle = new Label("Weekly Transactions");
        transTitle.setStyle("-fx-font-size: 18px; -fx-font-family: 'Segoe UI';");

        transContainer.getChildren().addAll(transTitle, graphs.createTransactionRings());

        // ADD ALL LOWER COMPONENTS
        lowerContainer.getChildren().addAll(purchaseCover, catCover, transContainer);
        root.getChildren().add(lowerContainer);
    }

    public Parent getView() {
        return root;
    }
}
