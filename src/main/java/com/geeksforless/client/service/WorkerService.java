package com.geeksforless.client.service;

import com.geeksforless.client.controller.dto.ScenarioDto;
import com.geeksforless.client.handler.ProxySourceQueueHandler;
import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.ProxyConfigHolder;
import org.springframework.stereotype.Service;

@Service
public class WorkerService {
    private final ScenarioSourceQueueHandler scenarioSourceQueueHandler;
    private final ProxySourceQueueHandler proxySourceQueueHandler;

    public WorkerService(ScenarioSourceQueueHandler scenarioSourceQueueHandler,
                         ProxySourceQueueHandler proxySourceQueueHandler) {
        this.scenarioSourceQueueHandler = scenarioSourceQueueHandler;
        this.proxySourceQueueHandler = proxySourceQueueHandler;
    }

    public ProxyConfigHolder getProxy() {
        return proxySourceQueueHandler.getProxy();
    }

    public ScenarioDto getScenario() {
        return scenarioSourceQueueHandler.takeScenarioFromQueue();
    }

    public void updateScenario(ScenarioDto scenarioDto) {
        scenarioSourceQueueHandler.updateScenario(scenarioDto);
    }
}
