package com.geeksforless.client.controller;

import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.Scenario;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/scenario")
public class ScenarioSourceController {
    private static final Logger logger = LogManager.getLogger(ScenarioSourceController.class);
    private final ScenarioSourceQueueHandler sourceQueueHandler;

    public ScenarioSourceController(ScenarioSourceQueueHandler sourceQueueHandler) {
        this.sourceQueueHandler = sourceQueueHandler;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<?> addScenario(@Valid @RequestBody Scenario scenario) {
//        System.out.println(request.isUserInRole("ROLE_USER"));
        if (scenario == null) {
            logger.warn("Scenario is null");
            return ResponseEntity.badRequest().build();
        }
        sourceQueueHandler.addScenario(scenario);
        logger.info("Scenario " + scenario.getName() + " added to queue");

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-scenario")
    private Optional<Scenario> getScenario() {
        try {
            return sourceQueueHandler.takeScenario();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
