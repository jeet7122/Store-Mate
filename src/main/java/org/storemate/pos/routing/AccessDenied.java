package org.storemate.pos.routing;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Represents a JavaFX view displayed when a user attempts to access a route
 * or feature without sufficient permissions.
 * <p>
 * This view shows an "Access Denied" message along with a button that allows
 * the user to navigate back to the dashboard.
 * @author Khushi, Jeet
 */
public class AccessDenied {
    /**
     * The root container for the AccessDenied view.
     * Contains a label with an error message and a navigation button.
     */
    private final VBox root;


    /**
     * Constructs a new AccessDenied view.
     * <p>
     * Initializes the layout and components:
     * <ul>
     *     <li>A label displaying an "Access Denied" message.</li>
     *     <li>A button that navigates back to the dashboard when clicked.</li>
     * </ul>
     */
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

    /**
     * Returns the root JavaFX node of this view.
     *
     * @return the {@link VBox} containing the access denied message and button
     */
    public Parent getView() {
        return root;
    }
}
