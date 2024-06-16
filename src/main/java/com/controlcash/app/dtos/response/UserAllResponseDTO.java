package com.controlcash.app.dtos.response;

import java.util.UUID;

public record UserAllResponseDTO(UUID id, String userName, String email) {
}
