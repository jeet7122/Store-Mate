package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.models.Product;
import org.storemate.pos.models.SupplierProduct;
import org.storemate.pos.models.enums.ProductStatus;
import org.storemate.pos.models.enums.TaxCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.storemate.pos.database.TableConstants.*;

/**
 * DAO (Data Access Object) responsible for CRUD operations on Product table.
 * Handles product search, insert, update, delete, and supplier-product relations.

 * NOTE:
 * - Uses DatabaseConnector to obtain a DB connection
 * - Uses constants from TableConstants for column/table names
 * @author Jeet, Billen
 */
public class ProductDAO {

    // Search for a product by its name
    /**
     * Search for a single product by partial name match.
     * Example: "Sam" → finds "Samsung S9"
     *
     * @param searchQuery text to match in product name (LIKE %query%)
     * @return Product if found, otherwise null
     */
    public Product searchForProduct(String searchQuery) {
        String sqlQuery = "SELECT * FROM " + PRODUCT_TABLE_NAME
                + " WHERE " + PRODUCT_COLUMN_NAME  + " LIKE ? LIMIT 1;";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

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
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        return null;
    }

    // Get all products from the database

    /**
     * Fetch all products from the database.
     *
     * @return List<Product>
     */
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

    // Add a new product to the database
    /**
     * Insert a new product record into DB.
     *
     * @param product Product object containing required fields
     * @return true if insert succeeded
     */
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO " + PRODUCT_TABLE_NAME + " (" +
                PRODUCT_COLUMN_ID + ", " +
                PRODUCT_COLUMN_NAME + ", " +
                PRODUCT_COLUMN_DESCRIPTION + ", " +
                PRODUCT_COLUMN_PRICE + ", " +
                PRODUCT_COLUMN_QUANTITY + ", " +
                PRODUCT_COLUMN_AVAILABILITY + ", " +
                PRODUCT_COLUMN_TAX_CATEGORY + ", " +
                PRODUCT_COLUMN_CATEGORY_ID + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProduct_id());
            stmt.setString(2, product.getProduct_name());
            stmt.setString(3, product.getProduct_description());
            stmt.setBigDecimal(4, product.getProduct_price());
            stmt.setInt(5, product.getProduct_quantity());
            stmt.setString(6, product.getAvailability().name());  // Convert enum to String
            stmt.setString(7, product.getTaxCategory().name());  // Convert enum to String
            stmt.setInt(8, product.getCategory_id());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Return true if product was added
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        return false;
    }

    // Update an existing product in the database
    /**
     * Update existing product values.

     * ID is used as WHERE condition → must exist in DB
     *
     * @param product Product object with updated fields
     * @return true if update succeeded
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE " + PRODUCT_TABLE_NAME + " SET " +
                PRODUCT_COLUMN_NAME + " = ?, " +
                PRODUCT_COLUMN_DESCRIPTION + " = ?, " +
                PRODUCT_COLUMN_PRICE + " = ?, " +
                PRODUCT_COLUMN_QUANTITY + " = ?, " +
                PRODUCT_COLUMN_AVAILABILITY + " = ?, " +
                PRODUCT_COLUMN_TAX_CATEGORY + " = ?, " +
                PRODUCT_COLUMN_CATEGORY_ID + " = ? WHERE " +
                PRODUCT_COLUMN_ID + " = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProduct_name());
            stmt.setString(2, product.getProduct_description());
            stmt.setBigDecimal(3, product.getProduct_price());
            stmt.setInt(4, product.getProduct_quantity());
            stmt.setString(5, product.getAvailability().name());
            stmt.setString(6, product.getTaxCategory().name());
            stmt.setInt(7, product.getCategory_id());
            stmt.setString(8, product.getProduct_id());  // Product ID to identify the product to update

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Return true if product was updated
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        return false;
    }

    // Delete a product from the database
    /**
     * Delete a product by ID.
     *
     * @param productId product unique identifier
     * @return true if delete succeeded
     */
    public boolean deleteProduct(String productId) {
        String sql = "DELETE FROM " + PRODUCT_TABLE_NAME + " WHERE " + PRODUCT_COLUMN_ID + " = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        return false;
    }

    /**
     * Add a supplier-product relationship record.
     * Used when supplier provides inventory for a product.
     *
     * @param supplierProduct SupplierProduct model
     * @return true if insert succeeded
     */
    public boolean addToPS_Table(SupplierProduct supplierProduct) {
        String sql = "INSERT INTO " + PRODUCT_SUPPLIER_TABLE_NAME + "("
                + PRODUCT_SUPPLIER_COLUMN_PRODUCT_ID + ", "
                + PRODUCT_SUPPLIER_COLUMN_SUPPLIER_ID + ", "
                + PRODUCT_SUPPLIER_COLUMN_COST_PRICE + ") VALUES (?, ?, ?)";

        try
                (Connection connection = DatabaseConnector.getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, supplierProduct.getProduct_id());
            ps.setInt(2, supplierProduct.getSupplier_id());
            ps.setBigDecimal(3, supplierProduct.getCostPrice());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION:" + e.getMessage());
        }
        return false;
    }
}
