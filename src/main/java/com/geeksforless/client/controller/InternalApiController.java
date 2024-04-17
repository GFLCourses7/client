package com.geeksforless.client.controller;

import com.geeksforless.client.model.ScenarioDto;
import com.geeksforless.client.service.scenario.ScenarioService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
public class InternalApiController {
    private static final Logger logger = LogManager.getLogger(InternalApiController.class);
    private final ScenarioService scenarioService;

    public InternalApiController(ScenarioService scenarioService){
        this.scenarioService = scenarioService;
    }
    @PostMapping("/set-result")
    public ResponseEntity<?> setResult(@RequestBody ScenarioDto scenarioDto){
        logger.info("Scenario " + scenarioDto.getName() + " going to updated");
        scenarioService.setScenarioResult(scenarioDto);
        return ResponseEntity.ok().build();
    }
}
