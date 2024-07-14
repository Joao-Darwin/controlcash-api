package com.controlcash.app.services;

import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import com.controlcash.app.dtos.permission.response.AllPermissionResponseDTO;
import com.controlcash.app.dtos.permission.response.PermissionResponseDTO;
import com.controlcash.app.models.Permission;
import com.controlcash.app.repositories.PermissionRepository;
import com.controlcash.app.utils.converters.PermissionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public PermissionResponseDTO create(PermissionCreateRequestDTO permissionCreateRequestDTO) {
        Permission permission = PermissionConverter.convertPermissionCreateRequestDTOToPermission(permissionCreateRequestDTO);

        permission = permissionRepository.save(permission);

        return PermissionConverter.convertPermissionToPermissionResponseDTO(permission);
    }

    public Page<AllPermissionResponseDTO> findAll(Pageable pageable) {
        Page<Permission> permissionPage = permissionRepository.findAll(pageable);

        return permissionPage.map(PermissionConverter::convertPermissionToAllPermissionResponseDTO);
    }

}
