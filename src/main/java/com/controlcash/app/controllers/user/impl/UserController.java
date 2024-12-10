package com.controlcash.app.controllers.user.impl;

import com.controlcash.app.controllers.user.IUserController;
import com.controlcash.app.dtos.user.request.UserCreateRequestDTO;
import com.controlcash.app.dtos.user.response.UserCreateResponseDTO;
import com.controlcash.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${path-api}/users")
public class UserController implements IUserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<UserCreateResponseDTO> create(@RequestBody UserCreateRequestDTO userCreateRequestDTO) {
        UserCreateResponseDTO userCreateResponseDTO = userService.create(userCreateRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(userCreateResponseDTO);
    }
}
