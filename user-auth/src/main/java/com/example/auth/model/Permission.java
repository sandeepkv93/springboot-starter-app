package com.example.auth.model;

import lombok.Getter;

@Getter
public enum Permission {
  // User management permissions
  USER_READ("user:read"),
  USER_CREATE("user:create"),
  USER_UPDATE("user:update"),
  USER_DELETE("user:delete"),

  // Role management permissions
  ROLE_READ("role:read"),
  ROLE_CREATE("role:create"),
  ROLE_UPDATE("role:update"),
  ROLE_DELETE("role:delete"),

  // Admin permissions
  ADMIN_ACCESS("admin:access");

  private final String permission;

  Permission(String permission) {
    this.permission = permission;
  }
}
