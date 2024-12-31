package com.example.dummy.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class TestService {

  public String getPublicMessage() {
    return "This is a public endpoint - anyone can access it";
  }

  @PreAuthorize("isAuthenticated()")
  public String getAuthenticatedMessage() {
    return "This endpoint requires authentication - any logged in user can access it";
  }

  @PreAuthorize("hasRole('USER')")
  public String getUserMessage() {
    return "This endpoint requires USER role";
  }

  @PreAuthorize("hasRole('ADMIN')")
  public String getAdminMessage() {
    return "This endpoint requires ADMIN role";
  }

  @PreAuthorize("hasRole('MANAGER')")
  public String getManagerMessage() {
    return "This endpoint requires MANAGER role";
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
  public String getAdminOrManagerMessage() {
    return "This endpoint requires either ADMIN or MANAGER role";
  }

  @PreAuthorize("hasRole('USER') and hasRole('MANAGER')")
  public String getUserAndManagerMessage() {
    return "This endpoint requires both USER and MANAGER roles";
  }

  @PreAuthorize("hasRole('ADMIN') and hasRole('MANAGER')")
  public String getNestedRolesMessage() {
    return "This endpoint requires both ADMIN and MANAGER roles - demonstrating nested role requirements";
  }

  @PreAuthorize("hasAuthority('user:read')")
  public String getPermissionBasedMessage() {
    return "This endpoint requires specific permission (user:read) rather than a role";
  }

  @PreAuthorize("hasAuthority(#resource + ':read')")
  public String getDynamicPermissionMessage(String resource) {
    return "This endpoint requires dynamic permission: " + resource + ":read";
  }
}
