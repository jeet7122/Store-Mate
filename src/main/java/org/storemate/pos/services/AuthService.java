package org.storemate.pos.services;

import org.storemate.pos.dao.AuthDAO;
import org.storemate.pos.dao.ShiftDAO;
import org.storemate.pos.dto.EmployeeDTO;
import org.storemate.pos.dto.SecurityAnswersDTO;
import org.storemate.pos.models.Employee;
import org.mindrot.jbcrypt.BCrypt;
import org.storemate.pos.routing.Router;
import org.storemate.pos.utils.Alerts;
import org.storemate.pos.utils.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthService {
    private final AuthDAO authDAO;
    private Alerts alerts = new Alerts();


    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public AuthService() {
        authDAO = new AuthDAO();
    }

    public boolean registerUser(
            String username,
            String email,
            String password,
            String roles,
            int ques1ID,
            int ques2ID,
            int ques3ID,
            String ans1,
            String ans2,
            String ans3
    ) {
        Employee employee = authDAO.findByUsername(username);
        if (employee != null) {
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

        SecurityAnswersDTO answersDTO1 = new SecurityAnswersDTO(ques1ID, ans1);
        SecurityAnswersDTO answersDTO2 = new SecurityAnswersDTO(ques2ID, ans2);
        SecurityAnswersDTO answersDTO3 = new SecurityAnswersDTO(ques3ID, ans3);
        SecurityAnswersDTO[] securityAnswersArray = {answersDTO1, answersDTO2, answersDTO3};

        authDAO.register(employeeToRegister, securityAnswersArray);
        return true;
    }

    // added login user
    public void loginUser(String username, String password) {
        Employee employee = authDAO.findByUsername(username);
        if (employee == null) {
            alerts.showErrorAlert("Login Error", "User not found");
            return;
        }
// added Session for logged-in User
        if (BCrypt.checkpw(password, employee.getPassword())) {
            Session.getInstance().setCurrentUser(employee);
            /* AS SOON AS EMPLOYEE LOGS IN SHIFT TABLE GETS UPDATED */
            ShiftDAO shiftDAO = new ShiftDAO();
            shiftDAO.startShift(employee.getEmployeeID());

            System.out.println("User logged in: " + employee.getUsername() + " | Role: " + employee.getRole());
            alerts.showSuccessAlert("Login Successful", "User logged in: " + employee.getUsername() + " | Role: " + employee.getRole());
        } else {
            alerts.showErrorAlert("Login Error", "Incorrect Password");
        }
    }

    public void logout() {
        Employee employee = Session.getInstance().getCurrentUser();
        if (employee != null) {
            ShiftDAO dao = new ShiftDAO();
            dao.endShift(employee.getEmployeeID());
            Session.getInstance().logout();
            alerts.showSuccessAlert("Logout Success", "User logged out successfully");
            Router.navigate("/login");
        }
    }

    // In your AuthService class, add these methods:
    public Employee findByUsername(String username) {
        try {
            return authDAO.findByUsername(username);
        } catch (Exception e) {
            System.out.println("Find employee error: " + e.getMessage());
            return null;
        }
    }

    public boolean updateEmployee(String username, String email, String role) {
        try {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setUsername(username);
            employeeDTO.setEmail(email);
            employeeDTO.setRole(role);

            authDAO.updateEmployee(employeeDTO);
            return true;
        } catch (Exception e) {
            System.out.println("Update employee error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEmployee(String username) {
        try {
            authDAO.deleteEmployee(username);
            return true;
        } catch (Exception e) {
            System.out.println("Delete employee error: " + e.getMessage());
            return false;
        }
    }
    public List<Employee> getAllEmployees() {
        try {
            return authDAO.getAllEmployees();
        } catch (Exception e) {
            System.out.println("Get all employees error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public int getTotalEmployeesCount() {
        try {
            return authDAO.getTotalEmployeesCount();
        } catch (Exception e) {
            System.out.println("Get total employees count error: " + e.getMessage());
            return 0;
        }
    }

    public int getActiveEmployeesToday() {
        try {
            return authDAO.getActiveEmployeesToday();
        } catch (Exception e) {
            System.out.println("Get active employees error: " + e.getMessage());
            return 0;
        }
    }

    public int getNewEmployeesThisMonth() {
        try {
            return authDAO.getNewEmployeesThisMonth();
        } catch (Exception e) {
            System.out.println("Get new employees this month error: " + e.getMessage());
            return 0;
        }
    }

    public Map<String, Integer> getRoleDistribution() {
        try {
            return authDAO.getRoleDistribution();
        } catch (Exception e) {
            System.out.println("Get role distribution error: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public Map<String, Integer> getMonthlyJoinStats() {
        try {
            return authDAO.getMonthlyJoinStats();
        } catch (Exception e) {
            System.out.println("Get monthly join stats error: " + e.getMessage());
            return new HashMap<>();
        }
    }
    public boolean credentialsExists(String username, String password){
        Employee employee = authDAO.findByUsername(username);
        if (employee == null) {
            alerts.showErrorAlert("Login Error", "User not found");
            return false;
        }
        return BCrypt.checkpw(password, employee.getPassword());
    }

    public boolean updatePass(int employeeID, String passwordText) {
        String hash_pass = BCrypt.hashpw(passwordText, BCrypt.gensalt());
        return authDAO.updatePass(employeeID, hash_pass);
    }

    public boolean updateUsername(String username, int employeeID) {
        Employee employee = findByUsername(username);
        if (employee != null){
            alerts.showErrorAlert("Error", "Username already exists");
            return false;
        }
        return authDAO.updateUsername(username,employeeID);
    }
}
