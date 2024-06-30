package com.controlcash.app.utils.converters;

import com.controlcash.app.dtos.goal.request.GoalCreateRequestDTO;
import com.controlcash.app.dtos.goal.response.GoalCompleteResponseDTO;
import com.controlcash.app.dtos.goal.response.GoalSimpleResponseDTO;
import com.controlcash.app.models.Goal;

public class GoalConverter {

    public static Goal convertGoalCreateRequestDTOToGoal(GoalCreateRequestDTO goalCreateRequestDTO) {
        Goal goal = new Goal();

        goal.setDueDate(goalCreateRequestDTO.dueDate());
        goal.setValue(goalCreateRequestDTO.value());
        goal.setUser(goalCreateRequestDTO.user());
        goal.setCategory(goalCreateRequestDTO.category());

        return goal;
    }

    public static GoalCompleteResponseDTO convertGoalToGoalCompleteResponseDTO(Goal goal) {
        return new GoalCompleteResponseDTO(goal.getId(), goal.getDueDate(), goal.getValue(), goal.getUser(), goal.getCategory());
    }

    public static GoalSimpleResponseDTO convertGoalToGoalSimpleResponseDTO(Goal goal) {
        return new GoalSimpleResponseDTO(goal.getId(), goal.getDueDate(), goal.getValue());
    }
}
