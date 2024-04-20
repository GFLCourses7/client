package com.geeksforless.client.handler;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.ScenarioDto;

import java.util.Optional;

public interface ScenarioSourceQueueHandler {

    void addScenario(Scenario scenario);

    Optional<Scenario> takeScenario() throws InterruptedException;

    ScenarioDto updateScenario(ScenarioDto scenarioDto);
}
