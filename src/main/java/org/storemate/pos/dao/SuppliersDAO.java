package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.models.Supplier;
import org.storemate.pos.models.enums.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.storemate.pos.database.TableConstants.*;

public class SuppliersDAO {

    // Get all suppliers from the database
    public List<Supplier> getAllSuppliers() {
        String sql = "SELECT * FROM " + SUPPLIER_TABLE_NAME;
        List<Supplier> suppliers = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getString(SUPPLIER_COLUMN_NAME),
                        rs.getString(SUPPLIER_COLUMN_EMAIL),
                        rs.getString(SUPPLIER_COLUMN_ADDRESS),
                        Status.valueOf(rs.getString(SUPPLIER_COLUMN_CONNECTION)) // Assuming Status enum
                );
                supplier.setSupplier_id(rs.getInt(SUPPLIER_COLUMN_ID));
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }

        return suppliers;
    }

    // Search suppliers by name or partial
    public List<Supplier> searchForSupplier(String searchQuery) {
        String sql = "SELECT * FROM " + SUPPLIER_TABLE_NAME +
                " WHERE " + SUPPLIER_COLUMN_NAME + " LIKE ?";
        List<Supplier> suppliers = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchQuery + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Supplier supplier = new Supplier(
                            rs.getString(SUPPLIER_COLUMN_NAME),
                            rs.getString(SUPPLIER_COLUMN_EMAIL),
                            rs.getString(SUPPLIER_COLUMN_ADDRESS),
                            Status.valueOf(rs.getString(SUPPLIER_COLUMN_CONNECTION))
                    );
                    supplier.setSupplier_id(rs.getInt(SUPPLIER_COLUMN_ID));
                    suppliers.add(supplier);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }

        return suppliers;
    }
    public Supplier getSupplierById(int supplierId) {
        String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Supplier s = new Supplier();
                s.setSupplier_id(rs.getInt("supplier_id"));
                s.setSupplier_name(rs.getString("supplier_name"));
                s.setSupplier_email(rs.getString("supplier_email"));
                // load other fields as needed
                return s;
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return null;
    }

    // Add a new supplier to the database
    public boolean addSupplier(Supplier supplier) {
        String sql = "INSERT INTO " + SUPPLIER_TABLE_NAME + " (" +
                SUPPLIER_COLUMN_NAME + ", " +
                SUPPLIER_COLUMN_EMAIL + ", " +
                SUPPLIER_COLUMN_ADDRESS + ", " +
                SUPPLIER_COLUMN_CONNECTION + ") VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supplier.getSupplier_name());
            stmt.setString(2, supplier.getSupplier_email());
            stmt.setString(3, supplier.getSupplier_location());
            stmt.setString(4, supplier.getSupplier_status().toString());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }

        return false;
    }

    // Update supplier in the database
    public boolean modifySupplier(Supplier supplier) {
        String sql = "UPDATE " + SUPPLIER_TABLE_NAME + " SET " +
                SUPPLIER_COLUMN_NAME + " = ?, " +
                SUPPLIER_COLUMN_EMAIL + " = ?, " +
                SUPPLIER_COLUMN_ADDRESS + " = ?, " +
                SUPPLIER_COLUMN_CONNECTION + " = ? WHERE " +
                SUPPLIER_COLUMN_ID + " = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getSupplier_name());
            stmt.setString(2, supplier.getSupplier_email());
            stmt.setString(3, supplier.getSupplier_location());
            stmt.setString(4, supplier.getSupplier_status().toString());
            stmt.setInt(5, supplier.getSupplier_id());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }

        return false;
    }

    // Delete a supplier from the database
    public boolean deleteSupplier(int supplierId) {
        String sql = "DELETE FROM " + SUPPLIER_TABLE_NAME + " WHERE " +
                SUPPLIER_COLUMN_ID + " = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, supplierId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }

        return false;
    }
}
