package com.geeksforless.client.model.dto;

import java.util.List;
import java.util.Objects;

public class ScenarioDtoInternal {

    private Long id;

    private String name;

    private String site;

    private String result;

    private List<StepDtoInternal> steps;

    public ScenarioDtoInternal() {
    }

    public ScenarioDtoInternal(Long id, String name, String site, String result, List<StepDtoInternal> steps) {
        this.id = id;
        this.name = name;
        this.site = site;
        this.result = result;
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

    public List<StepDtoInternal> getSteps() {
        return steps;
    }

    public void setSteps(List<StepDtoInternal> steps) {
        this.steps = steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScenarioDtoInternal that = (ScenarioDtoInternal) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(site, that.site) && Objects.equals(result, that.result) && Objects.equals(steps, that.steps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, site, result, steps);
    }

    @Override
    public String toString() {
        return "ScenarioDtoInternal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", result='" + result + '\'' +
                ", steps=" + steps +
                '}';
    }
}
