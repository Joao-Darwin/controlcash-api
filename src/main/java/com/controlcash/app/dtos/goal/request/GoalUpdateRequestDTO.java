package com.controlcash.app.dtos.goal.request;

import com.controlcash.app.models.Category;

import java.time.LocalDate;

public record GoalUpdateRequestDTO (LocalDate dueDate, Double value, Category category) {}
