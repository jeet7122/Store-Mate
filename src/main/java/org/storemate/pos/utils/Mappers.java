package org.storemate.pos.utils;

import org.storemate.pos.dto.EmployeeDTO;
import org.storemate.pos.models.Employee;
import org.storemate.pos.models.enums.Role;

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
}
