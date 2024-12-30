package com.example.auth.service;

import com.example.auth.dto.*;
import com.example.auth.exception.CustomException;
import com.example.auth.model.Permission;
import com.example.auth.model.Role;
import com.example.auth.model.User;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Transactional
  public SignupResponse signup(SignupRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new CustomException("Email already exists", HttpStatus.BAD_REQUEST);
    }

    // Get default user role
    Role userRole =
        roleRepository
            .findByName("ROLE_USER")
            .orElseThrow(
                () ->
                    new CustomException(
                        "Default role not found", HttpStatus.INTERNAL_SERVER_ERROR));

    User user =
        User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .roles(Set.of(userRole))
            .build();

    user = userRepository.save(user);

    return SignupResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .message("User registered successfully")
        .build();
  }

  public TokenResponse login(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    User user =
        userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

    String token = jwtService.generateToken(user);

    return TokenResponse.builder().accessToken(token).build();
  }

  @PreAuthorize("hasAuthority('user:update')")
  @Transactional
  public void updateUserRoles(Long userId, UpdateUserRolesRequest request) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

    Set<Role> newRoles =
        request.getRoleNames().stream()
            .map(
                roleName ->
                    roleRepository
                        .findByName(roleName)
                        .orElseThrow(
                            () ->
                                new CustomException(
                                    "Role not found: " + roleName, HttpStatus.NOT_FOUND)))
            .collect(Collectors.toSet());

    user.setRoles(newRoles);
    userRepository.save(user);
  }

  @PreAuthorize("hasAuthority('user:read')")
  public Set<RoleResponse> getUserRoles(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

    return user.getRoles().stream()
        .map(
            role ->
                RoleResponse.builder()
                    .id(role.getId())
                    .name(role.getName())
                    .permissions(role.getPermissions())
                    .build())
        .collect(Collectors.toSet());
  }

  public void logout(String token) {
    String jwt = token.substring(7);
    jwtService.extractUsername(jwt); // This will throw if token is invalid
  }

  @PreAuthorize("hasAuthority('user:update') or #email == authentication.name")
  @Transactional
  public void changePassword(String token, String oldPassword, String newPassword) {
    String userEmail = jwtService.extractUsername(token.substring(7));
    User user =
        userRepository
            .findByEmail(userEmail)
            .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new CustomException("Invalid old password", HttpStatus.BAD_REQUEST);
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
  }

  @PreAuthorize("hasAuthority('user:delete') or #email == authentication.name")
  @Transactional
  public void deleteUser(String token) {
    String userEmail = jwtService.extractUsername(token.substring(7));
    User user =
        userRepository
            .findByEmail(userEmail)
            .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

    userRepository.delete(user);
  }

  @PreAuthorize("hasAuthority('admin:access')")
  public SignupResponse createAdminUser(SignupRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new CustomException("Email already exists", HttpStatus.BAD_REQUEST);
    }

    Role adminRole =
        roleRepository
            .findByName("ROLE_ADMIN")
            .orElseThrow(
                () ->
                    new CustomException("Admin role not found", HttpStatus.INTERNAL_SERVER_ERROR));

    User admin =
        User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .roles(Set.of(adminRole))
            .build();

    admin = userRepository.save(admin);

    return SignupResponse.builder()
        .id(admin.getId())
        .email(admin.getEmail())
        .firstName(admin.getFirstName())
        .lastName(admin.getLastName())
        .message("Admin user created successfully")
        .build();
  }

  @PreAuthorize("hasAuthority('user:read')")
  public List<CurrentUserResponse> getAllUsers() {
    return userRepository.findAll().stream()
        .map(this::mapToCurrentUserResponse)
        .collect(Collectors.toList());
  }

  @PreAuthorize("hasAuthority('user:read')")
  public CurrentUserResponse getUserById(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
    return mapToCurrentUserResponse(user);
  }

  @PreAuthorize("hasAuthority('user:delete')")
  @Transactional
  public void deleteUserById(Long id) {
    if (!userRepository.existsById(id)) {
      throw new CustomException("User not found", HttpStatus.NOT_FOUND);
    }
    userRepository.deleteById(id);
  }

  public CurrentUserResponse getCurrentUser(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new CustomException("Not authenticated", HttpStatus.UNAUTHORIZED);
    }

    User user =
        userRepository
            .findByEmail(authentication.getName())
            .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

    return mapToCurrentUserResponse(user);
  }

  private CurrentUserResponse mapToCurrentUserResponse(User user) {
    Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

    Set<String> permissions =
        user.getRoles().stream()
            .flatMap(role -> role.getPermissions().stream())
            .map(Permission::getPermission)
            .collect(Collectors.toSet());

    return CurrentUserResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .roles(roles)
        .permissions(permissions)
        .build();
  }
}
