package com.example.auth.service;

import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.SignupRequest;
import com.example.auth.dto.SignupResponse;
import com.example.auth.dto.TokenResponse;
import com.example.auth.exception.CustomException;
import com.example.auth.model.User;
import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public SignupResponse signup(SignupRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new CustomException("Email already exists", HttpStatus.BAD_REQUEST);
    }

    User user =
        User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
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

  public void logout(String token) {
    // In a real application, you might want to blacklist the token
    // For now, we'll just validate it
    String jwt = token.substring(7);
    jwtService.extractUsername(jwt); // This will throw if token is invalid
  }

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

  public void deleteUser(String token) {
    String userEmail = jwtService.extractUsername(token.substring(7));
    User user =
        userRepository
            .findByEmail(userEmail)
            .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

    userRepository.delete(user);
  }
}
