package com.controlcash.app.dtos.response;

import java.util.Date;
import java.util.UUID;

public record TransactionCreateResponseDTO(UUID id, String name, String description, Date createdDate) {
}
