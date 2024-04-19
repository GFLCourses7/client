package com.geeksforless.client.handler.impl;

import com.geeksforless.client.exception.ScenarioNotFoundException;
import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.ScenarioDto;
import com.geeksforless.client.repository.ScenarioRepository;
import com.geeksforless.client.service.Publisher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ScenarioSourceQueueHandlerImpl implements ScenarioSourceQueueHandler {
    private static final Logger logger = LogManager.getLogger(ScenarioSourceQueueHandlerImpl.class);
    private final LinkedBlockingQueue<Scenario> queue = new LinkedBlockingQueue<>();
    private final Publisher publisher;

    private final ScenarioRepository scenarioRepository;


    public ScenarioSourceQueueHandlerImpl(Publisher publisher, ScenarioRepository scenarioRepository) {
        this.publisher = publisher;
        this.scenarioRepository = scenarioRepository;
    }

    @Override
    public void addScenario(Scenario scenario) {
        queue.add(scenario);

        publisher.sendMessage();
    }

    @Override
    public Optional<Scenario> takeScenario() throws InterruptedException {
        return Optional.of(queue.take());
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
            return updated.toDto();
        }
        logger.error("Scenario with id " + scenarioDto.getId() + " not found.");
        throw new ScenarioNotFoundException("Scenario with id " + scenarioDto.getId() + " not found.");

    }

    public LinkedBlockingQueue<Scenario> getQueue() {
        return queue;
    }
}