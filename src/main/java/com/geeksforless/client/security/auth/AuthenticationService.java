package com.geeksforless.client.security.auth;


import com.geeksforless.client.exception.DuplicateLoginException;
import com.geeksforless.client.model.User;
import com.geeksforless.client.model.enums.Role;
import com.geeksforless.client.model.enums.TokenType;
import com.geeksforless.client.repository.TokenRepository;
import com.geeksforless.client.repository.UserRepository;
import com.geeksforless.client.security.auth.dto.AuthenticationRequest;
import com.geeksforless.client.security.auth.dto.AuthenticationResponse;
import com.geeksforless.client.security.auth.dto.RegisterRequest;
import com.geeksforless.client.security.auth.dto.Token;
import com.geeksforless.client.security.config.JwtService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    var user = repository.findByUserName(request.getLogin())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);

    return new AuthenticationResponse(
            jwtToken, user.getRole().name(), user.getId()
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
}