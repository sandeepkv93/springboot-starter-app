package com.example.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponse {
  private Long id;
  private String email;
  private String firstName;
  private String lastName;
  private String message;
}
