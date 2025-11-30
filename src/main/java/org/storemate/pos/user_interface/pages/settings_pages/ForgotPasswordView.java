package org.storemate.pos.user_interface.pages.settings_pages;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.SecurityQuestionsDAO;
import org.storemate.pos.dto.SecurityQuestionWithAnswerDTO;
import org.storemate.pos.routing.Router;
import org.storemate.pos.services.AuthService;
import org.storemate.pos.utils.Alerts;
import org.storemate.pos.utils.Session;

import java.util.List;

public class ForgotPasswordView {
    private final AuthService authService;
    private final VBox root;

    public ForgotPasswordView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("vbox-spaced");

        authService = new AuthService();
        Alerts alerts = new Alerts();

        SecurityQuestionsDAO securityQuestionsDAO = new SecurityQuestionsDAO();
        List<SecurityQuestionWithAnswerDTO> list =
                securityQuestionsDAO.getAllQuestionsForUser(
                        Session.getInstance().getCurrentUser().getEmployeeID()
                );

        Label title = new Label("Forgot Password");
        title.getStyleClass().add("label-title");

        ComboBox<String> ques1 = new ComboBox<>();
        ComboBox<String> ques2 = new ComboBox<>();
        ComboBox<String> ques3 = new ComboBox<>();
        ques1.getStyleClass().add("combo-box");
        ques2.getStyleClass().add("combo-box");
        ques3.getStyleClass().add("combo-box");

        TextField ans1TF = new TextField();
        TextField ans2TF = new TextField();
        TextField ans3TF = new TextField();
        ans1TF.getStyleClass().add("text-field");
        ans2TF.getStyleClass().add("text-field");
        ans3TF.getStyleClass().add("text-field");

        ans1TF.setPromptText("Enter answer");
        ans2TF.setPromptText("Enter answer");
        ans3TF.setPromptText("Enter answer");

        if (list.size() == 3) {
            ques1.setValue(list.get(0).getQuestion());
            ques2.setValue(list.get(1).getQuestion());
            ques3.setValue(list.get(2).getQuestion());
        }

        GridPane grid = new GridPane(10, 15);
        grid.getStyleClass().add("form-grid");

        grid.add(ques1, 0, 0);
        grid.add(ans1TF, 1, 0);
        grid.add(ques2, 0, 1);
        grid.add(ans2TF, 1, 1);
        grid.add(ques3, 0, 2);
        grid.add(ans3TF, 1, 2);

        Button submitBtn = new Button("Submit");
        submitBtn.getStyleClass().addAll("button", "button-update");
        grid.add(submitBtn, 0, 3, 2, 1);

        submitBtn.setOnAction(e -> {
            boolean ok =
                    ans1TF.getText().equals(list.get(0).getAnswer()) &&
                            ans2TF.getText().equals(list.get(1).getAnswer()) &&
                            ans3TF.getText().equals(list.get(2).getAnswer());

            if (!ok) {
                alerts.showErrorAlert("Error", "Security answers do not match!");
                return;
            }

            // Load new password page
            VBox newPassBox = new VBox(12);
            newPassBox.getStyleClass().add("vbox-spaced");

            TextField newTF = new TextField();
            newTF.setPromptText("Enter new password");
            newTF.getStyleClass().add("text-field");

            TextField confirmTF = new TextField();
            confirmTF.setPromptText("Re-enter password");
            confirmTF.getStyleClass().add("text-field");

            Button updateBtn = new Button("Update");
            updateBtn.getStyleClass().addAll("button", "button-update");

            newPassBox.getChildren().setAll(newTF, confirmTF, updateBtn);

            root.getChildren().setAll(
                    new Label("Reset Password") {{
                        getStyleClass().add("label-title");
                    }},
                    newPassBox
            );

            updateBtn.setOnAction(ex -> {
                if (!newTF.getText().equals(confirmTF.getText())) {
                    alerts.showErrorAlert("Error", "Passwords must match!");
                    return;
                }

                boolean updated = authService.updatePass(
                        Session.getInstance().getCurrentUser().getEmployeeID(),
                        confirmTF.getText()
                );

                if (updated) {
                    alerts.showSuccessAlert("Success", "Password updated!");
                    Router.navigate("/dashboard");
                } else {
                    alerts.showErrorAlert("Error", "Update failed.");
                }
            });
        });

        root.getChildren().addAll(title, grid);
    }

    public Parent getView() {
        return root;
    }
}
