package com.example.auth.dto;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRolesRequest {
  @NotEmpty(message = "Role names cannot be empty")
  private Set<
          @Pattern(
              regexp = "^ROLE_[A-Z]+$",
              message = "Role names must start with 'ROLE_' and contain only uppercase letters")
          String>
      roleNames;
}
