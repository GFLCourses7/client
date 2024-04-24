package com.geeksforless.client.mapper;

import com.geeksforless.client.model.Step;
import com.geeksforless.client.model.dto.StepDtoExternal;
import com.geeksforless.client.model.dto.StepDtoInternal;
import org.springframework.stereotype.Service;

@Service
public class StepMapper {

    public StepDtoExternal toDtoExternal(Step step) {

        StepDtoExternal stepDto = new StepDtoExternal();

        stepDto.setAction(step.getAction());
        stepDto.setValue(step.getValue());

        return stepDto;
    }

    public StepDtoInternal toDtoInternal(Step step) {

        StepDtoInternal stepDto = new StepDtoInternal();

        stepDto.setId(step.getId());
        stepDto.setAction(step.getAction());
        stepDto.setValue(step.getValue());

        return stepDto;
    }

    public Step toStep(StepDtoExternal stepDto) {
        Step step = new Step();
        step.setAction(stepDto.getAction());
        step.setValue(stepDto.getValue());
        return step;
    }

    public Step toStep(StepDtoInternal stepDto) {
        Step step = new Step();
        step.setId(stepDto.getId());
        step.setAction(stepDto.getAction());
        step.setValue(stepDto.getValue());
        return step;
    }
}
