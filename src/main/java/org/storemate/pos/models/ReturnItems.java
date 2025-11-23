package org.storemate.pos.models;

import org.storemate.pos.models.enums.ReturnReason;

import java.math.BigDecimal;

public class ReturnItems {
    private int returnItemID;
    private String productID;
    private int returnID;
    private int quantity;
    private BigDecimal taxAmount;
    private BigDecimal unitPrice;
    private ReturnReason reason;
    private BigDecimal totalAmount;

    public ReturnItems() {
    }

    @Override
    public String toString() {
        return "ReturnItems{" +
                "returnItemID=" + returnItemID +
                ", productID='" + productID + '\'' +
                ", returnID=" + returnID +
                ", quantity=" + quantity +
                ", taxAmount=" + taxAmount +
                ", unitPrice=" + unitPrice +
                ", reason=" + reason +
                ", totalAmount=" + totalAmount +
                '}';
    }

    public ReturnItems(int returnItemID, String productID, int returnID, int quantity, BigDecimal taxAmount, BigDecimal unitPrice, ReturnReason reason, BigDecimal totalAmount) {
        this.returnItemID = returnItemID;
        this.productID = productID;
        this.returnID = returnID;
        this.quantity = quantity;
        this.taxAmount = taxAmount;
        this.unitPrice = unitPrice;
        this.reason = reason;
        this.totalAmount = totalAmount;
    }

    public int getReturnItemID() {
        return returnItemID;
    }

    public void setReturnItemID(int returnItemID) {
        this.returnItemID = returnItemID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getReturnID() {
        return returnID;
    }

    public void setReturnID(int returnID) {
        this.returnID = returnID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ReturnReason getReason() {
        return reason;
    }

    public void setReason(ReturnReason reason) {
        this.reason = reason;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
