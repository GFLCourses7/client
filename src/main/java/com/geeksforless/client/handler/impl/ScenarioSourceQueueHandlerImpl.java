package com.geeksforless.client.handler.impl;

import com.geeksforless.client.controller.dto.ScenarioDto;
import com.geeksforless.client.exception.ScenarioNotFoundException;
import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.User;
import com.geeksforless.client.repository.ScenarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ScenarioSourceQueueHandlerImpl implements ScenarioSourceQueueHandler {

    private final LinkedBlockingQueue<ScenarioDto> queue = new LinkedBlockingQueue<>();
    private final ScenarioRepository scenarioRepository;


    public ScenarioSourceQueueHandlerImpl(ScenarioRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }


    @Override
    public void addScenario(Scenario scenario) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                scenario.setUser((User) principal);

                Scenario saved = scenarioRepository.save(scenario);
                if (!saved.isDone()) queue.add(saved.toDto());
            }
        }
    }

    @Override
    public ScenarioDto takeScenarioFromQueue() {
        return queue.poll();
    }

    @Override
    @Transactional
    public ScenarioDto updateScenario(ScenarioDto scenarioDto) {
        Scenario scenarioFromRepo = scenarioRepository.findById(scenarioDto.getId())
                .orElseThrow(() -> new ScenarioNotFoundException("Scenario with {} not found", scenarioDto.getId()));

        scenarioFromRepo.setDone(true);
        scenarioFromRepo.setResult(scenarioDto.getResult());

        Scenario updated = scenarioRepository.save(scenarioFromRepo);

        return updated.toDto();
    }

    public LinkedBlockingQueue<ScenarioDto> getQueue() {
        return queue;
    }
}
