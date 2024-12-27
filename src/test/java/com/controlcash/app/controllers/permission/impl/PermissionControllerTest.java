package com.controlcash.app.controllers.permission.impl;

import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import com.controlcash.app.dtos.permission.response.AllPermissionResponseDTO;
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

        Mockito.verify(permissionService, Mockito.times(1)).create(Mockito.any(PermissionCreateRequestDTO.class));
    }

    @Test
    void testCreate_GivenADuplicatePermissionName_ShouldReturnABadRequest() throws Exception {
        String permissionDuplicatedExceptionMessage = "Permission '" + expectedDescription + "' already exist";
        PermissionCreateRequestDTO permissionRequest = new PermissionCreateRequestDTO(expectedDescription);
        Mockito
                .when(permissionService.create(Mockito.any(PermissionCreateRequestDTO.class)))
                .thenThrow(new IllegalArgumentException(permissionDuplicatedExceptionMessage));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(PERMISSION_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(permissionRequest)));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.moment").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.details").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(permissionDuplicatedExceptionMessage));

        Mockito.verify(permissionService, Mockito.times(1)).create(Mockito.any(PermissionCreateRequestDTO.class));
    }

    @Test
    void testFindAll_GivenAnAscPageable_ShouldReturnAPageWithAllPermissionResponseDTOAndOk() throws Exception {
        AllPermissionResponseDTO allPermissionResponseDTO =
                new AllPermissionResponseDTO(expectedId, expectedDescription);
        AllPermissionResponseDTO allPermissionResponseDTO2 =
                new AllPermissionResponseDTO(UUID.randomUUID(), "");
        Page<AllPermissionResponseDTO> allPermissionResponseDTOPage =
                new PageImpl<>(List.of(allPermissionResponseDTO, allPermissionResponseDTO2));
        Mockito.when(permissionService.findAll(Mockito.any(Pageable.class))).thenReturn(allPermissionResponseDTOPage);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(PERMISSION_BASE_ENDPOINT)
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(expectedId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value(expectedDescription));

        Mockito.verify(permissionService, Mockito.times(1)).findAll(Mockito.any(Pageable.class));
    }

    @Test
    void testFindAll_GivenAnDescPageable_ShouldReturnAPageWithAllPermissionResponseDTOAndOk() throws Exception {
        AllPermissionResponseDTO allPermissionResponseDTO =
                new AllPermissionResponseDTO(UUID.randomUUID(), "");
        AllPermissionResponseDTO allPermissionResponseDTO2 =
                new AllPermissionResponseDTO(expectedId, expectedDescription);
        Page<AllPermissionResponseDTO> allPermissionResponseDTOPage =
                new PageImpl<>(List.of(allPermissionResponseDTO, allPermissionResponseDTO2));
        Mockito.when(permissionService.findAll(Mockito.any(Pageable.class))).thenReturn(allPermissionResponseDTOPage);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(PERMISSION_BASE_ENDPOINT)
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(expectedId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].description").value(expectedDescription));

        Mockito.verify(permissionService, Mockito.times(1)).findAll(Mockito.any(Pageable.class));
    }
}
