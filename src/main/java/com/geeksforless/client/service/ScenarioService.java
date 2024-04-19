package com.geeksforless.client.service;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.ScenarioInfo;
import com.geeksforless.client.model.User;

import java.util.List;

public interface ScenarioService {
    Scenario saveScenario(Scenario scenario);

    List<ScenarioInfo> getScenarioInfoByUser(User user);
}
