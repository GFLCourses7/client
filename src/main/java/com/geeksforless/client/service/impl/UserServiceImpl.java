package com.geeksforless.client.service.impl;

import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.ScenarioInfo;
import com.geeksforless.client.model.User;
import com.geeksforless.client.repository.UserRepository;
import com.geeksforless.client.service.ScenarioService;
import com.geeksforless.client.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final ScenarioService scenarioService;
    private final ScenarioSourceQueueHandler scenarioQueueHandler;

    public UserServiceImpl(UserRepository userRepository, ScenarioService scenarioService,
                           ScenarioSourceQueueHandler scenarioQueueHandler) {
        this.userRepository = userRepository;
        this.scenarioService = scenarioService;
        this.scenarioQueueHandler = scenarioQueueHandler;
    }

    @Override
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findUserByUserName(userName);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void addScenario(Scenario scenario, String userName) {
        Optional<User> userOptional = getUserByUserName(userName);
        Scenario savedScenario = scenarioService.saveScenario(scenario);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.getScenarios().add(savedScenario);
            savedScenario.setUser(user);

            saveUser(user);
        }

        scenarioQueueHandler.addScenario(savedScenario);
        logger.info("Scenario " + scenario.getName() + " saved and added to the queue");
    }

    @Override
    public List<ScenarioInfo> getResult(String userName) {
        Optional<User> userOptional = getUserByUserName(userName);
        List<ScenarioInfo> resultList = new ArrayList<>();

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            resultList = scenarioService.getScenarioInfoByUser(user);
        }
        return resultList;
    }
}
