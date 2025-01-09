package com.controlcash.app.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "my-secret-key");

        jwtTokenProvider.init();
    }

    @Test
    void testCreateToken_ShouldReturnTokenCreated() {
        String email = "user@example.com";
        List<String> roles = Collections.singletonList("ROLE_USER");

        String token = jwtTokenProvider.createToken(email, roles);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGetAuthentication_ShouldReturnAnAuthentication() {
        String email = "user@example.com";
        String token = jwtTokenProvider.createToken(email, Collections.singletonList("ROLE_USER"));
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        assertNotNull(authentication);
        assertEquals(userDetails, authentication.getPrincipal());
    }

    @Test
    void testDecodeToken_ShouldReturnADecodedToken() {
        String email = "user@example.com";
        String token = jwtTokenProvider.createToken(email, Collections.singletonList("ROLE_USER"));

        DecodedJWT decodedJWT = jwtTokenProvider.decodedToken(token);

        assertNotNull(decodedJWT);
        assertEquals(email, decodedJWT.getSubject());
    }

    @Test
    void testResolveToken_ShouldReturnCurrentToken() {
        String bearerToken = "Bearer validToken";
        when(request.getHeader("Authorization")).thenReturn(bearerToken);

        String token = jwtTokenProvider.resolveToken(request);

        assertEquals("validToken", token);
    }

    @Test
    void testResolveToken_ShouldReturnNullWhenNoBearerToken() {
        when(request.getHeader("Authorization")).thenReturn(null);

        String token = jwtTokenProvider.resolveToken(request);

        assertNull(token);
    }

    @Test
    void testValidateToken_GivenAValidToken_ShouldReturnTrue() {
        String email = "user@example.com";
        String token = jwtTokenProvider.createToken(email, Collections.singletonList("ROLE_USER"));

        boolean isValid = jwtTokenProvider.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_GivenANotValidToken_ShouldThrowsAJWTVerificationException() {
        String expiredToken = JWT.create()
                .withSubject("user@example.com")
                .withIssuedAt(new Date(System.currentTimeMillis() - 3600000))
                .withExpiresAt(new Date(System.currentTimeMillis() - 1800000))
                .sign(Algorithm.HMAC256("secret".getBytes()));

        assertThrows(JWTVerificationException.class, () -> jwtTokenProvider.validateToken(expiredToken));
    }
}
