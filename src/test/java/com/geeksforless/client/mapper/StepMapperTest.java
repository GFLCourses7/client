package com.geeksforless.client.mapper;

import com.geeksforless.client.model.Step;
import com.geeksforless.client.model.dto.StepDto;
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
    public void testToDto() {

        Step step = new Step("action", "value");

        StepDto expected = new StepDto("action", "value");
        StepDto actual = stepMapper.toDto(step);

        assertEquals(expected, actual);
    }

    @Test
    void testToStep() {
        StepDto stepDto = new StepDto("action", "value");
        Step expected = new Step("action", "value");
        Step actual = stepMapper.toStep(stepDto);
        assertEquals(expected, actual);
    }
}
