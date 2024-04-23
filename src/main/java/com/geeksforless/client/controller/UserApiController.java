package com.geeksforless.client.controller;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.projections.ScenarioInfo;
import com.geeksforless.client.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @PostMapping("/add-scenario")
    public ResponseEntity<?> addScenario(@Valid @RequestBody Scenario scenario) {
        if (scenario == null) {
            logger.warn("Scenario is null");
            return ResponseEntity.badRequest().build();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String userName = userDetails.getUsername();
            logger.info("Adding scenario for user: {}", userName);
            userService.addScenario(scenario, userName);
            return ResponseEntity.ok().build();
        } else {
            logger.error("User is not authenticated or UserDetails principal not found");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get-result")
    public ResponseEntity<List<ScenarioInfo>> getResult() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String userName = userDetails.getUsername();
            logger.info("Fetching results for user: {}", userName);
            List<ScenarioInfo> result = userService.getResult(userName);
            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                logger.warn("No results found for user: {}", userName);
                return ResponseEntity.notFound().build();
            }
        } else {
            logger.error("User is not authenticated or UserDetails principal not found");
            return ResponseEntity.badRequest().build();
        }
    }
}
