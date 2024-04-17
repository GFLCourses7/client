package com.geeksforless.client.service.scenario.impl;

import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.ScenarioDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ScenarioServiceImplTest {
    @Mock
    private ScenarioSourceQueueHandler scenarioSourceQueueHandler;

    @InjectMocks
    private ScenarioServiceImpl scenarioService;

    private ScenarioDto scenarioDto;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scenarioDto = new ScenarioDto();
    }

    @Test
    void updateScenario() {
        ScenarioDto updatedScenarioDto = new ScenarioDto();
        updatedScenarioDto.setId(1L);

        when(scenarioSourceQueueHandler.updateScenario(any(ScenarioDto.class))).thenReturn(updatedScenarioDto);
        scenarioService.setScenarioResult(updatedScenarioDto);

        verify(scenarioSourceQueueHandler, times(1)).updateScenario(any(ScenarioDto.class));
    }
}