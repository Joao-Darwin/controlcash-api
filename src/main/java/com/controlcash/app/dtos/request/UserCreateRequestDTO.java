package com.controlcash.app.dtos.request;

public record UserCreateRequestDTO(String userName, String email, String password, String fullName, Double salary) {
}
