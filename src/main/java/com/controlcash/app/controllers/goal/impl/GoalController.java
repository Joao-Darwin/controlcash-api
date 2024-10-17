package com.controlcash.app.controllers.goal.impl;

import com.controlcash.app.controllers.goal.IGoalController;
import com.controlcash.app.dtos.goal.request.GoalCreateRequestDTO;
import com.controlcash.app.dtos.goal.request.GoalUpdateRequestDTO;
import com.controlcash.app.dtos.goal.response.GoalCompleteResponseDTO;
import com.controlcash.app.dtos.goal.response.GoalSimpleResponseDTO;
import com.controlcash.app.exceptions.GoalNotFoundException;
import com.controlcash.app.exceptions.ResponseEntityException;
import com.controlcash.app.services.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        try {
            GoalCompleteResponseDTO goalCompleteResponseDTO = goalService.findById(id);

            return ResponseEntity.status(HttpStatus.OK).body(goalCompleteResponseDTO);
        } catch (GoalNotFoundException goalNotFoundException) {
            ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), goalNotFoundException.getMessage(), "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityException);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody GoalUpdateRequestDTO goalUpdateRequestDTO, @PathVariable UUID id) {
        try {
            GoalCompleteResponseDTO goalCompleteResponseDTO = goalService.update(goalUpdateRequestDTO, id);

            return ResponseEntity.status(HttpStatus.OK).body(goalCompleteResponseDTO);
        } catch (GoalNotFoundException goalNotFoundException) {
            ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), goalNotFoundException.getMessage(), "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityException);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            goalService.delete(id);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (GoalNotFoundException goalNotFoundException) {
            ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), goalNotFoundException.getMessage(), "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityException);
        }
    }
}
