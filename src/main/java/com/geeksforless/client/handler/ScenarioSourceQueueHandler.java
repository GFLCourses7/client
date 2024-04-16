package com.geeksforless.client.handler;

import com.geeksforless.client.model.Scenario;

public interface ScenarioSourceQueueHandler {

    void addScenario(Scenario scenario);

    Scenario takeScenario();

    Scenario updateScenario(Scenario scenario);
}
