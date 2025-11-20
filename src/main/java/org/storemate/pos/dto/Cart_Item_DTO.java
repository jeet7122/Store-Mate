package org.storemate.pos.dto;

import org.storemate.pos.models.Product;

import java.math.BigDecimal;

public class Cart_Item_DTO {
    private Product product;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;

    public Cart_Item_DTO(Product product, int quantity){
        this.product = product;
        this.quantity = quantity;
        unitPrice = product.getProduct_price();
        calculateTotal();
    }
    public void calculateTotal(){
        double taxRate = product.getTaxCategory().getTax_value();
        this.taxAmount = unitPrice.multiply(BigDecimal.valueOf(quantity)).multiply(BigDecimal.valueOf(taxRate));
        this.totalAmount = unitPrice.multiply(BigDecimal.valueOf(quantity)).add(taxAmount);
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Cart_Item_DTO{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", taxAmount=" + taxAmount +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
