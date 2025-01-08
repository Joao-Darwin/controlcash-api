package com.controlcash.app.controllers.auth.impl;

import com.controlcash.app.dtos.auth.request.Credentials;
import com.controlcash.app.dtos.auth.response.AuthResponse;
import com.controlcash.app.models.Permission;
import com.controlcash.app.security.jwt.JwtTokenFilter;
import com.controlcash.app.security.jwt.JwtTokenProvider;
import com.controlcash.app.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = {AuthController.class})
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AuthControllerTest {

    private static final String AUTH_BASE_ENDPOINT = "/auth";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;
    @MockBean
    private JwtTokenFilter jwtTokenFilter;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private UUID expectedId;
    private String expectedEmail;
    private String expectedUserName;
    private String expectedFullName;
    private String expectedToken;
    private String expectedPermission;
    private boolean expectedIsAuthenticated;
    private List<Permission> expectedPermissions;
    private Credentials credentials;

    @BeforeEach
    void setUp() {
        expectedId = UUID.randomUUID();
        expectedEmail = "foobar@mail.com";
        expectedUserName = "foobar";
        expectedFullName = "Foo Bar";
        expectedToken = "token";
        expectedPermission = "admin";
        expectedIsAuthenticated = true;
        expectedPermissions = List.of(new Permission(UUID.randomUUID(), expectedPermission, List.of()));
        credentials = new Credentials(expectedEmail, "12345");
    }

    @Test
    void testSignIn_GivenValidCredentials_ShouldReturnAnUserAuthenticatedAndOk() throws Exception {
        AuthResponse authResponse = new AuthResponse(
                expectedId,
                expectedEmail,
                expectedUserName,
                expectedFullName,
                expectedToken,
                expectedIsAuthenticated,
                expectedPermissions
        );
        Mockito.when(authService.signIn(Mockito.any(Credentials.class))).thenReturn(authResponse);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(AUTH_BASE_ENDPOINT + "/" + "signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(expectedEmail))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value(expectedUserName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value(expectedFullName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(expectedToken))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAuthenticated").value(expectedIsAuthenticated))
                .andExpect(MockMvcResultMatchers.jsonPath("$.permissions[0].authority").value(expectedPermission));

        Mockito.verify(authService, Mockito.times(1)).signIn(Mockito.any(Credentials.class));
    }

    @Test
    void testSignIn_GivenNotValidCredentials_ShouldReturnAnUserAuthenticatedAndUnauthorized() throws Exception {
        String expectedExceptionMessage = "Invalid credentials. Email used: '" + credentials.email() + "'. Password used: '" + credentials.password() + "'";
        Mockito.when(authService.signIn(Mockito.any(Credentials.class))).thenThrow(new IllegalArgumentException(expectedExceptionMessage));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(AUTH_BASE_ENDPOINT + "/" + "signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedExceptionMessage));

        Mockito.verify(authService, Mockito.times(1)).signIn(Mockito.any(Credentials.class));
    }
}
