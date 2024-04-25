package com.geeksforless.client.mapper;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.dto.ScenarioDtoExternal;
import com.geeksforless.client.model.Step;
import com.geeksforless.client.model.dto.ScenarioDtoInternal;
import com.geeksforless.client.model.dto.StepDtoExternal;
import com.geeksforless.client.model.dto.StepDtoInternal;
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
    public void testToDtoExternal() {

        Scenario scenario = createScenario();

        when(stepMapper.toDtoExternal(any())).thenReturn(new StepDtoExternal("action", "value"));

        ScenarioDtoExternal expected = createScenarioDto();

        ScenarioDtoExternal actual = scenarioMapper.toDtoExternal(scenario);

        assertEquals(expected, actual);
    }

    @Test
    void testExternalToScenario() {
        ScenarioDtoExternal scenarioDto = createScenarioDto();

        when(stepMapper.toStep(any(StepDtoExternal.class))).thenReturn(new Step("action", "value"));

        Scenario expected = createScenario();

        Scenario actual = scenarioMapper.toScenario(scenarioDto);
        assertEquals(expected, actual);

        verify(stepMapper, times(1)).toStep(any(StepDtoExternal.class));
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

    private static ScenarioDtoExternal createScenarioDto() {
        return createScenarioDto("name", "site", List.of(createStepDto()));
    }

    private static ScenarioDtoExternal createScenarioDto(String name, String site, List<StepDtoExternal> steps) {
        ScenarioDtoExternal scenarioDto = new ScenarioDtoExternal();
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


    private static StepDtoExternal createStepDto() {
        return createStepDto("action", "value");
    }

    private static StepDtoExternal createStepDto(String action, String value) {
        return new StepDtoExternal(action, value);
    }


    @Test
    public void testToDtoInternal() {

        Scenario scenario = new Scenario();
        scenario.setId(1L);
        scenario.setSite("site");
        scenario.setName("name");
        scenario.setResult("result");
        scenario.setSteps(List.of(new Step("action", "value")));

        when(stepMapper.toDtoInternal(any())).thenReturn(new StepDtoInternal(0L, "action", "value"));

        ScenarioDtoInternal expected = new ScenarioDtoInternal();
        expected.setId(1L);
        expected.setSite("site");
        expected.setName("name");
        expected.setResult("result");
        expected.setSteps(List.of(new StepDtoInternal(0L, "action", "value")));

        ScenarioDtoInternal actual = scenarioMapper.toDtoInternal(scenario);

        assertEquals(expected, actual);
    }

    @Test
    void testInternalToScenario() {

        ScenarioDtoInternal scenario = new ScenarioDtoInternal();
        scenario.setId(1L);
        scenario.setSite("site");
        scenario.setName("name");
        scenario.setResult("result");
        scenario.setSteps(List.of(new StepDtoInternal(0L, "action", "value")));

        when(stepMapper.toStep(isA(StepDtoInternal.class))).thenReturn(new Step("action", "value"));

        Scenario expected = new Scenario();
        expected.setId(1L);
        expected.setSite("site");
        expected.setName("name");
        expected.setResult("result");
        expected.setSteps(List.of(new Step("action", "value")));

        Scenario actual = scenarioMapper.toScenario(scenario);

        assertEquals(expected, actual);
    }
}
