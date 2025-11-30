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
import org.storemate.pos.dao.SuppliersDAO;
import org.storemate.pos.models.Supplier;
import org.storemate.pos.routing.Router;
import org.storemate.pos.utils.Alerts;

import java.util.List;

public class DeleteSuppliersView {
    private final VBox root;
    private final SuppliersDAO suppliersDAO;
    private final Alerts alerts;
    private ComboBox<Supplier> supplierComboBox;
    private Button deleteButton;

    public DeleteSuppliersView() {
        suppliersDAO = new SuppliersDAO();
        alerts = new Alerts();
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        initializeView();
        loadSuppliers();
    }

    private void initializeView() {
        Label title = new Label("Delete Supplier");
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
            Router.navigate("/suppliers");
        });

        topBar.getChildren().addAll(backButton, title);

        supplierComboBox = new ComboBox<>();
        supplierComboBox.setPromptText("Select Supplier to Delete");
        supplierComboBox.setOnAction(e -> updateDeleteButtonText());

        deleteButton = new Button("Delete Supplier");
        deleteButton.getStyleClass().add("button-delete");
        deleteButton.setOnAction(e -> deleteSupplier());

        root.getChildren().addAll(topBar, supplierComboBox, deleteButton);
    }

    private void loadSuppliers() {
        List<Supplier> suppliers = suppliersDAO.getAllSuppliers();
        supplierComboBox.setItems(FXCollections.observableArrayList(suppliers));
    }

    private void updateDeleteButtonText() {
        Supplier selectedSupplier = supplierComboBox.getValue();
        if (selectedSupplier != null) {
            deleteButton.setText("Delete " + selectedSupplier.getSupplier_name());
        } else {
            deleteButton.setText("Delete Supplier");
        }
    }

    private void deleteSupplier() {
        Supplier selectedSupplier = supplierComboBox.getValue();
        if (selectedSupplier == null) {
            alerts.showErrorAlert("Error", "Please select a supplier to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete supplier: " + selectedSupplier.getSupplier_name() + "?",
                ButtonType.YES, ButtonType.NO);
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                boolean success = suppliersDAO.deleteSupplier(selectedSupplier.getSupplier_id());
                if (success) {
                    alerts.showSuccessAlert("Supplier Deleted", "Supplier '" + selectedSupplier.getSupplier_name() + "' was deleted successfully.");
                    loadSuppliers();
                    supplierComboBox.setValue(null);
                    deleteButton.setText("Delete Supplier");
                } else {
                    alerts.showErrorAlert("Error", "Failed to delete supplier.");
                }
            }
        });
    }

    public Parent getView() {
        return root;
    }
}
