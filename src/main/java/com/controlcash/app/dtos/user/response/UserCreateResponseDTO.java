package com.controlcash.app.dtos.user.response;

import java.util.UUID;

public record UserCreateResponseDTO(UUID id, String userName, String email) {
}
