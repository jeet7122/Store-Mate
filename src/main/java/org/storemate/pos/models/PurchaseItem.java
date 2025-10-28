package org.storemate.pos.models;

import org.storemate.pos.models.enums.TaxCategory;

import java.math.BigDecimal;

public class PurchaseItem {
    private Integer purchaseID;
    private String productID;
    private Integer quantity;
    private BigDecimal cost_price;
    private TaxCategory  taxCategory;
    private BigDecimal tax_amount;

    public PurchaseItem(Integer purchaseID, String productID, Integer quantity, BigDecimal cost_price, TaxCategory taxCategory, BigDecimal tax_amount) {
        this.purchaseID = purchaseID;
        this.productID = productID;
        this.quantity = quantity;
        this.cost_price = cost_price;
        this.taxCategory = taxCategory;
        this.tax_amount = tax_amount;
    }

    @Override
    public String toString() {
        return "PurchaseItem{" +
                "purchaseID=" + purchaseID +
                ", productID='" + productID + '\'' +
                ", quantity=" + quantity +
                ", cost_price=" + cost_price +
                ", taxCategory=" + taxCategory +
                ", tax_amount=" + tax_amount +
                '}';
    }

    public PurchaseItem() {
    }

    public Integer getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(Integer purchaseID) {
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

    public BigDecimal getCost_price() {
        return cost_price;
    }

    public void setCost_price(BigDecimal cost_price) {
        this.cost_price = cost_price;
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
