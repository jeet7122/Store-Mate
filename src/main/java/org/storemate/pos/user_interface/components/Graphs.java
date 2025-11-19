package org.storemate.pos.user_interface.components;

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

import java.util.LinkedHashMap;
import java.util.Map;


public class Graphs {
    public LineChart<String, Number> createSalesLineChart(){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Sales {$}");
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Monthly Sales Report");


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sales");

        //Dummy Data
        series.getData().add(new XYChart.Data<>("Jan", 5000));
        series.getData().add(new XYChart.Data<>("Feb", 7000));
        series.getData().add(new XYChart.Data<>("Mar", 6000));
        series.getData().add(new XYChart.Data<>("Apr", 8000));
        series.getData().add(new XYChart.Data<>("May", 9000));
        series.getData().add(new XYChart.Data<>("Jun", 7500));

        lineChart.getData().add(series);
        lineChart.setPrefHeight(400);
        lineChart.setPrefWidth(700);

        return lineChart;
    }

    public StackPane createEmployeeHoursRingChart(String username, double workedHours, double expectedHours) {
        // PieChart slices
        PieChart.Data worked = new PieChart.Data("Worked", workedHours);
        PieChart.Data remaining = new PieChart.Data("Remaining", expectedHours - workedHours);

        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(worked, remaining);

        // Hide labels and legend
        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(false);
        pieChart.setStartAngle(90);

        // Style slices
        worked.getNode().setStyle("-fx-pie-color: #00BCD4;"); // Bright cyan for worked
        remaining.getNode().setStyle("-fx-pie-color: #e0e0e0;"); // Dim gray for remaining

        // Inner circle to create the ring
        Circle innerCircle = new Circle();
        innerCircle.setRadius(100);
        innerCircle.setFill(Color.web("#d3d3d3")); // Background color of dashboard

        // Labels for username and hours
        Label nameLabel = new Label(username);
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label hoursLabel = new Label((int)workedHours + "h / " + (int)expectedHours + "h");
        hoursLabel.setTextFill(Color.WHITE);
        hoursLabel.setStyle("-fx-font-size: 12px;");

        VBox labelBox = new VBox(2, nameLabel, hoursLabel);
        labelBox.setAlignment(Pos.CENTER);

        // Stack everything together
        StackPane ringChart = new StackPane();
        ringChart.getChildren().addAll(pieChart, innerCircle, labelBox);
        ringChart.setPrefSize(300, 300);

        return ringChart;
    }

    public AreaChart<String, Number> createPurchaseFillChart() {

        // X-axis (Months)
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        // Y-axis (Purchase Amount in Dollars)
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Purchases ($)");

        // Create AreaChart
        AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setTitle("Monthly Purchases Overview");

        // Data series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Purchases");

        // Dummy data for now (replace with DB data later)
        series.getData().add(new XYChart.Data<>("Jan", 12000));
        series.getData().add(new XYChart.Data<>("Feb", 9000));
        series.getData().add(new XYChart.Data<>("Mar", 15000));
        series.getData().add(new XYChart.Data<>("Apr", 13000));
        series.getData().add(new XYChart.Data<>("May", 17000));
        series.getData().add(new XYChart.Data<>("Jun", 14000));

        areaChart.getData().add(series);

        // Make it look cleaner
        areaChart.setAnimated(true);
        areaChart.setLegendVisible(false);
        areaChart.setPrefHeight(350);

        return areaChart;
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
    public Map<String, Double> getWeeklyTransactions() {
        // Later this will come from your database
        Map<String, Double> data = new LinkedHashMap<>();
        data.put("Cash", 1200.0);
        data.put("Credit", 3100.0);
        data.put("Debit", 900.0);

        return data;
    }

    public HBox createTransactionRings() {

        Map<String, Double> data = getWeeklyTransactions();
        double maxValue = data.values().stream().max(Double::compare).orElse(1.0);

        HBox rings = new HBox(25);
        rings.setAlignment(Pos.CENTER);

        Color[] colors = { Color.DODGERBLUE, Color.MEDIUMSEAGREEN, Color.ORANGE };
        int index = 0;

        for (Map.Entry<String, Double> entry : data.entrySet()) {
            rings.getChildren().add(
                    createRing(entry.getKey(), entry.getValue(), maxValue, colors[index])
            );
            index++;
        }

        return rings;
    }

}
