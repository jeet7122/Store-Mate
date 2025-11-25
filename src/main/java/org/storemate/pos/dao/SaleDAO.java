package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.dto.CartDTO;
import org.storemate.pos.dto.Cart_Item_DTO;
import org.storemate.pos.models.Sale;
import org.storemate.pos.models.enums.PaymentMethod;
import org.storemate.pos.models.enums.SaleStatus;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.storemate.pos.database.TableConstants.*;

public class SaleDAO {
    public boolean saveSale(CartDTO cartDTO, String paymentType) {
        try (Connection connection = DatabaseConnector.getConnection()) {

            connection.setAutoCommit(false);

            String saleSQL = "INSERT INTO " + SALE_TABLE_NAME + " ( "
                    + SALE_COLUMN_EMPLOYEE_ID + ", "
                    + SALE_COLUMN_SUBTOTAL + ", "
                    + SALE_COLUMN_TAX_AMOUNT + ", "
                    + SALE_COLUMN_DISCOUNT_AMOUNT + ", "
                    + SALE_COLUMN_TOTAL_AMOUNT + ", "
                    + SALE_COLUMN_PAYMENT_METHOD + ", "
                    + SALE_COLUMN_STATUS + ") VALUES (?,?,?,?,?,?,?)";

            PreparedStatement salePS = connection.prepareStatement(
                    saleSQL,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            BigDecimal discount = cartDTO.getDiscount() == null ? BigDecimal.ZERO : cartDTO.getDiscount();
            salePS.setInt(1, cartDTO.getEmployeeID());
            salePS.setBigDecimal(2, cartDTO.getSaleSubtotal());
            salePS.setBigDecimal(3, cartDTO.getSaleTax());
            salePS.setBigDecimal(4, discount);
            salePS.setBigDecimal(5, cartDTO.getSaleSubtotal().add(cartDTO.getSaleTax()).subtract(cartDTO.getDiscount()));
            salePS.setString(6, paymentType);
            salePS.setString(7, SaleStatus.COMPLETED.name());

            salePS.executeUpdate();

            ResultSet rs = salePS.getGeneratedKeys();
            rs.next();
            int saleId = rs.getInt(1);   // ✔ MySQL-compatible

            // ---- INSERT ITEMS ----
            String itemSQL = "INSERT INTO " + SALE_ITEM_TABLE_NAME + " ( "
                    + SALE_ITEM_SALE_ID + ", "
                    + SALE_ITEM_PRODUCT_ID + ", "
                    + SALE_ITEM_QUANTITY + ", "
                    + SALE_ITEM_UNIT_PRICE + ", "
                    + SALE_ITEM_TAX_CATEGORY + ", "
                    + SALE_ITEM_TAX_AMOUNT + ", "
                    + SALE_ITEM_TOTAL_AMOUNT + ") VALUES (?,?,?,?,?,?,?)";

            PreparedStatement itemPS = connection.prepareStatement(itemSQL);

            for (Cart_Item_DTO cartItem : cartDTO.getSaleItem()) {
                itemPS.setInt(1, saleId);
                itemPS.setString(2, cartItem.getProduct().getProduct_id());
                itemPS.setInt(3, cartItem.getQuantity());
                itemPS.setBigDecimal(4, cartItem.getUnitPrice());
                itemPS.setString(5, cartItem.getProduct().getTaxCategory().name());
                itemPS.setBigDecimal(6, cartItem.getTaxAmount());
                itemPS.setBigDecimal(7, cartItem.getTotalAmount());
                itemPS.addBatch();
            }

            itemPS.executeBatch();

            String stockSQL = "UPDATE " + PRODUCT_TABLE_NAME + " SET quantity = quantity - ? WHERE product_id = ?;";
            PreparedStatement stockPS = connection.prepareStatement(stockSQL);

            for (Cart_Item_DTO item : cartDTO.getSaleItem()){
                stockPS.setInt(1, item.getQuantity());
                stockPS.setString(2, item.getProduct().getProduct_id());
                stockPS.addBatch();
            }
            stockPS.executeBatch();
            connection.commit();

            return true;

        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        return false;
    }
    public Map<String, Double> getSalesLast30Days() {
        Map<String, Double> salesData = new LinkedHashMap<>();
        String sql = """
                SELECT sale_date, SUM(total_amount) as total
                FROM sales
                WHERE sale_date >= ?
                GROUP BY sale_date
                ORDER BY sale_date
                """;
        try (
                Connection conn = DatabaseConnector.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            LocalDate fromDate = LocalDate.now().minusDays(30);
            ps.setDate(1, Date.valueOf(fromDate));
            ResultSet rs = ps.executeQuery();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

            while (rs.next()) {
                LocalDate date = rs.getDate("sale_date").toLocalDate();
                double total = rs.getDouble("total");
                salesData.put(date.format(formatter), total);
            }
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }

        return salesData;
    }

    // --- Fetch weekly transactions total by payment method ---
    public Map<String, Double> getWeeklyTransactions() {
        Map<String, Double> transactions = new LinkedHashMap<>();
        String sql = """
                SELECT payment_method, SUM(total_amount) as total
                FROM sales
                WHERE sale_date >= ?
                GROUP BY payment_method
                """;
        try (
                Connection conn = DatabaseConnector.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            LocalDate fromDate = LocalDate.now().minusDays(7);
            ps.setDate(1, Date.valueOf(fromDate));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String method = rs.getString("payment_method"); // e.g., CASH, CREDIT, DEBIT
                double total = rs.getDouble("total");
                transactions.put(method, total);
            }
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }

        return transactions;
    }

    // Optional: Fetch all sales (for table view)
    public ResultSet getAllSalesForGraph() {
        try
                (Connection conn = DatabaseConnector.getConnection())
        {
            return conn.createStatement().executeQuery("SELECT * FROM sales ORDER BY sale_timestamp DESC");
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
            return null;
        }
    }



}
