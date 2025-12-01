package org.storemate.pos.models;

import org.storemate.pos.models.enums.TaxCategory;

import java.math.BigDecimal;

/**
 * Represents an individual item within a purchase transaction.
 * <p>
 * Contains details such as the associated purchase ID, product ID, quantity, unit price,
 * tax category, tax amount, total amount, and total amount including tax.
 * @author Jeet
 */
public class PurchaseItem {
    private int purchaseID;
    private String productID;
    private Integer quantity;
    private BigDecimal unit_price;
    private TaxCategory taxCategory;
    private BigDecimal tax_amount;
    private BigDecimal total_amount;
    private BigDecimal total_amount_with_tax;


    public PurchaseItem(int purchaseID, String productID, Integer quantity, BigDecimal unit_price, TaxCategory taxCategory, BigDecimal tax_amount, BigDecimal total_amount_with_tax, BigDecimal total_amount) {
        this.purchaseID = purchaseID;
        this.productID = productID;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.taxCategory = taxCategory;
        this.tax_amount = tax_amount;
        this.total_amount_with_tax = total_amount_with_tax;
        this.total_amount = total_amount;
    }

    @Override
    public String toString() {
        return "PurchaseItem{" +
                "purchaseID=" + purchaseID +
                ", productID='" + productID + '\'' +
                ", quantity=" + quantity +
                ", unit_price=" + unit_price +
                ", taxCategory=" + taxCategory +
                ", tax_amount=" + tax_amount +
                '}';
    }

    public PurchaseItem() {
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }
    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    public BigDecimal getTotal_amount_with_tax() {
        return total_amount_with_tax;
    }
    public void setTotal_amount_with_tax(BigDecimal total_amount_with_tax) {
        this.total_amount_with_tax = total_amount_with_tax;
    }

    public int getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(int purchaseID) {
        this.purchaseID = purchaseID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getunit_price() {
        return unit_price;
    }

    public void setunit_price(BigDecimal unit_price) {
        this.unit_price = unit_price;
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
}
