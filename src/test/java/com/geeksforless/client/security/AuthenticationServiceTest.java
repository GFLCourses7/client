package com.geeksforless.client.security;

import com.geeksforless.client.exception.UsernameIsAlreadyExist;
import com.geeksforless.client.model.User;
import com.geeksforless.client.model.enums.Role;
import com.geeksforless.client.repository.UserRepository;
import com.geeksforless.client.security.auth.AuthenticationService;
import com.geeksforless.client.security.auth.dto.AuthRequest;
import com.geeksforless.client.security.auth.dto.AuthResponse;
import com.geeksforless.client.security.config.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;

public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        authenticationService = new AuthenticationService(userRepository, passwordEncoder, jwtService, authenticationManager);
    }

    @Test
    void testRegister_Successful() {
        AuthRequest request = new AuthRequest("testuser", "testpassword");

        when(userRepository.findByUserName(request.getLogin())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setUserName(request.getLogin());
        savedUser.setPassWord("encodedPassword");
        savedUser.setRole(Role.USER);

        when(userRepository.save(any())).thenReturn(savedUser);

        User registeredUser = authenticationService.register(request);

        assertNotNull(registeredUser);
        assertEquals(request.getLogin(), registeredUser.getUsername());
        assertEquals("encodedPassword", registeredUser.getPassword());
        assertEquals(Role.USER, registeredUser.getRole());
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        AuthRequest request = new AuthRequest("existinguser", "testpassword");

        when(userRepository.findByUserName(request.getLogin())).thenReturn(Optional.of(new User()));

        assertThrows(UsernameIsAlreadyExist.class, () -> authenticationService.register(request));
    }

    @Test
    void testAuthenticate_Successful() {
        AuthRequest request = new AuthRequest("testuser", "testpassword");
        User user = new User();
        user.setUserName(request.getLogin());
        user.setPassWord(request.getPassword());

        when(userRepository.findByUserName(request.getLogin())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtService.generateToken(any(User.class))).thenReturn("testToken");

        AuthResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("testToken", response.getToken());
        verify(jwtService, times(1)).revokeAllUserTokens(user);
        verify(jwtService, times(1)).saveUserToken(user, "testToken");

    }

    @Test
    void testAuthenticate_UserNotFound() {
        AuthRequest request = new AuthRequest("nonexistentuser", "testpassword");

        when(userRepository.findByUserName(request.getLogin())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authenticationService.authenticate(request));
    }
}

