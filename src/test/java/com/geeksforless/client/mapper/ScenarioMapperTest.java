package com.geeksforless.client.mapper;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.dto.ScenarioDto;
import com.geeksforless.client.model.Step;
import com.geeksforless.client.model.dto.StepDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ScenarioMapperTest {

    @InjectMocks
    private ScenarioMapper scenarioMapper;

    @Mock
    private StepMapper stepMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToDto() {

        Scenario scenario = createScenario();

        when(stepMapper.toDto(any())).thenReturn(new StepDto("action", "value"));

        ScenarioDto expected = createScenarioDto();

        ScenarioDto actual = scenarioMapper.toDto(scenario);

        assertEquals(expected, actual);
    }

    @Test
    void testToScenario() {
        ScenarioDto scenarioDto = createScenarioDto();

        when(stepMapper.toStep(any(StepDto.class))).thenReturn(new Step("action", "value"));

        Scenario expected = createScenario();

        Scenario actual = scenarioMapper.toScenario(scenarioDto);
        assertEquals(expected, actual);

        verify(stepMapper, times(1)).toStep(any(StepDto.class));
    }

    private static Scenario createScenario() {
        return createScenario("name", "site", List.of(createStep()));
    }

    private static Scenario createScenario(String name, String site, List<Step> steps) {
        Scenario scenario = new Scenario();
        scenario.setName(name);
        scenario.setSite(site);
        scenario.setSteps(steps);
        return scenario;
    }

    private static ScenarioDto createScenarioDto() {
        return createScenarioDto("name", "site", List.of(createStepDto()));
    }

    private static ScenarioDto createScenarioDto(String name, String site, List<StepDto> steps) {
        ScenarioDto scenarioDto = new ScenarioDto();
        scenarioDto.setName(name);
        scenarioDto.setSite(site);
        scenarioDto.setSteps(steps);
        return scenarioDto;
    }

    private static Step createStep() {
        return createStep("action", "value");
    }

    private static Step createStep(String action, String value) {
        return new Step(action, value);
    }


    private static StepDto createStepDto() {
        return createStepDto("action", "value");
    }

    private static StepDto createStepDto(String action, String value) {
        return new StepDto(action, value);
    }
}
