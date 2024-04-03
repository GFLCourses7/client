package com.geeksforless.client.handler;

import com.geeksforless.client.model.Scenario;

import java.util.Optional;

public interface ScenarioSourceQueueHandler {

    void addScenario(Scenario scenario);

    Optional<Scenario> getScenario() throws InterruptedException;
}
