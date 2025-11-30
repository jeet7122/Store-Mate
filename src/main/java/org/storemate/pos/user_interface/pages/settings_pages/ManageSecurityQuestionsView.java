package org.storemate.pos.user_interface.pages.settings_pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.storemate.pos.dao.SecurityQuestionsDAO;
import org.storemate.pos.dto.SecurityAnswersDTO;
import org.storemate.pos.dto.SecurityQuestionWithAnswerDTO;
import org.storemate.pos.routing.Router;
import org.storemate.pos.utils.Alerts;
import org.storemate.pos.utils.Session;

import java.util.List;

public class ManageSecurityQuestionsView {

    private final VBox root;
    private final SecurityQuestionsDAO dao;

    public ManageSecurityQuestionsView() {
        dao = new SecurityQuestionsDAO();
        Alerts alerts = new Alerts();

        root = new VBox(20);
        root.setPadding(new Insets(15));
        root.getStyleClass().add("vbox-spaced");

        Label title = new Label("Manage Security Questions");
        title.getStyleClass().add("label-title");
        HBox topBar = new HBox(50);
        topBar.setPadding(new Insets(10, 0, 20, 0));
        topBar.setAlignment(Pos.CENTER_LEFT);

        FontIcon backIcon = new FontIcon(MaterialDesignA.ARROW_LEFT_BOLD_OUTLINE);
        backIcon.setIconSize(30);
        backIcon.setIconColor(Color.DARKCYAN);
        Label backButton = new Label("", backIcon);
        backButton.getStyleClass().add("back-button"); // your CSS class
        backButton.setOnMouseClicked(e -> {
            // Handle navigation (replace this with your page switching logic)
            System.out.println("Back clicked!");
            Router.navigate("/settings/general");
        });
        topBar.getChildren().addAll(backButton, title);

        List<SecurityQuestionWithAnswerDTO> list =
                dao.getAllQuestionsForUser(Session.getInstance().getCurrentUser().getEmployeeID());

        ComboBox<String> q1 = new ComboBox<>();
        ComboBox<String> q2 = new ComboBox<>();
        ComboBox<String> q3 = new ComboBox<>();

        q1.getStyleClass().add("combo-box");
        q2.getStyleClass().add("combo-box");
        q3.getStyleClass().add("combo-box");

        TextField a1TF = new TextField();
        TextField a2TF = new TextField();
        TextField a3TF = new TextField();
        a1TF.getStyleClass().add("text-field");
        a2TF.getStyleClass().add("text-field");
        a3TF.getStyleClass().add("text-field");

        a1TF.setPromptText("Enter answer");
        a2TF.setPromptText("Enter answer");
        a3TF.setPromptText("Enter answer");

        if (list.size() == 3) {
            q1.setValue(list.get(0).getQuestion());
            q2.setValue(list.get(1).getQuestion());
            q3.setValue(list.get(2).getQuestion());
            a1TF.setText(list.get(0).getAnswer());
            a2TF.setText(list.get(1).getAnswer());
            a3TF.setText(list.get(2).getAnswer());
        }

        VBox form = new VBox(15);
        form.getStyleClass().add("form-grid");
        form.getChildren().addAll(
                new HBox(10, q1, a1TF),
                new HBox(10, q2, a2TF),
                new HBox(10, q3, a3TF)
        );

        Button updateBtn = new Button("Update");
        updateBtn.getStyleClass().addAll("button", "button-update");

        Button clearBtn = new Button("Clear");
        clearBtn.getStyleClass().addAll("button", "button-reset");

        clearBtn.setOnAction(e -> {
            a1TF.clear();
            a2TF.clear();
            a3TF.clear();
        });

        HBox buttons = new HBox(10, updateBtn, clearBtn);
        buttons.getStyleClass().add("hbox-spaced");

        updateBtn.setOnAction(e -> {
            int userID = Session.getInstance().getCurrentUser().getEmployeeID();
            int q1ID = dao.getQuestionIdByText(q1.getValue());
            int q2ID = dao.getQuestionIdByText(q2.getValue());
            int q3ID = dao.getQuestionIdByText(q3.getValue());

            SecurityAnswersDTO a1 = new SecurityAnswersDTO(q1ID, a1TF.getText());
            SecurityAnswersDTO a2 = new SecurityAnswersDTO(q2ID, a2TF.getText());
            SecurityAnswersDTO a3 = new SecurityAnswersDTO(q3ID, a3TF.getText());

            boolean updated = dao.updateSecurityQuestions(userID, List.of(a1, a2, a3));

            if (updated) alerts.showSuccessAlert("Success", "Security questions updated.");
            else alerts.showErrorAlert("Error", "Update failed.");
        });

        root.getChildren().addAll(topBar, form, buttons);
    }

    public Parent getView() {
        return root;
    }
}
