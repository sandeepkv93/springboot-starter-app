package com.example.dummy.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class TestService {

  public String getPublicMessage() {
    return "This is a public endpoint - anyone can access it";
  }

  @PreAuthorize("isAuthenticated()")
  public String getProtectedMessage() {
    return "This is a protected endpoint - only authenticated users can access it";
  }
}
