package com.geeksforless.client.handler;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.User;
import com.geeksforless.client.model.dto.ScenarioDto;
import com.geeksforless.client.model.projections.ScenarioInfo;

import java.util.List;
import java.util.Optional;

public interface ScenarioSourceQueueHandler {

    void addScenario(Scenario scenario);

    Optional<Scenario> takeScenario();
    List<Scenario> takeScenarios();

    ScenarioDto updateScenario(ScenarioInfo scenarioInfo);

    Scenario saveScenario(Scenario scenario);

    List<Scenario> getScenarioByUser(User user);
}
