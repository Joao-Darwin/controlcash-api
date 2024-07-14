package com.controlcash.app.dtos.permission.response;

import com.controlcash.app.dtos.user.response.UserAllResponseDTO;

import java.util.List;
import java.util.UUID;

public record PermissionResponseDTO(UUID id, String description, List<UserAllResponseDTO> userAllResponseDTOS) {
}
