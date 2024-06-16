package com.controlcash.app.services;

import com.controlcash.app.dtos.request.UserCreateRequestDTO;
import com.controlcash.app.dtos.response.UserCreateResponseDTO;
import com.controlcash.app.models.User;
import com.controlcash.app.repositories.UserRepository;
import com.controlcash.app.utils.converters.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserCreateResponseDTO create(UserCreateRequestDTO userCreateRequestDTO) {
        User user = UserConverter.convertUserCreateRequestDTOToUser(userCreateRequestDTO);

        // TODO: Add password encrypt before save on database
        user = userRepository.save(user);

        return UserConverter.convertUserToUserCreateResponseDTO(user);
    }
}
