package com.controlcash.app.controllers.permission;

import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import com.controlcash.app.dtos.permission.response.AllPermissionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IPermissionController {
    ResponseEntity<?> create(PermissionCreateRequestDTO permission);
    ResponseEntity<Page<AllPermissionResponseDTO>> findAll(int page, int size, String sort);
    ResponseEntity<?> findById(UUID id);
}
