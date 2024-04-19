package com.geeksforless.client.handler;

import com.geeksforless.client.handler.impl.ScenarioSourceQueueHandlerImpl;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.service.Publisher;
import com.geeksforless.client.service.PublisherImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class ScenarioSourceQueueHandlerImplTest {

    private ScenarioSourceQueueHandlerImpl queueHandler;

    @BeforeEach
    void setUp() {
        Publisher publisher = mock(PublisherImpl.class);
        doNothing().when(publisher).sendMessage();
        queueHandler = new ScenarioSourceQueueHandlerImpl();
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
}