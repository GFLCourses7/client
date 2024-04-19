package com.geeksforless.client.service.impl;

import com.geeksforless.client.model.Step;
import com.geeksforless.client.repository.StepRepository;
import com.geeksforless.client.service.StepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StepServiceImpl implements StepService {
    private final StepRepository stepRepository;

    public StepServiceImpl(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    @Override
    @Transactional
    public void addStep(Step step) {
        stepRepository.save(step);
    }
}
