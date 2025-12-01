package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.dto.CartDTO;
import org.storemate.pos.dto.Cart_Item_DTO;
import org.storemate.pos.models.Returns;
import org.storemate.pos.models.enums.PaymentMethod;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.storemate.pos.database.TableConstants.*;

/**
 * DAO responsible for handling product return operations within the POS system.
 * Provides functionality for:
 * <ul>
 *     <li>Processing returns and inserting related return & return item records</li>
 *     <li>Updating inventory after return transactions</li>
 *     <li>Fetching return history and analytics information</li>
 * </ul>
 *
 * Works directly with the database using JDBC.
 * @author Jeet, Khushi
 */
public class ReturnDAO {


    /**
     * Processes a customer return transaction.
     *
     * <p>This method performs a multi-step transactional process:
     * <ol>
     *     <li>Computes total refund amount based on returned items</li>
     *     <li>Creates a record in the {@code returns} table</li>
     *     <li>Stores each returned product in the {@code return_items} table</li>
     *     <li>Updates product stock by adding back returned quantities</li>
     * </ol>
     *
     * <p>If any database step fails, the entire transaction is rolled back.
     *
     * @param cartDTO               Data transfer object containing all returned items
     * @param processedByEmployeeId ID of the employee processing the return
     * @param refundMethod          Method of refund (e.g. CASH, CARD, STORE_CREDIT)
     * @param returnReason          Reason for return (customer provided)
     *
     * @return {@code true} if return is recorded and stock updated successfully,
     *         {@code false} if an error occurs.
     */
    public boolean processReturn(CartDTO cartDTO, int processedByEmployeeId, String refundMethod, String returnReason) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            connection.setAutoCommit(false);

            // 1️⃣ Compute total refund including tax
            BigDecimal returnTotal = BigDecimal.ZERO;
            for (Cart_Item_DTO item : cartDTO.getReturnItem()) {
                returnTotal = returnTotal.add(item.getTotalAmount());
            }

            // 2️⃣ Insert into returns table
            String returnSQL = "INSERT INTO " + RETURN_TABLE_NAME + " ("
                    + RETURN_COLUMN_PROCESSED_BY + ", "
                    + RETURN_COLUMN_REFUND_METHOD + ", "
                    + RETURN_COLUMN_REFUND_TOTAL + ") VALUES (?,?,?)";

            PreparedStatement returnPS = connection.prepareStatement(returnSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            returnPS.setInt(1, processedByEmployeeId);
            returnPS.setString(2, refundMethod);
            returnPS.setBigDecimal(3, returnTotal);
            returnPS.executeUpdate();

            ResultSet rs = returnPS.getGeneratedKeys();
            rs.next();
            int returnId = rs.getInt(1);

            // 3️⃣ Insert return items
            String itemSQL = "INSERT INTO " + RETURN_ITEMS_TABLE_NAME + " ("
                    + RETURN_ITEMS_COLUMN_RETURN_ID + ", "
                    + RETURN_ITEMS_COLUMN_PRODUCT_ID + ", "
                    + RETURN_ITEMS_COLUMN_PRODUCT_QUANTITY + ", "
                    + RETURN_ITEMS_COLUMN_PRODUCT_UNIT_PRICE + ", "
                    + RETURN_ITEMS_COLUMN_TAX_AMOUNT + ", "
                    + RETURN_ITEMS_COLUMN_TOTAL_AMOUNT + ", "
                    + RETURN_ITEMS_COLUMN_RETURN_REASON + ") VALUES (?,?,?,?,?,?,?)";

            PreparedStatement itemPS = connection.prepareStatement(itemSQL);
            for (Cart_Item_DTO item : cartDTO.getReturnItem()) {
                itemPS.setInt(1, returnId);
                itemPS.setString(2, item.getProduct().getProduct_id());
                itemPS.setInt(3, -item.getQuantity()); // positive quantity in DB
                itemPS.setBigDecimal(4, item.getUnitPrice());
                itemPS.setBigDecimal(5, item.getTaxAmount());
                itemPS.setBigDecimal(6, item.getTotalAmount()); // total = subtotal + tax
                itemPS.setString(7, returnReason);
                itemPS.addBatch();
            }
            itemPS.executeBatch();

            // 4️⃣ Update stock
            String stockSQL = "UPDATE " + PRODUCT_TABLE_NAME + " SET quantity = quantity + ? WHERE product_id = ?";
            PreparedStatement stockPS = connection.prepareStatement(stockSQL);
            for (Cart_Item_DTO item : cartDTO.getReturnItem()) {
                stockPS.setInt(1, item.getQuantity());
                stockPS.setString(2, item.getProduct().getProduct_id());
                stockPS.addBatch();
            }
            stockPS.executeBatch();

            connection.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("SQL Exception in ReturnDAO: " + e.getMessage());
            return false;
        }
    }

    // Get all returns
    /**
     * Retrieves all recorded return transactions.
     *
     * <p>The result contains only high-level meta-data:
     * <ul>
     *     <li>Return ID</li>
     *     <li>Payment / refund method</li>
     *     <li>Total refund amount</li>
     *     <li>Return timestamp</li>
     * </ul>
     *
     * <p>Return items are not included in this query.
     *
     * @return A list of {@link Returns} objects sorted by newest first.
     *         Returns an empty list if no records exist or a SQL error occurs.
     */

    public List<Returns> getAllReturns() {
        List<Returns> returnsList = new ArrayList<>();
        String sql = "SELECT return_id, payment_method, total_amount, return_date FROM returns ORDER BY return_date DESC";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Returns r = new Returns();
                String paymentSTR = rs.getString("payment_method");
                PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentSTR);
                r.setReturnID(rs.getInt("return_id"));
                r.setPaymentMethod(paymentMethod);
                r.setTotalAmount(rs.getBigDecimal("total_amount"));
                r.setReturnDate(rs.getTimestamp("return_date"));
                returnsList.add(r);
            }

        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        return returnsList;
    }

    // Count by payment method
    /**
     * Counts how many returns were processed using a specific payment/refund method.
     *
     * @param paymentMethod name of the refund/payment method (must match DB values)
     *                      e.g. "CARD", "CASH", "STORE_CREDIT"
     *
     * @return total number of return records with that payment method.
     *         Returns 0 if none exist or if a SQL error occurs.
     */
    public int countByPaymentMethod(String paymentMethod) {
        String sql = "SELECT COUNT(*) FROM returns WHERE payment_method = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paymentMethod);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        return 0;
    }

}
