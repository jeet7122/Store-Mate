package org.storemate.pos.user_interface;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.SecurityQuestionsDAO;
import org.storemate.pos.dto.SecurityQuestionsDTO;
import org.storemate.pos.models.enums.Role;
import org.storemate.pos.routing.Router;
import org.storemate.pos.services.AuthService;
import org.storemate.pos.utils.Alerts;
import org.storemate.pos.utils.Session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeeRegistrationFormView {
    private final VBox root;

    public EmployeeRegistrationFormView(){
        SecurityQuestionsDAO securityQuestionsDAO = new SecurityQuestionsDAO();
        AuthService authService = new AuthService();
        Alerts alerts = new Alerts();
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


        //Question Box
        VBox securityQuestionsBox = new VBox(10);
        securityQuestionsBox.setPadding(new Insets(10));
        Label security_box_title = new Label("Security Questions");


        List<SecurityQuestionsDTO> questionsList = securityQuestionsDAO.getAllQuestions();
        List<String> strQuestionList = questionsList.stream()
                .map(SecurityQuestionsDTO::getQuestion)
                .toList();
        HBox question1Box = new HBox(5);
        HBox question2Box = new HBox(5);
        HBox question3Box = new HBox(5);

        ComboBox<String> question1 = new ComboBox<>();
        ComboBox<String> question2 = new ComboBox<>();
        ComboBox<String> question3 = new ComboBox<>();
        TextField answer1TF = new TextField();
        answer1TF.setPromptText("enter you answer");
        TextField answer2TF = new TextField();
        answer2TF.setPromptText("enter you answer");
        TextField answer3TF = new TextField();
        answer3TF.setPromptText("enter you answer");
        if (!strQuestionList.isEmpty())
        {
            question1.setItems(FXCollections.observableList(strQuestionList));
            question2.setItems(FXCollections.observableList(strQuestionList));
            question3.setItems(FXCollections.observableList(strQuestionList));
        }
        question1Box.getChildren().addAll(question1, answer1TF);
        question2Box.getChildren().addAll(question2, answer2TF);
        question3Box.getChildren().addAll(question3, answer3TF);

        securityQuestionsBox.getChildren().addAll(security_box_title,question1Box,question2Box,question3Box);

        HBox buttonBox = new HBox(10);

        Button submitButton = new Button("Register");
        submitButton.getStyleClass().addAll("button", "button-connect");

        submitButton.disableProperty().bind(
                passwordTextField.textProperty().isEmpty()
                        .or(confirmPasswordTextField.textProperty().isEmpty())
                        .or(passwordTextField.textProperty().isNotEqualTo(confirmPasswordTextField.textProperty()))
        );

        //Register Trigger
        submitButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String email = emailTextField.getText();
            String password = passwordTextField.getText();
            String confirmPassword = confirmPasswordTextField.getText();
            Role role = roleBox.getSelectionModel().getSelectedItem();
            String selectedQuest1 = question1.getValue();
            String selectedQuest2 = question2.getValue();
            String selectedQuest3 = question3.getValue();
            SecurityQuestionsDTO ques1DTO = questionsList.stream()
                    .filter(q -> q.getQuestion().equalsIgnoreCase(selectedQuest1))
                    .findFirst().orElse(null);
            SecurityQuestionsDTO ques2DTO = questionsList.stream()
                    .filter(q -> q.getQuestion().equalsIgnoreCase(selectedQuest2))
                    .findFirst().orElse(null);
            SecurityQuestionsDTO ques3DTO = questionsList.stream()
                    .filter(q -> q.getQuestion().equalsIgnoreCase(selectedQuest3))
                    .findFirst().orElse(null);
            if (ques1DTO == null || ques2DTO == null || ques3DTO == null){
                alerts.showErrorAlert(
                        "Error",
                        "Security questions are required for registration"
                );
            }
            assert ques1DTO != null;
            assert ques2DTO != null;
            assert ques3DTO != null;
            int quest1ID = ques1DTO.getQuestionID();
            int quest2ID = ques2DTO.getQuestionID();
            int quest3ID = ques3DTO.getQuestionID();


            String answer1 = answer1TF.getText();
            String answer2 = answer2TF.getText();
            String answer3 = answer3TF.getText();


            if (!password.equals(confirmPassword)){
                alerts.showErrorAlert(
                        "Password Mismatch",
                        "The password and confirm password fields do not match. Please re-enter."
                );
            }
            if (email.isEmpty() || username.isEmpty() || answer1.isEmpty() || answer2.isEmpty() || answer3.isEmpty()){
                alerts.showErrorAlert(
                        "Missing Fields",
                        "In order to register, all fields are required"
                );
            }
            if (role == null) {
                alerts.showErrorAlert("Missing Role", "Please select a role before registering.");
                return;
            }
            try {
                String roleForDB = role.toString();
                boolean registered = authService.registerUser(username, email, password, roleForDB, quest1ID, quest2ID, quest3ID, answer1, answer2, answer3);
                if (registered){
                    alerts.showSuccessAlert("Store-Mate Employee Registration", "Employee Registered Successfully");
                    if (Session.getInstance().getCurrentUser() == null){
                        Router.navigate("/login");
                    }
                }
                return;
            }
            catch (Exception exception){
                System.out.println("Unknown Exception: " + exception.getMessage());
            }
            usernameTextField.clear();
            passwordTextField.clear();
            confirmPasswordTextField.clear();
            emailTextField.clear();
            roleBox.setValue(null);
        });



        Button resetButton = new Button("Clear");
        resetButton.getStyleClass().addAll("button", "button-reset");

        //Reset Trigger
        resetButton.setOnAction(e -> {
            usernameTextField.clear();
            passwordTextField.clear();
            confirmPasswordTextField.clear();
            emailTextField.clear();
            roleBox.setValue(null);
        });

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
        formGrid.add(securityQuestionsBox,0,6,2,5);
        formGrid.add(buttonBox, 0, 11);
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
