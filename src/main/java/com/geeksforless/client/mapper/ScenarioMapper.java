package com.geeksforless.client.mapper;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.dto.ScenarioDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ScenarioMapper {

    private final StepMapper stepMapper;

    public ScenarioMapper(StepMapper stepMapper) {
        this.stepMapper = stepMapper;
    }

    public ScenarioDto toDto(Scenario scenario) {

        ScenarioDto scenarioDto = new ScenarioDto();

        scenarioDto.setName(scenario.getName());
        scenarioDto.setSite(scenario.getSite());
        scenarioDto.setResult(scenario.getResult());
        scenarioDto.setSteps(
                Optional.ofNullable(scenario.getSteps())
                        .orElse(new ArrayList<>())
                        .stream()
                        .map(stepMapper::toDto)
                        .toList()
        );

        return scenarioDto;
    }

    public Scenario toScenario(ScenarioDto scenarioDto) {
        Scenario scenario = new Scenario();
        scenario.setName(scenarioDto.getName());
        scenario.setSite(scenarioDto.getSite());
//        scenario.setResult(scenarioDto.getResult());
        scenario.setSteps(
                Optional.ofNullable(scenarioDto.getSteps())
                        .orElse(new ArrayList<>())
                        .stream()
                        .map(stepMapper::toStep)
                        .toList()
        );
        return scenario;

    }
}
