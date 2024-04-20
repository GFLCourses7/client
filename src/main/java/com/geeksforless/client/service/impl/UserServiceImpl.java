package com.geeksforless.client.service.impl;

import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.projections.ScenarioInfo;
import com.geeksforless.client.model.User;
import com.geeksforless.client.repository.UserRepository;
import com.geeksforless.client.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final ScenarioSourceQueueHandler scenarioQueueHandler;

    public UserServiceImpl(UserRepository userRepository, ScenarioSourceQueueHandler scenarioQueueHandler) {
        this.userRepository = userRepository;
        this.scenarioQueueHandler = scenarioQueueHandler;
    }

    @Override
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void addScenario(Scenario scenario, String userName) {
        Optional<User> userOptional = getUserByUserName(userName);

        if (userOptional.isEmpty()) {
            logger.error("User with username '{}' not found", userName);
            throw new UsernameNotFoundException("User not found with username: " + userName);
        }
        Scenario savedScenario = scenarioQueueHandler.saveScenario(scenario);

        User user = userOptional.get();

        user.getScenarios().add(savedScenario);
        savedScenario.setUser(user);

        saveUser(user);

        scenarioQueueHandler.addScenario(savedScenario);
        logger.info("Scenario {} saved and added to the queue", savedScenario.getName());
    }

    @Override
    public List<ScenarioInfo> getResult(String userName) {
        Optional<User> userOptional = getUserByUserName(userName);

        if (userOptional.isEmpty()) {
            logger.error("User with username '{}' not found", userName);
            throw new UsernameNotFoundException("User not found with username: " + userName);
        }

        User user = userOptional.get();
        return scenarioQueueHandler.getScenarioInfoByUser(user);
    }
}
