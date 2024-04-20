package com.geeksforless.client.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geeksforless.client.handler.ScenarioSourceQueueHandler;
import com.geeksforless.client.model.User;
import com.geeksforless.client.model.enums.Role;
import com.geeksforless.client.repository.UserRepository;
import com.geeksforless.client.security.auth.dto.Token;
import com.geeksforless.client.repository.TokenRepository;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationFilterTest {

    private final MockMvc mockMvc;
    @MockBean
    private TokenRepository tokenRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ScenarioSourceQueueHandler sourceQueueHandler;

    private final JwtService jwtService;

    @Autowired
    public JwtAuthenticationFilterTest(MockMvc mockMvc, JwtService jwtService) {
        this.mockMvc = mockMvc;
        this.jwtService = jwtService;
    }

    @Test
    public void testJwtAuthenticationFilterForUser() throws Exception {

        User user = new User();
        user.setUserName("testUser");
        user.setPassWord("encodedPassword");
        user.setRole(Role.USER);

        String token = jwtService.generateToken(user);
        Token userToken = new Token(token, null, false, false, user);

        Mockito.when(tokenRepository.findByToken(token)).thenReturn(java.util.Optional.of(userToken));
        Mockito.when(userRepository.findByUserName(user.getUsername())).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/scenario/add")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testJwtAuthenticationFilterForWorker() throws Exception {

        User user = new User();
        user.setUserName("testWorker");
        user.setPassWord("encodedPassword");
        user.setRole(Role.WORKER);

        String token = jwtService.generateToken(user);
        Token userToken = new Token(token, null, false, false, user);

        Mockito.when(tokenRepository.findByToken(token)).thenReturn(java.util.Optional.of(userToken));
        Mockito.when(userRepository.findByUserName(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(sourceQueueHandler.takeScenario()).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/internal/scenario/get-scenario")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void testJwtAuthenticationFilter_InvalidToken() throws Exception {
        String invalidToken = "invalid.Token.test";

        assertThrows(MalformedJwtException.class, ()-> {
            mockMvc.perform(MockMvcRequestBuilders.get("/")
                            .header("Authorization", "Bearer " + invalidToken))
                    .andReturn();
        });
    }

    @Test
    public void testJwtAuthenticationFilter_NoToken() throws Exception {

        String json = new ObjectMapper().writeValueAsString(Map.of(
                "login", "testUser", "password", "testPassword")
        );

        when(userRepository.save(any())).thenReturn(new User());

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isForbidden());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/authenticate"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

    }
}
