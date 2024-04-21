package com.geeksforless.client.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geeksforless.client.model.User;
import com.geeksforless.client.security.auth.AuthenticationController;
import com.geeksforless.client.security.auth.AuthenticationService;
import com.geeksforless.client.security.auth.dto.AuthRequest;
import com.geeksforless.client.security.auth.dto.AuthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthenticationControllerTest {

    private MockMvc mockMvc;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = mock(AuthenticationService.class);
        AuthenticationController authenticationController = new AuthenticationController(authenticationService);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void testRegister() throws Exception {
        AuthRequest request = new AuthRequest("username", "password");

        User returnedUser = new User();
        returnedUser.setUserName("username");

        when(authenticationService.register(request)).thenReturn(returnedUser);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testAuthenticate() throws Exception {
        AuthRequest request = new AuthRequest("username", "password");
        AuthResponse response = new AuthResponse("token");

        when(authenticationService.authenticate(any(AuthRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("token"));
    }
}
