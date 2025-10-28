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
}
