package com.geeksforless.client.controller.worker;

import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.service.WorkerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping("/get-proxy")
    public ProxyConfigHolder getProxy() {
        return workerService.getProxy();
    }

    @GetMapping("/get-scenario")
    public Scenario getScenario() {
        return workerService.getScenario();
    }

    @PostMapping("/set-result")
    public void setResult(@Valid @RequestBody Scenario scenario) {
        workerService.updateScenario(scenario);
    }
}
