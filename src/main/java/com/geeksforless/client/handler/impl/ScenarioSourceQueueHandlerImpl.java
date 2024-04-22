package com.geeksforless.client.handler.impl;

import com.geeksforless.client.exception.ScenarioNotFoundException;
import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.mapper.ScenarioMapper;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.ScenarioDto;
import com.geeksforless.client.repository.ScenarioRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.geeksforless.client.model.projections.ScenarioInfo;
import com.geeksforless.client.model.Step;
import com.geeksforless.client.model.User;
import com.geeksforless.client.service.StepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ScenarioSourceQueueHandlerImpl implements ScenarioSourceQueueHandler {
    private static final Logger logger = LogManager.getLogger(ScenarioSourceQueueHandlerImpl.class);

    private final ScenarioRepository scenarioRepository;
    private final StepService stepService;
    private final ScenarioMapper scenarioMapper;
    private final LinkedBlockingQueue<Scenario> queue = new LinkedBlockingQueue<>();

    public ScenarioSourceQueueHandlerImpl(ScenarioRepository scenarioRepository,
                                          StepService stepService,
                                          ScenarioMapper scenarioMapper) {
        this.scenarioRepository = scenarioRepository;
        this.stepService = stepService;
        this.scenarioMapper = scenarioMapper;
    }

    @Override
    public void addScenario(Scenario scenario) {
        queue.add(scenario);
    }

    @Override
    public Optional<Scenario> takeScenario() {
        return Optional.ofNullable(queue.poll());
    }

    @Override
    @Transactional
    public ScenarioDto updateScenario(ScenarioDto scenarioDto) {
        Optional<Scenario> optionalScenario = scenarioRepository.findById(scenarioDto.getId());

        if (optionalScenario.isPresent()) {
            Scenario scenario = optionalScenario.get();
            scenario.setResult(scenarioDto.getResult());
            scenario.setDone(true);

            logger.info("Result of scenario " + scenario.getName() + " was saved");
            Scenario updated = scenarioRepository.save(scenario);
            return scenarioMapper.toDto(updated);
        }
        logger.error("Scenario with id " + scenarioDto.getId() + " not found.");
        throw new ScenarioNotFoundException("Scenario with id " + scenarioDto.getId() + " not found.");
    }

    public LinkedBlockingQueue<Scenario> getQueue() {
        return queue;
    }

    @Override
    @Transactional
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
