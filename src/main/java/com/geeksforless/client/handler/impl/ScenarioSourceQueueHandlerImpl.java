package com.geeksforless.client.handler.impl;

import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.projections.ScenarioInfo;
import com.geeksforless.client.model.Step;
import com.geeksforless.client.model.User;
import com.geeksforless.client.repository.ScenarioRepository;
import com.geeksforless.client.service.StepService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ScenarioSourceQueueHandlerImpl implements ScenarioSourceQueueHandler {
    private static final Logger logger = LogManager.getLogger(ScenarioSourceQueueHandlerImpl.class);

    private final ScenarioRepository scenarioRepository;
    private final StepService stepService;
    private final LinkedBlockingQueue<Scenario> queue = new LinkedBlockingQueue<>();

    public ScenarioSourceQueueHandlerImpl(ScenarioRepository scenarioRepository, StepService stepService) {
        this.scenarioRepository = scenarioRepository;
        this.stepService = stepService;
    }

    @Override
    public void addScenario(Scenario scenario) {
        queue.add(scenario);
    }

    @Override
    public Optional<Scenario> takeScenario() {
        return Optional.ofNullable(queue.poll());
    }

    public LinkedBlockingQueue<Scenario> getQueue() {
        return queue;
    }

    @Override
    public Scenario saveScenario(Scenario scenario) {
        List<Step> stepList = scenario.getSteps();
        if (stepList != null && !stepList.isEmpty()) {
            for (Step step : stepList) {
                if (step != null) {
                    step = stepService.addStep(step);
                    step.setScenario(scenario);
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
