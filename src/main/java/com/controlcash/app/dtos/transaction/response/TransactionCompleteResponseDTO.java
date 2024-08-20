package com.controlcash.app.dtos.transaction.response;

import com.controlcash.app.dtos.category.response.CategoryResponseDTO;
import com.controlcash.app.models.enums.TransactionType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record TransactionCompleteResponseDTO(UUID id, String name, String description, Date createdDate, Double value, Integer amountRepeat, TransactionType transactionType, List<CategoryResponseDTO> categories) {
}
