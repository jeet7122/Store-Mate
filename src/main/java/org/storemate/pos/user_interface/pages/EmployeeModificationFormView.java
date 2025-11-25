package org.storemate.pos.user_interface.pages;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.models.Employee;
import org.storemate.pos.models.enums.Role;
import org.storemate.pos.services.AuthService;
import org.storemate.pos.utils.Alerts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeeModificationFormView {
    private final VBox root;
    private final AuthService authService;
    private final Alerts alerts;
    private TextField usernameTextField;
    private TextField emailTextField;
    private ComboBox<Role> roleBox;
    private Button searchButton;
    private Button updateButton;
    private Button resetButton;
    private Employee currentEmployee;

    public EmployeeModificationFormView() {
        authService = new AuthService();
        alerts = new Alerts();
        root = new VBox(10);
        initializeView();
    }

    private void initializeView() {
        GridPane formGrid = new GridPane();

        // Search section
        Label searchTitle = new Label("Search Employee");
        searchTitle.getStyleClass().add("label-subtitle");

        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Enter username to search");

        searchButton = new Button("Search");
        searchButton.getStyleClass().addAll("button", "button-search");

        HBox searchBox = new HBox(10);
        searchBox.getChildren().addAll(searchTextField, searchButton);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        // Form fields
        usernameTextField = new TextField();
        usernameTextField.setPromptText("username");
        usernameTextField.setDisable(true);

        emailTextField = new TextField();
        emailTextField.setPromptText("email");

        roleBox = new ComboBox<>();
        Label rolesLabel = new Label("Role");
        rolesLabel.getStyleClass().add("combo-label");
        List<Role> roles = new ArrayList<>(Arrays.asList(Role.values()));
        roleBox.setItems(FXCollections.observableList(roles));

        Label formTitle = new Label("Employee Modification Form");
        formTitle.getStyleClass().add("label-title");

        // Buttons
        HBox buttonBox = new HBox(10);
        updateButton = new Button("Update");
        updateButton.getStyleClass().addAll("button", "button-update");
        updateButton.setDisable(true);

        resetButton = new Button("Reset");
        resetButton.getStyleClass().addAll("button", "button-reset");
        resetButton.setDisable(true);

        buttonBox.getChildren().addAll(updateButton, resetButton);
        buttonBox.setSpacing(20);

        // Search button action
        searchButton.setOnAction(e -> {
            String username = searchTextField.getText().trim();
            if (username.isEmpty()) {
                alerts.showErrorAlert("Search Error", "Please enter a username to search.");
                return;
            }

            try {
                currentEmployee = authService.findByUsername(username);
                if (currentEmployee != null) {
                    populateForm(currentEmployee);
                    updateButton.setDisable(false);
                    resetButton.setDisable(false);
                    alerts.showSuccessAlert("Employee Found", "Employee data loaded successfully.");
                } else {
                    alerts.showErrorAlert("Not Found", "No employee found with username: " + username);
                    clearForm();
                }
            } catch (Exception ex) {
                alerts.showErrorAlert("Search Error", "An error occurred while searching: " + ex.getMessage());
                System.out.println("Search Exception: " + ex.getMessage());
            }
        });

        // Update button action
        updateButton.setOnAction(e -> {
            if (currentEmployee == null) {
                alerts.showErrorAlert("Update Error", "No employee selected for update.");
                return;
            }

            if (!validateForm()) {
                return;
            }

            try {
                String email = emailTextField.getText().trim();
                Role role = roleBox.getSelectionModel().getSelectedItem();

                boolean updated = authService.updateEmployee(
                        currentEmployee.getUsername(),
                        email,
                        role.toString()
                );

                if (updated) {
                    alerts.showSuccessAlert(
                            "Store-Mate Employee Update",
                            "Employee Updated Successfully"
                    );
                    clearForm();
                    searchTextField.clear();
                    currentEmployee = null;
                    updateButton.setDisable(true);
                    resetButton.setDisable(true);
                } else {
                    alerts.showErrorAlert(
                            "Update Failed",
                            "Failed to update employee. Please try again."
                    );
                }
            } catch (Exception ex) {
                alerts.showErrorAlert("Update Error", "An error occurred: " + ex.getMessage());
                System.out.println("Update Exception: " + ex.getMessage());
            }
        });

        // Reset button action
        resetButton.setOnAction(e -> {
            if (currentEmployee != null) {
                populateForm(currentEmployee); // Reset to original values
                alerts.showSuccessAlert("Form Reset", "Form reset to original values.");
            }
        });

        // Layout
        formGrid.add(searchTitle, 0, 0);
        formGrid.add(searchBox, 1, 0);
        formGrid.add(new Label("Username:"), 0, 1);
        formGrid.add(usernameTextField, 1, 1);
        formGrid.add(new Label("Email:"), 0, 2);
        formGrid.add(emailTextField, 1, 2);
        formGrid.add(rolesLabel, 0, 3);
        formGrid.add(roleBox, 1, 3);
        formGrid.add(buttonBox, 0, 4, 2, 1);

        formGrid.setVgap(20);
        formGrid.setHgap(10);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.getStyleClass().add("form-grid");

        root.getChildren().addAll(formTitle, formGrid);
        formTitle.setAlignment(Pos.TOP_CENTER);
        root.setAlignment(Pos.CENTER);
    }

    private void populateForm(Employee employee) {
        usernameTextField.setText(employee.getUsername());
        emailTextField.setText(employee.getEmail());

        try {
            Role role = employee.getRole();
            roleBox.setValue(role);
        } catch (IllegalArgumentException e) {
            roleBox.setValue(null);
            System.out.println("Invalid role value: " + employee.getRole());
        }
    }

    private void clearForm() {
        usernameTextField.clear();
        emailTextField.clear();
        roleBox.setValue(null);
    }

    private boolean validateForm() {
        String email = emailTextField.getText().trim();
        Role role = roleBox.getSelectionModel().getSelectedItem();

        if (email.isEmpty()) {
            alerts.showErrorAlert("Validation Error", "Email field cannot be empty.");
            return false;
        }

        if (role == null) {
            alerts.showErrorAlert("Validation Error", "Please select a role.");
            return false;
        }

        // Basic email validation
        if (!email.contains("@") || !email.contains(".")) {
            alerts.showErrorAlert("Validation Error", "Please enter a valid email address.");
            return false;
        }

        return true;
    }

    public Parent getView() {
        return root;
    }
}