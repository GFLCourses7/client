package com.geeksforless.client.handler;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.User;
import com.geeksforless.client.model.dto.ScenarioDtoExternal;
import com.geeksforless.client.model.dto.ScenarioDtoInternal;
import com.geeksforless.client.model.projections.ScenarioInfo;

import java.util.List;
import java.util.Optional;

public interface ScenarioSourceQueueHandler {

    void addScenario(Scenario scenario);

    Optional<Scenario> takeScenario();
    List<Scenario> takeScenarios();

    ScenarioDtoExternal updateScenario(ScenarioDtoInternal scenarioInfo);

    Scenario saveScenario(Scenario scenario);

    List<Scenario> getScenarioByUser(User user);
}
