package com.controlcash.app.dtos.transaction.request;

import com.controlcash.app.models.Category;
import com.controlcash.app.models.User;
import com.controlcash.app.models.enums.TransactionType;

import java.util.List;

public record TransactionCreateRequestDTO(String name, String description, Double value, Integer amountRepeat, TransactionType transactionType, User user, List<Category> categories) {
}
