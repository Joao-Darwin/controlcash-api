package com.controlcash.app.services;

import com.controlcash.app.dtos.goal.request.GoalCreateRequestDTO;
import com.controlcash.app.dtos.goal.response.GoalCompleteResponseDTO;
import com.controlcash.app.models.Category;
import com.controlcash.app.models.Goal;
import com.controlcash.app.models.User;
import com.controlcash.app.repositories.GoalRepository;
import com.controlcash.app.utils.dates.DateFormatUtils;
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

import java.text.ParseException;
import java.util.Date;
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
    private UUID id;

    @BeforeEach
    void setUp() throws ParseException {
        DateFormatUtils dateFormatUtils = DateFormatUtils.getInstance();
        Date createdDate = dateFormatUtils.convertStringToDate("01/08/2024");

        id = UUID.randomUUID();
        goal = new Goal(id, createdDate, 2500.0, new User(), new Category());
        goalCreateRequestDTO = new GoalCreateRequestDTO(createdDate, 2500.0, new User(), new Category());
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
}
