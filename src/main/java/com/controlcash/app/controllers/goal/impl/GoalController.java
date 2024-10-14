package com.controlcash.app.controllers.goal.impl;

import com.controlcash.app.controllers.goal.IGoalController;
import com.controlcash.app.dtos.goal.request.GoalCreateRequestDTO;
import com.controlcash.app.dtos.goal.response.GoalCompleteResponseDTO;
import com.controlcash.app.services.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${path-api}/goals")
public class GoalController implements IGoalController {

    private final GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    public ResponseEntity<GoalCompleteResponseDTO> create(@RequestBody GoalCreateRequestDTO goalCreateRequestDTO) {
        GoalCompleteResponseDTO goalCompleteResponseDTO = goalService.create(goalCreateRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(goalCompleteResponseDTO);
    }
}
