package org.storemate.pos.services;

import org.storemate.pos.models.RolePermissions;
import org.storemate.pos.models.enums.Permission;
import org.storemate.pos.models.enums.Role;

public class AuthorizationService {
    private final Role role;
    public AuthorizationService(Role role){
        this.role = role;
    }
    public boolean hasPermission(Permission p){
        return RolePermissions.hasPermission(role, p);
    }
}
