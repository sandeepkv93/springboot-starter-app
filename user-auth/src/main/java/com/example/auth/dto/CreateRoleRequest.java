package com.example.auth.dto;

import com.example.auth.model.Permission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoleRequest {
  @NotBlank(message = "Role name is required")
  @Pattern(
      regexp = "^ROLE_[A-Z]+$",
      message = "Role name must start with 'ROLE_' and contain only uppercase letters")
  private String name;

  @NotEmpty(message = "Permissions cannot be empty")
  private Set<Permission> permissions;
}
