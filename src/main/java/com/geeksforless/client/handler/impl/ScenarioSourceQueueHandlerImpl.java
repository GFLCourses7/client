package com.geeksforless.client.handler.impl;

import com.geeksforless.client.exception.ScenarioNotFoundException;
import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.repository.ScenarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ScenarioSourceQueueHandlerImpl implements ScenarioSourceQueueHandler {

    private final ScenarioRepository scenarioRepository;

    private final LinkedBlockingQueue<Scenario> queue = new LinkedBlockingQueue<>();

    public ScenarioSourceQueueHandlerImpl(ScenarioRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @Override
    public void addScenario(Scenario scenario) {
        Scenario saved = scenarioRepository.save(scenario);
        queue.add(saved);
    }

    @Override
    public Scenario takeScenario() {
        return queue.poll();
    }

    @Override
    @Transactional
    public Scenario updateScenario(Scenario scenario) {
        Scenario scenarioFromRepo = scenarioRepository.findById(scenario.getId())
                .orElseThrow(() -> new ScenarioNotFoundException("Scenario with {} not found", scenario.getId()));

        scenarioFromRepo.setDone(true);
        scenarioFromRepo.setResult(scenario.getResult());

        Scenario updated = scenarioRepository.save(scenarioFromRepo);
        queue.add(updated);

        return updated;
    }

    public LinkedBlockingQueue<Scenario> getQueue() {
        return queue;
    }
}
