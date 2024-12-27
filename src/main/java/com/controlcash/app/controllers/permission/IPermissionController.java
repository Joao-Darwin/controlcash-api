package com.controlcash.app.controllers.permission;

import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import com.controlcash.app.dtos.permission.response.PermissionResponseDTO;
import org.springframework.http.ResponseEntity;

public interface IPermissionController {
    ResponseEntity<PermissionResponseDTO> create(PermissionCreateRequestDTO permission);
}
