package com.controlcash.app.dtos.goal.request;

import com.controlcash.app.models.Category;
import com.controlcash.app.models.User;

import java.time.LocalDate;

public record GoalCreateRequestDTO (LocalDate dueDate, Double value, User user, Category category) {}
