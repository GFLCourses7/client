package com.geeksforless.client.controller;

import com.geeksforless.client.model.ScenarioDto;
import com.geeksforless.client.service.scenario.impl.ScenarioServiceImpl;
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

class InternalApiControllerTest {

    @Mock
    private ScenarioServiceImpl scenarioService;

    @InjectMocks
    private  InternalApiController internalApiController;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSetResult_Success() {

        ScenarioDto scenarioDto = new ScenarioDto();
        scenarioDto.setName("Test Scenario");

        ResponseEntity<?> responseEntity = internalApiController.setResult(scenarioDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(scenarioService, times(1)).setScenarioResult(scenarioDto);
    }
}