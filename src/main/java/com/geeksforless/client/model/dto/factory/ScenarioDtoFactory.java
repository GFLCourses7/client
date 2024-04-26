package com.geeksforless.client.model.dto.factory;

import com.geeksforless.client.model.dto.ScenarioDtoExternal;
import com.geeksforless.client.model.dto.ScenarioDtoInternal;
import org.springframework.stereotype.Service;

@Service
public class ScenarioDtoFactory {

    public ScenarioDtoFactory() {
    }

    public ScenarioDtoInternal createInternalEmpty() {
        return new ScenarioDtoInternal();
    }

    public ScenarioDtoExternal createExternalEmpty() {
        return new ScenarioDtoExternal();
    }
}
