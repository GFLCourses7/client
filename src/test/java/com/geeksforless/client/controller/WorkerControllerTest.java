package com.geeksforless.client.controller;

import com.geeksforless.client.handler.ProxySourceQueueHandler;
import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.mapper.ScenarioMapper;
import com.geeksforless.client.model.*;
import com.geeksforless.client.model.dto.ScenarioDtoExternal;
import com.geeksforless.client.model.dto.ScenarioDtoInternal;
import com.geeksforless.client.model.dto.StepDtoExternal;
import com.geeksforless.client.model.dto.StepDtoInternal;
import com.geeksforless.client.model.projections.ScenarioInfo;
import com.geeksforless.client.model.projections.StepInfo;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkerControllerTest {

    @Mock
    private ScenarioSourceQueueHandler scenarioSourceQueueHandler;

    @Mock
    private ProxySourceQueueHandler proxySourceQueueHandler;

    @Mock
    private ScenarioMapper scenarioMapper;

    @InjectMocks
    private WorkerController workerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static ScenarioInfo getScenarioInfo() {
        return new ScenarioInfo() {
            @Override
            public Long getId() {
                return 0L;
            }

            @Override
            public String getName() {
                return "test scenario";
            }

            @Override
            public String getSite() {
                return "";
            }

            @Override
            public String getResult() {
                return "Success";
            }

            @Override
            public List<StepInfo> getSteps() {
                return List.of();
            }
        };
    }

    @Test
    public void testSetResult_Success() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(scenarioSourceQueueHandler.updateScenario(any(ScenarioDtoInternal.class))).thenReturn(new ScenarioDtoExternal());

        ScenarioDtoExternal fakeScenario = new ScenarioDtoExternal();
        fakeScenario.setName("test scenario");
        fakeScenario.setResult("Success");

        ResponseEntity<?> responseEntity = workerController.setResult(new ScenarioDtoInternal(
                0L,
                "test scenario",
                "site",
                "success",
                List.of()
        ));
        int responseCode = responseEntity.getStatusCode().value();

        assertEquals(HttpStatus.OK.value(), responseCode);
    }

    @Test
    public void testGetProxy() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ProxyConfigHolder expected = new ProxyConfigHolder();

        expected.setProxyNetworkConfig(
                new ProxyNetworkConfig(
                        "hostname",
                        8080
                )
        );
        expected.setProxyCredentials(
                new ProxyCredentials(
                        "username",
                        "password"
                )
        );

        when(proxySourceQueueHandler.getProxy()).thenReturn(expected);

        ResponseEntity<ProxyConfigHolder> responseEntity = workerController.getProxy();
        ProxyConfigHolder actual = responseEntity.getBody();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetProxyEmpty() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ProxyConfigHolder expected = new ProxyConfigHolder();

        when(proxySourceQueueHandler.getProxy()).thenReturn(null);

        ResponseEntity<ProxyConfigHolder> responseEntity = workerController.getProxy();
        ProxyConfigHolder actual = responseEntity.getBody();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetScenario() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String name = "name";
        String site = "site";

        Scenario scenario = new Scenario();
        scenario.setName(name);
        scenario.setSite(site);
        scenario.setSteps(List.of(new Step("action", "value")));

        ScenarioDtoExternal expected = new ScenarioDtoExternal();
        expected.setName(name);
        expected.setSite(site);
        expected.setSteps(List.of(new StepDtoExternal("action", "value")));

        when(scenarioMapper.toDtoExternal(any())).thenReturn(expected);

        when(scenarioSourceQueueHandler.takeScenario()).thenReturn(Optional.of(scenario));

        ResponseEntity<ScenarioDtoExternal> responseEntity = workerController.getScenario();
        ScenarioDtoExternal actual = responseEntity.getBody();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetScenarioEmpty() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Scenario scenario = new Scenario();

        ScenarioDtoExternal expected = new ScenarioDtoExternal();

        when(scenarioMapper.toDtoExternal(any())).thenReturn(expected);

        when(scenarioSourceQueueHandler.takeScenario()).thenReturn(Optional.of(scenario));

        ResponseEntity<ScenarioDtoExternal> responseEntity = workerController.getScenario();
        ScenarioDtoExternal actual = responseEntity.getBody();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetScenarios() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String name = "name";
        String site = "site";

        Scenario scenario = new Scenario();
        scenario.setName(name);
        scenario.setSite(site);
        scenario.setSteps(List.of(new Step("action", "value")));

        ScenarioDtoInternal scenarioDtoInternal = new ScenarioDtoInternal();
        scenarioDtoInternal.setName(name);
        scenarioDtoInternal.setSite(site);
        scenarioDtoInternal.setSteps(List.of(new StepDtoInternal(null, "action", "value")));

        List<ScenarioDtoInternal> expected = List.of(scenarioDtoInternal);

        when(scenarioMapper.toDtoInternal(any())).thenReturn(scenarioDtoInternal);

        when(scenarioSourceQueueHandler.takeScenarios()).thenReturn(List.of(scenario));

        ResponseEntity<List<ScenarioDtoInternal>> responseEntity = workerController.getScenarios();
        List<ScenarioDtoInternal> actual = responseEntity.getBody();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetScenariosEmpty() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ScenarioDtoInternal scenarioDtoInternal = new ScenarioDtoInternal();

        List<ScenarioDtoInternal> expected = List.of(scenarioDtoInternal);

        when(scenarioMapper.toDtoInternal(any())).thenReturn(scenarioDtoInternal);

        when(scenarioSourceQueueHandler.takeScenarios()).thenReturn(new ArrayList<>());

        ResponseEntity<List<ScenarioDtoInternal>> responseEntity = workerController.getScenarios();
        List<ScenarioDtoInternal> actual = responseEntity.getBody();

        assertEquals(expected, actual);
    }

}
