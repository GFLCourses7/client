package com.geeksforless.client.handler.impl;

import com.geeksforless.client.exception.ScenarioNotFoundException;
import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.mapper.ScenarioMapper;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.Step;
import com.geeksforless.client.model.User;
import com.geeksforless.client.model.dto.ScenarioDtoExternal;
import com.geeksforless.client.model.dto.ScenarioDtoInternal;import com.geeksforless.client.repository.ScenarioRepository;
import com.geeksforless.client.service.StepService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
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
    private Integer batchSize;

    public ScenarioSourceQueueHandlerImpl(ScenarioRepository scenarioRepository,
                                          StepService stepService,
                                          ScenarioMapper scenarioMapper,
                                          @Value("${scenario.queue.batch.size}") Integer batchSize) {
        this.scenarioRepository = scenarioRepository;
        this.stepService = stepService;
        this.scenarioMapper = scenarioMapper;
        this.batchSize = batchSize;
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
    public synchronized List<Scenario> takeScenarios() {
        logger.trace("Scenarios in queue: {}", queue.size());
        List<Scenario> scenarios = new LinkedList<>();
        if (queue.size() <= batchSize) {
            while (!queue.isEmpty() && queue.size() < batchSize) {
                Scenario poll = queue.poll();
                if (poll != null) {
                    scenarios.add(poll);
                }
            }
        } else {
            while (batchSize > 0 && !queue.isEmpty()) {
                Scenario poll = queue.poll();
                if (poll != null) {
                    scenarios.add(poll);
                }
                batchSize--;
            }
        }
        logger.trace("Sending {} scenarios. Scenarios in queue left: {}", scenarios.size(), queue.size());
        return scenarios;
    }

    @Override
    @Transactional
    public ScenarioDtoExternal updateScenario(ScenarioDtoInternal scenarioDtoInternal) {
        Optional<Scenario> optionalScenario = scenarioRepository.findById(scenarioDtoInternal.getId());

        if (optionalScenario.isPresent()) {
            Scenario scenario = optionalScenario.get();
            scenario.setResult(scenarioDtoInternal.getResult());
            scenario.setDone(true);

            logger.info("Result of scenario {} was saved", scenario.getName());
            Scenario updated = scenarioRepository.save(scenario);
            return scenarioMapper.toDtoExternal(updated);
        }
        logger.error("Scenario with id {} not found.", scenarioDtoInternal.getId());
        throw new ScenarioNotFoundException("Scenario with id " + scenarioDtoInternal.getId() + " not found.");
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
    public List<Scenario> getScenariosByUser(User user) {
        return scenarioRepository.findByUser(user);
    }
}
