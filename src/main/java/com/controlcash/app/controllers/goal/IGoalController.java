package com.controlcash.app.controllers.goal;

import com.controlcash.app.dtos.goal.request.GoalCreateRequestDTO;
import com.controlcash.app.dtos.goal.response.GoalCompleteResponseDTO;
import org.springframework.http.ResponseEntity;

public interface IGoalController {

    ResponseEntity<GoalCompleteResponseDTO> create(GoalCreateRequestDTO goalCreateRequestDTO);
    ResponseEntity<?> findAll(int page, int size, String sort);
}
