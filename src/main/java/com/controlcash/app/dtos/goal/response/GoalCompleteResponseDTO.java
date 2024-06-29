package com.controlcash.app.dtos.goal.response;

import com.controlcash.app.models.Category;
import com.controlcash.app.models.User;

import java.util.Date;
import java.util.UUID;

public record GoalCompleteResponseDTO(UUID id, Date dueDate, Double value, User user, Category category) {
}
