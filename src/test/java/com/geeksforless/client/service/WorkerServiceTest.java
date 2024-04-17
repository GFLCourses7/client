package com.geeksforless.client.service;

import com.geeksforless.client.controller.dto.ScenarioDto;
import com.geeksforless.client.handler.ProxySourceQueueHandler;
import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.ProxyConfigHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WorkerServiceTest {

    @Mock
    private ScenarioSourceQueueHandler scenarioSourceQueueHandler;
    @Mock
    private ProxySourceQueueHandler proxySourceQueueHandler;
    @InjectMocks
    private WorkerService workerService;

    private ProxyConfigHolder proxyConfigHolder;
    private ScenarioDto scenarioDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        proxyConfigHolder = new ProxyConfigHolder();
        scenarioDto = new ScenarioDto();
    }

    @Test
    void getProxy() {
        when(proxySourceQueueHandler.getProxy()).thenReturn(proxyConfigHolder);

        ProxyConfigHolder actual = workerService.getProxy();

        assertNotNull(actual);
        assertEquals(proxyConfigHolder, actual);
    }

    @Test
    void getScenario() {
        when(scenarioSourceQueueHandler.takeScenarioFromQueue()).thenReturn(scenarioDto);

        ScenarioDto actual = workerService.getScenario();

        assertNotNull(actual);
        assertEquals(scenarioDto, actual);
    }

    @Test
    void updateScenario() {
        ScenarioDto updatedScenarioDto = new ScenarioDto();
        updatedScenarioDto.setId(1L);

        when(scenarioSourceQueueHandler.updateScenario(any(ScenarioDto.class))).thenReturn(updatedScenarioDto);
        workerService.updateScenario(updatedScenarioDto);

        verify(scenarioSourceQueueHandler, times(1)).updateScenario(any(ScenarioDto.class));
    }
}