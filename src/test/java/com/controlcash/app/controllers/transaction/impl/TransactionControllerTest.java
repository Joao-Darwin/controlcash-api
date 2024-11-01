package com.controlcash.app.controllers.transaction.impl;

import com.controlcash.app.dtos.transaction.request.TransactionCreateRequestDTO;
import com.controlcash.app.dtos.transaction.response.TransactionCreateResponseDTO;
import com.controlcash.app.models.Category;
import com.controlcash.app.models.Permission;
import com.controlcash.app.models.User;
import com.controlcash.app.models.enums.TransactionType;
import com.controlcash.app.services.TransactionService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TransactionControllerTest {

    private static final String TRANSACTION_BASE_ENDPOINT = "/api/transactions";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        Permission permission = new Permission(UUID.randomUUID(), "admin", List.of());
        user = new User(
                UUID.randomUUID(),
                "foobar",
                "foobar@gmail.com",
                "123456",
                "Foo Bar",
                2500.00,
                true,
                true,
                true,
                true,
                List.of(permission),
                List.of(),
                List.of());

        category = new Category(UUID.randomUUID(), "Home", List.of(), List.of());
    }

    @Test
    void testCreate_GivenAnTransactionCreateRequestDTO_ShouldReturnTransactionCreateResponseDTOAndOk() throws Exception {
        UUID expectedId = UUID.randomUUID();
        String expectedName = "Books";
        String expectedDescription = "Books that i buy on Amazon";
        LocalDate expectedCreatedDate = LocalDate.now();
        TransactionCreateRequestDTO transactionCreateRequestDTO = new TransactionCreateRequestDTO(
                expectedName,
                expectedDescription,
                1400.0,
                1,
                TransactionType.PAYMENT,
                user,
                List.of(category)
        );
        TransactionCreateResponseDTO transactionCreateResponseDTO = new TransactionCreateResponseDTO(
                expectedId,
                expectedName,
                expectedDescription,
                expectedCreatedDate);
        Mockito.when(transactionService.create(Mockito.any(TransactionCreateRequestDTO.class))).thenReturn(transactionCreateResponseDTO);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(TRANSACTION_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionCreateRequestDTO)));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedDescription))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdDate").value(expectedCreatedDate.toString()));

    }

    @Test
    void testFindAll_GivenAPageable_ShouldReturnAPageWithTransactionCreateResponseDTOAndOk() throws Exception {
        UUID expectedId = UUID.randomUUID();
        String expectedName = "Books";
        String expectedDescription = "Books that i buy on Amazon";
        LocalDate expectedCreatedDate = LocalDate.now();
        TransactionCreateResponseDTO transactionCreateResponseDTO = new TransactionCreateResponseDTO(
                expectedId,
                expectedName,
                expectedDescription,
                expectedCreatedDate);
        TransactionCreateResponseDTO transactionCreateResponseDTO2 = new TransactionCreateResponseDTO(
                UUID.randomUUID(),
                "Foo",
                "Foo Bar",
                LocalDate.now());
        Page<TransactionCreateResponseDTO> expectedPage = new PageImpl<>(List.of(transactionCreateResponseDTO, transactionCreateResponseDTO2));
        Mockito.when(transactionService.findAll(Mockito.any(Pageable.class))).thenReturn(expectedPage);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(TRANSACTION_BASE_ENDPOINT)
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value(expectedName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value(expectedDescription))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].createdDate").value(expectedCreatedDate.toString()));
    }

    @Test
    void testFindAll_GivenAPageableWithDescSort_ShouldReturnAPageWithTransactionCreateResponseDTOAndOk() throws Exception {
        UUID expectedId = UUID.randomUUID();
        String expectedName = "Books";
        String expectedDescription = "Books that i buy on Amazon";
        LocalDate expectedCreatedDate = LocalDate.now();
        TransactionCreateResponseDTO transactionCreateResponseDTO2 = new TransactionCreateResponseDTO(
                expectedId,
                expectedName,
                expectedDescription,
                expectedCreatedDate);
        TransactionCreateResponseDTO transactionCreateResponseDTO = new TransactionCreateResponseDTO(
                UUID.randomUUID(),
                "Foo",
                "Foo Bar",
                LocalDate.now());
        Page<TransactionCreateResponseDTO> expectedPage = new PageImpl<>(List.of(transactionCreateResponseDTO, transactionCreateResponseDTO2));
        Mockito.when(transactionService.findAll(Mockito.any(Pageable.class))).thenReturn(expectedPage);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(TRANSACTION_BASE_ENDPOINT)
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value(expectedName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].description").value(expectedDescription))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].createdDate").value(expectedCreatedDate.toString()));
    }
}