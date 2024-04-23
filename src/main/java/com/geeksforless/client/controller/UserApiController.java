package com.geeksforless.client.controller;

import com.geeksforless.client.mapper.ScenarioMapper;
import com.geeksforless.client.model.dto.ScenarioDtoExternal;
import com.geeksforless.client.service.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserApiController {
    private static final Logger logger = LogManager.getLogger(UserApiController.class);

    private final UserService userService;
    private final ScenarioMapper scenarioMapper;

    public UserApiController(UserService userService, ScenarioMapper scenarioMapper) {
        this.userService = userService;
        this.scenarioMapper = scenarioMapper;
    }

    @PostMapping("/add-scenario")
    public ResponseEntity<?> addScenario(@Valid @RequestBody ScenarioDtoExternal scenarioDto) {
        if (scenarioDto == null) {
            logger.warn("Scenario is null");
            return ResponseEntity.badRequest().build();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String userName = userDetails.getUsername();
            logger.info("Adding scenarioDto for user: {}", userName);
            userService.addScenario(scenarioMapper.toScenario(scenarioDto), userName);
            return ResponseEntity.ok().build();
        } else {
            logger.error("User is not authenticated or UserDetails principal not found");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get-result")
    public ResponseEntity<List<ScenarioDtoExternal>> getResult() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String userName = userDetails.getUsername();
            logger.info("Fetching results for user: {}", userName);


            List<ScenarioDtoExternal> result = userService.getResult(userName)
                    .stream()
                    .map(scenarioMapper::toDtoExternal)
                    .toList();

            if (!result.isEmpty()) {
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
