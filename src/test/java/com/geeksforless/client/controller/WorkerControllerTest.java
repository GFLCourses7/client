package com.geeksforless.client.controller;

import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.ScenarioDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class WorkerControllerTest {

    @Mock
    private ScenarioSourceQueueHandler queueHandler;

    @InjectMocks
    private  WorkerController workerController;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSetResult_Success() {

        ScenarioDto scenarioDto = new ScenarioDto();
        scenarioDto.setName("Test Scenario");

        ResponseEntity<?> responseEntity = workerController.setResult(scenarioDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(queueHandler, times(1)).updateScenario(scenarioDto);
    }
}