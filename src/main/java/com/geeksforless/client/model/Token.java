package com.geeksforless.client.model;


import com.geeksforless.client.model.enums.TokenType;
import jakarta.persistence.*;
import java.util.Objects;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Token {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  public Long id;
  @Column(unique = true)
  public String token;
  @Enumerated(EnumType.STRING)
  public TokenType tokenType = TokenType.BEARER;
  public boolean revoked;
  public boolean expired;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  public User user;

  public Token() {
  }

  public Token(Long id, String token, TokenType tokenType, boolean revoked, boolean expired, User user) {
    this.id = id;
    this.token = token;
    this.tokenType = tokenType;
    this.revoked = revoked;
    this.expired = expired;
    this.user = user;
  }

  public Token(String token, TokenType tokenType, boolean revoked, boolean expired, User user) {
    this.token = token;
    this.tokenType = tokenType;
    this.revoked = revoked;
    this.expired = expired;
    this.user = user;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public TokenType getTokenType() {
    return tokenType;
  }

  public void setTokenType(TokenType tokenType) {
    this.tokenType = tokenType;
  }

  public boolean isRevoked() {
    return revoked;
  }

  public void setRevoked(boolean revoked) {
    this.revoked = revoked;
  }

  public boolean isExpired() {
    return expired;
  }

  public void setExpired(boolean expired) {
    this.expired = expired;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Token token1 = (Token) o;
    return revoked == token1.revoked && expired == token1.expired && Objects.equals(id, token1.id) && Objects.equals(token, token1.token) && tokenType == token1.tokenType && Objects.equals(user, token1.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, token, tokenType, revoked, expired, user);
  }
}
