package com.controlcash.app.dtos.transaction.response;

import java.time.LocalDate;
import java.util.UUID;

public record TransactionCreateResponseDTO(UUID id, String name, String description, LocalDate createdDate) {
}
