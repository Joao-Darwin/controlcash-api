package com.controlcash.app.utils.converters;

import com.controlcash.app.dtos.goal.request.GoalCreateRequestDTO;
import com.controlcash.app.dtos.goal.response.GoalCompleteResponseDTO;
import com.controlcash.app.models.Category;
import com.controlcash.app.models.Goal;
import com.controlcash.app.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GoalConverterTest {

    @Test
    public void testConvertGoalCreateRequestDTOToGoal_ShouldReturnGoal() {
        GoalCreateRequestDTO expectedGoalCreateRequestDTO = new GoalCreateRequestDTO(
                Date.from(Instant.now()),
                1000.0,
                new User(),
                new Category()
        );

        Goal actualGoal = GoalConverter.convertGoalCreateRequestDTOToGoal(expectedGoalCreateRequestDTO);

        Assertions.assertEquals(expectedGoalCreateRequestDTO.dueDate(), actualGoal.getDueDate());
        Assertions.assertEquals(expectedGoalCreateRequestDTO.value(), actualGoal.getValue());
        Assertions.assertEquals(expectedGoalCreateRequestDTO.user(), actualGoal.getUser());
        Assertions.assertEquals(expectedGoalCreateRequestDTO.category(), actualGoal.getCategory());
    }

    @Test
    public void testConvertGoalToGoalCompleteResponseDTO_ShouldReturnGoalCompleteResponseDTO() {
        UUID expectedId = UUID.randomUUID();
        Date expectedDueDate = Date.from(Instant.now());
        Double expectedValue = 1250.00;
        User expectedUser = new User();
        Category expectedCategory = new Category();
        Goal goal = new Goal(expectedId, expectedDueDate, expectedValue, expectedUser, expectedCategory);

        GoalCompleteResponseDTO goalCompleteResponseDTO = GoalConverter.convertGoalToGoalCompleteResponseDTO(goal);

        Assertions.assertEquals(expectedId, goalCompleteResponseDTO.id());
        Assertions.assertEquals(expectedDueDate, goalCompleteResponseDTO.dueDate());
        Assertions.assertEquals(expectedValue, goalCompleteResponseDTO.value());
        Assertions.assertEquals(expectedUser, goalCompleteResponseDTO.user());
        Assertions.assertEquals(expectedCategory, goalCompleteResponseDTO.category());
    }
}
