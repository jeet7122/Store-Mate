package org.storemate.pos.services;

import org.storemate.pos.dao.AuthDAO;
import org.storemate.pos.dao.ShiftDAO;
import org.storemate.pos.dto.EmployeeDTO;
import org.storemate.pos.dto.SecurityAnswersDTO;
import org.storemate.pos.models.Employee;
import org.mindrot.jbcrypt.BCrypt;
import org.storemate.pos.utils.Alerts;
import org.storemate.pos.utils.SceneManager;
import org.storemate.pos.utils.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class responsible for handling authentication, user registration, login, logout,
 * and employee management operations.
 * <p>
 * This class interacts with {@link AuthDAO} to perform database operations and handles
 * security-related tasks such as password hashing using BCrypt and session management.
 * </p>
 * @author Khushi, Jeet, Billen
 */
public class AuthService {
    private final AuthDAO authDAO;
    private Alerts alerts = new Alerts();


    /**
     * Creates an AuthService with a provided {@link AuthDAO} instance.
     *
     * @param authDAO DAO instance used for database operations related to authentication
     */
    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    /**
     * Creates an AuthService with a default {@link AuthDAO} instance.
     */
    public AuthService() {
        authDAO = new AuthDAO();
    }

    /**
     * Registers a new employee with credentials and security questions.
     *
     * @param username the username of the employee
     * @param email the email address
     * @param password the plaintext password (will be hashed)
     * @param roles roles assigned to the employee
     * @param ques1ID ID of the first security question
     * @param ques2ID ID of the second security question
     * @param ques3ID ID of the third security question
     * @param ans1 answer to the first security question
     * @param ans2 answer to the second security question
     * @param ans3 answer to the third security question
     * @return true if registration is successful, false if user already exists
     */
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


    /**
     * Logs in a user by verifying credentials and starting a shift session.
     *
     * @param username the username of the employee
     * @param password the plaintext password
     */
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


    /**
     * Logs out the currently logged-in user and ends their shift session.
     */
    public void logout() {
        Employee employee = Session.getInstance().getCurrentUser();
        if (employee != null) {
            ShiftDAO dao = new ShiftDAO();
            dao.endShift(employee.getEmployeeID());
            Session.getInstance().logout();
            alerts.showSuccessAlert("Logout Success", "User logged out successfully");
            SceneManager.showEmployeeLoginForm();
        }
    }

    /**
     * Finds an employee by username.
     *
     * @param username the username to search
     * @return {@link Employee} if found, otherwise null
     */
    public Employee findByUsername(String username) {
        try {
            return authDAO.findByUsername(username);
        } catch (Exception e) {
            System.out.println("Find employee error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates employee information such as email and role.
     *
     * @param username the username of the employee
     * @param email the new email
     * @param role the new role
     * @return true if update succeeds, false otherwise
     */
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


    /**
     * Deletes an employee from the database.
     *
     * @param username the username of the employee to delete
     * @return true if deletion succeeds, false otherwise
     */
    public boolean deleteEmployee(String username) {
        try {
            authDAO.deleteEmployee(username);
            return true;
        } catch (Exception e) {
            System.out.println("Delete employee error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all employees from the database.
     *
     * @return list of {@link Employee} objects, empty if error occurs
     */
    public List<Employee> getAllEmployees() {
        try {
            return authDAO.getAllEmployees();
        } catch (Exception e) {
            System.out.println("Get all employees error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves the total number of employees.
     *
     * @return total count of employees
     */
    public int getTotalEmployeesCount() {
        try {
            return authDAO.getTotalEmployeesCount();
        } catch (Exception e) {
            System.out.println("Get total employees count error: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Retrieves the number of employees active today.
     *
     * @return count of active employees
     */
    public int getActiveEmployeesToday() {
        try {
            return authDAO.getActiveEmployeesToday();
        } catch (Exception e) {
            System.out.println("Get active employees error: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Retrieves the number of new employees joined this month.
     *
     * @return count of new employees this month
     */
    public int getNewEmployeesThisMonth() {
        try {
            return authDAO.getNewEmployeesThisMonth();
        } catch (Exception e) {
            System.out.println("Get new employees this month error: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Returns a map of roles to their distribution counts.
     *
     * @return map where key is role name and value is count
     */
    public Map<String, Integer> getRoleDistribution() {
        try {
            return authDAO.getRoleDistribution();
        } catch (Exception e) {
            System.out.println("Get role distribution error: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Returns monthly joining statistics of employees.
     *
     * @return map of month names to count of new employees
     */
    public Map<String, Integer> getMonthlyJoinStats() {
        try {
            return authDAO.getMonthlyJoinStats();
        } catch (Exception e) {
            System.out.println("Get monthly join stats error: " + e.getMessage());
            return new HashMap<>();
        }
    }
    /**
     * Checks if the provided username and password combination exists.
     *
     * @param username the username
     * @param password the plaintext password
     * @return true if credentials are valid, false otherwise
     */
    public boolean credentialsExists(String username, String password){
        Employee employee = authDAO.findByUsername(username);
        if (employee == null) {
            alerts.showErrorAlert("Login Error", "User not found");
            return false;
        }
        return BCrypt.checkpw(password, employee.getPassword());
    }

    /**
     * Updates an employee's password.
     *
     * @param employeeID ID of the employee
     * @param passwordText new plaintext password
     * @return true if update succeeds, false otherwise
     */
    public boolean updatePass(int employeeID, String passwordText) {
        String hash_pass = BCrypt.hashpw(passwordText, BCrypt.gensalt());
        return authDAO.updatePass(employeeID, hash_pass);
    }

    /**
     * Updates an employee's username.
     *
     * @param username new username
     * @param employeeID ID of the employee
     * @return true if update succeeds, false otherwise
     */
    public boolean updateUsername(String username, int employeeID) {
        Employee employee = findByUsername(username);
        if (employee != null){
            alerts.showErrorAlert("Error", "Username already exists");
            return false;
        }
        return authDAO.updateUsername(username,employeeID);
    }
}
