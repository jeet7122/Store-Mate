package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.models.Supplier;
import org.storemate.pos.models.enums.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.storemate.pos.database.TableConstants.*;

/**
 * DAO class for managing supplier records in the database.
 *
 * <p>This class provides methods to:
 * <ul>
 *     <li>Retrieve all suppliers</li>
 *     <li>Search suppliers by name</li>
 *     <li>Get a supplier by ID</li>
 *     <li>Add, modify, or delete suppliers</li>
 * </ul>
 *
 * <p>All queries use JDBC and the {@link DatabaseConnector} for obtaining database connections.
 * @author Jeet, Billen
 */
public class SuppliersDAO {

    /**
     * Retrieves all suppliers from the database.
     *
     * @return a list of {@link Supplier} objects; returns an empty list if no suppliers are found or on SQL error
     */
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

    /**
     * Searches suppliers by name or partial match.
     *
     * @param searchQuery the string to search in supplier names
     * @return a list of {@link Supplier} objects matching the search; empty list if none found
     */
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

    /**
     * Retrieves a supplier by its unique ID.
     *
     * @param supplierId the ID of the supplier to retrieve
     * @return the {@link Supplier} object if found; null otherwise
     */
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


    /**
     * Adds a new supplier to the database.
     *
     * @param supplier the {@link Supplier} object containing supplier details
     * @return true if the supplier was added successfully; false otherwise
     */
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

    /**
     * Updates an existing supplier's information in the database.
     *
     * @param supplier the {@link Supplier} object containing updated supplier details
     * @return true if the supplier was updated successfully; false otherwise
     */
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


    /**
     * Deletes a supplier from the database by its ID.
     *
     * @param supplierId the ID of the supplier to delete
     * @return true if the supplier was deleted successfully; false otherwise
     */
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
