package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}
