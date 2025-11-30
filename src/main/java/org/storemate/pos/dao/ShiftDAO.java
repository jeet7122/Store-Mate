package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.models.ShiftSummary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.storemate.pos.database.TableConstants.*;

public class ShiftDAO {

    public void startShift(int employeeID) {
        String sql = "INSERT INTO " + SHIFT_TABLE_NAME
                + " ( " + SHIFT_COLUMN_EMPLOYEE_ID + " ) VALUES (?); ";

        try
                (
                        Connection connection = DatabaseConnector.getConnection();
                        PreparedStatement statement = connection.prepareStatement(sql)
                        )
        {
            statement.setInt(1, employeeID);
            int rows = statement.executeUpdate();
            if (rows > 0){
                System.out.println("Shift Logged Successfully");
            }
            else {
                System.out.println("Unknown error");
            }
        }
        catch (SQLException ex){
            System.out.println("SQL EXCEPTION: " + ex.getMessage());
        }
        catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    public void endShift(int employeeID) {
        String sql = "UPDATE " + SHIFT_TABLE_NAME
                + " SET " + SHIFT_COLUMN_LOGOUT_TIME + " = CURRENT_TIMESTAMP "
                + "WHERE " + SHIFT_COLUMN_EMPLOYEE_ID + " = ? "
                + "AND " + SHIFT_COLUMN_LOGOUT_TIME + " IS NULL "
                + "ORDER BY " + SHIFT_COLUMN_LOGIN_TIME + " DESC "
                + "LIMIT 1";

        try (
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, employeeID);
            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("SHIFT LOGOUT Success");
            } else {
                System.out.println("SHIFT LOGOUT Error (no open session found)");
            }

        } catch (SQLException ex) {
            System.out.println("SQL EXCEPTION: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("EXCEPTION: " + ex.getMessage());
        }
    }

    public Map<String, Integer> getWeeklyHours() {
        String sql = """
            SELECT e.username, SUM(TIMESTAMPDIFF(HOUR, s.login_time, s.logout_time)) AS hours
            FROM shift s
            JOIN employees e ON e.employee_id = s.employee_id
            WHERE WEEK(s.login_time) = WEEK(CURDATE())
            GROUP BY e.username
        """;

        Map<String, Integer> map = new LinkedHashMap<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getString("username"), rs.getInt("hours"));
            }

        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        return map;
    }

    public List<ShiftSummary> getShiftSummary() {
        String sql = """
        SELECT e.employee_id, e.username,
               COALESCE(SUM(TIMESTAMPDIFF(HOUR, s.login_time, s.logout_time)), 0) AS weekly_hours
        FROM employees e
        LEFT JOIN shift s ON s.employee_id = e.employee_id
             AND WEEK(s.login_time) = WEEK(CURDATE())
        GROUP BY e.employee_id, e.username
        ORDER BY e.username;
    """;

        List<ShiftSummary> list = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new ShiftSummary(
                        rs.getInt("employee_id"),
                        rs.getString("username"),
                        rs.getInt("weekly_hours")
                ));
            }

        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }

        return list;
    }

}
