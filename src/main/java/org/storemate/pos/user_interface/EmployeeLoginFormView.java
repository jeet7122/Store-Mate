package org.storemate.pos.user_interface;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.storemate.pos.routing.Router;
import org.storemate.pos.services.AuthService;
import org.storemate.pos.utils.SceneManager;

public class EmployeeLoginFormView {
    private final VBox root;

    public EmployeeLoginFormView() {
        AuthService authService = new AuthService(); // your auth service
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("form-box");

        // Title
        Label title = new Label("User Login");
        title.getStyleClass().add("label-title");

        // Fields
        TextField usernameTF = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField passwordTF = new TextField();

        // Sync password fields
        passwordTF.managedProperty().bind(passwordTF.visibleProperty());
        passwordTF.visibleProperty().bind(passwordField.visibleProperty().not());
        passwordTF.textProperty().bindBidirectional(passwordField.textProperty());

        CheckBox showPasswordCB = new CheckBox("Show Password");
        showPasswordCB.selectedProperty().addListener((obs, oldVal, newVal) -> {
            passwordField.setVisible(!newVal);
            passwordField.setManaged(!newVal);
        });

        // Login Button with actual implementation
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("button-connect");
        loginButton.setOnAction(e -> {
            //Extracting data from fields
            String username = usernameTF.getText();
            String password = passwordField.isVisible() ? passwordField.getText() : passwordTF.getText();
            authService.loginUser(username, password);
            SceneManager.showHomePage();
        });
        // --- Form Container (max width 450px) ---
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setMaxWidth(450);
        formContainer.getStyleClass().add("form-grid");

        // GridPane setup
        GridPane loginGrid = new GridPane();
        loginGrid.setHgap(10);
        loginGrid.setVgap(15);
        loginGrid.setAlignment(Pos.CENTER);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.RIGHT);
        col1.setPercentWidth(35);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(65);

        loginGrid.getColumnConstraints().addAll(col1, col2);

        loginGrid.add(new Label("Username:"), 0, 0);
        loginGrid.add(usernameTF, 1, 0);
        loginGrid.add(new Label("Password:"), 0, 1);
        loginGrid.add(passwordField, 1, 1);
        loginGrid.add(passwordTF, 1, 1);
        loginGrid.add(showPasswordCB, 1, 2);

        // Register link
        HBox registerBox = new HBox(5);
        Label registerInfo = new Label("Want to register a new user?");
        Label registerLink = new Label("Register");
        registerLink.getStyleClass().add("hyper-link");
        registerLink.setOnMouseClicked(e -> Router.navigate("/register"));
        registerBox.getChildren().addAll(registerInfo, registerLink);
        registerBox.setAlignment(Pos.CENTER_LEFT);
        loginGrid.add(registerBox, 1, 3);

        // Add grid + button to container
        formContainer.getChildren().addAll(loginGrid, loginButton);

        // Add title + form container to root
        root.getChildren().addAll(title, formContainer);
    }

    public Parent getView() {
        StackPane wrapper = new StackPane(root);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return wrapper;
    }
}
