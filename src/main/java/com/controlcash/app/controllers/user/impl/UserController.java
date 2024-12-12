package com.controlcash.app.controllers.user.impl;

import com.controlcash.app.controllers.user.IUserController;
import com.controlcash.app.dtos.user.request.UserCreateRequestDTO;
import com.controlcash.app.dtos.user.response.UserAllResponseDTO;
import com.controlcash.app.dtos.user.response.UserCompleteResponseDTO;
import com.controlcash.app.dtos.user.response.UserCreateResponseDTO;
import com.controlcash.app.exceptions.ResponseEntityException;
import com.controlcash.app.exceptions.UserNotFoundException;
import com.controlcash.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

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

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Page<UserAllResponseDTO>> findAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "asc") String sort) {

        Sort.Direction direction = sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "username"));

        Page<UserAllResponseDTO> userAllResponseDTOS = userService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(userAllResponseDTOS);
    }

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<?> findById(@PathVariable(name = "id") UUID id) {
        try {
            UserCompleteResponseDTO user = userService.findById(id);

            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (UserNotFoundException userNotFoundException) {
            ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), userNotFoundException.getMessage(), "");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityException);
        }
    }

    @PutMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<?> update(@PathVariable(name = "id") UUID id, @RequestBody UserCreateRequestDTO userUpdated) {
        try {
            UserCreateResponseDTO user = userService.update(id, userUpdated);

            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (UserNotFoundException userNotFoundException) {
            ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), userNotFoundException.getMessage(), "");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityException);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") UUID id) {
        try {
            userService.delete(id);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (UserNotFoundException userNotFoundException) {
            ResponseEntityException responseEntityException = new ResponseEntityException(Instant.now(), userNotFoundException.getMessage(), "");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseEntityException);
        }
    }
}
