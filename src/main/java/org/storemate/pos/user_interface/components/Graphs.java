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
import org.storemate.pos.dao.SaleDAO;

import java.util.Map;


/**
 * Provides utility methods to create various types of charts and graphs
 * for the POS system UI, including sales line charts, purchase area charts,
 * and ring/donut charts for visualizing employee hours and transactions.
 * @author Khushi, Jeet
 */
public class Graphs {
    /**
     * DAO to fetch sales and transaction data from the database.
     */
    private final SaleDAO saleDAO;

    /**
     * Constructs a Graphs instance and initializes the SaleDAO.
     */
    public Graphs(){
        saleDAO = new SaleDAO();
    }

    /**
     * Creates a line chart showing sales for the last 30 days.
     *
     * @return a LineChart with daily sales data.
     */
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

    /**
     * Creates a ring (donut) chart visualizing an employee's worked hours
     * versus expected hours, with the employee's name and hours displayed in the center.
     *
     * @param username      the employee's username
     * @param workedHours   hours the employee has worked
     * @param expectedHours total expected hours
     * @return a StackPane containing the donut chart and labels
     */
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

    /**
     * Creates an area chart showing monthly purchase data.
     *
     * @param data a Map of month names to purchase amounts
     * @return an AreaChart visualizing purchases over months
     */
    public AreaChart<String, Number> createPurchaseFillChart(Map<String, Double> data) {
        NumberAxis y = new NumberAxis();
        CategoryAxis x = new CategoryAxis();
        x.setLabel("Month");
        y.setLabel("Purchases {$}");

        AreaChart<String, Number> chart = new AreaChart<>(x, y);
        chart.setTitle("Monthly Purchase Overview");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Purchases");

        data.forEach((day, amount) -> series.getData().add(new XYChart.Data<>(day, amount)));

        chart.getData().add(series);
        chart.setAnimated(true);
        chart.setLegendVisible(false);
        chart.setPrefHeight(350);

        return chart;
    }


    /**
     * Creates a single ring chart for visualizing a value relative to a maximum value.
     *
     * @param label     label to display in the center
     * @param value     current value
     * @param maxValue  maximum value
     * @param fillColor the color of the ring
     * @return a StackPane containing the ring and label
     */
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

    /**
     * Creates a horizontal box containing multiple transaction ring charts
     * for visualizing weekly transaction amounts.
     *
     * @return an HBox with multiple rings representing weekly transactions
     */
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
