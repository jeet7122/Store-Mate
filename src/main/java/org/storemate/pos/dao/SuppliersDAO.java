package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.models.Supplier;
import org.storemate.pos.models.enums.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.storemate.pos.database.TableConstants.*;

public class SuppliersDAO {


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
}
