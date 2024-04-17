package com.geeksforless.client.handler;

import com.geeksforless.client.controller.dto.ScenarioDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.geeksforless.client.handler.impl.ScenarioSourceQueueHandlerImpl;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.service.Publisher;
import com.geeksforless.client.service.PublisherImpl;
import com.geeksforless.client.model.User;
import com.geeksforless.client.repository.ScenarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScenarioSourceQueueHandlerImplTest {

    @Mock
    private ScenarioRepository scenarioRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ScenarioSourceQueueHandlerImpl scenarioSourceQueueHandler;

    @BeforeEach
    void setUp() {
        Publisher publisher = mock(PublisherImpl.class);
        doNothing().when(publisher).sendMessage();
        queueHandler = new ScenarioSourceQueueHandlerImpl(publisher);
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void addScenario_WithAuthenticatedUser_ShouldSaveScenarioAndAddToQueue() {
        // Arrange
        Scenario scenario = new Scenario();
        scenario.setName("Test Scenario");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(new User());
        when(scenarioRepository.save(any(Scenario.class))).thenReturn(scenario);

        // Act
        scenarioSourceQueueHandler.addScenario(scenario);

        // Assert
        assertEquals(scenarioSourceQueueHandler.getQueue().size(), 1);
        verify(scenarioRepository, times(1)).save(scenario);
    }

    @Test
    void takeScenarioFromQueue_ShouldReturnScenarioDtoFromQueue() {
        // Arrange
        ScenarioDto scenarioDto = new ScenarioDto();
        scenarioDto.setName("Test Scenario");

        Scenario scenario = new Scenario();
        scenario.setName("Test Scenario");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(new User());
        when(scenarioRepository.save(any(Scenario.class))).thenReturn(scenario);


        scenarioSourceQueueHandler.addScenario(scenario);


        // Act
        ScenarioDto takenScenario = scenarioSourceQueueHandler.takeScenarioFromQueue();

        // Assert
        assertNotNull(takenScenario);
        assertEquals(scenarioDto.getScenarioId(), takenScenario.getScenarioId());
        assertEquals(scenarioDto.getName(), takenScenario.getName());
        assertEquals(scenarioDto.getSite(), takenScenario.getSite());
        assertEquals(scenarioDto.getResult(), takenScenario.getResult());
        assertEquals(scenarioDto.getSteps(), takenScenario.getSteps());
    }

    @Test
    void updateScenario_WithExistingScenarioDto_ShouldUpdateScenarioAndReturnUpdatedDto() {
        // Arrange
        ScenarioDto scenarioDto = new ScenarioDto();
        scenarioDto.setScenarioId(1L);
        scenarioDto.setResult("Success");

        Scenario existingScenario = new Scenario();
        existingScenario.setId(1L);
        existingScenario.setName("Test Scenario");

        when(scenarioRepository.findById(scenarioDto.getScenarioId())).thenReturn(of(existingScenario));
        when(scenarioRepository.save(any(Scenario.class))).thenReturn(existingScenario);

        // Act
        ScenarioDto updatedScenarioDto = scenarioSourceQueueHandler.updateScenario(scenarioDto);

        // Assert
        assertNotNull(updatedScenarioDto);
        assertEquals(scenarioDto.getScenarioId(), updatedScenarioDto.getScenarioId());
        assertEquals(scenarioDto.getResult(), updatedScenarioDto.getResult());
        assertTrue(existingScenario.isDone());
        assertEquals(scenarioDto.getResult(), existingScenario.getResult());
    }
}
