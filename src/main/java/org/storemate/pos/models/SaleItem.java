package org.storemate.pos.models;

import org.storemate.pos.models.enums.TaxCategory;

import java.math.BigDecimal;

/**
 * Represents an individual item within a sale transaction in the POS system.
 * <p>
 * Each SaleItem contains information about the product sold, the quantity,
 * unit price, applicable tax category, tax amount, and the total amount
 * for that item.
 * @author Jeet
 */
public class SaleItem {
    private int saleID;
    private String productID;
    private int quantity;
    private BigDecimal price;
    private TaxCategory taxCategory;
    private BigDecimal tax_amount;
    private BigDecimal total_amount;

    public SaleItem(int saleID, String productID, Integer quantity, BigDecimal price, TaxCategory taxCategory, BigDecimal tax_amount, BigDecimal total_amount) {
        this.saleID = saleID;
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
        this.taxCategory = taxCategory;
        this.tax_amount = tax_amount;
        this.total_amount = total_amount;
    }

    public SaleItem() {
    }

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
}
