package org.storemate.pos.user_interface.pages;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.storemate.pos.dao.CategoryDAO;
import org.storemate.pos.models.Category;
import org.storemate.pos.routing.Router;
import org.storemate.pos.utils.Alerts;

import java.util.List;

public class DeleteCategoriesView {

    private final VBox root;
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final Alerts alerts = new Alerts();

    private ComboBox<Category> categorySelector;
    private Button deleteButton;

    public DeleteCategoriesView() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("page-root");

        initializeView();
        loadCategories();
    }

    private void initializeView() {
        Label title = new Label("Delete Category");
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
            Router.navigate("/categories");
        });
        topBar.getChildren().addAll(backButton, title);

        categorySelector = new ComboBox<>();
        categorySelector.setPromptText("Select Category");
        categorySelector.setOnAction(e -> updateInfoLabel());

        deleteButton = new Button("Delete Category");
        deleteButton.getStyleClass().add("button-connect");
        deleteButton.setOnAction(e -> deleteCategory());

        root.getChildren().addAll(topBar, categorySelector, deleteButton);
    }

    private void loadCategories() {
        List<Category> categories = categoryDAO.getAllCategories();
        categorySelector.setItems(FXCollections.observableArrayList(categories));
    }

    private void updateInfoLabel() {
        Category selected = categorySelector.getValue();
        if (selected != null) {
            deleteButton.setText("Delete Category: " + selected.getCategory_name());
        } else {
            deleteButton.setText("Delete Category");
        }
    }

    private void deleteCategory() {
        Category selected = categorySelector.getValue();
        if (selected == null) {
            alerts.showErrorAlert("Error", "Please select a category to delete.");
            return;
        }

        boolean success = categoryDAO.deleteCategory(selected.getCategory_id());
        if (success) {
            alerts.showSuccessAlert("Category Deleted", "Category '" + selected.getCategory_name() + "' deleted successfully.");
            loadCategories(); // refresh selector
            categorySelector.setValue(null);
            deleteButton.setText("Delete Category");
        } else {
            alerts.showErrorAlert("Error", "Failed to delete category.");
        }
    }

    public Parent getView() {
        return root;
    }
}
