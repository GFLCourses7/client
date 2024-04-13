package com.geeksforless.client.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Scenarios")
public class Scenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "name")
    private String name;
    @Column(name = "site")
    private String site;
    @Column(name = "result")
    private String result;
    @Column(name = "is_done")
    private boolean isDone;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "scenario", cascade = CascadeType.ALL)
    private List<Step> steps;

    public Scenario() {
    }

    public Scenario(Long id, String name, String site, String result, boolean isDone, User user, List<Step> steps) {
        this.id = id;
        this.name = name;
        this.site = site;
        this.result = result;
        this.isDone = isDone;
        this.user = user;
        this.steps = steps;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scenario scenario = (Scenario) o;
        return isDone == scenario.isDone && Objects.equals(id, scenario.id) && Objects.equals(name, scenario.name) && Objects.equals(site, scenario.site) && Objects.equals(result, scenario.result) && Objects.equals(user, scenario.user) && Objects.equals(steps, scenario.steps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, site, result, isDone, user, steps);
    }
}