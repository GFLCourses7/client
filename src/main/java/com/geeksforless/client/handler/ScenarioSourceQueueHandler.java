package com.geeksforless.client.handler;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.ScenarioDto;
import com.geeksforless.client.model.projections.ScenarioInfo;
import com.geeksforless.client.model.User;

import java.util.List;
import java.util.Optional;

public interface ScenarioSourceQueueHandler {

    void addScenario(Scenario scenario);

    Optional<Scenario> takeScenario();

    ScenarioDto updateScenario(ScenarioDto scenarioDto);

    Scenario saveScenario(Scenario scenario);

    List<ScenarioInfo> getScenarioInfoByUser(User user);
}
