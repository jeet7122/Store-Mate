package org.storemate.pos.models;

import org.storemate.pos.models.enums.Permission;
import org.storemate.pos.models.enums.Role;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Maintains a mapping of Roles to their associated Permissions in the POS system.
 * <p>
 * This class defines which actions each role is allowed to perform and provides
 * a utility method to check if a specific role has a given permission.
 * @author Khushi, Jeet
 */
public class RolePermissions {
    private static final Map<Role, Set<Permission>> permissions = new HashMap<>();
    //Added set of rules of Permissions based on role
    static {
        permissions.put(Role.ADMIN, Set.of(Permission.values()));
        permissions.put(Role.CASHIER, Set.of(
                Permission.PROCESS_SALES,
                Permission.PROCESS_REFUND,
                Permission.VIEW_INVENTORY,
                Permission.VIEW_DASHBOARD,
                Permission.MANAGE_USER_PROFILE,
                Permission.VIEW_CATEGORIES,
                Permission.VIEW_PRODUCTS,
                Permission.VIEW_TRANSACTIONS,
                Permission.MANAGE_SETTINGS
        ));

    }

    /**
     *
     * @param role (ENUM)
     * @param permission (ENUM)
     * @return boolean Value
     * checks in hashmap whether role has a permission/not
     */
    public static boolean hasPermission(Role role, Permission permission){
        return permissions.get(role).contains(permission);
    }
}
