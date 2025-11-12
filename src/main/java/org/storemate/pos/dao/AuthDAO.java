package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.dto.EmployeeDTO;
import org.storemate.pos.models.Employee;
import org.storemate.pos.utils.Mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
