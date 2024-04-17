package com.geeksforless.client.service.scenario.impl;

import com.geeksforless.client.controller.InternalApiController;
import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.ScenarioDto;
import com.geeksforless.client.service.scenario.ScenarioService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScenarioServiceImpl implements ScenarioService {
    private static final Logger logger = LogManager.getLogger(InternalApiController.class);
    private final ScenarioSourceQueueHandler sourceQueueHandler;

    public ScenarioServiceImpl(ScenarioSourceQueueHandler sourceQueueHandler) {
        this.sourceQueueHandler = sourceQueueHandler;
    }

    @Override
    @Transactional
    public void setScenarioResult(ScenarioDto scenarioDto) {
        logger.info("Scenario " + scenarioDto.getName() + " updated");
        sourceQueueHandler.updateScenario(scenarioDto);
    }
}
