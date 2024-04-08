package com.geeksforless.client.controller;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.Scenario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@ExtendWith(MockitoExtension.class)
public class ScenarioSourceControllerTest {
    private ScenarioSourceQueueHandler mockQueueHandler;
    private ScenarioSourceController controller;

    @BeforeEach
    void setUp() {
        mockQueueHandler = mock(ScenarioSourceQueueHandler.class);
        controller = new ScenarioSourceController(mockQueueHandler);
    }
    @Test
    void addScenario_ValidScenario_Success() {
        Scenario scenario = new Scenario();
        scenario.setName("Test Scenario");

        ResponseEntity<?> response = controller.addScenario(scenario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(mockQueueHandler, times(1)).addScenario(scenario);
    }

    @Test
    void addScenario_NullScenario_BadRequest() {
        ResponseEntity<?> response = controller.addScenario(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(mockQueueHandler);
    }
}
