package org.storemate.pos.user_interface;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.storemate.pos.utils.Session;

public class SideNavBar {
    private final VBox root;
    public SideNavBar(){
        root = new VBox(10);
        Label c_name = new Label("Store-Mate");
        Label helloGreet = new Label("Welcome " + Session.getInstance().getCurrentUser().getUsername());
        helloGreet.getStyleClass().add("title");
        Label reportsHeader = new Label("OVERVIEW");
        Button btnDashboard = createNavButton("Dashboard 📊", "/dashboard");
        Button btnSalesReports = createNavButton("Sales Reports 📈", "/reports/sales");

        // 2. CORE OPERATIONS
        Label operationsHeader = new Label("OPERATIONS");
        Button btnPOS = createNavButton("Point of Sale 🛒", "/pos");
        Button btnTransactions = createNavButton("Transactions Hist. 🧾", "/transactions");
        Button btnRefunds = createNavButton("Refunds & Voids ↩️", "/transactions/refunds");

        // 3. INVENTORY & PURCHASING
        Label inventoryHeader = new Label("INVENTORY");
        Button btnProducts = createNavButton("Products 🏷️", "/products");
        Button btnCategories = createNavButton("Categories 🗂️", "/categories");
        Button btnPurchaseOrders = createNavButton("Purchase Orders 📝", "/purchases");
        Button btnSuppliers = createNavButton("Suppliers 🚚", "/suppliers");

        // 4. MANAGEMENT & CONFIGURATION
        Label managementHeader = new Label("MANAGEMENT");
        Button btnEmployees = createNavButton("Employees 👥", "/employees");
        Button btnShifts = createNavButton("Shifts Tracking ⌚", "/shifts");
        Button btnSettings = createNavButton("Settings ⚙️", "/settings/general");

        // Add all elements to the VBox
        root.getChildren().addAll(
                c_name, helloGreet,
                reportsHeader, btnDashboard, btnSalesReports,
                operationsHeader, btnPOS, btnTransactions, btnRefunds,
                inventoryHeader, btnProducts, btnCategories, btnPurchaseOrders, btnSuppliers,
                managementHeader, btnEmployees, btnShifts, btnSettings
        );
    }

    // Helper method to create consistent buttons
    private Button createNavButton(String text, String route) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE); // Ensures button fills the width
        button.getStyleClass().add("nav-button");
        // NOTE: You will implement the navigation logic (e.g., event handler
        // that calls a method to change the central view) here or in the controller.
        button.setOnAction(e -> System.out.println("Navigating to: " + route));
        return button;
    }

    public VBox getRoot() {
        return root;
    }

}
