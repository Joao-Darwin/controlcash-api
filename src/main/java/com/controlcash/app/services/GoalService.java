package com.controlcash.app.services;

import com.controlcash.app.dtos.goal.request.GoalCreateRequestDTO;
import com.controlcash.app.dtos.goal.request.GoalUpdateRequestDTO;
import com.controlcash.app.dtos.goal.response.GoalCompleteResponseDTO;
import com.controlcash.app.dtos.goal.response.GoalSimpleResponseDTO;
import com.controlcash.app.exceptions.GoalNotFoundException;
import com.controlcash.app.models.Goal;
import com.controlcash.app.repositories.GoalRepository;
import com.controlcash.app.utils.converters.GoalConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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

    public Page<GoalSimpleResponseDTO> findAll(Pageable pageable) {
        Page<Goal> goals = goalRepository.findAll(pageable);

        return goals.map(GoalConverter::convertGoalToGoalSimpleResponseDTO);
    }

    public GoalCompleteResponseDTO findById(UUID id) {
        Goal goal = findGoalByIdAndVerifyIfExists(id);

        return GoalConverter.convertGoalToGoalCompleteResponseDTO(goal);
    }

    private Goal findGoalByIdAndVerifyIfExists(UUID id) {
        Optional<Goal> goalOptional = goalRepository.findById(id);
        boolean goalExists = goalOptional.isPresent();

        if (!goalExists) {
            throw new GoalNotFoundException("Goal not found. Id used: " + id);
        }

        return goalOptional.get();
    }

    public GoalCompleteResponseDTO update(GoalUpdateRequestDTO goalUpdateRequestDTO, UUID id) {
        Goal goal = findGoalByIdAndVerifyIfExists(id);

        goal.setCategory(goalUpdateRequestDTO.category());
        goal.setDueDate(goalUpdateRequestDTO.dueDate());
        goal.setValue(goalUpdateRequestDTO.value());

        goal = goalRepository.save(goal);

        return GoalConverter.convertGoalToGoalCompleteResponseDTO(goal);
    }

    public void delete(UUID id) {
        Goal goal = findGoalByIdAndVerifyIfExists(id);

        goalRepository.delete(goal);
    }
}
