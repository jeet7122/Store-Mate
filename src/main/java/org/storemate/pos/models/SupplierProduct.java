package org.storemate.pos.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class SupplierProduct {
    private int supplier_id;
    private String product_id;
    private BigDecimal costPrice;
    private Timestamp lastUpdated;

    @Override
    public String toString() {
        return "SupplierProduct{" +
                "supplier_id=" + supplier_id +
                ", product_id='" + product_id + '\'' +
                ", costPrice=" + costPrice +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    public SupplierProduct() {
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public SupplierProduct(Integer supplier_id, String product_id, BigDecimal costPrice) {
        this.supplier_id = supplier_id;
        this.product_id = product_id;
        this.costPrice = costPrice;
    }
}
