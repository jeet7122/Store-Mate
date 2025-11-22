package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.dto.SalesReportDTO;
import org.storemate.pos.utils.Mappers;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.storemate.pos.database.TableConstants.*;

public class SalesReportDAO {
    public List<SalesReportDTO> getSalesReport(
            int employeeID, String productID, LocalDate startDate, LocalDate endDate
    ){
        List<SalesReportDTO> salesReportList = new ArrayList<>();
        Mappers mappers = new Mappers();

        try
                (
                        Connection connection = DatabaseConnector.getConnection()
                        )
        {
            StringBuilder sql = new StringBuilder(
                    "SELECT"
                            + "s." + SALE_COLUMN_ID + ", "
                            + "s." + SALE_COLUMN_EMPLOYEE_ID + ", "
                            + "e." + EMPLOYEE_COLUMN_USER_NAME + ", "
                            + "si." + SALE_ITEM_PRODUCT_ID + ", "
                            + "si." + SALE_ITEM_QUANTITY + ", "
                            + "p." + PRODUCT_COLUMN_NAME + ", "
                            + "si." + SALE_ITEM_UNIT_PRICE + ", "
                            + "s." + SALE_COLUMN_TOTAL_AMOUNT + ", "
                            + "s." + SALE_COLUMN_PAYMENT_METHOD + ", "
                            + "s." + SALE_COLUMN_SALE_DATE_AND_TIME + " "
                            + "FROM " + SALE_TABLE_NAME + " s "
                            + "JOIN " + SALE_ITEM_TABLE_NAME + " si ON s." + SALE_COLUMN_ID + " = si." + SALE_ITEM_SALE_ID
                            +  " JOIN " + PRODUCT_TABLE_NAME + " p ON si." + SALE_ITEM_PRODUCT_ID + " = p." + PRODUCT_COLUMN_ID
                            + " JOIN " + EMPLOYEE_TABLE_NAME + " e ON s." + SALE_COLUMN_EMPLOYEE_ID + " = p." + EMPLOYEE_COLUMN_ID
                            + " WHERE 1 = 1 "
            );


            if (employeeID != 0) sql.append(" AND s." + EMPLOYEE_COLUMN_ID + " = ? " );
            if(productID != null && productID.isBlank()) sql.append(" AND si." + PRODUCT_COLUMN_ID + " = ? ");
            if (startDate != null) sql.append(" AND s." + SALE_COLUMN_SALE_DATE_AND_TIME + " >= ? " );
            if (startDate != null) sql.append(" AND s." + SALE_COLUMN_SALE_DATE_AND_TIME + " <= ? " );

            PreparedStatement ps = connection.prepareStatement(sql.toString());
            int index = 1;

            if (employeeID != 0) ps.setInt(index++, employeeID);
            if (productID != null && productID.isBlank()) ps.setString(index++, productID);
            if (startDate != null){
                ps.setTimestamp(index++, Timestamp.valueOf(startDate.atStartOfDay()));
            }
            if (endDate != null)
            {
                ps.setTimestamp(index++, Timestamp.valueOf(endDate.atTime(23, 59, 59)));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                salesReportList.add(mappers.mapRow(rs));
            }

        }
        catch (SQLException ex){
            System.out.println("SQL EXCEPTION: " + ex.getMessage());
        }
        catch (Exception ex){
            System.out.println("EXCEPTION: " + ex.getMessage());
        }
        return salesReportList;
    }

    public List<SalesReportDTO> getPaginatedSales(int limit, int offset) {
        List<SalesReportDTO> list = new ArrayList<>();
        Mappers mappers = new Mappers();

        String sql = """
        SELECT 
            s.sale_id, 
            s.employee_id, 
            e.username,
            si.product_id, 
            p.product_name,
            si.quantity, 
            si.unit_price,
            s.total_amount,
            s.payment_method,
            s.sale_date_and_time
        FROM sales s
        JOIN sale_items si ON s.sale_id = si.sale_id
        JOIN products p ON si.product_id = p.product_id
        JOIN employees e ON s.employee_id = e.employee_id
        ORDER BY s.sale_date_and_time DESC
        LIMIT ? OFFSET ?
    """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mappers.mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<SalesReportDTO> filterSales(
            Integer employeeID,
            String productID,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<SalesReportDTO> list = new ArrayList<>();
        Mappers mappers = new Mappers();

        StringBuilder sql = new StringBuilder("""
        SELECT 
            s.sale_id, 
            s.employee_id, 
            e.username,
            si.product_id, 
            p.product_name,
            si.quantity, 
            si.unit_price,
            s.total_amount,
            s.payment_method,
            s.sale_date_and_time
        FROM sales s
        JOIN sale_items si ON s.sale_id = si.sale_id
        JOIN products p ON si.product_id = p.product_id
        JOIN employees e ON s.employee_id = e.employee_id
        WHERE 1 = 1
    """);

        if (employeeID != null && employeeID != 0)
            sql.append(" AND s.").append(SALE_COLUMN_EMPLOYEE_ID).append(" = ? ");

        if (productID != null && !productID.isBlank())
            sql.append(" AND si.").append(SALE_ITEM_PRODUCT_ID).append(" = ? ");

        if (startDate != null)
            sql.append(" AND s.sale_date_and_time >= ? ");

        if (endDate != null)
            sql.append(" AND s.sale_date_and_time <= ? ");

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (employeeID != null && employeeID != 0)
                ps.setInt(index++, employeeID);

            if (productID != null && !productID.isBlank())
                ps.setString(index++, productID);

            if (startDate != null)
                ps.setTimestamp(index++, Timestamp.valueOf(startDate.atStartOfDay()));

            if (endDate != null)
                ps.setTimestamp(index++, Timestamp.valueOf(endDate.atTime(23, 59, 59)));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mappers.mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


}
