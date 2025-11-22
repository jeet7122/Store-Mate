package org.storemate.pos.utils;

import org.storemate.pos.dto.EmployeeDTO;
import org.storemate.pos.dto.SalesReportDTO;
import org.storemate.pos.models.Employee;
import org.storemate.pos.models.enums.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.storemate.pos.database.TableConstants.*;

public class Mappers {

    public Employee fromDTO(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        employee.setEmployeeID(employeeDTO.getEmployeeID());
        employee.setEmail(employeeDTO.getEmail());
        employee.setUsername(employeeDTO.getUsername());
        employee.setRole(Role.valueOf(employeeDTO.getRole()));
        employee.setCreatedAt(employeeDTO.getCreatedAt());
        employee.setUpdatedAt(employeeDTO.getUpdatedAt());
        employee.setPassword(employeeDTO.getPassword());
        return employee;
    }
    public SalesReportDTO mapRow(ResultSet rs) throws SQLException {
        SalesReportDTO dto = new SalesReportDTO();
        dto.setSaleID(rs.getInt(SALE_COLUMN_ID));
        dto.setEmployeeID(rs.getInt(EMPLOYEE_COLUMN_ID));
        dto.setEmployeeName(rs.getString(EMPLOYEE_COLUMN_USER_NAME));
        dto.setProductID(rs.getString(PRODUCT_COLUMN_ID));
        dto.setProductName(rs.getString(PRODUCT_COLUMN_NAME));
        dto.setQuantity(rs.getInt(SALE_ITEM_QUANTITY));
        dto.setUnitPrice(rs.getDouble(SALE_ITEM_UNIT_PRICE));
        dto.setSaleTotal(rs.getDouble(SALE_COLUMN_TOTAL_AMOUNT));
        dto.setPaymentMethod(rs.getString(SALE_COLUMN_PAYMENT_METHOD));
        dto.setSaleDateAndTime(rs.getTimestamp(SALE_COLUMN_SALE_DATE_AND_TIME));
        return dto;
    }
}
