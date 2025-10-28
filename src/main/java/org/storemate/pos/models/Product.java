package org.storemate.pos.models;

import org.storemate.pos.models.enums.ProductStatus;
import org.storemate.pos.models.enums.TaxCategory;

import java.math.BigDecimal;

public class Product {
    private String product_id;
    private String product_name;
    private String product_description;
    private Integer category_id;
    private TaxCategory taxCategory;
    private BigDecimal product_price;
    private Integer product_quantity;
    private ProductStatus availability;

    public Product(String product_id, String product_name, String product_description, Integer category_id, TaxCategory taxCategory, BigDecimal product_price, Integer product_quantity, ProductStatus availability) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.category_id = category_id;
        this.taxCategory = taxCategory;
        this.product_price = product_price;
        this.product_quantity = product_quantity;
        this.availability = availability;
    }

    public Product() {
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_id='" + product_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_description='" + product_description + '\'' +
                ", category_id=" + category_id +
                ", taxCategory=" + taxCategory +
                ", product_price=" + product_price +
                ", product_quantity=" + product_quantity +
                ", availability=" + availability +
                '}';
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public TaxCategory getTaxCategory() {
        return taxCategory;
    }

    public void setTaxCategory(TaxCategory taxCategory) {
        this.taxCategory = taxCategory;
    }

    public BigDecimal getProduct_price() {
        return product_price;
    }

    public void setProduct_price(BigDecimal product_price) {
        this.product_price = product_price;
    }

    public Integer getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(Integer product_quantity) {
        this.product_quantity = product_quantity;
    }

    public ProductStatus getAvailability() {
        return availability;
    }

    public void setAvailability(ProductStatus availability) {
        this.availability = availability;
    }
}
