package com.geeksforless.client.security.auth;

import com.geeksforless.client.security.auth.dto.AuthenticationRequest;
import com.geeksforless.client.security.auth.dto.AuthenticationResponse;
import com.geeksforless.client.security.auth.dto.RegisterRequest;
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
  public void register(@RequestBody RegisterRequest request) {
    service.register(request);
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(service.authenticate(request));
  }
}
