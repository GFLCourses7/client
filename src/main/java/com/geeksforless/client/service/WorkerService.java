package com.geeksforless.client.service;

import com.geeksforless.client.controller.dto.ScenarioDto;
import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.ProxyConfigHolder;
import org.springframework.stereotype.Service;

@Service
public class WorkerService {

    private final ScenarioSourceQueueHandler scenarioSourceQueueHandler;
    private final ProxySourceService proxySourceService;

    public WorkerService(ScenarioSourceQueueHandler scenarioSourceQueueHandler, ProxySourceService proxySourceService) {
        this.scenarioSourceQueueHandler = scenarioSourceQueueHandler;
        this.proxySourceService = proxySourceService;
    }

    public ProxyConfigHolder getProxy() {
        return proxySourceService.getProxies().get(0); //todo rewrite to get next proxy
    }

    public ScenarioDto getScenario() {
        return scenarioSourceQueueHandler.takeScenarioFromQueue();
    }

    public void updateScenario(ScenarioDto scenarioDto) {
        scenarioSourceQueueHandler.updateScenario(scenarioDto);
    }
}
