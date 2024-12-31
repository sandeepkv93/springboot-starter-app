package com.example.dummy.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class TestServiceTest {

  @Configuration
  @EnableMethodSecurity
  static class TestConfig {
    @Bean
    public TestService testService() {
      return new TestService();
    }
  }

  @Autowired private TestService testService;

  @Nested
  class PublicEndpoint {
    @Test
    void shouldAllowAccessWithoutAuthentication() {
      String message = testService.getPublicMessage();
      assertThat(message).isNotEmpty();
    }
  }

  @Nested
  class AuthenticatedEndpoint {
    @Test
    void shouldDenyAccessWhenNotAuthenticated() {
      assertThrows(
          AuthenticationCredentialsNotFoundException.class,
          () -> testService.getAuthenticatedMessage());
    }

    @Test
    @WithMockUser
    void shouldAllowAccessWhenAuthenticated() {
      String message = testService.getAuthenticatedMessage();
      assertThat(message).contains("requires authentication");
    }
  }

  @Nested
  class UserEndpoint {
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDenyAccessWithoutUserRole() {
      assertThrows(AccessDeniedException.class, () -> testService.getUserMessage());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldAllowAccessWithUserRole() {
      String message = testService.getUserMessage();
      assertThat(message).contains("USER role");
    }
  }

  @Nested
  class AdminEndpoint {
    @Test
    @WithMockUser(roles = "USER")
    void shouldDenyAccessWithoutAdminRole() {
      assertThrows(AccessDeniedException.class, () -> testService.getAdminMessage());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessWithAdminRole() {
      String message = testService.getAdminMessage();
      assertThat(message).contains("ADMIN role");
    }
  }

  @Nested
  class ManagerEndpoint {
    @Test
    @WithMockUser(roles = "USER")
    void shouldDenyAccessWithoutManagerRole() {
      assertThrows(AccessDeniedException.class, () -> testService.getManagerMessage());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldAllowAccessWithManagerRole() {
      String message = testService.getManagerMessage();
      assertThat(message).contains("MANAGER role");
    }
  }

  @Nested
  class AdminOrManagerEndpoint {
    @Test
    @WithMockUser(roles = "USER")
    void shouldDenyAccessWithoutAdminOrManagerRole() {
      assertThrows(AccessDeniedException.class, () -> testService.getAdminOrManagerMessage());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAccessWithAdminRole() {
      String message = testService.getAdminOrManagerMessage();
      assertThat(message).contains("either ADMIN or MANAGER");
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldAllowAccessWithManagerRole() {
      String message = testService.getAdminOrManagerMessage();
      assertThat(message).contains("either ADMIN or MANAGER");
    }
  }

  @Nested
  class UserAndManagerEndpoint {
    @Test
    @WithMockUser(roles = "USER")
    void shouldDenyAccessWithOnlyUserRole() {
      assertThrows(AccessDeniedException.class, () -> testService.getUserAndManagerMessage());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldDenyAccessWithOnlyManagerRole() {
      assertThrows(AccessDeniedException.class, () -> testService.getUserAndManagerMessage());
    }

    @Test
    @WithMockUser(roles = {"USER", "MANAGER"})
    void shouldAllowAccessWithBothRoles() {
      String message = testService.getUserAndManagerMessage();
      assertThat(message).contains("both USER and MANAGER");
    }
  }

  @Nested
  class NestedRolesEndpoint {
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDenyAccessWithOnlyAdminRole() {
      assertThrows(AccessDeniedException.class, () -> testService.getNestedRolesMessage());
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    void shouldDenyAccessWithOnlyManagerRole() {
      assertThrows(AccessDeniedException.class, () -> testService.getNestedRolesMessage());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER"})
    void shouldAllowAccessWithBothRoles() {
      String message = testService.getNestedRolesMessage();
      assertThat(message).contains("both ADMIN and MANAGER");
    }
  }

  @Nested
  class PermissionBasedEndpoint {
    @Test
    @WithMockUser(authorities = "user:read")
    void shouldAllowAccessWithCorrectPermission() {
      String message = testService.getPermissionBasedMessage();
      assertThat(message).contains("specific permission");
    }

    @Test
    @WithMockUser(authorities = "user:write")
    void shouldDenyAccessWithWrongPermission() {
      assertThrows(AccessDeniedException.class, () -> testService.getPermissionBasedMessage());
    }
  }

  @Nested
  class DynamicPermissionEndpoint {
    @Test
    @WithMockUser(authorities = "user:read")
    void shouldAllowAccessWithMatchingResourcePermission() {
      String message = testService.getDynamicPermissionMessage("user");
      assertThat(message).contains("dynamic permission: user:read");
    }

    @Test
    @WithMockUser(authorities = "role:read")
    void shouldDenyAccessWithNonMatchingResourcePermission() {
      assertThrows(
          AccessDeniedException.class, () -> testService.getDynamicPermissionMessage("user"));
    }
  }
}
