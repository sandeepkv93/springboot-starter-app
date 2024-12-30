package com.example.auth.dto;

import com.example.auth.model.Permission;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
  private Long id;
  private String name;
  private Set<Permission> permissions;
}
