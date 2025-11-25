package org.storemate.pos.dto;

import org.storemate.pos.models.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    public Purchase_Item_DTO(Product product, int quantity, BigDecimal costPrice) {
        this.product = product;
        this.quantity = quantity;
        this.costPrice = costPrice == null ? product.getProduct_price() : costPrice;
        recalc();
    }

    public void recalc(){
        BigDecimal qty = BigDecimal.valueOf(quantity);
        BigDecimal taxRate = BigDecimal.valueOf(product.getTaxCategory().getTax_value());
        this.subTotal = costPrice.multiply(qty).setScale(2, RoundingMode.HALF_UP);
        this.taxAmount = subTotal.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
        this.totalAmount = subTotal.add(taxAmount).setScale(2, RoundingMode.HALF_UP);
    }
}
