package com.controlcash.app.controllers.permission;

import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import org.springframework.http.ResponseEntity;

public interface IPermissionController {
    ResponseEntity<?> create(PermissionCreateRequestDTO permission);
}
