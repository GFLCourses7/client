package com.geeksforless.client.controller;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.ScenarioInfo;
import com.geeksforless.client.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserApiController {
    private static final Logger logger = LogManager.getLogger(UserApiController.class);

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/add-scenario")
    public ResponseEntity<?> addScenario(@Valid @RequestBody Scenario scenario) {
        if (scenario == null) {
            logger.warn("Scenario is null");
            return ResponseEntity.badRequest().build();
        }
        String userName = ""; //stub
        userService.addScenario(scenario, userName);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-result")
    public ResponseEntity<List<ScenarioInfo>> getScenario() {
        String userName = ""; //stub
        return new ResponseEntity<>(userService.getResult(userName), HttpStatus.OK);
    }

}
