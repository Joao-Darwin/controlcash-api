package com.controlcash.app.services;

import com.controlcash.app.dtos.auth.request.Credentials;
import com.controlcash.app.dtos.auth.response.AuthResponse;
import com.controlcash.app.models.Permission;
import com.controlcash.app.models.User;
import com.controlcash.app.repositories.UserRepository;
import com.controlcash.app.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private UUID expectedId;
    private String expectedEmail;
    private String expectedUserName;
    private String expectedFullName;
    private String expectedToken;
    private String expectedPermission;
    private String expectedPassword;
    private Double expectedSalary;
    private boolean expectedAccountNonExpired;
    private boolean expectedAccountNonLocked;
    private boolean expectedCredentialsNonExpired;
    private boolean expectedEnabled;
    private boolean expectedIsAuthenticated;
    private AuthResponse expectedAuthResponse;
    private List<Permission> expectedPermissions;
    private Credentials credentials;
    private User user;

    @BeforeEach
    void setUp() {
        expectedId = UUID.randomUUID();
        expectedEmail = "foobar@mail.com";
        expectedUserName = "foobar";
        expectedFullName = "Foo Bar";
        expectedToken = "token";
        expectedPermission = "admin";
        expectedPassword = "12345";
        expectedAccountNonExpired = false;
        expectedAccountNonLocked = false;
        expectedCredentialsNonExpired = false;
        expectedEnabled = false;
        expectedIsAuthenticated = true;
        expectedPermissions = List.of(new Permission(UUID.randomUUID(), expectedPermission, List.of()));
        expectedAuthResponse = new AuthResponse(
                expectedId,
                expectedEmail,
                expectedUserName,
                expectedFullName,
                expectedToken,
                expectedIsAuthenticated,
                expectedPermissions
        );
        credentials = new Credentials(expectedEmail, expectedPassword);
        user = new User(
                expectedId,
                expectedUserName,
                expectedEmail,
                expectedPassword,
                expectedFullName,
                expectedSalary,
                expectedAccountNonExpired,
                expectedAccountNonLocked,
                expectedCredentialsNonExpired,
                expectedEnabled,
                expectedPermissions,
                List.of(),
                List.of()
        );
    }

    @Test
    void testSignIn_GivenAValidCredentials_ShouldReturnAnAuthResponse() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(jwtTokenProvider.createToken(Mockito.anyString(), Mockito.anyList())).thenReturn(expectedToken);

        AuthResponse response = authService.signIn(credentials);

        Assertions.assertEquals(expectedId, response.id());
        Assertions.assertEquals(expectedEmail, response.email());
        Assertions.assertEquals(expectedUserName, response.userName());
        Assertions.assertEquals(expectedFullName, response.fullName());
        Assertions.assertEquals(expectedToken, response.token());
        Assertions.assertEquals(expectedIsAuthenticated, response.isAuthenticated());
        Assertions.assertEquals(expectedPermissions.size(), response.permissions().size());
    }
}
