package org.storemate.pos.models;

import javafx.scene.Parent;
import org.storemate.pos.models.enums.Permission;

import java.util.function.Supplier;

public class Route {
    private final Supplier<Parent> viewBuilder;
    private final Permission requiredPermission;
    public Route(Supplier<Parent> viewBuilder, Permission permission){
        this.viewBuilder = viewBuilder;
        this.requiredPermission = permission;
    }
    public Supplier<Parent> getViewBuilder() {
        return viewBuilder;
    }

    public Permission getRequiredPermission() {
        return requiredPermission;
    }
}
