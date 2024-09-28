package com.controlcash.app.services;

import com.controlcash.app.dtos.goal.request.GoalCreateRequestDTO;
import com.controlcash.app.dtos.goal.request.GoalUpdateRequestDTO;
import com.controlcash.app.dtos.goal.response.GoalCompleteResponseDTO;
import com.controlcash.app.dtos.goal.response.GoalSimpleResponseDTO;
import com.controlcash.app.exceptions.GoalNotFoundException;
import com.controlcash.app.models.Category;
import com.controlcash.app.models.Goal;
import com.controlcash.app.models.User;
import com.controlcash.app.repositories.GoalRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private GoalService goalService;

    private Goal goal;
    private GoalCreateRequestDTO goalCreateRequestDTO;
    private String goalNotFoundExceptionMessage;
    private UUID id;

    @BeforeEach
    void setUp() {
        LocalDate dueDate = LocalDate.parse("2024-08-01");

        id = UUID.randomUUID();
        goal = new Goal(id, dueDate, 2500.0, new User(), new Category());
        goalCreateRequestDTO = new GoalCreateRequestDTO(dueDate, 2500.0, new User(), new Category());

        goalNotFoundExceptionMessage = "Goal not found. Id used: " + id;
    }

    @Test
    void testCreate_GivenAGoalCreateRequestDTO_ShouldSaveAndReturnAGoalCompleteResponseDTO() {
        Mockito.when(goalRepository.save(Mockito.any(Goal.class))).thenReturn(goal);

        GoalCompleteResponseDTO actualGoalCompleteResponseDTO = goalService.create(goalCreateRequestDTO);

        Assertions.assertNotNull(actualGoalCompleteResponseDTO);
        Assertions.assertEquals(id, actualGoalCompleteResponseDTO.id());
        Assertions.assertEquals(goalCreateRequestDTO.dueDate(), actualGoalCompleteResponseDTO.dueDate());
        Assertions.assertEquals(goalCreateRequestDTO.value(), actualGoalCompleteResponseDTO.value());
    }

    @Test
    void testFindAll_GivenAPageable_ShouldReturnAGoalSimpleResponseDTOPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dueDate"));
        LocalDate dueDate = LocalDate.parse("2024-09-01");
        Goal goal2 = new Goal(UUID.randomUUID(), dueDate, 2400.0, new User(), new Category());
        List<Goal> goalList = List.of(goal, goal2);
        Page<Goal> goalPage = new PageImpl<>(goalList);
        Mockito.when(goalRepository.findAll(Mockito.any(Pageable.class))).thenReturn(goalPage);

        Page<GoalSimpleResponseDTO> actualGoalSimpleResponseDTOPage = goalService.findAll(pageable);

        Assertions.assertNotNull(actualGoalSimpleResponseDTOPage);
        Assertions.assertEquals(2, actualGoalSimpleResponseDTOPage.getSize());
    }

    @Test
    void testFindAll_GivenAPageableWithNotExistentPageNumber_ShouldReturnAnEmptyPage() {
        Pageable pageable = PageRequest.of(10, 10, Sort.by(Sort.Direction.ASC, "dueDate"));
        Page<Goal> goalPage = Page.empty();
        Mockito.when(goalRepository.findAll(Mockito.any(Pageable.class))).thenReturn(goalPage);

        Page<GoalSimpleResponseDTO> actualGoalSimpleResponseDTOPage = goalService.findAll(pageable);

        Assertions.assertNotNull(actualGoalSimpleResponseDTOPage);
        Assertions.assertTrue(actualGoalSimpleResponseDTOPage.isEmpty());
    }

    @Test
    void testFindById_GivenAValidId_ShouldReturnAGoalCompleteResponseDTO() {
        Mockito.when(goalRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(goal));

        GoalCompleteResponseDTO actualGoalCompleteResponseDTO = goalService.findById(id);

        Assertions.assertNotNull(actualGoalCompleteResponseDTO);
        Assertions.assertEquals(id, actualGoalCompleteResponseDTO.id());
        Assertions.assertEquals(goal.getDueDate(), actualGoalCompleteResponseDTO.dueDate());
        Assertions.assertEquals(goal.getValue(), actualGoalCompleteResponseDTO.value());
    }

    @Test
    void testFindById_GivenANotValidId_ShouldThrowsAGoalNotFoundException() {
        Mockito.when(goalRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());

        GoalNotFoundException goalNotFoundException = Assertions.assertThrows(GoalNotFoundException.class, () -> goalService.findById(id));

        Assertions.assertEquals(goalNotFoundExceptionMessage, goalNotFoundException.getMessage());
    }

    @Test
    void testUpdate_GivenAGoalCreateRequestDTOAndValidId_ShouldReturnAGoalCompleteResponseDTOUpdated() {
        Mockito.when(goalRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(goal));
        LocalDate newDueDate = LocalDate.parse("2025-08-04");
        Double newValue = 2900.0;
        GoalUpdateRequestDTO goalUpdateRequestDTO = new GoalUpdateRequestDTO(newDueDate, newValue, new Category());
        Goal goalUpdated = new Goal(id, newDueDate, newValue, goal.getUser(), goalUpdateRequestDTO.category());
        Mockito.when(goalRepository.save(Mockito.any(Goal.class))).thenReturn(goalUpdated);

        GoalCompleteResponseDTO goalCompleteResponseDTO = goalService.update(goalUpdateRequestDTO, id);

        Assertions.assertNotNull(goalCompleteResponseDTO);
        Assertions.assertEquals(goalUpdateRequestDTO.dueDate(), goalCompleteResponseDTO.dueDate());
        Assertions.assertEquals(goalUpdateRequestDTO.value(), goalCompleteResponseDTO.value());
        Assertions.assertEquals(goalUpdateRequestDTO.category(), goalCompleteResponseDTO.category());
        Assertions.assertEquals(goal.getUser(), goalCompleteResponseDTO.user());
    }

    @Test
    void testUpdate_GivenANotValidId_ShouldThrowsAGoalNotFoundException() {
        LocalDate newDueDate = LocalDate.parse("2025-08-04");
        Double newValue = 2900.0;
        GoalUpdateRequestDTO goalUpdateRequestDTO = new GoalUpdateRequestDTO(newDueDate, newValue, new Category());
        Mockito.when(goalRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());

        GoalNotFoundException goalNotFoundException = Assertions.assertThrows(GoalNotFoundException.class, () -> goalService.update(goalUpdateRequestDTO, id));

        Assertions.assertEquals(goalNotFoundExceptionMessage, goalNotFoundException.getMessage());
    }

    @Test
    void testDelete_GivenAValidId_ShouldDeleteGoal() {
        Mockito.when(goalRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(goal));
        Mockito.doNothing().when(goalRepository).delete(Mockito.any(Goal.class));

        Assertions.assertDoesNotThrow(() -> goalService.delete(id));

        Mockito.verify(goalRepository, Mockito.times(1)).delete(Mockito.any(Goal.class));
    }

    @Test
    void testDelete_GivenANotValidId_ShouldThrowsAGoalNotFoundException() {
        Mockito.when(goalRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());

        GoalNotFoundException goalNotFoundException = Assertions.assertThrows(GoalNotFoundException.class, () -> goalService.delete(id));

        Assertions.assertEquals(goalNotFoundExceptionMessage, goalNotFoundException.getMessage());
        Mockito.verify(goalRepository, Mockito.never()).delete(Mockito.any(Goal.class));
    }
}
