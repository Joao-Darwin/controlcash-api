package com.controlcash.app.services;

import com.controlcash.app.dtos.goal.request.GoalCreateRequestDTO;
import com.controlcash.app.dtos.goal.response.GoalCompleteResponseDTO;
import com.controlcash.app.models.Goal;
import com.controlcash.app.repositories.GoalRepository;
import com.controlcash.app.utils.converters.GoalConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoalService {

    private final GoalRepository goalRepository;

    @Autowired
    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public GoalCompleteResponseDTO create(GoalCreateRequestDTO goalRequestDTO) {
        Goal goal = GoalConverter.convertGoalCreateRequestDTOToGoal(goalRequestDTO);

        goal = goalRepository.save(goal);

        return GoalConverter.convertGoalToGoalCompleteResponseDTO(goal);
    }
}
