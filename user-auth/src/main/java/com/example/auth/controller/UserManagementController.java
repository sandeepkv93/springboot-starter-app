package com.example.auth.controller;

import com.example.auth.dto.CurrentUserResponse;
import com.example.auth.dto.UpdateUserRolesRequest;
import com.example.auth.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserManagementController {
  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<CurrentUserResponse>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CurrentUserResponse> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @PutMapping("/{id}/roles")
  public ResponseEntity<Void> updateUserRoles(
      @PathVariable Long id, @Valid @RequestBody UpdateUserRolesRequest request) {
    userService.updateUserRoles(id, request);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
    userService.deleteUserById(id);
    return ResponseEntity.ok().build();
  }
}
