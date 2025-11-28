package org.storemate.pos.user_interface.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.storemate.pos.dao.SuppliersDAO;
import org.storemate.pos.models.Supplier;
import org.storemate.pos.models.enums.Status;
import org.storemate.pos.utils.Alerts;

public class AddSuppliersView {

    private final VBox root;
    private final TextField supplierNameField;
    private final TextField supplierEmailField;
    private final TextField supplierLocationField;
    private final ComboBox<Status> statusComboBox;
    private final Button saveButton;
    private final SuppliersDAO suppliersDAO;
    private final Alerts alerts;

    public AddSuppliersView() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        suppliersDAO = new SuppliersDAO();
        alerts = new Alerts();

        // Form Fields
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

        saveButton = new Button("Save Supplier");
        saveButton.getStyleClass().add("button-connect");
        saveButton.setOnAction(e -> addSupplier());

        // Layout
        VBox formLayout = new VBox(10);
        formLayout.getChildren().addAll(
                new Label("Add New Supplier"),
                supplierNameField,
                supplierEmailField,
                supplierLocationField,
                statusComboBox,
                saveButton
        );

        formLayout.setAlignment(Pos.CENTER);
        root.getChildren().add(formLayout);
    }

    private void addSupplier() {
        // Get form data
        String name = supplierNameField.getText().trim();
        String email = supplierEmailField.getText().trim();
        String location = supplierLocationField.getText().trim();
        Status status = statusComboBox.getValue();

        // Validate form data
        if (name.isEmpty() || email.isEmpty() || location.isEmpty() || status == null) {
            alerts.showErrorAlert("Error", "Please fill all fields.");
            return;
        }

        // Create a new supplier object
        Supplier newSupplier = new Supplier(name, email, location, status);


        boolean success = suppliersDAO.addSupplier(newSupplier);

        if (success) {
            alerts.showSuccessAlert("Supplier Added", "The supplier has been added successfully.");
        } else {
            alerts.showErrorAlert("Error", "Failed to add the supplier.");
        }
    }

    public Parent getView() {
        return root;
    }
}
