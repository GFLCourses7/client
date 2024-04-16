package com.geeksforless.client.handler;

import com.geeksforless.client.handler.impl.ScenarioSourceQueueHandlerImpl;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.repository.ScenarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ScenarioSourceQueueHandlerImplTest {

    @Mock
    private ScenarioRepository scenarioRepository;
    private ScenarioSourceQueueHandlerImpl queueHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        queueHandler = new ScenarioSourceQueueHandlerImpl(scenarioRepository);
    }

    @Test
    void addScenario_SuccessfullyAdded() {
        // Arrange
        Scenario scenario = new Scenario();
        when(scenarioRepository.save(any(Scenario.class))).thenReturn(scenario);

        // Act
        queueHandler.addScenario(scenario);

        // Assert
        assertEquals(1, queueHandler.getQueue().size());
        assertTrue(queueHandler.getQueue().contains(scenario));
        verify(scenarioRepository, times(1)).save(any(Scenario.class));
    }

    @Test
    void takeScenarioFromQueueReturnsScenario() {
        // Arrange
        Scenario scenario = new Scenario();
        when(scenarioRepository.save(any(Scenario.class))).thenReturn(scenario);

        queueHandler.addScenario(scenario);

        // Act
        Scenario takenScenario = queueHandler.takeScenario();

        // Assert
        assertNotNull(takenScenario);
        assertEquals(scenario, takenScenario);
        assertEquals(0, queueHandler.getQueue().size());
        verify(scenarioRepository, times(1)).save(any(Scenario.class));
    }

    @Test
    void takeScenarioFromEmptyQueueReturnsNull() {
        // Act
        Scenario takenScenario = queueHandler.takeScenario();

        // Assert
        assertNull(takenScenario);
        assertEquals(0, queueHandler.getQueue().size());
        verifyNoInteractions(scenarioRepository);
    }

    @Test
    void updateScenario() {
        // Mock scenario data
        Long scenarioId = 1L;

        Scenario existingScenario = new Scenario();
        existingScenario.setId(scenarioId);
        existingScenario.setDone(false);
        existingScenario.setResult("Old Result");

        // Mock repository behavior
        when(scenarioRepository.findById(anyLong())).thenReturn(of(existingScenario));
        when(scenarioRepository.save(any(Scenario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service method
        Scenario updatedScenario = new Scenario();
        updatedScenario.setId(scenarioId);
        updatedScenario.setDone(true);
        updatedScenario.setResult("New Result");

        Scenario scenarioFromRepo = queueHandler.updateScenario(updatedScenario);

        assertEquals(updatedScenario, scenarioFromRepo);

        // Verify that the scenario was updated and added to the queue
        verify(scenarioRepository, times(1)).findById(scenarioId);
        verify(scenarioRepository, times(1)).save(existingScenario);

    }
}