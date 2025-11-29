package org.storemate.pos.user_interface.pages;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.CategoryDAO;
import org.storemate.pos.models.Category;
import org.storemate.pos.models.enums.Status;
import org.storemate.pos.routing.Router;
import org.storemate.pos.utils.Alerts;

import java.util.List;

public class ModifyCategoriesView {

    private final VBox root;
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final Alerts alerts = new Alerts();

    private ComboBox<Category> categorySelector;
    private TextField nameTextField;
    private TextField descTextField;
    private ComboBox<Status> statusComboBox;
    private Button saveButton;
    private Button cancelButton;

    private Category currentCategory;

    public ModifyCategoriesView() {
        root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        initializeView();
        loadCategories();
    }

    private void initializeView() {
        Label formTitle = new Label("Modify Category");
        formTitle.getStyleClass().add("label-title");

        GridPane formGrid = new GridPane();
        formGrid.setVgap(20);
        formGrid.setHgap(10);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.getStyleClass().add("form-grid");

        // Category selector
        categorySelector = new ComboBox<>();
        categorySelector.setPromptText("Select Category");
        categorySelector.setOnAction(e -> populateForm(categorySelector.getValue()));

        nameTextField = new TextField();
        nameTextField.setPromptText("Category Name");

        descTextField = new TextField();
        descTextField.setPromptText("Description");

        statusComboBox = new ComboBox<>(FXCollections.observableArrayList(Status.values()));
        statusComboBox.setPromptText("Select Status");

        saveButton = new Button("Save Changes");
        saveButton.getStyleClass().addAll("button", "button-connect");
        saveButton.setOnAction(e -> saveChanges());

        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().addAll("button", "button-cancel");
        cancelButton.setOnAction(e -> Router.navigate("/categories"));

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        formGrid.add(new Label("Category:"), 0, 0);
        formGrid.add(categorySelector, 1, 0);
        formGrid.add(new Label("Category Name:"), 0, 1);
        formGrid.add(nameTextField, 1, 1);
        formGrid.add(new Label("Description:"), 0, 2);
        formGrid.add(descTextField, 1, 2);
        formGrid.add(new Label("Status:"), 0, 3);
        formGrid.add(statusComboBox, 1, 3);
        formGrid.add(buttonBox, 0, 4, 2, 1);

        root.getChildren().addAll(formTitle, formGrid);
    }

    private void loadCategories() {
        List<Category> categories = categoryDAO.getAllCategories();
        categorySelector.setItems(FXCollections.observableArrayList(categories));
    }

    private void populateForm(Category category) {
        if (category == null) return;
        currentCategory = category;
        nameTextField.setText(category.getCategory_name());
        descTextField.setText(category.getCategory_description());
        statusComboBox.setValue(category.getStatus());
    }

    private void saveChanges() {
        if (currentCategory == null) {
            alerts.showErrorAlert("Error", "Please select a category to modify.");
            return;
        }

        String name = nameTextField.getText().trim();
        String desc = descTextField.getText().trim();
        Status status = statusComboBox.getValue();

        if (name.isEmpty() || desc.isEmpty() || status == null) {
            alerts.showErrorAlert("Validation Error", "Please fill in all fields.");
            return;
        }

        currentCategory.setCategory_name(name);
        currentCategory.setCategory_description(desc);
        currentCategory.setStatus(status);

        boolean updated = categoryDAO.updateCategory(currentCategory);
        if (updated) {
            alerts.showSuccessAlert("Success", "Category updated successfully.");
            loadCategories();
        } else {
            alerts.showErrorAlert("Error", "Failed to update category.");
        }
    }

    public Parent getView() {
        return root;
    }
}
