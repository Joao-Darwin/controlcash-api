package com.controlcash.app.controllers.user.impl;

import com.controlcash.app.dtos.user.request.UserCreateRequestDTO;
import com.controlcash.app.dtos.user.response.UserAllResponseDTO;
import com.controlcash.app.dtos.user.response.UserCompleteResponseDTO;
import com.controlcash.app.dtos.user.response.UserCreateResponseDTO;
import com.controlcash.app.services.UserService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
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

    private String expectedUsername;
    private String expectedFullName;
    private String expectedEmail;
    private Double expectedSalary;
    private UUID expectedUUID;
    private UserCreateResponseDTO userResponse;

    @BeforeEach
    void setUp() {
        expectedUsername = "foobar";
        expectedEmail = "foobar@gmail.com";
        expectedFullName = "Foo bar";
        expectedSalary = 1405.0;
        expectedUUID = UUID.randomUUID();
        userResponse = new UserCreateResponseDTO(expectedUUID, expectedUsername, expectedEmail);
    }

    @Test
    void testCreate_GivenAValidUserCreateRequestDTO_ShouldReturnAnUserCreateResponseDTOAndOk() throws Exception {
        UserCreateRequestDTO userRequest = new UserCreateRequestDTO(expectedUsername, expectedEmail, "123456", expectedFullName, expectedSalary);
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

        Mockito.verify(userService, Mockito.times(1)).create(Mockito.any(UserCreateRequestDTO.class));
    }

    @Test
    void testFindAll_GivenAnAscPageable_ShouldReturnAPageWithUserCreateResponseDTOAndOk() throws Exception {
        UserAllResponseDTO userAllResponseDTO = new UserAllResponseDTO(expectedUUID, expectedUsername, expectedEmail);
        UserAllResponseDTO userAllResponseDTO2 = new UserAllResponseDTO(UUID.randomUUID(), "foobar2", "foobar2@gmail.com");
        Page<UserAllResponseDTO> pageWithUsers = new PageImpl<>(List.of(userAllResponseDTO, userAllResponseDTO2));
        Mockito.when(userService.findAll(Mockito.any(Pageable.class))).thenReturn(pageWithUsers);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(USER_BASE_ENDPOINT)
                .queryParam("page", "0")
                .queryParam("size", "5")
                .queryParam("sort", "asc"));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.first").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(expectedUUID.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].userName").value(expectedUsername))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value(expectedEmail));

        Mockito.verify(userService, Mockito.times(1)).findAll(Mockito.any(Pageable.class));
    }

    @Test
    void testFindAll_GivenAnDescPageable_ShouldReturnAPageWithUserCreateResponseDTOAndOk() throws Exception {
        UserAllResponseDTO userAllResponseDTO = new UserAllResponseDTO(UUID.randomUUID(), "foobar2", "foobar2@gmail.com");
        UserAllResponseDTO userAllResponseDTO2 = new UserAllResponseDTO(expectedUUID, expectedUsername, expectedEmail);
        Page<UserAllResponseDTO> pageWithUsers = new PageImpl<>(List.of(userAllResponseDTO, userAllResponseDTO2));
        Mockito.when(userService.findAll(Mockito.any(Pageable.class))).thenReturn(pageWithUsers);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(USER_BASE_ENDPOINT)
                .queryParam("page", "0")
                .queryParam("size", "5")
                .queryParam("sort", "desc"));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.first").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(expectedUUID.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].userName").value(expectedUsername))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].email").value(expectedEmail));

        Mockito.verify(userService, Mockito.times(1)).findAll(Mockito.any(Pageable.class));
    }

    @Test
    void testFindAll_GivenAPageableWithInvalidPageParam_ShouldReturnAResponseEntityException() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(USER_BASE_ENDPOINT)
                .queryParam("page", "-1")
                .queryParam("size", "5")
                .queryParam("sort", "desc"));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Page index must not be less than zero"));

        Mockito.verify(userService, Mockito.never()).findAll(Mockito.any(Pageable.class));
    }

    @Test
    void testFindAll_GivenAPageableWithInvalidSizeParam_ShouldReturnAResponseEntityException() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(USER_BASE_ENDPOINT)
                .queryParam("page", "0")
                .queryParam("size", "-1")
                .queryParam("sort", "desc"));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Page size must not be less than one"));

        Mockito.verify(userService, Mockito.never()).findAll(Mockito.any(Pageable.class));
    }

    @Test
    void testFindById_GivenAValidId_ShouldReturnUserCompleteResponseDTO() throws Exception {
        UserCompleteResponseDTO user = new UserCompleteResponseDTO(
                expectedUUID,
                expectedUsername,
                expectedEmail,
                expectedFullName,
                expectedSalary,
                List.of(),
                List.of(),
                List.of());
        Mockito.when(userService.findById(Mockito.any(UUID.class))).thenReturn(user);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(USER_BASE_ENDPOINT + "/" + expectedUUID));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedUUID.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value(expectedUsername))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(expectedEmail))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value(expectedFullName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(expectedSalary));

    }
}
