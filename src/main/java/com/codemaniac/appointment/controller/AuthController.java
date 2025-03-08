package com.codemaniac.appointment.controller;

import com.codemaniac.appointment.model.AuthRequest;
import com.codemaniac.appointment.model.AuthResponse;
import com.codemaniac.appointment.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
    String jwt =
        authService.authenticateAndGenerateToken(
            authRequest.getUsername(), authRequest.getPassword());

    return ResponseEntity.ok(new AuthResponse(jwt));
  }
}
