package org.storemate.pos.models;

import org.storemate.pos.models.enums.PaymentMethod;
import org.storemate.pos.models.enums.SaleStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a sale transaction in the POS system.
 * <p>
 * This class holds information about a specific sale, including the employee
 * who processed it, payment method, subtotal, tax, total amount, timestamp,
 * and the sale's status.
 * @author Jeet
 */
public class Sale {
    private int saleID;
    private int employeeID;
    private PaymentMethod paymentMethod;
    private BigDecimal subTotal;
    private BigDecimal tax_amount;
    private BigDecimal total_amount;
    private Timestamp sale_timestamp;
    private SaleStatus status;

    public Sale() {
    }

    public Sale(BigDecimal total_amount, BigDecimal tax_amount, BigDecimal subTotal, PaymentMethod paymentMethod, int employeeID, int saleID, SaleStatus status) {
        this.total_amount = total_amount;
        this.tax_amount = tax_amount;
        this.subTotal = subTotal;
        this.paymentMethod = paymentMethod;
        this.employeeID = employeeID;
        this.saleID = saleID;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "saleID=" + saleID +
                ", employeeID=" + employeeID +
                ", paymentMethod=" + paymentMethod +
                ", subTotal=" + subTotal +
                ", tax_amount=" + tax_amount +
                ", total_amount=" + total_amount +
                ", sale_timestamp=" + sale_timestamp +
                ", status=" + status +
                '}';
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

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
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

    public Timestamp getSale_timestamp() {
        return sale_timestamp;
    }

    public void setSale_timestamp(Timestamp sale_timestamp) {
        this.sale_timestamp = sale_timestamp;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }
}
