package org.storemate.pos.user_interface.pages.settings_pages;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.SecurityQuestionsDAO;
import org.storemate.pos.dto.SecurityAnswersDTO;
import org.storemate.pos.dto.SecurityQuestionWithAnswerDTO;
import org.storemate.pos.dto.SecurityQuestionsDTO;
import org.storemate.pos.utils.Alerts;
import org.storemate.pos.utils.Session;

import java.util.List;

public class ManageSecurityQuestionsView {
    private final VBox root;
    private final SecurityQuestionsDAO securityQuestionsDAO;
    public ManageSecurityQuestionsView(){
        Alerts alerts = new Alerts();
        securityQuestionsDAO = new SecurityQuestionsDAO();
        List<SecurityQuestionWithAnswerDTO> questionsList = securityQuestionsDAO.getAllQuestionsForUser(Session.getInstance().getCurrentUser().getEmployeeID());
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
        if (questionsList.size() == 3)
        {
            question1.setValue(questionsList.get(0).getQuestion());
            question2.setValue(questionsList.get(1).getQuestion());
            question3.setValue(questionsList.get(2).getQuestion());
            answer1TF.setText(questionsList.get(0).getAnswer());
            answer2TF.setText(questionsList.get(1).getAnswer());
            answer3TF.setText(questionsList.get(2).getAnswer());
        }
        question1Box.getChildren().addAll(question1, answer1TF);
        question2Box.getChildren().addAll(question2, answer2TF);
        question3Box.getChildren().addAll(question3, answer3TF);

        Button updateBTN = new Button("Update");
        Button clearBTN = new Button("Clear");
        clearBTN.setOnAction(e -> {
            answer1TF.clear();
            answer2TF.clear();
            answer3TF.clear();
        });

        HBox buttons = new HBox(5);
        buttons.getChildren().addAll(updateBTN, clearBTN);


        updateBTN.setOnAction(e -> {
            int userID = Session.getInstance().getCurrentUser().getEmployeeID();

            int q1ID = securityQuestionsDAO.getQuestionIdByText(question1.getValue());
            int q2ID = securityQuestionsDAO.getQuestionIdByText(question2.getValue());
            int q3ID = securityQuestionsDAO.getQuestionIdByText(question3.getValue());

            SecurityAnswersDTO a1 = new SecurityAnswersDTO(q1ID, answer1TF.getText());
            SecurityAnswersDTO a2 = new SecurityAnswersDTO(q2ID, answer2TF.getText());
            SecurityAnswersDTO a3 = new SecurityAnswersDTO(q3ID, answer3TF.getText());

            List<SecurityAnswersDTO> list = List.of(a1,a2,a3);

            boolean updated = securityQuestionsDAO.updateSecurityQuestions(userID, list);
            if (updated) {
                System.out.println("Security questions updated successfully");
                alerts.showSuccessAlert(
                        "Success",
                        "Security Questions updated successfully"
                );

                answer1TF.clear();
                answer2TF.clear();
                answer3TF.clear();

            }
            else{
                System.out.println("Update failed");
                alerts.showErrorAlert(
                        "Error",
                        "Error updating security questions"
                );
            }
        });



        root = new VBox(10);
        root.setPadding(new Insets(10));
        Label title = new Label("Manage Security Questions");
        root.getChildren().addAll(title, question1Box, question2Box, question3Box, buttons);



    }
    public Parent getView(){
        return root;
    }
}
