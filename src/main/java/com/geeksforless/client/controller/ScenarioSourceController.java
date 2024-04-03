package com.geeksforless.client.controller;

import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.Scenario;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/scenario")
public class ScenarioSourceController {

   private final ScenarioSourceQueueHandler sourceQueueHandler;

   public ScenarioSourceController (ScenarioSourceQueueHandler sourceQueueHandler){
       this.sourceQueueHandler=sourceQueueHandler;
   }

    @PostMapping(value = "/add", consumes = "application/json")
    public ResponseEntity<String> addScenario(@RequestBody Scenario scenario) {
        try {
            if (scenario == null) {
                return ResponseEntity.badRequest().body("Request body cannot be null");
            }
            if (scenario.getName() == null || scenario.getName().isEmpty()) {
                return ResponseEntity.badRequest().body("Scenario name is required");
            }
            sourceQueueHandler.addScenario(scenario);
            return ResponseEntity.ok("Scenario " + scenario.getName() + " added to queue");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }
}
