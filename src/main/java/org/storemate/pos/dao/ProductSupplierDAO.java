package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//@author Jeet
public class ProductSupplierDAO {

    /*
     * Returns cost price for product supplied by supplier (or null if not found)
     */
    /**
     * Fetches the cost price of a product for a specific supplier.
     *
     * @param productId  Unique product identifier
     * @param supplierId Supplier ID
     * @return BigDecimal cost price if record exists,
     *         otherwise null

     * Use case:
     * - When showing inventory purchase cost
     * - When calculating profit margins per supplier

     * Behavior:
     * - Uses parameterized SQL to prevent SQL injection
     * - Returns only one record (first match)
     */
    public BigDecimal getCostPriceForSupplier(String productId, int supplierId) {
        String sql = "SELECT " + "cost_price FROM product_supplier WHERE product_id = ? AND supplier_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            ps.setInt(2, supplierId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("cost_price");
                }
            }
        } catch (Exception ex) {
            System.out.println("EXCEPTION: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Checks if a supplier-product record exists.
     *
     * @param productId  Product unique ID
     * @param supplierId Supplier ID
     * @return true if the product has a supplier entry, otherwise false

     * Behavior:
     * - Uses SELECT 1 because we only care about existence, not data
     * - Stops at first found match (efficient)
     * Use case:
     * - Prevent duplicate supplier entries
     * - Validation before insert
     */
    public boolean hasProductSupplier(String productId, int supplierId) {
        String sql = "SELECT 1 FROM product_supplier WHERE product_id = ? AND supplier_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            ps.setInt(2, supplierId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception ex) {
            System.out.println("EXCEPTION: " + ex.getMessage());;
        }
        return false;
    }
}
