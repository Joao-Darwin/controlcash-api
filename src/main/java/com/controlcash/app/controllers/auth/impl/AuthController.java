package com.controlcash.app.controllers.auth.impl;

import com.controlcash.app.controllers.auth.IAuthController;
import com.controlcash.app.dtos.auth.request.Credentials;
import com.controlcash.app.dtos.auth.response.AuthResponse;
import com.controlcash.app.dtos.user.request.UserCreateRequestDTO;
import com.controlcash.app.exceptions.ResponseEntityException;
import com.controlcash.app.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
public class AuthController implements IAuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody Credentials credentials) {
        try {
            AuthResponse authResponse = authService.signIn(credentials);
            return ResponseEntity.status(HttpStatus.OK).body(authResponse);
        } catch (IllegalArgumentException illegalArgumentException) {
            ResponseEntityException responseEntityException = new ResponseEntityException(
                            Instant.now(),
                            illegalArgumentException.getMessage(),
                            "/auth/signIn");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseEntityException);
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<AuthResponse> signUp(@RequestBody UserCreateRequestDTO userToCreate) {
        AuthResponse authResponse = authService.signUp(userToCreate);
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }
}
