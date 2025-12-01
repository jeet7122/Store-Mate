package org.storemate.pos.routing;

import javafx.scene.Parent;
import org.storemate.pos.models.enums.Permission;

import java.util.function.Supplier;


/**
 * Represents a route in the application.
 * Each route has a view builder (to create the UI) and an optional permission required
 * to access it.
 * @author Jeet
 */
public class Route {
    private final Supplier<Parent> viewBuilder;
    private final Permission requiredPermission;

    /**
     * Constructs a new Route.
     *
     * @param viewBuilder      a Supplier that builds the JavaFX Parent (UI component) for this route
     * @param permission       the Permission required to access this route, can be null if no permission is required
     */
    public Route(Supplier<Parent> viewBuilder, Permission permission){
        this.viewBuilder = viewBuilder;
        this.requiredPermission = permission;
    }
    /**
     * Returns the Supplier that builds the view (UI) for this route.
     *
     * @return Supplier of Parent representing the view
     */
    public Supplier<Parent> getViewBuilder() {
        return viewBuilder;
    }


    /**
     * Returns the permission required to access this route.
     *
     * @return Permission required for access
     */
    public Permission getRequiredPermission() {
        return requiredPermission;
    }
}
