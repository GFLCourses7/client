package com.geeksforless.client.mapper;

import com.geeksforless.client.model.Step;
import com.geeksforless.client.model.StepDto;
import org.springframework.stereotype.Service;

@Service
public class StepMapper {

    public StepDto toDto(Step step) {

        StepDto stepDto = new StepDto();

        stepDto.setAction(step.getAction());
        stepDto.setValue(step.getValue());

        return stepDto;
    }
}
