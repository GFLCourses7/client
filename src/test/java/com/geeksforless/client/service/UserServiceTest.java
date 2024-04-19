package com.geeksforless.client.service;

import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.projections.ScenarioInfo;
import com.geeksforless.client.model.User;
import com.geeksforless.client.model.enums.Role;
import com.geeksforless.client.repository.UserRepository;
import com.geeksforless.client.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ScenarioSourceQueueHandler scenarioQueueHandler;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUserByUserName_UserExists_ReturnsOptionalUser() {
        User user = new User("testUser", "password", Role.USER);
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUserName("testUser");

        assertEquals(Optional.of(user), result);
    }

    @Test
    void getUserByUserName_UserDoesNotExist_ReturnsEmptyOptional() {
        when(userRepository.findByUserName("nonExistingUser")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByUserName("nonExistingUser");

        assertEquals(Optional.empty(), result);
    }

    @Test
    void saveUser_ValidUser_SavesUser() {
        User user = new User("testUser", "password", Role.USER);

        userService.saveUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void addScenario_UserExists_SavesScenarioAndAddsToQueue() {
        User user = new User("testUser", "password", Role.USER);
        Scenario scenario = new Scenario(1L, "Name", "site", "result", true, user, new ArrayList<>());
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));
        when(scenarioQueueHandler.saveScenario(scenario)).thenReturn(scenario);

        userService.addScenario(scenario, "testUser");

        verify(userRepository, times(1)).save(user);
        verify(scenarioQueueHandler, times(1)).addScenario(scenario);
    }


    @Test
    public void testAddScenario_UserNotFound() {
        String userName = "nonExistingUser";
        Scenario scenario = new Scenario();

        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.addScenario(scenario, userName));
    }

    @Test
    void getResult_UserExists_ReturnsListOfScenarioInfo() {
        User user = new User("testUser", "password", Role.USER);
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));
        List<ScenarioInfo> scenarioInfos = new ArrayList<>();
        when(scenarioQueueHandler.getScenarioInfoByUser(user)).thenReturn(scenarioInfos);

        List<ScenarioInfo> result = userService.getResult("testUser");

        assertEquals(scenarioInfos, result);
    }

    @Test
    public void testGetResult() {
        String userName = "testUser";
        User user = new User(userName, "pass", Role.USER);
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
        List<ScenarioInfo> scenarioInfos = new ArrayList<>();
        when(scenarioQueueHandler.getScenarioInfoByUser(user)).thenReturn(scenarioInfos);

        List<ScenarioInfo> result = userService.getResult("testUser");

        assertEquals(scenarioInfos, result);
    }

    @Test
    public void testGetResult_UserNotFound() {
        String userName = "nonExistingUser";
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.getResult(userName));
    }
}
