package org.storemate.pos.routing;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AccessDenied {
    private final VBox root;

    public AccessDenied() {
        root = new VBox(20);
        root.getChildren().add(new Label("❌ Access Denied — You do not have permission."));
    }

    public Parent getView() {
        return root;
    }
}
