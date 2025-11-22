package org.storemate.pos.dto;

import javafx.beans.value.ObservableValue;

import java.sql.Timestamp;
import java.time.LocalDate;

public class SalesReportDTO {
    private int employeeID;
    private int saleID;
    private String employeeName;
    private String productID;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double saleTotal;
    private String paymentMethod;
    private String status;
    private Timestamp saleDateAndTime;

    public ObservableValue<LocalDate> getSaleDateAndTime() {
        return saleDateAndTime;
    }

    public void setSaleDateAndTime(Timestamp saleDateAndTime) {
        this.saleDateAndTime = saleDateAndTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ObservableValue<String> getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ObservableValue<Double> getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(double saleTotal) {
        this.saleTotal = saleTotal;
    }

    public ObservableValue<Integer> getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ObservableValue<Double> getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ObservableValue<String> getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ObservableValue<String> getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public ObservableValue<String> getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public ObservableValue<Integer> getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public ObservableValue<Integer> getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public SalesReportDTO() {
    }
}
