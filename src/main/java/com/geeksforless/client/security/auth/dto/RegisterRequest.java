package com.geeksforless.client.security.auth.dto;


import java.util.Objects;

public class RegisterRequest {
  private String first_name;
  private String last_name;
  private String login;
  private String password;

  public RegisterRequest() {
  }

  public RegisterRequest(String first_name, String last_name, String login, String password) {
    this.first_name = first_name;
    this.last_name = last_name;
    this.login = login;
    this.password = password;
  }

  public String getFirst_name() {
    return first_name;
  }

  public void setFirst_name(String first_name) {
    this.first_name = first_name;
  }

  public String getLast_name() {
    return last_name;
  }

  public void setLast_name(String last_name) {
    this.last_name = last_name;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RegisterRequest that = (RegisterRequest) o;
    return Objects.equals(first_name, that.first_name) && Objects.equals(last_name, that.last_name) && Objects.equals(login, that.login) && Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first_name, last_name, login, password);
  }
}
