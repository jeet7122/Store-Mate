package org.storemate.pos.dto;

import org.storemate.pos.models.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    public void removeItem(Purchase_Item_DTO item){
        cart.remove(item);
    }
    public void clear(){
        cart.clear();
    }
    public BigDecimal getSubtotal(){
        return cart.stream()
                .map(Purchase_Item_DTO::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
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
