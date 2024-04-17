package com.geeksforless.client.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.geeksforless.client.handler.impl.ScenarioSourceQueueHandlerImpl;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.ScenarioDto;
import com.geeksforless.client.repository.ScenarioRepository;
import com.geeksforless.client.service.Publisher;
import com.geeksforless.client.service.PublisherImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.util.Optional.of;

import java.util.Optional;

public class ScenarioSourceQueueHandlerImplTest {
    @InjectMocks
    private ScenarioSourceQueueHandlerImpl queueHandler;

    @Mock
    private ScenarioRepository scenarioRepository;

    @BeforeEach
    void setUp() {
        Publisher publisher = mock(PublisherImpl.class);
        doNothing().when(publisher).sendMessage();
        queueHandler = new ScenarioSourceQueueHandlerImpl(publisher, scenarioRepository);
        MockitoAnnotations.openMocks(this);
    }

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
    void takeScenarioFromQueueReturnsScenario() throws InterruptedException {
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
    void updateScenario_WithExistingScenarioDto_ShouldUpdateScenarioAndReturnUpdatedDto() {
        // Arrange
        ScenarioDto scenarioDto = new ScenarioDto();
        scenarioDto.setId(1L);
        scenarioDto.setResult("Success");

        Scenario existingScenario = new Scenario();
        existingScenario.setId(1L);
        existingScenario.setName("Test Scenario");

        when(scenarioRepository.findById(scenarioDto.getId())).thenReturn(of(existingScenario));
        when(scenarioRepository.save(any(Scenario.class))).thenReturn(existingScenario);
    }
}