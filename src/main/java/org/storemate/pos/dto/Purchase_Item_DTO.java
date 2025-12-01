package org.storemate.pos.dto;

import org.storemate.pos.models.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Data Transfer Object (DTO) representing a purchase item in a purchase order.
 * Contains product details, quantity, cost price, and calculated totals including tax.
 * @author Jeet
 */
public class Purchase_Item_DTO {
    private Product product;
    private int quantity;
    private BigDecimal costPrice;
    private BigDecimal subTotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;

    @Override
    public String toString() {
        return "Purchase_Item_DTO{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", costPrice=" + costPrice +
                ", subTotal=" + subTotal +
                ", taxAmount=" + taxAmount +
                ", totalAmount=" + totalAmount +
                '}';
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Constructs a Purchase_Item_DTO with the specified product, quantity, and cost price.
     * If costPrice is null, the product's default price will be used.
     *
     * @param product   The product being purchased.
     * @param quantity  The quantity of the product.
     * @param costPrice The cost price per unit (nullable).
     */
    public Purchase_Item_DTO(Product product, int quantity, BigDecimal costPrice) {
        this.product = product;
        this.quantity = quantity;
        this.costPrice = costPrice == null ? product.getProduct_price() : costPrice;
        recalc();
    }

    /**
     * Recalculates the subtotal, tax amount, and total amount based on the current quantity and cost price.
     */
    public void recalc(){
        BigDecimal qty = BigDecimal.valueOf(quantity);
        BigDecimal taxRate = BigDecimal.valueOf(product.getTaxCategory().getTax_value());
        this.subTotal = costPrice.multiply(qty).setScale(2, RoundingMode.HALF_UP);
        this.taxAmount = subTotal.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
        this.totalAmount = subTotal.add(taxAmount).setScale(2, RoundingMode.HALF_UP);
    }
}
