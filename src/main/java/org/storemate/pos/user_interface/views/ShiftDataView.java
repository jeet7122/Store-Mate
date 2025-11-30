package org.storemate.pos.user_interface.views;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.ShiftDAO;
import org.storemate.pos.models.ShiftSummary;

import java.util.List;

public class ShiftDataView {

    private final BorderPane root;

    public ShiftDataView() {
        root = new BorderPane();
        root.setPadding(new Insets(15));

        ShiftDAO dao = new ShiftDAO();
        List<ShiftSummary> data = dao.getShiftSummary();

        // ---------- TABLE ----------
        TableView<ShiftSummary> table = new TableView<>();

        TableColumn<ShiftSummary, Integer> colId = new TableColumn<>("Employee ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));

        TableColumn<ShiftSummary, String> colName = new TableColumn<>("Username");
        colName.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<ShiftSummary, Integer> colWeekly = new TableColumn<>("Weekly Hours");
        colWeekly.setCellValueFactory(new PropertyValueFactory<>("weeklyHours"));

        TableColumn<ShiftSummary, Integer> colTotal = new TableColumn<>("Total Hours");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalHours"));

        table.getColumns().addAll(colId, colName, colWeekly, colTotal);
        table.getItems().addAll(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        VBox tableBox = new VBox(10, table);
        tableBox.setPadding(new Insets(10));

        // ---------- BAR CHART ----------
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Employees");

        NumberAxis yAxis = new NumberAxis(0, 40, 5);
        yAxis.setLabel("Weekly Worked Hours");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Employee Weekly Working Hours");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Hours Worked");

        for (ShiftSummary s : data) {
            series.getData().add(new XYChart.Data<>(s.getUsername(), s.getWeeklyHours()));
        }

        barChart.getData().add(series);
        barChart.setCategoryGap(30);
        barChart.setBarGap(5);

        root.setTop(tableBox);
        root.setCenter(barChart);
    }

    public Parent getView() {
        return root;
    }
}
