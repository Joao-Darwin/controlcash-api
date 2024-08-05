package com.controlcash.app.dtos.goal.request;

import com.controlcash.app.models.Category;

import java.util.Date;

public record GoalUpdateRequestDTO (Date dueDate, Double value, Category category) {}
