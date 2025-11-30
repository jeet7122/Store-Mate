package org.storemate.pos.user_interface.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignP;
import org.kordamp.ikonli.materialdesign2.MaterialDesignT;
import org.storemate.pos.dao.CategoryDAO;
import org.storemate.pos.models.Category;
import org.storemate.pos.models.enums.Status;
import org.storemate.pos.routing.Router;

import java.util.List;

public class CategoriesDataView {

    private final VBox root;
    private TableView<Category> categoryTable;
    private ObservableList<Category> categoryData;
    private final CategoryDAO categoryDAO;

    public CategoriesDataView() {
        categoryDAO = new CategoryDAO();
        root = new VBox(15);
        root.setPadding(new Insets(10));
        initializeView();
        loadCategories();
    }

    private void initializeView() {
        Label title = new Label("Category Management Data View");
        title.getStyleClass().add("dashboard-title");

        // Action buttons
        HBox actionButtons = createActionButtons();

        // Table Setup
        categoryTable = new TableView<>();
        categoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        TableColumn<Category, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("category_id"));

        TableColumn<Category, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("category_name"));

        TableColumn<Category, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("category_description"));

        TableColumn<Category, Status> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        categoryTable.getColumns().addAll(idCol, nameCol, descCol, statusCol);

        // Search
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search categories...");

        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("button-search");
        searchBtn.setOnAction(e -> searchCategories(searchField.getText()));

        Button refreshBtn = new Button("Refresh");
        refreshBtn.getStyleClass().add("button-reset");
        refreshBtn.setOnAction(e -> loadCategories());

        searchBox.getChildren().addAll(searchField, searchBtn, refreshBtn);

        root.getChildren().addAll(title, actionButtons, searchBox, categoryTable);
    }

    private HBox createActionButtons() {
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER_LEFT);

        // Add button
        Button addBtn = new Button("Add Category");
        addBtn.getStyleClass().add("button-connect");
        addBtn.setOnAction(e -> Router.navigate("/categories/add"));


        // Modify button
        Button modifyBtn = new Button("Modify Category");
        modifyBtn.getStyleClass().add("button-update");
                modifyBtn.setOnAction(e -> {
            Category selected = categoryTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Router.navigate("/categories/modify");
            } else {
                showAlert("Select a category to modify!");
            }
        });

        // Delete button
        Button deleteBtn = new Button("Delete Category");
        deleteBtn.getStyleClass().add("button-delete");
        deleteBtn.setOnAction(e -> {
            Category selected = categoryTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to delete category: " + selected.getCategory_name() + "?",
                        ButtonType.YES, ButtonType.NO);

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        boolean success = categoryDAO.deleteCategory(selected.getCategory_id());
                        if (success) {
                            loadCategories();
                            showAlert("Category deleted successfully!");
                        } else {
                            showAlert("Failed to delete category.");
                        }
                    }
                });
            } else {
                showAlert("Select a category to delete!");
            }
        });



        actionButtons.getChildren().addAll(addBtn, modifyBtn, deleteBtn);
        return actionButtons;
    }

    private void loadCategories() {
        List<Category> categories = categoryDAO.getAllCategories();
        categoryData = FXCollections.observableArrayList(categories);
        categoryTable.setItems(categoryData);
    }

    private void searchCategories(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            loadCategories();
            return;
        }
        List<Category> results = categoryDAO.searchCategories(keyword);
        categoryData = FXCollections.observableArrayList(results);
        categoryTable.setItems(categoryData);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Parent getView() {
        return root;
    }
}
