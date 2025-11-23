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

    public Timestamp getSaleDateAndTime() {
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

        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

        public double getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(double saleTotal) {
        this.saleTotal = saleTotal;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public SalesReportDTO() {
    }
}
