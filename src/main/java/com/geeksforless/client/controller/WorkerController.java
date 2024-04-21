package com.geeksforless.client.controller;

import com.geeksforless.client.handler.ProxySourceQueueHandler;
import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.mapper.ScenarioMapper;
import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.ScenarioDto;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/internal")
public class WorkerController {
    private static final Logger logger = LogManager.getLogger(WorkerController.class);

    private final ScenarioSourceQueueHandler scenarioSourceQueueHandler;
    private final ProxySourceQueueHandler proxySourceQueueHandler;
    private final ScenarioMapper scenarioMapper;

    public WorkerController(ScenarioSourceQueueHandler scenarioSourceQueueHandler,
                            ProxySourceQueueHandler proxySourceQueueHandler,
                            ScenarioMapper scenarioMapper1) {
        this.scenarioSourceQueueHandler = scenarioSourceQueueHandler;
        this.proxySourceQueueHandler = proxySourceQueueHandler;
        this.scenarioMapper = scenarioMapper1;
    }

    @PostMapping("/set-result")
    public ResponseEntity<?> setResult(@Valid @RequestBody ScenarioDto scenarioDto) {
        logger.info("client sending result");
        logger.info("Scenario " + scenarioDto.getName() + " going to updated");
        scenarioSourceQueueHandler.updateScenario(scenarioDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-proxy")
    public ResponseEntity<ProxyConfigHolder> getProxy() {
        logger.info("client requesting proxy");
        return ResponseEntity.ok(Optional.ofNullable(proxySourceQueueHandler.getProxy())
                .orElse(new ProxyConfigHolder())
        );
    }

    @GetMapping("/get-scenario")
    public ResponseEntity<ScenarioDto> getScenario() {
        logger.info("client requesting scenarios");
        return ResponseEntity.ok(scenarioSourceQueueHandler.takeScenario()
                .map(scenarioMapper::toDto)
                .orElse(new ScenarioDto())
        );
    }
}
