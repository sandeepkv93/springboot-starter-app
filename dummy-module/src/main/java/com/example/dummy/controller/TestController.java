package com.example.dummy.controller;

import com.example.dummy.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

  private final TestService testService;

  @GetMapping("/public")
  public ResponseEntity<String> publicEndpoint() {
    return ResponseEntity.ok(testService.getPublicMessage());
  }

  @GetMapping("/protected")
  public ResponseEntity<String> protectedEndpoint() {
    return ResponseEntity.ok(testService.getProtectedMessage());
  }

  @GetMapping("/admin")
  public ResponseEntity<String> adminEndpoint() {
    return ResponseEntity.ok(testService.getAdminMessage());
  }
}
