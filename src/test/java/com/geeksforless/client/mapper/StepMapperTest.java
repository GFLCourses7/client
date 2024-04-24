package com.geeksforless.client.mapper;

import com.geeksforless.client.model.Step;
import com.geeksforless.client.model.dto.StepDtoExternal;
import com.geeksforless.client.model.dto.StepDtoInternal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepMapperTest {

    @InjectMocks
    private StepMapper stepMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToDtoExternal() {

        Step step = new Step("action", "value");

        StepDtoExternal expected = new StepDtoExternal("action", "value");
        StepDtoExternal actual = stepMapper.toDtoExternal(step);

        assertEquals(expected, actual);
    }

    @Test
    void testExternalToStep() {
        StepDtoExternal stepDto = new StepDtoExternal("action", "value");
        Step expected = new Step("action", "value");
        Step actual = stepMapper.toStep(stepDto);
        assertEquals(expected, actual);
    }

    @Test
    public void testToDtoInternal() {

        Step step = new Step("action", "value");
        step.setId(1L);

        StepDtoInternal expected = new StepDtoInternal(1L, "action", "value");

        StepDtoInternal actual = stepMapper.toDtoInternal(step);

        assertEquals(expected, actual);
    }

    @Test
    void testInternalToStep() {
        StepDtoInternal stepDto = new StepDtoInternal(1L, "action", "value");
        Step expected = new Step("action", "value");
        expected.setId(1L);
        Step actual = stepMapper.toStep(stepDto);
        assertEquals(expected, actual);
    }
}
