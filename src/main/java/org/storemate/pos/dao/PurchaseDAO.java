package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.dto.PurchaseDTO;
import org.storemate.pos.dto.Purchase_Item_DTO;
import org.storemate.pos.models.Purchase;
import org.storemate.pos.models.enums.PaymentMethod;
import org.storemate.pos.models.enums.PurchaseStatus;

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

    public int createPurchase(PurchaseDTO cart, String status, String paymentMethod) {
        if (cart == null || cart.isEmpty()) return -1;

        String insertPurchaseSql = "INSERT INTO purchases (supplier_id, tax_amount, total_amount, status, payment_method, purchase_date) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        String insertItemSql = "INSERT INTO purchase_items (purchase_id, product_id, quantity, unit_price, tax_category, tax_amount, total_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updateProductSql = "UPDATE products SET quantity = quantity + ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psPurchase = conn.prepareStatement(insertPurchaseSql, Statement.RETURN_GENERATED_KEYS)) {

                // compute totals
                java.math.BigDecimal tax = cart.getTax();
                java.math.BigDecimal total = cart.getTotal();

                psPurchase.setInt(1, cart.getSupplierID());
                psPurchase.setBigDecimal(2, tax);
                psPurchase.setBigDecimal(3, total);
                psPurchase.setString(4, status);
                psPurchase.setString(5, paymentMethod == null ? "" : paymentMethod);

                int affected = psPurchase.executeUpdate();
                if (affected == 0) throw new RuntimeException("Failed to insert purchase");

                int purchaseId;
                try (ResultSet keys = psPurchase.getGeneratedKeys()) {
                    if (keys.next()) purchaseId = keys.getInt(1);
                    else throw new RuntimeException("No purchase id generated");
                }

                try (PreparedStatement psItem = conn.prepareStatement(insertItemSql);
                     PreparedStatement psUpdateProd = conn.prepareStatement(updateProductSql)) {
                    for (Purchase_Item_DTO item : cart.getCart()) {
                        psItem.setInt(1, purchaseId);
                        psItem.setString(2, item.getProduct().getProduct_id());
                        psItem.setInt(3, item.getQuantity());
                        psItem.setBigDecimal(4, item.getCostPrice());
                        psItem.setString(5, item.getProduct().getTaxCategory().name()); // adjust if enum->string mapping differs
                        psItem.setBigDecimal(6, item.getTaxAmount());
                        psItem.setBigDecimal(7, item.getTotalAmount());
                        psItem.addBatch();

                        // update product quantity
                        psUpdateProd.setInt(1, item.getQuantity());
                        psUpdateProd.setString(2, item.getProduct().getProduct_id());
                        psUpdateProd.addBatch();
                    }

                    psItem.executeBatch();
                    psUpdateProd.executeBatch();
                }

                conn.commit();
                return purchaseId;

            } catch (Exception ex) {
                conn.rollback();
                System.out.println("Exception: " + ex.getMessage());
                return -1;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return -1;
        }
    }

    public List<Purchase> getAllPurchasesForTable() {
        List<Purchase> purchases = new ArrayList<>();
        String sql = "SELECT * FROM purchases ORDER BY purchase_date DESC";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Purchase p = new Purchase();
                String paymentSTR = rs.getString("payment_method");
                String purchaseStatusSTR = rs.getString("status");
                PurchaseStatus purchaseStatus = PurchaseStatus.valueOf(purchaseStatusSTR);
                PaymentMethod method = PaymentMethod.valueOf(paymentSTR);
                p.setPurchaseID(rs.getInt("purchase_id"));
                p.setPaymentMethod(method);
                p.setPurchaseStatus(purchaseStatus);
                p.setSupplierID(rs.getInt("supplier_id"));
                p.setTotal_amount(rs.getBigDecimal("total_amount"));
                p.setPurchaseDate(rs.getTimestamp("purchase_date"));
                p.setTax_amount(rs.getBigDecimal("tax_amount"));
                purchases.add(p);
            }

        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        return purchases;

    }

    public List<Purchase> searchPurchaseByPaymentMethod(String keyword) {
        String sql = "SELECT * FROM purchases WHERE payment_method = ?";
        List<Purchase> purchases = new ArrayList<>();

        try
                (Connection connection = DatabaseConnector.getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, keyword);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Purchase p = new Purchase();
                p.setPurchaseID(rs.getInt("purchase_id"));
                p.setSupplierID(rs.getInt("supplier_id"));
                p.setPurchaseStatus(PurchaseStatus.valueOf(rs.getString("status")));
                p.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
                p.setPurchaseDate(rs.getTimestamp("purchase_date"));
                p.setTax_amount(rs.getBigDecimal("tax_amount"));
                p.setTotal_amount(rs.getBigDecimal("total_amount"));
                purchases.add(p);
            }

        }
        catch (SQLException ex){
            System.out.println("SQl Exception: " + ex.getMessage());
        }
        catch (Exception ex){
            System.out.println("Exception: " +  ex.getMessage());
        }
        return purchases;
    }
}
