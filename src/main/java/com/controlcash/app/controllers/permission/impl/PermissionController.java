package com.controlcash.app.controllers.permission.impl;

import com.controlcash.app.controllers.permission.IPermissionController;
import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import com.controlcash.app.dtos.permission.response.PermissionResponseDTO;
import com.controlcash.app.exceptions.ResponseEntityException;
import com.controlcash.app.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

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
}
