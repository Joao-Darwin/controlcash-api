package com.controlcash.app.dtos.goal.response;

import java.time.LocalDate;
import java.util.UUID;

public record GoalSimpleResponseDTO(UUID id, LocalDate dueDate, Double value) {
}
