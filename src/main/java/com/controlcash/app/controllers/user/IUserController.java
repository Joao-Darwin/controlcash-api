package com.controlcash.app.controllers.user;

import com.controlcash.app.dtos.user.request.UserCreateRequestDTO;
import com.controlcash.app.dtos.user.response.UserAllResponseDTO;
import com.controlcash.app.dtos.user.response.UserCreateResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface IUserController {
    ResponseEntity<UserCreateResponseDTO> create(UserCreateRequestDTO userCreateRequestDTO);
    ResponseEntity<Page<UserAllResponseDTO>> findAll(int page, int size, String sort);
}
