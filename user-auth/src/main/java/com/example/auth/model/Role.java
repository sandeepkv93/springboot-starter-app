package com.example.auth.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String name;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
  @Enumerated(EnumType.STRING)
  private Set<Permission> permissions = new HashSet<>();

  @ManyToMany(mappedBy = "roles")
  private Set<User> users = new HashSet<>();

  // Factory methods for default roles
  public static Role defaultUserRole() {
    return Role.builder().name("ROLE_USER").permissions(Set.of(Permission.USER_READ)).build();
  }

  public static Role adminRole() {
    return Role.builder()
        .name("ROLE_ADMIN")
        .permissions(
            Set.of(
                Permission.USER_READ,
                Permission.USER_CREATE,
                Permission.USER_UPDATE,
                Permission.USER_DELETE,
                Permission.ROLE_READ,
                Permission.ROLE_CREATE,
                Permission.ROLE_UPDATE,
                Permission.ROLE_DELETE,
                Permission.ADMIN_ACCESS))
        .build();
  }

  // Override toString to prevent infinite recursion
  @Override
  public String toString() {
    return "Role{" + "id=" + id + ", name='" + name + '\'' + ", permissions=" + permissions + '}';
  }

  // Override equals and hashCode to prevent infinite recursion
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Role)) return false;
    Role role = (Role) o;
    return getId() != null && getId().equals(role.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
