package com.geeksforless.client.mapper;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.ScenarioDto;
import com.geeksforless.client.model.Step;
import com.geeksforless.client.model.StepDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

        Scenario scenario = new Scenario();
        scenario.setName("name");
        scenario.setSite("site");
        scenario.setSteps(List.of(new Step("action", "value")));

        when(stepMapper.toDto(any())).thenReturn(new StepDto("action", "value"));

        ScenarioDto expected = new ScenarioDto();
        expected.setName("name");
        expected.setSite("site");
        expected.setSteps(List.of(new StepDto("action", "value")));

        ScenarioDto actual = scenarioMapper.toDto(scenario);

        assertEquals(expected, actual);
    }

}
