package com.controlcash.app.controllers.permission.impl;

import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import com.controlcash.app.dtos.permission.response.PermissionResponseDTO;
import com.controlcash.app.services.PermissionService;
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

@WebMvcTest(controllers = PermissionController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class PermissionControllerTest {
    private static final String PERMISSION_BASE_ENDPOINT = "/api/permissions";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PermissionService permissionService;

    private UUID expectedId;
    private String expectedDescription;
    private PermissionResponseDTO permissionResponse;

    @BeforeEach
    void setUp() {
        expectedId = UUID.randomUUID();
        expectedDescription = "Foo";
        permissionResponse = new PermissionResponseDTO(expectedId, expectedDescription, List.of());
    }

    @Test
    void testCreate_GivenAPermissionCreateRequestDTO_ShouldReturnAPermissionResponseDTOAndOk() throws Exception {
        PermissionCreateRequestDTO permissionRequest = new PermissionCreateRequestDTO(expectedDescription);
        Mockito.when(permissionService.create(Mockito.any(PermissionCreateRequestDTO.class))).thenReturn(permissionResponse);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(PERMISSION_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(permissionRequest)));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedDescription));
    }
}
