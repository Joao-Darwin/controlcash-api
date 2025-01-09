package com.controlcash.app.services;

import com.controlcash.app.dtos.auth.request.Credentials;
import com.controlcash.app.dtos.auth.response.AuthResponse;
import com.controlcash.app.dtos.user.request.UserCreateRequestDTO;
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
import org.springframework.security.core.Authentication;
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
    private String expectedExceptionMessage;
    private Double expectedSalary;
    private boolean expectedIsAuthenticated;
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
        expectedExceptionMessage = "Invalid credentials. Email used: '" + expectedEmail + "'. Password used: '" + expectedPassword + "'";
        expectedSalary = 1300.0;
        expectedIsAuthenticated = true;
        expectedPermissions = List.of(new Permission(UUID.randomUUID(), expectedPermission, List.of()));
        credentials = new Credentials(expectedEmail, expectedPassword);
        user = new User(
                expectedId,
                expectedUserName,
                expectedEmail,
                expectedPassword,
                expectedFullName,
                expectedSalary,
                false,
                false,
                false,
                false,
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
        Assertions.assertEquals(expectedPermission, response.permissions().get(0).getAuthority());

        Mockito.verify(userRepository, Mockito.only()).findByEmail(Mockito.anyString());
        Mockito.verify(passwordEncoder, Mockito.only()).matches(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(authenticationManager, Mockito.only()).authenticate(Mockito.any(Authentication.class));
        Mockito.verify(jwtTokenProvider, Mockito.only()).createToken(Mockito.anyString(), Mockito.anyList());
    }

    @Test
    void testSignIn_GivenANotValidEmail_ShouldThrowsAnIllegalArgumentException() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        IllegalArgumentException expectedException = Assertions.assertThrows(IllegalArgumentException.class, () -> authService.signIn(credentials));

        Assertions.assertEquals(expectedExceptionMessage, expectedException.getMessage());

        Mockito.verify(userRepository, Mockito.only()).findByEmail(Mockito.anyString());
        Mockito.verify(passwordEncoder, Mockito.never()).matches(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(authenticationManager, Mockito.never()).authenticate(Mockito.any(Authentication.class));
        Mockito.verify(jwtTokenProvider, Mockito.never()).createToken(Mockito.anyString(), Mockito.anyList());
    }

    @Test
    void testSignIn_GivenANotValidPassword_ShouldThrowsAnIllegalArgumentException() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        IllegalArgumentException expectedException = Assertions.assertThrows(IllegalArgumentException.class, () -> authService.signIn(credentials));

        Assertions.assertEquals(expectedExceptionMessage, expectedException.getMessage());

        Mockito.verify(userRepository, Mockito.only()).findByEmail(Mockito.anyString());
        Mockito.verify(passwordEncoder, Mockito.only()).matches(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(authenticationManager, Mockito.never()).authenticate(Mockito.any(Authentication.class));
        Mockito.verify(jwtTokenProvider, Mockito.never()).createToken(Mockito.anyString(), Mockito.anyList());
    }

    @Test
    void testSignUp_GivenAUser_ShouldReturnAnAuthResponse() {
        UserCreateRequestDTO userToCreate = new UserCreateRequestDTO(
                expectedUserName,
                expectedEmail,
                expectedPassword,
                expectedFullName,
                expectedSalary
        );
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(jwtTokenProvider.createToken(Mockito.anyString(), Mockito.anyList())).thenReturn(expectedToken);

        AuthResponse response = authService.signUp(userToCreate);

        Assertions.assertEquals(expectedId, response.id());
        Assertions.assertEquals(expectedEmail, response.email());
        Assertions.assertEquals(expectedUserName, response.userName());
        Assertions.assertEquals(expectedFullName, response.fullName());
        Assertions.assertEquals(expectedToken, response.token());
        Assertions.assertEquals(expectedIsAuthenticated, response.isAuthenticated());
        Assertions.assertEquals(expectedPermissions.size(), response.permissions().size());
        Assertions.assertEquals(expectedPermission, response.permissions().get(0).getAuthority());

        Mockito.verify(passwordEncoder, Mockito.only()).encode(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.only()).save(Mockito.any());
        Mockito.verify(authenticationManager, Mockito.only()).authenticate(Mockito.any(Authentication.class));
        Mockito.verify(jwtTokenProvider, Mockito.only()).createToken(Mockito.anyString(), Mockito.anyList());
    }
}
