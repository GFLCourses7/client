package com.geeksforless.client.model.projections;

import com.geeksforless.client.model.Step;

/**
 * Projection for {@link Step}
 */
public interface StepInfo {
    Long getId();
    String getAction();

    String getValue();
}