package org.storemate.pos.services;

import org.storemate.pos.dao.AuthDAO;
import org.storemate.pos.dto.EmployeeDTO;
import org.storemate.pos.models.Employee;
import org.mindrot.jbcrypt.BCrypt;
import org.storemate.pos.utils.Alerts;

public class AuthService {
    private final AuthDAO authDAO;
    private Alerts alerts = new Alerts();


    public AuthService(AuthDAO authDAO){
        this.authDAO = authDAO;
    }
    public AuthService(){
        authDAO = new AuthDAO();
    }

    public boolean registerUser(
            String username,
            String email,
            String password,
            String roles
    ){
        Employee employee = authDAO.findByUsername(username);
        if (employee != null){
            System.out.println("User already exists");
            alerts.showErrorAlert("Registration Error", "User with username " + username + " already exists in database");
            return false;
        }
        String hash_pass = BCrypt.hashpw(password, BCrypt.gensalt());

        EmployeeDTO employeeToRegister = new EmployeeDTO(
                username,
                email,
                hash_pass,
                roles
        );
        authDAO.register(employeeToRegister);
        return true;
    }

}
