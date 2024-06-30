package com.controlcash.app.dtos.user.response;

import com.controlcash.app.models.Goal;
import com.controlcash.app.models.Transaction;

import java.util.List;
import java.util.UUID;

public record UserCompleteResponseDTO(UUID id, String userName, String email, String password, String fullName,
                                      Double salary,
                                      List<String> permissions, List<Goal> goals, List<Transaction> transactions) {
}
