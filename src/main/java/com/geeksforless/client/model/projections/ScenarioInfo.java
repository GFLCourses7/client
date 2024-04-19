package com.geeksforless.client.model.projections;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.projections.StepInfo;

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