package org.storemate.pos.services;

import org.storemate.pos.models.RolePermissions;
import org.storemate.pos.models.enums.Permission;
import org.storemate.pos.models.enums.Role;

/**
 * Service class responsible for checking whether a given {@link Role} has specific {@link Permission}.
 * <p>
 * This class acts as a layer of abstraction over the {@link RolePermissions} utility, allowing
 * for role-based authorization checks in the application.
 * </p>
 */
public class AuthorizationService {

    private final Role role;

    /**
     * Creates a new AuthorizationService for the given role.
     *
     * @param role the {@link Role} of the current user for which permissions will be checked
     */
    public AuthorizationService(Role role) {
        this.role = role;
    }

    /**
     * Checks whether the current role has the specified permission.
     *
     * @param p the {@link Permission} to check
     * @return {@code true} if the role has the permission; {@code false} otherwise
     */
    public boolean hasPermission(Permission p) {
        return RolePermissions.hasPermission(role, p);
    }
}
