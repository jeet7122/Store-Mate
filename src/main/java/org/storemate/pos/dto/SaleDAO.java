package org.storemate.pos.dto;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.models.enums.SaleStatus;
import org.storemate.pos.models.enums.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

            salePS.setInt(1, cartDTO.getEmployeeId());
            salePS.setBigDecimal(2, cartDTO.getSubtotal());
            salePS.setBigDecimal(3, cartDTO.getTotalTax());
            salePS.setBigDecimal(4, cartDTO.getDiscount());
            salePS.setBigDecimal(5, cartDTO.getTotalAmount());
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

            for (Cart_Item_DTO cartItem : cartDTO.getCartItems()) {
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
            connection.commit();

            return true;

        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        return false;
    }


}
