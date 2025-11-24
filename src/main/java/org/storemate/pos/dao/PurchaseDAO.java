package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.models.Purchase;
import org.storemate.pos.models.enums.PaymentMethod;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Double> getPurchasesLast30Days() {
        Map<String, Double> purchaseData = new LinkedHashMap<>();
        String sql = """
                SELECT DATE(purchase_date) as purchase_day, SUM(total_amount) as total
                FROM purchases
                WHERE purchase_date >= ?
                GROUP BY purchase_day
                ORDER BY purchase_day
                """;
        try (
                Connection conn = DatabaseConnector.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            LocalDate fromDate = LocalDate.now().minusDays(30);
            ps.setDate(1, Date.valueOf(fromDate));
            ResultSet rs = ps.executeQuery();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

            while (rs.next()) {
                LocalDate date = rs.getDate("purchase_day").toLocalDate();
                double total = rs.getDouble("total");
                purchaseData.put(date.format(formatter), total);
            }

        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }

        return purchaseData;
    }
}
