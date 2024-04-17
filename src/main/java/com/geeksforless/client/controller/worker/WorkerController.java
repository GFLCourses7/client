package com.geeksforless.client.controller.worker;

import com.geeksforless.client.controller.dto.ScenarioDto;
import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.service.WorkerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping("/get-proxy")
    public ResponseEntity<ProxyConfigHolder> getProxy() {
        return ResponseEntity.ok(workerService.getProxy());
    }

    @GetMapping("/get-scenario")
    public ResponseEntity<ScenarioDto> getScenario() {
        return ResponseEntity.ok(workerService.getScenario());
    }

    @PostMapping("/set-result")
    public ResponseEntity<Void> setResult(@Valid @RequestBody ScenarioDto scenario) {
        workerService.updateScenario(scenario);
        return ResponseEntity.ok().build();
    }
}
