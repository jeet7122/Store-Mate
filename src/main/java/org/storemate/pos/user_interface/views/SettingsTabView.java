package org.storemate.pos.user_interface.views;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.*;
import org.storemate.pos.routing.Router;
import org.storemate.pos.services.AuthService;

public class SettingsTabView {

    private final VBox root;
    private final AuthService authService;

    public SettingsTabView(){
        authService = new AuthService();

        // Root container
        root = new VBox(15);
        root.getStyleClass().add("vbox-spaced");

        // Main page title
        Label title = new Label("Settings Panel");
        title.getStyleClass().add("label-title");
        root.getChildren().add(title);

        // ------- Manage Profiles Section -------
        VBox manageUserProfilesBox = new VBox(12);
        manageUserProfilesBox.getStyleClass().add("vbox-spaced");

        Label innerTitleMP = new Label("Manage Profile");
        innerTitleMP.getStyleClass().add("label-subtitle");

        FontIcon modifyFontIcon = new FontIcon(MaterialDesignF.FACE_PROFILE);
        modifyFontIcon.setIconColor(Color.DARKCYAN);
        modifyFontIcon.setIconSize(25);

        HBox titleBox = new HBox(innerTitleMP, modifyFontIcon);
        titleBox.getStyleClass().add("hbox-spaced");

        // ---- Edit Username ----
        HBox editBox = new HBox(10);
        editBox.getStyleClass().add("employee-option-box");

        Label editLabel = new Label("Edit username");
        editLabel.getStyleClass().add("label");

        FontIcon editFontIcon = new FontIcon(MaterialDesignA.ACCOUNT_EDIT);
        editFontIcon.setIconColor(Color.DARKCYAN);
        editFontIcon.setIconSize(25);

        editBox.getChildren().addAll(editLabel, editFontIcon);
        editBox.setOnMouseClicked(e -> Router.navigate("/settings/general/edit-username"));

        // ---- Modify Password ----
        HBox modifyPassBox = new HBox(10);
        modifyPassBox.getStyleClass().add("employee-option-box");

        Label modifyPassword = new Label("Modify Password");
        modifyPassword.getStyleClass().add("label");

        FontIcon modifyPassFontIcon = new FontIcon(MaterialDesignL.LOCK_RESET);
        modifyPassFontIcon.setIconColor(Color.DARKCYAN);
        modifyPassFontIcon.setIconSize(25);

        modifyPassBox.getChildren().addAll(modifyPassword, modifyPassFontIcon);
        modifyPassBox.setOnMouseClicked(e -> Router.navigate("/settings/general/modify-password"));

        // ---- Forgot Password ----
        HBox forgotPassBox = new HBox(10);
        forgotPassBox.getStyleClass().add("employee-option-box");

        Label forgotPassword = new Label("Forgot Password");
        forgotPassword.getStyleClass().add("label");

        FontIcon forgotIcon = new FontIcon(MaterialDesignL.LOCK_QUESTION);
        forgotIcon.setIconColor(Color.DARKCYAN);
        forgotIcon.setIconSize(25);

        forgotPassBox.getChildren().addAll(forgotPassword, forgotIcon);
        forgotPassBox.setOnMouseClicked(e -> Router.navigate("/settings/general/forgot-password"));

        // ---- Security Questions ----
        HBox manageSecurityQuesBox = new HBox(10);
        manageSecurityQuesBox.getStyleClass().add("employee-option-box");

        Label manageSecurityQuestions = new Label("Manage Security Questions");
        manageSecurityQuestions.getStyleClass().add("label");

        FontIcon manageQuesIcon = new FontIcon(MaterialDesignA.ACCOUNT_KEY);
        manageQuesIcon.setIconColor(Color.DARKCYAN);
        manageQuesIcon.setIconSize(25);

        manageSecurityQuesBox.getChildren().addAll(manageSecurityQuestions, manageQuesIcon);
        manageSecurityQuesBox.setOnMouseClicked(
                e -> Router.navigate("/settings/general/manage-security-questions")
        );

        // ---- Logout Button ----
        Button logoutBTN = new Button("Logout");
        logoutBTN.getStyleClass().addAll("button", "button-warning");
        logoutBTN.setOnAction(e -> authService.logout());

        // Attach everything
        manageUserProfilesBox.getChildren().addAll(
                titleBox,
                editBox,
                modifyPassBox,
                forgotPassBox,
                manageSecurityQuesBox,
                logoutBTN
        );

        root.getChildren().add(manageUserProfilesBox);
    }

    public Parent getView(){
        return root;
    }
}
