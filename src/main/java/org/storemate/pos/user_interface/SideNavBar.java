package org.storemate.pos.user_interface;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.storemate.pos.models.enums.Permission;
import org.storemate.pos.services.AuthorizationService;
import org.storemate.pos.utils.Session;
import org.storemate.pos.routing.Router;

public class SideNavBar {

    private final VBox root;
    private final AuthorizationService authService;

    public SideNavBar() {
        root = new VBox();
        root.getStyleClass().add("sidebar");
        VBox.setVgrow(root, javafx.scene.layout.Priority.ALWAYS);

        var currentUser = Session.getInstance().getCurrentUser();
        authService = new AuthorizationService(currentUser.getRole());

        // Profile header
        Label title = new Label("StoreMate");
        title.getStyleClass().add("sidebar-title");

        Label welcome = new Label("Welcome " + currentUser.getUsername());
        welcome.getStyleClass().add("sidebar-welcome");

        VBox profileBox = new VBox(title, welcome);
        profileBox.getStyleClass().add("sidebar-profile");

        root.getChildren().add(profileBox);

        // Sections
        addSection("OVERVIEW",
                createNavButton("Dashboard", "/dashboard", Permission.VIEW_DASHBOARD),
                createNavButton("Sales Reports", "/reports/sales", Permission.VIEW_SALES_REPORTS)
        );

        addSection("OPERATIONS",
                createNavButton("Point of Sale", "/pos", Permission.PROCESS_SALES),
                createNavButton("Transactions", "/transactions", Permission.VIEW_TRANSACTIONS),
                createNavButton("Refunds & Voids", "/transactions/refunds", Permission.PROCESS_REFUND)
        );

        addSection("INVENTORY",
                createNavButton("Products", "/products", Permission.MANAGE_PRODUCTS),
                createNavButton("Categories", "/categories", Permission.MANAGE_PRODUCTS),
                createNavButton("Purchase Orders", "/purchases", Permission.MANAGE_PURCHASES),
                createNavButton("Suppliers", "/suppliers", Permission.MANAGE_SUPPLIERS)
        );

        addSection("MANAGEMENT",
                createNavButton("Employees", "/employees", Permission.MANAGE_EMPLOYEES),
                createNavButton("Shifts", "/shifts", Permission.VIEW_SHIFTS),
                createNavButton("Settings", "/settings/general", Permission.MANAGE_SETTINGS)
        );
    }


    // Helper method to add a labeled section with buttons
    private void addSection(String title, Button... buttons) {
        Label header = new Label(title);
        root.getChildren().add(header);
        for (Button btn : buttons) {
            root.getChildren().add(btn);
        }
    }

    // Creates buttons, disables them if the user lacks permission
    private Button createNavButton(String text, String route, Permission permission) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add("nav-button");

        if (!authService.hasPermission(permission)) {
            button.setDisable(true); // disable instead of hiding
        }

        // Use Router to navigate
        button.setOnAction(e -> {
            highlightActive(button);
            Router.navigate(route);
        });

        return button;
    }

    public VBox getRoot() {
        return root;
    }
    private void highlightActive(Button activeBtn) {
        root.getChildren().forEach(node -> {
            if (node instanceof Button b) {
                b.getStyleClass().remove("active");
            }
        });
        activeBtn.getStyleClass().add("active");
    }

}
