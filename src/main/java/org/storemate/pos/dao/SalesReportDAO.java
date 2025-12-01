package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.dto.SalesReportDTO;
import org.storemate.pos.utils.Mappers;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.storemate.pos.database.TableConstants.*;

/**
 * DAO class for fetching and filtering sales reports from the database.
 *
 * <p>This class provides methods to:
 * <ul>
 *     <li>Retrieve a sales report based on filters such as employee, product, and date range</li>
 *     <li>Fetch paginated sales data for UI tables or reports</li>
 *     <li>Filter sales dynamically with optional parameters</li>
 * </ul>
 *
 * <p>All queries use JDBC and rely on the {@link Mappers} utility class
 * to map {@link ResultSet} rows to {@link SalesReportDTO} objects.
 * @author Jeet
 */
public class SalesReportDAO {

    /**
     * Fetches a list of sales report entries based on multiple optional filters.
     *
     * <p>Filters include:
     * <ul>
     *     <li>{@code employeeID} – filter by employee (0 means no filter)</li>
     *     <li>{@code productID} – filter by product (null or blank means no filter)</li>
     *     <li>{@code startDate} – filter sales on or after this date</li>
     *     <li>{@code endDate} – filter sales on or before this date</li>
     * </ul>
     *
     * @param employeeID the employee ID to filter by (0 for no filter)
     * @param productID the product ID to filter by (null or blank for no filter)
     * @param startDate start date for filtering sales (inclusive)
     * @param endDate end date for filtering sales (inclusive)
     *
     * @return list of {@link SalesReportDTO} objects matching the filters;
     *         returns an empty list if no results or on error.
     */
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

    /**
     * Retrieves paginated sales entries from the database.
     *
     * <p>Useful for displaying sales in UI tables with pagination.
     *
     * @param limit  maximum number of rows to return
     * @param offset number of rows to skip
     *
     * @return list of {@link SalesReportDTO} objects for the specified page;
     *         returns an empty list on SQL error or no data.
     */
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
            s.sale_date
        FROM sales s
        JOIN sale_items si ON s.sale_id = si.sale_id
        JOIN products p ON si.product_id = p.product_id
        JOIN employees e ON s.employee_id = e.employee_id
        ORDER BY s.sale_date DESC
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

    /**
     * Dynamically filters sales based on optional parameters.
     *
     * <p>Supported filters:
     * <ul>
     *     <li>{@code employeeID} – employee ID to filter by (null or 0 = no filter)</li>
     *     <li>{@code productID} – product ID to filter by (null or blank = no filter)</li>
     *     <li>{@code startDate} – start of date range (inclusive)</li>
     *     <li>{@code endDate} – end of date range (inclusive)</li>
     * </ul>
     *
     * <p>Uses {@link Mappers} to convert {@link ResultSet} rows into {@link SalesReportDTO} objects.
     *
     * @param employeeID the employee ID to filter (nullable)
     * @param productID the product ID to filter (nullable or blank)
     * @param startDate start date for filtering sales (inclusive, nullable)
     * @param endDate end date for filtering sales (inclusive, nullable)
     *
     * @return list of {@link SalesReportDTO} objects matching the criteria;
     *         returns an empty list on SQL error or if no matches are found.
     */
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
            s.sale_date
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
            sql.append(" AND s.sale_date >= ? ");

        if (endDate != null)
            sql.append(" AND s.sale_date <= ? ");

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
