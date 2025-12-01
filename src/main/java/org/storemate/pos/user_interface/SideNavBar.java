package org.storemate.pos.user_interface;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.*;
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
        Label title = new Label("Store-Mate");
        FontIcon storeIcon = new FontIcon(MaterialDesignS.STORE);
        storeIcon.setIconSize(30);
        storeIcon.setIconColor(Color.DARKCYAN);
        HBox titleBox = new HBox(5,title, storeIcon);
        title.getStyleClass().add("sidebar-title");

        Label welcome = new Label("Welcome " + currentUser.getUsername());
        welcome.getStyleClass().add("sidebar-welcome");

        VBox profileBox = new VBox(titleBox, welcome);
        profileBox.getStyleClass().add("sidebar-profile");

        root.getChildren().add(profileBox);

        // Sections
        addSection("OVERVIEW",
                createNavButton("Dashboard", "/dashboard", Permission.VIEW_DASHBOARD, MaterialDesignV.VIEW_DASHBOARD_OUTLINE),
                createNavButton("Sales Reports", "/reports/sales", Permission.VIEW_SALES_REPORTS, MaterialDesignC.CHART_LINE)
        );

        addSection("OPERATIONS",
                createNavButton("Point of Sale", "/pos", Permission.PROCESS_SALES, MaterialDesignS.STORE_OUTLINE),
                createNavButton("Transactions", "/transactions", Permission.VIEW_TRANSACTIONS, MaterialDesignR.RECEIPT),
                createNavButton("Refunds & Voids", "/transactions/refunds", Permission.PROCESS_REFUND, MaterialDesignC.CASH_REFUND)
        );

        addSection("INVENTORY",
                createNavButton("Products", "/products", Permission.VIEW_PRODUCTS, MaterialDesignP.PACKAGE_VARIANT),
                createNavButton("Categories", "/categories", Permission.VIEW_CATEGORIES, MaterialDesignL.LABEL_OUTLINE),
                createNavButton("Purchase Orders", "/purchases", Permission.MANAGE_PURCHASES, MaterialDesignC.CLIPBOARD_LIST_OUTLINE),
                createNavButton("Suppliers", "/suppliers", Permission.MANAGE_SUPPLIERS, MaterialDesignT.TRUCK_DELIVERY)
        );

        addSection("MANAGEMENT",
                createNavButton("Employees", "/employees", Permission.MANAGE_EMPLOYEES, MaterialDesignA.ACCOUNT_GROUP_OUTLINE),
                createNavButton("Shifts", "/shifts", Permission.VIEW_SHIFTS, MaterialDesignB.BRIEFCASE_CLOCK_OUTLINE),
                createNavButton("Settings", "/settings/general", Permission.MANAGE_SETTINGS, MaterialDesignC.COG_OUTLINE)
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
    private Button createNavButton(String text, String route, Permission permission, Ikon icon) {

        Button button = new Button(text);
        button.getStyleClass().add("nav-button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);

        // Build icon
        FontIcon fontIcon = new FontIcon(icon);
        fontIcon.setIconSize(18);
        fontIcon.getStyleClass().add("nav-icon");

        // Put icon + text inside HBox
        HBox content = new HBox(10, fontIcon);
        content.setAlignment(Pos.CENTER_LEFT);
        button.setGraphic(content);

        // Disable if no permission
        if (!authService.hasPermission(permission)) {
            button.setDisable(true);
        }

        // Navigation action
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
