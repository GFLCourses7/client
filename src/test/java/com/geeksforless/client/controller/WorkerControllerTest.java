package com.geeksforless.client.controller;

import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.ScenarioDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkerControllerTest {

    @Mock
    private ScenarioSourceQueueHandler queueHandler;

    @InjectMocks
    private WorkerController workerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSetResult_Success() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(queueHandler.updateScenario(any(ScenarioDto.class))).thenReturn(new ScenarioDto());

        ScenarioDto fakeScenario = new ScenarioDto();
        fakeScenario.setId(1L);
        fakeScenario.setName("test scenario");
        fakeScenario.setResult("Success");

        ResponseEntity<?> responseEntity = workerController.setResult(fakeScenario);
        int responseCode = responseEntity.getStatusCode().value();

        assertEquals(HttpStatus.OK.value(), responseCode);
    }
}
