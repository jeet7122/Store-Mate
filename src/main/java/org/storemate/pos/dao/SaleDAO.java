package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.dto.CartDTO;
import org.storemate.pos.dto.Cart_Item_DTO;
import org.storemate.pos.models.enums.SaleStatus;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.storemate.pos.database.TableConstants.*;

/**
 * DAO responsible for executing sale-related database operations.
 *
 * <p>This class handles:
 * <ul>
 *     <li>Recording a completed sale transaction</li>
 *     <li>Storing associated sale items</li>
 *     <li>Stock reduction for sold products</li>
 *     <li>Fetching analytical data for charts and dashboards</li>
 *     <li>Returning raw ResultSets for data visualization</li>
 * </ul>
 *
 * All queries use JDBC with explicit transaction handling for write operations.
 * @author Jeet
 */
public class SaleDAO {

    /**
     * Persists a completed sale into the database.
     *
     * <p>This method performs a multistep transactional process:
     * <ol>
     *     <li>Inserts a record into the {@code sales} table</li>
     *     <li>Stores each sold item in the {@code sale_items} table</li>
     *     <li>Updates product stock (subtracts sold quantities)</li>
     * </ol>
     *
     * <p>If any step fails, the transaction is rolled back automatically
     * because the method encloses all operations inside a transaction scope.
     *
     * @param cartDTO     DTO representing all sale items and totals
     * @param paymentType Name of payment method used (e.g., CASH, CARD, DEBIT)
     *
     * @return {@code true} if the sale and all items are recorded successfully,
     *         {@code false} if any SQL error occurs.
     */
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

    /**
     * Retrieves aggregated daily sales totals for the last 30 calendar days.
     *
     * <p>The results are grouped by sale date and stored in insertion order
     * using a LinkedHashMap where:
     * <ul>
     *      <li>Key = formatted date string (MM-dd)</li>
     *      <li>Value = total aggregated sale amount for that day</li>
     * </ul>
     *
     * <p>Useful for chart display such as line graphs or analytics dashboards.
     *
     * @return ordered map of {@code date → total sales} values.
     *         Returns an empty map on SQL error or no data.
     */

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
    /**
     * Computes total sales amounts grouped by payment method
     * over the last 7 days.
     *
     * <p>This is typically used to generate donut/pie charts
     * categorizing sales by payment type.
     *
     * <ul>
     *     <li>Key = payment method (e.g., CASH, CARD, STORE_CREDIT)</li>
     *     <li>Value = aggregated transaction total in that period</li>
     * </ul>
     *
     * @return ordered map of {@code payment method → total sales}.
     *         Returns an empty map on SQL error.
     */
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
    /**
     * Retrieves all sales records in descending timestamp order.
     *
     * <p>This method returns a raw {@link ResultSet} intended
     * for UI components or graph rendering where the caller
     * processes cursor-based row iteration.
     * <strong>⚠️ Note:</strong> The caller is responsible for handling
     * the ResultSet lifecycle and closing database resources.
     *
     * @return a ResultSet containing all sales data,
     *         or {@code null} if a database error occurs.
     */
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
