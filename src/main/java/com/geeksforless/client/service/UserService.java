package com.geeksforless.client.service;

import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
        Optional<User> getUserByUserName(String userName);

        void saveUser(User user);

        void addScenario(Scenario scenario, String userName);

        List<Scenario> getResults(String userName);
}
