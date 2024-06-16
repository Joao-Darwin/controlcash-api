package com.controlcash.app.services;

import com.controlcash.app.dtos.request.UserCreateRequestDTO;
import com.controlcash.app.dtos.response.UserAllResponseDTO;
import com.controlcash.app.dtos.response.UserCompleteResponseDTO;
import com.controlcash.app.dtos.response.UserCreateResponseDTO;
import com.controlcash.app.exceptions.UserNotFoundException;
import com.controlcash.app.models.User;
import com.controlcash.app.repositories.UserRepository;
import com.controlcash.app.utils.converters.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<UserAllResponseDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        return users.map(UserConverter::convertUserToUserAllResponseDTO).stream().toList();
    }

    public UserCompleteResponseDTO findById(UUID id) {
        User user = findUserByIdAndVerifyIfExists(id);

       return UserConverter.convertUserToUserCompleteResponseDTO(user);
    }

    private User findUserByIdAndVerifyIfExists(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        boolean userExists = userOptional.isPresent();

        if (!userExists) {
            throw new UserNotFoundException("User not found. Id used: " + id);
        }

        return userOptional.get();
    }

    public UserCreateResponseDTO update(UUID id, UserCreateRequestDTO userCreateRequestDTO) {
        User user = findUserByIdAndVerifyIfExists(id);

        updateUser(user, userCreateRequestDTO);

        user = userRepository.save(user);

        return UserConverter.convertUserToUserCreateResponseDTO(user);
    }

    private void updateUser(User user, UserCreateRequestDTO userCreateRequestDTO) {
        user.setUserName(userCreateRequestDTO.userName());
        user.setEmail(userCreateRequestDTO.email());
        // TODO: Add password encrypt before save on database
        user.setPassword(userCreateRequestDTO.password());
        user.setFullName(userCreateRequestDTO.fullName());
        user.setSalary(userCreateRequestDTO.salary());
    }

    public void delete(UUID id) {
        User user = findUserByIdAndVerifyIfExists(id);

        userRepository.delete(user);
    }
}
