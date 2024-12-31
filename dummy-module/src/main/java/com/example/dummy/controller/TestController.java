package com.example.dummy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dummy.service.TestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

  private final TestService testService;

  @GetMapping("/public")
  public ResponseEntity<String> publicEndpoint() {
    return ResponseEntity.ok(testService.getPublicMessage());
  }

  @GetMapping("/authenticated")
  public ResponseEntity<String> authenticatedEndpoint() {
    return ResponseEntity.ok(testService.getAuthenticatedMessage());
  }

  @GetMapping("/user")
  public ResponseEntity<String> userEndpoint() {
    return ResponseEntity.ok(testService.getUserMessage());
  }

  @GetMapping("/admin")
  public ResponseEntity<String> adminEndpoint() {
    return ResponseEntity.ok(testService.getAdminMessage());
  }

  @GetMapping("/manager")
  public ResponseEntity<String> managerEndpoint() {
    return ResponseEntity.ok(testService.getManagerMessage());
  }

  @GetMapping("/admin-or-manager")
  public ResponseEntity<String> adminOrManagerEndpoint() {
    return ResponseEntity.ok(testService.getAdminOrManagerMessage());
  }

  @GetMapping("/user-and-manager")
  public ResponseEntity<String> userAndManagerEndpoint() {
    return ResponseEntity.ok(testService.getUserAndManagerMessage());
  }

  @GetMapping("/nested-roles")
  public ResponseEntity<String> nestedRolesEndpoint() {
    return ResponseEntity.ok(testService.getNestedRolesMessage());
  }

  @GetMapping("/permission-based")
  public ResponseEntity<String> permissionBasedEndpoint() {
    return ResponseEntity.ok(testService.getPermissionBasedMessage());
  }

  @GetMapping("/dynamic-permission")
  public ResponseEntity<String> dynamicPermissionEndpoint(@RequestParam String resource) {
    return ResponseEntity.ok(testService.getDynamicPermissionMessage(resource));
  }
}
