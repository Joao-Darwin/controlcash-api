package com.controlcash.app.dtos.category.request;

import com.controlcash.app.models.Goal;
import com.controlcash.app.models.Transaction;

import java.util.List;

public record CategoryRequestDTO(String name, List<Goal> goals, List<Transaction> transactions) {
}
