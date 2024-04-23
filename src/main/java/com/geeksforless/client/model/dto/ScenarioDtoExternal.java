package com.geeksforless.client.model.dto;

import java.util.List;
import java.util.Objects;

public class ScenarioDtoExternal {


    private String name;

    private String site;

    private String result;

    private List<StepDtoExternal> steps;

    public ScenarioDtoExternal() {
    }

    public ScenarioDtoExternal(String name, String site, String result, List<StepDtoExternal> steps) {
        this.name = name;
        this.site = site;
        this.result = result;
        this.steps = steps;
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

    public List<StepDtoExternal> getSteps() {
        return steps;
    }

    public void setSteps(List<StepDtoExternal> steps) {
        this.steps = steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScenarioDtoExternal that = (ScenarioDtoExternal) o;
        return Objects.equals(name, that.name) && Objects.equals(site, that.site) && Objects.equals(result, that.result) && Objects.equals(steps, that.steps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, site, result, steps);
    }

    @Override
    public String toString() {
        return "ScenarioDto{" +
                "name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", result='" + result + '\'' +
                ", steps=" + steps +
                '}';
    }
}
