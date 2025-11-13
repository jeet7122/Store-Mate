package org.storemate.pos.user_interface;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.ShiftDAO;
import org.storemate.pos.models.Employee;
import org.storemate.pos.services.AuthService;
import org.storemate.pos.utils.Alerts;
import org.storemate.pos.utils.Session;

public class EmployeeLoginFormView {
    private final VBox root;
    public EmployeeLoginFormView(){
        Alerts alerts = new Alerts();
        AuthService authService = new AuthService();
        root = new VBox(10);
        root.getStyleClass().add("form-box");
        Label title = new Label("User Login");
        title.getStyleClass().add("label-title");

        /* TextField Declarations */
        TextField usernameTF = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField passwordTF = new TextField();

        //Keep Them in Sync
        passwordTF.managedProperty().bind(passwordTF.visibleProperty());
        passwordTF.visibleProperty().bind(passwordField.visibleProperty().not());

        //Bind text properties together
        passwordTF.textProperty().bindBidirectional(passwordField.textProperty());


        CheckBox showPasswordCB = new CheckBox();

        showPasswordCB.selectedProperty().addListener((obs, oldVal, newVal) ->{
            passwordField.setVisible(!newVal);
            passwordField.setManaged(!newVal);
        });


        /* Login Button */
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("button-connect");

        /* Login Button On Click Action */
        loginButton.setOnAction(e -> {
            //Extracting data from fields
            String username = usernameTF.getText();
            String password = passwordField.isVisible() ? passwordField.getText() : passwordTF.getText();
            authService.loginUser(username, password);
        });

        /* TESTING PURPOSE LOGOUT BUTTON NOT INCLUDED IN FINAL FORM */
        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("button-reset");
        logoutButton.setOnAction(e -> authService.logout());



        /* Grid Arrangement */
        GridPane loginGrid = new GridPane();
        loginGrid.setVgap(20);
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.getStyleClass().add("form-grid");
        loginGrid.add(new Label("Username:"), 0, 0);
        loginGrid.add(usernameTF, 1,0);
        loginGrid.add(new Label("Password"),0,1);
        loginGrid.add(passwordField, 1, 1);
        loginGrid.add(passwordTF, 1, 1);
        loginGrid.add(new Label("Show Password"), 0,2);
        loginGrid.add(showPasswordCB, 1,2);


        /* VBOX Arrangement */
        root.getChildren().addAll(title, loginGrid, loginButton);


    }
    public Parent getView(){
        return root;
    }
}
