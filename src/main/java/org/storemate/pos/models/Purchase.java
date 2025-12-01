package org.storemate.pos.models;

import org.storemate.pos.models.enums.PaymentMethod;
import org.storemate.pos.models.enums.PurchaseStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;


/**
 * Represents a purchase transaction made from a supplier.
 * <p>
 * Contains details such as the purchase ID, supplier ID, tax amount, total amount,
 * purchase date, payment method, and current status of the purchase.
 * @author Jeet, Khushi
 */
public class Purchase {
    private int purchaseID;
    private int supplierID;
    private BigDecimal tax_amount;
    private BigDecimal total_amount;
    private Timestamp purchaseDate;
    private PurchaseStatus purchaseStatus;

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    private PaymentMethod paymentMethod;

    public Purchase(BigDecimal total_amount, BigDecimal tax_amount, int supplierID, int purchaseID, PurchaseStatus status) {
        this.total_amount = total_amount;
        this.tax_amount = tax_amount;
        this.supplierID = supplierID;
        this.purchaseID = purchaseID;
        this.purchaseStatus = status;
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
                ", status=" + purchaseStatus +
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

    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public PurchaseStatus getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(PurchaseStatus status) {
        this.purchaseStatus = status;
    }
}
