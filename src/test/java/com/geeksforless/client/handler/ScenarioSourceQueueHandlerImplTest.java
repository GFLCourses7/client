package com.geeksforless.client.handler;

import com.geeksforless.client.exception.ScenarioNotFoundException;
import com.geeksforless.client.handler.impl.ScenarioSourceQueueHandlerImpl;
import com.geeksforless.client.mapper.ScenarioMapper;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.Step;
import com.geeksforless.client.model.User;
import com.geeksforless.client.model.dto.ScenarioDtoExternal;
import com.geeksforless.client.model.dto.ScenarioDtoInternal;
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

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScenarioSourceQueueHandlerImplTest {

    @InjectMocks
    private ScenarioSourceQueueHandlerImpl queueHandler;

    @Mock
    private StepService stepService;

    @Mock
    private ScenarioRepository scenarioRepository;

    @Mock
    private ScenarioMapper scenarioMapper;

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
    public void testUpdateScenario_Success() {
        ScenarioDtoExternal scenarioDto = new ScenarioDtoExternal();
        scenarioDto.setName("Test");
        scenarioDto.setResult("Success");

        Scenario scenario = new Scenario();
        scenario.setId(0L);
        scenario.setName("Test Scenario");
        scenario.setResult("Initial");
        scenario.setDone(false);


        when(scenarioRepository.findById(0L)).thenReturn(of(scenario));
        when(scenarioRepository.save(any(Scenario.class))).thenReturn(scenario);

        when(scenarioMapper.toDtoExternal(any())).thenReturn(scenarioDto);

        ScenarioDtoExternal updatedScenarioDto = queueHandler.updateScenario(new ScenarioDtoInternal(
                0L,
                "Test",
                "site",
                "Success",
                null
        ));

        assertEquals(scenarioDto.getResult(), updatedScenarioDto.getResult());
        assertTrue(scenario.isDone());
    }

    @Test
    public void testUpdateScenario_ScenarioNotFound() {

        when(scenarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ScenarioNotFoundException.class, () -> {
            queueHandler.updateScenario(new ScenarioDtoInternal(1L, null, null, null, null));
        });
    }

    @Test
    void saveScenario_ValidScenarioWithSteps_SavesScenarioAndSteps() {
        Scenario scenario = new Scenario();
        List<Step> steps = new ArrayList<>();
        steps.add(new Step());
        steps.add(new Step());
        scenario.setSteps(steps);
        when(scenarioRepository.save(scenario)).thenReturn(scenario);
        when(stepService.addStep(any(Step.class))).thenReturn(new Step());

        Scenario result = queueHandler.saveScenario(scenario);

        assertEquals(scenario, result);
        verify(stepService, times(2)).addStep(any(Step.class));
        assertEquals(steps.size(), result.getSteps().size());
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
    void getScenariosByUser_UserExists_ReturnsListOfScenarios() {
        User user = new User();
        List<Scenario> scenarios = new ArrayList<>();
        when(scenarioRepository.findByUser(any(User.class))).thenReturn(new ArrayList<>());

        List<Scenario> result = queueHandler.getScenariosByUser(user);

        assertEquals(scenarios, result);
    }
}