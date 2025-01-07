package com.controlcash.app.controllers.auth;

import com.controlcash.app.dtos.auth.request.Credentials;
import com.controlcash.app.dtos.auth.response.AuthResponse;
import com.controlcash.app.dtos.user.request.UserCreateRequestDTO;
import org.springframework.http.ResponseEntity;

public interface IAuthController {
    ResponseEntity<?> signIn(Credentials credentials);
    ResponseEntity<AuthResponse> signUp(UserCreateRequestDTO userToCreate);
}
