package com.controlcash.app.controllers.permission.impl;

import com.controlcash.app.controllers.permission.IPermissionController;
import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import com.controlcash.app.dtos.permission.request.PermissionUpdateRequestDTO;
import com.controlcash.app.dtos.permission.response.AllPermissionResponseDTO;
import com.controlcash.app.dtos.permission.response.PermissionResponseDTO;
import com.controlcash.app.exceptions.PermissionNotFoundException;
import com.controlcash.app.exceptions.ResponseEntityException;
import com.controlcash.app.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("${path-api}/permissions")
public class PermissionController implements IPermissionController {
    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<?> create(@RequestBody PermissionCreateRequestDTO permission) {
        try {
            PermissionResponseDTO permissionResponseDTO = permissionService.create(permission);

            return ResponseEntity.status(HttpStatus.OK).body(permissionResponseDTO);
        } catch (IllegalArgumentException argumentException) {
            ResponseEntityException response = new ResponseEntityException(
                    Instant.now(),
                    argumentException.getMessage(),
                    "uri=/api/permissions"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Page<AllPermissionResponseDTO>> findAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "asc") String sort) {

        Sort.Direction direction = sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "description"));

        Page<AllPermissionResponseDTO> allPermissionResponseDTOS = permissionService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(allPermissionResponseDTOS);
    }

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        try {
            PermissionResponseDTO permissionResponseDTO = permissionService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(permissionResponseDTO);
        } catch (PermissionNotFoundException permissionNotFoundException) {
            ResponseEntityException response = new ResponseEntityException(
                    Instant.now(),
                    permissionNotFoundException.getMessage(),
                    "uri=/api/permissions/" + id.toString()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<?> update(
            @RequestBody PermissionUpdateRequestDTO permissionUpdateRequestDTO,
            @PathVariable UUID id) {
        try {
            PermissionResponseDTO response = permissionService.update(permissionUpdateRequestDTO, id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (PermissionNotFoundException | IllegalArgumentException exception) {
            ResponseEntityException response = new ResponseEntityException(
                    Instant.now(),
                    exception.getMessage(),
                    "uri=/api/permissions/" + id.toString()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
