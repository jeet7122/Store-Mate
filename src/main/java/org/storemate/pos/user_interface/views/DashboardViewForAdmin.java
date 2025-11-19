package org.storemate.pos.user_interface.views;

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
import org.storemate.pos.routing.Router;
import org.storemate.pos.user_interface.components.Cards;
import org.storemate.pos.user_interface.components.Graphs;

public class DashboardViewForAdmin {
    private final VBox root;
    public DashboardViewForAdmin(){
        Graphs graphs = new Graphs();
        Cards cards = new Cards();
        root = new VBox(10);
        Label title = new Label("Admin Dashboard");
        root.getChildren().add(title);

        HBox topContainer = new HBox(120);
        topContainer.getStyleClass().add("top-container");

        //------PRODUCT BOX---------//
        HBox productsBox = new HBox(10);
        FontIcon productIcon = new FontIcon(MaterialDesignP.PACKAGE_VARIANT);
        productIcon.setIconColor(Color.DARKCYAN);
        productIcon.setIconSize(50);
        Label productsLabel = new Label("Products");
        productsBox.getChildren().addAll(productIcon, productsLabel);
        productsBox.setSpacing(30);
        productsBox.setOnMouseClicked(e -> Router.navigate("/products"));

        //-------CATEGORIES BOX---------//
        HBox categoriesBox = new HBox(10);
        FontIcon categoryIcon = new FontIcon(MaterialDesignL.LABEL_VARIANT_OUTLINE);
        categoryIcon.setIconColor(Color.DARKCYAN);
        categoryIcon.setIconSize(50);
        Label categoryLabel = new Label("Categories");
        categoriesBox.getChildren().addAll(categoryIcon, categoryLabel);
        categoriesBox.setSpacing(30);
        categoriesBox.setOnMouseClicked(e -> Router.navigate("/categories"));

        //--------EMPLOYEES BOX--------------//
        HBox employeeBox = new HBox(10);
        FontIcon employeeIcon = new FontIcon(MaterialDesignA.ACCOUNT_GROUP_OUTLINE);
        employeeIcon.setIconColor(Color.DARKCYAN);
        employeeIcon.setIconSize(50);
        Label employeeLabel = new Label("Staff");
        employeeBox.getChildren().addAll(employeeIcon, employeeLabel);
        employeeBox.setSpacing(30);
        employeeBox.setOnMouseClicked(e -> Router.navigate("/employees"));

        //---------SUPPLIERS BOX--------//
        HBox supplierBox = new HBox(10);
        FontIcon supplierIcon = new FontIcon(MaterialDesignT.TRUCK_OUTLINE);
        supplierIcon.setIconColor(Color.DARKCYAN);
        supplierIcon.setIconSize(50);
        Label supplierLabel = new Label("Suppliers");
        supplierBox.getChildren().addAll(supplierIcon, supplierLabel);
        supplierBox.setSpacing(30);
        supplierBox.setOnMouseClicked(e -> Router.navigate("/suppliers"));

        //Adding style classes
        productsBox.getStyleClass().add("dashboard-box");
        categoriesBox.getStyleClass().add("dashboard-box");
        employeeBox.getStyleClass().add("dashboard-box");
        supplierBox.getStyleClass().add("dashboard-box");


        topContainer.getChildren().addAll(productsBox, categoriesBox, employeeBox, supplierBox);
        root.getChildren().add(topContainer);
        HBox midGraphBox = new HBox(10);
        LineChart<String, Number> salesChart = graphs.createSalesLineChart();
        salesChart.setStyle("-fx-background-color: #d3d3d3");
        midGraphBox.getChildren().add(salesChart);
        root.getChildren().add(midGraphBox);
        topContainer.setAlignment(Pos.TOP_CENTER);

        //pie chart
        HBox ringsContainer = new HBox(20);
        Label employeesMetricsLabel = new Label("Weekly Working Hours");
        employeesMetricsLabel.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: semi-bold;" +
                        "-fx-text-fill: #111;" // White text for dark POS theme
        );
        employeesMetricsLabel.setAlignment(Pos.CENTER);
        ringsContainer.getChildren().addAll(
                graphs.createEmployeeHoursRingChart("Alice", 30, 40),
                graphs.createEmployeeHoursRingChart("Bob", 35, 40)
        );
        VBox ringGraph = new VBox();
        ringGraph.setAlignment(Pos.CENTER);
        ringGraph.getChildren().addAll(employeesMetricsLabel, ringsContainer);

        midGraphBox.getChildren().add(ringGraph);
        ringGraph.setStyle("-fx-background-color: #d3d3d3; -fx-padding: 10");
        ringsContainer.setAlignment(Pos.CENTER);

        Parent cat_container = cards.createCategoryList();
        cat_container.setStyle("-fx-pref-height: 300; -fx-pref-width: 200");
        HBox lowerContainer = new HBox(10);
        VBox cat_cover = new VBox(10);
        cat_cover.setStyle("-fx-background-color: #d3d3d3");
        Label cat_label = new Label("Categories");
        cat_label.setStyle("-fx-font-size: 18px; -fx-font-family: 'Segoe UI Semibold';");
        cat_cover.getChildren().addAll(cat_label, cat_container);
        root.getChildren().add(lowerContainer);

        //Area Chart
        AreaChart<String, Number> purchaseChart = graphs.createPurchaseFillChart();
        purchaseChart.setStyle("-fx-background-color: #d3d3d3");

        //Transactions Chart
        VBox transContainer = new VBox(60);
        transContainer.setStyle("-fx-padding: 10; -fx-background-color: #d3d3d3; -fx-pref-width: 650");
        Label transTitle = new Label("Weekly Transactions");
        transTitle.setStyle("-fx-font-size: 18px; -fx-font-family: 'Segoe UI Semibold';");

        transContainer.getChildren().addAll(transTitle, graphs.createTransactionRings());
        lowerContainer.getChildren().addAll(purchaseChart, cat_cover, transContainer);


    }

    public Parent getView() {
        return root;
    }
}
