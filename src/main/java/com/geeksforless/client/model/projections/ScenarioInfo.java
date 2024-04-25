package com.geeksforless.client.model.projections;

import com.geeksforless.client.model.Scenario;

import java.util.List;

/**
 * Projection for {@link Scenario}
 */
public interface ScenarioInfo {
    Long getId();

    String getName();

    String getSite();

    String getResult();

    List<StepInfo> getSteps();
}