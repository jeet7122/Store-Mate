package org.storemate.pos.dto;

import org.storemate.pos.models.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartDTO {
    private int employeeId;
    private List<Cart_Item_DTO> cartItems;
    private BigDecimal subtotal;
    private BigDecimal totalTax;
    private BigDecimal totalAmount;
    private BigDecimal discount;
    public CartDTO(int employeeId) {
        this.employeeId = employeeId;
        this.cartItems = new ArrayList<>();
        this.subtotal = BigDecimal.ZERO;
        this.totalTax = BigDecimal.ZERO;
        this.totalAmount = BigDecimal.ZERO;
        this.discount = BigDecimal.ZERO;
    }

    public void addItem(Product product, int quantity){
        for (Cart_Item_DTO item : cartItems){
            if (item.getProduct().getProduct_id().equals(product.getProduct_id())){
                item.setQuantity(item.getQuantity() + quantity);
                item.calculateTotal();
                recalculateCartTotal();
                return;
            }
        }
        Cart_Item_DTO newCartItem = new Cart_Item_DTO(product, quantity);
        cartItems.add(newCartItem);
        recalculateCartTotal();
    }
    public void removeItem(Cart_Item_DTO item) {
        cartItems.remove(item);
        recalculateCartTotal();
    }

    public void recalculateCartTotal() {
        subtotal = BigDecimal.ZERO;
        totalTax = BigDecimal.ZERO;
        for (Cart_Item_DTO item : cartItems){
            subtotal = subtotal.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            totalTax = totalTax.add(item.getTaxAmount());
        }
        totalAmount = subtotal.add(totalTax).subtract(discount);
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public List<Cart_Item_DTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<Cart_Item_DTO> cartItems) {
        this.cartItems = cartItems;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public void clearCart() {
        cartItems.clear();
        recalculateCartTotal();
    }
}
