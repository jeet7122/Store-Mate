package org.storemate.pos.dto;

import java.sql.Timestamp;

/**
 * Data Transfer Object (DTO) representing a sales report entry.
 * Contains information about the sale, product, employee, and payment details.
 */
public class SalesReportDTO {
    private int employeeID;

    /** ID of the sale */
    private int saleID;

    /** Name of the employee who made the sale */
    private String employeeName;

    /** ID of the product sold */
    private String productID;

    /** Name of the product sold */
    private String productName;

    /** Unit price of the product at the time of sale */
    private double unitPrice;

    /** Quantity of the product sold */
    private int quantity;

    /** Total amount of the sale (unitPrice * quantity) */
    private double saleTotal;

    /** Payment method used for the sale */
    private String paymentMethod;

    /** Status of the sale (e.g., completed, pending, returned) */
    private String status;

    /** Timestamp of when the sale occurred */
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

    public String getPaymentMethod() {
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

    /** Default constructor */
    public SalesReportDTO() {
    }
}
