package com.controlcash.app.dtos.permission.request;

import com.controlcash.app.models.User;

import java.util.List;

public record PermissionUpdateRequestDTO(String description, List<User> users) {}
