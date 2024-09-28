package com.controlcash.app.dtos.goal.response;

import com.controlcash.app.models.Category;
import com.controlcash.app.models.User;

import java.time.LocalDate;
import java.util.UUID;

public record GoalCompleteResponseDTO(UUID id, LocalDate dueDate, Double value, User user, Category category) {
}
