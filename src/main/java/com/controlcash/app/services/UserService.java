package com.controlcash.app.services;

import com.controlcash.app.dtos.user.request.UserCreateRequestDTO;
import com.controlcash.app.dtos.user.response.UserAllResponseDTO;
import com.controlcash.app.dtos.user.response.UserCompleteResponseDTO;
import com.controlcash.app.dtos.user.response.UserCreateResponseDTO;
import com.controlcash.app.exceptions.UserNotFoundException;
import com.controlcash.app.models.User;
import com.controlcash.app.repositories.UserRepository;
import com.controlcash.app.utils.converters.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserCreateResponseDTO create(UserCreateRequestDTO userCreateRequestDTO) {
        User user = UserConverter.convertUserCreateRequestDTOToUser(userCreateRequestDTO);

        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user = userRepository.save(user);

        return UserConverter.convertUserToUserCreateResponseDTO(user);
    }

    public Page<UserAllResponseDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        return users.map(UserConverter::convertUserToUserAllResponseDTO);
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
        user.setPassword(passwordEncoder.encode(userCreateRequestDTO.password()));
        user.setFullName(userCreateRequestDTO.fullName());
        user.setSalary(userCreateRequestDTO.salary());
    }

    public void delete(UUID id) {
        User user = findUserByIdAndVerifyIfExists(id);

        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (userOptional.isEmpty()) throw new UsernameNotFoundException("Email not found. Email used: '" + username + "'");

        return userOptional.get();
    }
}
