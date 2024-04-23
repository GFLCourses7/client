package com.geeksforless.client.mapper;

import com.geeksforless.client.model.Step;
import com.geeksforless.client.model.dto.StepDto;
import org.springframework.stereotype.Service;

@Service
public class StepMapper {

    public StepDto toDto(Step step) {

        StepDto stepDto = new StepDto();

        stepDto.setAction(step.getAction());
        stepDto.setValue(step.getValue());

        return stepDto;
    }

    public Step toStep(StepDto stepDto) {
        Step step = new Step();
        step.setAction(stepDto.getAction());
        step.setValue(stepDto.getValue());
        return step;
    }
}
