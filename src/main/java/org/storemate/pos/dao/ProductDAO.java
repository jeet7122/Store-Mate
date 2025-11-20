package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.models.Product;
import org.storemate.pos.models.enums.ProductStatus;
import org.storemate.pos.models.enums.TaxCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.storemate.pos.database.TableConstants.*;

public class ProductDAO {
    public Product searchForProduct(String searchQuery){
        String sqlQuery = "SELECT * FROM " + PRODUCT_TABLE_NAME
                + " WHERE " + PRODUCT_COLUMN_NAME  + " LIKE ? LIMIT 1;";
        try
                (
                        Connection connection = DatabaseConnector.getConnection();
                        PreparedStatement statement = connection.prepareStatement(sqlQuery)
                )
        {
            statement.setString(1, "%" + searchQuery + "%");
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String stringTaxCat = rs.getString(PRODUCT_COLUMN_TAX_CATEGORY);
                String availableStr = rs.getString(PRODUCT_COLUMN_AVAILABILITY);
                ProductStatus availability = ProductStatus.valueOf(ProductStatus.class, availableStr);
                TaxCategory taxCategory = TaxCategory.valueOf(TaxCategory.class, stringTaxCat);
                Product product = new Product();
                product.setProduct_id(rs.getString(PRODUCT_COLUMN_ID));
                product.setProduct_name(rs.getString(PRODUCT_COLUMN_NAME));
                product.setProduct_description(rs.getString(PRODUCT_COLUMN_DESCRIPTION));
                product.setProduct_price(rs.getBigDecimal(PRODUCT_COLUMN_PRICE));
                product.setProduct_quantity(rs.getInt(PRODUCT_COLUMN_QUANTITY));
                product.setCategory_id(rs.getInt(PRODUCT_COLUMN_CATEGORY_ID));
                product.setTaxCategory(taxCategory);
                product.setAvailability(availability);
                return product;
            }
        }
        catch (SQLException e){
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        catch (Exception e){
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        return null;
    }

    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM " + PRODUCT_TABLE_NAME;
        List<Product> products = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setProduct_id(rs.getString(PRODUCT_COLUMN_ID));
                p.setProduct_name(rs.getString(PRODUCT_COLUMN_NAME));
                p.setProduct_description(rs.getString(PRODUCT_COLUMN_DESCRIPTION));
                p.setProduct_quantity(rs.getInt(PRODUCT_COLUMN_QUANTITY));
                p.setAvailability(ProductStatus.valueOf(rs.getString(PRODUCT_COLUMN_AVAILABILITY)));
                p.setTaxCategory(TaxCategory.valueOf(rs.getString(PRODUCT_COLUMN_TAX_CATEGORY)));
                p.setProduct_price(rs.getBigDecimal(PRODUCT_COLUMN_PRICE));
                p.setCategory_id(rs.getInt(PRODUCT_COLUMN_CATEGORY_ID));
                products.add(p);
            }
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        return products;
    }

}
