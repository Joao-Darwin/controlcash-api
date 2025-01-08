package com.controlcash.app.controllers.category.impl;

import com.controlcash.app.dtos.category.request.CategoryRequestDTO;
import com.controlcash.app.dtos.category.response.CategoryResponseDTO;
import com.controlcash.app.exceptions.CategoryNotFoundException;
import com.controlcash.app.security.jwt.JwtTokenFilter;
import com.controlcash.app.security.jwt.JwtTokenProvider;
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

@WebMvcTest(controllers = {CategoryController.class})
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class CategoryControllerTest {

    private static final String CATEGORY_BASE_ENDPOINT = "/api/categories";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private JwtTokenFilter jwtTokenFilter;

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

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(CATEGORY_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDTO)));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Electronics"));
    }

    @Test
    void testCreate_WhenIsThrowsAnException_ShouldReturnABadRequest() throws Exception {
        Mockito.when(categoryService.create(Mockito.any(CategoryRequestDTO.class))).thenThrow(new RuntimeException("Exception message"));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(CATEGORY_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDTO)));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @Test
    void testFindAll_GivenRequestParamsToPagination_ShouldReturnAPageWithCategoryResponseDTOList() throws Exception {
        CategoryResponseDTO category2 = new CategoryResponseDTO(UUID.randomUUID(), "House");
        Page<CategoryResponseDTO> page = new PageImpl<>(List.of(categoryResponseDTO, category2));
        Mockito.when(categoryService.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(CATEGORY_BASE_ENDPOINT)
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
    void testFindAll_GivenRequestParamsToPaginationWithSortDesc_ShouldReturnAPageWithCategoryResponseDTOList() throws Exception {
        CategoryResponseDTO category1 = new CategoryResponseDTO(UUID.randomUUID(), "House");
        Page<CategoryResponseDTO> page = new PageImpl<>(List.of(category1, categoryResponseDTO));
        Mockito.when(categoryService.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(CATEGORY_BASE_ENDPOINT)
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("House"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("Electronics"));
    }

    @Test
    void testFindAll_GivenNoParams_ShouldReturnADefaultPageWithCategoryResponseDTOList() throws Exception {
        CategoryResponseDTO category2 = new CategoryResponseDTO(UUID.randomUUID(), "House");
        Page<CategoryResponseDTO> page = new PageImpl<>(List.of(categoryResponseDTO, category2));
        Mockito.when(categoryService.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(CATEGORY_BASE_ENDPOINT));

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
    void testFindById_GivenAValidId_ShouldReturnACategoryResponseDTO() throws Exception {
        Mockito.when(categoryService.findById(Mockito.any(UUID.class))).thenReturn(categoryResponseDTO);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(CATEGORY_BASE_ENDPOINT + "/" + UUID.randomUUID()));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Electronics"));
    }

    @Test
    void testFindById_GivenAnInvalidId_ShouldReturnABadRequestError() throws Exception {
        UUID id = UUID.randomUUID();
        String exceptionMessage = "Category not found. Id used: " + id;
        Mockito.when(categoryService.findById(Mockito.any(UUID.class))).thenThrow(new CategoryNotFoundException(exceptionMessage));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(CATEGORY_BASE_ENDPOINT + "/" + id));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(exceptionMessage));
    }

    @Test
    void testUpdate_GivenAValidIdAndACategoryRequestDTO_ShouldReturnAOkAndACategoryResponseDTOUpdated() throws Exception {
        UUID id = UUID.randomUUID();
        categoryRequestDTO = new CategoryRequestDTO("Games", List.of(), List.of());
        categoryResponseDTO = new CategoryResponseDTO(id, "Games");
        Mockito.when(categoryService.update(Mockito.any(CategoryRequestDTO.class), Mockito.any(UUID.class))).thenReturn(categoryResponseDTO);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put(CATEGORY_BASE_ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDTO)));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(categoryRequestDTO.name()));
    }

    @Test
    void testUpdate_GivenAnInvalidId_ShouldReturnABadRequestError() throws Exception {
        UUID id = UUID.randomUUID();
        String exceptionMessage = "Category not found. Id used: " + id;
        categoryRequestDTO = new CategoryRequestDTO("Games", List.of(), List.of());
        Mockito.when(categoryService.update(Mockito.any(CategoryRequestDTO.class), Mockito.any(UUID.class))).thenThrow(new CategoryNotFoundException(exceptionMessage));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put(CATEGORY_BASE_ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDTO)));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(exceptionMessage));
    }

    @Test
    void testDelete_GivenAValidId_ShouldReturnAnOk() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(categoryService).delete(Mockito.any(UUID.class));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete(CATEGORY_BASE_ENDPOINT + "/" + id));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void testDelete_GivenAnInvalidId_ShouldReturnAnBadRequest() throws Exception {
        UUID id = UUID.randomUUID();
        String exceptionMessage = "Category not found. Id used: " + id;
        Mockito.doThrow(new CategoryNotFoundException(exceptionMessage)).when(categoryService).delete(Mockito.any(UUID.class));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete(CATEGORY_BASE_ENDPOINT + "/" + id));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(exceptionMessage));
    }
}
