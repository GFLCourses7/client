package com.geeksforless.client.controller.dto;

import com.geeksforless.client.model.Step;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Objects;

public class ScenarioDto {
    private Long scenarioId;
    @NotBlank(message = "Scenario name must be present.")
    private String name;
    private String site;
    private String result;
    private List<Step> steps;

    public ScenarioDto() {
    }

    public ScenarioDto(Long scenarioId,
                       String name,
                       String site,
                       String result,
                       List<Step> steps) {
        this.scenarioId = scenarioId;
        this.name = name;
        this.site = site;
        this.result = result;
        this.steps = steps;
    }

    public Long getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(Long scenarioId) {
        this.scenarioId = scenarioId;
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
        ScenarioDto that = (ScenarioDto) o;
        return Objects.equals(scenarioId, that.scenarioId) && Objects.equals(name, that.name) && Objects.equals(site, that.site) && Objects.equals(result, that.result) && Objects.equals(steps, that.steps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scenarioId, name, site, result, steps);
    }
}
