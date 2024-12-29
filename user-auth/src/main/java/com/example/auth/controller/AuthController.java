package com.example.auth.controller;

import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.SignupRequest;
import com.example.auth.dto.SignupResponse;
import com.example.auth.dto.TokenResponse;
import com.example.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
    return ResponseEntity.ok(userService.signup(request));
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
    return ResponseEntity.ok(userService.login(request));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
    userService.logout(token);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/change-password")
  public ResponseEntity<Void> changePassword(
      @RequestHeader("Authorization") String token,
      @RequestParam String oldPassword,
      @RequestParam String newPassword) {
    userService.changePassword(token, oldPassword, newPassword);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String token) {
    userService.deleteUser(token);
    return ResponseEntity.ok().build();
  }
}
