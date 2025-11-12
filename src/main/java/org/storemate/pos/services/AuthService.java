package org.storemate.pos.services;

import org.storemate.pos.dao.AuthDAO;
import org.storemate.pos.dto.EmployeeDTO;
import org.storemate.pos.models.Employee;
import org.mindrot.jbcrypt.BCrypt;
import org.storemate.pos.utils.Alerts;
import org.storemate.pos.utils.Session;

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

    // added login user
    public boolean loginUser(String username, String password) {
        Employee employee = authDAO.findByUsername(username);
        if (employee == null) {
            alerts.showErrorAlert("Login Error", "User not found");
            return false;
        }
// added Session for logged-in User
        if (BCrypt.checkpw(password, employee.getPassword())) {
            Session.getInstance().setCurrentUser(employee);
            System.out.println("User logged in: " + employee.getUsername() + " | Role: " + employee.getRole());
            return true;
        } else {
            alerts.showErrorAlert("Login Error", "Password not Correct");
            return false;
        }
    }

}
