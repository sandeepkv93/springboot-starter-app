package com.example.auth.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

@Getter
public enum RoleHierarchy {
  ROLE_USER(Set.of(Permission.USER_READ)),

  ROLE_MANAGER(Set.of(Permission.USER_READ, Permission.USER_UPDATE, Permission.ROLE_READ)),

  ROLE_ADMIN(
      Set.of(
          Permission.USER_READ,
          Permission.USER_CREATE,
          Permission.USER_UPDATE,
          Permission.USER_DELETE,
          Permission.ROLE_READ,
          Permission.ROLE_CREATE,
          Permission.ROLE_UPDATE,
          Permission.ROLE_DELETE,
          Permission.ADMIN_ACCESS));

  private final Set<Permission> permissions;

  RoleHierarchy(Set<Permission> permissions) {
    this.permissions = permissions;
  }

  public static Set<Permission> getInheritedPermissions(String roleName) {
    Set<Permission> allPermissions = new HashSet<>();

    // Get all roles that should be inherited
    RoleHierarchy[] allRoles = RoleHierarchy.values();
    for (RoleHierarchy role : allRoles) {
      if (shouldInheritRole(roleName, role.name())) {
        allPermissions.addAll(role.getPermissions());
      }
    }

    return allPermissions;
  }

  private static boolean shouldInheritRole(String targetRole, String roleToCheck) {
    if (targetRole.equals(roleToCheck)) {
      return true;
    }

    // Define inheritance rules
    if (targetRole.equals(ROLE_ADMIN.name())) {
      return true; // Admin inherits all permissions
    }
    if (targetRole.equals(ROLE_MANAGER.name()) && roleToCheck.equals(ROLE_USER.name())) {
      return true; // Manager inherits User permissions
    }

    return false;
  }
}
