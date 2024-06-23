package com.controlcash.app.dtos.request;

import com.controlcash.app.models.enums.TransactionType;

public record TransactionCreateRequestDTO(String name, String description, Double value, Integer amountRepeat, TransactionType transactionType) {
}
