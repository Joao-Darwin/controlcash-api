package com.controlcash.app.controllers.category.impl;

import com.controlcash.app.dtos.category.request.CategoryRequestDTO;
import com.controlcash.app.dtos.category.response.CategoryResponseDTO;
import com.controlcash.app.services.CategoryService;
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

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    private CategoryRequestDTO categoryRequestDTO;
    private CategoryResponseDTO categoryResponseDTO;

    @BeforeEach
    void setUp() {
        categoryRequestDTO = new CategoryRequestDTO("Electronics", List.of(), List.of());
        categoryResponseDTO = new CategoryResponseDTO(UUID.randomUUID(), "Electronics");
    }

    @Test
    void testCreate_GivenACategoryRequestDTOOnBody_ShouldReturnCategoryResponseDTO() throws Exception {
        Mockito.when(categoryService.create(Mockito.any(CategoryRequestDTO.class))).thenReturn(categoryResponseDTO);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDTO)));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Electronics"));
    }

    @Test
    void testFindAll_GivenRequestParamsToPagination_ShouldReturnAPageWithCategoryResponseDTOList() throws Exception {
        CategoryResponseDTO category1 = new CategoryResponseDTO(UUID.randomUUID(), "Electronics");
        CategoryResponseDTO category2 = new CategoryResponseDTO(UUID.randomUUID(), "House");
        Page<CategoryResponseDTO> page = new PageImpl<>(List.of(category1, category2));
        Mockito.when(categoryService.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/categories")
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Electronics"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("House"));
    }

    @Test
    void testFindAll_GivenNoParams_ShouldReturnADefaultPageWithCategoryResponseDTOList() throws Exception {
        CategoryResponseDTO category1 = new CategoryResponseDTO(UUID.randomUUID(), "Electronics");
        CategoryResponseDTO category2 = new CategoryResponseDTO(UUID.randomUUID(), "House");
        Page<CategoryResponseDTO> page = new PageImpl<>(List.of(category1, category2));
        Mockito.when(categoryService.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/categories"));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.first").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Electronics"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("House"));
    }
}
