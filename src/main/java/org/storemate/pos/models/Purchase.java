package org.storemate.pos.models;

import org.storemate.pos.models.enums.PurchaseStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Purchase {
    private int purchaseID;
    private int supplierID;
    private BigDecimal tax_amount;
    private BigDecimal total_amount;
    private LocalDate purchaseDate;
    private PurchaseStatus status;

    public Purchase(BigDecimal total_amount, BigDecimal tax_amount, int supplierID, int purchaseID, PurchaseStatus status) {
        this.total_amount = total_amount;
        this.tax_amount = tax_amount;
        this.supplierID = supplierID;
        this.purchaseID = purchaseID;
        this.status = status;
    }

    public Purchase() {
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "purchaseID=" + purchaseID +
                ", supplierID=" + supplierID +
                ", tax_amount=" + tax_amount +
                ", total_amount=" + total_amount +
                ", purchaseDate=" + purchaseDate +
                ", status=" + status +
                '}';
    }

    public int getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(int purchaseID) {
        this.purchaseID = purchaseID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public BigDecimal getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(BigDecimal tax_amount) {
        this.tax_amount = tax_amount;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public PurchaseStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseStatus status) {
        this.status = status;
    }
}
