package org.storemate.pos.dto;

import org.storemate.pos.models.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a shopping cart for an employee.
 * Holds a list of cart items, manages sale/return mode,
 * and calculates totals including subtotal, tax, and total amount.
 */
public class CartDTO {
    private final List<Cart_Item_DTO> cartItems = new ArrayList<>();
    private final int employeeID;
    private boolean returnMode = false;
    private BigDecimal discount = BigDecimal.ZERO;

    /**
     * Constructor for creating a cart for a specific employee.
     *
     * @param employeeID The ID of the employee associated with this cart.
     */
    public CartDTO(int employeeID) { this.employeeID = employeeID; }

    public boolean isReturnMode() { return returnMode; }
    public void setReturnMode(boolean returnMode) { this.returnMode = returnMode; }

    public int getEmployeeID() { return employeeID; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public List<Cart_Item_DTO> getCartItems() { return cartItems; }

    /**
     * Adds a product to the cart, considering sale or return mode.
     * Prevents mixing sale and return items in the same cart.
     * If the product already exists in the cart, increments its quantity.
     *
     * @param product  The product to add.
     * @param quantity Quantity of the product.
     * @return True if the item was added or updated, false if rejected due to mixed mode.
     */
    public boolean addItem(Product product, int quantity) {
        boolean itemIsReturn = returnMode;

        boolean hasReturnItems = cartItems.stream().anyMatch(Cart_Item_DTO::isReturn);
        boolean hasSaleItems = cartItems.stream().anyMatch(Cart_Item_DTO::isSale);

        // Prevent mixing return and sale items
        if ((itemIsReturn && hasSaleItems) || (!itemIsReturn && hasReturnItems)) return false;

        // Check if item already exists in cart
        for (Cart_Item_DTO item : cartItems) {
            if (item.getProduct().getProduct_id() == product.getProduct_id() && item.isReturn() == itemIsReturn) {
                // Update quantity instead of adding new row
                item.setQuantity(item.getQuantity() + quantity);
                return true;
            }
        }

        // Item not in cart yet, add new row
        cartItems.add(new Cart_Item_DTO(product, quantity, itemIsReturn));
        return true;
    }



    /**
     * Removes a specific item from the cart.
     *
     * @param item The cart item to remove.
     */
    public void removeItem(Cart_Item_DTO item) { cartItems.remove(item); }

    /**
     * Clears all items from the cart.
     */
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
