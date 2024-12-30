package com.example.auth.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth.dto.CreateRoleRequest;
import com.example.auth.dto.RoleResponse;
import com.example.auth.dto.UpdateRoleRequest;
import com.example.auth.exception.CustomException;
import com.example.auth.model.Permission;
import com.example.auth.model.Role;
import com.example.auth.model.RoleHierarchy;
import com.example.auth.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
  private final RoleRepository roleRepository;

  @PostConstruct
  public void initializeRoles() {
    if (!roleRepository.existsByName("ROLE_USER")) {
      roleRepository.save(Role.defaultUserRole());
    }

    if (!roleRepository.existsByName("ROLE_ADMIN")) {
      roleRepository.save(Role.adminRole());
    }
  }

  @PreAuthorize("hasAuthority('role:read')")
  public List<RoleResponse> getAllRoles() {
    return roleRepository.findAll().stream()
        .map(this::mapToRoleResponse)
        .collect(Collectors.toList());
  }

  @PreAuthorize("hasAuthority('role:read')")
  public RoleResponse getRoleById(Long id) {
    Role role = findRoleById(id);
    return mapToRoleResponse(role);
  }

  @PreAuthorize("hasAuthority('role:create')")
  @Transactional
  public RoleResponse createRole(CreateRoleRequest request) {
    validateRoleRequest(request.getName(), request.getPermissions());

    Set<Permission> permissions = RoleHierarchy.getInheritedPermissions(request.getName());
    permissions.addAll(request.getPermissions());

    Role role = Role.builder().name(request.getName()).permissions(permissions).build();

    role = roleRepository.save(role);
    return mapToRoleResponse(role);
  }

  @PreAuthorize("hasAuthority('role:update')")
  @Transactional
  public RoleResponse updateRole(Long id, UpdateRoleRequest request) {
    Role role = findRoleById(id);
    validateRoleUpdate(role, request.getName(), request.getPermissions());

    Set<Permission> permissions = RoleHierarchy.getInheritedPermissions(request.getName());
    permissions.addAll(request.getPermissions());

    role.setName(request.getName());
    role.setPermissions(permissions);

    role = roleRepository.save(role);
    return mapToRoleResponse(role);
  }

  @PreAuthorize("hasAuthority('role:delete')")
  @Transactional
  public void deleteRole(Long id) {
    Role role = findRoleById(id);

    if (isBuiltInRole(role.getName())) {
      throw new CustomException("Cannot delete built-in role", HttpStatus.BAD_REQUEST);
    }

    // Check if role is assigned to any users
    if (!role.getUsers().isEmpty()) {
      throw new CustomException(
          "Cannot delete role that is assigned to users", HttpStatus.BAD_REQUEST);
    }

    roleRepository.delete(role);
  }

  @Transactional(readOnly = true)
  public Role findRoleByName(String name) {
    return roleRepository
        .findByName(name)
        .orElseThrow(() -> new CustomException("Role not found: " + name, HttpStatus.NOT_FOUND));
  }

  private Role findRoleById(Long id) {
    return roleRepository
        .findById(id)
        .orElseThrow(() -> new CustomException("Role not found", HttpStatus.NOT_FOUND));
  }

  private void validateRoleRequest(String name, Set<Permission> permissions) {
    if (roleRepository.existsByName(name)) {
      throw new CustomException("Role already exists", HttpStatus.BAD_REQUEST);
    }
    validateRoleName(name);
    validatePermissions(permissions);
  }

  private void validateRoleUpdate(Role role, String newName, Set<Permission> newPermissions) {
    if (isBuiltInRole(role.getName())) {
      throw new CustomException("Cannot modify built-in role", HttpStatus.BAD_REQUEST);
    }

    if (!role.getName().equals(newName) && roleRepository.existsByName(newName)) {
      throw new CustomException("Role name already exists", HttpStatus.BAD_REQUEST);
    }

    validateRoleName(newName);
    validatePermissions(newPermissions);
  }

  private void validateRoleName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new CustomException("Role name cannot be empty", HttpStatus.BAD_REQUEST);
    }

    if (!name.startsWith("ROLE_")) {
      throw new CustomException("Role name must start with 'ROLE_'", HttpStatus.BAD_REQUEST);
    }

    if (name.length() < 6) { // "ROLE_" + at least one character
      throw new CustomException("Role name too short", HttpStatus.BAD_REQUEST);
    }

    if (!name.matches("^ROLE_[A-Z0-9_]+$")) {
      throw new CustomException(
          "Role name must contain only uppercase letters, numbers, and underscores",
          HttpStatus.BAD_REQUEST);
    }
  }

  private void validatePermissions(Set<Permission> permissions) {
    if (permissions == null || permissions.isEmpty()) {
      throw new CustomException("Permissions cannot be empty", HttpStatus.BAD_REQUEST);
    }
  }

  private boolean isBuiltInRole(String roleName) {
    try {
      RoleHierarchy.valueOf(roleName);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  private RoleResponse mapToRoleResponse(Role role) {
    return RoleResponse.builder()
        .id(role.getId())
        .name(role.getName())
        .permissions(role.getPermissions())
        .build();
  }
}
