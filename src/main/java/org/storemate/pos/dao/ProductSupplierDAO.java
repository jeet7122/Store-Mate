package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductSupplierDAO {

    /**
     * Returns cost price for product supplied by supplier (or null if not found)
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
     * returns true if record exists for this product & supplier
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
