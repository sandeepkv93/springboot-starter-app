package com.example.auth.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
  @Pattern(
      regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
      message =
          "Password must contain at least one digit, one lowercase, one uppercase, and one special character")
  private String password;

  @NotBlank(message = "First name is required")
  @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
  private String lastName;
}
