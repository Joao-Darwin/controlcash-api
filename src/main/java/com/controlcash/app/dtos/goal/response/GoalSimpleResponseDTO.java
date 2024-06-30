package com.controlcash.app.dtos.goal.response;

import java.util.Date;
import java.util.UUID;

public record GoalSimpleResponseDTO(UUID id, Date dueDate, Double value) {
}
