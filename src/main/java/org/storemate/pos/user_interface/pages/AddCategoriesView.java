package org.storemate.pos.user_interface.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.CategoryDAO;
import org.storemate.pos.models.Category;
import org.storemate.pos.models.enums.*;
import org.storemate.pos.utils.Alerts;

import java.util.Arrays;

public class AddCategoriesView {

    private final VBox root;
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final Alerts alerts = new Alerts();


    public AddCategoriesView() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("root");
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Add New Category");
        title.getStyleClass().add("label-title");

        TextField nameField = new TextField();
        nameField.setPromptText("Category Name");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Category Description");
        descriptionField.setPrefRowCount(3);

        ComboBox<Status> statusBox = new ComboBox<>();
        statusBox.getItems().addAll(Arrays.asList(Status.values()));
        statusBox.setPromptText("Select Status");


        Button addBtn = new Button("Add Category");
        addBtn.getStyleClass().add("button-connect");

        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String desc = descriptionField.getText().trim();
            Status status = statusBox.getValue();

            if (name.isEmpty() || desc.isEmpty() || status == null) {
                alerts.showErrorAlert("Validation Error", "All fields must be filled.");
                return;
            }

            Category category = new Category(name, desc, status);

            boolean success = categoryDAO.addCategory(category);
            if (success) {
                alerts.showSuccessAlert("Category Added", "Category added successfully.");
                nameField.clear();
                descriptionField.clear();
                statusBox.setValue(null);
            } else {
                alerts.showErrorAlert("Error", "Failed to add category.");
            }
        });

        root.getChildren().addAll(title, nameField, descriptionField, statusBox, addBtn);
    }

    public Parent getView() {
        return root;
    }
}
