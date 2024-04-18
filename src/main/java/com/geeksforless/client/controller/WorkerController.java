package com.geeksforless.client.controller;

import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.ScenarioDto;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
public class WorkerController {
    private static final Logger logger = LogManager.getLogger(WorkerController.class);
    private final ScenarioSourceQueueHandler queueHandler;

    public WorkerController(ScenarioSourceQueueHandler queueHandler) {
        this.queueHandler = queueHandler;
    }


    @PostMapping("/set-result")
    public ResponseEntity<?> setResult(@Valid @RequestBody ScenarioDto scenarioDto) {
        logger.info("Scenario " + scenarioDto.getName() + " going to updated");
        queueHandler.updateScenario(scenarioDto);
        return ResponseEntity.ok().build();
    }
}
