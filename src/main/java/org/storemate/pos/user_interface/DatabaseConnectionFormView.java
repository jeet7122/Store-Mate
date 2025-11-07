package org.storemate.pos.user_interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.storemate.pos.utils.Alerts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnectionFormView {
    private final VBox root;
    private final CredentialManagement credentialManagement = CredentialManagement.getInstance();
    public DatabaseConnectionFormView() {
        Alerts alerts = new Alerts();
        VBox formVBox = new VBox();
        root = new VBox();
        GridPane formGrid = new GridPane();
        ComboBox<String> dbProviders = new ComboBox<>();

        String[] dbProvidersArray = {"Postgres", "MariaDB", "MySQL", "College Server" };

        dbProviders.setItems(FXCollections.observableArrayList(dbProvidersArray));



        Label titleLabel = new Label("Database Connection Form");
        titleLabel.getStyleClass().add("label-title");

        Label footerLabel = new Label("@2025 Store-Mate. All rights reserved.");
        footerLabel.getStyleClass().add("label-footer");

        TextField databaseUrlTextField = new TextField();
        databaseUrlTextField.setPromptText("Enter database URL");

        TextField usernameTextField = new TextField();
        usernameTextField.setPromptText("Enter username");

        TextField passwordTextField = new TextField();
        passwordTextField.setPromptText("Enter password");

        TextField databaseNameTextField = new TextField();
        databaseNameTextField.setPromptText("Enter database name");

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
        formGrid.getStyleClass().add("form-grid");

        Label loginTitle = new Label("Welcome to Store-Mate");
        loginTitle.getStyleClass().add("label-sub_head_title");

        Label infoLabel = new Label("Please enter your database credentials");
        infoLabel.getStyleClass().add("label-subtitle");

        Button loginButton = new Button("Connect");
        loginButton.getStyleClass().addAll("button", "button-connect");

        Button resetButton = new Button("Reset");
        resetButton.getStyleClass().addAll("button", "button-reset");


        //Login Button Function
        loginButton.setOnAction(e -> {
            try {
                credentialManagement.saveCredentials(
                        databaseUrlTextField.getText(),
                        databaseNameTextField.getText(),
                        usernameTextField.getText(),
                        passwordTextField.getText(),
                        dbProviders.getValue()
                );
                alerts.showSuccessAlert("Store Mate", "Credentials saved successfully");
                System.out.println("Successfully stored credentials");

            }
            catch (IOException ioException) {
                System.out.println("IO-EXCEPTION: " + ioException.getMessage());
            }
            catch (Exception exception) {
                System.out.println("EXCEPTION: " + exception.getMessage());
            }
        });


        HBox buttonBox = new HBox(10, loginButton, resetButton);
        buttonBox.setAlignment(Pos.CENTER);

        formVBox.getChildren().addAll(loginTitle, infoLabel, formGrid, dbProviders , buttonBox);
        formVBox.getStyleClass().add("form-box");
        formVBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(titleLabel, formVBox, footerLabel);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.getStyleClass().add("root");
    }

    public Parent getView() {
        return root;
    }

    // Optional helper method to attach CSS when creating a Scene
    public Scene createScene() {
        Scene scene = new Scene(getView(), 600, 500);
        scene.getStylesheets().add(getClass().getResource("/css/index.css").toExternalForm());
        return scene;
    }
}
