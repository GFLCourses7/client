package com.geeksforless.client.security.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.geeksforless.client.exception.DuplicateLoginException;
import com.geeksforless.client.model.Token;
import com.geeksforless.client.model.User;
import com.geeksforless.client.model.enums.Role;
import com.geeksforless.client.model.enums.TokenType;
import com.geeksforless.client.repository.TokenRepository;
import com.geeksforless.client.repository.UserRepository;
import com.geeksforless.client.security.config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationService(UserRepository repository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
    this.repository = repository;
    this.tokenRepository = tokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  public void register(RegisterRequest request) {

    User user = new User(
            request.getLogin(),
            passwordEncoder.encode(request.getPassword()),
            Role.USER
    );
    try {
      repository.save(user);
    } catch (DataIntegrityViolationException ex) {
      throw new DuplicateLoginException("User with login " + user.getUsername() + " already exists");
    }
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getLogin(),
            request.getPassword()
        )
    );

    var user = repository.findByLogin(request.getLogin())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);

    return new AuthenticationResponse(
            jwtToken, refreshToken, user.getRole().name(), user.getId()
    );
  }

  private void saveUserToken(User user, String jwtToken) {

    Token token = new Token(
            jwtToken,
            TokenType.BEARER,
            false,
            false,
            user
    );
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    final String authHeader = request.getHeader("Authorization");
    final String refreshToken;
    final String userLogin;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userLogin = jwtService.extractUsername(refreshToken);
    if (userLogin != null) {
      var user = this.repository.findByLogin(userLogin)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        AuthenticationResponse authResponse = new AuthenticationResponse(
                accessToken, refreshToken
        );
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
