package com.controlcash.app.controllers.goal;

import com.controlcash.app.dtos.goal.request.GoalCreateRequestDTO;
import com.controlcash.app.dtos.goal.request.GoalUpdateRequestDTO;
import com.controlcash.app.dtos.goal.response.GoalCompleteResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IGoalController {

    ResponseEntity<GoalCompleteResponseDTO> create(GoalCreateRequestDTO goalCreateRequestDTO);
    ResponseEntity<?> findAll(int page, int size, String sort);
    ResponseEntity<?> findById(UUID id);
    ResponseEntity<?> update(GoalUpdateRequestDTO goalUpdateRequestDTO, UUID id);
    ResponseEntity<?> delete(UUID id);
}
