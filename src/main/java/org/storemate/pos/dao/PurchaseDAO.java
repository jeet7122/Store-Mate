package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.models.Purchase;
import org.storemate.pos.models.enums.PaymentMethod;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAO {

    // Get all purchases
    public List<Purchase> getAllPurchases() {
        List<Purchase> purchases = new ArrayList<>();
        String sql = "SELECT purchase_id, payment_method, total_amount, purchase_date FROM purchases ORDER BY purchase_date DESC";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Purchase p = new Purchase();
                String paymentSTR = rs.getString("payment_method");
                PaymentMethod method = PaymentMethod.valueOf(paymentSTR);
                p.setPurchaseID(rs.getInt("purchase_id"));
                p.setPaymentMethod(method);
                p.setTotal_amount(rs.getBigDecimal("total_amount"));
                p.setPurchaseDate(rs.getTimestamp("purchase_date"));
                purchases.add(p);
            }

        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        return purchases;
    }

    // Count by payment method
    public int countByPaymentMethod(String paymentMethod) {
        String sql = "SELECT COUNT(*) FROM purchases WHERE payment_method = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paymentMethod);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: "+ e.getMessage());
        }
        return 0;
    }
}
