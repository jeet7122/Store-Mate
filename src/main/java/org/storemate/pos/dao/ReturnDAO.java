package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.dto.CartDTO;
import org.storemate.pos.dto.Cart_Item_DTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.storemate.pos.database.TableConstants.*;

public class ReturnDAO {

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
                stockPS.setInt(1, -item.getQuantity());
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

}
