package com.geeksforless.client.handler;

import com.geeksforless.client.controller.dto.ScenarioDto;
import com.geeksforless.client.model.Scenario;

public interface ScenarioSourceQueueHandler {

    void addScenario(Scenario scenario);

    ScenarioDto takeScenarioFromQueue();

    ScenarioDto updateScenario(ScenarioDto scenario);
}
