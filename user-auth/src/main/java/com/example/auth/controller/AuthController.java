package com.example.auth.controller;

import com.example.auth.dto.*;
import com.example.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
    return ResponseEntity.ok(userService.signup(request));
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(userService.login(request));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@RequestHeader("Authorization") @Valid String token) {
    userService.logout(token);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/change-password")
  public ResponseEntity<Void> changePassword(
      @RequestHeader("Authorization") String token,
      @RequestParam @Valid String oldPassword,
      @RequestParam @Valid String newPassword) {
    userService.changePassword(token, oldPassword, newPassword);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") @Valid String token) {
    userService.deleteUser(token);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/admin/create")
  public ResponseEntity<SignupResponse> createAdmin(@Valid @RequestBody SignupRequest request) {
    return ResponseEntity.ok(userService.createAdminUser(request));
  }

  @GetMapping("/profile")
  public ResponseEntity<CurrentUserResponse> getCurrentUser(Authentication authentication) {
    return ResponseEntity.ok(userService.getCurrentUser(authentication));
  }
}
