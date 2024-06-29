package com.controlcash.app.utils.converters;

import com.controlcash.app.dtos.goal.request.GoalCreateRequestDTO;
import com.controlcash.app.models.Category;
import com.controlcash.app.models.Goal;
import com.controlcash.app.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.Instant;
import java.util.Date;

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
}
