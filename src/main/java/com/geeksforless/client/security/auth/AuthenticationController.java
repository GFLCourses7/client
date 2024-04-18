package com.geeksforless.client.security.auth;

import com.geeksforless.client.security.auth.dto.AuthRequest;
import com.geeksforless.client.security.auth.dto.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class AuthenticationController {

  private final AuthenticationService service;

  public AuthenticationController(AuthenticationService service) {
      this.service = service;
  }

  @PostMapping("/register")
  public void register(@RequestBody AuthRequest request) {
    service.register(request);
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
    return ResponseEntity.ok(service.authenticate(request));
  }
}
