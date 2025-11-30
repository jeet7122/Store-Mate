package org.storemate.pos.user_interface.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.storemate.pos.models.enums.Status;
import org.storemate.pos.routing.Router;
import org.storemate.pos.utils.Alerts;

public class ModifySuppliersView {

    private final VBox root;
    private final ComboBox<Supplier> supplierComboBox;
    private final TextField supplierNameField;
    private final TextField supplierEmailField;
    private final TextField supplierLocationField;
    private final ComboBox<Status> statusComboBox;
    private final Button saveButton;
    private final SuppliersDAO suppliersDAO;
    private final Alerts alerts;

    public ModifySuppliersView() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        suppliersDAO = new SuppliersDAO();
        alerts = new Alerts();

        Label title = new Label("Modify Suppliers");
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

        // Form Fields
        supplierComboBox = new ComboBox<>();
        supplierComboBox.setPromptText("Select Supplier");
        supplierComboBox.setOnAction(e -> fillFormFields(supplierComboBox.getValue()));

        supplierNameField = new TextField();
        supplierNameField.setPromptText("Supplier Name");

        supplierEmailField = new TextField();
        supplierEmailField.setPromptText("Supplier Email");

        supplierLocationField = new TextField();
        supplierLocationField.setPromptText("Supplier Location");

        // Status ComboBox (Active, Inactive)
        statusComboBox = new ComboBox<>();
        statusComboBox.setItems(FXCollections.observableArrayList(Status.values()));
        statusComboBox.setPromptText("Select Status");

        saveButton = new Button("Save Changes");
        saveButton.getStyleClass().add("button-connect");
        saveButton.setOnAction(e -> modifySupplier());

        // Layout
        VBox formLayout = new VBox(10);
        formLayout.getChildren().addAll(
                new Label("Modify Supplier"),
                supplierComboBox,
                supplierNameField,
                supplierEmailField,
                supplierLocationField,
                statusComboBox,
                saveButton
        );

        formLayout.setAlignment(Pos.CENTER);
        root.getChildren().addAll(topBar,formLayout);

        // Load suppliers into ComboBox
        loadSuppliers();
    }

    private void loadSuppliers() {
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList(suppliersDAO.getAllSuppliers());
        supplierComboBox.setItems(suppliers);
    }

    private void fillFormFields(Supplier selectedSupplier) {
        if (selectedSupplier != null) {
            supplierNameField.setText(selectedSupplier.getSupplier_name());
            supplierEmailField.setText(selectedSupplier.getSupplier_email());
            supplierLocationField.setText(selectedSupplier.getSupplier_location());
            statusComboBox.setValue(selectedSupplier.getSupplier_status());
        }
    }

    private void modifySupplier() {
        // Get selected supplier
        Supplier selectedSupplier = supplierComboBox.getValue();

        // Validate form fields
        if (selectedSupplier == null) {
            alerts.showErrorAlert("Error", "Please select a supplier.");
            return;
        }

        String name = supplierNameField.getText().trim();
        String email = supplierEmailField.getText().trim();
        String location = supplierLocationField.getText().trim();
        Status status = statusComboBox.getValue();

        if (name.isEmpty() || email.isEmpty() || location.isEmpty() || status == null) {
            alerts.showErrorAlert("Error", "Please fill all fields.");
            return;
        }

        // Set new values to the selected supplier
        selectedSupplier.setSupplier_name(name);
        selectedSupplier.setSupplier_email(email);
        selectedSupplier.setSupplier_location(location);
        selectedSupplier.setSupplier_status(status);

        // Call DAO to update the supplier in the database
        boolean success = suppliersDAO.modifySupplier(selectedSupplier);

        if (success) {
            alerts.showSuccessAlert("Supplier Modified", "The supplier details have been updated successfully.");

            // Navigate back to the suppliers list (SuppliersDataView)
            Router.navigate("/suppliers");
        } else {
            alerts.showErrorAlert("Error", "Failed to update the supplier.");
        }
    }

    public Parent getView() {
        return root;
    }
}
