package com.controlcash.app.controllers.goal.impl;

import com.controlcash.app.dtos.goal.request.GoalCreateRequestDTO;
import com.controlcash.app.dtos.goal.request.GoalUpdateRequestDTO;
import com.controlcash.app.dtos.goal.response.GoalCompleteResponseDTO;
import com.controlcash.app.dtos.goal.response.GoalSimpleResponseDTO;
import com.controlcash.app.exceptions.GoalNotFoundException;
import com.controlcash.app.models.Category;
import com.controlcash.app.models.User;
import com.controlcash.app.services.GoalService;
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

@WebMvcTest(controllers = {GoalController.class})
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class GoalControllerTest {

    private static final String GOAL_BASE_ENDPOINT = "/api/goals";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GoalService goalService;

    private UUID id;
    private GoalCreateRequestDTO goalCreateRequestDTO;
    private GoalCompleteResponseDTO goalCompleteResponseDTO;
    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
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
                List.of(),
                List.of(),
                List.of());

        category = new Category(UUID.randomUUID(), "Credit card", List.of(), List.of());

        goalCreateRequestDTO = new GoalCreateRequestDTO(LocalDate.parse("2024-08-01"), 1200.0, user, category);
        id = UUID.randomUUID();
        goalCompleteResponseDTO = new GoalCompleteResponseDTO(id, LocalDate.parse("2024-08-01"), 1200.0, user, category);
    }

    @Test
    void testCreate_GivenAGoalCreateRequestDTO_ShouldReturnAGoalCompleteResponseDTOAndOk() throws Exception {
        Mockito.when(goalService.create(Mockito.any(GoalCreateRequestDTO.class))).thenReturn(goalCompleteResponseDTO);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(GOAL_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalCreateRequestDTO)));

        Mockito.verify(goalService, Mockito.times(1)).create(Mockito.any());

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(goalCreateRequestDTO.dueDate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.value").value(goalCreateRequestDTO.value()));
    }

    @Test
    void testFindAll_GivenAPageable_ShouldReturnAPageWithGoalSimpleResponseDTO() throws Exception {
        GoalSimpleResponseDTO goalSimpleResponseDTO = new GoalSimpleResponseDTO(id, LocalDate.now(), 1500.0);
        GoalSimpleResponseDTO goalSimpleResponseDTO2 = new GoalSimpleResponseDTO(id, LocalDate.now(), 1700.0);
        Page<GoalSimpleResponseDTO> goalSimpleResponseDTOPage = new PageImpl<>(List.of(goalSimpleResponseDTO, goalSimpleResponseDTO2));
        Mockito.when(goalService.findAll(Mockito.any(Pageable.class))).thenReturn(goalSimpleResponseDTOPage);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(GOAL_BASE_ENDPOINT)
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].value").value("1500.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].value").value("1700.0"));
    }

    @Test
    void testFindAll_GivenRequestParamsToPaginationWithSortDesc_ShouldReturnAPageWithGoalSimpleResponseDTO() throws Exception {
        GoalSimpleResponseDTO goalSimpleResponseDTO = new GoalSimpleResponseDTO(id, LocalDate.now(), 1700.0);
        GoalSimpleResponseDTO goalSimpleResponseDTO2 = new GoalSimpleResponseDTO(id, LocalDate.now(), 1500.0);
        Page<GoalSimpleResponseDTO> goalSimpleResponseDTOPage = new PageImpl<>(List.of(goalSimpleResponseDTO, goalSimpleResponseDTO2));
        Mockito.when(goalService.findAll(Mockito.any(Pageable.class))).thenReturn(goalSimpleResponseDTOPage);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(GOAL_BASE_ENDPOINT)
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].value").value("1700.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].value").value("1500.0"));
    }

    @Test
    void testFindById_GivenAValidId_ShouldReturnAGoalCompleteResponseDTOAndOk() throws Exception {
        Mockito.when(goalService.findById(Mockito.any(UUID.class))).thenReturn(goalCompleteResponseDTO);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(GOAL_BASE_ENDPOINT + "/" + id));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(goalCompleteResponseDTO.dueDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.value").value(goalCompleteResponseDTO.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(goalCompleteResponseDTO.user().getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category.name").value(goalCompleteResponseDTO.category().getName()));

        Mockito.verify(goalService, Mockito.times(1)).findById(Mockito.any(UUID.class));
    }

    @Test
    void testFindById_GivenANotValidId_ShouldReturnBadRequest() throws Exception {
        String expectedMessageException = "Goal not found. Id used: " + id;
        Mockito.when(goalService.findById(Mockito.any(UUID.class))).thenThrow(new GoalNotFoundException(expectedMessageException));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(GOAL_BASE_ENDPOINT + "/" + id));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedMessageException));

        Mockito.verify(goalService, Mockito.times(1)).findById(Mockito.any(UUID.class));
    }

    @Test
    void testUpdate_GivenAValidIdAndGoalUpdateRequestDTO_ShouldReturnAGoalCompleteResponseDTOAndOk() throws Exception {
        category = new Category(UUID.randomUUID(), "Invest", List.of(), List.of());
        GoalUpdateRequestDTO goalUpdateRequestDTO = new GoalUpdateRequestDTO(LocalDate.parse("2024-08-02"), 4200.0, category);
        goalCompleteResponseDTO = new GoalCompleteResponseDTO(id, LocalDate.parse("2024-08-02"), 4200.0, user, category);
        Mockito.when(goalService.update(Mockito.any(GoalUpdateRequestDTO.class), Mockito.any(UUID.class)))
                .thenReturn(goalCompleteResponseDTO);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put(GOAL_BASE_ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalUpdateRequestDTO)));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(goalCompleteResponseDTO.dueDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.value").value(goalCompleteResponseDTO.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(goalCompleteResponseDTO.user().getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category.name").value(goalCompleteResponseDTO.category().getName()));

        Mockito.verify(goalService, Mockito.times(1))
                .update(Mockito.any(GoalUpdateRequestDTO.class), Mockito.any(UUID.class));
    }

    @Test
    void testUpdate_GivenANotValidId_ShouldReturnABadRequest() throws Exception {
        category = new Category(UUID.randomUUID(), "Invest", List.of(), List.of());
        GoalUpdateRequestDTO goalUpdateRequestDTO = new GoalUpdateRequestDTO(LocalDate.parse("2024-08-02"), 4200.0, category);
        String expectedMessageException = "Goal not found. Id used: " + id;
        Mockito.when(goalService.update(Mockito.any(GoalUpdateRequestDTO.class), Mockito.any(UUID.class)))
                .thenThrow(new GoalNotFoundException(expectedMessageException));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put(GOAL_BASE_ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalUpdateRequestDTO)));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedMessageException));

        Mockito.verify(goalService, Mockito.times(1))
                .update(Mockito.any(GoalUpdateRequestDTO.class), Mockito.any(UUID.class));
    }

    @Test
    void testDelete_GivenAValidId_ShouldReturnOk() throws Exception {
        Mockito.doNothing().when(goalService).delete(Mockito.any(UUID.class));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete(GOAL_BASE_ENDPOINT + "/" + id));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }
}
