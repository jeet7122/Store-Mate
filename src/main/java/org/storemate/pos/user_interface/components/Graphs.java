package org.storemate.pos.user_interface.components;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.storemate.pos.dao.PurchaseDAO;
import org.storemate.pos.dao.SaleDAO;

import java.util.LinkedHashMap;
import java.util.Map;


public class Graphs {
    private final SaleDAO saleDAO;
    private final PurchaseDAO purchaseDAO;

    public Graphs(){
        saleDAO = new SaleDAO();
        purchaseDAO = new PurchaseDAO();
    }

    public LineChart<String, Number> createSalesLineChart(){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Day");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Sales {$}");
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("SALES REPORT - LAST 30 DAYS");


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sales");

       Map<String, Double> salesData = saleDAO.getSalesLast30Days();
       for (Map.Entry<String, Double> entry: salesData.entrySet()){
           series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
       }

        lineChart.getData().add(series);
        lineChart.setPrefHeight(400);
        lineChart.setPrefWidth(700);

        return lineChart;
    }

    public StackPane createEmployeeHoursRingChart(String username, double workedHours, double expectedHours) {

        PieChart.Data worked = new PieChart.Data("Worked", workedHours);
        PieChart.Data remaining = new PieChart.Data("Remaining", expectedHours - workedHours);

        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(worked, remaining);

        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(false);
        pieChart.setStartAngle(90);
        pieChart.setMinSize(220, 220);
        pieChart.setMaxSize(220, 220);

        // ---- FIX: Apply colors AFTER the pie chart is attached to scene ----
        Platform.runLater(() -> {
            if (worked.getNode() != null) {
                worked.getNode().setStyle("-fx-pie-color: #00BCD4;");
            }
            if (remaining.getNode() != null) {
                remaining.getNode().setStyle("-fx-pie-color: #d0d0d0;");
            }
        });

        // ---- Donut hole ----
        Circle innerCircle = new Circle(80);
        innerCircle.setFill(Color.web("#d3d3d3"));

        Label nameLabel = new Label(username);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        nameLabel.setTextFill(Color.BLACK);

        Label hoursLabel = new Label((int) workedHours + "h / " + (int) expectedHours + "h");
        hoursLabel.setStyle("-fx-font-size: 12px;");
        hoursLabel.setTextFill(Color.BLACK);

        VBox textBox = new VBox(2, nameLabel, hoursLabel);
        textBox.setAlignment(Pos.CENTER);

        StackPane stack = new StackPane(pieChart, innerCircle, textBox);
        stack.setPrefSize(220, 220);

        return stack;
    }

    public AreaChart<String, Number> createPurchaseFillChart(Map<String, Double> data) {
        NumberAxis y = new NumberAxis();
        CategoryAxis x = new CategoryAxis();
        x.setLabel("Month");
        y.setLabel("Purchases {$}");

        AreaChart<String, Number> chart = new AreaChart<>(x, y);
        chart.setTitle("Monthly Purchase Overview");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Purchases");

        data.forEach((day, amount) -> {
            series.getData().add(new XYChart.Data<>(day, amount));
        });

        chart.getData().add(series);
        chart.setAnimated(true);
        chart.setLegendVisible(false);
        chart.setPrefHeight(350);

        return chart;
    }

    public StackPane createRing(String label, double value, double maxValue, Color fillColor) {
        double size = 140;

        Circle bgCircle = new Circle(size / 2);
        bgCircle.setStroke(Color.rgb(220, 220, 220));
        bgCircle.setFill(Color.TRANSPARENT);
        bgCircle.setStrokeWidth(18);

        Circle ring = new Circle(size / 2);
        ring.setFill(Color.TRANSPARENT);
        ring.setStroke(fillColor);
        ring.setStrokeWidth(18);

        double progress = value / maxValue;
        ring.getStrokeDashArray().addAll(360.0);
        ring.setStrokeDashOffset(360 * (1 - progress));
        ring.setRotate(-90);

        Label centerLabel = new Label(label + "\n$" + (int) value);
        centerLabel.setStyle("""
                -fx-font-family: 'Segoe UI Semibold';
                -fx-font-size: 15px;
                -fx-alignment: center;
                """);

        StackPane container = new StackPane(bgCircle, ring, centerLabel);
        container.setMinSize(size, size);
        container.setMaxSize(size, size);

        return container;
    }

    public HBox createTransactionRings() {
        Map<String, Double> transactions = saleDAO.getWeeklyTransactions();
        double maxValue = transactions.values().stream().max(Double::compare).orElse(1.0);

        HBox rings = new HBox(25);
        rings.setAlignment(Pos.CENTER);

        Color[] colors = {Color.DODGERBLUE, Color.MEDIUMSEAGREEN, Color.ORANGE};
        int index = 0;

        for (Map.Entry<String, Double> entry : transactions.entrySet()) {
            rings.getChildren().add(createRing(entry.getKey(), entry.getValue(), maxValue, colors[index]));
            index++;
        }

        return rings;
    }
}
