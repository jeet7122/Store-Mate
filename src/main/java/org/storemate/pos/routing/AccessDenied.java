package org.storemate.pos.routing;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AccessDenied {
    private final VBox root;

    public AccessDenied() {
        root = new VBox(20);
        Button navigate = new Button("Back to Dashboard");
        navigate.getStyleClass().add("button-connect");
        navigate.setOnAction(e -> {
            System.out.println("Trying to navigate to dashboard");
            Router.navigate("/dashboard");
        });

        root.getChildren().addAll(new Label("❌ Access Denied — You do not have permission."), navigate);
    }

    public Parent getView() {
        return root;
    }
}
