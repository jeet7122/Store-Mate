package org.storemate.pos.user_interface;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.models.Employee;
import org.storemate.pos.models.enums.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeeRegistrationFormView {
    private final VBox root;
    public EmployeeRegistrationFormView(){
        root = new VBox(10);
        GridPane formGrid = new GridPane();
        TextField usernameTextField = new TextField();
        usernameTextField.setPromptText("enter username");
        TextField emailTextField = new TextField();
        emailTextField.setPromptText("enter email");
        TextField passwordTextField = new TextField();
        passwordTextField.setPromptText("enter password");
        TextField confirmPasswordTextField = new TextField();
        confirmPasswordTextField.setPromptText("re-enter password");

        ComboBox<Role> roleBox = new ComboBox<>();
        Label rolesLabel = new Label("Roles");
        rolesLabel.getStyleClass().add("combo-label");
        List<Role> roles = new ArrayList<>(Arrays.asList(Role.values()));
        roleBox.setItems(FXCollections.observableList(roles));
        Label formTitle = new Label("Employee Registration Form");
        formTitle.getStyleClass().add("label-title");


        HBox buttonBox = new HBox(10);

        Button submitButton = new Button("Register");
        submitButton.getStyleClass().addAll("button", "button-connect");
        Button resetButton = new Button("Clear");
        resetButton.getStyleClass().addAll("button", "button-reset");

        buttonBox.getChildren().addAll(submitButton, resetButton);
        buttonBox.setSpacing(20);


        formGrid.add(new Label("Username:"), 0, 1);
        formGrid.add(usernameTextField,1, 1);
        formGrid.add(new Label("Email:"), 0,2);
        formGrid.add(emailTextField, 1,2);
        formGrid.add(new Label("Password:"), 0,3);
        formGrid.add(passwordTextField, 1,3);
        formGrid.add(new Label("Confirm-Password:"), 0,4);
        formGrid.add(confirmPasswordTextField, 1,4);
        formGrid.add(rolesLabel,0,5);
        formGrid.add(roleBox, 1,5);
        formGrid.add(buttonBox, 0, 6);
        formGrid.setVgap(20);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.getStyleClass().add("form-grid");

        root.getChildren().addAll(formTitle,formGrid);
        formTitle.setAlignment(Pos.TOP_CENTER);
        root.setAlignment(Pos.CENTER);
    }

    public Parent getView(){
        return root;
    }

}
