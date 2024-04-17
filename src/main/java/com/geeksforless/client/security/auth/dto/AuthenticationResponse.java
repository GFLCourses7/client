package com.geeksforless.client.security.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class AuthenticationResponse {
  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("role")
  private String role;
  @JsonProperty("userId")
  private Long userId;

  public AuthenticationResponse(String accessToken, String role, Long userId) {
    this.accessToken = accessToken;
    this.role = role;
    this.userId = userId;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AuthenticationResponse that = (AuthenticationResponse) o;
    return Objects.equals(accessToken, that.accessToken) && Objects.equals(role, that.role) && Objects.equals(userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessToken, role, userId);
  }
}
