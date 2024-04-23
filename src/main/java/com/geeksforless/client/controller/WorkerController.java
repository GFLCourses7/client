package com.geeksforless.client.controller;

import com.geeksforless.client.handler.ProxySourceQueueHandler;
import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.mapper.ScenarioMapper;
import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.projections.ScenarioInfo;
import com.geeksforless.client.repository.ScenarioRepository;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/internal")
public class WorkerController {
    private static final Logger logger = LogManager.getLogger(WorkerController.class);

    private final ScenarioSourceQueueHandler scenarioSourceQueueHandler;
    private final ProxySourceQueueHandler proxySourceQueueHandler;
    private final ScenarioMapper scenarioMapper;
    private final ScenarioRepository scenarioRepository;

    public WorkerController(ScenarioSourceQueueHandler scenarioSourceQueueHandler,
                            ProxySourceQueueHandler proxySourceQueueHandler,
                            ScenarioMapper scenarioMapper1, ScenarioRepository scenarioRepository) {
        this.scenarioSourceQueueHandler = scenarioSourceQueueHandler;
        this.proxySourceQueueHandler = proxySourceQueueHandler;
        this.scenarioMapper = scenarioMapper1;
        this.scenarioRepository = scenarioRepository;
    }

    @PostMapping("/set-result")
    public ResponseEntity<?> setResult(@Valid @RequestBody ScenarioInfo scenarioDto) {
        logger.info("Worker sending result");
        logger.info("Scenario {} going to updated", scenarioDto.getName());
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

//    @GetMapping("/get-scenario")
//    public ResponseEntity<ScenarioDto> getScenario() {
//        logger.info("client requesting scenarios");
//        return ResponseEntity.ok(scenarioSourceQueueHandler.takeScenario()
//                .map(scenarioMapper::toDto)
//                .orElse(new ScenarioDto())
//        );
//    }

    @GetMapping("/get-scenarios")
    public ResponseEntity<List<ScenarioInfo>> getScenarios() {
        logger.info("Client requesting scenarios");

        List<Scenario> scenarios = scenarioSourceQueueHandler.takeScenarios();

        List<ScenarioInfo> list = scenarios.stream()
                .map(scenario -> scenarioRepository.findScenarioInfoById(scenario.getId()))
                .toList();

        return ResponseEntity.ok(list);
    }
}
