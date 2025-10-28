package org.storemate.pos.models;

import org.storemate.pos.models.enums.PaymentMethod;
import org.storemate.pos.models.enums.SaleStatus;
import org.storemate.pos.models.enums.TaxCategory;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Sale {
    private Integer saleID;
    private Integer employeeID;
    private PaymentMethod paymentMethod;
    private BigDecimal subTotal;
    private TaxCategory taxCategory;
    private BigDecimal tax_amount;
    private BigDecimal total_amount;
    private Timestamp sale_timestamp;
    private SaleStatus status;

    public Sale() {
    }

    public Sale(BigDecimal total_amount, BigDecimal tax_amount, TaxCategory taxCategory, BigDecimal subTotal, PaymentMethod paymentMethod, Integer employeeID, Integer saleID, SaleStatus status) {
        this.total_amount = total_amount;
        this.tax_amount = tax_amount;
        this.taxCategory = taxCategory;
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
                ", taxCategory=" + taxCategory +
                ", tax_amount=" + tax_amount +
                ", total_amount=" + total_amount +
                ", sale_timestamp=" + sale_timestamp +
                ", status=" + status +
                '}';
    }

    public Integer getSaleID() {
        return saleID;
    }

    public void setSaleID(Integer saleID) {
        this.saleID = saleID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
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

    public TaxCategory getTaxCategory() {
        return taxCategory;
    }

    public void setTaxCategory(TaxCategory taxCategory) {
        this.taxCategory = taxCategory;
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
