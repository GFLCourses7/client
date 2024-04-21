package com.geeksforless.client.security.config;

import com.geeksforless.client.model.User;
import com.geeksforless.client.model.enums.Role;
import com.geeksforless.client.model.enums.TokenType;
import com.geeksforless.client.repository.TokenRepository;
import com.geeksforless.client.security.auth.dto.Token;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration
public class JwtServiceTest {

    private JwtService jwtService;
    private TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        this.tokenRepository = mock(TokenRepository.class);
        this.jwtService = new JwtService(tokenRepository, 5000L, "SecretKeyForTesting+SpringSecurityLayer123456789");
    }
    @Test
    public void generationAndExtractingToken_Success(){
        User user = createUser();
        String token = jwtService.generateToken(user);

        assertEquals(user.getUsername(), jwtService.extractUsername(token));
    }

    @Test
    public void generationAndExtractingToken_Failure(){
        User user1 = createUser();
        User user2 = createUser();
        user2.setUserName("Second user");

        String token1 = jwtService.generateToken(user1);
        String token2 = jwtService.generateToken(user2);

        assertNotEquals(user1.getUsername(), jwtService.extractUsername(token2));
        assertNotEquals(token1, token2);
    }

    @Test
    public void testIsTokenValid_Success() {
        User user = createUser();
        assertTrue(jwtService.isTokenValid(jwtService.generateToken(user), user));
    }
    @Test
    public void testIsTokenValid_Failure() {
        User user1 = createUser();
        User user2 = createUser();
        user2.setUserName("SecondUser");
        assertFalse(jwtService.isTokenValid(jwtService.generateToken(user1), user2));
    }
    @Test
    public void testIsTokenValid_Error() {
        User user = createUser();
        assertThrows(MalformedJwtException.class, () -> jwtService.isTokenValid("wrongToken", user));
    }

    @Test
    void testSaveUserToken() {
        User user = createUser();
        jwtService.saveUserToken(user, "testToken");
        Mockito.verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void testRevokeAllUserTokens() {
        User user = createUser();
        Token token = new Token("testToken", TokenType.BEARER, false, false, user);

        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(Collections.singletonList(token));

        jwtService.revokeAllUserTokens(user);

        Mockito.verify(tokenRepository, times(1)).saveAll(any());
        assertTrue(token.isExpired());
        assertTrue(token.isRevoked());
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setRole(Role.USER);
        return user;
    }
}
