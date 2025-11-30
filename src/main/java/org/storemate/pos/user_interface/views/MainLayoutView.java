package org.storemate.pos.user_interface.views;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import org.storemate.pos.user_interface.RouterPageDemo;
import org.storemate.pos.user_interface.SideNavBar;

public class MainLayoutView {
    private final BorderPane root;

    public MainLayoutView() {
        root = new BorderPane();

        // --- Side Nav ---
        SideNavBar sideNav = new SideNavBar();
        root.setLeft(sideNav.getRoot());

        // --- Center Content ---
        // Initially, show the dashboard or welcome page
        Parent initialContent = RouterPageDemo.createInitialContent();
        root.setCenter(initialContent);
        root.setPadding(new Insets(10,10,10,10));
    }

    public BorderPane getView() {
        return root;
    }
}
