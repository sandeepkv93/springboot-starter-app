package com.example.auth.controller;

import com.example.auth.dto.CreateRoleRequest;
import com.example.auth.dto.RoleResponse;
import com.example.auth.dto.UpdateRoleRequest;
import com.example.auth.service.RoleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
  private final RoleService roleService;

  @GetMapping
  public ResponseEntity<List<RoleResponse>> getAllRoles() {
    return ResponseEntity.ok(roleService.getAllRoles());
  }

  @GetMapping("/{id}")
  public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long id) {
    return ResponseEntity.ok(roleService.getRoleById(id));
  }

  @PostMapping
  public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRoleRequest request) {
    return ResponseEntity.ok(roleService.createRole(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<RoleResponse> updateRole(
      @PathVariable Long id, @Valid @RequestBody UpdateRoleRequest request) {
    return ResponseEntity.ok(roleService.updateRole(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
    roleService.deleteRole(id);
    return ResponseEntity.ok().build();
  }
}
