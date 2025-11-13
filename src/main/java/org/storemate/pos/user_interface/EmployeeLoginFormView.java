package org.storemate.pos.user_interface;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EmployeeLoginFormView {
    private final VBox root;
    public EmployeeLoginFormView(){
        root = new VBox(10);
        root.getStyleClass().add("form-box");
        Label title = new Label("User Login");
        title.getStyleClass().add("label-title");

        /* TextField Declarations */
        TextField usernameTF = new TextField();
        TextField passwordTF = new TextField();
        CheckBox showPasswordCB = new CheckBox();

        /* Login Button */
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("login-connect");


        /* Grid Arrangement */
        GridPane loginGrid = new GridPane();
        loginGrid.setVgap(20);
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.getStyleClass().add("form-grid");
        loginGrid.add(new Label("Username:"), 0, 0);
        loginGrid.add(usernameTF, 1,0);
        loginGrid.add(new Label("Password"),0,1);
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
