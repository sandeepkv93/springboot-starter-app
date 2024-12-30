package com.example.auth.dto;

import java.util.Set;

import com.example.auth.model.Permission;

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
