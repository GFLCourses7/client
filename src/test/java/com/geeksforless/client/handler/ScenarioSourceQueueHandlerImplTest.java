package com.geeksforless.client.handler;

import com.geeksforless.client.handler.impl.ScenarioSourceQueueHandlerImpl;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.projections.ScenarioInfo;
import com.geeksforless.client.model.Step;
import com.geeksforless.client.model.User;
import com.geeksforless.client.repository.ScenarioRepository;
import com.geeksforless.client.service.StepService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScenarioSourceQueueHandlerImplTest {

    @InjectMocks
    private ScenarioSourceQueueHandlerImpl queueHandler;

    @Mock
    ScenarioRepository scenarioRepository;

    @Mock
    StepService stepService;

    @Test
    void addScenario_SuccessfullyAdded() {
        // Arrange
        Scenario scenario = new Scenario();

        // Act
        queueHandler.addScenario(scenario);

        // Assert
        assertEquals(1, queueHandler.getQueue().size());
        assertTrue(queueHandler.getQueue().contains(scenario));
    }

    @Test
    void takeScenarioFromQueueReturnsScenario() {
        // Arrange
        Scenario scenario = new Scenario();
        queueHandler.addScenario(scenario);

        // Act
        Optional<Scenario> scenarioOptional = queueHandler.takeScenario();

        // Assert
        assertTrue(scenarioOptional.isPresent());
        assertEquals(scenario, scenarioOptional.get());
        assertEquals(0, queueHandler.getQueue().size());
    }

    @Test
    void saveScenario_ValidScenarioWithSteps_SavesScenarioAndSteps() {
        Scenario scenario = new Scenario();
        List<Step> steps = new ArrayList<>();
        steps.add(new Step("action1", "value1"));
        steps.add(new Step("action2", "value2"));
        scenario.setSteps(steps);
        when(scenarioRepository.save(scenario)).thenReturn(scenario);

        Scenario result = queueHandler.saveScenario(scenario);

        assertEquals(scenario, result);
        verify(stepService, times(2)).addStep(any(Step.class));
    }

    @Test
    void saveScenario_ValidScenarioWithNullSteps_SavesScenarioButNotSteps() {
        Scenario scenario = new Scenario();
        scenario.setSteps(null);
        when(scenarioRepository.save(scenario)).thenReturn(scenario);

        Scenario result = queueHandler.saveScenario(scenario);

        assertEquals(scenario, result);
        verify(stepService, never()).addStep(any(Step.class));
    }

    @Test
    void getScenarioInfoByUser_UserExists_ReturnsListOfScenarioInfo() {
        User user = new User();
        List<ScenarioInfo> scenarioInfos = new ArrayList<>();
        when(scenarioRepository.findByUser(user)).thenReturn(scenarioInfos);

        List<ScenarioInfo> result = queueHandler.getScenarioInfoByUser(user);

        assertEquals(scenarioInfos, result);
    }
}