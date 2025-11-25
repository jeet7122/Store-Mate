package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.dto.EmployeeDTO;
import org.storemate.pos.models.Employee;
import org.storemate.pos.utils.Mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.storemate.pos.database.TableConstants.*;

public class AuthDAO {
    private final Mappers mappers = new Mappers();

    public Employee findByUsername(String username) {
        String query = "SELECT * FROM " + EMPLOYEE_TABLE_NAME
                + " WHERE " + EMPLOYEE_COLUMN_USER_NAME + " = ?";

        try
                (
                        Connection connection = DatabaseConnector.getConnection();
                        PreparedStatement statement = connection.prepareStatement(query)

                )
        {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                EmployeeDTO existingEmployee = new EmployeeDTO();
                existingEmployee.setUsername(rs.getString(EMPLOYEE_COLUMN_USER_NAME));
                existingEmployee.setEmployeeID(rs.getInt(EMPLOYEE_COLUMN_ID));
                existingEmployee.setEmail(rs.getString(EMPLOYEE_COLUMN_EMAIL));
                existingEmployee.setRole(rs.getString(EMPLOYEE_COLUMN_ROLE));
                existingEmployee.setCreatedAt(rs.getTimestamp(EMPLOYEE_COLUMN_JOINING_DATE));
                existingEmployee.setUpdatedAt(rs.getTimestamp(EMPLOYEE_COLUMN_MODIFIED_PROFILE));
                existingEmployee.setPassword(rs.getString(EMPLOYEE_COLUMN_PASSWORD));
                return mappers.fromDTO(existingEmployee);
            }
        }
        catch (SQLException exception){
            System.out.println("SQL EXCEPTION: " + exception.getMessage());
        }
        catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }
        return null;
    }

    public void register(EmployeeDTO employeeToRegister) {
        String query = "INSERT INTO " + EMPLOYEE_TABLE_NAME
                + " ( " + EMPLOYEE_COLUMN_USER_NAME + ", " + EMPLOYEE_COLUMN_EMAIL + ", " + EMPLOYEE_COLUMN_PASSWORD
                + ", " + EMPLOYEE_COLUMN_ROLE + " ) VALUES (?,?,?,?)";

        try
                (
                        Connection connection = DatabaseConnector.getConnection();
                        PreparedStatement statement = connection.prepareStatement(query)
                        )

        {
            statement.setString(1, employeeToRegister.getUsername());
            statement.setString(2, employeeToRegister.getEmail());
            statement.setString(3, employeeToRegister.getPassword());
            statement.setString(4, employeeToRegister.getRole());
            int rows = statement.executeUpdate();
            if (rows > 0){
                System.out.println("Employee registered successfully");
            }
            else {
                System.out.println("Error occurred while registration");
            }

        }
        catch (SQLException ex){
            System.out.println("SQL EXCEPTION: " + ex.getMessage());
        }
        catch (Exception ex){
            System.out.println("EXCEPTION: " + ex.getMessage());
        }
    }

    public void updateEmployee(EmployeeDTO employeeToUpdate) {
        String query = "UPDATE " + EMPLOYEE_TABLE_NAME
                + " SET " + EMPLOYEE_COLUMN_EMAIL + " = ?, "
                + EMPLOYEE_COLUMN_ROLE + " = ?, "
                + EMPLOYEE_COLUMN_MODIFIED_PROFILE + " = CURRENT_TIMESTAMP"
                + " WHERE " + EMPLOYEE_COLUMN_USER_NAME + " = ?";

        try (
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, employeeToUpdate.getEmail());
            statement.setString(2, employeeToUpdate.getRole());
            statement.setString(3, employeeToUpdate.getUsername());

            int rows = statement.executeUpdate();
            if (rows > 0) {
                System.out.println("Employee updated successfully");
            } else {
                System.out.println("No employee found with the given username");
            }
        } catch (SQLException ex) {
            System.out.println("SQL EXCEPTION: " + ex.getMessage());
            throw new RuntimeException("Failed to update employee", ex);
        } catch (Exception ex) {
            System.out.println("EXCEPTION: " + ex.getMessage());
            throw new RuntimeException("Failed to update employee", ex);
        }
    }

    public void deleteEmployee(String username) {
        String query = "DELETE FROM " + EMPLOYEE_TABLE_NAME
                + " WHERE " + EMPLOYEE_COLUMN_USER_NAME + " = ?";

        try (
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, username);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                System.out.println("Employee deleted successfully: " + username);
            } else {
                System.out.println("No employee found with username: " + username);
                throw new RuntimeException("Employee not found");
            }
        } catch (SQLException ex) {
            System.out.println("SQL EXCEPTION: " + ex.getMessage());
            throw new RuntimeException("Failed to delete employee", ex);
        } catch (Exception ex) {
            System.out.println("EXCEPTION: " + ex.getMessage());
            throw new RuntimeException("Failed to delete employee", ex);
        }
    }

    public List<Employee> getAllEmployees() {
        String query = "SELECT * FROM " + EMPLOYEE_TABLE_NAME + " ORDER BY " + EMPLOYEE_COLUMN_JOINING_DATE + " DESC";
        List<Employee> employees = new ArrayList<>();

        try (
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery()
        ) {
            while(rs.next()) {
                EmployeeDTO employeeDTO = new EmployeeDTO();
                employeeDTO.setUsername(rs.getString(EMPLOYEE_COLUMN_USER_NAME));
                employeeDTO.setEmployeeID(rs.getInt(EMPLOYEE_COLUMN_ID));
                employeeDTO.setEmail(rs.getString(EMPLOYEE_COLUMN_EMAIL));
                employeeDTO.setRole(rs.getString(EMPLOYEE_COLUMN_ROLE));
                employeeDTO.setCreatedAt(rs.getTimestamp(EMPLOYEE_COLUMN_JOINING_DATE));
                employeeDTO.setUpdatedAt(rs.getTimestamp(EMPLOYEE_COLUMN_MODIFIED_PROFILE));

                employees.add(mappers.fromDTO(employeeDTO));
            }
        } catch (SQLException exception) {
            System.out.println("SQL EXCEPTION: " + exception.getMessage());
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return employees;
    }

    public int getTotalEmployeesCount() {
        String query = "SELECT COUNT(*) as total FROM " + EMPLOYEE_TABLE_NAME;

        try (
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException exception) {
            System.out.println("SQL EXCEPTION: " + exception.getMessage());
        }
        return 0;
    }

    public int getActiveEmployeesToday() {
        String query = "SELECT COUNT(DISTINCT " + SHIFT_COLUMN_EMPLOYEE_ID + ") as active_today " +
                "FROM " + SHIFT_TABLE_NAME + " " +
                "WHERE DATE(" + SHIFT_COLUMN_LOGIN_TIME + ") = CURDATE()";

        try (
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt("active_today");
            }
        } catch (SQLException exception) {
            System.out.println("SQL EXCEPTION: " + exception.getMessage());
        }
        return 0;
    }

    public int getNewEmployeesThisMonth() {
        String query = "SELECT COUNT(*) as new_this_month FROM " + EMPLOYEE_TABLE_NAME + " " +
                "WHERE MONTH(" + EMPLOYEE_COLUMN_JOINING_DATE + ") = MONTH(CURRENT_DATE()) " +
                "AND YEAR(" + EMPLOYEE_COLUMN_JOINING_DATE + ") = YEAR(CURRENT_DATE())";

        try (
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt("new_this_month");
            }
        } catch (SQLException exception) {
            System.out.println("SQL EXCEPTION: " + exception.getMessage());
        }
        return 0;
    }

    public Map<String, Integer> getRoleDistribution() {
        String query = "SELECT " + EMPLOYEE_COLUMN_ROLE + ", COUNT(*) as count " +
                "FROM " + EMPLOYEE_TABLE_NAME + " " +
                "GROUP BY " + EMPLOYEE_COLUMN_ROLE;
        Map<String, Integer> roleDistribution = new HashMap<>();

        try (
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery()
        ) {
            while(rs.next()) {
                roleDistribution.put(rs.getString(EMPLOYEE_COLUMN_ROLE), rs.getInt("count"));
            }
        } catch (SQLException exception) {
            System.out.println("SQL EXCEPTION: " + exception.getMessage());
        }
        return roleDistribution;
    }

    public Map<String, Integer> getMonthlyJoinStats() {
        String query = "SELECT DATE_FORMAT(" + EMPLOYEE_COLUMN_JOINING_DATE + ", '%Y-%m') as month, " +
                "COUNT(*) as count " +
                "FROM " + EMPLOYEE_TABLE_NAME + " " +
                "WHERE " + EMPLOYEE_COLUMN_JOINING_DATE + " >= DATE_SUB(CURRENT_DATE, INTERVAL 6 MONTH) " +
                "GROUP BY DATE_FORMAT(" + EMPLOYEE_COLUMN_JOINING_DATE + ", '%Y-%m') " +
                "ORDER BY month DESC LIMIT 6";
        Map<String, Integer> monthlyStats = new LinkedHashMap<>();

        try (
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery()
        ) {
            while(rs.next()) {
                monthlyStats.put(rs.getString("month"), rs.getInt("count"));
            }
        } catch (SQLException exception) {
            System.out.println("SQL EXCEPTION: " + exception.getMessage());
        }
        return monthlyStats;
    }
}
