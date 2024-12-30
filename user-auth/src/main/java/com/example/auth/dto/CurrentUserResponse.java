package com.example.auth.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserResponse {
  private Long id;
  private String email;
  private String firstName;
  private String lastName;
  private Set<String> roles;
  private Set<String> permissions;
}
