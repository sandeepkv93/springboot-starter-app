package com.example.dummy.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import com.example.dummy.service.TestService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestControllerTest {

  @Mock private TestService testService;

  @InjectMocks private TestController testController;

  @Nested
  class PublicEndpoint {
    @Test
    void shouldReturnPublicMessage() {
      // Arrange
      String expectedMessage = "public message";
      when(testService.getPublicMessage()).thenReturn(expectedMessage);

      // Act
      ResponseEntity<String> response = testController.publicEndpoint();

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isEqualTo(expectedMessage);
      verify(testService, times(1)).getPublicMessage();
    }
  }

  @Nested
  class AuthenticatedEndpoint {
    @Test
    void shouldReturnMessageWhenAuthenticated() {
      // Arrange
      String expectedMessage = "authenticated message";
      when(testService.getAuthenticatedMessage()).thenReturn(expectedMessage);

      // Act
      ResponseEntity<String> response = testController.authenticatedEndpoint();

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isEqualTo(expectedMessage);
      verify(testService, times(1)).getAuthenticatedMessage();
    }

    @Test
    void shouldHandleServiceException() {
      // Arrange
      when(testService.getAuthenticatedMessage())
          .thenThrow(new AccessDeniedException("Not authenticated"));

      // Act & Assert
      assertThrows(AccessDeniedException.class, () -> testController.authenticatedEndpoint());
      verify(testService, times(1)).getAuthenticatedMessage();
    }
  }

  @Nested
  class UserEndpoint {
    @Test
    void shouldReturnMessageForUserRole() {
      // Arrange
      String expectedMessage = "user message";
      when(testService.getUserMessage()).thenReturn(expectedMessage);

      // Act
      ResponseEntity<String> response = testController.userEndpoint();

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isEqualTo(expectedMessage);
      verify(testService, times(1)).getUserMessage();
    }
  }

  @Nested
  class AdminEndpoint {
    @Test
    void shouldReturnMessageForAdminRole() {
      // Arrange
      String expectedMessage = "admin message";
      when(testService.getAdminMessage()).thenReturn(expectedMessage);

      // Act
      ResponseEntity<String> response = testController.adminEndpoint();

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isEqualTo(expectedMessage);
      verify(testService, times(1)).getAdminMessage();
    }
  }

  @Nested
  class ManagerEndpoint {
    @Test
    void shouldReturnMessageForManagerRole() {
      // Arrange
      String expectedMessage = "manager message";
      when(testService.getManagerMessage()).thenReturn(expectedMessage);

      // Act
      ResponseEntity<String> response = testController.managerEndpoint();

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isEqualTo(expectedMessage);
      verify(testService, times(1)).getManagerMessage();
    }
  }

  @Nested
  class AdminOrManagerEndpoint {
    @Test
    void shouldReturnMessageForAdminOrManagerRole() {
      // Arrange
      String expectedMessage = "admin or manager message";
      when(testService.getAdminOrManagerMessage()).thenReturn(expectedMessage);

      // Act
      ResponseEntity<String> response = testController.adminOrManagerEndpoint();

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isEqualTo(expectedMessage);
      verify(testService, times(1)).getAdminOrManagerMessage();
    }
  }

  @Nested
  class UserAndManagerEndpoint {
    @Test
    void shouldReturnMessageForUserAndManagerRole() {
      // Arrange
      String expectedMessage = "user and manager message";
      when(testService.getUserAndManagerMessage()).thenReturn(expectedMessage);

      // Act
      ResponseEntity<String> response = testController.userAndManagerEndpoint();

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isEqualTo(expectedMessage);
      verify(testService, times(1)).getUserAndManagerMessage();
    }
  }

  @Nested
  class NestedRolesEndpoint {
    @Test
    void shouldReturnMessageForNestedRoles() {
      // Arrange
      String expectedMessage = "nested roles message";
      when(testService.getNestedRolesMessage()).thenReturn(expectedMessage);

      // Act
      ResponseEntity<String> response = testController.nestedRolesEndpoint();

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isEqualTo(expectedMessage);
      verify(testService, times(1)).getNestedRolesMessage();
    }
  }

  @Nested
  class PermissionBasedEndpoint {
    @Test
    void shouldReturnMessageForPermissionBased() {
      // Arrange
      String expectedMessage = "permission based message";
      when(testService.getPermissionBasedMessage()).thenReturn(expectedMessage);

      // Act
      ResponseEntity<String> response = testController.permissionBasedEndpoint();

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isEqualTo(expectedMessage);
      verify(testService, times(1)).getPermissionBasedMessage();
    }
  }

  @Nested
  class DynamicPermissionEndpoint {
    @Test
    void shouldReturnMessageForDynamicPermission() {
      // Arrange
      String resource = "user";
      String expectedMessage = "dynamic permission message";
      when(testService.getDynamicPermissionMessage(resource)).thenReturn(expectedMessage);

      // Act
      ResponseEntity<String> response = testController.dynamicPermissionEndpoint(resource);

      // Assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isEqualTo(expectedMessage);
      verify(testService, times(1)).getDynamicPermissionMessage(resource);
    }
  }
}
