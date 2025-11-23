package org.storemate.pos.models;

import org.storemate.pos.models.enums.PaymentMethod;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Returns {
    private int returnID;
    private int saleID;
    private int employeeID;
    private String productID;
    private PaymentMethod paymentMethod;
    private Timestamp returnDate;
    private int quantity;
    private BigDecimal totalAmount;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    @Override
    public String toString() {
        return "Returns{" +
                "returnID=" + returnID +
                ", saleID=" + saleID +
                ", employeeID=" + employeeID +
                ", productID='" + productID + '\'' +
                ", paymentMethod=" + paymentMethod +
                ", returnDate=" + returnDate +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                '}';
    }

    public Returns() {
    }


    public Returns(int returnID, int saleID, int employeeID, String productID, PaymentMethod paymentMethod, Timestamp returnDate, int quantity, BigDecimal totalAmount) {
        this.returnID = returnID;
        this.saleID = saleID;
        this.employeeID = employeeID;
        this.paymentMethod = paymentMethod;
        this.returnDate = returnDate;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.productID = productID;
    }

    public int getReturnID() {
        return returnID;
    }

    public void setReturnID(int returnID) {
        this.returnID = returnID;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Timestamp getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
