package com.controlcash.app.controllers.goal.impl;

import com.controlcash.app.controllers.goal.IGoalController;
import com.controlcash.app.dtos.goal.request.GoalCreateRequestDTO;
import com.controlcash.app.dtos.goal.response.GoalCompleteResponseDTO;
import com.controlcash.app.dtos.goal.response.GoalSimpleResponseDTO;
import com.controlcash.app.services.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "asc") String sort) {

        Sort.Direction sortDirection = sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "dueDate"));

        Page<GoalSimpleResponseDTO> goalSimpleResponseDTOPage = goalService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(goalSimpleResponseDTOPage);
    }
}
