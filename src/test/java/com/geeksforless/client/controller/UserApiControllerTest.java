package com.geeksforless.client.controller;

import com.geeksforless.client.mapper.ScenarioMapper;
import com.geeksforless.client.model.Scenario;
import com.geeksforless.client.model.dto.ScenarioDto;
import com.geeksforless.client.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserApiControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private ScenarioMapper scenarioMapper;

    @InjectMocks
    private UserApiController userApiController;

    @Test
    void addScenario_ValidScenario_ReturnsOk() {
        Scenario scenario = new Scenario();
        ScenarioDto scenarioDto = new ScenarioDto();
        UserDetails userDetails = User.withUsername("testUser")
                .password("testPassword")
                .roles("USER")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(scenarioMapper.toScenario(any(ScenarioDto.class))).thenReturn(scenario);

        ResponseEntity<?> responseEntity = userApiController.addScenario(scenarioDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(scenarioMapper, times(1)).toScenario(any(ScenarioDto.class));
        verify(userService, times(1)).addScenario(scenario, "testUser");
    }

    @Test
    void addScenario_NullScenario_ReturnsBadRequest() {
        ResponseEntity<?> responseEntity = userApiController.addScenario(null);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(userService, never()).addScenario(any(), any());
    }

    @Test
    void getResult_AuthenticatedUser_ReturnsListOfScenarioDTO() {
        ScenarioDto dto = new ScenarioDto();
        dto.setName("name");
        List<ScenarioDto> scenarioDtoList = new ArrayList<>(List.of(dto));

        Scenario scenario = new Scenario();
        scenario.setName(dto.getName());
        List<Scenario> scenarios = List.of(scenario);

        when(userService.getResult("testUser")).thenReturn(scenarios);
        UserDetails userDetails = User.withUsername("testUser")
                .password("testPassword")
                .roles("USER")
                .build();

        when(scenarioMapper.toDto(any(Scenario.class))).thenReturn(dto);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<List<ScenarioDto>> responseEntity = userApiController.getResult();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(scenarioDtoList, responseEntity.getBody());
    }

    @Test
    void getResult_UnauthenticatedUser_ReturnsNotFound() {
        SecurityContextHolder.clearContext();

        ResponseEntity<List<ScenarioDto>> responseEntity = userApiController.getResult();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(userService, never()).getResult(any());
    }
}
