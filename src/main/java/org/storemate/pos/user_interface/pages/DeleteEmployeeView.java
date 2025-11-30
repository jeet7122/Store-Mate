package org.storemate.pos.user_interface.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.storemate.pos.models.Employee;
import org.storemate.pos.routing.Router;
import org.storemate.pos.services.AuthService;
import org.storemate.pos.utils.Alerts;

public class DeleteEmployeeView {
    private final VBox root;
    private final AuthService authService;
    private final Alerts alerts;
    private TextField usernameTextField;
    private TextField emailTextField;
    private TextField roleTextField;
    private Button searchButton;
    private Button deleteButton;
    private Button cancelButton;
    private Employee currentEmployee;

    public DeleteEmployeeView() {
        authService = new AuthService();
        alerts = new Alerts();
        root = new VBox(10);
        initializeView();
    }

    private void initializeView() {
        GridPane formGrid = new GridPane();

        // Search section
        Label searchTitle = new Label("Search Employee to Delete");
        searchTitle.getStyleClass().add("label-subtitle");

        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Enter username to search");

        searchButton = new Button("Search");
        searchButton.getStyleClass().addAll("button", "button-search");

        HBox searchBox = new HBox(10);
        searchBox.getChildren().addAll(searchTextField, searchButton);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        // Display fields (read-only)
        usernameTextField = new TextField();
        usernameTextField.setPromptText("username");
        usernameTextField.setDisable(true);

        emailTextField = new TextField();
        emailTextField.setPromptText("email");
        emailTextField.setDisable(true);

        roleTextField = new TextField();
        roleTextField.setPromptText("role");
        roleTextField.setDisable(true);

        // Warning message
        Label warningLabel = new Label("⚠️ WARNING: This action cannot be undone. All employee data will be permanently deleted.");
        warningLabel.getStyleClass().add("warning-label");
        warningLabel.setWrapText(true);

        Label formTitle = new Label("Delete Employee");
        formTitle.getStyleClass().add("label-title");

        HBox topBar = new HBox(50);
        topBar.setPadding(new Insets(10, 0, 20, 0));
        topBar.setAlignment(Pos.CENTER_LEFT);

        FontIcon backIcon = new FontIcon(MaterialDesignA.ARROW_LEFT_BOLD_OUTLINE);
        backIcon.setIconSize(30);
        backIcon.setIconColor(Color.DARKCYAN);
        Label backButton = new Label("", backIcon);
        backButton.getStyleClass().add("back-button"); // your CSS class
        backButton.setOnMouseClicked(e -> {
            // Handle navigation (replace this with your page switching logic)
            System.out.println("Back clicked!");
            Router.navigate("/employees");
        });
        topBar.getChildren().addAll(backButton, formTitle);
        // Buttons
        HBox buttonBox = new HBox(10);
        deleteButton = new Button("Delete Employee");
        deleteButton.getStyleClass().addAll("button", "button-delete");
        deleteButton.setDisable(true);

        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().addAll("button", "button-cancel");
        cancelButton.setOnAction(e -> Router.navigate("/employees"));

        buttonBox.getChildren().addAll(deleteButton, cancelButton);
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
                    deleteButton.setDisable(false);
                    alerts.showSuccessAlert("Employee Found",
                            "Employee found. Please review the details before deletion.\n\n" +
                                    "Username: " + currentEmployee.getUsername() + "\n" +
                                    "Email: " + currentEmployee.getEmail() + "\n" +
                                    "Role: " + currentEmployee.getRole() + "\n\n" +
                                    "This action cannot be undone!");
                } else {
                    alerts.showErrorAlert("Not Found", "No employee found with username: " + username);
                    clearForm();
                    deleteButton.setDisable(true);
                }
            } catch (Exception ex) {
                alerts.showErrorAlert("Search Error", "An error occurred while searching: " + ex.getMessage());
                System.out.println("Search Exception: " + ex.getMessage());
                clearForm();
                deleteButton.setDisable(true);
            }
        });

        // Delete button action
        deleteButton.setOnAction(e -> {
            if (currentEmployee == null) {
                alerts.showErrorAlert("Delete Error", "No employee selected for deletion.");
                return;
            }

            // Confirmation dialog
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Permanent Employee Deletion");
            confirmationAlert.setContentText(
                    "Are you absolutely sure you want to delete this employee?\n\n" +
                            "Username: " + currentEmployee.getUsername() + "\n" +
                            "Email: " + currentEmployee.getEmail() + "\n" +
                            "Role: " + currentEmployee.getRole() + "\n\n" +
                            "This action cannot be undone!"
            );

            ButtonType yesButton = new ButtonType("Yes, Delete");
            ButtonType noButton = new ButtonType("Cancel");
            confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == yesButton) {
                    performDeletion();
                }
            });
        });

        // Layout
        formGrid.add(searchTitle, 0, 0);
        formGrid.add(searchBox, 1, 0);
        formGrid.add(new Label("Username:"), 0, 1);
        formGrid.add(usernameTextField, 1, 1);
        formGrid.add(new Label("Email:"), 0, 2);
        formGrid.add(emailTextField, 1, 2);
        formGrid.add(new Label("Role:"), 0, 3);
        formGrid.add(roleTextField, 1, 3);
        formGrid.add(warningLabel, 0, 4, 2, 1);
        formGrid.add(buttonBox, 0, 5, 2, 1);

        formGrid.setVgap(20);
        formGrid.setHgap(10);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.getStyleClass().add("form-grid");

        root.getChildren().addAll(topBar, formGrid);
        topBar.setAlignment(Pos.TOP_CENTER);
        root.setAlignment(Pos.CENTER);
    }

    private void populateForm(Employee employee) {
        usernameTextField.setText(employee.getUsername());
        emailTextField.setText(employee.getEmail());
        roleTextField.setText(String.valueOf(employee.getRole()));
    }

    private void clearForm() {
        usernameTextField.clear();
        emailTextField.clear();
        roleTextField.clear();
    }

    private void performDeletion() {
        try {
            boolean deleted = authService.deleteEmployee(currentEmployee.getUsername());

            if (deleted) {
                alerts.showSuccessAlert(
                        "Employee Deleted",
                        "Employee '" + currentEmployee.getUsername() + "' has been successfully deleted."
                );
                clearForm();
                currentEmployee = null;
                deleteButton.setDisable(true);

                // Optionally navigate back to employee list after successful deletion
                // Router.navigate("/employees");
            } else {
                alerts.showErrorAlert(
                        "Deletion Failed",
                        "Failed to delete employee. Please try again."
                );
            }
        } catch (Exception ex) {
            alerts.showErrorAlert("Deletion Error", "An error occurred: " + ex.getMessage());
            System.out.println("Deletion Exception: " + ex.getMessage());
        }
    }

    public Parent getView() {
        return root;
    }
}