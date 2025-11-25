package org.storemate.pos.user_interface.views;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.CategoryDAO;
import org.storemate.pos.models.Category;
import org.storemate.pos.models.enums.Status;
import org.storemate.pos.routing.Router;

import java.util.List;

public class CategoriesDataView {

    private final VBox root;
    private final ObservableList<Category> data = FXCollections.observableArrayList();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    public CategoriesDataView() {

        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("root");

        // Title
        Label title = new Label("Categories Data View");
        title.getStyleClass().add("label-title");

        // Search
        TextField searchTF = new TextField();
        searchTF.setPromptText("Search Category...");
        searchTF.getStyleClass().add("text-field");

        Button searchBTN = new Button("Search");
        searchBTN.getStyleClass().add("button-connect");

        Label searchLabel = new Label("Search:");
        searchLabel.getStyleClass().add("label-sub_head_title");

        HBox searchBox = new HBox(10, searchLabel, searchTF, searchBTN);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.getStyleClass().add("search-box");

        // Action Buttons
        Button addCategoryBTN = new Button("Add Category");
        addCategoryBTN.getStyleClass().add("button-connect");
        addCategoryBTN.setOnAction(e -> Router.navigate("/categories/add"));

        Button refreshBTN = new Button("Refresh");
        refreshBTN.getStyleClass().add("button-reset");

        HBox topActionBox = new HBox(10, addCategoryBTN, refreshBTN);
        topActionBox.setAlignment(Pos.CENTER_LEFT);

        // Table
        TableView<Category> table = new TableView<>();
        table.setItems(data);
        table.getStyleClass().add("categories-table");

        // Define Columns
        TableColumn<Category, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(v -> new ReadOnlyObjectWrapper<>(v.getValue().getCategory_id()));

        TableColumn<Category, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(v -> new ReadOnlyStringWrapper(v.getValue().getCategory_name()));

        TableColumn<Category, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(v -> new ReadOnlyStringWrapper(v.getValue().getCategory_description()));

        TableColumn<Category, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(v -> new ReadOnlyStringWrapper(v.getValue().getStatus().toString()));

        // Add Columns to Table
        table.getColumns().addAll(idCol, nameCol, descCol, statusCol);

        loadCategoryData();
        searchBTN.setOnAction(e -> {
            String searchQuery = searchTF.getText().trim();
            if (!searchQuery.isEmpty()) {
                List<Category> categories = categoryDAO.searchCategories(searchQuery);
                data.setAll(categories);
            } else {
                loadCategoryData();
            }
        });

        // Refresh Button
        refreshBTN.setOnAction(e -> loadCategoryData());

        //  Build Root
        root.getChildren().addAll(
                title,
                topActionBox,
                searchBox,
                table
        );
    }


    private void loadCategoryData() {
        List<Category> categories = categoryDAO.getAllCategories();
        data.setAll(categories);
    }

    public Parent getView() {
        return root;
    }
}
