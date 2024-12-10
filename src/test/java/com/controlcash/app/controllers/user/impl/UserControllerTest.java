package com.controlcash.app.controllers.user.impl;

import com.controlcash.app.dtos.user.request.UserCreateRequestDTO;
import com.controlcash.app.dtos.user.response.UserCreateResponseDTO;
import com.controlcash.app.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.UUID;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class UserControllerTest {

    private static final String USER_BASE_ENDPOINT = "/api/users";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void testCreate_GivenAValidUserCreateRequestDTO_ShouldReturnAnUserCreateResponseDTOAndOk() throws Exception {
        String expectedUsername = "foobar";
        String expectedEmail = "foobar@gmail.com";
        UUID expectedUUID = UUID.randomUUID();
        UserCreateRequestDTO userRequest = new UserCreateRequestDTO(expectedUsername, expectedEmail, "123456", "Foo Bar", 1405.00);
        UserCreateResponseDTO userResponse = new UserCreateResponseDTO(expectedUUID, expectedUsername, expectedEmail);
        Mockito.when(userService.create(Mockito.any(UserCreateRequestDTO.class))).thenReturn(userResponse);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(USER_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedUUID.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value(expectedUsername))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(expectedEmail));
    }
}
