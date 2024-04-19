package com.geeksforless.client.model;

import java.util.List;

/**
 * Projection for {@link Scenario}
 */
public interface ScenarioInfo {
    String getName();

    String getSite();

    String getResult();

    List<StepInfo> getSteps();
}