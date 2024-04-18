package com.geeksforless.client.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    String userName;
    @Column(name = "password")
    String passWord;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Scenario> scenarios;

    public User() {
    }

    public User(Long id, String userName, String passWord, List<Scenario> scenarios) {
        this.id = id;
        this.userName = userName;
        this.passWord = passWord;
        this.scenarios = scenarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(userName, user.userName) && Objects.equals(passWord, user.passWord) && Objects.equals(scenarios, user.scenarios);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, passWord, scenarios);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", scenarios=" + scenarios +
                '}';
    }
}
