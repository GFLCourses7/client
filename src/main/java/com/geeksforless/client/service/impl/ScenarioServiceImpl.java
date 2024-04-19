package com.geeksforless.client.service.impl;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.ScenarioInfo;
import com.geeksforless.client.model.Step;
import com.geeksforless.client.model.User;
import com.geeksforless.client.repository.ScenarioRepository;
import com.geeksforless.client.service.ScenarioService;
import com.geeksforless.client.service.StepService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScenarioServiceImpl implements ScenarioService {

    private static final Logger logger = LogManager.getLogger(ScenarioService.class);

    private final ScenarioRepository scenarioRepository;
    private final StepService stepService;

    public ScenarioServiceImpl(ScenarioRepository scenarioRepository, StepService stepService) {
        this.scenarioRepository = scenarioRepository;
        this.stepService = stepService;
    }

    @Override
    public Scenario saveScenario(Scenario scenario) {
        List<Step> stepList = scenario.getSteps();
        if (stepList != null && !stepList.isEmpty()) {
            for (Step step : stepList) {
                if (step != null) {
                    stepService.addStep(step);
                } else {
                    logger.warn("Null step found while saving scenario: {}", scenario.getName());
                }
            }
        }
        return scenarioRepository.save(scenario);
    }

    @Override
    public List<ScenarioInfo> getScenarioInfoByUser(User user) {
        return scenarioRepository.findByUser(user);
    }
}
