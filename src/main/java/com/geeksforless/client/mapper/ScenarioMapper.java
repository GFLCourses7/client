package com.geeksforless.client.mapper;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.ScenarioDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class ScenarioMapper {

    private final StepMapper stepMapper;

    public ScenarioMapper(StepMapper stepMapper) {
        this.stepMapper = stepMapper;
    }

    public ScenarioDto toDto(Scenario scenario) {

        ScenarioDto scenarioDto = new ScenarioDto();

        scenarioDto.setId(scenario.getId());
        scenarioDto.setName(scenario.getName());
        scenarioDto.setSite(scenario.getSite());
        scenarioDto.setResult(scenario.getResult());
        scenarioDto.setSteps(scenario.getSteps().stream().map(stepMapper::toDto).toList());

        return scenarioDto;
    }

}
