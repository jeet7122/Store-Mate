package org.storemate.pos.dto;

import org.storemate.pos.models.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Cart_Item_DTO {
    private Product product;
    private int quantity;
    private boolean isReturn;

    private BigDecimal unitPrice;
    private BigDecimal lineSubtotal;  // price * qty
    private BigDecimal taxAmount;     // subtotal * tax
    private BigDecimal totalAmount;   // subtotal + tax

    public Cart_Item_DTO(Product product, int quantity, boolean isReturn){
        this.isReturn = isReturn;
        this.product = product;
        this.quantity = quantity;

        this.unitPrice = product.getProduct_price();
        recalc();
    }

    public void recalc() {
        BigDecimal qty = BigDecimal.valueOf(quantity);
        BigDecimal taxRate = BigDecimal.valueOf(product.getTaxCategory().getTax_value());

        this.lineSubtotal = unitPrice.multiply(qty).setScale(2, RoundingMode.HALF_UP);
        this.taxAmount = lineSubtotal.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
        this.totalAmount = lineSubtotal.add(taxAmount).setScale(2, RoundingMode.HALF_UP);
    }

    public void setQuantity(int quantity) { this.quantity = quantity; recalc(); }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; recalc(); }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getLineSubtotal() { return lineSubtotal; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }

    // ✅ Returns true if this item is a return
    public boolean isReturn() { return isReturn; }

    // ✅ Returns true if this item is a sale
    public boolean isSale() { return !isReturn; }

    @Override
    public String toString() {
        return "Cart_Item_DTO{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + lineSubtotal +
                ", taxAmount=" + taxAmount +
                ", totalAmount=" + totalAmount +
                ", isReturn=" + isReturn +
                '}';
    }
}
