package com.controlcash.app.utils.converters;

import com.controlcash.app.dtos.user.request.UserCreateRequestDTO;
import com.controlcash.app.dtos.user.response.UserAllResponseDTO;
import com.controlcash.app.dtos.user.response.UserCompleteResponseDTO;
import com.controlcash.app.dtos.user.response.UserCreateResponseDTO;
import com.controlcash.app.models.User;

public class UserConverter {
    public static User convertUserCreateRequestDTOToUser(UserCreateRequestDTO userCreateRequestDTO) {
        User user = new User();

        user.setUserName(userCreateRequestDTO.userName());
        user.setEmail(userCreateRequestDTO.email());
        user.setPassword(userCreateRequestDTO.password());
        user.setFullName(userCreateRequestDTO.fullName());
        user.setSalary(userCreateRequestDTO.salary());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        return user;
    }

    public static UserCreateResponseDTO convertUserToUserCreateResponseDTO(User user) {
        return new UserCreateResponseDTO(user.getUsername(), user.getEmail());
    }

    public static UserAllResponseDTO convertUserToUserAllResponseDTO(User user) {
        return new UserAllResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public static UserCompleteResponseDTO convertUserToUserCompleteResponseDTO(User user) {
        return new UserCompleteResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getFullName(), user.getSalary(), user.getRoles(), user.getGoals(), user.getTransactions());
    }
}
