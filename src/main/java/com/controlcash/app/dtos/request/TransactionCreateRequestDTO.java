package com.controlcash.app.dtos.request;

import com.controlcash.app.models.Category;
import com.controlcash.app.models.enums.TransactionType;

import java.util.List;

public record TransactionCreateRequestDTO(String name, String description, Double value, Integer amountRepeat, TransactionType transactionType, List<Category> categories) {
}
