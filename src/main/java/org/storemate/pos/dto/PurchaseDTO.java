package org.storemate.pos.dto;

import org.storemate.pos.models.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing a purchase order.
 * Contains a list of purchase items and the supplier associated with the purchase.
 * @author Jeet, Khushi
 */
public class PurchaseDTO {
    public final List<Purchase_Item_DTO> cart = new ArrayList<>();
    private int supplierID = -1;

    public List<Purchase_Item_DTO> getCart() {
        return cart;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    /**
     * Adds an item to the purchase. If the item already exists, it increases the quantity and optionally updates the cost price.
     *
     * @param product   The product to add.
     * @param quantity  The quantity to add.
     * @param costPrice The cost price per unit (nullable; if null, existing cost price is used).
     * @return true if the item was added or updated successfully.
     */
    public boolean addItem(Product product, int quantity, BigDecimal costPrice){
        for (Purchase_Item_DTO item : cart){
            if (item.getProduct().getProduct_id().equals(product.getProduct_id())){
                item.setQuantity(item.getQuantity() + quantity);
                if (costPrice != null){
                    item.setCostPrice(costPrice);
                }
                return true;
            }
        }
        cart.add(new Purchase_Item_DTO(product, quantity, costPrice));
        return true;
    }
    /**
     * Removes an item from the purchase.
     *
     * @param item The purchase item to remove.
     */
    public void removeItem(Purchase_Item_DTO item){
        cart.remove(item);
    }
    public void clear(){
        cart.clear();
    }

    /**
     * @return The subtotal for all items in the purchase (sum of item subtotals).
     */
    public BigDecimal getSubtotal(){
        return cart.stream()
                .map(Purchase_Item_DTO::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * @return The total tax amount for all items in the purchase.
     */
    public BigDecimal getTax() {
        return cart.stream().map(Purchase_Item_DTO::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public BigDecimal getTotal() {
        return getSubtotal().add(getTax());
    }

    public void recalcALL(){
        cart.forEach(Purchase_Item_DTO::recalc);
    }
    public boolean isEmpty(){
        return cart.isEmpty();
    }
}
