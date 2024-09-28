package com.controlcash.app.dtos.transaction.response;

import com.controlcash.app.dtos.category.response.CategoryResponseDTO;
import com.controlcash.app.models.enums.TransactionType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TransactionCompleteResponseDTO(UUID id, String name, String description, LocalDate createdDate, Double value, Integer amountRepeat, TransactionType transactionType, List<CategoryResponseDTO> categories) {
}
