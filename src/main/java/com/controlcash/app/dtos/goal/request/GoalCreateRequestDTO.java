package com.controlcash.app.dtos.goal.request;

import com.controlcash.app.models.Category;
import com.controlcash.app.models.User;

import java.util.Date;

public record GoalCreateRequestDTO (Date dueDate, Double value, User user, Category category) {}
