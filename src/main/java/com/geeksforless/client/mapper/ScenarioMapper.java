package com.geeksforless.client.mapper;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.dto.ScenarioDtoExternal;
import com.geeksforless.client.model.dto.ScenarioDtoInternal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScenarioMapper {

    private final StepMapper stepMapper;

    public ScenarioMapper(StepMapper stepMapper) {
        this.stepMapper = stepMapper;
    }

    public ScenarioDtoExternal toDtoExternal(Scenario scenario) {

        ScenarioDtoExternal scenarioDto = new ScenarioDtoExternal();

        scenarioDto.setName(scenario.getName());
        scenarioDto.setSite(scenario.getSite());
        scenarioDto.setResult(scenario.getResult());
        scenarioDto.setSteps(
                Optional.ofNullable(scenario.getSteps())
                        .orElse(new ArrayList<>())
                        .stream()
                        .map(stepMapper::toDtoExternal)
                        .collect(Collectors.toList())
        );

        return scenarioDto;
    }

    public ScenarioDtoInternal toDtoInternal(Scenario scenario) {

        ScenarioDtoInternal scenarioDto = new ScenarioDtoInternal();

        scenarioDto.setId(scenario.getId());
        scenarioDto.setName(scenario.getName());
        scenarioDto.setSite(scenario.getSite());
        scenarioDto.setResult(scenario.getResult());
        scenarioDto.setSteps(
                Optional.ofNullable(scenario.getSteps())
                        .orElse(new ArrayList<>())
                        .stream()
                        .map(stepMapper::toDtoInternal)
                        .collect(Collectors.toList())
        );

        return scenarioDto;
    }

    public Scenario toScenario(ScenarioDtoExternal scenarioDto) {
        Scenario scenario = new Scenario();
        scenario.setName(scenarioDto.getName());
        scenario.setSite(scenarioDto.getSite());
//        scenario.setResult(scenarioDto.getResult());
        scenario.setSteps(
                Optional.ofNullable(scenarioDto.getSteps())
                        .orElse(new ArrayList<>())
                        .stream()
                        .map(stepMapper::toStep)
                        .collect(Collectors.toList())
        );
        return scenario;

    }

    public Scenario toScenario(ScenarioDtoInternal scenarioDto) {

        Scenario scenario = new Scenario();
        scenario.setId(scenarioDto.getId());
        scenario.setName(scenarioDto.getName());
        scenario.setSite(scenarioDto.getSite());
        scenario.setResult(scenarioDto.getResult());
        scenario.setSteps(
                Optional.ofNullable(scenarioDto.getSteps())
                        .orElse(new ArrayList<>())
                        .stream()
                        .map(stepMapper::toStep)
                        .collect(Collectors.toList())
        );
        return scenario;

    }
}
