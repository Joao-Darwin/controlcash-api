package com.controlcash.app.services;

import com.controlcash.app.dtos.auth.request.Credentials;
import com.controlcash.app.dtos.auth.response.AuthResponse;
import com.controlcash.app.dtos.user.request.UserCreateRequestDTO;
import com.controlcash.app.models.User;
import com.controlcash.app.repositories.UserRepository;
import com.controlcash.app.security.jwt.JwtTokenProvider;
import com.controlcash.app.utils.converters.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse signIn(Credentials credentials) {
        Optional<User> userOptional = userRepository.findByEmail(credentials.email());

        if (userOptional.isEmpty()) throw new IllegalArgumentException(getCredentialsExceptionMessage(credentials));

        User user = userOptional.get();

        if (!passwordEncoder.matches(credentials.password(), user.getPassword())) throw new IllegalArgumentException(getCredentialsExceptionMessage(credentials));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getFullName(),
                token,
                true,
                user.getPermissions()
        );
    }

    private String getCredentialsExceptionMessage(Credentials credentials) {
        return "Invalid credentials. Email used: '" + credentials.email() + "'. Password used: '" + credentials.password() + "'";
    }

    public AuthResponse signUp(UserCreateRequestDTO userToCreate) {
        User user = UserConverter.convertUserCreateRequestDTOToUser(userToCreate);

        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user = userRepository.save(user);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getFullName(),
                token,
                true,
                user.getPermissions()
        );
    }
}
