package org.storemate.pos.user_interface;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DatabaseConnectionFormView {
    private final VBox root;
    private final GridPane formGrid;
    private final VBox formVBox;
    public DatabaseConnectionFormView() {

        //initializing objects
        formVBox = new VBox();
        root = new VBox();
        formGrid = new GridPane();

        //Main Title
        Label titleLabel = new Label("Database Connection Form");
        titleLabel.setStyle("-fx-font-size: 25; -fx-padding: 10; -fx-text-fill: #2d3436;");
        titleLabel.setAlignment(Pos.TOP_CENTER);

        //Footer Label
        Label footerLabel = new Label("@2025 Store-Mate. All rights reserved.");
        footerLabel.setStyle("-fx-font-size: 25; -fx-text-fill: #00b894;");
        footerLabel.setAlignment(Pos.BOTTOM_CENTER);


        //DB URL TEXT FIELD
        TextField databaseUrlTextField = new TextField();
        databaseUrlTextField.setPromptText("Enter database URL");
        databaseUrlTextField.setPrefColumnCount(15);

        //Username Text-field
        TextField usernameTextField = new TextField();
        usernameTextField.setPromptText("Enter username");
        usernameTextField.setPrefColumnCount(10);

        //Password Text-field
        TextField passwordTextField = new TextField();
        passwordTextField.setPromptText("Enter password");
        passwordTextField.setPrefColumnCount(15);

        //Database Name Text-field
        TextField databaseNameTextField = new TextField();
        databaseNameTextField.setPromptText("Enter database name");
        databaseNameTextField.setPrefColumnCount(10);

        //Adding children to form grid
        formGrid.add(new Label("Database URL:"), 0, 0);
        formGrid.add(databaseUrlTextField, 1, 0);
        formGrid.add(new Label("Database Name:"), 0, 1);
        formGrid.add(databaseNameTextField, 1, 1);
        formGrid.add(new Label("Username:"), 0, 2);
        formGrid.add(usernameTextField, 1, 2);
        formGrid.add(new Label("Password:"), 0, 3);
        formGrid.add(passwordTextField, 1, 3);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setHgap(20);
        formGrid.setVgap(20);

        //form title labels
        Label loginTitle = new Label("Welcome to Store-Mate");
        Label infoLabel = new Label("Please enter your database credentials");

        Button loginButton = new Button("Connect");
        Button resetButton = new Button("Reset");

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(loginButton, resetButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);

        formVBox.getChildren().addAll(loginTitle, infoLabel, formGrid, buttonBox);
        formVBox.setSpacing(20);
        formVBox.setAlignment(Pos.TOP_CENTER);


        root.getChildren().addAll(titleLabel, formVBox, footerLabel);

    }
    public Parent getView() {
        return root;
    }
}
