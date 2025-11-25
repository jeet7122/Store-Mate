package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionsDAO {
    private static final String SELECT_ALL_TRANSACTIONS = """
        SELECT id, type, payment_method, total_amount, date
        FROM transactions_view
        ORDER BY date DESC
        """;

    /**
     * Fetch all transactions (sales, purchases, returns) from the unified view.
     * @return List of Transaction objects
     */
    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_TRANSACTIONS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getString("id"));
                t.setType(rs.getString("type"));
                t.setPaymentMethod(rs.getString("payment_method"));
                t.setAmount(rs.getBigDecimal("total_amount"));
                t.setDate(rs.getTimestamp("date"));
                list.add(t);
            }

        } catch (SQLException e) {
            System.err.println("SQL EXCEPTION in TransactionDAO: " + e.getMessage());
        }

        return list;
    }
}
