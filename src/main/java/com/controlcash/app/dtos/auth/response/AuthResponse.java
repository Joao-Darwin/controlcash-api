package com.controlcash.app.dtos.auth.response;

import com.controlcash.app.models.Permission;

import java.util.List;
import java.util.UUID;

public record AuthResponse(UUID id, String userName, String fullName, String token, boolean isAuthenticated,
                           List<Permission> permissions) {
}
