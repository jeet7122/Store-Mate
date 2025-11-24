package org.storemate.pos.dto;

import org.storemate.pos.models.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartDTO {
    private final List<Cart_Item_DTO> cartItems = new ArrayList<>();
    private final int employeeID;
    private boolean returnMode = false;
    private BigDecimal discount = BigDecimal.ZERO;

    public CartDTO(int employeeID) { this.employeeID = employeeID; }

    public boolean isReturnMode() { return returnMode; }
    public void setReturnMode(boolean returnMode) { this.returnMode = returnMode; }

    public int getEmployeeID() { return employeeID; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public List<Cart_Item_DTO> getCartItems() { return cartItems; }

    // Add item with sale/return separation
    public boolean addItem(Product product, int quantity) {
        boolean itemIsReturn = returnMode;

        boolean hasReturnItems = cartItems.stream().anyMatch(Cart_Item_DTO::isReturn);
        boolean hasSaleItems = cartItems.stream().anyMatch(Cart_Item_DTO::isSale);

        if ((itemIsReturn && hasSaleItems) || (!itemIsReturn && hasReturnItems)) return false;

        cartItems.add(new Cart_Item_DTO(product, quantity, itemIsReturn));
        return true;
    }

    public void removeItem(Cart_Item_DTO item) { cartItems.remove(item); }
    public void clearCart() { cartItems.clear(); }

    // Totals for all items
    public BigDecimal getSubtotal() {
        return cartItems.stream().map(Cart_Item_DTO::getLineSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public BigDecimal getTotalTax() {
        return cartItems.stream().map(Cart_Item_DTO::getTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public BigDecimal getTotalAmount() {
        return getSubtotal().add(getTotalTax());
    }

    // Totals for sales only
    public List<Cart_Item_DTO> getSaleItem() {
        List<Cart_Item_DTO> sales = new ArrayList<>();
        for (Cart_Item_DTO item : cartItems) if (item.isSale()) sales.add(item);
        return sales;
    }
    public BigDecimal getSaleSubtotal() {
        return getSaleItem().stream().map(Cart_Item_DTO::getLineSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public BigDecimal getSaleTax() {
        return getSaleItem().stream().map(Cart_Item_DTO::getTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Returns only
    public List<Cart_Item_DTO> getReturnItem() {
        List<Cart_Item_DTO> returns = new ArrayList<>();
        for (Cart_Item_DTO item : cartItems) if (item.isReturn()) returns.add(item);
        return returns;
    }

    // Checks
    public boolean isMixed() {
        boolean hasReturn = cartItems.stream().anyMatch(Cart_Item_DTO::isReturn);
        boolean hasSale = cartItems.stream().anyMatch(Cart_Item_DTO::isSale);
        return hasReturn && hasSale;
    }
    public boolean isSaleOnly() { return !cartItems.isEmpty() && cartItems.stream().allMatch(Cart_Item_DTO::isSale); }
    public boolean isReturnOnly() { return !cartItems.isEmpty() && cartItems.stream().allMatch(Cart_Item_DTO::isReturn); }
    public void recalculateCartTotal(){
        for (Cart_Item_DTO item : cartItems) {
            item.recalc(); // make recalc() public in Cart_Item_DTO
        }
    }
}
